package com.plumdo.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.domain.User;

public interface UserRepository extends BaseRepository<User, Integer> {
	User findByAccount(String account);

	@Query("select a from User a, UserRole b where a.id = b.userId and b.roleId = ?1 ")
	List<User> findByRoleId(int roleId);
	

	@Query("select a from User a, UserGroup b where a.id = b.userId and b.groupId = ?1 ")
	List<User> findByGroupId(int groupId);
}