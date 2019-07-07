package com.plumdo.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plumdo.common.client.jdbc.JdbcClient;
import com.plumdo.common.client.rest.RestClient;
import com.plumdo.common.client.rest.ServiceUrl;
import com.plumdo.common.exception.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.util.Locale;

/**
 * 程序相关配置
 *
 * @author wengwenhui
 * @date 2018年4月20日
 */
@Configuration
public class ApplicationConfig {
    private final MessageSource messageSource;
    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate;

    @Autowired
    public ApplicationConfig(MessageSource messageSource, JdbcTemplate jdbcTemplate, RestTemplate restTemplate) {
        this.messageSource = messageSource;
        this.jdbcTemplate = jdbcTemplate;
        this.restTemplate = restTemplate;
    }

    @Bean
    @ConfigurationProperties(prefix = "serviceUrl")
    public ServiceUrl serviceUrl() {
        return new ServiceUrl();
    }

    @Bean
    public ExceptionFactory exceptionFactory() {
        return new ExceptionFactory(messageSource);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    @Bean
    public JdbcClient jdbcClient() {
        return new JdbcClient(jdbcTemplate);
    }

    @Bean
    public RestClient restClient() {
        return new RestClient(restTemplate, serviceUrl());
    }

}
