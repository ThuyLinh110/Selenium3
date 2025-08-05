package org.example.page.book;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.shadowDeepCss;
import static com.codeborne.selenide.Selenide.$;

public class HomePage {

    @Step("Enter keyword into search text box: {0}")
    public void searchBook(String keyword) {
        SelenideElement element = $(shadowDeepCss(searchTextbox));
        element.click();
        element.setValue(keyword).pressEnter();
    }

    private String searchTextbox = "input#input";
}
