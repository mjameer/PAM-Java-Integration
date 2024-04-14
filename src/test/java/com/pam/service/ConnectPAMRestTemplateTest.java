package com.pam.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConnectPAMRestTemplateTest {

    @Autowired
    ConnectPAMRestTemplate connectPAMRestTemplate;

    @Test
    void fetchDataFromService() {
        connectPAMRestTemplate.fetchDataFromService();
    }
}