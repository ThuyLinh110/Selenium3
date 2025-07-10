package org.example.utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.actions;
import static com.codeborne.selenide.Selenide.sleep;

public class WebUtils {
    private static final Logger log = LoggerFactory.getLogger(WebUtils.class);

    public static void switchToLatestTab() {
        Selenide.switchTo().window(WebDriverRunner.getWebDriver().getWindowHandles().size() - 1);
    }

    public static void switchToPreviousTab() {
        List<String> handleList = new ArrayList<>(WebDriverRunner.getWebDriver().getWindowHandles());
        int currentIndex = handleList.indexOf(WebDriverRunner.getWebDriver().getWindowHandle());
        Selenide.switchTo().window(handleList.get(currentIndex - 1));
    }

    public static void scrollUntilElementVisible(SelenideElement element) {
        int maxTries = 50;
        int tries = 0;

        while (!element.exists() && tries < maxTries) {
            actions().sendKeys(Keys.PAGE_DOWN).perform();
            sleep(500); // Wait for lazy-load
            tries++;
        }
        actions().sendKeys(Keys.PAGE_DOWN).perform();
        if (!element.exists()) {
            throw new RuntimeException("Element not found after scrolling");
        }
    }

}
