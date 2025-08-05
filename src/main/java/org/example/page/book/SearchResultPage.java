package org.example.page.book;

import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.Allure;

import java.util.List;

import static com.codeborne.selenide.Selectors.shadowDeepCss;
import static com.codeborne.selenide.Selenide.$$;

public class SearchResultPage {

    public List<String> getAllBookTitles() {
        return $$(shadowDeepCss(bookTitle)).should(CollectionCondition.allMatch("",
                el -> !el.getText().isEmpty())).texts();
    }

    public boolean areAllBookTitlesContainKeyword(String keyword) {
        List<String> titles = getAllBookTitles();
        boolean result = true;
        for (String title : titles) {
            if (!title.toLowerCase().contains(keyword.toLowerCase())) {
                Allure.step(String.format("The book title %s does not contain keyword %s", title, keyword));
                result = false;
            }
        }
        return result;
    }

    private String bookTitle = "h2.title";
}
