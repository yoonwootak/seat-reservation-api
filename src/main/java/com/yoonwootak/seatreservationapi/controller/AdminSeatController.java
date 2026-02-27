package com.yoonwootak.seatreservationapi.controller;

import com.yoonwootak.seatreservationapi.domain.Seat;
import com.yoonwootak.seatreservationapi.repository.SeatRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/sections/{sectionId}/seats")
public class AdminSeatController {
    private final SeatRepository seatRepository;

    public AdminSeatController(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @PostMapping("/generate")
    public String generateSeats(@PathVariable Long sectionId, @RequestParam int count) {
        for (int i = 1; i <= count; i++) {
            Seat seat = new Seat();
            seat.setSectionId(sectionId);
            seat.setSeatNo(i);
            seatRepository.save(seat);
        }
        return "generated " + count;
    }
}
