package com.bekwam.spi.users.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for binary encoders
 *
 * @since 1.1
 * @author carl
 */
public class BinaryEncodersTest {

    // SOME_BYTES is sha256("abc123")
    private final static byte[] SOME_BYTES = new byte[] { 108, -95, 61, 82, -54, 112, -56, -125, -32, -16, -69, 16, 30, 66, 90, -119, -24, 98, 77, -27, 29, -78, -46, 57, 37, -109, -81, 106, -124, 17, -128, -112};

    @Test
    public void base64() {
        assertEquals(
                "bKE9UspwyIPg8LsQHkJaiehiTeUdstI5JZOvaoQRgJA=",
                BinaryEncoders.base64(SOME_BYTES)
        );
    }

    @Test
    public void hex() {
        assertEquals(
                "6ca13d52ca70c883e0f0bb101e425a89e8624de51db2d2392593af6a84118090",
                BinaryEncoders.hex(SOME_BYTES)
        );
    }

    @Test
    public void bad() {
        assertThrows(IllegalArgumentException.class, () -> BinaryEncoders.base64(null));
        assertThrows(IllegalArgumentException.class, () -> BinaryEncoders.base64(new byte[0]));
    }
}
