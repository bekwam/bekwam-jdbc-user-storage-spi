package com.bekwam.spi.users.config;

import com.bekwam.spi.users.crypto.BinaryEncoderType;

/**
 * List of shared constants
 *
 * @author carl
 * @since 1.0
 */
public class Constants {

    public final static String PROVIDER_PROPERTY_CONNECTION_URL = "connection_url";
    public final static String PROVIDER_PROPERTY_USERNAME       = "username";
    public final static String PROVIDER_PROPERTY_PASSWORD       = "password";
    public final static String PROVIDER_PROPERTY_USER_QUERY     = "user_query";
    public final static String PROVIDER_PROPERTY_ROLES_QUERY    = "roles_query";
    public final static String PROVIDER_PROPERTY_MIN_SIZE    = "min_size";
    public final static String PROVIDER_PROPERTY_MAX_SIZE    = "max_size";
    public final static String PROVIDER_PROPERTY_METRICS_ENABLED    = "metrics_enabled";
    public final static String PROVIDER_PROPERTY_USERNAME_CASE = "username_case";
    public final static String PROVIDER_PROPERTY_VALIDATION_TIMEOUT = "validation_timeout";
    public final static String PROVIDER_PROPERTY_DB_VENDOR = "db_vendor";
    public final static String PROVIDER_PROPERTY_ALL_USERS_QUERY = "select_all_users_query";
    public final static String PROVIDER_PROPERTY_SEARCH_USERS_QUERY = "select_users_query";
    public final static String PROVIDER_PROPERTY_BINARY_ENCODER = "binary_encoder";

    public final static String PROVIDER_PROPERTY_VALIDATION_QUERY = "validation_query";

    public final static String DEFAULT_VALUE_DB_URL = "jdbc:postgresql://localhost:5433/userdb";

    public final static String DEFAULT_VALUE_ALL_USERS_QUERY =
            "SELECT username, password, name, email FROM user ORDER BY username";

    public final static String DEFAULT_VALUE_SEARCH_USERS_QUERY =
            "SELECT username, password, name, email FROM user WHERE username LIKE ? OR name LIKE ? OR email LIKE ? ORDER BY username";

    public final static String DEFAULT_VALUE_VALIDATION_QUERY = "SELECT 1 FROM DUAL";

    public final static String DEFAULT_VALUE_PASSWORD_QUERY = "SELECT password FROM user WHERE username = ?";

    public final static String DEFAULT_VALUE_ROLES_QUERY = "SELECT role_name FROM role WHERE username = ?";

    public final static String PROVIDER_NAME = "Bekwam JDBC";

    public final static BinaryEncoderType DEFAULT_BINARY_ENCODER = BinaryEncoderType.BASE64;
}
