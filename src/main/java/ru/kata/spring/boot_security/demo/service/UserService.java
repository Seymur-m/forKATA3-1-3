package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


public interface UserService extends UserDetailsService {
    List<User> findAll();
    void saveUserWithRole(User user);
    User findById(Long id);
    void delete(Long id);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException; // Обновлено для соответствия UserDetailsService
}
