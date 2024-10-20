package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import java.util.List;


@Service
public class RoleService implements RoleServiceIntr{
    @Autowired
    RoleRepository roleRepository;

    @Transactional
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Transactional
    public void saveAll(List<Role> roles) {
        roleRepository.saveAll(roles);
    }

    @Transactional
    public void deleteRoles(List<Role> roles) {
        roleRepository.deleteAll(roles);
    }

    @Transactional
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
