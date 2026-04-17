package com.yoonwootak.seatreservationapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "sections",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_sections_event_name", columnNames = {"event_id", "name"})
        }
)
public class Section {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String name; // 0구역

    @Column(nullable = false)
    private Integer price;

    protected Section() {}

    public Section(Event event, String name, Integer price) {
        this.event = event;
        this.name = name;
        this.price = price;
    }
}
