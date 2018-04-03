package com.plumdo.identity.response;

import java.util.ArrayList;
import java.util.List;

import com.plumdo.common.model.ObjectMap;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.identity.domain.Role;

/**
 * TODO
 *
 * @author wengwenhui
 * @date 2018年3月28日
 */
public class RoleConverter {

	
	public static List<ObjectMap> convertMultiSelect(List<Role> roles,List<Role> userRoles) {
		List<ObjectMap> menuList = new ArrayList<>();
		for (Role role : roles) {
			if(ObjectUtils.isNotEmpty(userRoles) && userRoles.contains(role)) {
				menuList.add(ObjectMap.of("id", role.getId(), "name", role.getName(),"selected", true));
			}else {
				menuList.add(ObjectMap.of("id", role.getId(), "name", role.getName(),"selected", false));
			}
		}
		return menuList;
	}
	

}
