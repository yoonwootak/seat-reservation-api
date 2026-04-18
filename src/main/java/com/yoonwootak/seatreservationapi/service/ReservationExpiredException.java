package com.yoonwootak.seatreservationapi.service;

public class ReservationExpiredException extends RuntimeException {
    public ReservationExpiredException(String message) {
        super(message);
    }
}
