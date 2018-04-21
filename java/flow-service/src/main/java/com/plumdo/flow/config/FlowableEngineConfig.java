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
 * 流程引擎配置类
 *
 * @author wengwenhui
 * @date 2018年4月20日
 */
@Configuration
public class FlowableEngineConfig extends ProcessEngineAutoConfiguration {

	public FlowableEngineConfig(FlowableProperties flowableProperties, FlowableProcessProperties processProperties, FlowableIdmProperties idmProperties, FlowableMailProperties mailProperties) {
		super(flowableProperties, processProperties, idmProperties, mailProperties);
	}

	@Override
	public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager platformTransactionManager, ObjectProvider<AsyncExecutor> asyncExecutorProvider)
			throws IOException {
		SpringProcessEngineConfiguration conf = super.springProcessEngineConfiguration(dataSource, platformTransactionManager, asyncExecutorProvider);
		String databaseSchema = conf.getDatabaseSchema();
		conf.setDatabaseCatalog(databaseSchema);
		conf.setDatabaseTablePrefix(databaseSchema + ".");
		conf.setTablePrefixIsSchema(true);
		conf.setActivityFontName("微软雅黑");
		conf.setLabelFontName("微软雅黑");
		conf.setAnnotationFontName("微软雅黑");
		return conf;
	}

}
