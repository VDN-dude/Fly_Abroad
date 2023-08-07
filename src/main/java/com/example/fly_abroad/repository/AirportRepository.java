package com.example.fly_abroad.repository;

import com.example.fly_abroad.entity.Airport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    Optional<Airport> findByName(String string);

    @Query(
            value = "SELECT a FROM Flight f JOIN Airport a ON a.id = f.to.id GROUP BY a.id ORDER BY COUNT (f.to) DESC "
    )
    List<Airport> findTop5ByPopularity(Pageable pageable);
}
