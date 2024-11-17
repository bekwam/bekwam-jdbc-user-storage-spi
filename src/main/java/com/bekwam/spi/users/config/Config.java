package com.bekwam.spi.users.config;

import com.bekwam.spi.users.crypto.BinaryEncoderType;
import com.google.common.base.Preconditions;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;

import java.util.Objects;

/**
 * Transport object for JDBCUserStorageProvider config
 *
 * @author carl
 * @since 1.0
 */
public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class);

    private final String connectionURL;

    private final String username;

    private final String password;

    private final String usersSQL;

    private final String rolesSQL;

    private final String minSize;

    private final String maxSize;

    private final boolean metricsEnabled;

    private final String usernameCase;

    private final String validationTimeout;

    private final String dbVendor;

    private final String allUsersSQL;

    private final String searchUsersSQL;

    private final String validationSQL;

    private final BinaryEncoderType binaryEncoder;

    public Config(String connectionURL,
                  String username,
                  String password,
                  String usersSQL,
                  String rolesSQL,
                  String minSize,
                  String maxSize,
                  boolean metricsEnabled,
                  String usernameCase,
                  String validationTimeout,
                  String dbVendor,
                  String allUsersSQL,
                  String searchUsersSQL,
                  String validationSQL,
                  String binaryEncoder_s) {
        this.connectionURL = connectionURL;
        this.username = username;
        this.password = password;
        this.usersSQL = usersSQL;
        this.rolesSQL = rolesSQL;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.metricsEnabled = metricsEnabled;
        this.usernameCase = usernameCase;
        this.validationTimeout = validationTimeout;
        this.dbVendor = dbVendor;
        this.allUsersSQL = allUsersSQL;
        this.searchUsersSQL = searchUsersSQL;
        this.validationSQL = validationSQL;

        var enc = BinaryEncoderType.BASE64; // handle legacy data
        try {
            enc = Enum.valueOf(BinaryEncoderType.class, binaryEncoder_s);
        } catch (NullPointerException | IllegalArgumentException exc) {
            LOGGER.warn("received bad binaryEncoder; defaulting to " + enc.name());
        }
        this.binaryEncoder = enc;
    }

    public static Config from(ComponentModel config) {
        return new Config(
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_CONNECTION_URL),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_USERNAME),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_PASSWORD),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_USER_QUERY),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_ROLES_QUERY),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_MIN_SIZE),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_MAX_SIZE),
                Boolean.valueOf(config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_METRICS_ENABLED)),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_USERNAME_CASE),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_VALIDATION_TIMEOUT),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_DB_VENDOR),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_ALL_USERS_QUERY),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_SEARCH_USERS_QUERY),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_VALIDATION_QUERY),
                config.getConfig().getFirst(Constants.PROVIDER_PROPERTY_BINARY_ENCODER)
        );
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsersSQL() {
        return usersSQL;
    }

    public String getRolesSQL() {
        return rolesSQL;
    }

    public int getMinSizeAsInt() {
        return Integer.parseInt(Preconditions.checkNotNull(minSize));
    }

    public String getMinSize() {
        return minSize;
    }


    public int getMaxSizeAsInt() {
        return Integer.parseInt(Preconditions.checkNotNull(maxSize));
    }

    public String getMaxSize() {
        return maxSize;
    }

    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    public String getUsernameCase() {
        return usernameCase;
    }

    public Long getValidationTimeoutAsLong() {
        return Long.parseLong(Preconditions.checkNotNull(validationTimeout));
    }

    public String getValidationTimeout() {
        return validationTimeout;
    }

    public String getDbVendor() {
        return dbVendor;
    }

    public String getAllUsersSQL() {
        return allUsersSQL;
    }

    public String getSearchUsersSQL() {
        return searchUsersSQL;
    }

    public String getValidationSQL() {
        return validationSQL;
    }

    public BinaryEncoderType getBinaryEncoder() {
        return binaryEncoder;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return metricsEnabled == config.metricsEnabled && Objects.equals(connectionURL, config.connectionURL) && Objects.equals(username, config.username) && Objects.equals(password, config.password) && Objects.equals(usersSQL, config.usersSQL) && Objects.equals(rolesSQL, config.rolesSQL) && Objects.equals(minSize, config.minSize) && Objects.equals(maxSize, config.maxSize) && Objects.equals(usernameCase, config.usernameCase) && Objects.equals(validationTimeout, config.validationTimeout) && Objects.equals(dbVendor, config.dbVendor) && Objects.equals(allUsersSQL, config.allUsersSQL) && Objects.equals(searchUsersSQL, config.searchUsersSQL) && Objects.equals(validationSQL, config.validationSQL) && binaryEncoder == config.binaryEncoder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionURL, username, password, usersSQL, rolesSQL, minSize, maxSize, metricsEnabled, usernameCase, validationTimeout, dbVendor, allUsersSQL, searchUsersSQL, validationSQL, binaryEncoder);
    }

    @Override
    public String toString() {
        return "Config{" +
                "connectionURL='" + connectionURL + '\'' +
                ", username='" + username + '\'' +
                ", usersSQL='" + usersSQL + '\'' +
                ", rolesSQL='" + rolesSQL + '\'' +
                ", minSize='" + minSize + '\'' +
                ", maxSize='" + maxSize + '\'' +
                ", metricsEnabled=" + metricsEnabled +
                ", usernameCase='" + usernameCase + '\'' +
                ", validationTimeout='" + validationTimeout + '\'' +
                ", dbVendor='" + dbVendor + '\'' +
                ", allUsersSQL='" + allUsersSQL + '\'' +
                ", searchUsersSQL='" + searchUsersSQL + '\'' +
                ", validationSQL='" + validationSQL + '\'' +
                ", binaryEncoder=" + binaryEncoder +
                '}';
    }
}
