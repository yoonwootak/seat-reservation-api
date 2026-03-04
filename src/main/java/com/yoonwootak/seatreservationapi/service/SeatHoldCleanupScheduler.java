package com.yoonwootak.seatreservationapi.service;

import com.yoonwootak.seatreservationapi.domain.Seat;
import com.yoonwootak.seatreservationapi.domain.SeatStatus;
import com.yoonwootak.seatreservationapi.repository.SeatRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SeatHoldCleanupScheduler {
    private final SeatRepository seatRepository;

    public SeatHoldCleanupScheduler(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void releaseExpiredHolds() {
        LocalDateTime now = LocalDateTime.now();
        List<Seat> expired = seatRepository.findByStatusAndHoldExpiresAtBefore(SeatStatus.HELD, now);

        for (Seat seat : expired) {
            seat.releaseIfExpired();
        }
    }
}
