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
        String baseURL;
        switch (SharedParameter.LANGUAGE) {
            case "vi":
                baseURL = PropertiesUtils.getProperty("url_vi");
                break;
            case "en":
                baseURL = PropertiesUtils.getProperty("url_en");
                break;
            default:
                baseURL = PropertiesUtils.getProperty("url");
                break;
        }
        gotoURL(baseURL);
    }
}
