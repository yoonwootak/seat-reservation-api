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
                @UniqueConstraint(name = "uk_seats_section_seatno", columnNames = {"section_id", "seat_no"}) // unique key for seats(sectionId, seatno)
        }
)
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(nullable = false)
    private Integer seatNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Seat() {}

    public Seat(Section section, Integer seatNo) {
        this.section = section;
        this.seatNo = seatNo;
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

    public void markHeld() {
        if (this.status == SeatStatus.SOLD) {
            throw new IllegalStateException("이미 판매된 좌석입니다.");
        }
        this.status = SeatStatus.HELD;
    }

    public void markSold() {
        if (this.status != SeatStatus.HELD) {
            throw new IllegalStateException("선점 상태인 좌석만 판매 완료할 수 있습니다.");
        }
        this.status = SeatStatus.SOLD;
    }

    public void markAvailable() {
        this.status = SeatStatus.AVAILABLE;
    }
}
