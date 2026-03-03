package com.yoonwootak.seatreservationapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SeatHoldResponse {
    private Long seatId;
    private String holdToken;
    private LocalDateTime holdExpiresAt;
}
