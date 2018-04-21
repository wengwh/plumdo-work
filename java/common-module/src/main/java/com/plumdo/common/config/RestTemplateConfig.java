package com.plumdo.common.config;

import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

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
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.plumdo.common.constant.CoreConstant;

/**
 * 定义restTemplate的配置
 * 
 * @author wengwh
 * @date 2018年4月21日
 */
@Configuration
@ConfigurationProperties(prefix = "restConfig")
public class RestTemplateConfig {
	private final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);

	private int connectionTimeout;
	private int readTimeout;
	private int connectionRequestTimeout;
	private int maxTotal;
	private int maxPerRoute;
	
	@Bean
	@ConditionalOnMissingBean({ RestOperations.class, RestTemplate.class })
	public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
		RestTemplate restTemplate = new RestTemplate(factory);
		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
		Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
		while (iterator.hasNext()) {
			HttpMessageConverter<?> converter = iterator.next();
			if (converter instanceof StringHttpMessageConverter) {
				iterator.remove();
			}
		}
		messageConverters.add(new StringHttpMessageConverter(Charset.forName(CoreConstant.DEFAULT_CHARSET)));
		return restTemplate;
	}

	@Bean
	@ConditionalOnMissingBean({ ClientHttpRequestFactory.class })
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		SSLContext sslContext;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			}).build();

			httpClientBuilder.setSSLContext(sslContext);
			HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
					hostnameVerifier);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslConnectionSocketFactory).build();// 注册http和https请求
			PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);// 开始设置连接池
			poolingHttpClientConnectionManager.setMaxTotal(maxTotal); // 最大连接数
			poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute); // 同路由并发数
			httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
			httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));// 重试次数
			HttpClient httpClient = httpClientBuilder.build();
			HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
					httpClient);// httpClient连接配置
			clientHttpRequestFactory.setConnectTimeout(connectionTimeout);// 连接超时
			clientHttpRequestFactory.setReadTimeout(readTimeout);// 数据读取超时时间
			clientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);// 连接不够用的等待时间
			return clientHttpRequestFactory;
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			logger.error("设置HttpClient异常", e);
		}
		return null;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxPerRoute() {
		return maxPerRoute;
	}

	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

}