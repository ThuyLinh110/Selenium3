package org.example.driver;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.WebDriverRunner;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Config;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DriverUtils {

    private static final ConcurrentHashMap<Long, SelenideDriver> drivers = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, SelenideConfig> configs = new ConcurrentHashMap<>();

    public static void initializeDriver(Config config) {
        long currentThreadID = getCurrentThreadID();
        if (!drivers.containsKey(currentThreadID)) {
            SelenideDriver driver = new SelenideDriver(config.toSelenideConfig());
            synchronized (driver) {
                drivers.put(currentThreadID, driver);
            }
            driver.open();
            driver.getWebDriver().manage().window().maximize();
            WebDriverRunner.setWebDriver(driver.getWebDriver());
        }
    }

    public static SelenideDriver getCurrentDriver() {
        long threadID = getCurrentThreadID();
        SelenideDriver driver = drivers.get(threadID);

        if (driver == null) {
            throw new IllegalStateException(
                    String.format("WebDriver is not initialized for thread %d. Call initializeDriver() first.", threadID)
            );
        }

        return driver;
    }

    public static boolean isDriverAlive() {
        long threadID = getCurrentThreadID();
        return drivers.get(threadID) != null;
    }

    public static void quitDriver() {
        long threadID = getCurrentThreadID();
        SelenideDriver driver = drivers.remove(threadID);

        if (driver != null) {
            try {
                log.debug("Closing driver for thread: {}", threadID);
                driver.close();
            } catch (Exception e) {
                log.warn("Error closing driver for thread {}: {}", threadID, e.getMessage());
            } finally {
                configs.remove(threadID);
            }
        }
    }

    public static void quitAllDrivers() {
        log.debug("Closing all drivers. Active threads: {}", drivers.keySet());
        drivers.forEach((threadId, driver) -> {
            try {
                driver.close();
            } catch (Exception e) {
                log.warn("Error closing driver for thread {}: {}", threadId, e.getMessage());
            }
        });

        drivers.clear();
        configs.clear();
        Selenide.closeWebDriver();
    }

    @SuppressWarnings("deprecation")
    private static long getCurrentThreadID() {
        return Thread.currentThread().getId();
    }
}