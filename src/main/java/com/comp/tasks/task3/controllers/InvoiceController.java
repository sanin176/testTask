package com.comp.tasks.task3.controllers;

import com.comp.tasks.security.exceptions.ExceptionResponse;
import com.comp.tasks.security.jwt.CompAuthenticationToken;
import com.comp.tasks.task3.services.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
public class InvoiceController {
    InvoiceService invoiceService;

    @PostMapping("/invoice")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Create invoice", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201")
    public Mono<Object> createInvoice(@AuthenticationPrincipal CompAuthenticationToken compAuthenticationToken) {
        return invoiceService.createInvoice(compAuthenticationToken);
    }

    @GetMapping("/invoice")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Get invoice", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200")
    public Mono<Object> getInvoice(@AuthenticationPrincipal CompAuthenticationToken compAuthenticationToken) {
        return invoiceService.getInvoice(compAuthenticationToken);
    }
}
