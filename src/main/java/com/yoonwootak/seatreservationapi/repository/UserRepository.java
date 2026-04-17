package com.yoonwootak.seatreservationapi.repository;

import com.yoonwootak.seatreservationapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
