package base;

import org.example.config.ConfigLoader;
import org.example.driver.DriverUtils;
import org.example.utils.PropertiesUtils;
import org.example.utils.SharedParameter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import static org.example.utils.Constants.ConfigFiles;

public class TestBase {

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser", "environment"})
    public void beforeClass(String browser, String env) {
        SharedParameter.ENV = env;
        PropertiesUtils.loadProperties();
        DriverUtils.initializeDriver(ConfigLoader.loadConfig(ConfigFiles.get(browser)));
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        DriverUtils.quitDriver();
    }
}
