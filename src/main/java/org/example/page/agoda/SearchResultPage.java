package org.example.page.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import org.example.data.agoda.FilterHotelData;
import org.example.enumData.FilterOption;
import org.example.enumData.FilterType;
import org.example.utils.Common;
import org.example.utils.Constants;
import org.example.utils.YamlUtils;

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
            waitForHotelImgLoading();
            price.last().shouldBe(Condition.visible, Constants.MEDIUM_TIMEOUT).scrollIntoView(true);
        }
        header.scrollIntoView(true);
        waitForHotelImgLoading();
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
     * Add commentMore actions
     * Get all rating
     *
     * @param hotelNumber Number of hotel need to get
     * @return List of all rating
     */
    public List<Float> getRatingList(Integer hotelNumber) {
        List<String> ratingList = rating.texts();
        if (Objects.nonNull(hotelNumber)) {
            if (hotelNumber > ratingList.size()) {
                String errorMessage = String.format("Page only show %d instead of %d result(s). Scroll down for more results", rating.size(), hotelNumber);
                Allure.step(String.format(errorMessage), Status.FAILED);
                throw new IllegalStateException(errorMessage);
            } else {
                ratingList = rating.texts().subList(0, hotelNumber);
            }
        }
        return ratingList.stream()
                .map(text -> Float.parseFloat(text.split(" ")[0]))
                .collect(Collectors.toList());
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

    /**
     * Are all the price in the selected price range
     *
     * @param hotelNumber Number of hotel need to check
     * @param min         The min price
     * @param max         The max price
     * @return
     */
    public boolean areAllPriceInSelectedRange(Integer hotelNumber, int min, int max) {
        List<Integer> priceList = getPriceList(hotelNumber);
        if (Objects.isNull(priceList)) {
            Allure.step("No result found", Status.FAILED);
            return false;
        }
        for (Integer price : priceList) {
            if (price < min || price > max) {
                Allure.step(String.format("Price '%d' does not in the range '%d' - '%d'", price, min, max), Status.FAILED);
                return false;
            }
        }
        return true;
    }

    /**
     * Are all the rating stars matched with selected rating
     *
     * @param hotelNumber Number of hotel need to check
     * @param starRating  The selected rating
     * @return
     */
    public boolean areAllStarsMatchWithSelectedRating(Integer hotelNumber, FilterOption starRating) {
        List<Float> stars = getRatingList(hotelNumber);
        if (Objects.isNull(stars)) {
            Allure.step("No result found", Status.FAILED);
            return false;
        }
        for (Float star : stars) {
            if (star != starRating.getValue()) {
                Allure.step(String.format("Star '%f' does not matched with selected rating '%d' ", star, rating), Status.FAILED);
                return false;
            }
        }
        return true;
    }

    @Step("Click 'Lowes price first' button")
    public void clickLowestPriceFirstButton() {
        lowestPriceButton.click();
        waitForHotelImgLoading();
    }

    /**
     * Filter hotel with info
     *
     * @param filterHotelData
     */
    public void filterHotel(FilterHotelData filterHotelData) {
        filterByPriceRange(filterHotelData.getMinPrice(), filterHotelData.getMaxPrice(), false);
        for (FilterHotelData.Filter filter : filterHotelData.getFilter()) {
            filterByOption(filter.getFilterType(), filter.getFilterOption(), true);
        }
    }

    @Step("Filter by Price Range: {0} - {1}")
    public void filterByPriceRange(Integer minPrice, Integer maxPrice, boolean isWaitDataLoading) {
        minPriceTextBox.setValue(minPrice.toString()).pressEnter();
        maxPriceTextBox.setValue(maxPrice.toString()).pressEnter();
        if (isWaitDataLoading) {
            waitForHotelImgLoading();
        }
    }

    @Step("Filter by type: {0}, option: {1}")
    public void filterByOption(FilterType filterType, FilterOption filterOption, boolean isWaitDataLoading) {
        getCheckboxByFilterTypeAndOption(filterType, filterOption).click();
        if (isWaitDataLoading) {
            waitForHotelImgLoading();
        }
    }

    public void waitForHotelImgLoading() {
        hotelImg.stream().allMatch(e -> e.shouldBe(Condition.visible, Constants.MEDIUM_TIMEOUT).is(Condition.image, Constants.MEDIUM_TIMEOUT));
    }

    public boolean areAllSelectedFilterHighlighted(FilterHotelData filterHotelData) {
        List<Integer> optionValue = filterHotelData.getFilter()
                .stream()
                .map(f -> f.getFilterOption().getValue())
                .collect(Collectors.toList());

        return optionValue.stream().allMatch(value -> getSelectedCheckboxByValue(value).isSelected())
                && getMinPriceSlider() == filterHotelData.getMinPrice()
                && getMaxPriceSlider() == filterHotelData.getMaxPrice();
    }

    public int getMinPriceSlider() {
        String min = (String) YamlUtils.getProperty("slider.min");
        return Common.convertMoneyToNumber(getPriceSlider(min).getAttribute("aria-valuetext"));
    }

    public int getMaxPriceSlider() {
        String max = (String) YamlUtils.getProperty("slider.max");
        return Common.convertMoneyToNumber(getPriceSlider(max).getAttribute("aria-valuetext"));
    }

    public int getMinPrice() {
        return Common.convertMoneyToNumber(minPriceTextBox.getValue());
    }

    public int getMaxPrice() {
        return Common.convertMoneyToNumber(maxPriceTextBox.getValue());
    }

    private SelenideElement getCheckboxByFilterTypeAndOption(FilterType filterType, FilterOption filterOption) {
        return $x(String.format("//legend[contains(., '%s')]/..//span[.= '%s']//ancestor::label//input", filterType.toString(), filterOption.toString()))
                .shouldBe(Condition.exist);
    }

    private SelenideElement getPriceSlider(String text) {
        return $x(String.format("//div[@id='SideBarLocationFilters']//div[@aria-label='%s']", text))
                .shouldBe(Condition.visible);
    }

    private SelenideElement getSelectedCheckboxByValue(int dataValue) {
        return $x(String.format("//div[@id='SideBarLocationFilters']//legend[@id='filter-menu-RecentFilters']//following-sibling::ul//li//label[@data-element-value='%d']//input", dataValue))
                .shouldBe(Condition.exist);
    }

    private ElementsCollection destination = $$x("//li[@data-selenium='hotel-item']//div[@data-selenium='area-city']");
    private ElementsCollection price = $$x("//div[@data-element-name='final-price']");
    private ElementsCollection hotelImg = $$x("//li[@data-selenium='hotel-item']//button[@data-element-name='ssrweb-mainphoto']/img");
    private SelenideElement lowestPriceButton = $x("//button[@data-element-name='search-sort-price']");
    private SelenideElement header = $("header");
    private ElementsCollection rating = $$x("//div[@data-testid='rating-container']//span");
    private SelenideElement minPriceTextBox = $("#price_box_0");
    private SelenideElement maxPriceTextBox = $("#price_box_1");

}