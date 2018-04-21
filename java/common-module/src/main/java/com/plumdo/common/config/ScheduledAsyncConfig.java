package com.plumdo.common.config;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 重新设置异步和定时器配置
 * 
 * @author wengwh
 * @date 2018年4月21日
 */
@Configuration
@EnableScheduling
@EnableAsync
@ConfigurationProperties(prefix = "scheduleConfig")
public class ScheduledAsyncConfig implements AsyncConfigurer, SchedulingConfigurer {
	private final Logger logger = LoggerFactory.getLogger(ScheduledAsyncConfig.class);

	private int poolSize;
	private String prefix;
	private int awaitTerminationSeconds;
	private boolean waitForTasksToCompleteOnShutdown;

	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(poolSize);
		scheduler.setThreadNamePrefix(prefix);
		scheduler.setAwaitTerminationSeconds(awaitTerminationSeconds);
		scheduler.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
		return scheduler;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar registrar) {
		TaskScheduler scheduler = taskScheduler();
		registrar.setTaskScheduler(scheduler);
	}

	@Override
	public Executor getAsyncExecutor() {
		return taskScheduler();
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				logger.error("异步执行器出现异常:{},方法:{},参数:{}", ex.getMessage(), method.getName(), params);
			}
		};
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getAwaitTerminationSeconds() {
		return awaitTerminationSeconds;
	}

	public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
		this.awaitTerminationSeconds = awaitTerminationSeconds;
	}

	public boolean isWaitForTasksToCompleteOnShutdown() {
		return waitForTasksToCompleteOnShutdown;
	}

	public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
		this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
	}

}