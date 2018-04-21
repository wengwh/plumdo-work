package com.plumdo.flow.identity;

import java.util.ArrayList;
import java.util.List;

import org.flowable.idm.api.Group;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.impl.GroupQueryImpl;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntity;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityManagerImpl;
import org.flowable.idm.engine.impl.persistence.entity.data.GroupDataManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.plumdo.common.client.rest.RestClient;
import com.plumdo.common.model.ObjectMap;

/**
 * TODO
 *
 * @author wengwenhui
 * @date 2018年4月19日
 */
public class AiaGroupEntityManager extends GroupEntityManagerImpl {
	private RestClient restClient;

	public AiaGroupEntityManager(RestClient restClient,IdmEngineConfiguration idmEngineConfiguration, GroupDataManager groupDataManager) {
		super(idmEngineConfiguration, groupDataManager);
		this.restClient = restClient;
	}

	@Override
	public GroupEntity findById(String entityId) {
		return restClient.getForIdentityService("/groups/"+entityId, GroupEntity.class);
	}


	@Override
	public List<Group> findGroupByQueryCriteria(GroupQueryImpl query) {
		List<Group> groups =  new ArrayList<>();
		ObjectMap response = restClient.getForIdentityService("/groups", queryToParams(query), ObjectMap.class);
		List<ObjectMap> dataMap = response.getAsList("data");
		for(ObjectMap groupMap : dataMap) {
			Group group = new GroupEntityImpl();
			group.setId(groupMap.getAsString("id"));
			group.setName(groupMap.getAsString("name"));
			group.setType(groupMap.getAsString("type"));
			groups.add(group);
		}
		return groups;
	}
	

	@Override
	public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
		ObjectMap response = restClient.getForIdentityService("/groups", queryToParams(query), ObjectMap.class);
		return response.getAsLong("total");
	}

	private MultiValueMap<String, String> queryToParams(GroupQueryImpl query) {
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		if(query.getName() != null) {
			queryParams.add("name", query.getName());
		}
		if(query.getNameLike() != null) {
			queryParams.add("name", query.getNameLike());
		}
		if(query.getType() != null) {
			queryParams.add("type", query.getType());
		}
		
		if(query.getId() != null) {
			queryParams.add("id", query.getId());
		}
		return queryParams;
	}
}
