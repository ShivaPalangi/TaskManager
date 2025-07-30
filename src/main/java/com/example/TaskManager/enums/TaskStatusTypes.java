package com.example.TaskManager.enums;

public enum TaskStatusTypes {
    DRAFT("Draft"),
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELED("Canceled"),
    DELETED("Deleted");

    private final String type;

    TaskStatusTypes(String type){
        this.type = type;
    }

}