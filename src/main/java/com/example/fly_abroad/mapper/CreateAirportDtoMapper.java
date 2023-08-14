package com.example.fly_abroad.mapper;

import com.example.fly_abroad.dto.CreateAirportDto;
import com.example.fly_abroad.entity.Address;
import com.example.fly_abroad.entity.Airport;

public class CreateAirportDtoMapper {

    public static Airport createAirportDtoToAirport(CreateAirportDto createAirportDto){

        return Airport.builder()
                .name(createAirportDto.getName())
                .description(createAirportDto.getDescription())
                .address(Address.builder()
                        .country(createAirportDto.getCountry())
                        .state(createAirportDto.getState())
                        .city(createAirportDto.getCity())
                        .street(createAirportDto.getStreet())
                        .building(createAirportDto.getBuilding())
                        .zip(createAirportDto.getZip())
                        .build())
                .build();
    }
}
