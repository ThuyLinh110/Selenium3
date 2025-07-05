package org.example.enumData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.utils.YamlUtils;

@Getter
@AllArgsConstructor
public enum FilterOption {

    RATING_THREE_STAR("3_stars", 3),
    RATING_FOUR_STAR("4_stars", 4),
    SWIMMING_POOL("swimming_pool", 93);

    private final String code;
    private final int value;

    @Override
    public String toString() {
        return (String) YamlUtils.getProperty("checkbox." + this.code);
    }
}
