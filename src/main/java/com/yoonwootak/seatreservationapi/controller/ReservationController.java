package com.yoonwootak.seatreservationapi.controller;

import com.yoonwootak.seatreservationapi.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/{reservationId}/start-payment")
    public ResponseEntity<?> startPayment(@PathVariable Long reservationId) {
        try {
            reservationService.startPayment(reservationId);
            return ResponseEntity.ok("결제 시작 완료");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{reservationId}/confirm")
    public ResponseEntity<?> confirm(@PathVariable Long reservationId) {
        try {
            reservationService.confirmReservation(reservationId);
            return ResponseEntity.ok("예약 확정 완료");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.ok("예약 취소 완료");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
