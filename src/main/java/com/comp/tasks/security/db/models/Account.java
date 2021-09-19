package com.comp.tasks.security.db.models;

import com.comp.tasks.security.db.models.enums.AccountRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
public class Account implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    Long id;
    String login;
    String password;
    String name;
    String surname;
    AccountRole role;
    Timestamp createdDate;
    Timestamp lastLoginAt;
    String customerInvoiceId;
}
