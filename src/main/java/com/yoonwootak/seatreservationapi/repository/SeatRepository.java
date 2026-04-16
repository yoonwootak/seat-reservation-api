package com.yoonwootak.seatreservationapi.repository;

import com.yoonwootak.seatreservationapi.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findBySectionId(Long sectionId);
}