package com.example.TaskManager.enums;

public enum TaskCategories {
    ORDINARY("Ordinary"),
    PERIODICAL("Periodical");

    private final String category;

    TaskCategories(String category){
        this.category = category;
    }
}