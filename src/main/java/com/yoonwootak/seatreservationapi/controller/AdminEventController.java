package com.yoonwootak.seatreservationapi.controller;

import com.yoonwootak.seatreservationapi.domain.Event;
import com.yoonwootak.seatreservationapi.repository.EventRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventRepository eventRepository;

    public  AdminEventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public Event create(@RequestBody Event event){
        return eventRepository.save(event);
    }
}
