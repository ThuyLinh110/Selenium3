package agoda;

import base.TestBase;
import org.example.data.agoda.SearchHotelData;
import org.example.page.agoda.HomePage;
import org.example.page.agoda.SearchResultPage;
import org.example.page.general.GeneralPage;
import org.example.utils.Assertion;
import org.example.utils.Common;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class Agoda_VerifyUserCanSearchAndSortHotelSuccessfully extends TestBase {

    @BeforeMethod
    public void setUp() {
        place = "Da Nang";
        nextFriday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        threeDaysFromNextFriday = nextFriday.plusDays(3);
        searchHotelData = SearchHotelData.builder()
                .place(place)
                .fromDate(nextFriday)
                .toDate(threeDaysFromNextFriday)
                .isDayUseStay(false)
                .occupancy(SearchHotelData.Occupancy.builder()
                        .room(2)
                        .adults(4)
                        .build())
                .build();
    }

    @Test(description = "TC01 - Agoda - Search and sort hotel successfully")
    public void agoda_VerifyUserCanSearchAndSortHotelSuccessfully() {
        generalPage.openPage();
        homePage.closeAds();

        homePage.searchHotel(searchHotelData);
        searchResultPage.scrollUntilAllHotelDataLoaded(5);
        Assertion.assertTrue(searchResultPage.areAllTheDestinationsHaveSearchContent(5, place), String.format("VP: Check the first 5 destinations have search content: %s", place));

        searchResultPage.clickLowestPriceFirstButton();
        searchResultPage.scrollUntilAllHotelDataLoaded(5);
        priceList = searchResultPage.getPriceList(5);
        Assertion.assertEquals(priceList, Common.sort(priceList), "VP: Check the 5 first hotel prices are sorted in ascending");
        Assertion.assertTrue(searchResultPage.areAllTheDestinationsHaveSearchContent(5, place), "VP: Check all the hotel destination are still correct");

        Assertion.assertAll("Complete running test case");
    }

    HomePage homePage = new HomePage();
    GeneralPage generalPage = new GeneralPage();
    String place;
    LocalDate nextFriday, threeDaysFromNextFriday;
    SearchHotelData searchHotelData;
    SearchResultPage searchResultPage = new SearchResultPage();
    List<Integer> priceList;
}
