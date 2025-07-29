package vietjet;

import base.TestBase;
import org.apache.commons.lang3.tuple.Pair;
import org.example.data.vietjet.Passenger;
import org.example.data.vietjet.SearchTicketData;
import org.example.data.vietjet.TicketInfoData;
import org.example.page.general.GeneralPage;
import org.example.page.vietjet.HomePage;
import org.example.page.vietjet.PassengerInformationPage;
import org.example.page.vietjet.SelectFlightCheapPage;
import org.example.page.vietjet.SelectFlightPage;
import org.example.utils.Assertion;
import org.example.utils.YamlUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class Vietjet_VerifyUserCanSearchAndChooseCheapestTicketOnNext3Months extends TestBase {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        departureDate = LocalDate.now().plusDays(1);
        returnDate = LocalDate.now().plusDays(4);
        totalAdult = 2;
        searchTicketData = SearchTicketData.builder()
                .from("Ho Chi Minh")
                .to("Ha Noi")
                .departureDate(departureDate)
                .returnDate(returnDate)
                .passenger(Passenger.builder()
                        .adultNumber(totalAdult)
                        .build())
                .isFindLowestFare(true)
                .build();
        language = (String) YamlUtils.getProperty("config.displayed_language");
    }

    @Test(groups = {"regression", "smoke"}, description = "TC05 - Vietjet - Search and choose cheapest tickets on next 3 months successfully")
    public void vietjet_VerifyUserCanSearchAndChooseCheapestTicketOnNext3Months() {
        generalPage.openPage();
        homePage.acceptCookies();
        homePage.clickNotNow();

        Assertion.assertEquals(homePage.getCurrentLanguage(), language, "VP: Verify the page displays with correct language");

        homePage.searchTicket(searchTicketData);

        Assertion.assertTrue(selectFlightCheapPage.isPageDisplayed(), "VP: Verify Select Flight Cheap page is displayed.");
        Assertion.assertEquals(selectFlightCheapPage.getFromPlace(), searchTicketData.getFrom(), "VP: Verify the departure place is correct");
        Assertion.assertEquals(selectFlightCheapPage.getToPlace(), searchTicketData.getTo(), "VP: Verify the arrival place is correct");
        Assertion.assertEquals(selectFlightCheapPage.getPassengerInfo(), searchTicketData.getPassenger(), "VP: Verify number of passenger is correct");

        flightDate = selectFlightCheapPage.findDepartureAndReturnDatesOfCheapestTicket(3, 3);
        departureDate = flightDate.getLeft();
        returnDate = flightDate.getRight();
        selectFlightCheapPage.selectTicket(departureDate, returnDate);

        Assertion.assertTrue(selectFlightPage.isSelectTravelOptionsPageDisplayed(), "Verify Select Flight page is displayed.");
        Assertion.assertEquals(selectFlightPage.getFromPlace(), searchTicketData.getFrom(), "VP: Verify the departure place is correct");
        Assertion.assertEquals(selectFlightPage.getToPlace(), searchTicketData.getTo(), "VP: Verify the arrival place is correct");
        Assertion.assertEquals(selectFlightPage.getPassengerInfo(), searchTicketData.getPassenger(), "VP: Verify number of passenger is correct");

//       Select the Departure ticket
        selectFlightPage.selectTheFirstCheapestTicket();
        selectFlightPage.clickContinueButton();
        departureInfo = selectFlightPage.getDepartureTicketInfo();

//       Select the Return ticket
        selectFlightPage.selectTheFirstCheapestTicket();
        selectFlightPage.clickContinueButton();
        returnInfo = selectFlightPage.getReturnTicketInfo();

        Assertion.assertTrue(passengerInformationPage.isPageDisplayed(), "VP: Verify Passenger Information page is displayed");
        Assertion.assertEquals(departureInfo, passengerInformationPage.getDepartureTicketInfo(), "VP: Verify tickets information of departure flight is correct");
        Assertion.assertEquals(returnInfo, passengerInformationPage.getReturnTicketInfo(), "VP: Verify tickets information of return flight is correct");

        Assertion.assertAll("Complete running test case");
    }

    SearchTicketData searchTicketData;
    TicketInfoData departureInfo, returnInfo;
    LocalDate departureDate, returnDate;
    int totalAdult;
    HomePage homePage = new HomePage();
    SelectFlightCheapPage selectFlightCheapPage = new SelectFlightCheapPage();
    SelectFlightPage selectFlightPage = new SelectFlightPage();
    GeneralPage generalPage = new GeneralPage();
    PassengerInformationPage passengerInformationPage = new PassengerInformationPage();
    Pair<LocalDate, LocalDate> flightDate;
    String language;
}
