package com.example.fly_abroad.repository;

import com.example.fly_abroad.entity.Ticket;
import com.example.fly_abroad.entity.TicketClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByFlight_IdAndTicketClass(Long flightId, TicketClass ticketClass);
}
