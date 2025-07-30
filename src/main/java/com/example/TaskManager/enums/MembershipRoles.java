package com.example.TaskManager.enums;

public enum MembershipRoles {
    OWNER("Owner"),
    ADMIN("Admin"),
    MEMBER("Member");

    private final String role;

    MembershipRoles(String role){
        this.role = role;
    }
}