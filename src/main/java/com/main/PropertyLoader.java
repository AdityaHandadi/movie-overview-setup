package com.main;

import java.io.IOException;
import java.util.Properties;

public abstract class PropertyLoader {

    public static Properties loadProperties() {
        Properties prop = new Properties();
        try {
            prop.load(PropertyLoader.class.getClassLoader().getResourceAsStream("config.properties"));

            System.setProperty("aws.s3.bucket", prop.getProperty("aws.s3.bucket"));
            System.setProperty("aws.file.path", prop.getProperty("aws.file.path"));
            System.setProperty("download.file.name", prop.getProperty("download.file.name"));
            System.setProperty("download.output.file.path", prop.getProperty("download.output.file.path"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }
}
