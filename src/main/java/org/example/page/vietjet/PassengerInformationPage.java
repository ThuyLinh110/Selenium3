package org.example.page.vietjet;

import org.example.utils.YamlUtils;

import static com.codeborne.selenide.Selenide.$x;

public class PassengerInformationPage extends SelectFlightPage {

    public boolean isPageDisplayed() {
        return $x(String.format(text, YamlUtils.getProperty("title.passenger_info"))).isDisplayed();
    }

    private String text = "//h3[.='%s']";

}
