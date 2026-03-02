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

    // 동시성 테스트용 예약 api : Request body 에서 요청을 받는게 아닌 테스트용 요청을 직접 만들어서 return
    @PostMapping("/test/{seatId}")
    public ResponseEntity<?> createTest(@PathVariable Long seatId){
        ReservationCreateRequest req = new ReservationCreateRequest();
        req.setEventId(1L);
        req.setSectionId(1L);
        req.setSeatId(seatId);
        return create(req);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirm(@PathVariable Long id){
        Reservation updated = reservationService.confirmReservation(id);
        return ResponseEntity.ok().body(updated);
    }
}
