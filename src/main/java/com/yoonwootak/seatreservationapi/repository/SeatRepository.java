package com.yoonwootak.seatreservationapi.repository;

import com.yoonwootak.seatreservationapi.domain.Seat;
import com.yoonwootak.seatreservationapi.domain.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findBySectionId(Long sectionId);
    List<Seat> findByStatusAndHoldExpiresAtBefore(SeatStatus status, LocalDateTime time);
}