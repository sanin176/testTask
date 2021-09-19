package com.comp.tasks.security.dtos.requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {
    String login;
    String password;
    String name;
    String surname;
}
