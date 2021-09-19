package com.comp.tasks.task3.services;

import com.comp.tasks.security.db.repositories.AccountRepository;
import com.comp.tasks.security.exceptions.BadRequestException;
import com.comp.tasks.security.jwt.CompAuthenticationToken;
import com.comp.tasks.task3.db.models.Invoice;
import com.comp.tasks.task3.db.repositories.InvoiceRepository;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceService {

    @Value("${stripe.api.auth}")
    @NonFinal
    String AUTH_HEADER;
    @Value("${stripe.api.url}")
    @NonFinal
    String url;
    @Value("${stripe.api.test-token}")
    @NonFinal
    String testToken;
    @Value("${stripe.api.invoices}")
    @NonFinal
    String invoices;

    InvoiceRepository invoiceRepository;
    AccountRepository accountRepository;

    /*
     * TODO
     *  if correct, then you need to contact another authorization service,
     *  for example via GRPS, rsocket or Kafka, to get the user id in order to bind this
     *  record to it, but I do it without microservices
     *  but I will just refer to the account
     * */
    public Mono<Object> createInvoice(CompAuthenticationToken compAuthenticationToken) {
        WebClient webClient = WebClient.builder().baseUrl(url)
                .defaultHeader(AUTH_HEADER, testToken)
                .build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        return accountRepository.findByLogin(compAuthenticationToken.getEmail())
                .switchIfEmpty(Mono.error(new BadRequestException("Account does not exists.")))
                .flatMap(account -> invoiceRepository.findByLogin(compAuthenticationToken.getEmail())
                        .switchIfEmpty(Mono.just(Invoice.builder().id(-1L).build()))
                        .flatMap(invoice -> {
                            if (invoice.getId().equals(-1L)) {
                                formData.add("customer", account.getCustomerInvoiceId());
                                return webClient.post()
                                        .uri(invoices)
                                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                        .body(BodyInserters.fromFormData(formData))
                                        .accept(MediaType.APPLICATION_JSON)
                                        .retrieve()
                                        .bodyToMono(JSONObject.class)
                                        .flatMap(result -> invoiceRepository.save(Invoice.builder()
                                                .login(compAuthenticationToken.getEmail())
                                                .invoiceId(String.valueOf(result.get("id")))
                                                .build())
                                                .thenReturn(result));
                            }
                            return Mono.error(new BadRequestException("You can create only one invoice."));
                        }));
    }

    /*
     * TODO
     *  if correct, then you need to contact another authorization service,
     *  for example via GRPS, rsocket or Kafka, to get the user id in order to bind this
     *  record to it, but I do it without microservices
     *  but I will just refer to the account
     * */
    public Mono<Object> getInvoice(CompAuthenticationToken compAuthenticationToken) {
        WebClient webClient = WebClient.builder().baseUrl(url)
                .defaultHeader(AUTH_HEADER, testToken)
                .build();

        return invoiceRepository.findByLogin(compAuthenticationToken.getEmail())
                .switchIfEmpty(Mono.error(new BadRequestException("Invoice does not exists.")))
                .flatMap(invoice -> webClient.get()
                        .uri(invoices + "/" + invoice.getInvoiceId())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(JSONObject.class));
    }
}
