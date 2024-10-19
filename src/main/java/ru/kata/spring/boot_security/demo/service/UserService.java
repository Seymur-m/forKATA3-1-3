package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userdao, RoleService roleservice, PasswordEncoder passwordEncoder) {
        this.userRepository = userdao;
        this.roleService = roleservice;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveUserWithRole(User user) {
        List<Role> roles = user.getRoles().stream().peek(t -> t.setUser(user)).toList();
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            roleService.saveAll(roles);
        } else {
            Optional<User> optionalUser = userRepository.findById(user.getId());
            User userToDB = optionalUser.get();
            userToDB.setUser(user);
            userToDB.setRoles(user.getRoles());
            userRepository.save(userToDB);
            roleService.saveAll(userToDB.getRoles());
        }
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.getById(id);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}