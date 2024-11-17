package com.bekwam.spi.users.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for SHA256PasswordEncoderTest
 *
 * @since 1.0
 * @author carl
 */
public class SHA256PasswordEncoderTest {

    @Test
    public void ok() {
        //
        // cross-check in linux with
        // $ echo -n 'abc123' | openssl dgst -sha256 -binary | base64
        //
        assertEquals(
                "bKE9UspwyIPg8LsQHkJaiehiTeUdstI5JZOvaoQRgJA=",
                new SHA256PasswordEncoder().encodeBase64("abc123")
        );
    }

    @Test
    public void bad() {
        assertNull(
                new SHA256PasswordEncoder().encodeBase64(null)
        );
        assertNull(
                new SHA256PasswordEncoder().encodeBase64("")
        );
    }
}
