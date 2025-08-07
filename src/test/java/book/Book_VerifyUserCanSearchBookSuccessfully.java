package book;

import base.TestBase;
import org.example.page.book.HomePage;
import org.example.page.book.SearchResultPage;
import org.example.page.general.GeneralPage;
import org.example.utils.Assertion;
import org.testng.annotations.Test;

public class Book_VerifyUserCanSearchBookSuccessfully extends TestBase {

    @Test(groups = {"smoke"}, description = "Book - Verify user can search book successfully")
    public void book_VerifyUserCanSearchBookSuccessfully() {
        generalPage.openPage();
        homePage.searchBook(keyword);

        Assertion.assertTrue(searchResultPage.areAllBookTitlesContainKeyword(keyword), "VP: Verify all book titles contain the searched keyword");

        Assertion.assertAll("Complete running test case");
    }

    GeneralPage generalPage = new GeneralPage();
    HomePage homePage = new HomePage();
    SearchResultPage searchResultPage = new SearchResultPage();
    String keyword = "playwright";
}
