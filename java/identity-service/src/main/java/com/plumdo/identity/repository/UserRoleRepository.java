package com.plumdo.identity.repository;


import org.springframework.transaction.annotation.Transactional;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.domain.UserRole;

public interface UserRoleRepository extends BaseRepository<UserRole, Integer> {
	
	@Transactional
	int deleteByUserId(int userId);
	 
	
}