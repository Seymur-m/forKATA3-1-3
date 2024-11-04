package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/admin")
    public String forAdmin(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("listUsers", userServiceImpl.findAll());
        model.addAttribute("user", new User());
        model.addAttribute("role", new Role());
        model.addAttribute("currentuser", currentUser);
        return "users";
    }

    @GetMapping("/user")
    public String forUser(@AuthenticationPrincipal User currentUser, Model model) {
        User user = (User) userServiceImpl.loadUserByUsername(currentUser.getUsername());
        model.addAttribute("currentuser", currentUser);
        return "user";
    }

    @GetMapping("/admin/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", new Role());
        return "add-user";
    }

    @PostMapping("/admin/save")
    public String saveUser(@ModelAttribute("user") User user, @ModelAttribute("role") Role role, @RequestParam(value = "id", required = false) Long id, Model model) {
        if (role.getRoleName() == null || role.getRoleName().isEmpty()) {
            model.addAttribute("error", "Пожалуйста, выберите роль");
            model.addAttribute("user", user);
            return "add-user";
        }

        System.out.println("Received User ID: " + id);

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        if (id != null) {
            User existingUser = userServiceImpl.findById(id);
            if (existingUser != null) {
                System.out.println("Editing existing user: " + existingUser.getUsername());
                existingUser.setRoles(roles);
                existingUser.setUsername(user.getUsername());
                existingUser.setPassword(user.getPassword());
                userServiceImpl.saveUserWithRole(existingUser);
            } else {
                System.out.println("User not found, saving new user.");
                userServiceImpl.saveUserWithRole(user);
            }
        } else {
            System.out.println("Creating new user.");
            userServiceImpl.saveUserWithRole(user);
        }
        return "redirect:/admin/";
    }


    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userServiceImpl.delete(id);
        return "redirect:/admin/";
    }

    @PostMapping("/admin/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userServiceImpl.findById(id);
        Role role = user.getRoles().get(0);
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        return "add-user";
    }
}
