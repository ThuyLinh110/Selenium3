package org.example.enumData.agoda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.utils.YamlUtils;

@Getter
@AllArgsConstructor
public enum FilterType {
    POPULAR_FILTERS("popular_filter"),
    PROPERTY_TYPE("property_type"),
    STAR_RATING("star_rating"),
    NEIGHBORHOOD("neighborhood"),
    ROOM_OFFERS("room_offers"),
    PROPERTY_FACILITIES("property_facilities");

    private final String code;

    @Override
    public String toString() {
        return (String) YamlUtils.getProperty("filter." + this.code);
    }
}
