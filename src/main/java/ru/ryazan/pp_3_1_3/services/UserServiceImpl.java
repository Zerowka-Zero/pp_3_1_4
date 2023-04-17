package ru.ryazan.pp_3_1_3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ryazan.pp_3_1_3.exceprion.RolenameAlreadyExistsException;
import ru.ryazan.pp_3_1_3.exceprion.UsernameAlreadyExistsException;
import ru.ryazan.pp_3_1_3.models.User;
import ru.ryazan.pp_3_1_3.repositors.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    @Lazy
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
    }

    @Transactional
    @Override
    public void register(User user) throws UsernameAlreadyExistsException, RolenameAlreadyExistsException {
        if (userRepository.findByEmail(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Пользователь с таким именем есть в базе данных");
        }
        if (!user.getRole().contains(roleService.getUser())) {
            user.getRole().add(roleService.getUser());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String name) {
        return userRepository.findByEmail(name);
    }

    @Transactional
    @Override
    public void save(User user) throws RolenameAlreadyExistsException {
        if (userRepository.findById(user.getId()).isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (!user.getRole().contains(roleService.getUser())) {
            user.getRole().add(roleService.getUser());
        }
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
