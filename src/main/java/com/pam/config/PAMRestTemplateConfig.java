package com.pam.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.DefaultHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Slf4j
@Configuration
public class PAMRestTemplateConfig {

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${pam.keystore.path}")
    private String keystore;

    @Value("${pam.keystore.key.password}")
    private String keystoreKeyPassword;

    @Bean
    public RestTemplate pamRestTemplate(RestTemplateBuilder builder) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + keystore);
            log.info("Cert path: {}", resource.getFile().getAbsolutePath());

            final SSLContext sslContext = new SSLContextBuilder()
                    .loadKeyMaterial(resource.getFile(), keystoreKeyPassword.toCharArray(), keystoreKeyPassword.toCharArray())
                    .build();
            final ConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new DefaultHostnameVerifier());
            final Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("https", connectionSocketFactory)
                            .register("http", new PlainConnectionSocketFactory())
                            .build();
            final BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);

            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();

            return builder
                    .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                    .build();
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException |
                 UnrecoverableKeyException | KeyManagementException e) {
            log.error("Error initializing HTTP client: {}", e.getMessage());
            throw new RuntimeException("Error initializing HTTP client", e);
        }

    }
}
