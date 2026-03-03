package com.yoonwootak.seatreservationapi.service;

import com.yoonwootak.seatreservationapi.domain.Seat;
import com.yoonwootak.seatreservationapi.dto.SeatHoldResponse;
import com.yoonwootak.seatreservationapi.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional
    public SeatHoldResponse holdSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다. id=" + seatId)); // seatId 에 해당하는 좌석이 없는 경우를 대비해 Optional 타입으로 반환. 예외처리 해야함

        String token = UUID.randomUUID().toString();
        seat.hold(token, 7);

        return new SeatHoldResponse(seat.getId(), token, seat.getHoldExpiresAt());
    }
}
