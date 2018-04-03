package com.plumdo.identity.response;

import java.util.ArrayList;
import java.util.List;

import com.plumdo.common.model.ObjectMap;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Group;

/**
 * TODO
 *
 * @author wengwenhui
 * @date 2018年3月28日
 */
public class GroupConverter {

	public static List<ObjectMap> convertMultiSelect(List<Group> groups, List<Group> userGroups) {
		return getChildren(TableConstant.GROUP_PARNET_ID, groups);
	}

	private static List<ObjectMap> getChildren(int parentId, List<Group> groups) {
		List<ObjectMap> groupList = new ArrayList<>();
		for (Group child : groups) {
			if(parentId==child.getParentId()) {
				if(child.getType()==TableConstant.GROUP_TYPE_CHILD) {
					groupList.add(ObjectMap.of("id", child.getId(), "name", child.getName(), "selected", false));
				}else {
					groupList.add(ObjectMap.of("id", child.getId(), "name", child.getName(), "group", true));
					groupList.addAll(getChildren(child.getId(), groups));
					groupList.add(ObjectMap.of("group", false));
				}
				
			}
		}
		return groupList;
	}

}
