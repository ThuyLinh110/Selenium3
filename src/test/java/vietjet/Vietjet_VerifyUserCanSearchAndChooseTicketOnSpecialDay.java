package vietjet;

import base.TestBase;
import org.example.data.vietjet.SearchTicketData;
import org.example.data.vietjet.TicketInfoData;
import org.example.enumData.vietjet.Label;
import org.example.page.general.GeneralPage;
import org.example.page.vietjet.HomePage;
import org.example.page.vietjet.PassengerInformationPage;
import org.example.page.vietjet.SelectFlightPage;
import org.example.utils.Assertion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class Vietjet_VerifyUserCanSearchAndChooseTicketOnSpecialDay extends TestBase {

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
                .passenger(SearchTicketData.Passenger.builder()
                        .adultNumber(totalAdult)
                        .build())
                .build();
    }

    @Test(groups = {"regression"}, description = "TC04 - Vietjet - Search and choose tickets on a specific day successfully")
    public void vietjet_VerifyUserCanSearchAndChooseTicketOnSpecialDay() {
        generalPage.openPage();
        homePage.acceptCookies();
        homePage.clickNotNow();
        homePage.searchTicket(searchTicketData);

        selectFlightPage.waitForPriceListLoading();
        Assertion.assertTrue(selectFlightPage.isSelectTravelOptionsPageDisplayed(), "VP: Verify Select Travel Options page is displayed.");
        Assertion.assertTrue(selectFlightPage.isTheTicketPriceDisplayedIn("VND"), "VP: Verify the ticket price is displayed in VND");
        Assertion.assertEquals(selectFlightPage.getFromPlace(), searchTicketData.getFrom(), "VP: Verify the departure place is correct");
        Assertion.assertEquals(selectFlightPage.getToPlace(), searchTicketData.getTo(), "VP: Verify the arrival place is correct");
        Assertion.assertEquals(selectFlightPage.getTotalPassengerByType(Label.ADULT), totalAdult, "VP: Verify number of passenger is correct");

//       Select the Departure ticket
        selectFlightPage.selectTheFirstCheapestTicket();
        selectFlightPage.clickContinueButton();
        departureInfo = selectFlightPage.getTicketInfoOfReservation(true);

//       Select the Return ticket
        selectFlightPage.selectTheFirstCheapestTicket();
        selectFlightPage.clickContinueButton();
        returnInfo = selectFlightPage.getTicketInfoOfReservation(false);

        Assertion.assertTrue(passengerInformationPage.isPageDisplayed(), "VP: Verify Passenger Information page is displayed");
        Assertion.assertEquals(departureInfo, passengerInformationPage.getTicketInfoOfReservation(true), "VP: Verify tickets information of departure flight is correct");
        Assertion.assertEquals(returnInfo, passengerInformationPage.getTicketInfoOfReservation(false), "VP: Verify tickets information of return flight is correct");

        Assertion.assertAll("Complete running test case");
    }

    SearchTicketData searchTicketData;
    TicketInfoData departureInfo, returnInfo;
    LocalDate departureDate, returnDate;
    int totalAdult;
    HomePage homePage = new HomePage();
    SelectFlightPage selectFlightPage = new SelectFlightPage();
    GeneralPage generalPage = new GeneralPage();
    PassengerInformationPage passengerInformationPage = new PassengerInformationPage();

}
