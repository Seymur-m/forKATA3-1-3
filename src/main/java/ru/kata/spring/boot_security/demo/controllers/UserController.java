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
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@Controller
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping("/admin")
    public String forAdmin(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("listUsers", userServiceImpl.findAll());
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleServiceImpl.findAll()); // Добавляем список всех ролей
        model.addAttribute("currentuser", currentUser);
        return "users";
    }

    @GetMapping("/user")
    public String forUser(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("currentuser", currentUser);
        return "user";
    }

    @GetMapping("/admin/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleServiceImpl.findAll());
        return "add-user";
    }

    @PostMapping("/admin/save")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("roleIds") List<Long> roleIds) {
        // Получаем список ролей по их ID
        List<Role> roles = roleServiceImpl.findRolesByIds(roleIds);
        user.setRoles(roles); // Устанавливаем роли пользователю
        userServiceImpl.saveUserWithRole(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userServiceImpl.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userServiceImpl.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleServiceImpl.findAll()); // Передаем все доступные роли
        return "add-user";
    }
}