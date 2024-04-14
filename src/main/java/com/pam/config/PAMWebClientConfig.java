package com.pam.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Slf4j
@Configuration
public class PAMWebClientConfig {

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${pam.keystore.path}")
    private String keystore;

    @Value("${pam.keystore.key.password}")
    private String keystoreKeyPassword;

    @Bean
    public WebClient pamWebClient() {

        Resource keyStoreResource = resourceLoader.getResource("classpath:" + keystore);

        try (FileInputStream keyStoreFileInputStream = new FileInputStream(keyStoreResource.getFile());
                 FileInputStream trustStoreFileInputStream =new FileInputStream(keyStoreResource.getFile());
            ) {
                KeyStore keyStore = KeyStore.getInstance("jks");
                keyStore.load(keyStoreFileInputStream, keystoreKeyPassword.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                keyManagerFactory.init(keyStore, keystoreKeyPassword.toCharArray());

                KeyStore trustStore = KeyStore.getInstance("jks");
                trustStore.load(trustStoreFileInputStream, keystoreKeyPassword.toCharArray());
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
                trustManagerFactory.init(trustStore);

            SslContext build = SslContextBuilder.forClient()
                    .keyManager(keyManagerFactory)
                   // .trustManager(trustManagerFactory)
                    .build();


            HttpClient httpClient = HttpClient.create()
                    .secure(sslSpec -> sslSpec.sslContext(build));

            ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

            return WebClient.builder()
                    .clientConnector(connector)
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                            .build())
                    .build();
        } catch (IOException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException | KeyStoreException e) {
            log.error("Error initializing WebClient: {}", e.getMessage());
            throw new RuntimeException("Error initializing WebClient", e);
        }
    }
}
