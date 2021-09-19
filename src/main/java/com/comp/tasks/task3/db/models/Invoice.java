package com.comp.tasks.task3.db.models;

import com.comp.tasks.task2.db.models.Auditable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true, onlyExplicitlyIncluded = true, callSuper = false)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice extends Auditable implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    Long id;
    String invoiceId;
    String login;
}
