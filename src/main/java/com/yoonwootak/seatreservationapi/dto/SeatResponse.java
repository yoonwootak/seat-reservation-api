package com.yoonwootak.seatreservationapi.dto;

import com.yoonwootak.seatreservationapi.domain.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatResponse {
    private Long seatId;
    private Integer seatNo;
    private SeatStatus status;
}
