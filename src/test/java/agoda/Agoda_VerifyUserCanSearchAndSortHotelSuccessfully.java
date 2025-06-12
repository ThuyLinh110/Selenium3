package agoda;

import base.TestBase;
import io.qameta.allure.Allure;
import org.example.data.agoda.SearchHotelData;
import org.example.page.agoda.HomePage;
import org.example.page.general.GeneralPage;
import org.example.utils.Assertion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

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

    @Test
    public void agoda_VerifyUserCanSearchAndSortHotelSuccessfully() {
        Allure.step("Step 1: Navigate to Agoda page");
        generalPage.openPage();

        Allure.step("Step 2:  Search hotel with info: " + searchHotelData);
        homePage.fillHotelInfo(searchHotelData);

        Assertion.assertAll("Complete running test case");
    }

    HomePage homePage = new HomePage();
    GeneralPage generalPage = new GeneralPage();
    String place;
    LocalDate nextFriday, threeDaysFromNextFriday;
    SearchHotelData searchHotelData;
}
