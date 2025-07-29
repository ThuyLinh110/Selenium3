package org.example.page.general;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.UIAssertionError;
import io.qameta.allure.Step;
import org.example.driver.DriverUtils;
import org.example.utils.Constants;
import org.example.utils.PropertiesUtils;
import org.example.utils.SharedParameter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

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

    @Step("Wait for page loading")
    public void waitForPageLoading() {
        loadingImg.shouldBe(Condition.hidden, Constants.MEDIUM_TIMEOUT);
        try {
            closeButton.should(Condition.visible, Constants.MEDIUM_TIMEOUT).click();
        } catch (UIAssertionError ignore) {
        }
    }

    public String getCurrentLanguage() {
        return languageButton.getText();
    }

    private SelenideElement closeButton = $("button[aria-label = 'close']");
    private SelenideElement loadingImg = $x("//img[@alt='loading...']");
    private SelenideElement languageButton = $x("//button[contains(@class, 'MuiButton-text')]//div");
}
