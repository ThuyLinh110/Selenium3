package org.example.page.book;

import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.Allure;
import org.example.driver.DriverUtils;
import org.example.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selectors.shadowDeepCss;
import static com.codeborne.selenide.Selenide.$$;

public class SearchResultPage {

    public List<String> getAllBookTitles() {
        return $$(shadowDeepCss(bookTitle)).should(CollectionCondition.allMatch("",
                el -> !el.getText().isEmpty())).texts();
    }

    public boolean areAllBookTitlesContainKeyword(List<String> titles, String keyword) {
        boolean result = true;
        for (String title : titles) {
            if (!title.toLowerCase().contains(keyword.toLowerCase())) {
                Allure.step(String.format("The book title %s does not contain keyword %s", title, keyword));
                result = false;
            }
        }
        return result;
    }

    public List<String> getAllBookTitlesBySelenium() {
        WebDriver driver = DriverUtils.getCurrentDriver().getWebDriver();
        WebDriverWait wait = new WebDriverWait(driver, Constants.MEDIUM_TIMEOUT);
        return wait.until(d -> {
            List<String> titles = driver.findElement(bookApp).getShadowRoot()
                    .findElement(bookExplore).getShadowRoot()
                    .findElements(bookItem).stream()
                    .map(i -> i.getShadowRoot().findElement(By.cssSelector(bookTitle)).getText().trim())
                    .collect(Collectors.toList());
            return !titles.isEmpty() && titles.stream().noneMatch(String::isEmpty) ? titles : null;
        });
    }

    private String bookTitle = "h2.title";
    private By bookApp = By.cssSelector("book-app");
    private By bookExplore = By.cssSelector("book-explore");
    private By bookItem = By.cssSelector("book-item");
}
