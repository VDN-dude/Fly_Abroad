package com.example.fly_abroad.repository;

import com.example.fly_abroad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsUserByEmailOrPhone(String email, String phone);
}
