package com.yoonwootak.seatreservationapi.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private LocalDateTime holdExpiresAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Reservation() {} // 기본 생성자

    public Reservation(User user, Seat seat, LocalDateTime holdExpiresAt) {
        this.user = user;
        this.seat = seat;
        this.status = ReservationStatus.HELD;
        this.holdExpiresAt = holdExpiresAt;
    }

    public void startPayment() {
        if (this.status != ReservationStatus.HELD) {
            throw new IllegalStateException("HELD 상태의 예약만 결제를 시작할 수 있습니다.");
        }

        this.status = ReservationStatus.PAYMENT_PENDING;
    }

    public void confirm() {
        if (this.status != ReservationStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("PAYMENT_PENDING 상태의 예약만 확정할 수 있습니다.");
        }

        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status != ReservationStatus.HELD && this.status != ReservationStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("활성 상태의 예약만 취소할 수 있습니다.");
        }

        this.status = ReservationStatus.CANCELLED;
    }

    public void expire() {
        if (this.status != ReservationStatus.HELD && this.status != ReservationStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("활성 상태의 예약만 만료 처리할 수 있습니다.");
        }

        this.status = ReservationStatus.EXPIRED;
    }

    public boolean isExpiredAt(LocalDateTime now) {
        return this.holdExpiresAt.isBefore(now);
    }

    @PrePersist
    private void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
