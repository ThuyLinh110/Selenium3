package org.example.page.general;


import io.qameta.allure.Step;
import org.example.driver.DriverUtils;
import org.example.utils.PropertiesUtils;
import org.example.utils.SharedParameter;

public class GeneralPage {

    @Step("Open page on URL: {url}")
    public void gotoURL(String url) {
        DriverUtils.getCurrentDriver().open(url);
    }

    public void openPage() {
        String baseURL = PropertiesUtils.getProperty("url");
        switch (SharedParameter.LANGUAGE) {
            case "vi":
                baseURL += SharedParameter.ENV.equals("agoda") ? "vi-vn" : "vi";
                break;
            case "en":
                baseURL += SharedParameter.ENV.equals("agoda") ? "" : "en";
                break;
            default:
                break;
        }
        gotoURL(baseURL);
    }
}
