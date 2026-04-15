package com.yoonwootak.seatreservationapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservationCreateRequest {
    private Long userId;
    private Long seatId;
}
