package com.yoonwootak.seatreservationapi.controller;

import com.yoonwootak.seatreservationapi.domain.Event;
import com.yoonwootak.seatreservationapi.domain.Section;
import com.yoonwootak.seatreservationapi.repository.EventRepository;
import com.yoonwootak.seatreservationapi.repository.SectionRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/events/{eventId}/sections")
public class AdminSectionController {
    private final SectionRepository sectionRepository;
    private final EventRepository eventRepository;

    public AdminSectionController(SectionRepository sectionRepository, EventRepository eventRepository) {
        this.sectionRepository = sectionRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public Section createSection(@PathVariable Long eventId, @RequestBody Section req) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다. id=" + eventId));

        Section section = new Section(event, req.getName(), req.getPrice());
        return sectionRepository.save(section);
    }
}
