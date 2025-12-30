package com.cinestream.auth_service.service;

import com.cinestream.auth_service.domain.User;
import com.cinestream.auth_service.dto.request.CreateUserRequest;

import java.util.List;

public interface UserService {

    User createUser(CreateUserRequest request);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, CreateUserRequest request);

    void deleteUser(Long id);
}

