package com.bekwam.spi.users.data;

import org.jboss.logging.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Implements user-provided SQL DAO
 *
 * @since 1.0
 * @author carl
 */
public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);

    private final String allUsersSQL;
    private final String searchUsersSQL;

    public UserDAOImpl(
            String allUsersSQL,
            String searchUsersSQL
    ) {
        this.allUsersSQL = allUsersSQL;
        this.searchUsersSQL = searchUsersSQL;
    }

    @Override
    public Optional<User> findUserByUsername(DataSource ds, String sql, String username) {
        LOGGER.trace("findUserByUsrCode method has been called with sql= " + sql + "; username=" + username);
        try (
                Connection c = ds.getConnection();
                PreparedStatement preparedStatement = c.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if( rs.next() ) {
                // query must have SELECT username, password_h
                return Optional.of(new User(rs.getString(1), rs.getString(2), null, null));
            }
        } catch(SQLException exc) {
            LOGGER.error("error running users query '" + sql + "' for username=" + username, exc);
        }
        return Optional.empty();
    }

    public Set<Role> findRolesByUsername(DataSource ds, String sql, String username) {
        LOGGER.trace("findRolesByUsername method has been called with sql: " + sql + " and username: " + username);
        Set<Role> roles = new HashSet<>();
        try (
                Connection c = ds.getConnection();
                PreparedStatement preparedStatement = c.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, username);
            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                roles
                        .add(
                                new Role(rs.getString(1))
                        );
            }
        } catch (SQLException exc) {
            LOGGER.error("error running roles query '" + sql + "' for username=" + username, exc);
        }
        return roles;
    }

    @Override
    public List<User> findUsers(DataSource ds, int first, int max, String criteria) {
        LOGGER.trace("findUsers method has been called with first: " + first + ", max: " + max + ", and criteria: " + criteria);
        var users = new ArrayList<User>();
        if( criteria == null || criteria.isEmpty() ) {
            try (
                    var c = ds.getConnection();
                    var stmt = c.createStatement()
            ) {
                var rs = stmt.executeQuery(allUsersSQL);
                unpackResults(rs, first, max, users);
            } catch (SQLException exc) {
                LOGGER.error("error running all users query '" + allUsersSQL, exc);
            }
        } else {
            try (
                    Connection c = ds.getConnection();
                    PreparedStatement preparedStatement = c.prepareStatement(searchUsersSQL)
            ) {
                preparedStatement.setString(1, criteria);
                preparedStatement.setString(2, criteria);
                preparedStatement.setString(3, criteria);

                var rs = preparedStatement.executeQuery();
                unpackResults(rs, first, max, users);
            } catch (SQLException exc) {
                LOGGER.error("error running search users query '" + searchUsersSQL, exc);
            }
        }

        LOGGER.trace("users: " + users);
        return users;
    }

    void unpackResults(ResultSet rs, int first, int max, List<User> users) throws SQLException {
        int pos = 0;
        while (rs.next() && pos < (first + max)) {
            if (pos >= first) {
                users
                        .add(
                                new User(
                                        rs.getString(1),
                                        rs.getString(2),
                                        rs.getString(3),
                                        rs.getString(4)
                                )
                        );
            }
            pos++;
        }
    }
}
