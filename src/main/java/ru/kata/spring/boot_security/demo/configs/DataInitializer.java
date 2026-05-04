package ru.kata.spring.boot_security.demo.configs;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserService userService;

    public DataInitializer(RoleRepository roleRepository,
                           UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {

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


        if (userService.findByEmail("Smirnov@mail.ru") == null) {
            User user = new User("Smirnov", "Viktor", "Smirnov@mail.ru", "user");
            user.setAge(25);
            user.setRoles(Set.of(userRole));
            userService.addUser(user);
        }

        if (userService.findByEmail("Kulaksazyan@list.ru") == null) {
            User admin = new User("George", "Kulaksazyan", "Kulaksazyan@list.ru", "admin");
            admin.setAge(30);

            admin.setRoles(Set.of(adminRole, userRole));
            userService.addUser(admin);
        }
    }
}