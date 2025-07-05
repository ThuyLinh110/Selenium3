package org.example.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.$x;

public class CalendarControl {
    private SelenideElement previousMonthButton;
    private SelenideElement nextMonthButton;
    private ElementsCollection calendarDate;

    public CalendarControl(SelenideElement prev, SelenideElement next, ElementsCollection dates) {
        this.previousMonthButton = prev;
        this.nextMonthButton = next;
        this.calendarDate = dates;
    }

    public void selectDate(LocalDate date) {
        while (!isDateVisible(date)) {
            navigateToMonth(date);
        }
        getSelectDate(date).click();
    }

    private boolean isDateVisible(LocalDate date) {
        LocalDate minDate = LocalDate.parse(calendarDate.first().getAttribute("data-selenium-date"));
        LocalDate maxDate = LocalDate.parse(calendarDate.last().getAttribute("data-selenium-date"));
        return !(date.isBefore(minDate) || date.isAfter(maxDate));
    }

    private void navigateToMonth(LocalDate targetDate) {
        LocalDate minDate = LocalDate.parse(calendarDate.first().getAttribute("data-selenium-date"));
        LocalDate maxDate = LocalDate.parse(calendarDate.last().getAttribute("data-selenium-date"));
        if (targetDate.isBefore(minDate)) {
            previousMonthButton.click();
        }
        if (targetDate.isAfter(maxDate)) {
            nextMonthButton.click();
        }
    }

    private SelenideElement getSelectDate(LocalDate date) {
        return $x(String.format("//span[@data-selenium-date='%s']", date.toString()))
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled);
    }

}
