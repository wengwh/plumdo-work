package com.plumdo.common.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * 异步任务执行器
 *
 * @author wengwh
 * @date 2018年7月5日
 */
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
	private final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

	@Bean
	@ConfigurationProperties(prefix = "spring.async")
	public Executor executor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
		return executor;
	}

	@Override
	public Executor getAsyncExecutor() {
		return executor();
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				logger.error("async executor exception:{},method:{},params:{}", ex.getMessage(), method.getName(), params);
			}
		};
	}

}