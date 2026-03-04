package com.yoonwootak.seatreservationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SeatReservationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeatReservationApiApplication.class, args);
    }

}
