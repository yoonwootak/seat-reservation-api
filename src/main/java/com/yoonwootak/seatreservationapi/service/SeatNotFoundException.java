package com.yoonwootak.seatreservationapi.service;

public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(Long sectionId, Long seatId) {
        super("좌석을 찾을 수 없습니다. sectionId=" + sectionId + ", seatId=" + seatId);
    }
}
