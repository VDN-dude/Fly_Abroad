package com.example.fly_abroad.service;

import com.example.fly_abroad.entity.Airport;
import com.example.fly_abroad.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    public List<Airport> findTop5(){
        return airportRepository.findTop5ByPopularity(PageRequest.of(0, 5));
    }
}
