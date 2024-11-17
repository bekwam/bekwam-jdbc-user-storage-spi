package com.bekwam.spi.users.crypto;

/**
 * An interface for different cryptographic hashes used for encoding credentials
 * in the database
 *
 * @author carl
 * @since 1.0
 */
public interface PasswordEncoder {
    String encode(String password);
}

