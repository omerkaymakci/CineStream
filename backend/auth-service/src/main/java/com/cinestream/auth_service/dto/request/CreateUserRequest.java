package com.cinestream.auth_service.dto.request;

import java.util.List;

public class CreateUserRequest {

    private String username;
    private String password;
    private boolean enabled = true; // opsiyonel, default true
    private List<String> roles; // örn. ["USER"], ["ADMIN"]

    public CreateUserRequest() {}

    public CreateUserRequest(String username, String password, boolean enabled, List<String> roles) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

