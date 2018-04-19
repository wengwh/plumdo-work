package com.plumdo.flow.config;


import java.io.IOException;

import javax.sql.DataSource;

import org.flowable.job.service.impl.asyncexecutor.AsyncExecutor;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.FlowableMailProperties;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.flowable.spring.boot.process.FlowableProcessProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * 程序相关配置
 * 
 * @author wengwenhui
 * 
 */
@Configuration
public class FlowableConfig extends ProcessEngineAutoConfiguration{

	public FlowableConfig(FlowableProperties flowableProperties, FlowableProcessProperties processProperties, FlowableIdmProperties idmProperties, FlowableMailProperties mailProperties) {
		super(flowableProperties, processProperties, idmProperties, mailProperties);
	}

	@Override
	public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager platformTransactionManager, ObjectProvider<AsyncExecutor> asyncExecutorProvider)
			throws IOException {
		SpringProcessEngineConfiguration conf = super.springProcessEngineConfiguration(dataSource, platformTransactionManager, asyncExecutorProvider);
		conf.setDatabaseTablePrefix("plumdo_flow.");
		conf.setTablePrefixIsSchema(true);
		conf.setActivityFontName("微软雅黑");
		conf.setLabelFontName("微软雅黑");
		conf.setAnnotationFontName("微软雅黑");
		conf.setDatabaseCatalog("plumdo_flow");
		return conf;
	}

	
}
