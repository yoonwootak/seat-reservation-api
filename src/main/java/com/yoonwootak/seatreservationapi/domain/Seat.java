package com.yoonwootak.seatreservationapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sectionId;

    @Column(nullable = false)
    private Integer seatNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    protected Seat() {}

    public Seat(Long sectionId, Integer seatNo) {
        this.sectionId = sectionId;
        this.seatNo = seatNo;
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
