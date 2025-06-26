package org.example.data.agoda;

import lombok.Builder;
import lombok.Data;
import org.example.enumData.FilterOption;
import org.example.enumData.FilterType;

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
