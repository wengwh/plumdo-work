package com.plumdo.identity.response;

import java.util.ArrayList;
import java.util.List;

import com.plumdo.common.model.ObjectMap;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Menu;

/**
 * TODO
 *
 * @author wengwenhui
 * @date 2018年3月28日
 */
public class MenuConverter {

	public static List<ObjectMap> convertMultiSelect(List<Menu> menus,List<Menu> roleMenus) {
		List<ObjectMap> menuList = new ArrayList<>();
		for (Menu menu : menus) {
			if (menu.getType() == TableConstant.MENU_TYPE_CHILD) {
				continue;
			}
			menuList.add(ObjectMap.of("id", menu.getId(), "name", menu.getName(), "group", true));
			for (Menu childMenu : menus) {
				if (menu.getId().equals(childMenu.getParentId())) {
					if(ObjectUtils.isNotEmpty(roleMenus) && roleMenus.contains(childMenu)) {
						menuList.add(ObjectMap.of("id", childMenu.getId(), "name", childMenu.getName(),"selected", true));
					}else {
						menuList.add(ObjectMap.of("id", childMenu.getId(), "name", childMenu.getName(),"selected", false));
					}
				}
			}
			menuList.add(ObjectMap.of("group", false));
		}
		return menuList;
	}

}
