package com.cinestream.auth_service.controller;

import com.cinestream.auth_service.domain.Role;
import com.cinestream.auth_service.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public Role create(@RequestParam("name") String name) {
        return roleService.createRole(name);
    }

    @GetMapping("/{id}")
    public Role get(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @GetMapping
    public List<Role> list() {
        return roleService.getAllRoles();
    }

    @PutMapping("/{id}")
    public Role update(
            @PathVariable Long id,
            @RequestParam String name) {

        return roleService.updateRole(id, name);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roleService.deleteRole(id);
    }
}
