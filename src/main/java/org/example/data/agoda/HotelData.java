package org.example.data.agoda;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class HotelData {

    String address;
    String hotelName;
    Float star;
    Integer price;
}
