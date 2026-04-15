package com.yoonwootak.seatreservationapi.service;

import com.yoonwootak.seatreservationapi.domain.Reservation;
import com.yoonwootak.seatreservationapi.dto.ReservationCreateRequest;
import com.yoonwootak.seatreservationapi.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation createReservation(ReservationCreateRequest req) {
        LocalDateTime holdExpiresAt = LocalDateTime.now().plusMinutes(5);

        Reservation reservation = new Reservation(
                req.getUserId(),
                req.getSeatId(),
                holdExpiresAt
        );

        return reservationRepository.save(reservation);
    }
}
