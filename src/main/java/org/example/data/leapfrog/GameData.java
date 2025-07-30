package org.example.data.leapfrog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class GameData {
    private String title;
    private String age;
    private String price;

    /**
     * Get only different information between 2 games
     *
     * @param expected
     * @return a string with format
     * Title: game title
     * Actual: actual information (only fields that are mismatched)
     * Expected: expected information (only fields that are mismatched)
     */
    public String getDifferentInfo(GameData expected) {
        String title = expected.getTitle();
        String expectedFields = "";
        String actualFields = "";
        if (!Objects.equals(expected.getAge(), this.age)) {
            expectedFields += String.format("Age = %s, ", expected.getAge());
            actualFields += String.format("Age = %s, ", this.age);
        }
        if (!Objects.equals(expected.getPrice(), this.price)) {
            expectedFields += String.format("Price = %s, ", expected.getPrice());
            actualFields += String.format("Price = %s, ", this.price);
        }
        // remove trailing comma and space if needed
        expectedFields = expectedFields.substring(0, expectedFields.length() - 2);
        actualFields = actualFields.substring(0, actualFields.length() - 2);

        return String.format("Title: %s \nActual:   %s \nExpected: %s", title, actualFields, expectedFields);
    }


}