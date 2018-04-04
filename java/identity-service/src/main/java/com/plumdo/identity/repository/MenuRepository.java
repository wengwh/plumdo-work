package com.plumdo.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Menu;

public interface MenuRepository extends BaseRepository<Menu, Integer> {

	@Query("select a from Menu a, RoleMenu b where a.id = b.menuId and a.status=" + TableConstant.MENU_STATUS_NORMAL + " and b.roleId = ?1 order by a.order asc")
	List<Menu> findByRoleId(int roleId);

	@Query("select a from Menu a, RoleMenu b, UserRole c where a.id = b.menuId and b.roleId = c.roleId and a.status=" + TableConstant.MENU_STATUS_NORMAL + " and c.userId = ?1 order by a.order asc")
	List<Menu> findByUserId(int userId);

	List<Menu> findByTypeAndStatusOrderByOrderAsc(byte type, byte status);

	List<Menu> findByParentId(int parentId);

	List<Menu> findByStatusOrderByOrderAsc(byte status);
}