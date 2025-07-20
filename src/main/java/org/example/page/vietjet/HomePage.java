package org.example.page.vietjet;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.data.vietjet.Passenger;
import org.example.data.vietjet.SearchTicketData;
import org.example.enumData.vietjet.Label;
import org.example.utils.Common;
import org.example.utils.Constants;
import org.example.utils.YamlUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;

public class HomePage {

    @Step("Accept cookies")
    public void acceptCookies() {
        acceptCookiesButton.click();
    }

    @Step("Close the notification alert ")
    public void clickNotNow() {
        switchTo().frame(iframe);
        notNowButton.shouldBe(Condition.visible).click();
        switchTo().defaultContent();
    }

    public void fillTicketInfo(SearchTicketData searchTicketData) {
        if (Objects.nonNull(searchTicketData.getFrom())) {
            String from = searchAndGetTheFirstResult(Label.FROM, searchTicketData.getFrom());
            searchTicketData.setFrom(from);
        }
        if (Objects.nonNull(searchTicketData.getTo())) {
            String to = searchAndGetTheFirstResult(Label.TO, searchTicketData.getTo());
            searchTicketData.setTo(to);
        }
        if (Objects.nonNull(searchTicketData.getDepartureDate())) {
            selectDatePicker(searchTicketData.getDepartureDate());
        }
        if (Objects.nonNull(searchTicketData.getReturnDate())) {
            selectDatePicker(searchTicketData.getReturnDate());
        }
        if (Objects.nonNull(searchTicketData.getPassenger())) {
            Passenger passenger = searchTicketData.getPassenger();
            if (Objects.nonNull(passenger.getAdultNumber())) {
                selectNumberOfAdults(passenger.getAdultNumber());
            }
            if (Objects.nonNull(passenger.getChildrenNumber())) {
                selectNumberOfChildren(passenger.getChildrenNumber());
            }
            if (Objects.nonNull(passenger.getInfantNumber())) {
                selectNumberOfInfants(passenger.getInfantNumber());
            }
        }
    }

    public void searchTicket(SearchTicketData searchTicketData) {
        fillTicketInfo(searchTicketData);
        clickSearchButton();
        waitForPriceListLoading();
    }

    @Step("Search and select the first result: {0} {1}")
    public String searchAndGetTheFirstResult(Label searchField, String searchKeyword) {
        getTextboxByLabel(searchField).setValue(searchKeyword).click();
        SelenideElement element = firstPlaceOptions.$$x(".//div[text()]").first();
        String place = element.getText();
        element.shouldBe(Condition.visible).scrollIntoView(false).click();
        return place;
    }

    @Step("Select data: {0}")
    public void selectDatePicker(LocalDate localDate) {
        getDate(localDate).click();
    }

    @Step("Select number of adults: {0}")
    public void selectNumberOfAdults(int targetNumber) {
        int currentNumber = Integer.parseInt(adultsValue.getText());
        SelenideElement buttonToClick = currentNumber < targetNumber ? plusAdultsButton : minusAdultsButton;
        for (int i = 0; i < Math.abs(targetNumber - currentNumber); i++) {
            buttonToClick.click();
        }
    }

    @Step("Select number of adults: {0}")
    public void selectNumberOfChildren(int targetNumber) {
        int currentNumber = Integer.parseInt(childrenValue.getText());
        SelenideElement buttonToClick = currentNumber < targetNumber ? plusChildrenButton : minusChildrenButton;
        for (int i = 0; i < Math.abs(targetNumber - currentNumber); i++) {
            buttonToClick.click();
        }
    }

    @Step("Select number of adults: {0}")
    public void selectNumberOfInfants(int targetNumber) {
        int currentNumber = Integer.parseInt(babyValue.getText());
        SelenideElement buttonToClick = currentNumber < targetNumber ? plusBabyButton : minusBabyButton;
        for (int i = 0; i < Math.abs(targetNumber - currentNumber); i++) {
            buttonToClick.click();
        }
    }

    @Step("Click on Search button")
    public void clickSearchButton() {
        getButtonByText((String) YamlUtils.getProperty("button.letgo")).click();
    }

    public void waitForPriceListLoading() {
        ecoImg.shouldBe(Condition.exist, Constants.MEDIUM_TIMEOUT);
        if (closeButton.should(Condition.exist, Constants.MEDIUM_TIMEOUT).exists()) {
            closeButton.click();
        }
    }

    private SelenideElement getTextboxByLabel(Label label) {
        return $x(String.format("//div[label[.='%s']]//input", label.toString()))
                .shouldBe(Condition.visible);
    }

    private SelenideElement getDate(LocalDate localDate) {
        String pattern = (String) YamlUtils.getProperty("config.month_year_format");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Common.getLocale());
        String monthYear = localDate.format(formatter);
        return $x(String.format(date, monthYear, localDate.getDayOfMonth())).shouldBe(Condition.visible);
    }

    private SelenideElement getButtonByText(String text) {
        return $x(String.format(button, text)).shouldBe(Condition.visible);
    }

    private SelenideElement ecoImg = $x("//img[contains(@src,'eco')]");
    private SelenideElement closeButton = $("button[aria-label = 'close']");
    private SelenideElement acceptCookiesButton = $x("//button[contains(@class,'MuiButton-root')]//h5");
    private SelenideElement iframe = $("#preview-notification-frame");
    private SelenideElement notNowButton = $("#NC_CTA_TWO");
    private SelenideElement adultsValue = $x("//div[div/img[contains(@alt,'adults')]]/following-sibling::div/span");
    private SelenideElement plusAdultsButton = $x("//div[div/img[contains(@alt,'adults')]]/following-sibling::div//button[2]");
    private SelenideElement minusAdultsButton = $x("//div[div/img[contains(@alt,'adults')]]/following-sibling::div//button[1]");
    private SelenideElement childrenValue = $x("//div[div/img[contains(@alt,'adults')]]/following-sibling::div/span");
    private SelenideElement plusChildrenButton = $x("//div[div/img[contains(@alt,'adults')]]/following-sibling::div//button[2]");
    private SelenideElement minusChildrenButton = $x("//div[div/img[contains(@alt,'adults')]]/following-sibling::div//button[1]");
    private SelenideElement babyValue = $x("//div[div/img[contains(@alt,'baby')]]/following-sibling::div/span");
    private SelenideElement plusBabyButton = $x("//div[div/img[contains(@alt,'baby')]]/following-sibling::div//button[2]");
    private SelenideElement minusBabyButton = $x("//div[div/img[contains(@alt,'baby')]]/following-sibling::div//button[1]");
    private SelenideElement firstPlaceOptions = $x("(//div[contains(@class, 'MuiExpansionPanelDetails-root')]//div[contains(@class, 'MuiBox-root')])[1]");
    private String date = "(//div[div[@class='rdrMonthName' and .='%s']]//button[not(contains(@class,'rdrDayPassive'))]//span[.='%d'])[1]";
    private String button = "//div[contains(@class, 'MuiPaper-root')]//button[span[.=\"%s\"]]";
}
