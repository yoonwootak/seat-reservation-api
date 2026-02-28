package com.yoonwootak.seatreservationapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatResponse {
    private Long seatId;
    private Integer seatNo;
    private boolean isAvailable;
}
