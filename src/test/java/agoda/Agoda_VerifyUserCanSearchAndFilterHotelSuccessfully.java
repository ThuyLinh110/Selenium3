package agoda;

import base.TestBase;
import org.example.data.agoda.FilterHotelData;
import org.example.data.agoda.SearchHotelData;
import org.example.enumData.agoda.FilterOption;
import org.example.enumData.agoda.FilterType;
import org.example.page.agoda.HomePage;
import org.example.page.agoda.SearchResultPage;
import org.example.page.general.GeneralPage;
import org.example.utils.Assertion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class Agoda_VerifyUserCanSearchAndFilterHotelSuccessfully extends TestBase {

    @BeforeMethod(alwaysRun = true)
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

        filterType = FilterType.STAR_RATING;
        filterOption = FilterOption.RATING_THREE_STAR;
        filterHotelData = FilterHotelData.builder()
                .minPrice(500000)
                .maxPrice(1000000)
                .filter(List.of(FilterHotelData.Filter.builder()
                        .filterType(filterType)
                        .filterOption(filterOption)
                        .build()))
                .build();
    }

    @Test(groups = {"regression", "smoke"}, description = "TC01 - Agoda - Search and filter hotel successfully")
    public void agoda_VerifyUserCanSearchAndFilterHotelSuccessfully() {
        generalPage.openPage();
        homePage.closeAds();

        homePage.searchHotel(searchHotelData);
        searchResultPage.scrollUntilAllHotelDataLoaded(5);
        Assertion.assertTrue(searchResultPage.areAllTheDestinationsHaveSearchContent(5, place), String.format("VP: Check the first 5 destinations have search content: %s", place));

        defaultMinPrice = searchResultPage.getMinPrice();
        defaultMaxPrice = searchResultPage.getMaxPrice();

        searchResultPage.filterHotel(filterHotelData);
        Assertion.assertTrue(searchResultPage.areAllSelectedFilterHighlighted(filterHotelData), String.format("VP: Check all selected filer are highlighted"));

        searchResultPage.scrollUntilAllHotelDataLoaded(5);
        Assertion.assertTrue(searchResultPage.areAllTheDestinationsHaveSearchContent(5, place), String.format("VP: Check the first 5 destinations have search content: %s", place));
        Assertion.assertTrue(searchResultPage.areAllPriceInSelectedRange(5, filterHotelData.getMinPrice(), filterHotelData.getMaxPrice()), String.format("VP: Check the first 5 price in the range: %d - %d", filterHotelData.getMinPrice(), filterHotelData.getMaxPrice()));
        Assertion.assertTrue(searchResultPage.areAllStarsMatchWithSelectedRating(5, filterOption), String.format("VP: Check the first 5 rating match with selected rating: %d", filterOption.getValue()));

        searchResultPage.filterByPriceRange(defaultMinPrice, defaultMaxPrice, true);
        Assertion.assertEquals(searchResultPage.getMinPriceSlider(), defaultMinPrice, String.format("VP: Check the min price in slider is reset to default: %d", defaultMinPrice));
        Assertion.assertEquals(searchResultPage.getMaxPriceSlider(), defaultMaxPrice, String.format("VP: Check the max price in slider is reset to default: %d", defaultMaxPrice));

        Assertion.assertAll("Complete running test case");
    }

    HomePage homePage = new HomePage();
    GeneralPage generalPage = new GeneralPage();
    String place;
    LocalDate nextFriday, threeDaysFromNextFriday;
    SearchHotelData searchHotelData;
    FilterHotelData filterHotelData;
    FilterType filterType;
    FilterOption filterOption;
    int defaultMinPrice, defaultMaxPrice;
    SearchResultPage searchResultPage = new SearchResultPage();
}
