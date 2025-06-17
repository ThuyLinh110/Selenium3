package org.example.page.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import org.example.data.agoda.HotelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

public class SearchResultPage {

    /**
     * Get hotel data in result page
     */
    public List<HotelData> getHotelData(Integer hotelNumber) {
        List<HotelData> hotelData = new ArrayList<>();
        List<Integer> priceList = getPriceList(hotelNumber);
        List<String> destinationList = getDestinationList(hotelNumber);
        List<Float> starList = getRatingList(hotelNumber);
        List<String> hotelNameList = getHotelNameList(hotelNumber);
        for (int i = 0; i < priceList.size(); i++) {
            hotelData.add(HotelData.builder()
                    .address(destinationList.get(i))
                    .price(priceList.get(i))
                    .star(starList.get(i))
                    .hotelName(hotelNameList.get(i))
                    .build());
        }
        return hotelData;
    }

    /**
     * Scroll page until number hotels are loaded as expected
     * @param numberHotel
     */
    @Step("Scroll until {numberHotel} hotels loaded data")
    public void scrollUntilAllHotelDataLoaded(int numberHotel) {
        while (true) {
            if (price.size() >= numberHotel) {
                break;
            }
            hotelImg.stream().allMatch(e -> e.shouldBe(Condition.visible).is(Condition.image));
            price.last().shouldBe(Condition.visible).scrollIntoView(true);
        }
        header.scrollIntoView(true);
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
                Allure.step(String.format("Page only show %d instead of %d result(s). Scroll down for more results", priceList.size(), hotelNumber), Status.FAILED);
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
                Allure.step(String.format("Page only show %d instead of %d result(s). Scroll down or try with another filter for more results", destinationList.size(), hotelNumber), Status.FAILED);
                return destinationList;
            } else {
                destinationList = destination.texts().subList(0, hotelNumber);
            }
        }
        return destinationList;
    }

    /**
     * Get all rating
     *
     * @param hotelNumber Number of hotel need to get
     * @return List of all rating
     */
    public List<Float> getRatingList(Integer hotelNumber) {
        List<String> ratingList = rating.texts();
        if (Objects.nonNull(hotelNumber)) {
            if (hotelNumber > ratingList.size()) {
                Allure.step(String.format("Page only show %d instead of %d result(s). Scroll down for more results", ratingList.size(), hotelNumber), Status.FAILED);
            } else {
                ratingList = rating.texts().subList(0, hotelNumber);
            }
        }
        return ratingList.stream()
                .map(text -> Float.parseFloat(text.split(" ")[0]))
                .collect(Collectors.toList());
    }

    /**
     * Get all the hotel name
     *
     * @param hotelNumber Number of hotel need to get
     * @return List of all hotel name
     */
    public List<String> getHotelNameList(Integer hotelNumber) {
        List<String> hotelNameList = hotelName.stream().map(e -> e.getText().trim()).collect(Collectors.toList());
        if (Objects.nonNull(hotelNumber)) {
            if (hotelNumber > hotelNameList.size()) {
                Allure.step(String.format("Page only show %d instead of %d result(s). Scroll down or try with another filter for more results", hotelNameList.size(), hotelNumber), Status.FAILED);
            } else {
                hotelNameList = hotelNameList.subList(0, hotelNumber);
            }
        }
        return hotelNameList;
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
    }

    private ElementsCollection destination = $$x("//li[@data-selenium='hotel-item']//div[@data-selenium='area-city']");
    private ElementsCollection hotelName = $$x("//h3[@data-selenium='hotel-name']");
    private ElementsCollection price = $$x("//div[@data-element-name='final-price']");
    private ElementsCollection rating = $$x("//div[@data-testid='rating-container']//span");
    private ElementsCollection hotelImg = $$x("//li[@data-selenium='hotel-item']//button[@data-element-name='ssrweb-mainphoto']/img");
    private SelenideElement lowestPriceButton = $x("//button[@data-element-name='search-sort-price']");
    private SelenideElement header = $("header");

}
