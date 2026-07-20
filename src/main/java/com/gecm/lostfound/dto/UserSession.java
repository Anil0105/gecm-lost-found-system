package com.gecm.lostfound.dto;

import com.gecm.lostfound.model.UserRole;

public class UserSession {

    private final Integer id;
    private final String name;
    private final String email;
    private final UserRole role;

    public UserSession(Integer id, String name, String email, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role == UserRole.admin;
    }
}
