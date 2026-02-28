package com.yoonwootak.seatreservationapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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

    @Column(nullable = false)
    private String status; // PENDING_PAYMENT, CONFIRMED, CANCELLED

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
