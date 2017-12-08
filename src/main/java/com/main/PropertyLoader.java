package com.main;

import java.io.IOException;
import java.util.Properties;

public abstract class PropertyLoader {

    public static Properties loadProperties() {
        Properties prop = new Properties();
        try {
            prop.load(PropertyLoader.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }
}
