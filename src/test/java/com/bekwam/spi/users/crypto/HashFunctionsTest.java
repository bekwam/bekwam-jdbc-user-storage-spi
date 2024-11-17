package com.bekwam.spi.users.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Hash class
 *
 * @since 1.1
 * @author carl
 */
public class HashFunctionsTest {

/*    @Test
    public void ok() {


        assertEquals(
                "bKE9UspwyIPg8LsQHkJaiehiTeUdstI5JZOvaoQRgJA=",
                HashFunctions.from("abc123")
        );

        assertEquals(
                "j2GtXPoMRxyMv4EOooXLHl+cLF5eXk9YoyKWZ3A+FYc=",
                HashFunctions.from("def456")
        );
    }
*/
    @Test
    public void bad() {
        assertThrows(IllegalArgumentException.class, () -> HashFunctions.sha256(null));
        assertThrows(IllegalArgumentException.class, () -> HashFunctions.sha256(""));
    }
}
