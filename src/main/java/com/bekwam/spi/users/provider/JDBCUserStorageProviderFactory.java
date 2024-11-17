package com.bekwam.spi.users.provider;

import com.bekwam.spi.users.config.Config;
import com.bekwam.spi.users.config.ConfigMetadataFactory;
import com.bekwam.spi.users.config.DBVendorType;
import com.bekwam.spi.users.data.UserDAOImpl;
import com.bekwam.spi.users.metadata.PropertiesServerInfoDelegate;
import com.google.common.base.Preconditions;
import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.security.NamePrincipal;
import io.agroal.api.security.SimplePassword;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ServerInfoAwareProviderFactory;
import org.keycloak.storage.UserStorageProviderFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.bekwam.spi.users.config.Constants.PROVIDER_NAME;

/**
 * Factory for Keycloak JDBC User Storage SPI
 *
 * @since 1.0
 * @author carl
 */
@ThreadSafe
public class JDBCUserStorageProviderFactory
        implements UserStorageProviderFactory<JDBCUserStorageProvider>,
            ServerInfoAwareProviderFactory {

    private static final Logger LOGGER = Logger.getLogger(JDBCUserStorageProviderFactory.class);

    protected static final List<ProviderConfigProperty> configMetadata
            = new ConfigMetadataFactory().create();

    private final Map<String, Object> componentLocks = new ConcurrentHashMap<>();

    private final Map<String, Config> lastConfigs = new HashMap<>();
    private final Map<String, AgroalDataSource> dataSources = new HashMap<>();

    @Override
    public JDBCUserStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        LOGGER.trace("create");
        LOGGER.trace("JDBCUserStorageProvider.create() for id=" + componentModel.getId() + " and name=" + componentModel.getName());

        Config config = Config.from(componentModel);

        try {
            var lastConfig = getLastConfig(componentModel.getId());
            if( lastConfig == null || !lastConfig.equals(config) ) { // init case or config change
                initFactory(componentModel.getId(), config); // thread safe
            }

            var ds = getDataSource(componentModel.getId());

            Preconditions.checkNotNull(ds);

            if( ds.isHealthy(false) ) {
                return new JDBCUserStorageProvider(
                        keycloakSession,
                        componentModel,
                        ds,
                        config.getUsersSQL(),
                        config.getRolesSQL(),
                        new UserDAOImpl(
                                config.getAllUsersSQL(),
                                config.getSearchUsersSQL()
                        ),
                        config.getBinaryEncoder()
                );
            } else {
                LOGGER.warn("ds not healthy (is it initializing?)");
            }
        } catch(SQLException exc) {
            LOGGER.error("cannot create datasource to " + config.getConnectionURL(), exc);
        }
        return null;
    }

    @Override
    public String getId() {
        LOGGER.trace("getId");
        return PROVIDER_NAME;
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        LOGGER.trace("getOperationalInfo");
        return new PropertiesServerInfoDelegate().getProperties();
    }

    protected Object getComponentLock(String componentId) {
        LOGGER.trace("getComponentLock() for componentId=" + componentId);
        synchronized (componentLocks) {
            if (!componentLocks.containsKey(componentId)) {
                componentLocks.put(componentId, new Object());
            }
            LOGGER.trace("exiting getComponentLock()");
            return componentLocks.get(componentId);
        }
    }

    @Override
    public void close() {
        // heavy-duty entire object synchronized call but only needed at shutdown
        LOGGER.trace("JDBCUserStorageProviderFactory.close()");
        dataSources.values().stream().filter( ds -> ds != null ).forEach( AgroalDataSource::close );
        dataSources.clear();
        lastConfigs.clear();
        componentLocks.clear();
    }

    public Config getLastConfig(String componentId) {
        return getLastConfigs().get(componentId);
    }

    public Map<String, Config> getLastConfigs() {
        return lastConfigs;
    }

    public void initFactory(String componentId, Config config) throws SQLException {

        LOGGER.trace("initFactory() for componentId=" + componentId);

        var componentLock = getComponentLock(componentId);

        LOGGER.trace("after getting componentLock");

        /**
         * componentLock is needed because locking this whole object will
         * interfere with components that are currently running and not needing
         * a config adjustment
         *
         * Ex, saving someone's admin panel will kill the token requests for
         * someone else
         */
        synchronized (componentLock) {
            var lastConfig = getLastConfig(componentId);

            if (lastConfig == null || !lastConfig.equals(config)) {
                AgroalDataSourceConfigurationSupplier configurationSupplier =
                        new AgroalDataSourceConfigurationSupplier()
                                .metricsEnabled(config.isMetricsEnabled())
                                .connectionPoolConfiguration(cp -> cp
                                        .connectionFactoryConfiguration(cf -> cf
                                                .jdbcUrl(config.getConnectionURL())
                                                .principal(new NamePrincipal(config.getUsername()))
                                                .credential(new SimplePassword(config.getPassword()))
                                                .recoveryPrincipal(new NamePrincipal(config.getUsername()))
                                                .recoveryCredential(new SimplePassword(config.getPassword()))
                                                .connectionProviderClassName(
                                                        Enum
                                                                .valueOf(DBVendorType.class, config.getDbVendor())
                                                                .driverClass()
                                                )
                                                .initialSql(config.getValidationSQL())
                                        )
                                        .minSize(config.getMinSizeAsInt())
                                        .initialSize(config.getMinSizeAsInt())
                                        .maxSize(config.getMaxSizeAsInt())
                                        .validationTimeout(Duration.ofSeconds(config.getValidationTimeoutAsLong()))
                                        .connectionValidator(connection -> {
                                            var SQL = config.getValidationSQL();
                                            if (SQL != null && !SQL.isEmpty()) {
                                                try (
                                                        Statement stmt = connection.createStatement()
                                                ) {
                                                    LOGGER.trace("running validation query=" + SQL);
                                                    return stmt.executeQuery(SQL).next();
                                                } catch (SQLException exc) {
                                                    LOGGER.error("unable to call validation query " + SQL, exc);
                                                }
                                            }
                                            return false;
                                        })
                                );

                String reason = (lastConfig == null) ? "init" : "changed config";

                LOGGER.trace("generating new datasource from " + reason);

                var ds = this.getDataSources().get(componentId);

                if (ds != null) {
                    LOGGER.trace("closing old datasource");
                    ds.close();
                }

                dataSources.put(componentId, AgroalDataSource.from(configurationSupplier));
                lastConfigs.put(componentId, config);
            }
        }
    }

    public AgroalDataSource getDataSource(String componentId) {
        return getDataSources().get(componentId);
    }


    public Map<String, AgroalDataSource> getDataSources() {
        return dataSources;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }
}

