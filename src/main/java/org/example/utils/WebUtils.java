package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.driver.DriverUtils;

@Slf4j
public class WebUtils {

    /**
     * Switch to page with page number
     */
    public static void switchToPage(int page) {
        DriverUtils.getCurrentDriver().switchTo().window(page);
        switchToMain();
    }

    /**
     * Switch to main
     */
    public static void switchToMain() {
        DriverUtils.getCurrentDriver().switchTo().defaultContent();
    }

}
