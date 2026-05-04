package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;   // ← интерфейс
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;   // ← интерфейс

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByEmail(auth.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "index";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User user,
                          @RequestParam(value = "roles", required = false) List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            Role defaultRole = roleService.findByName("ROLE_USER");
            user.setRoles(Set.of(defaultRole));
        } else {
            Set<Role> roles = roleService.findByIds(roleIds);
            user.setRoles(roles);
        }
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/updateuser")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam(value = "roles", required = false) List<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            user.setRoles(roleService.findByIds(roleIds));
        } else {
            user.setRoles(Set.of(roleService.findByName("ROLE_USER")));
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}