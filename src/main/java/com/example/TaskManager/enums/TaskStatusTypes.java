package com.example.TaskManager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatusTypes {
    DRAFT("Draft"),
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELED("Canceled"),
    DELETED("Deleted");

    private final String type;
}