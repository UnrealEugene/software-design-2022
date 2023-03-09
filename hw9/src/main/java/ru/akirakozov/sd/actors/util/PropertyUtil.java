package ru.akirakozov.sd.actors.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class PropertyUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(PropertyUtil.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        PROPERTIES.setProperty(key, value);
    }
}
