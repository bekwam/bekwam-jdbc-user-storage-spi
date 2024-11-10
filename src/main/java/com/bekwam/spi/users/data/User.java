package com.bekwam.spi.users.data;

/**
 * User entity filled in by provided SQL
 *
 * @param username
 * @param password
 * @param name
 * @param email
 *
 */
public record User(String username, String password, String name, String email) {
    // overridden so hashed password not exposed in logs by accident
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password not null?='" + (password!=null) + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
