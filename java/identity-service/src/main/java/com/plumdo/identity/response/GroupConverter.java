package com.plumdo.identity.response;

import java.util.ArrayList;
import java.util.List;

import com.plumdo.common.model.ObjectMap;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Group;

/**
 * 群组数据转换类
 *
 * @author wengwenhui
 * @date 2018年3月28日
 */
public class GroupConverter {

	public static List<ObjectMap> convertMultiSelect(List<Group> groups, List<Group> userGroups) {
		return getChildren(TableConstant.GROUP_PARNET_ID, groups, userGroups);
	}

	private static List<ObjectMap> getChildren(int parentId, List<Group> groups, List<Group> userGroups) {
		List<ObjectMap> groupList = new ArrayList<>();
		for (Group child : groups) {
			if (parentId == child.getParentId()) {
				if (child.getType() == TableConstant.GROUP_TYPE_CHILD) {
					if (ObjectUtils.isNotEmpty(userGroups) && userGroups.contains(child)) {
						groupList.add(ObjectMap.of("id", child.getId(), "name", child.getName(), "selected", true));
					} else {
						groupList.add(ObjectMap.of("id", child.getId(), "name", child.getName(), "selected", false));
					}
				} else {
					List<ObjectMap> children = getChildren(child.getId(), groups, userGroups);
					if (ObjectUtils.isNotEmpty(children)) {
						groupList.add(ObjectMap.of("id", child.getId(), "name", child.getName(), "group", true));
						groupList.addAll(children);
						groupList.add(ObjectMap.of("group", false));
					}
				}

			}
		}
		return groupList;
	}

}
