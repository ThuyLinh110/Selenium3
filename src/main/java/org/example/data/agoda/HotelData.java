package org.example.data.agoda;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class HotelData {

    String address;
    String hotelName;
    Float star;
    Float price;
    List<String> services;
}
