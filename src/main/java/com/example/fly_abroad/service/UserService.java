package com.example.fly_abroad.service;

import com.example.fly_abroad.dto.RegUserDto;
import com.example.fly_abroad.entity.*;
import com.example.fly_abroad.mapper.RegUserDtoMapper;
import com.example.fly_abroad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean save(RegUserDto regUserDto) {

        if (userRepository.existsUserByEmailOrPhone(regUserDto.getEmail(), regUserDto.getPhone())) {
            return false;
        }

        User user = RegUserDtoMapper.regUserToUser(regUserDto);
        user.setRole(Set.of(UserRole.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            return byEmail.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

    public Optional<PageableBookedTicket> paginatedBookedTicket(User user, int page, int size){
        if(!user.getBookedTickets().isEmpty()) {
            int offset;

            if (page == 1) {
                offset = 0;
            } else {
                offset = ((page - 1) * size);
            }

            List<BookedTicket> bookedTickets = user.getBookedTickets();
            List<BookedTicket> subBookedTickets;
            long countOfPages = (long) Math.ceil((double) bookedTickets.size() / size);

            if (page == countOfPages) {
                subBookedTickets = user.getBookedTickets().subList(offset, bookedTickets.size() - 1);
            } else {
                subBookedTickets = user.getBookedTickets().subList(offset, offset + size);
            }

            return Optional.of(PageableBookedTicket.builder()
                    .bookedTickets(subBookedTickets)
                    .page(page)
                    .size(size)
                    .countOfPages(countOfPages)
                    .build());
        }
        return Optional.empty();
    }
}
