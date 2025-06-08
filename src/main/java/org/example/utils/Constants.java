package org.example.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final Map<String, String> ConfigFiles = new HashMap<>();

    public static final String DEV_PROFILE_FILE_PATH = "src/test/resources/profile/dev.properties";
    public static final String QA_PROFILE_FILE_PATH = "src/test/resources/profile/qa.properties";
    public static final String CHROME = "chrome";
    public static final String EDGE = "edge";

    static {
        ConfigFiles.put(CHROME, "src/main/resources/configuration/chrome.json");
        ConfigFiles.put(EDGE, "src/main/resources/configuration/edge.json");
    }
}
