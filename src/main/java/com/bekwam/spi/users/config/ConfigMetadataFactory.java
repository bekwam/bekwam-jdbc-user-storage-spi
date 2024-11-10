package com.bekwam.spi.users.config;

import org.jboss.logging.Logger;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.bekwam.spi.users.config.Constants.*;

/**
 * Creates block of Config items displayed in Keycloak UI
 *
 * @author carl
 * @since 1.0
 */
public class ConfigMetadataFactory {

    private static final Logger LOGGER = Logger.getLogger(ConfigMetadataFactory.class);

    public List<ProviderConfigProperty> create() {

        LOGGER.trace("create()");

        ProviderConfigurationBuilder builder = ProviderConfigurationBuilder
                .create();

        builder
                .property().name(Constants.PROVIDER_PROPERTY_CONNECTION_URL)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("DB Connection URL")
                .defaultValue(DEFAULT_VALUE_DB_URL)
                .helpText("JDBC-formatted connection URL to DB")
                .add();

        builder
                .property().name(Constants.PROVIDER_PROPERTY_USERNAME)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Username")
                .defaultValue("username")
                .helpText("Database-defined username for DB connection")
                .add();

        builder
                .property().name(Constants.PROVIDER_PROPERTY_PASSWORD)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Password")
                .defaultValue("password")
                .helpText("Credential for user connection to DB")
                .secret(true)
                .add();

        builder
                .property().name(Constants.PROVIDER_PROPERTY_MIN_SIZE)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Min Pool Size")
                .defaultValue("0")
                .helpText("Minimum # of DB connections in pool")
                .add();

        builder
                .property().name(Constants.PROVIDER_PROPERTY_MAX_SIZE)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Max Pool Size")
                .defaultValue("10")
                .helpText("Maximum # of DB connections in pool")
                .add();

        builder
                .property().name(Constants.PROVIDER_PROPERTY_FLUSH_ON_CLOSE)
                .type(ProviderConfigProperty.BOOLEAN_TYPE)
                .label("Flush On Close")
                .defaultValue(Boolean.FALSE)
                .helpText("Flush connections on return to pool (TRUE is discouraged because a big performance penalty was discovered)")
                .add();

        builder
                .property().name(Constants.PROVIDER_PROPERTY_METRICS_ENABLED)
                .type(ProviderConfigProperty.BOOLEAN_TYPE)
                .label("Enable Metrics")
                .defaultValue(Boolean.FALSE)
                .helpText("Collect metrics")
                .add();

        builder
                .property().name(Constants.PROVIDER_PROPERTY_VALIDATION_TIMEOUT)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Validation Timeout")
                .defaultValue("0")
                .helpText("Interval between background checks in seconds; zero is no checks applied")
                .add();


        builder
                .property().name(Constants.PROVIDER_PROPERTY_DB_VENDOR)
                .type(ProviderConfigProperty.LIST_TYPE)
                .label("DB Vendor")
                .options(
                        Arrays
                                .stream(DBVendorType.values())
                                .map(DBVendorType::name)
                                .collect(Collectors.toList())
                )
                .defaultValue(DBVendorType.Oracle.name())
                .helpText("Database Vendor")
                .add();

        builder
                .property().name(Constants.PROVIDER_PROPERTY_USER_QUERY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("User Query")
                .defaultValue(DEFAULT_VALUE_PASSWORD_QUERY)
                .helpText("SQL select statement that returns a hashed password when given an end user's username")
                .add();

        builder
                .property().name(Constants.PROVIDER_PROPERTY_ROLES_QUERY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Roles Query")
                .defaultValue(DEFAULT_VALUE_ROLES_QUERY)
                .helpText("SQL select statement that returns a list of roles given an end user's username")
                .add();

        builder
                .property().name(PROVIDER_PROPERTY_ALL_USERS_QUERY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Select All Users Query")
                .defaultValue(DEFAULT_VALUE_ALL_USERS_QUERY)
                .helpText("SQL select statement that returns all users")
                .add();

        builder
                .property().name(PROVIDER_PROPERTY_SEARCH_USERS_QUERY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Search Users Query")
                .defaultValue(DEFAULT_VALUE_SEARCH_USERS_QUERY)
                .helpText("SQL select statement that filters users")
                .add();

        builder
                .property().name(PROVIDER_PROPERTY_VALIDATION_QUERY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Validation Query")
                .defaultValue(DEFAULT_VALUE_VALIDATION_QUERY)
                .helpText("SQL select statement called when validating a connection")
                .add();

        return builder.build();
    }
}
