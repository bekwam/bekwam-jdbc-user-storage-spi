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
        assertEquals(
                "bKE9UspwyIPg8LsQHkJaiehiTeUdstI5JZOvaoQRgJA=",
                new SHA256PasswordEncoder().encode("abc123")
        );
    }

    @Test
    public void bad() {
        assertNull(
                new SHA256PasswordEncoder().encode(null)
        );
        assertNull(
                new SHA256PasswordEncoder().encode("")
        );
    }
}
