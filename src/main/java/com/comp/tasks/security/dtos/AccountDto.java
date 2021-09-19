package com.comp.tasks.security.dtos;

import com.comp.tasks.security.db.models.enums.AccountRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDto {
    String login;
    String name;
    String surname;
    AccountRole role;
    Timestamp dateBirth;
}
