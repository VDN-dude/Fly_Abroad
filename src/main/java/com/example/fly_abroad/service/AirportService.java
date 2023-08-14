package com.example.fly_abroad.service;

import com.example.fly_abroad.dto.CreateAirportDto;
import com.example.fly_abroad.entity.Airport;
import com.example.fly_abroad.mapper.CreateAirportDtoMapper;
import com.example.fly_abroad.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    public boolean save(CreateAirportDto createAirportDto){
        if (airportRepository.findByName(createAirportDto.getName()).isPresent()) {
            return false;
        }
        Airport airport = CreateAirportDtoMapper.createAirportDtoToAirport(createAirportDto);
        airportRepository.save(airport);
        return true;
    }

    public Optional<Airport> findByName(String name){
        return airportRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Airport> findTop5(){
        return airportRepository.findTop5ByPopularity(PageRequest.of(0, 5));
    }

    public void update(Airport airport){
        airportRepository.save(airport);
    }
}
