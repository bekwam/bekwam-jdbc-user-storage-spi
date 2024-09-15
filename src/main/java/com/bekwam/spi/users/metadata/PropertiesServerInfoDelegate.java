package com.bekwam.spi.users.metadata;

import com.google.common.collect.Maps;
import org.jboss.logging.Logger;

import java.util.Map;
import java.util.Properties;

/**
 * Reads a properties file including Maven substitutions for display in
 * Keycloak's Server Info
 *
 * @author carl
 * @since 1.0
 */
public class PropertiesServerInfoDelegate {

    private static final Logger LOGGER = Logger.getLogger(PropertiesServerInfoDelegate.class);

    private final String FILE_PATH = "/info.properties";

    public Map<String, String> getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(
                getClass().getResourceAsStream(FILE_PATH)
            );
        } catch(Exception exc) {
            LOGGER.warn("cannot find properties file at " + FILE_PATH + "; no server info will be displayed in Keycloak");
        }
        return Maps.newHashMap(Maps.fromProperties(properties));
    }
}
