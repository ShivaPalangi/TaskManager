package com.example.TaskManager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskCategories {
    ORDINARY("Ordinary"),
    PERIODICAL("Periodical");

    private final String category;
}