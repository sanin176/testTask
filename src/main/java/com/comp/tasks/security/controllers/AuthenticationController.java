package com.comp.tasks.security.controllers;

import com.comp.tasks.security.dtos.TokenInfoDto;
import com.comp.tasks.security.dtos.requests.AccountRequest;
import com.comp.tasks.security.dtos.requests.CredentialsRequest;
import com.comp.tasks.security.exceptions.ExceptionResponse;
import com.comp.tasks.security.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/comp/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ApiResponses(value = {
        @ApiResponse(responseCode = "400",
                description = "Bad Request",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "403",
                description = "Forbidden",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404",
                description = "Not Found",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "500",
                description = "Internal Server Error",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ExceptionResponse.class)))
})
public class AuthenticationController {
    AccountService accountService;

    @PostMapping("/accounts/sign-up")
    @Operation(summary = "Create account")
    @ApiResponse(responseCode = "200")
    public Mono<TokenInfoDto> createAccount(@RequestBody AccountRequest accountRequest) {
        return accountService.createAccount(accountRequest);
    }

    @PostMapping("/accounts/sign-in")
    @Operation(summary = "Getting token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "412",
                    description = "Blocking authorization (in minutes)/ use field 'message' for getting minutes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public Mono<TokenInfoDto> login(@RequestBody CredentialsRequest credentials) {
        return accountService.login(credentials);
    }
}
