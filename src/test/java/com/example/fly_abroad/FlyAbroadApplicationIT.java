package com.example.fly_abroad;

import com.example.fly_abroad.entity.Address;
import com.example.fly_abroad.entity.Airline;
import com.example.fly_abroad.entity.User;
import com.example.fly_abroad.repository.AirlineRepository;
import com.example.fly_abroad.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FlyAbroadApplicationIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AirlineRepository airlineRepository;

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


    @Test
    public void save_and_read_user() {
        User user = User.builder().firstName("Test").lastName("Test").email("test@gmail.com").password("testTEST").build();
        userRepository.save(user);
        List<User> allUsers = userRepository.findAll();
        assert !allUsers.isEmpty();
        assert allUsers.get(0).getFirstName().equalsIgnoreCase("Test");
        assert allUsers.size() == 1;
    }

    @Test
    public void save_and_read_airline() {
        Airline airline = Airline.builder()
                .id(1L)
                .name("TestAirline")
                .phone("+375446665577")
                .administrator(User.builder().firstName("Test").lastName("Test").email("test@gmail.com").password("testTEST").build())
                .officeAddress(Address.builder().country("Belarus").state("Minsk").city("Minsk").street("Street").building("32").zip("220550").build())
                .build();

        airlineRepository.save(airline);
        List<Airline> allAirlines = airlineRepository.findAll();
        assertFalse(allAirlines.isEmpty());
        assertEquals(allAirlines.size(), 1);
        assertEquals(allAirlines.get(0).getName(), "TestAirline");
        assertEquals(allAirlines.get(0).getAdministrator().getFirstName(), "Test");
        assertEquals(allAirlines.get(0).getOfficeAddress().getCountry(), "Belarus");
    }
}
