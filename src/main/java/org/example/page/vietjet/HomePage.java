package org.example.page.vietjet;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.data.vietjet.SearchTicketData;
import org.example.enumData.vietjet.Label;
import org.example.utils.WebUtils;
import org.example.utils.YamlUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;

public class HomePage {

    @Step("Accept cookies")
    public void acceptCookies() {
        acceptCookiesButton.click();
    }

    @Step("Close the notification alert ")
    public void clickNotNow() {
        WebUtils.switchToFrame(iframe);
        notNowButton.shouldBe(Condition.visible).click();
        WebUtils.switchToMain();
    }

    public void fillTicketInfo(SearchTicketData searchTicketData) {
        if (Objects.nonNull(searchTicketData.getFrom())) {
            searchAndSelectTheFirstResult(Label.FROM, searchTicketData.getFrom());
        }
        if (Objects.nonNull(searchTicketData.getTo())) {
            searchAndSelectTheFirstResult(Label.TO, searchTicketData.getTo());
        }
        if (Objects.nonNull(searchTicketData.getDepartureDate())) {
            selectDatePicker(searchTicketData.getDepartureDate());
        }
        if (Objects.nonNull(searchTicketData.getReturnDate())) {
            selectDatePicker(searchTicketData.getReturnDate());
        }
        if (Objects.nonNull(searchTicketData.getPassenger())) {
            SearchTicketData.Passenger passenger = searchTicketData.getPassenger();
            if (Objects.nonNull(passenger.getAdultNumber())) {
                selectNumberOfAdults(passenger.getAdultNumber());
            }
            if (Objects.nonNull(passenger.getChildren())) {
                selectNumberOfChildren(passenger.getChildren());
            }
            if (Objects.nonNull(passenger.getInfants())) {
                selectNumberOfInfants(passenger.getInfants());
            }
            getTextboxByLabel(Label.PASSENGER).click();
        }
    }

    public void searchTicket(SearchTicketData searchTicketData) {
        fillTicketInfo(searchTicketData);
        clickSearchButton();
    }

    @Step("Search and select the first result: {0} {1}")
    public void searchAndSelectTheFirstResult(Label searchField, String searchKeyword) {
        getTextboxByLabel(searchField).setValue(searchKeyword).click();
        placeOptions.first().shouldBe(Condition.visible).scrollIntoView(false).click();
    }

    @Step("Select data: {0}")
    public void selectDatePicker(LocalDate localDate) {
        String country = (String) YamlUtils.getProperty("config.country");
        String language = (String) YamlUtils.getProperty("config.language");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale.Builder()
                .setLanguage(language)
                .setRegion(country)
                .build());
        getDate(localDate, formatter).click();
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

    private SelenideElement getTextboxByLabel(Label label) {
        return $x(String.format("//div[label[.='%s']]//input", label.toString()))
                .shouldBe(Condition.visible);
    }

    private SelenideElement getDate(LocalDate localDate, DateTimeFormatter formatter) {
        String monthYear = localDate.format(formatter);
        return $x(String.format(date, monthYear, localDate.getDayOfMonth())).shouldBe(Condition.visible);
    }

    private SelenideElement getButtonByText(String text) {
        return $x(String.format(button, text)).shouldBe(Condition.visible);
    }

    private SelenideElement acceptCookiesButton = $x("//button[contains(@class,'MuiButton-root')]//h5");
    private SelenideElement iframe = $("#preview-notification-frame");
    private SelenideElement notNowButton = $("#NC_CTA_TWO");
    private SelenideElement roundTripRadio = $("input[value='roundTrip']");
    private SelenideElement adultsValue = $x("//div[div[img[contains(@alt,'adults')]]]/following-sibling::div/span");
    private SelenideElement plusAdultsButton = $x("//div[div[img[contains(@alt,'adults')]]]/following-sibling::div//button[2]");
    private SelenideElement minusAdultsButton = $x("//div[div[img[contains(@alt,'adults')]]]/following-sibling::div//button[1]");
    private SelenideElement childrenValue = $x("//div[div[img[contains(@alt,'adults')]]]/following-sibling::div/span");
    private SelenideElement plusChildrenButton = $x("//div[div[img[contains(@alt,'adults')]]]/following-sibling::div//button[2]");
    private SelenideElement minusChildrenButton = $x("//div[div[img[contains(@alt,'adults')]]]/following-sibling::div//button[1]");
    private SelenideElement babyValue = $x("//div[div[img[contains(@alt,'baby')]]]/following-sibling::div/span");
    private SelenideElement plusBabyButton = $x("//div[div[img[contains(@alt,'baby')]]]/following-sibling::div//button[2]");
    private SelenideElement minusBabyButton = $x("//div[div[img[contains(@alt,'baby')]]]/following-sibling::div//button[1]");
    private ElementsCollection placeOptions = $$x("//div[contains(@class, 'MuiExpansionPanelDetails-root')]//div[contains(@class, 'MuiBox-root')]");
    private String date = "(//div[div[@class='rdrMonthName' and .='%s']]//button[not(contains(@class,'rdrDayPassive'))]//span[.='%d'])[1]";
    private String button = "//button[span[.=\"%s\"]]";
}
