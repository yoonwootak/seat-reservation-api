package com.yoonwootak.seatreservationapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservationCreateRequest {
    private Long eventId;
    private Long sectionId;
    private Long seatId;
}
