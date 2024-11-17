package com.bekwam.spi.users.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for Hash class
 *
 * @since 1.1
 * @author carl
 */
public class HashTest {

    @Test
    public void ok() {

        //
        // cross-check in linux with
        // $ echo -n 'abc123' | openssl dgst -sha256 -binary | base64
        //

        assertEquals(
                "bKE9UspwyIPg8LsQHkJaiehiTeUdstI5JZOvaoQRgJA=",
                Hash.from("abc123")
        );

        assertEquals(
                "j2GtXPoMRxyMv4EOooXLHl+cLF5eXk9YoyKWZ3A+FYc=",
                Hash.from("def456")
        );
    }

    @Test
    public void bad() {
        assertNull(Hash.from(null));
        assertNull(Hash.from(""));
    }
}
