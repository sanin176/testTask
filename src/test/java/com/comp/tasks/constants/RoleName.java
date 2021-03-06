package com.comp.tasks.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum RoleName {
    ROLE_OWNER("owner", "ROLE_OWNER"),
    ROLE_ADMIN("admin", "ROLE_ADMIN"),
    ROLE_USER("user", "ROLE_USER");

    @Getter
    String name;
    @Getter
    private final String role;

    private static final Map<String, RoleName> BY_NAME_MAP = new LinkedHashMap<>();

    static {
        for (RoleName roleName : RoleName.values()) {
            BY_NAME_MAP.put(roleName.getName(), roleName);
        }
    }

    public static RoleName getRoleNameByName(String name) {
        return BY_NAME_MAP.get(name);
    }
}
