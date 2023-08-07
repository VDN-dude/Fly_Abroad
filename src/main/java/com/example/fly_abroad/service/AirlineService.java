package com.example.fly_abroad.service;

import com.example.fly_abroad.dto.RegAirlineDto;
import com.example.fly_abroad.entity.Airline;
import com.example.fly_abroad.entity.User;
import com.example.fly_abroad.mapper.RegAirlineDtoMapper;
import com.example.fly_abroad.repository.AirlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean save(RegAirlineDto regAirlineDto) {

        if (airlineRepository.existsAirlineByNameOrPhone(regAirlineDto.getCompanyName(), regAirlineDto.getOfficePhone())) {
            return false;
        }

        Airline airline = RegAirlineDtoMapper.regAirlineDtoToAirline(regAirlineDto);
        airline.getAdministrator().setPassword(passwordEncoder.encode(regAirlineDto.getPassword()));
        airlineRepository.save(airline);
        return true;
    }

    @Transactional(readOnly = true)
    public Optional<Airline> findByUser(User user) {
        return airlineRepository.findByAdministrator(user);
    }

    public boolean updateInfo(Long id, String param, Airline airlineUpdate) {
        Optional<Airline> byId = airlineRepository.findById(id);
        if (byId.isPresent()){
            Airline airline = byId.get();
            switch (param) {
                case "name" -> airline.setName(airlineUpdate.getName());
                case "phone" -> airline.setPhone(airlineUpdate.getPhone());
                case "description" -> airline.setDescription(airlineUpdate.getDescription());
                case "baggageInfo" -> airline.setBaggageInfo(airlineUpdate.getBaggageInfo());
                case "childInfo" -> airline.setChildInfo(airlineUpdate.getChildInfo());
                case "address" -> airline.setOfficeAddress(airlineUpdate.getOfficeAddress());
            }
            airlineRepository.save(airline);
            return true;
        }
        return false;
    }
}
