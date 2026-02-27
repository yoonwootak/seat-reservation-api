package com.yoonwootak.seatreservationapi.controller;

import com.yoonwootak.seatreservationapi.domain.Section;
import com.yoonwootak.seatreservationapi.repository.SectionRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/events/{eventId}/sections")
public class AdminSectionController {
    private final SectionRepository sectionRepository;

    public AdminSectionController(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @PostMapping
    public Section createSection(@PathVariable Long eventId, @RequestBody Section req) {
        Section section = new Section();
        section.setEventId(eventId);
        section.setName(req.getName());
        section.setPrice(req.getPrice());
        return sectionRepository.save(section);
    }
}
