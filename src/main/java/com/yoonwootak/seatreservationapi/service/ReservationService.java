package com.yoonwootak.seatreservationapi.service;

import com.yoonwootak.seatreservationapi.domain.Reservation;
import com.yoonwootak.seatreservationapi.dto.ReservationCreateRequest;
import com.yoonwootak.seatreservationapi.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation createReservation(ReservationCreateRequest req) {
        Reservation r = new Reservation(req.getEventId(), req.getSectionId(), req.getSeatId());

        return reservationRepository.save(r); // 이때 예외가 발생하지 않는 다면 바로 return. 예외 발생 시 catch문으로
    }

    @Transactional
    public Reservation confirmReservation(Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId));

        r.confirm();
        return r;
    }
}