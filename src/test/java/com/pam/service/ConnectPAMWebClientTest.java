package com.pam.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ConnectPAMWebClientTest {
    @Autowired
    ConnectPAMWebClient connectPAMWebClient;

    @Test
    void fetchDataFromService() {
        connectPAMWebClient.fetchDataFromService();
    }
}
