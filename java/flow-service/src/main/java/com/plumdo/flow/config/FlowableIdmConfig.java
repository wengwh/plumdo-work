package com.plumdo.flow.config;

import javax.sql.DataSource;

import org.flowable.idm.spring.SpringIdmEngineConfiguration;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.flowable.spring.boot.idm.IdmEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.plumdo.common.client.rest.RestClient;
import com.plumdo.flow.identity.AiaGroupEntityManager;
import com.plumdo.flow.identity.AiaUserEntityManager;

/**
 * 人员配置类
 *
 * @author wengwenhui
 * @date 2018年4月20日
 */
@Configuration
public class FlowableIdmConfig extends IdmEngineAutoConfiguration{
	@Autowired
	private RestClient restClient;
	
	public FlowableIdmConfig(FlowableProperties flowableProperties, FlowableIdmProperties idmProperties) {
		super(flowableProperties, idmProperties);
	}

	@Autowired
	public SpringIdmEngineConfiguration idmEngineConfiguration(DataSource dataSource, PlatformTransactionManager platformTransactionManager) {
        SpringIdmEngineConfiguration configuration = super.idmEngineConfiguration(dataSource, platformTransactionManager);
        configuration.setGroupEntityManager(new AiaGroupEntityManager(restClient,configuration,configuration.getGroupDataManager()));
        configuration.setUserEntityManager(new AiaUserEntityManager(restClient, configuration, configuration.getUserDataManager()));
        return configuration;
    }
	
}
