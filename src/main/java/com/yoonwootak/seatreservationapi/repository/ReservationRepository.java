package com.yoonwootak.seatreservationapi.repository;

import com.yoonwootak.seatreservationapi.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}