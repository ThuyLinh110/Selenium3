package org.example.enumData.vietjet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.utils.YamlUtils;

@Getter
@AllArgsConstructor
public enum Label {

    FROM("from"),
    TO("to"),
    PASSENGER("passenger"),
    ADULT("adult"),
    CHILDREN("children"),
    INFANT("infant");

    private final String code;

    @Override
    public String toString() {
        return (String) YamlUtils.getProperty("label." + this.code);
    }
}
