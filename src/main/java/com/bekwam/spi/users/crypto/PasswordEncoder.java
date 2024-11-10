package com.bekwam.spi.users.crypto;

/**
 * An interface for different cryptographic hashes used for encoding credentials
 * in the database
 *
 * @since 1.0
 * @author carl
 */
public interface PasswordEncoder {
  String encode(String password);
}

