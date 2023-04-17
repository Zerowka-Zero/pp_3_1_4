package ru.ryazan.pp_3_1_3.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ryazan.pp_3_1_3.exceprion.RolenameAlreadyExistsException;
import ru.ryazan.pp_3_1_3.exceprion.UsernameAlreadyExistsException;
import ru.ryazan.pp_3_1_3.models.Role;
import ru.ryazan.pp_3_1_3.models.User;
import ru.ryazan.pp_3_1_3.services.RoleService;
import ru.ryazan.pp_3_1_3.services.UserService;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
public class UtilStart {
    private final RoleService roleService;
    private final UserService userService;
    @Autowired
    public UtilStart(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostConstruct
    public void init() throws UsernameAlreadyExistsException, RolenameAlreadyExistsException {
        if (roleService.findByName("ROLE_USER").isEmpty()) {
            roleService.save(new Role("ROLE_USER"));
        }
        if (roleService.findByName("ROLE_ADMIN").isEmpty()) {
            roleService.save(new Role("ROLE_ADMIN"));
        }
        if (userService.findByEmail("admin@ya.ru").isEmpty()) {
            userService.register(new User("admin", "admin", 100, "admin@ya.ru", "admin",List.of(roleService.getAdmin(), roleService.getUser())));
        }
        if (userService.findByEmail("test@ya.ru").isEmpty()) {
            userService.register(new User("test", "test", 100, "test@ya.ru", "test", Collections.singleton(roleService.getUser())));
        }
    }
}

