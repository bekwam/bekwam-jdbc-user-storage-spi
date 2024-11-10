package com.bekwam.spi.users.crypto;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Base64;

/**
 * Applies SHA256 / Base64 to password for comparision with data-at-rest
 *
 * @author carl
 * @since 1.0
 */
public class Hash {
    public static String from(String clearText) {
        return Base64.getEncoder().encodeToString(DigestUtils.sha256(clearText.getBytes()));
    }
}
