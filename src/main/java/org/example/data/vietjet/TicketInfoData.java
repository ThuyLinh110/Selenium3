package org.example.data.vietjet;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@ToString
public class TicketInfoData {

    private String from;
    private String to;
    private LocalDate flightDate;
    private LocalTime fromTime;
    private LocalTime toTime;
    private String flightNo;
    private String ticketType;
}
