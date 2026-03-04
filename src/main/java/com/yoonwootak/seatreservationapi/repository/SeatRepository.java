package com.yoonwootak.seatreservationapi.repository;

import com.yoonwootak.seatreservationapi.domain.Seat;
import com.yoonwootak.seatreservationapi.domain.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findBySectionId(Long sectionId);
    List<Seat> findByStatusAndHoldExpiresAtBefore(SeatStatus status, LocalDateTime time);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Seat s
           set s.status = com.yoonwootak.seatreservationapi.domain.SeatStatus.HELD,
               s.holdToken = :token,
               s.holdExpiresAt = :expiresAt
         where s.id = :seatId
           and (
               s.status = com.yoonwootak.seatreservationapi.domain.SeatStatus.AVAILABLE
               or (s.status = com.yoonwootak.seatreservationapi.domain.SeatStatus.HELD and s.holdExpiresAt < :now)
           )
    """)
    int tryHoldAvailableOrExpired(@Param("seatId") Long seatId,
                                  @Param("token") String token,
                                  @Param("expiresAt") LocalDateTime expiresAt,
                                  @Param("now") LocalDateTime now);
}