package org.example.enumData.vietjet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.utils.YamlUtils;

@Getter
@AllArgsConstructor
public enum FlightType {

    DEPARTURE("departure_flight"),
    RETURN("return_flight");

    private final String code;

    @Override
    public String toString() {
        return (String) YamlUtils.getProperty("title." + this.code);
    }
}
