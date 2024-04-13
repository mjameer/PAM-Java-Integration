package com.pam.service;

import com.pam.dto.PAMResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Gateway for PAM Service
 */
@Slf4j
@Component
public class ConnectPAM {

    @Value("${pam.url}")
    private String pamUrl;

    @Autowired
    private RestTemplate pamRestTemplate;

    /**
     * Fetches Credential from PAM Gateway
     */
    public void fetchCredential() {
        try {
            log.info("Calling PAM to get Credential...");
            PAMResponseDTO response = pamRestTemplate.getForObject(pamUrl, PAMResponseDTO.class);
            log.info("Response received from PAM: {}", response);
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
    }
}