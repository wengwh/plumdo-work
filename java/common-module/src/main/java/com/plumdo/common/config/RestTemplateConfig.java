package com.plumdo.common.config;

import com.plumdo.common.constant.CoreConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 定义restTemplate的配置
 *
 * @author wengwh
 * @date 2018年4月21日
 */
@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    @ConditionalOnMissingBean({RestOperations.class, RestTemplate.class})
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setErrorHandler(new CustomerErrorHandler());

        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.removeIf(converter -> converter instanceof StringHttpMessageConverter);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName(CoreConstant.DEFAULT_CHARSET)));

        return restTemplate;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.rest")
    @ConditionalOnMissingBean({ClientHttpRequestFactory.class})
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        httpClientBuilder.setSSLContext(sslContext());

        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));

        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager());

        return httpClientBuilder.build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.rest.pool")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        return new PoolingHttpClientConnectionManager(socketFactoryRegistry());
    }

    @Bean
    public SSLContext sslContext() {
        try {
            return new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("init httpclient bean exception", e);
        }
        return null;
    }

    @Bean
    public Registry<ConnectionSocketFactory> socketFactoryRegistry() {
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext(),
                hostnameVerifier);

        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslConnectionSocketFactory).build();
    }

}

/**
 * 异常处理，只打印异常，不做处理
 *
 * @author wengwh
 * @date 2018年5月10日
 */
@Slf4j
class CustomerErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("http request error,statusCode:{},body:{}", getHttpStatusCode(response), new String(getResponseBody(response)));
    }
}