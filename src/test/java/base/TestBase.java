package base;

import org.example.config.Config;
import org.example.config.ConfigLoader;
import org.example.driver.DriverUtils;
import org.example.utils.Constants;
import org.example.utils.PropertiesUtils;
import org.example.utils.SharedParameter;
import org.example.utils.YamlUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Objects;

import static org.example.utils.Constants.ConfigFiles;

public class TestBase {

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "environment", "language"})
    public void beforeClass(String browser, String env, @Optional String language) {
        SharedParameter.ENV = env;
        SharedParameter.LANGUAGE = Objects.requireNonNullElse(language, "en");

        String propPath = getPropPath(SharedParameter.ENV);
        String yamlPath = SharedParameter.LANGUAGE.equals("vi") ? Constants.VI_LANGUAGE_YAML_FILE_PATH : Constants.EN_LANGUAGE_YAML_FILE_PATH;

        PropertiesUtils.loadProperties(propPath);
        YamlUtils.loadYaml(yamlPath);
        Config config = ConfigLoader.loadConfig(ConfigFiles.get(browser));

        DriverUtils.initializeDriver(config);

    }

    @AfterMethod(alwaysRun = true)
    public void afterClass() {
        DriverUtils.quitDriver();
    }

    public String getPropPath(String env) {
        switch (env) {
            case "vietjet":
                return Constants.VIETJET_PROFILE_FILE_PATH;
            case "agoda":
                return Constants.AGODA_PROFILE_FILE_PATH;
            case "book":
                return Constants.BOOK_PROFILE_FILE_PATH;
            default:
                return "";
        }
    }
}
