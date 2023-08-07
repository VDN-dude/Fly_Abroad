package com.example.fly_abroad.mapper;

import com.example.fly_abroad.dto.RegUserDto;
import com.example.fly_abroad.entity.Address;
import com.example.fly_abroad.entity.User;
import com.example.fly_abroad.entity.UserRole;

import java.util.Arrays;
import java.util.Set;

public class RegUserDtoMapper {

    public static User regUserToUser(RegUserDto regUserDto){

        Address address = Address.builder()
                .country(regUserDto.getCountry())
                .city(regUserDto.getCity())
                .state(regUserDto.getState())
                .street(regUserDto.getStreet())
                .building(regUserDto.getBuilding())
                .zip(regUserDto.getZip())
                .build();

        return User.builder()
                .firstName(regUserDto.getFirstName())
                .lastName(regUserDto.getLastName())
                .username(Arrays.stream(regUserDto.getEmail().split("@")).toArray()[0].toString())
                .email(regUserDto.getEmail())
                .address(address)
                .password(regUserDto.getPassword())
                .role(Set.of(UserRole.USER, UserRole.AIRLINE_ADMIN))
                .build();
    }
}
