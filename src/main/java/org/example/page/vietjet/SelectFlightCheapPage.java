package org.example.page.vietjet;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import org.apache.commons.lang3.tuple.Pair;
import org.example.enumData.vietjet.FlightType;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.WebDriverRunner.url;

public class SelectFlightCheapPage extends SelectFlightPage {
    public boolean isPageDisplayed() {
        if (!url().endsWith("/select-flight-cheap")) {
            Allure.step(String.format("The url \"%s\" does not end with \"/select-flight-cheap\"", url()), Status.FAILED);
            return false;
        }
        return true;
    }

    /**
     * Find the flight date of the cheapest ticket for n dayTrip in next m months
     *
     * @param dayTrip
     * @param monthRange
     * @return departure and return dates
     */
    @Step("Find the flight date of the cheapest ticket for {0} day trip in next {1} months")
    public Pair<LocalDate, LocalDate> findDepartureAndReturnDatesOfCheapestTicket(int dayTrip, int monthRange) {
        Pair<LocalDate, LocalDate> flightDate = null;
        int minPrice = Integer.MAX_VALUE;
        for (int i = 0; i < monthRange; i++) {
            YearMonth currentMonth = getCurrentDepartureMonth();
            int endDate = currentMonth.lengthOfMonth();
            int firstDate = getTheFirstAvailableDateNumber(FlightType.DEPARTURE);
            for (int day = firstDate; day <= endDate; day++) {
                LocalDate departureDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day);
                LocalDate returnDate = departureDate.plusDays(dayTrip);
                int totalPrice = getTicketPriceByDate(FlightType.DEPARTURE, departureDate) + getTicketPriceByDate(FlightType.RETURN, returnDate);
                if (totalPrice < minPrice) {
                    minPrice = totalPrice;
                    flightDate = Pair.of(departureDate, returnDate);
                }
            }
            navigateToMonth(FlightType.DEPARTURE, currentMonth.plusMonths(1));
        }
        return flightDate;
    }

    @Step("Select ticket on departure date {0} and return date {1}")
    public void selectTicket(LocalDate departureDate, LocalDate returnDate) {
        selectTicketByDate(FlightType.DEPARTURE, departureDate);
        selectTicketByDate(FlightType.RETURN, returnDate);
        clickContinueButton();
        waitForPageLoading();
    }

    public YearMonth getCurrentDepartureMonth() {
        return getHighlightedMonth(FlightType.DEPARTURE);
    }

    public YearMonth getHighlightedMonth(FlightType flightType) {
        SelenideElement element = getContainerByFlightType(flightType)
                .$$x("./following-sibling::div//div[contains(@class, 'slick-current')]//p")
                .first();
        return YearMonth.parse(element.getText(), DateTimeFormatter.ofPattern("MM/yyyy"));
    }

    public int getTheFirstAvailableDateNumber(FlightType flightType) {
        SelenideElement element = getContainerByFlightType(flightType)
                .$$x("./following-sibling::div//div[@role='button' and div/span]/p")
                .first();
        return Integer.parseInt(element.getText());
    }

    public ElementsCollection getSliderButton(FlightType flightType) {
        return getContainerByFlightType(flightType)
                .$$x("./following-sibling::div//div[contains(@class, 'slick-slider')]//button");
    }

    public int getTicketPriceByDate(FlightType flightType, LocalDate date) {
        navigateToMonth(flightType, YearMonth.from(date));
        return Integer.parseInt(getPriceElementByDate(flightType, date)
                .getText()
                .replaceAll(",", ""));
    }

    public void navigateToMonth(FlightType flightType, YearMonth targetMonth) {
        boolean clickNext = targetMonth.isAfter(getHighlightedMonth(flightType));
        while (!getHighlightedMonth(flightType).equals(targetMonth)) {
            if (clickNext) {
                getSliderButton(flightType).last().scrollIntoCenter().click();
            } else {
                getSliderButton(flightType).first().scrollIntoCenter().click();
            }
        }
    }

    public void selectTicketByDate(FlightType flightType, LocalDate date) {
        navigateToMonth(flightType, YearMonth.from(date));
        getPriceElementByDate(flightType, date).scrollIntoCenter().click();
    }

    private SelenideElement getPriceElementByDate(FlightType flightType, LocalDate date) {
        return getContainerByFlightType(flightType)
                .$$x(String.format("./following-sibling::div//div[p[text()='%d']]//span", date.getDayOfMonth()))
                .first();
    }

    private SelenideElement getContainerByFlightType(FlightType flightType) {
        return $x(String.format(containerByFlightType, flightType.toString()));
    }

    private String containerByFlightType = "//div[p[contains(@class, 'MuiTypography-h2') and text()='%s']]";

}
