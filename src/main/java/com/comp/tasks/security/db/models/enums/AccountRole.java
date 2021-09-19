package com.comp.tasks.security.db.models.enums;


import org.springframework.data.r2dbc.convert.EnumWriteSupport;

public enum AccountRole {
    ROLE_OWNER, ROLE_ADMIN, ROLE_USER;

    public static class AccountRoleWriting extends EnumWriteSupport<AccountRole> {

    }
}
