package com.comp.tasks.security.services;

import com.comp.tasks.security.db.models.Account;
import com.comp.tasks.security.db.models.enums.AccountRole;
import com.comp.tasks.security.db.repositories.AccountRepository;
import com.comp.tasks.security.dtos.TokenInfoDto;
import com.comp.tasks.security.dtos.mappers.AccountMapper;
import com.comp.tasks.security.dtos.requests.AccountRequest;
import com.comp.tasks.security.dtos.requests.CredentialsRequest;
import com.comp.tasks.security.exceptions.BadRequestException;
import com.comp.tasks.security.jwt.JWTUtil;
import com.comp.tasks.security.utils.DateUtils;
import com.comp.tasks.security.utils.PBKDF2Encoder;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {

    @Value("${stripe.api.auth}")
    @NonFinal
    String AUTH_HEADER;
    @Value("${stripe.api.url}")
    @NonFinal
    String url;
    @Value("${stripe.api.test-token}")
    @NonFinal
    String testToken;
    @Value("${stripe.api.customers}")
    @NonFinal
    String customers;
    @Value("${stripe.api.invoiceitems}")
    @NonFinal
    String invoiceitems;

    JWTUtil jwtUtil;
    PBKDF2Encoder pbkdf2Encoder;
    AccountRepository accountRepository;

    AccountMapper userMapper;

    public Mono<TokenInfoDto> login(CredentialsRequest credentials) {
        Timestamp timeNow = DateUtils.getCurrentUtcTime();
        return accountRepository.findByLogin(credentials.getLogin())
                .switchIfEmpty(Mono.error(new BadRequestException("Account not found")))
                .flatMap(account -> {
                    if (pbkdf2Encoder.encode(credentials.getPassword()).equals(account.getPassword())) {
                        return createToken(account)
                                .map(res -> TokenInfoDto.builder()
                                        .accessToken(jwtUtil.generateToken(account.getLogin(),
                                                account.getRole().name()))
                                        .user(userMapper.toUserDto(account))
                                        .build());
                    }
                    account.setLastLoginAt(timeNow);
                    return accountRepository.save(account)
                            .flatMap(res -> Mono.error(new BadRequestException("Wrong password")));
                })
                .switchIfEmpty(Mono.error(new BadRequestException()))
                .doOnError(error -> log.warn("Error in login: {}", error.getMessage()));
    }

    private Mono<Account> createToken(Account account) {
        account.setLastLoginAt(DateUtils.getCurrentUtcTime());
        return accountRepository.save(account);
    }

    public Mono<TokenInfoDto> createAccount(AccountRequest request) {
        WebClient webClient = WebClient.builder().baseUrl(url)
                .defaultHeader(AUTH_HEADER, testToken)
                .build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("price", "price_1JbNNqIFX9QksJzEtbQotfZg");
        return webClient.post()
                .uri(customers)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JSONObject.class)
                .flatMap(result -> {
                    formData.add("customer", String.valueOf(result.get("id")));
                    /*
                    * in this case, it will be executed asynchronously,
                    * so as not to delay the request, since in this case we
                    * only need the request to be executed, but we do not need
                    * to return any data from it to the screen.
                    * */
                    webClient.post()
                            .uri(invoiceitems)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .body(BodyInserters.fromFormData(formData))
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(JSONObject.class)
                            .subscribe();
                    return Mono.justOrEmpty(request)
                            .flatMap(res -> accountRepository.findByLogin(request.getLogin())
                                    .flatMap(account -> Mono.error(new BadRequestException("User with this login already exists.")))
                                    .thenReturn(res))
                            .flatMap(accountRequest -> buildNewAccount(accountRequest, String.valueOf(result.get("id")))
                                    .flatMap(account -> accountRepository.save(account)
                                            .map(userMapper::toUserDto)
                                            .map(accountDto -> TokenInfoDto.builder()
                                                    .user(accountDto)
                                                    .accessToken(jwtUtil.generateToken(account.getLogin(),
                                                            account.getRole().name()))
                                                    .build())))
                            .doOnError(error -> log.warn("Error in createAccount: {}", error.getMessage()));
                });
    }

    private Mono<Account> buildNewAccount(AccountRequest accountRequest, String customerInvoiceId) {
        return Mono.justOrEmpty(accountRequest)
                .map(userMapper::toUser)
                .map(account -> {
                    account.setCustomerInvoiceId(customerInvoiceId);
                    account.setRole(AccountRole.ROLE_USER);
                    account.setCreatedDate(DateUtils.getCurrentUtcTime());
                    account.setLastLoginAt(DateUtils.getCurrentUtcTime());
                    account.setPassword(pbkdf2Encoder.encode(accountRequest.getPassword()));
                    return account;
                });
    }

}
