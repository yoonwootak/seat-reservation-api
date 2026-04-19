package com.yoonwootak.seatreservationapi.repository;

import com.yoonwootak.seatreservationapi.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findBySectionId(Long sectionId);
    Optional<Seat> findByIdAndSectionId(Long seatId, Long sectionId);
}