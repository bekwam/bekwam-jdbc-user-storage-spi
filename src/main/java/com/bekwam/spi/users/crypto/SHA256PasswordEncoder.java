package com.bekwam.spi.users.crypto;

/**
 * SHA256 implementation of encoded passwords
 *
 * @since 1.0
 * @author carl
 */
public class SHA256PasswordEncoder implements PasswordEncoder {
  @Override
  public String encode(String password) {
    // Existing SHA-256 hashing logic
    return Hash.from(password);
  }
}
