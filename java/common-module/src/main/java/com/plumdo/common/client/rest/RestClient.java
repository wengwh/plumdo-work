package com.plumdo.common.client.rest;

import com.plumdo.common.model.Authentication;
import com.plumdo.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 调用http接口封装工具类
 *
 * @author wengwenhui
 * @date 2018年4月20日
 */
@Slf4j
public class RestClient {
    private RestTemplate restTemplate;
    private ServiceUrl serviceUrl;

    public RestClient(RestTemplate restTemplate, ServiceUrl serviceUrl) {
        this.restTemplate = restTemplate;
        this.serviceUrl = serviceUrl;
    }

    public <T> T getForIdentityService(String url, Class<T> responseType) {
        return getForIdentityService(url, null, responseType);
    }

    public <T> T getForIdentityService(String url, MultiValueMap<String, String> queryParams, Class<T> responseType) {
        return exchange(serviceUrl.getIdentityService() + url, HttpMethod.GET, queryParams, null, responseType);
    }

    public <T> T get(String url, Class<T> responseType) {
        return exchange(url, HttpMethod.GET, null, null, responseType);
    }

    public <T> T exchange(String url, HttpMethod method, MultiValueMap<String, String> queryParams, Object requestBody, Class<T> responseType) {
        T response = null;
        long beginTime = System.currentTimeMillis();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            builder.queryParams(queryParams);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Token", Authentication.getToken());
            response = restTemplate.exchange(builder.build().toUri(), method, new HttpEntity<>(requestBody, headers), responseType).getBody();
            log.debug("发送Http请求:{},方法:{},参数:{},返回:{},[{}]ms", url, method, requestBody, response, DateUtils.getTimeMillisConsume(beginTime));
        } catch (RestClientException e) {
            log.error("发送http请求:{},方法:{},参数:{},异常:{},[{}]ms", url, method, requestBody, e.getMessage(), DateUtils.getTimeMillisConsume(beginTime));
        }

        return response;
    }

}