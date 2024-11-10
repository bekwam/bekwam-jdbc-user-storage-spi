package com.bekwam.spi.users.config;

/**
 * Defines supported DBs and contains JDBC driver class name
 *
 * @since 1.2
 * @author carl
 */
public enum DBVendorType {

    Oracle("oracle.jdbc.driver.OracleDriver"),

    Postgres("org.postgresql.Driver");

    private final String driverClass;

    DBVendorType(String driverClass) {
        this.driverClass = driverClass;
    }

    public String driverClass() { return driverClass; }
}
