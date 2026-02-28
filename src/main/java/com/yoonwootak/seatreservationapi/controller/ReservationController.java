package com.yoonwootak.seatreservationapi.controller;

import com.yoonwootak.seatreservationapi.domain.Reservation;
import com.yoonwootak.seatreservationapi.dto.ReservationCreateRequest;
import com.yoonwootak.seatreservationapi.repository.ReservationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReservationCreateRequest req) {
        Reservation r = new Reservation();
        r.setEventId(req.getEventId());
        r.setSectionId(req.getSectionId());
        r.setSeatId(req.getSeatId());
        r.setStatus("PENDING_PAYMENT");
        r.setCreatedAt(LocalDateTime.now());
        // 이때 r의 id는 db에서 insert 될때 자동생성 되기 때문에 save(insert) 시도를 통해서 id를 채워 넣어 줘야함

        try {
            Reservation saved = reservationRepository.save(r); // 이때 예외가 발생하지 않는 다면 바로 return. 예외 발생 시 catch문으로
            return ResponseEntity.ok().body(saved);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("이미 예약된 좌석입니다.");
        }
    }
}
