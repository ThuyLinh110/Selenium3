package org.example.page.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import org.example.utils.Constants;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

public class SearchResultPage {

    /**
     * Scroll page until number hotels are loaded as expected
     *
     * @param numberHotel
     */
    @Step("Scroll until {numberHotel} hotels loaded data")
    public void scrollUntilAllHotelDataLoaded(int numberHotel) {
        while (true) {
            if (price.size() >= numberHotel) {
                break;
            }
            hotelImg.stream().allMatch(e -> e.shouldBe(Condition.visible, Constants.MEDIUM_TIMEOUT).is(Condition.image, Constants.MEDIUM_TIMEOUT));
            price.last().shouldBe(Condition.visible, Constants.MEDIUM_TIMEOUT).scrollIntoView(true);
        }
        header.scrollIntoView(true);
        hotelImg.stream().allMatch(e -> e.shouldBe(Condition.visible, Constants.MEDIUM_TIMEOUT).is(Condition.image, Constants.MEDIUM_TIMEOUT));
    }

    /**
     * Get all the hotel prices
     *
     * @param hotelNumber Number of hotel need to get
     * @return List of all prices
     */
    public List<Integer> getPriceList(Integer hotelNumber) {
        List<String> priceList = price.texts();
        if (Objects.nonNull(hotelNumber)) {
            if (hotelNumber > priceList.size()) {
                String errorMessage = String.format("Page only show %d instead of %d result(s). Scroll down for more results", priceList.size(), hotelNumber);
                Allure.step(String.format(errorMessage), Status.FAILED);
                throw new IllegalStateException(errorMessage);
            } else {
                priceList = price.texts().subList(0, hotelNumber);
            }
        }
        return priceList.stream()
                .map(e -> e.replaceAll("[^0-9]", "")) // Keep only digits
                .map(Integer::parseInt) // Parse to Float
                .collect(Collectors.toList());
    }

    /**
     * Get all the hotel area city
     *
     * @param hotelNumber Number of hotel need to get
     * @return List of all area city
     */
    public List<String> getDestinationList(Integer hotelNumber) {
        List<String> destinationList = destination.texts();
        if (Objects.nonNull(hotelNumber)) {
            if (hotelNumber > destinationList.size()) {
                String errorMessage = String.format("Page only show %d instead of %d result(s). Scroll down for more results", destinationList.size(), hotelNumber);
                Allure.step(String.format(errorMessage), Status.FAILED);
                throw new IllegalStateException(errorMessage);
            } else {
                destinationList = destination.texts().subList(0, hotelNumber);
            }
        }
        return destinationList;
    }

    /**
     * Are all the destinations have search content
     *
     * @param destinationNumber Number of destinations need to check
     * @param place             Place name
     * @return
     */
    public boolean areAllTheDestinationsHaveSearchContent(Integer destinationNumber, String place) {
        List<String> destinationList = getDestinationList(destinationNumber);
        if (Objects.isNull(destinationList)) {
            Allure.step("No result found", Status.FAILED);
            return false;
        }
        for (String str : destinationList) {
            String lowerStr = str.toLowerCase();
            String lowerPlace = place.toLowerCase();
            if (!lowerStr.contains(lowerPlace) && !lowerStr.contains(String.join("", lowerPlace.split(" ")))) {
                Allure.step(String.format("Destination '%s' does not contain '%s'", str, place), Status.FAILED);
                return false;
            }
        }
        return true;
    }

    @Step("Click 'Lowes price first' button")
    public void clickLowestPriceFirstButton() {
        lowestPriceButton.click();
        hotelImg.stream().allMatch(e -> e.shouldBe(Condition.visible, Constants.MEDIUM_TIMEOUT).is(Condition.image, Constants.MEDIUM_TIMEOUT));
    }

    private ElementsCollection destination = $$x("//li[@data-selenium='hotel-item']//div[@data-selenium='area-city']");
    private ElementsCollection price = $$x("//div[@data-element-name='final-price']");
    private ElementsCollection hotelImg = $$x("//li[@data-selenium='hotel-item']//button[@data-element-name='ssrweb-mainphoto']/img");
    private SelenideElement lowestPriceButton = $x("//button[@data-element-name='search-sort-price']");
    private SelenideElement header = $("header");
    private SelenideElement loadingItem = $x("//div[@data-selenium='loading-item']");


}