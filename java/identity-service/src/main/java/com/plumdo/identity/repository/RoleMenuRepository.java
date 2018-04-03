package com.plumdo.identity.repository;


import org.springframework.transaction.annotation.Transactional;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.domain.RoleMenu;

public interface RoleMenuRepository extends BaseRepository<RoleMenu, Integer> {
	
	@Transactional
	int deleteByRoleId(int roleId);
	 
	
}