package com.bekwam.spi.users.provider;

import com.bekwam.spi.users.metadata.PropertiesServerInfoDelegate;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ServerInfoAwareProviderFactory;
import org.keycloak.storage.UserStorageProviderFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;

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

    private static final String PROVIDER_ID = "Bekwam JDBC"; // displays in Admin UI

    @Override
    public JDBCUserStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        LOGGER.trace("create");
        return new JDBCUserStorageProvider();
    }

    @Override
    public String getId() {
        LOGGER.trace("getId");
        return PROVIDER_ID;
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        LOGGER.trace("getOperationalInfo");
        return new PropertiesServerInfoDelegate().getProperties();
    }
}

