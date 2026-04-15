package com.yoonwootak.seatreservationapi.controller;

import com.yoonwootak.seatreservationapi.domain.Reservation;
import com.yoonwootak.seatreservationapi.dto.ReservationCreateRequest;
import com.yoonwootak.seatreservationapi.service.ReservationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReservationCreateRequest req) {
        try {
            Reservation saved = reservationService.createReservation(req);
            return ResponseEntity.ok().body(saved);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("이미 예약된 좌석입니다.");
        }
    }
}
