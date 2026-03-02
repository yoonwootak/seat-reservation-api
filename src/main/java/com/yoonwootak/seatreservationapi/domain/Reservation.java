package com.yoonwootak.seatreservationapi.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "reservations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_reservations_seat", columnNames = {"seatId"})
        })
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private Long sectionId;

    @Column(nullable = false)
    private Long seatId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status; // PENDING_PAYMENT, CONFIRMED, CANCELLED

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Reservation() {} // 기본 생성자

    public Reservation(Long eventId, Long sectionId, Long seatId) {
        this.eventId = eventId;
        this.sectionId = sectionId;
        this.seatId = seatId;
        this.status = ReservationStatus.PENDING_PAYMENT;
    }

    public void confirm() {
        if (this.status == ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("이미 확정된 예약입니다.");
        }
        if (this.status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("취소된 예약은 확정할 수 없습니다.");
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 예약입니다.");
        }
        if (this.status == ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("확정된 예약은 취소할 수 없습니다."); // 확정된 예약 취소 구현 필요
        }
        this.status = ReservationStatus.CANCELLED;
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
