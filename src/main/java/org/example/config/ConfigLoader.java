package org.example.config;

import org.example.utils.JsonUtils;

public class ConfigLoader {

    public static Config loadConfig(String jsonPath) {
        return JsonUtils.fromJson(JsonUtils.getJson(jsonPath), Config.class);
    }
}
