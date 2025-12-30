package com.cinestream.auth_service.controller;

import com.cinestream.auth_service.domain.User;
import com.cinestream.auth_service.dto.request.CreateUserRequest;
import com.cinestream.auth_service.exception.ResourceNotFoundException;
import com.cinestream.auth_service.repository.RoleRepository;
import com.cinestream.auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(PasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/createUser")
    public User createUser(@RequestBody CreateUserRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);

        user.setRoles(
                request.getRoles().stream()
                        .map(id -> roleRepository.findByName(id)
                                .orElseThrow(() -> new ResourceNotFoundException(id)))
                        .collect(Collectors.toSet())
        );

        return userRepository.save(user);
    }
}
