package planit.gjacolbia.framework;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Configuration {
    private static final long DEFAULT_TIMEOUT = 10;
    private Configuration() {}

    private static ResourceBundle config;
    static {
        config = ResourceBundle.getBundle("config");
    }

    public static String get(String key) {
        try {
            return config.getString(key);
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            // TODO Use logger
            System.out.println("Unable to get value for key " + key);
            return "";
        }
    }

    public static long timeout() {
        try {
            return Long.parseLong(get("timeout"));
        } catch (NumberFormatException nfe) {
            // TODO Use logger
            System.out.println("Unable to parse timeout value. Using default timeout of " + DEFAULT_TIMEOUT);
            return DEFAULT_TIMEOUT;
        }
    }
}
