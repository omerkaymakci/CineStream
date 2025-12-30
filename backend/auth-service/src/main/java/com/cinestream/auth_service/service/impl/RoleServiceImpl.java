package com.cinestream.auth_service.service.impl;

import com.cinestream.auth_service.domain.Role;
import com.cinestream.auth_service.exception.AlreadyExistsException;
import com.cinestream.auth_service.exception.ResourceNotFoundException;
import com.cinestream.auth_service.repository.RoleRepository;
import com.cinestream.auth_service.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(String name) {
        if (roleRepository.existsByName(name)) {
            throw new AlreadyExistsException("Role already exists: " + name);
        }
        return roleRepository.save(new Role(name));
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found with id: " + id));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role updateRole(Long id, String name) {
        Role role = getRoleById(id);

        if (!role.getName().equals(name) && roleRepository.existsByName(name)) {
            throw new AlreadyExistsException("Role already exists: " + name);
        }

        role.setName(name);
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }
}
