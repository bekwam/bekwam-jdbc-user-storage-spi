package com.bekwam.spi.users.provider;

import com.bekwam.spi.users.crypto.SHA256PasswordEncoder;
import com.bekwam.spi.users.data.Role;
import com.bekwam.spi.users.data.User;
import com.bekwam.spi.users.data.UserAndRoles;
import com.bekwam.spi.users.data.UserDAO;
import io.agroal.api.AgroalDataSource;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Keycloak JDBC User Storage SPI
 *
 * @since 1.0
 * @author carl
 */
public class JDBCUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        CredentialInputValidator,
        CredentialInputUpdater,
        UserQueryProvider {

    private static final Logger LOGGER = Logger.getLogger(JDBCUserStorageProvider.class);

    private KeycloakSession session;
    private ComponentModel model;
    private AgroalDataSource ds;
    private String usersSQL;
    private String rolesSQL;
    private UserDAO userDAO;

    public JDBCUserStorageProvider(
            KeycloakSession session,
            ComponentModel model,
            AgroalDataSource ds,
            String userSQL,
            String rolesSQL,
            UserDAO userDAO) {

        LOGGER.trace("JDBCUserStorageProvider constructor");

        this.session = session;
        this.model = model;
        this.ds = ds;
        this.usersSQL = userSQL;
        this.rolesSQL = rolesSQL;
        this.userDAO = userDAO;

        if( ds != null ) {
            LOGGER.trace( ds.getMetrics());
        }
    }

    @Override
    public void close() {
        LOGGER.trace("close");
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(PasswordCredentialModel.TYPE);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType) {
        return credentialType.equals(PasswordCredentialModel.TYPE);
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        LOGGER.trace("isValid method has been called with username: " + userModel.getUsername());
        if (credentialInput.getType().equals("password")) {
            var u = userDAO.findUserByUsername(ds, usersSQL, userModel.getUsername());
            if (u.isPresent()) {
                var user = u.get();
                var storedPassword = user.password();
                var enteredPassword = credentialInput.getChallengeResponse();

                // Compute the hash
                var computedHash = new SHA256PasswordEncoder().encode(enteredPassword);
LOGGER.debug("computedHash=" + computedHash);
                // Check if the computed hash matches the stored password hash
                return storedPassword.equals(computedHash);
            }
            return false;
        } else {
            throw new UnsupportedOperationException("only credential type 'password' is supported");
        }
    }

    @Override
    public UserModel getUserById(RealmModel realmModel, String id) {
        LOGGER.trace("getUserById(), id=" + id);
        var storageId = new StorageId(id);
        var username = storageId.getExternalId();
        return getUserByUsername(realmModel, username);
    }

    @Override
    public UserModel getUserByUsername(RealmModel realmModel, String username) {
        LOGGER.trace("getUserByUsername(), username=" + username);
        Optional<User> u = userDAO
                .findUserByUsername(ds, usersSQL, username);
        Set<Role> roles = userDAO
                .findRolesByUsername(ds, rolesSQL, username);

        if( u.isPresent() ) {
            return UserAndRoles.userModel(session, realmModel, model, u.get(), roles);
        }
        return null;
    }

    @Override
    public UserModel getUserByEmail(RealmModel realmModel, String s) {
        return null;
    }

    @Override
    public boolean updateCredential(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (credentialInput.getType().equals(PasswordCredentialModel.TYPE))
            throw new ReadOnlyException("user is read only for this update");
        return false;
    }

    @Override
    public void disableCredentialType(RealmModel realmModel, UserModel userModel, String s) {

    }

    @Override
    public Stream<String> getDisableableCredentialTypesStream(RealmModel realmModel, UserModel userModel) {
        return Stream.of(PasswordCredentialModel.TYPE);
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realmModel, Map<String, String> params, Integer firstResult, Integer maxResults) {
        LOGGER.trace("searchForUserStream() #1");
        LOGGER.trace("searchForUserStream() #2; search=" + params.get(UserModel.SEARCH));
        return userDAO
                .findUsers(ds, firstResult, maxResults, params.get(UserModel.SEARCH))
                .stream()
                .map( u -> UserAndRoles.userModel(session, realmModel, model, u));
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group) {
        LOGGER.trace("setGroupMembersStream");
        return UserQueryProvider.super.getGroupMembersStream(realm, group);
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realmModel, GroupModel groupModel, Integer integer, Integer integer1) {
        LOGGER.trace("setGroupMembersStream");
        return Stream.empty();
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realmModel, String s, String s1) {
        LOGGER.trace("searchForUserByUserAttributeStream");
        return Stream.empty();
    }
}
