package com.example.fly_abroad;

import com.example.fly_abroad.entity.*;
import com.example.fly_abroad.repository.AirlineRepository;
import com.example.fly_abroad.repository.FlightRepository;
import com.example.fly_abroad.repository.UserRepository;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FlyAbroadApplicationIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private FlightRepository flightRepository;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }
    
    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("postgres")
            .withPassword("root")
            .withUsername("postgres");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @Test
    @Order(1)
    public void save_and_read_user() {
        User user = User.builder().firstName("Test").lastName("Test").email("test@gmail.com").password("testTEST").build();
        userRepository.save(user);
        List<User> allUsers = userRepository.findAll();
        assertFalse(allUsers.isEmpty());
        assertEquals(allUsers.size(), 1);
        assertEquals(allUsers.get(0).getFirstName(), "Test");
    }

    @Test
    @Order(2)
    public void save_and_read_airline() {
        Airline airline = Airline.builder()
                .id(1L)
                .name("TestAirline")
                .phone("+375446665577")
                .administrator(User.builder().firstName("Admin").lastName("Admin").email("admin@gmail.com").password("adminADMIN").build())
                .officeAddress(Address.builder().country("Belarus").state("Minsk").city("Minsk").street("Street").building("32").zip("220550").build())
                .build();

        airlineRepository.save(airline);
        List<Airline> allAirlines = airlineRepository.findAll();
        assertFalse(allAirlines.isEmpty());
        assertEquals(allAirlines.size(), 1);
        assertEquals(allAirlines.get(0).getName(), "TestAirline");
        assertEquals(allAirlines.get(0).getAdministrator().getFirstName(), "Admin");
        assertEquals(allAirlines.get(0).getOfficeAddress().getCountry(), "Belarus");
    }

    @Test
    @Order(3)
    public void save_and_read_flight() {

        assertTrue(airlineRepository.findById(1L).isPresent());
        Airline airline = airlineRepository.findById(1L).get();

        Flight flight = Flight.builder()
                .id(1L)
                .from(Airport.builder()
                        .id(1L)
                        .name("InternationalFly")
                        .address(Address.builder()
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
                        .name("MinskNational")
                        .address(Address.builder()
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

        airlineRepository.save(airline);

        List<Flight> allByAirline_name = flightRepository.findAllByAirline_Name(airline.getName(), PageRequest.of(0, 5));
        assertFalse(allByAirline_name.isEmpty());
        assertEquals(allByAirline_name.size(), 1);
        assertEquals(allByAirline_name.get(0).getFrom().getName(), "InternationalFly");
        assertEquals(allByAirline_name.get(0).getTo().getName(), "MinskNational");
        assertEquals(allByAirline_name.get(0).getTotalTicketsQuantity(), 600);
    }
}
