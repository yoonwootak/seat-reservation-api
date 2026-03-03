package com.yoonwootak.seatreservationapi.controller;

import com.yoonwootak.seatreservationapi.domain.Seat;
import com.yoonwootak.seatreservationapi.dto.SeatResponse;
import com.yoonwootak.seatreservationapi.repository.SeatRepository;
import com.yoonwootak.seatreservationapi.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections/{sectionId}/seats")
public class SeatController {
    private final SeatRepository seatRepository;
    private final SeatService seatService;

    public SeatController(SeatRepository seatRepository, SeatService seatService) {
        this.seatRepository = seatRepository;
        this.seatService = seatService;
    }

    @GetMapping
    public List<SeatResponse> list(@PathVariable Long sectionId) {
        List<Seat> seats = seatRepository.findBySectionId(sectionId);

        return seats.stream()
                .map(s -> new SeatResponse(s.getId(), s.getSeatNo(), true))
                .toList();
    }

    @PostMapping("/{seatId}/hold")
    public ResponseEntity<?> hold(@PathVariable Long sectionId, @PathVariable Long seatId) {
        try {
            return ResponseEntity.ok(seatService.holdSeat(seatId));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // 이미 판매가 된 좌석 또는 이미 다른 사용자가 선점 중인 좌석인 경우
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400. 섹션, 좌석 id가 이상한 경우
        }
    }
}
