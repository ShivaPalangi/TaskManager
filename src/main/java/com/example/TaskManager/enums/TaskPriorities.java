package com.example.TaskManager.enums;

public enum TaskPriorities {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical");

    private final String priority;

    TaskPriorities(String priority){
        this.priority = priority;
    }
}