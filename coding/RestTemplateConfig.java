package com.guide.common.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class RestTemplateConfig {

    @Value("${http.connection-timeout}")
    private int connectionTimeout;

    @Value("${http.read-timeout}")
    private int readTimeout;

    @Value("${http.max-connect}")
    private int maxConnect;

    @Value("${http.max-route}")
    private int maxRoute;

    @Bean
    public CloseableHttpClient httpClient() {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(maxConnect) // 최대 오픈되는 connection
                .setMaxConnPerRoute(maxRoute) // IP, 포트 1쌍에 대해 수행할 커넥션 수
                .build();

        return httpClient;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(readTimeout); // 읽기시간초과, 30초
        //factory.setConnectTimeout(connectionTimeout); // 연결시간초과, 30초
        factory.setHttpClient(httpClient());

        return factory;
    }

    @Bean
    public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {

        RestTemplate restTemplate = new RestTemplate(factory);

		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

	
		return restTemplate;
    }


}
