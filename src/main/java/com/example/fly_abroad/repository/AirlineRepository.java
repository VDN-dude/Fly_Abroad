package com.example.fly_abroad.repository;

import com.example.fly_abroad.entity.Airline;
import com.example.fly_abroad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long> {

    boolean existsAirlineByNameOrPhone(String name, String phone);

    Optional<Airline> findByAdministrator(User user);
}
