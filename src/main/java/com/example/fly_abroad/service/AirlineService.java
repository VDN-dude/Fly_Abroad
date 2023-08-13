package com.example.fly_abroad.service;

import com.example.fly_abroad.dto.RegAirlineDto;
import com.example.fly_abroad.entity.Airline;
import com.example.fly_abroad.mapper.RegAirlineDtoMapper;
import com.example.fly_abroad.repository.AirlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger log = Logger.getLogger(AirlineService.class.getName());

    public boolean save(RegAirlineDto regAirlineDto) {

        if (airlineRepository.existsAirlineByNameOrPhone(regAirlineDto.getCompanyName(), regAirlineDto.getOfficePhone())) {
            log.log(Level.INFO, "Airline already exist");
            return false;
        }

        Airline airline = RegAirlineDtoMapper.regAirlineDtoToAirline(regAirlineDto);
        airline.getAdministrator().setPassword(passwordEncoder.encode(regAirlineDto.getPassword()));
        airlineRepository.save(airline);
        log.log(Level.INFO, "Airline " + airline.getName() + " has just saved");
        return true;
    }

    @Transactional(readOnly = true)
    public Optional<Airline> findByAdminUsername(String username) {
        return airlineRepository.findByAdministrator_Username(username);
    }

    public Optional<Airline> updateInfo(Airline updatedAirline) {
        Optional<Airline> byId = airlineRepository.findById(updatedAirline.getId());
        if (byId.isPresent()){
            airlineRepository.save(updatedAirline);
            log.log(Level.INFO, "Airline with id: " + updatedAirline.getId() + " , has just updated");
            return Optional.of(updatedAirline);
        }
        log.log(Level.INFO, "Airline with id: " + updatedAirline.getId() + " , is not exist");
        return Optional.empty();
    }
}
