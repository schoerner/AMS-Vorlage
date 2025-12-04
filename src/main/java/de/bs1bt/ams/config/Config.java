package de.bs1bt.ams.config;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try {
            props.load(Config.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Konfigurationsdatei konnte nicht geladen werden", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
