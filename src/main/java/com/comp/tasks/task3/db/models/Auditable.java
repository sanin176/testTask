package com.comp.tasks.task3.db.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Auditable {
    @CreatedBy
    String createdBy;
    @CreatedDate
    LocalDateTime createdDate;
    @LastModifiedBy
    String lastModifiedBy;
    @LastModifiedDate
    LocalDateTime lastModifiedDate;
}
