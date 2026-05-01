package ru.kata.spring.boot_security.demo.configs;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        // 1. Создаём роли, если их нет
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        // 2. Создаём демо-пользователей для теста (опционально)
        if (userService.findByEmail("user@example.com") == null) {
            User user = new User("User", "Userov", "user@example.com",
                    passwordEncoder.encode("user"));
            user.setRoles(Set.of(userRole));
            userService.addUser(user);
        }

        if (userService.findByEmail("admin@example.com") == null) {
            User admin = new User("Admin", "Adminov", "admin@example.com",
                    passwordEncoder.encode("admin"));
            admin.setRoles(Set.of(adminRole, userRole)); // админ с двумя ролями
            userService.addUser(admin);
        }
    }
}