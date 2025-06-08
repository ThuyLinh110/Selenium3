package org.example.page.general;


import org.example.driver.DriverUtils;
import org.example.utils.PropertiesUtils;

import static com.codeborne.selenide.Selenide.open;

public class GeneralPage {

    public void gotoURL(String url) {
        open(url);
    }

    public void openPage() {
        DriverUtils.getCurrentDriver().open(PropertiesUtils.getProperty("url"));
    }
}
