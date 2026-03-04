package com.yoonwootak.seatreservationapi.service;

import com.yoonwootak.seatreservationapi.domain.Seat;
import com.yoonwootak.seatreservationapi.dto.SeatHoldResponse;
import com.yoonwootak.seatreservationapi.dto.SeatResponse;
import com.yoonwootak.seatreservationapi.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional
    public SeatHoldResponse holdSeat(Long seatId) {
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(7);

        int updated = seatRepository.tryHoldAvailableOrExpired(seatId, token, expiresAt, now);

        if (updated == 0) { // 선점 실패의 경우
            if (!seatRepository.existsById(seatId)) {
                throw new IllegalArgumentException("좌석을 찾을 수 없습니다. id=" + seatId);
            }
            throw new IllegalStateException("이미 다른 사용자가 선점 중입니다.");
        }

        return new SeatHoldResponse(seatId, token, expiresAt);
    }

    @Transactional
    public void confirmSeat(Long seatId, String token) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다. id=" + seatId));

        seat.confirm(token);
    }

    @Transactional
    public List<SeatResponse> listSeats(Long sectionId) {
        List<Seat> seats = seatRepository.findBySectionId(sectionId);

        // 조회 시점에 만료된 HELD 는 AVAILABLE 로 자동 해제
        // releaseIfExpired가 update를 만들기 때문에 @Transactional 필요
        for (Seat seat : seats) {
            seat.releaseIfExpired();
        }

        return seats.stream()
                .map(s -> new SeatResponse(s.getId(), s.getSeatNo(), s.getStatus()))
                .toList();
    }
}
