package org.example.utils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final Map<String, String> ConfigFiles = new HashMap<>();

    public static final String VIETJET_PROFILE_FILE_PATH = "src/test/resources/profile/vietjet.properties";
    public static final String AGODA_PROFILE_FILE_PATH = "src/test/resources/profile/agoda.properties";
    public static final String EN_LANGUAGE_YAML_FILE_PATH = "src/test/resources/language/language_en.yaml";
    public static final String VI_LANGUAGE_YAML_FILE_PATH = "src/test/resources/language/language_vi.yaml";
    public static final Duration MEDIUM_TIMEOUT = Duration.ofSeconds(10);
    public static final String CHROME = "chrome";
    public static final String EDGE = "edge";

    static {
        ConfigFiles.put(CHROME, "src/main/resources/configuration/chrome.json");
        ConfigFiles.put(EDGE, "src/main/resources/configuration/edge.json");
    }
}
