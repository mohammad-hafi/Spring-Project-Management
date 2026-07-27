package com.example.projectmanagement.Domain.Enums;

public enum UserStatus {
    ACTIVE(1),
    INACTIVE(2),
    SUSPENDED(3);

    private final int id;

    UserStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static UserStatus fromId(int id) {
        for (UserStatus status : values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status ID: " + id);
    }
}
