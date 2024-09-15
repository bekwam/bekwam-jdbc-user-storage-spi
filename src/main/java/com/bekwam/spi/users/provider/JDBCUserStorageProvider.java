package com.bekwam.spi.users.provider;

import org.jboss.logging.Logger;
import org.keycloak.storage.UserStorageProvider;

/**
 * Keycloak JDBC User Storage SPI
 *
 * @since 1.0
 * @author carl
 */
public class JDBCUserStorageProvider implements UserStorageProvider {

    private static final Logger LOGGER = Logger.getLogger(JDBCUserStorageProvider.class);

    @Override
    public void close() {
        LOGGER.trace("close");
    }
}
