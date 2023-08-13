package com.example.fly_abroad.service_test;

import com.example.fly_abroad.dto.RegAirlineDto;
import com.example.fly_abroad.entity.Address;
import com.example.fly_abroad.entity.Airline;
import com.example.fly_abroad.entity.User;
import com.example.fly_abroad.repository.AirlineRepository;
import com.example.fly_abroad.service.AirlineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AirlineServiceTest {

    private final Logger log = Logger.getLogger(AirlineServiceTest.class.getName());

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AirlineService airlineService;

    private Airline airline;

    @BeforeEach
    void init() {
        airline = Airline.builder()
                .id(1L)
                .name("TestAirline")
                .phone("+375446665577")
                .administrator(User.builder().firstName("Test").lastName("Test").email("test@gmail.com").password("testTEST").build())
                .officeAddress(Address.builder().country("Belarus").state("Minsk").city("Minsk").street("Street").building("32").zip("220550").build())
                .build();
    }

    @Test
    void save() {
        RegAirlineDto regAirlineDto = new RegAirlineDto();
        regAirlineDto.setAdminFirstName("Test");
        regAirlineDto.setAdminLastName("Test");
        regAirlineDto.setAdminEmail("test@gmail.com");
        regAirlineDto.setPassword("testTEST");
        regAirlineDto.setCompanyName("TestAirline");
        regAirlineDto.setOfficePhone("+375446665577");
        regAirlineDto.setCountry("Belarus");
        regAirlineDto.setState("Minsk");
        regAirlineDto.setCity("Minsk");
        regAirlineDto.setStreet("Street");
        regAirlineDto.setBuilding("32");
        regAirlineDto.setZip("220550");

        assertTrue(airlineService.save(regAirlineDto));
    }

    @Test
    void findByAdminUsername() {
        when(airlineRepository.findByAdministrator_Username("test")).thenReturn(Optional.of(airline));
        Optional<Airline> test = airlineService.findByAdminUsername("test");
        assertTrue(test.isPresent());
    }

    @Test
    void updateInfo() {

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airlineRepository.save(airline)).thenReturn(airline);
        airline.setPhone("+375290000000");
        airline.setName("ChangedName");

        Optional<Airline> updatedAirline = airlineService.updateInfo(airline);
        assertTrue(updatedAirline.isPresent());
        assertEquals("ChangedName", updatedAirline.get().getName());
        assertEquals("+375290000000", updatedAirline.get().getPhone());
    }
}
