package com.plumdo.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.domain.Menu;

public interface MenuRepository extends BaseRepository<Menu, Integer> {
	
	@Query("select a from Menu a , RoleMenu b where a.id = b.menuId and b.roleId = ?1 ")
	List<Menu> findByRoleId(int roleId);
	

	@Query("select a from Menu a , RoleMenu b, UserRole c "
			+ "where a.id = b.menuId and b.roleId = c.roleId and c.userId = ?1 ")
	List<Menu> findByUserId(int userId);
	

	List<Menu> findByType(byte type);
}