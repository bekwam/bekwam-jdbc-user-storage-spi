package com.bekwam.spi.users.crypto;

import org.apache.commons.codec.binary.Hex;

import java.util.Base64;

/**
 * Functions for encoding binary data
 *
 * @since 1.1
 * @author carl
 */
public class BinaryEncoders {
    public static String base64(byte[] bytes) {
        if( bytes == null || bytes.length == 0 ) {
            throw new IllegalArgumentException("base64 bytes cannot be null or empty");
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static String hex(byte[] bytes) {
        if( bytes == null || bytes.length == 0 ) {
            throw new IllegalArgumentException("hex bytes cannot be null or empty");
        }
        return Hex.encodeHexString(bytes);
    }
}
