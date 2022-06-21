package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String getUsers(Model model, Principal principal) {
        User userPrincipal = userService.findByName(principal.getName());
        model.addAttribute("user", userPrincipal);
        model.addAttribute("newUser", new User());
        model.addAttribute("allUsers", userService.getAllUsers());
        return "allUsers";
    }

    @PostMapping
    public String create(@ModelAttribute("newUser") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("userEdit", userService.getUser(id));
        return "/edit";
    }

    @PatchMapping ("/{id}")
    public String update(@ModelAttribute("userEdit") User user, @PathVariable("id") Long id) {
        user.setId(id);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}