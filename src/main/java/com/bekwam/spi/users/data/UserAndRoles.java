package com.bekwam.spi.users.data;

import com.bekwam.spi.users.provider.JDBCUserStorageProvider;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Factory method for UserModel
 *
 * @author carl
 * @since 1.0
 */
public class UserAndRoles {

    private static final Logger LOGGER = Logger.getLogger(UserAndRoles.class);

    public static UserModel userModel(
            KeycloakSession session,
            RealmModel realmModel,
            ComponentModel model,
            User u
    ) {
        return new AbstractUserAdapterFederatedStorage(session, realmModel, model) {
            @Override
            public String getUsername() {
                return u.username();
            }

            @Override
            public void setUsername(String username) {
                throw new UnsupportedOperationException("jdbc users are read-only");
            }

            @Override
            public String getFirstName() {
                return u.name();
            }

            @Override
            public String getLastName() {
                return u.name();
            }

            @Override
            public String getEmail() {
                return u.email();
            }


        };
    }

    public static UserModel userModel(
            KeycloakSession session,
            RealmModel realmModel,
            ComponentModel model,
            User u,
            Set<Role> roles
    ) {
        return new AbstractUserAdapterFederatedStorage(session, realmModel, model) {
            @Override
            public String getUsername() {
                return u.username();
            }

            @Override
            public void setUsername(String s) {
                throw new UnsupportedOperationException("jdbc read-only for now");
            }

            @Override
            protected Set<RoleModel> getRoleMappingsInternal() {
                Set<String> roleNames = roles
                        .stream()
                        .map(Role::name)
                        .collect(Collectors.toSet());
                return realmModel
                        .getRolesStream()
                        .filter(rm -> roleNames.contains(rm.getName()))
                        .collect(Collectors.toSet());
            }
        };
    }
}
