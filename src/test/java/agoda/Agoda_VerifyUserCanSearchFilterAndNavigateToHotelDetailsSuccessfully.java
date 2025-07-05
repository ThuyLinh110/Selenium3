package agoda;

import base.TestBase;
import org.example.data.agoda.HotelData;
import org.example.data.agoda.ReviewPointData;
import org.example.data.agoda.SearchHotelData;
import org.example.enumData.FilterOption;
import org.example.enumData.FilterType;
import org.example.enumData.ReviewType;
import org.example.page.agoda.HomePage;
import org.example.page.agoda.HotelDetailPage;
import org.example.page.agoda.SearchResultPage;
import org.example.page.general.GeneralPage;
import org.example.utils.Assertion;
import org.example.utils.Common;
import org.example.utils.ReviewPointUtils;
import org.example.utils.WebUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Agoda_VerifyUserCanSearchFilterAndNavigateToHotelDetailsSuccessfully extends TestBase {

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
        filterType = FilterType.PROPERTY_FACILITIES;
        filterOption = FilterOption.SWIMMING_POOL;
        reviewTypes = new ArrayList<>(Arrays.asList(ReviewType.values()));
    }

    @Test(groups = {"regression"}, description = "TC03 - Agoda - Search, filter and navigate to hotel details successfully")
    public void Agoda_VerifyUserCanSearchFilterAndNavigateToHotelDetailsSuccessfully() {
        generalPage.openPage();
        homePage.closeAds();
        homePage.searchHotel(searchHotelData);
        searchResultPage.scrollUntilAllHotelDataLoaded(5);
        Assertion.assertTrue(searchResultPage.areAllTheDestinationsHaveSearchContent(5, place), String.format("VP: Check the first 5 destinations have search content: %s", place));

        searchResultPage.filterByOption(filterType, filterOption, true);
        searchResultPage.scrollUntilAllHotelDataLoaded(5);
        the5thHotelData = searchResultPage.getHotelDataByIndex(5);
        searchResultPage.clickOnHotelByIndex(5);
        Assertion.assertEquals(hotelDetailPage.getHotelName(), the5thHotelData.getHotelName(), "VP: Check the hotel detail page displays correct hotel name");
        Assertion.assertTrue(hotelDetailPage.isHotelAddressMatched(hotelDetailPage.getAddress(), the5thHotelData.getAddress()), "VP: Check the hotel detail page displays correct destination");
        Assertion.assertTrue(Common.lowerCaseList(hotelDetailPage.getSpecialBenefitList()).contains(filterOption.toString().toLowerCase()), "VP: Check the hotel detail page displays correct the selected filter");

        WebUtils.switchToPreviousTab();
        reviewPointData = searchResultPage.getListReviewPointOfHotelByIndex(1);
        Assertion.assertTrue(reviewTypes.containsAll(ReviewPointUtils.getListReviewTypeFromReviewPointData(reviewPointData)), "VP: Check the detail review popup appears with correct information");

        firstHotelData = searchResultPage.getHotelDataByIndex(1);
        searchResultPage.clickOnHotelByIndex(1);
        reviewPointData1 = hotelDetailPage.getListReviewPointOfHotel();
        Assertion.assertEquals(hotelDetailPage.getHotelName(), firstHotelData.getHotelName(), "VP: Check the hotel detail page displays correct hotel name");
        Assertion.assertTrue(hotelDetailPage.isHotelAddressMatched(hotelDetailPage.getAddress(), firstHotelData.getAddress()), "VP: Check the hotel detail page displays correct destination");
        Assertion.assertTrue(Common.lowerCaseList(hotelDetailPage.getSpecialBenefitList()).contains(filterOption.toString().toLowerCase()), "VP: Check the hotel detail page displays correct the selected filter");

        reviewPointData1.sort(Comparator.comparing(ReviewPointData::getReviewType));
        reviewPointData.sort(Comparator.comparing(ReviewPointData::getReviewType));
        Assertion.assertEquals(reviewPointData1, reviewPointData, "VP: Check the hotel detail page displays correct review");

        Assertion.assertAll("Complete running test case");
    }

    HomePage homePage = new HomePage();
    GeneralPage generalPage = new GeneralPage();
    SearchResultPage searchResultPage = new SearchResultPage();
    HotelDetailPage hotelDetailPage = new HotelDetailPage();
    String place;
    LocalDate nextFriday, threeDaysFromNextFriday;
    SearchHotelData searchHotelData;
    HotelData the5thHotelData, firstHotelData;
    FilterType filterType;
    FilterOption filterOption;
    List<ReviewType> reviewTypes;
    List<ReviewPointData> reviewPointData, reviewPointData1;
}
