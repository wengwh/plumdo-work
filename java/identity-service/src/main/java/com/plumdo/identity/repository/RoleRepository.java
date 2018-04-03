package com.plumdo.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.domain.Role;

public interface RoleRepository extends BaseRepository<Role, Integer> {
	@Query("select a from Role a , UserRole b where a.id = b.roleId and b.userId = ?1 ")
	List<Role> findByUserId(int userId);
}