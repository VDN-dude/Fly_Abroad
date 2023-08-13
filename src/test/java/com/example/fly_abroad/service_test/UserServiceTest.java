package com.example.fly_abroad.service_test;

import com.example.fly_abroad.dto.RegUserDto;
import com.example.fly_abroad.repository.UserRepository;
import com.example.fly_abroad.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void save() {
        RegUserDto regUserDto = new RegUserDto();
        regUserDto.setFirstName("Test");
        regUserDto.setLastName("Test");
        regUserDto.setEmail("test@gmail.com");
        regUserDto.setPassword("testTEST");
        regUserDto.setPhone("+375555555555");
        regUserDto.setCountry("Belarus");
        regUserDto.setState("Minsk");
        regUserDto.setCity("Minsk");
        regUserDto.setStreet("Lidscaja");
        regUserDto.setBuilding("125");
        regUserDto.setZip("220055");

        assertTrue(userService.save(regUserDto));
    }
}

