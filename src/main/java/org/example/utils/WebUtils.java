package org.example.utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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

}
