package org.example.page.vietjet;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.data.vietjet.Passenger;
import org.example.data.vietjet.TicketInfoData;
import org.example.enumData.vietjet.Label;
import org.example.utils.Common;
import org.example.utils.YamlUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.*;
import static org.example.utils.Constants.TIME_FORMATTER;

public class SelectFlightPage {

    public boolean isTheTicketPriceDisplayedIn(String currency) {
        return highlightedDate.$x(".//p[3]").getText().contains(currency);
    }

    public boolean isSelectTravelOptionsPageDisplayed() {
        return ecoImg.isDisplayed();
    }

    public int getCheapestPrice() {
        return price.stream()
                .mapToInt(e -> Integer.parseInt(e.scrollIntoView(true).getText().replaceAll(",", "")))
                .min()
                .orElseThrow();
    }

    public void scrollUntilAllTicketLoaded() {
        int previousCount = 0;
        int currentCount = price.size();
        while (previousCount != currentCount) {
            previousCount = currentCount;
            price.last().scrollIntoView(true);
            sleep(500); // // Wait for lazy-load
            currentCount = price.size();
        }
    }

    @Step("Select the first cheapest ticket")
    public void selectTheFirstCheapestTicket() {
        scrollUntilAllTicketLoaded();
        String minPriceStr = Common.formatWithCommas(getCheapestPrice());
        SelenideElement ticketPrice = $x(String.format(firstTicketByPrice, minPriceStr));
        ticketPrice.scrollIntoView(true).click();
    }

    @Step("Click on Continue button")
    public void clickContinueButton() {
        getButtonByText((String) YamlUtils.getProperty("button.continue")).click();
    }

    public MonthDay getHighlightedDate() {
        String text = highlightedDate.$$("p").get(1).getText().replaceAll("(\\d+)(st|nd|rd|th)", "$1");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                (String) YamlUtils.getProperty("config.day_month_format"),
                Common.getLocale()
        );
        return MonthDay.parse(text, formatter);
    }

    public String getFromPlace() {
        return $x(String.format(textByLabel, Label.FROM.toString())).getText().split("\\(")[0].trim();
    }

    public String getToPlace() {
        return $x(String.format(textByLabel, Label.TO.toString())).getText().split("\\(")[0].trim();
    }

    private Integer getTotalPassengerByType(Label label) {
        SelenideElement element = $x(String.format(passengerText, label.toString()));
        if (element.exists()) {
            String content = element.getText();
            String regex = "(\\d+)\\s+" + label.toString();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return null;
    }

    public Passenger getPassengerInfo() {
        return Passenger.builder()
                .adultNumber(getTotalPassengerByType(Label.ADULT))
                .childrenNumber(getTotalPassengerByType(Label.CHILDREN))
                .infantNumber(getTotalPassengerByType(Label.INFANT)).build();
    }

    public TicketInfoData getDepartureTicketInfo() {
        return getTicketInfoOfReservation(true);
    }

    public TicketInfoData getReturnTicketInfo() {
        return getTicketInfoOfReservation(false);
    }

    private TicketInfoData getTicketInfoOfReservation(boolean isDepartureFlight) {
        String flight = isDepartureFlight ? (String) YamlUtils.getProperty("title.departure_flight") : (String) YamlUtils.getProperty("title.return_flight");
        String formatter = (String) YamlUtils.getProperty("config.datetime_format");
        ElementsCollection elements = $$x(String.format(flightInfo, flight));

        String fromPlace = elements.get(0).getText().split("\\(")[0].trim();
        String toPlace = elements.get(1).getText().split("\\(")[0].trim();
        String[] info = elements.last().getText().split("\\|");
        LocalDate date = LocalDate.parse(info[0].split(",")[1].trim(), DateTimeFormatter.ofPattern(formatter));
        LocalTime fromTime = LocalTime.parse(info[1].split("-")[0].trim(), TIME_FORMATTER);
        LocalTime toTime = LocalTime.parse(info[1].split("-")[1].trim(), TIME_FORMATTER);
        String flightNo = info[2].trim();
        String ticketType = info[3].trim();

        return TicketInfoData.builder()
                .from(fromPlace)
                .to(toPlace)
                .flightDate(date)
                .fromTime(fromTime)
                .toTime(toTime)
                .flightNo(flightNo)
                .ticketType(ticketType)
                .build();
    }

    private SelenideElement getButtonByText(String text) {
        return $x(String.format(button, text)).shouldBe(Condition.visible);
    }

    private SelenideElement ecoImg = $x("//img[contains(@src,'eco')]");
    private SelenideElement highlightedDate = $x("//div[contains(@class, 'slick-current')]");
    private String firstTicketByPrice = "(//p[contains(@class, 'MuiTypography-h4')][text()='%s'])[1]";
    private ElementsCollection price = $$x("//p[.='000 VND']/preceding-sibling::p");
    private String textByLabel = "//span[.='%s']/following-sibling::span";
    private String passengerText = "//p[@variantmd='h3']//span[contains(.,'%s')]";
    private String button = "//button[contains(@class, 'MuiButtonBase-root') and span[.=\"%s\"]]";
    private String flightInfo = "//div[p[.='%s']]/following-sibling::div//h5";

}
