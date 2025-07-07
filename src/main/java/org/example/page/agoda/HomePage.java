package org.example.page.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.control.CalendarControl;
import org.example.data.agoda.SearchHotelData;
import org.example.utils.YamlUtils;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;

public class HomePage {

    /**
     * Filter hotel with info
     *
     * @param searchHotelData
     */
    public void fillHotelInfo(SearchHotelData searchHotelData) {
        if (!searchHotelData.isDayUseStay()) {
            if (Objects.nonNull(searchHotelData.getPlace())) {
                searchAndGetTheFirstPlace(searchHotelData.getPlace());
            }
            if (Objects.nonNull(searchHotelData.getFromDate())) {
                selectDate(searchHotelData.getFromDate());
            }
            if (Objects.nonNull(searchHotelData.getToDate())) {
                selectDate(searchHotelData.getToDate());
            }
            if (Objects.nonNull(searchHotelData.getOccupancy())) {
                if (Objects.nonNull(searchHotelData.getOccupancy().getRoom())) {
                    selectNumberOfRooms(searchHotelData.getOccupancy().getRoom());
                }
                if (Objects.nonNull(searchHotelData.getOccupancy().getAdults())) {
                    selectNumberOfAdults(searchHotelData.getOccupancy().getAdults());
                }
                if (Objects.nonNull(searchHotelData.getOccupancy().getChildren())) {
                    selectNumberOfChildren(searchHotelData.getOccupancy().getChildren());
                }
            }
        }
    }

    public void searchHotel(SearchHotelData searchHotelData) {
        fillHotelInfo(searchHotelData);
        clickSearchButton();
        Selenide.switchTo().window(1);
    }

    @Step("Search place: {place}")
    public void searchAndGetTheFirstPlace(String place) {
        placeTextBox.setValue(place);
        firstPlaceResult.shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
    }

    @Step("Select date: {date}")
    public void selectDate(LocalDate date) {
        calendarControl.selectDate(date);
    }

    @Step("Select number of rooms: {targetNumber}")
    public void selectNumberOfRooms(int targetNumber) {
        int currentNumber = Integer.parseInt(roomValue.getText());
        SelenideElement buttonToClick = currentNumber < targetNumber ? plusRoomButton : minusRoomButton;
        for (int i = 0; i < Math.abs(targetNumber - currentNumber); i++) {
            buttonToClick.click();
        }
    }

    @Step("Select number of adults: {targetNumber}")
    public void selectNumberOfAdults(int targetNumber) {
        int currentNumber = Integer.parseInt(adultsValue.getText());
        SelenideElement buttonToClick = currentNumber < targetNumber ? plusAdultsButton : minusAdultsButton;
        for (int i = 0; i < Math.abs(targetNumber - currentNumber); i++) {
            buttonToClick.click();
        }
    }

    @Step("Select number of children: {targetNumber}")
    public void selectNumberOfChildren(int targetNumber) {
        int currentNumber = Integer.parseInt(childrenValue.getText());
        SelenideElement buttonToClick = currentNumber < targetNumber ? plusChildrenButton : minusChildrenButton;
        for (int i = 0; i < Math.abs(targetNumber - currentNumber); i++) {
            buttonToClick.click();
        }
    }

    @Step("Close the advertisement popup")
    public void closeAds() {
        adsCloseButton.shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
    }

    @Step("Click Search button")
    public void clickSearchButton() {
        getButton((String) YamlUtils.getProperty("button.search")).click();
    }

    private SelenideElement getButton(String text) {
        return $x(String.format("//span[.='%s']", text))
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled);
    }

    private SelenideElement placeTextBox = $(By.id("textInput"));
    private SelenideElement firstPlaceResult = $x("//div[contains(@class, 'Popup__content')]/ul/li[1]");
    private SelenideElement plusRoomButton = $x("//button[@data-element-name='occupancy-selector-panel-rooms' and @data-selenium='plus']");
    private SelenideElement minusRoomButton = $x("//button[@data-element-name='occupancy-selector-panel-rooms' and @data-selenium='minus']");
    private SelenideElement roomValue = $x("//div[@data-component='desktop-occ-room-value']//p");
    private SelenideElement plusAdultsButton = $x("//button[@data-element-name='occupancy-selector-panel-adult' and @data-selenium='plus']");
    private SelenideElement minusAdultsButton = $x("//button[@data-element-name='occupancy-selector-panel-adult' and @data-selenium='minus']");
    private SelenideElement adultsValue = $x("//div[@data-component='desktop-occ-adult-value']//p");
    private SelenideElement plusChildrenButton = $x("//button[@data-element-name='occupancy-selector-panel-children' and @data-selenium='plus']");
    private SelenideElement minusChildrenButton = $x("//button[@data-element-name='occupancy-selector-panel-children' and @data-selenium='minus']");
    private SelenideElement childrenValue = $x("//div[@data-component='desktop-occ-children-value']//p");
    private SelenideElement adsCloseButton = $x("//button[@data-element-name='prominent-app-download-floating-button']");
    private ElementsCollection calendarDate = $$(By.cssSelector("[data-selenium-date]"));
    private SelenideElement previousMonthButton = $x("//button[@aria-label='Previous Month']");
    private SelenideElement nextMonthButton = $x("//button[@aria-label='Next Month']");
    private CalendarControl calendarControl = new CalendarControl(previousMonthButton, nextMonthButton, calendarDate);

}
