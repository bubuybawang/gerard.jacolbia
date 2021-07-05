package planit.gjacolbia.framework;

import lombok.extern.log4j.Log4j2;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Log4j2
public final class Configuration {
    private static final int DEFAULT_TIMEOUT = 10;
    private Configuration() {}

    private static ResourceBundle config;
    static {
        config = ResourceBundle.getBundle("config");
    }

    public static String get(String key) {
        try {
            return config.getString(key);
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            log.warn("Unable to get value for key " + key);
            return "";
        }
    }

    public static int timeout() {
        try {
            return Integer.parseInt(get("timeout"));
        } catch (NumberFormatException nfe) {
            log.warn("Unable to parse timeout value. Using default timeout of " + DEFAULT_TIMEOUT);
            return DEFAULT_TIMEOUT;
        }
    }
}
