package com.bekwam.spi.users.crypto;

import org.jboss.logging.Logger;

/**
 * SHA256 implementation of encoded passwords
 *
 * @author carl
 * @since 1.0
 */
public class SHA256PasswordEncoder implements PasswordEncoder {
    private static final Logger LOGGER = Logger.getLogger(SHA256PasswordEncoder.class);
    @Override
    public String encodeBase64(String password) {
        if( password == null || password.isEmpty() ) {
            LOGGER.warn("password is null or empty");
            return null;
        }
        return BinaryEncoders.base64(HashFunctions.sha256(password));
    }
    @Override
    public String encodeHex(String password) {
        if( password == null || password.isEmpty() ) {
            LOGGER.warn("password is null or empty");
            return null;
        }
        return BinaryEncoders.hex(HashFunctions.sha256(password));
    }
}
