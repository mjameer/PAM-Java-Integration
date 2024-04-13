package com.pam.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConnectPAMTest {

    @Autowired
    ConnectPAM connectPAM;

    @Test
    void fetchCredential() {
        connectPAM.fetchCredential();
    }

}