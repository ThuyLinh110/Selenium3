package org.example.page.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.example.data.agoda.SearchHotelData;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

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
                    selectNumberOfAdults(searchHotelData.getOccupancy().getChildren());
                }
            }
        }
    }

    public void searchAndGetTheFirstPlace(String place) {
        placeTextBox.setValue(place);
        firstPlaceResult.click();
    }

    public void selectDate(LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        getSelectDate(dateStr).click();
    }

    public void selectNumberOfRooms(int targetNumber) {
        int currentNumber = Integer.parseInt(roomValue.getText());
        SelenideElement buttonToClick = currentNumber < targetNumber ? plusRoomButton : minusRoomButton;
        for (int i = 0; i < Math.abs(targetNumber - currentNumber); i++) {
            buttonToClick.click();
        }
    }

    public void selectNumberOfAdults(int targetNumber) {
        int currentNumber = Integer.parseInt(adultsValue.getText());
        SelenideElement buttonToClick = currentNumber < targetNumber ? plusAdultsButton : minusAdultsButton;
        for (int i = 0; i < Math.abs(targetNumber - currentNumber); i++) {
            buttonToClick.click();
        }
    }

    public void selectNumberOfChildren(int targetNumber) {
        int currentNumber = Integer.parseInt(childrenValue.getText());
        SelenideElement buttonToClick = currentNumber < targetNumber ? plusChildrenButton : minusChildrenButton;
        for (int i = 0; i < Math.abs(targetNumber - currentNumber); i++) {
            buttonToClick.click();
        }
    }

    private SelenideElement getSelectDate(String dateStr) {
        return $x(String.format("//span[@data-selenium-date='%s']", dateStr))
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
}
