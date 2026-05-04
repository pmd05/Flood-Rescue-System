package com.example.floodrescue.model;

import java.util.HashMap;
import java.util.Map;

public enum AppRole {
    CITIZEN("citizen"),
    RESCUE_TEAM("rescue_team"),
    COORDINATOR("coordinator"),
    MANAGER("manager"),
    ADMIN("admin");

    private final String dbName;

    private static final Map<String, AppRole> ROLE_MAP = new HashMap<>();

    static {
        for (AppRole role : values()) {
            ROLE_MAP.put(role.dbName.toLowerCase(), role);
        }
    }

    AppRole(String dbName) {
        this.dbName = dbName;
    }

    public String dbName() {
        return dbName;
    }

    public String authority() {
        return "ROLE_" + name();
    }

    public static AppRole fromDbName(String dbName) {
        if (dbName == null || dbName.isBlank()) {
            throw new RuntimeException("Role is blank");
        }

        AppRole role = ROLE_MAP.get(dbName.toLowerCase());

        if (role == null) {
            throw new RuntimeException("Unknown role: " + dbName);
        }

        return role;
    }
}