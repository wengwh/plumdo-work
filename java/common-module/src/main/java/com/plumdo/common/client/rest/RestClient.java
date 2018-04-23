package com.plumdo.common.client.rest;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.plumdo.common.model.Authentication;
import com.plumdo.common.utils.DateUtils;

/**
 * 调用http接口封装工具类
 *
 * @author wengwenhui
 * @date 2018年4月20日
 */
public class RestClient {
	private final Logger logger = LoggerFactory.getLogger(RestClient.class);

	private RestTemplate restTemplate;
	private ServiceUrl serviceUrl;
	private static MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
	static {
		headers.add("Token", Authentication.getToken());
	}
	
	public RestClient(RestTemplate restTemplate, ServiceUrl serviceUrl) {
		this.restTemplate = restTemplate;
		this.serviceUrl = serviceUrl;
	}

	public <T> T getForIdentityService(String url, Class<T> responseType) {
		return getForIdentityService(url, null, responseType);
	}
	
	public <T> T getForIdentityService(String url, MultiValueMap<String, String> queryParams, Class<T> responseType) {
		return exchange(serviceUrl.getIdentityService()+url, HttpMethod.GET, queryParams, null, responseType);
	}
	
	public <T> T get(String url, Class<T> responseType) {
		return exchange(url, HttpMethod.GET, null, null, responseType);
	}

	public <T> T exchange(String url, HttpMethod method, MultiValueMap<String, String> queryParams, Object requestbody, Class<T> responseType) {
		T response = null;
		long begintime = System.currentTimeMillis();
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			builder.queryParams(queryParams);
			HttpHeaders headers1 = new HttpHeaders();
			headers1.add("Token", Authentication.getToken());
			response = restTemplate.exchange(builder.build().toUri(), method, new HttpEntity<>(requestbody, headers1), responseType).getBody();
			logger.debug("发送Http请求:{},方法:{},参数:{},返回:{},[{}]ms", url, method, requestbody, response, DateUtils.getTimeMillisConsume(begintime));
		} catch (RestClientException e) {
			logger.error("发送http请求:{},方法:{},参数:{},异常:{},[{}]ms", url, method, requestbody, e.getMessage(), DateUtils.getTimeMillisConsume(begintime));
		}

		return response;
	}

}