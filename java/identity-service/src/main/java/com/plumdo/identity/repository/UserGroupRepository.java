package com.plumdo.identity.repository;

import org.springframework.transaction.annotation.Transactional;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.domain.UserGroup;

public interface UserGroupRepository extends BaseRepository<UserGroup, Integer> {

	@Transactional
	int deleteByUserId(int userId);

	@Transactional
	int deleteByGroupIdAndUserId(int groupId, int userId);
}