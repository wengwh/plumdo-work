package com.plumdo.identity.resource;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.common.jpa.Criteria;
import com.plumdo.common.jpa.Restrictions;
import com.plumdo.common.model.ObjectMap;
import com.plumdo.common.resource.BaseResource;
import com.plumdo.common.resource.PageResponse;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.identity.constant.ErrorConstant;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Group;
import com.plumdo.identity.domain.Role;
import com.plumdo.identity.domain.User;
import com.plumdo.identity.domain.UserGroup;
import com.plumdo.identity.domain.UserRole;
import com.plumdo.identity.repository.GroupRepository;
import com.plumdo.identity.repository.RoleRepository;
import com.plumdo.identity.repository.UserGroupRepository;
import com.plumdo.identity.repository.UserRepository;
import com.plumdo.identity.repository.UserRoleRepository;
import com.plumdo.identity.response.ConvertFactory;

@RestController
public class UserAuthResource extends BaseResource {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private UserGroupRepository userGroupRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;

	@PostMapping("/users/login")
	@ResponseStatus(HttpStatus.OK)
	public User loginUser(@RequestBody ObjectMap loginRequest) {
		String account = loginRequest.getAsString("account");
		String pwd = loginRequest.getAsString("pwd");
		User user = userRepository.findByAccount(account);
		if (user == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.USER_NOT_FOUND);
		}
		if (!user.getPwd().equals(pwd)) {
			exceptionFactory.throwConflict(ErrorConstant.USER_PWD_NOT_MATCH);
		}
		if (user.getStatus() == TableConstant.USER_STATUS_STOP) {
			exceptionFactory.throwForbidden(ErrorConstant.USER_ALREADY_STOP);
		}
		return user;
	}

	@PostMapping("/users/password/change")
	@ResponseStatus(HttpStatus.OK)
	public User changePwd(@RequestBody ObjectMap loginRequest) {
		String account = loginRequest.getAsString("account");
		String pwd = loginRequest.getAsString("pwd");
		User user = userRepository.findByAccount(account);
		if (user == null) {
			return null;
		}
		if (user.getPwd().equals(pwd)) {
			return user;
		}
		return null;
	}

	@PostMapping("/users/password/reset")
	@ResponseStatus(HttpStatus.OK)
	public User resetPwd(@RequestBody ObjectMap loginRequest) {
		String account = loginRequest.getAsString("account");
		String pwd = loginRequest.getAsString("pwd");
		User user = userRepository.findByAccount(account);
		if (user == null) {
			return null;
		}
		if (user.getPwd().equals(pwd)) {
			return user;
		}
		return null;
	}
}
