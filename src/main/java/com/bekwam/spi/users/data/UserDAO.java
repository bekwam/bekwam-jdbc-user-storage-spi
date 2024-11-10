package com.bekwam.spi.users.data;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * DAO based on user-provided SQL
 *
 * @since 1.0
 * @author carl
 */
public interface UserDAO {

    Optional<User> findUserByUsername(DataSource ds, String sql, String username);

    Set<Role> findRolesByUsername(DataSource ds, String sql, String usrCode);

    List<User> findUsers(DataSource ds, int first, int max, String criteria);

}
