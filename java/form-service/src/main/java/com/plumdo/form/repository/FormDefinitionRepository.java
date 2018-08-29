package com.plumdo.form.repository;


import org.springframework.data.jpa.repository.Query;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.form.domain.FormDefinition;

public interface FormDefinitionRepository extends BaseRepository<FormDefinition, Integer> {
	
	@Query("select f from FormDefinition f where f.key = ?1 and (tenantId = ''  or tenantId is null) "
			+ " and version = (select max(version) from FormDefinition where key = ?1 and (tenantId = ''  or tenantId is null))")
	FormDefinition findLatestFormDefinitionByKey(String key);
	
	
	@Query("select f from FormDefinition f where f.key = ?1 and tenantId = ?2 "
			+ " and version = (select max(version) from FormDefinition where key = ?1 and tenantId = ?2)")
	FormDefinition findLatestFormDefinitionByKeyAndTenantId(String key,String tenantId);
}