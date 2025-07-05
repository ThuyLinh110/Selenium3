package org.example.page.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import org.example.data.agoda.ReviewPointData;
import org.example.enumData.ReviewType;
import org.example.utils.Constants;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.*;

public class HotelDetailPage {


    /**
     * Get hotel name
     */
    public String getHotelName() {
        return hotelName.getText().trim();
    }

    /**
     * Get hotel address
     */
    public String getAddress() {
        return hotelAddress.getText().split("-")[0].trim();
    }

    public List<String> getSpecialBenefitList() {
        return specialBenefits.texts();
    }

    @Step("Hover on show review button of hotel")
    public void hoverOnShowReviewButton() {
        showReviewTooltipButton.shouldBe(Condition.visible, Constants.MEDIUM_TIMEOUT).scrollIntoView(false).hover();
        reviewTooltip.shouldBe(Condition.visible, Constants.MEDIUM_TIMEOUT);
    }

    public List<ReviewPointData> getListReviewPointOfHotel() {
        hoverOnShowReviewButton();
        return IntStream.range(0, reviewType.size())
                .mapToObj(i -> ReviewPointData.builder()
                        .reviewType(ReviewType.fromText(reviewType.get(i).getText()))
                        .point(Float.parseFloat(reviewPoint.get(i).getText()))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Check if hotel address in details page match with address in result page
     */
    public boolean isHotelAddressMatched(String addressInDetailsPage, String addressInResultPage) {
        if (!addressInDetailsPage.contains(addressInResultPage)) {
            Allure.step(String.format("Address in details page '%s' does not contain '%s'", addressInDetailsPage, addressInResultPage), Status.FAILED);
            return false;
        }
        return true;
    }

    private SelenideElement hotelName = $("h1[data-selenium='hotel-header-name']");
    private SelenideElement hotelAddress = $("span[data-selenium='hotel-address-map']");
    private ElementsCollection specialBenefits = $$("div[data-element-value='available']");
    private SelenideElement showReviewTooltipButton = $("button[data-testid='review-tooltip-icon']");
    private SelenideElement reviewTooltip = $("div[data-testid='floater-container']");
    private ElementsCollection reviewType = $$x("//div[contains(@class,'Review-travelerGrade-Cell')]//span");
    private ElementsCollection reviewPoint = $$x("//div[contains(@class,'Review-travelerGrade-Cell')]//p");

}