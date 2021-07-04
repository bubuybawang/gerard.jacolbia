package planit.gjacolbia.framework;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Configuration {
    private Configuration() {}

    private static ResourceBundle config;
    static {
        config = ResourceBundle.getBundle("config");
    }

    public static String get(String key) {
        try {
            return config.getString(key);
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            return "";
        }
    }
}
