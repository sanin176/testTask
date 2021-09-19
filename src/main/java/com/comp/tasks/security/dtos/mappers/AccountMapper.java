package com.comp.tasks.security.dtos.mappers;

import com.comp.tasks.security.db.models.Account;
import com.comp.tasks.security.dtos.AccountDto;
import com.comp.tasks.security.dtos.requests.AccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toUserDto(Account account);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Account toUser(AccountRequest accountRequest);
}
