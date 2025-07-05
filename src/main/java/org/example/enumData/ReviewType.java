package org.example.enumData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.utils.YamlUtils;

@Getter
@AllArgsConstructor
public enum ReviewType {
    LOCATION("location"),
    CLEANLINESS("cleanliness"),
    SERVICE("service"),
    FACILITIES("facilities"),
    ROOM_COMFORT_AND_QUALITY("room_comfort_and_quantity"),
    VALUE_FOR_MONEY("value_for_money");

    private final String code;

    @Override
    public String toString() {
        return (String) YamlUtils.getProperty("review_type." + this.code);
    }

    public static ReviewType fromText(String text) {
        for (ReviewType type : values()) {
            String label = (String) YamlUtils.getProperty("review_type." + type.code);
            if (label != null && label.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ReviewType display value: " + text);
    }
}
