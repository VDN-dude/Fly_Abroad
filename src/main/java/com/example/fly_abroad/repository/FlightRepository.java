package com.example.fly_abroad.repository;

import com.example.fly_abroad.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findAllByAirline_Name(String name, Pageable pageable);

    Long countAllByAirline_Name(String name);

    List<Flight> findAllByTo_Address_City(String city, Pageable pageable);

    Long countAllByTo_Address_City(String city);

    List<Flight> findAllByFrom_Address_CityAndTo_Address_CityAndDepartureAfterAndDepartureBeforeAndUnbookedTicketsQuantityIsGreaterThanEqual(String fromCity, String toCity, LocalDateTime departureAfter, LocalDateTime departureBefore, int unbookedTicketsQuantity, Pageable pageable);

    Long countAllByFrom_Address_CityAndTo_Address_CityAndDepartureAfterAndDepartureBeforeAndUnbookedTicketsQuantityIsGreaterThanEqual(String fromCity, String toCity, LocalDateTime departureAfter, LocalDateTime departureBefore, int unbookedTicketsQuantity);

    boolean existsByFrom_NameAndTo_NameAndDepartureAndArrival(String from, String to, LocalDateTime departure, LocalDateTime arrival);
}
