package com.cinestream.auth_service.service;

import com.cinestream.auth_service.domain.Role;

import java.util.List;

public interface RoleService {
    Role createRole(String name);

    Role getRoleById(Long id);

    List<Role> getAllRoles();

    Role updateRole(Long id, String name);

    void deleteRole(Long id);
}
