package org.example.page.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.example.data.agoda.HotelData;
import org.example.enumData.ReviewType;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

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

    public HotelData getHotelData() {
        return HotelData.builder()
                .hotelName(getHotelName())
                .address(getAddress())
                .build();
    }

    public void hoverOnReviewDetailButton() {
        reviewDetailButton.shouldBe(Condition.visible).hover();
    }

    public List<ReviewType> getListReviewType() {

    }


    private SelenideElement hotelName = $x("//h1[@data-selenium='hotel-header-name']");
    private SelenideElement hotelAddress = $x("//span[@data-selenium='hotel-address-map']");
//    private ElementsCollection propertyList = $$x("//h5[.='The property']/following-sibling::ul//li");
    private SelenideElement reviewDetailButton = $x("//button[@data-testid='review-tooltip-icon']");

}
