package com.pam.service;

import com.pam.dto.PAMResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Gateway for PAM Service
 */
@Slf4j
@Component
public class ConnectPAMWebClient {

    @Value("${pam.url}")
    private String pamUrl;

    @Autowired
    private WebClient pamWebClient;

    /**
     * Fetches Credential from PAM Gateway
     */
    public PAMResponseDTO fetchDataFromService() {
        log.info("Calling PAM to get Credential...");
        return pamWebClient.get()
                .uri(pamUrl)
                .retrieve()
                .bodyToMono(PAMResponseDTO.class)
                .doOnSuccess(response -> {
                    log.info("Response received from PAM: {}", response);
                })
                .onErrorResume(Exception.class, e -> {
                    log.error(e.getMessage());
                    return Mono.empty();
                })
                .block();
    }
}