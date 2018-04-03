package com.plumdo.identity.response;


import com.plumdo.common.model.ObjectMap;
import com.plumdo.identity.domain.User;

/**
 * TODO
 *
 * @author wengwenhui
 * @date 2018年3月28日
 */
public class UserConverter {

	public static ObjectMap convertAuth(User user, String token) {
		ObjectMap result = new ObjectMap();
		result.put("id", user.getId());
		result.put("name", user.getName());
		result.put("avatar", user.getAvatar());
		result.put("token", token);
		return result;
	}

	

}
