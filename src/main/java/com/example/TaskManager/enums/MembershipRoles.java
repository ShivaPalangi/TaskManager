package com.example.TaskManager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MembershipRoles {
    OWNER("Owner"),
    ADMIN("Admin"),
    MEMBER("Member");

    private final String role;
}