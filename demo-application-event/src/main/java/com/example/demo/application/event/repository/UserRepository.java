package com.example.demo.application.event.repository;

import com.example.demo.application.event.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}