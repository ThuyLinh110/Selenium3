package base;

import org.example.config.ConfigLoader;
import org.example.driver.DriverUtils;
import org.example.report.AllureReport;
import org.example.utils.Constants;
import org.example.utils.PropertiesUtils;
import org.example.utils.SharedParameter;
import org.example.utils.YamlUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Objects;

import static org.example.utils.Constants.ConfigFiles;

public class TestBase {

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser", "environment", "language", "retryCount"})
    public void beforeClass(String browser, String env, @Optional String language, @Optional String retryCount) {
        SharedParameter.ENV = env;
        SharedParameter.LANGUAGE = Objects.requireNonNullElse(language, "en");
        SharedParameter.RETRY_COUNT = Objects.isNull(retryCount) ? 0 : Integer.parseInt(retryCount);

        String propPath = SharedParameter.ENV.equals("agoda") ? Constants.AGODA_PROFILE_FILE_PATH : Constants.VIETJET_PROFILE_FILE_PATH;
        String yamlPath = SharedParameter.LANGUAGE.equals("vi") ? Constants.VI_LANGUAGE_YAML_FILE_PATH : Constants.EN_LANGUAGE_YAML_FILE_PATH;

        PropertiesUtils.loadProperties(propPath);
        YamlUtils.loadYaml(yamlPath);
        DriverUtils.initializeDriver(ConfigLoader.loadConfig(ConfigFiles.get(browser)));

        AllureReport.setupAllure();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        DriverUtils.quitDriver();
    }
}
