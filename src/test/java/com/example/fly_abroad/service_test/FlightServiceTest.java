package com.example.fly_abroad.service_test;

import com.example.fly_abroad.dto.CreateFlightDto;
import com.example.fly_abroad.dto.CreateTicketDto;
import com.example.fly_abroad.entity.*;
import com.example.fly_abroad.repository.AirlineRepository;
import com.example.fly_abroad.repository.AirportRepository;
import com.example.fly_abroad.repository.FlightRepository;
import com.example.fly_abroad.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private FlightService flightService;

    private Airline airline;

    private Flight flight;

    @BeforeEach
    void init() {
        airline = Airline.builder()
                .id(1L)
                .name("TestAirline")
                .phone("+375446665577")
                .administrator(User.builder().firstName("Test").lastName("Test").email("test@gmail.com").username("test").password("testTEST").build())
                .officeAddress(Address.builder().country("Belarus").state("Minsk").city("Minsk").street("Street").building("32").zip("220550").build())
                .build();

        flight = Flight.builder()
                .id(1L)
                .from(Airport.builder()
                        .id(2L)
                        .name("InternationalFly")
                        .address(Address.builder()
                                .id(2L)
                                .country("Japan")
                                .state("Japan")
                                .city("Tokyo")
                                .street("Ginza")
                                .building("5")
                                .zip("170-3293")
                                .build())
                        .build())
                .to(Airport.builder()
                        .id(2L)
                        .name("InternationalFly")
                        .address(Address.builder()
                                .id(2L)
                                .country("Belarus")
                                .state("Minsk")
                                .city("Minsk")
                                .street("Central")
                                .building("23")
                                .zip("170546")
                                .build())
                        .build())
                .departure(LocalDateTime.now())
                .arrival(LocalDateTime.now().plusHours(2))
                .duration(Duration.between(LocalDateTime.now(), LocalDateTime.now().plusHours(2)))
                .airline(airline)
                .build();

        List<Ticket> tickets = new ArrayList<>();
        int totalTicketsQuantity = 0;
        for (TicketClass ticketClass : TicketClass.values()) {
            Ticket ticket = Ticket.builder()
                    .ticketClass(ticketClass)
                    .price(BigDecimal.valueOf(200))
                    .totalQuantity(200)
                    .bookedQuantity(0)
                    .unbookedQuantity(200)
                    .flight(flight)
                    .build();
            tickets.add(ticket);
            totalTicketsQuantity = totalTicketsQuantity + ticket.getTotalQuantity();
        }
        flight.setTotalTicketsQuantity(totalTicketsQuantity);
        flight.setUnbookedTicketsQuantity(totalTicketsQuantity);
        flight.setTickets(tickets);
        airline.setFlights(Set.of(flight));
    }

    @Test
    void save() {
        CreateFlightDto createFlightDto = new CreateFlightDto();
        List<CreateTicketDto> tickets = createFlightDto.getTickets();
        for (CreateTicketDto ticket : tickets) {
            ticket.setPrice(BigDecimal.valueOf(200));
            ticket.setQuantity(300);
        }
        createFlightDto.setFromName("MinskNational");
        createFlightDto.setFromCountry("Belarus");
        createFlightDto.setFromCity("Minsk");
        createFlightDto.setToName("Vnukovo");
        createFlightDto.setToCountry("Russia Federation");
        createFlightDto.setToCity("Moskow");
        createFlightDto.setDeparture(String.valueOf(LocalDateTime.now()));
        createFlightDto.setArrival(String.valueOf(LocalDateTime.now().plusHours(2)));
        createFlightDto.setTickets(tickets);

        when(airportRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(airlineRepository.findByAdministrator_Username("test")).thenReturn(Optional.of(airline));
        when(airlineRepository.save(airline)).thenReturn(airline);
        assertTrue(flightService.save(createFlightDto, "test"));
    }

    @Test
    void findByAirlineName() {
        int page = 1;
        int size = 5;
        int offset = 0;
        if (page != 1) {
            offset = ((page - 1) * size);
        }

        Set<Flight> flights = airline.getFlights();
        long countOfPages = (long) Math.ceil((double) flights.size() / size);

        if (page == countOfPages && flights.size() > 1) {
            when(flightRepository.findAllByAirline_Name("TestAirline", PageRequest.of(page, size)))
                    .thenReturn(airline.getFlights().stream().toList().subList(offset, flights.size() - 1));
        } else if (page == countOfPages && flights.size() == 1){
            when(flightRepository.findAllByAirline_Name("TestAirline", PageRequest.of(page, size)))
                    .thenReturn(airline.getFlights().stream().toList().subList(offset, flights.size()));
        } else {
            when(flightRepository.findAllByAirline_Name("TestAirline", PageRequest.of(page, size)))
                    .thenReturn(airline.getFlights().stream().toList().subList(offset, offset + size));
        }
        when(flightRepository.countAllByAirline_Name("TestAirline")).thenReturn(Long.valueOf(airline.getFlights().size()));

        PageableFlights pageableFlights = flightService.findAllByAirlineName("TestAirline", page, size);
        assertEquals(pageableFlights.getSize(), size);
        assertEquals(pageableFlights.getPage(), page);
        assertEquals((long) pageableFlights.getCountOfPages(), countOfPages);
        assertFalse(pageableFlights.getFlights().isEmpty());
    }

    @Test
    void update() {
        when(flightRepository.save(flight)).thenReturn(flight);

        flight.setDeparture(LocalDateTime.parse("2023-08-11T12:00"));
        flight.setArrival(LocalDateTime.parse("2023-08-11T14:00"));
        flight.setDuration(Duration.between(flight.getDeparture(), flight.getArrival()));

        Flight update = flightService.update(flight);
        assertEquals("2023-08-11T12:00", String.valueOf(flight.getDeparture()));
        assertEquals("2023-08-11T14:00", String.valueOf(flight.getArrival()));
        assertEquals(2, flight.getDuration().toHours());
    }

    @Test
    void findByParams() {

    }
}
