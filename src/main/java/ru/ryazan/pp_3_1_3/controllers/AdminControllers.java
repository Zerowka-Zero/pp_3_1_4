package ru.ryazan.pp_3_1_3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ryazan.pp_3_1_3.exceprion.RolenameAlreadyExistsException;
import ru.ryazan.pp_3_1_3.exceprion.UsernameAlreadyExistsException;
import ru.ryazan.pp_3_1_3.models.User;
import ru.ryazan.pp_3_1_3.services.RoleService;
import ru.ryazan.pp_3_1_3.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminControllers {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AdminControllers(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String adminPanel(Model model) throws InterruptedException, RolenameAlreadyExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("admin", user);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", List.of(roleService.getAdmin(), roleService.getUser()));
        model.addAttribute("new_user", new User());
        return "admin/admin_panel_bootstrap";
    }

    @PatchMapping("/{id}")
    public String editUser(@ModelAttribute("user") User userEdit, @PathVariable("id") Long id) throws UsernameAlreadyExistsException, RolenameAlreadyExistsException {
        User user = userService.findById(id)
                .orElseThrow(() -> new UsernameAlreadyExistsException("Пользователь с таким id для редактирования не найден"));
        if (!userEdit.getPassword().equals("")) {
            userEdit.setPassword(passwordEncoder.encode(userEdit.getPassword()));
        } else {
            userEdit.setPassword(user.getPassword());
        }
        userService.save(userEdit);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

}
