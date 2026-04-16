package com.yoonwootak.seatreservationapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SeatHoldResponse {
    private Long reservationId;
    private LocalDateTime holdExpiresAt;
}
