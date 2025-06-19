package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class YamlUtils {

    public static void loadYaml(String yamlPath) {
        try (FileReader reader = new FileReader(yamlPath)) {
            Yaml snakeYAML = new Yaml();
            yaml = snakeYAML.load(reader);
        } catch (IOException e) {
            log.error("Error loading YAML from " + yamlPath, e);
        }
    }

    public static Object getProperty(String key) {
        Object current = ((Map<String, Object>) yaml);
        if (current == null) {
            return null;
        }
        // Support nested keys with dot notation, e.g. "button.close"
        String[] keys = key.split("\\.");
        for (String k : keys) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(k);
            } else {
                return null;
            }
        }
        return current;
    }

    private static Map<String, Object> yaml;
}
