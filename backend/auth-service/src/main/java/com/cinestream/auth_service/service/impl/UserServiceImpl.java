package com.cinestream.auth_service.service.impl;

import com.cinestream.auth_service.domain.Role;
import com.cinestream.auth_service.domain.User;
import com.cinestream.auth_service.dto.request.CreateUserRequest;
import com.cinestream.auth_service.exception.AlreadyExistsException;
import com.cinestream.auth_service.exception.ResourceNotFoundException;
import com.cinestream.auth_service.repository.RoleRepository;
import com.cinestream.auth_service.repository.UserRepository;
import com.cinestream.auth_service.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException(
                    "User already exists: " + request.getUsername()
            );
        }

        Set<Role> roles = request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, CreateUserRequest request) {

        User user = getUserById(id);

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}

