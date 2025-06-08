package org.example.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropertiesUtils {

    public static void loadProperties() {
        try {
            String propPath = SharedParameter.ENV.equals("dev") ? Constants.DEV_PROFILE_FILE_PATH : Constants.QA_PROFILE_FILE_PATH;
            prop = new Properties();
            prop.load(new FileReader(propPath));
        } catch (IOException e) {
            log.error("" + e);
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    private static Properties prop;
}
