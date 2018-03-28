package com.plumdo.identity.response;

import java.util.ArrayList;
import java.util.List;

import com.plumdo.common.model.ObjectMap;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Menu;
import com.plumdo.identity.domain.Role;

/**
 * TODO
 *
 * @author wengwenhui
 * @date 2018年3月28日
 */
public class RoleConverter {

	public static ObjectMap convertDetail(Role role, List<Menu> allMenus, List<Menu> roleMenus) {
		ObjectMap detailMap =  new ObjectMap();
		detailMap.put("id", role.getId());
		detailMap.put("name", role.getName());
		detailMap.put("remark", role.getRemark());
		detailMap.put("tenantId", role.getTenantId());
		
		List<ObjectMap> menuList = new ArrayList<>();
		for (Menu menu : allMenus) {
			if (menu.getType() == TableConstant.MENU_TYPE_CHILD) {
				continue;
			}
			menuList.add(ObjectMap.of("id", menu.getId(), "name", menu.getName(), "group", true));
			for (Menu childMenu : allMenus) {
				if (menu.getId().equals(childMenu.getParentId())) {
					ObjectMap menuMap = ObjectMap.of("id", childMenu.getId(), "name", childMenu.getName());
					if (roleMenus.contains(childMenu)) {
						menuMap.put("selected", true);
					} else {
						menuMap.put("selected", false);
					}
					menuList.add(menuMap);
				}
			}
			menuList.add(ObjectMap.of("group", false));
		}
		
		detailMap.put("menus", menuList);
		return detailMap;
	}

}
