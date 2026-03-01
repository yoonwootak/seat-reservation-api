package com.yoonwootak.seatreservationapi.service;

import com.yoonwootak.seatreservationapi.domain.Reservation;
import com.yoonwootak.seatreservationapi.dto.ReservationCreateRequest;
import com.yoonwootak.seatreservationapi.repository.ReservationRepository;
import org.springframework.dao.DataIntegrityViolationException;
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
        Reservation r = new Reservation();
        r.setEventId(req.getEventId());
        r.setSectionId(req.getSectionId());
        r.setSeatId(req.getSeatId());
        r.setStatus("PENDING_PAYMENT");
        r.setCreatedAt(LocalDateTime.now());
        // 이때 r의 id는 db에서 insert 될때 자동생성 되기 때문에 save(insert) 시도를 통해서 id를 채워 넣어 줘야함

        try {
            return reservationRepository.save(r); // 이때 예외가 발생하지 않는 다면 바로 return. 예외 발생 시 catch문으로
        } catch (DataIntegrityViolationException e) {
            throw e;
        }


    }
}
