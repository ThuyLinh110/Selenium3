package agoda;

import base.TestBase;
import org.example.data.agoda.FilterHotelData;
import org.example.data.agoda.HotelData;
import org.example.data.agoda.SearchHotelData;
import org.example.enumData.FilterOption;
import org.example.enumData.FilterType;
import org.example.page.agoda.HomePage;
import org.example.page.agoda.HotelDetailPage;
import org.example.page.agoda.SearchResultPage;
import org.example.page.general.GeneralPage;
import org.example.utils.Assertion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class Agoda_VerifyUserCanAddHotelToFavouriteSuccessfully extends TestBase {

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
        hotelIndex = 5;
//        filterType = FilterType.STAR_RATING;
//        filterOption = FilterOption.RATING_THREE_STAR;
//        filterHotelData = FilterHotelData.builder()
//                .minPrice(500000)
//                .maxPrice(1000000)
//                .filter(List.of(FilterHotelData.Filter.builder()
//                        .filterType(filterType)
//                        .filterOption(filterOption)
//                        .build()))
//                .build();
    }

    @Test(description = "TC01 - Agoda - Add hotel to favourite successfully")
    public void agoda_VerifyUserCanAddHotelToFavouriteSuccessfully() {
        generalPage.openPage();
        homePage.closeAds();

        homePage.searchHotel(searchHotelData);
        searchResultPage.scrollUntilAllHotelDataLoaded(5);
        Assertion.assertTrue(searchResultPage.areAllTheDestinationsHaveSearchContent(5, place), String.format("VP: Check the first 5 destinations have search content: %s", place));

        searchResultPage.filterByOption(FilterType.PROPERTY_FACILITIES, FilterOption.NON_SMOKING, true);
        searchResultPage.scrollUntilAllHotelDataLoaded(5);

        the5thHotelData = HotelData.builder()
                .address(searchResultPage.getDestinationList(hotelIndex).get(hotelIndex - 1))
                .hotelName(searchResultPage.getHotelNameList(hotelIndex).get(hotelIndex - 1))
                .build();

        searchResultPage.clickOnHotelByIndex(5);
        hotelDetailData = hotelDetailPage.getHotelData();
        Assertion.assertEquals(hotelDetailData.getHotelName(), the5thHotelData.getHotelName(), "VP: Check the hotel detail page displays correct hotel name");
        Assertion.assertTrue(hotelDetailData.getAddress().contains(the5thHotelData.getAddress()), "VP: Check the hotel detail page displays correct destination");
// need to check for non-smoking



        Assertion.assertAll("Complete running test case");
    }

    HomePage homePage = new HomePage();
    GeneralPage generalPage = new GeneralPage();
    SearchResultPage searchResultPage = new SearchResultPage();
    HotelDetailPage hotelDetailPage = new HotelDetailPage();
    String place;
    LocalDate nextFriday, threeDaysFromNextFriday;
    SearchHotelData searchHotelData;
    HotelData the5thHotelData, hotelDetailData;
    int hotelIndex;
}
