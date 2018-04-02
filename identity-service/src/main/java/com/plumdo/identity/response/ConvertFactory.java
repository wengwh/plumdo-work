package com.plumdo.identity.response;

import java.util.List;

import com.plumdo.common.model.ObjectMap;
import com.plumdo.identity.domain.Group;
import com.plumdo.identity.domain.Menu;
import com.plumdo.identity.domain.Role;
import com.plumdo.identity.domain.User;

/**
 * TODO
 *
 * @author wengwenhui
 * @date 2018年3月28日
 */
public abstract class ConvertFactory {
	public static ObjectMap convertUseAuth(User user, String token) {
		return UserConverter.convertAuth(user, token);
	}
	
	public static List<ObjectMap> convertUserGroups(List<Group> groups,List<Group> roleGroups) {
		return GroupConverter.convertMultiSelect(groups, roleGroups);
	}
	
	public static List<ObjectMap> convertUseRoles(List<Role> roles,List<Role> userRoles) {
		return RoleConverter.convertMultiSelect(roles, userRoles);
	}

	public static List<ObjectMap> convertRoleMenus(List<Menu> menus, List<Menu> roleMenus) {
		return MenuConverter.convertMultiSelect(menus, roleMenus);
	}
	
	public static List<ObjectMap> convertUserMenus(List<Menu> parentMenus,List<Menu> childMenus) {
		return MenuConverter.convertUserMenus(parentMenus, childMenus);
	}
}
