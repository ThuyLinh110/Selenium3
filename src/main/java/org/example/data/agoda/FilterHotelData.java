package org.example.data.agoda;

import lombok.Builder;
import lombok.Data;
import org.example.enumData.agoda.FilterOption;
import org.example.enumData.agoda.FilterType;

import java.util.List;

@Data
@Builder
public class FilterHotelData {
    private Integer minPrice;
    private Integer maxPrice;
    private List<Filter> filter;

    @Data
    @Builder
    public static class Filter {
        FilterType filterType;
        FilterOption filterOption;
    }
}
