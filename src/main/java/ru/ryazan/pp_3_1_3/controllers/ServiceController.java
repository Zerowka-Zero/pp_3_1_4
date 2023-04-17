package ru.ryazan.pp_3_1_3.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.ryazan.pp_3_1_3.exceprion.RolenameAlreadyExistsException;
import ru.ryazan.pp_3_1_3.exceprion.UsernameAlreadyExistsException;
import ru.ryazan.pp_3_1_3.models.User;
import ru.ryazan.pp_3_1_3.services.UserService;

@Controller
public class ServiceController {

    private final UserService userService;

    public ServiceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/service/login_page";
    }

    @PostMapping("/registration")
    public String registrationUser(@ModelAttribute("user") User user) throws UsernameAlreadyExistsException, RolenameAlreadyExistsException {
        userService.register(user);
        return "redirect:/admin";
    }
    @GetMapping("/user")
    public String userStart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "/service/user_page";
    }
}
