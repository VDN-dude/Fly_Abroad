package com.example.fly_abroad.mapper;

import com.example.fly_abroad.dto.RegAirlineDto;
import com.example.fly_abroad.entity.Address;
import com.example.fly_abroad.entity.Airline;
import com.example.fly_abroad.entity.User;
import com.example.fly_abroad.entity.UserRole;

import java.util.Arrays;
import java.util.Set;

public class RegAirlineDtoMapper {

    public static Airline regAirlineDtoToAirline(RegAirlineDto regAirlineDto) {

        Address address = Address.builder()
                .country(regAirlineDto.getCountry())
                .city(regAirlineDto.getCity())
                .state(regAirlineDto.getState())
                .street(regAirlineDto.getStreet())
                .building(regAirlineDto.getBuilding())
                .zip(regAirlineDto.getZip())
                .build();

        User admin = User.builder()
                .firstName(regAirlineDto.getAdminFirstName())
                .lastName(regAirlineDto.getAdminLastName())
                .username(Arrays.stream(regAirlineDto.getAdminEmail().split("@")).toArray()[0].toString())
                .email(regAirlineDto.getAdminEmail())
                .password(regAirlineDto.getPassword())
                .role(Set.of(UserRole.USER, UserRole.AIRLINE_ADMIN))
                .build();

        return Airline.builder()
                .administrator(admin)
                .name(regAirlineDto.getCompanyName())
                .officeAddress(address)
                .phone(regAirlineDto.getOfficePhone())
                .build();
    }
}
