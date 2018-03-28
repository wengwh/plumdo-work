package com.plumdo.identity.response;

import java.util.List;

import com.plumdo.common.model.ObjectMap;
import com.plumdo.identity.domain.Menu;
import com.plumdo.identity.domain.Role;

/**
 * TODO
 *
 * @author wengwenhui
 * @date 2018年3月28日
 */
public abstract class ConvertFactory {
	public static ObjectMap convertRoleDetail(Role role, List<Menu> allMenus, List<Menu> roleMenus) {
		return RoleConverter.convertDetail(role, allMenus, roleMenus);
	}

	public static List<ObjectMap> convertMenuTree(List<Menu> menus) {
		return MenuConverter.convertTree(menus);
	}

	public static List<ObjectMap> convertRoleMenuTree(List<Menu> menus, List<Menu> roleMenus) {
		return MenuConverter.convertSelectedTree(menus, roleMenus);
	}
}
