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
}
