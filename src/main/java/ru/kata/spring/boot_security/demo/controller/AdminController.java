package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // 🔹 список пользователей (главная админка)
    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }

    // 🔹 форма создания
    @GetMapping("/adduser")
    public String createUserForm(@ModelAttribute("user") User user) {
        return "adduser";
    }

    // 🔹 создание
    @PostMapping("/adduser")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "adduser";
        }
        userService.addUser(user);
        return "redirect:/admin";
    }

    // 🔹 удаление
    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    // 🔹 форма редактирования
    @GetMapping("/updateuser")
    public String getEditUserForm(Model model,
                                  @RequestParam("id") long id) {
        model.addAttribute("user", userService.getUser(id));
        return "updateuser";
    }

    // 🔹 обновление
    @PostMapping("/updateuser")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "updateuser";
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }
}