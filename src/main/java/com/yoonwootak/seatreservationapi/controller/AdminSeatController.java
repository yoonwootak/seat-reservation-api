package com.yoonwootak.seatreservationapi.controller;

import com.yoonwootak.seatreservationapi.domain.Seat;
import com.yoonwootak.seatreservationapi.domain.Section;
import com.yoonwootak.seatreservationapi.repository.SeatRepository;
import com.yoonwootak.seatreservationapi.repository.SectionRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/sections/{sectionId}/seats")
public class AdminSeatController {
    private final SeatRepository seatRepository;
    private final SectionRepository sectionRepository;

    public AdminSeatController(SeatRepository seatRepository, SectionRepository sectionRepository) {
        this.seatRepository = seatRepository;
        this.sectionRepository = sectionRepository;
    }

    @PostMapping("/generate")
    public String generateSeats(@PathVariable Long sectionId, @RequestParam int count) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("구역을 찾을 수 없습니다. id=" + sectionId));

        for (int i = 1; i <= count; i++) {
            Seat seat = new Seat(section, i);
            seatRepository.save(seat);
        }
        return "generated " + count;
    }
}
