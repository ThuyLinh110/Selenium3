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

    @Override
    public boolean equals(Object o) {
        GameData that = (GameData) o;
        return Objects.equals(title, that.title) && Objects.equals(age, that.age) && Objects.equals(price, that.price);
    }
}