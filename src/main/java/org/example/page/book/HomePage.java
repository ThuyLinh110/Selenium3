package org.example.page.book;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.driver.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selectors.shadowDeepCss;
import static com.codeborne.selenide.Selenide.$;

public class HomePage {

    @Step("Enter keyword into search text box: {0}")
    public void searchBook(String keyword) {
        SelenideElement element = $(shadowDeepCss(searchTextbox));
        element.click();
        element.setValue(keyword).pressEnter();
    }

    @Step("Enter keyword into search text box: {0}")
    public void searchBookBySelenium(String keyword) {
        WebElement input = DriverUtils.getCurrentDriver().getWebDriver()
                .findElement(bookApp).getShadowRoot()
                .findElement(searchTextBox);
        input.sendKeys(keyword);
        input.sendKeys(Keys.ENTER);
    }

    private String searchTextbox = "input#input";
    private By bookApp = By.cssSelector("book-app");
    private By searchTextBox = By.cssSelector(searchTextbox);
}
