package com.plumdo.form.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.LogFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;


@Configuration
@ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)
public class DruidConfiguration {
	protected  final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	

	@Autowired
	protected DruidDataSource druidDataSource;
	
	@PostConstruct
	public void init(){
		
		LOGGER.debug("xcccccccccccc");
		LOGGER.info(LOGGER.getName());
		LOGGER.info(LOGGER.getClass().getName());
		List<Filter> filters = new ArrayList<>();
		filters.add(logFilter());
		druidDataSource.setProxyFilters(filters);
	}
	
	@Bean
	public LogFilter logFilter() {
		LogFilter logFilter = new Slf4jLogFilter();
		logFilter.setConnectionLogEnabled(true);
		logFilter.setStatementLogEnabled(true);
		logFilter.setResultSetLogEnabled(true);
		logFilter.setStatementExecutableSqlLogEnable(true);
		return logFilter;
	}
	
	
	
}