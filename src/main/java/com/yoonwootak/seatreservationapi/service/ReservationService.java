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
    public void startPayment(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId));

        Seat seat = seatRepository.findById(reservation.getSeat().getId())
                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다. id=" + reservation.getSeat().getId()));

        if (reservation.getStatus() != ReservationStatus.HELD) {
            throw new IllegalStateException("HELD 상태의 예약만 결제를 시작할 수 있습니다.");
        }

        if (seat.getStatus() != SeatStatus.HELD) {
            throw new IllegalStateException("결제를 시작할 수 있는 좌석 상태가 아닙니다.");
        }

        if (reservation.isExpiredAt(LocalDateTime.now())) {
            reservation.expire();
            seat.markAvailable();
            throw new IllegalStateException("선점 시간이 만료된 예약입니다.");
        }

        reservation.startPayment();
    }

    @Transactional
    public void confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId));

        Seat seat = seatRepository.findById(reservation.getSeat().getId())
                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다. id=" + reservation.getSeat().getId()));

        if (reservation.getStatus() != ReservationStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("PAYMENT_PENDING 상태의 예약만 확정할 수 있습니다.");
        }

        if (seat.getStatus() != SeatStatus.HELD) {
            throw new IllegalStateException("확정 가능한 좌석 상태가 아닙니다.");
        }

        if (reservation.isExpiredAt(LocalDateTime.now())) {
            reservation.expire();
            seat.markAvailable();
            throw new IllegalStateException("선점 시간이 만료된 예약입니다.");
        }

        reservation.confirm();
        seat.markSold();
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId));

        Seat seat = seatRepository.findById(reservation.getSeat().getId())
                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다. id=" + reservation.getSeat().getId()));

        if (reservation.getStatus() != ReservationStatus.HELD && reservation.getStatus() != ReservationStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("활성 상태의 예약만 취소할 수 있습니다.");
        }

        if (seat.getStatus() != SeatStatus.HELD) {
            throw new IllegalStateException("취소 가능한 좌석 상태가 아닙니다.");
        }

        if (reservation.isExpiredAt(LocalDateTime.now())) {
            reservation.expire();
            seat.markAvailable();
            throw new IllegalStateException("선점 시간이 만료된 예약입니다.");
        }

        reservation.cancel();
        seat.markAvailable();
    }
}
