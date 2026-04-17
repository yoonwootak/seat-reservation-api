package com.yoonwootak.seatreservationapi.service;

import com.yoonwootak.seatreservationapi.domain.Reservation;
import com.yoonwootak.seatreservationapi.domain.ReservationStatus;
import com.yoonwootak.seatreservationapi.domain.Seat;
import com.yoonwootak.seatreservationapi.domain.SeatStatus;
import com.yoonwootak.seatreservationapi.repository.ReservationRepository;
import com.yoonwootak.seatreservationapi.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    public ReservationService(ReservationRepository reservationRepository, SeatRepository seatRepository) {
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public void confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId));

        if (reservation.getHoldExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("선점 시간이 만료된 예약입니다.");
        }

        Seat seat = seatRepository.findById(reservation.getSeat().getId())
                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다. id=" + reservation.getSeat().getId()));

        reservation.confirm();
        seat.markSold();
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId));

        if (reservation.getStatus() != ReservationStatus.HELD) {
            throw new IllegalStateException("선점 상태의 예약만 취소할 수 있습니다.");
        }

        Seat seat = seatRepository.findById(reservation.getSeat().getId())
                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다. id=" + reservation.getSeat().getId()));

        if (reservation.getHoldExpiresAt().isBefore(LocalDateTime.now())) {
            reservation.expire();
            seat.markAvailable();
            throw new IllegalStateException("이미 만료된 예약입니다. 취소할 수 없습니다.");
        }

        if (seat.getStatus() != SeatStatus.HELD) {
            throw new IllegalStateException("취소 가능한 좌석 상태가 아닙니다.");
        }

        reservation.cancel();
        seat.markAvailable();
    }
}
