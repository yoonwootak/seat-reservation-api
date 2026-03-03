package com.yoonwootak.seatreservationapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "seats",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_seats_section_seatno", columnNames = {"sectionId", "seatNo"}) // unique key for seats(sectionId, seatno)
        }
)
public class Seat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sectionId;

    @Column(nullable = false)
    private Integer seatNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    private LocalDateTime holdExpiresAt; // hold 만료 시간

    private String holdToken;

    public void hold(String token, int minutes) {
        if (this.status == SeatStatus.SOLD) {
            throw new IllegalStateException("이미 판매된 좌석입니다.");
        }
        if (this.status == SeatStatus.HELD &&
            this.holdExpiresAt != null &&
            this.holdExpiresAt.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("이미 다른 사용자가 선점 중입니다.");
        }

        this.status = SeatStatus.HELD;
        this.holdToken = token;
        this.holdExpiresAt = LocalDateTime.now().plusMinutes(minutes);
    }

    public void confirm(String token) {
        if (this.status != SeatStatus.HELD) {
            throw new IllegalStateException("좌석 선점 상태가 아닙니다.");
        }
        if (!this.holdToken.equals(token)) {
            throw new IllegalStateException("좌석 선점 토큰이 일치하지 않습니다.");
        }
        if (this.holdExpiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("좌석 선점 시간이 만료되었습니다.");
        }

        this.status = SeatStatus.SOLD;
        this.holdToken = null;
        this.holdExpiresAt = null;
    }
}
