package com.yoonwootak.seatreservationapi.service;

import com.yoonwootak.seatreservationapi.domain.Reservation;
import com.yoonwootak.seatreservationapi.domain.Seat;
import com.yoonwootak.seatreservationapi.domain.SeatStatus;
import com.yoonwootak.seatreservationapi.dto.SeatHoldResponse;
import com.yoonwootak.seatreservationapi.dto.SeatResponse;
import com.yoonwootak.seatreservationapi.repository.ReservationRepository;
import com.yoonwootak.seatreservationapi.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    public SeatService(SeatRepository seatRepository, ReservationRepository reservationRepository) {
        this.seatRepository = seatRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public List<SeatResponse> listSeats(Long sectionId) {
        List<Seat> seats = seatRepository.findBySectionId(sectionId);

        return seats.stream()
                .map(s -> new SeatResponse(s.getId(), s.getSeatNo(), s.getStatus()))
                .toList();
    }

    @Transactional
    public SeatHoldResponse holdSeat(Long userId, Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다. id=" + seatId));

        if (seat.getStatus() == SeatStatus.SOLD) {
            throw new IllegalStateException("이미 판매된 좌석 입니다.");
        }

        if (seat.getStatus() == SeatStatus.HELD) {
            throw new IllegalStateException("이미 다른 사용자가 선점 중입니다.");
        }

        LocalDateTime holdExpiresAt = LocalDateTime.now().plusMinutes(5);

        Reservation reservation = new Reservation(userId, seatId, holdExpiresAt);
        Reservation saved = reservationRepository.save(reservation);

        seat.markHeld();

        return new SeatHoldResponse(saved.getId(), holdExpiresAt);
    }
}
