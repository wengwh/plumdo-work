package com.plumdo.identity.resource;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.common.constant.CoreConstant;
import com.plumdo.common.model.ObjectMap;
import com.plumdo.common.resource.BaseResource;
import com.plumdo.common.utils.DateUtils;
import com.plumdo.identity.constant.ErrorConstant;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Menu;
import com.plumdo.identity.domain.User;
import com.plumdo.identity.repository.MenuRepository;
import com.plumdo.identity.repository.UserRepository;
import com.plumdo.identity.response.ConvertFactory;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class AuthResource extends BaseResource {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MenuRepository menuRepository;

	@PostMapping("/auths/login")
	@ResponseStatus(HttpStatus.OK)
	public ObjectMap login(@RequestBody ObjectMap loginRequest) {
		String account = loginRequest.getAsString("account");
		String password = loginRequest.getAsString("password");
		User user = userRepository.findByAccount(account);
		if (user == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.USER_NOT_FOUND);
		}
		if (!user.getPwd().equals(password)) {
			exceptionFactory.throwConflict(ErrorConstant.USER_PWD_NOT_MATCH);
		}
		if (user.getStatus() == TableConstant.USER_STATUS_STOP) {
			exceptionFactory.throwForbidden(ErrorConstant.USER_ALREADY_STOP);
		}
		String token = Jwts.builder().setSubject(account)
				.setIssuedAt(DateUtils.currentTimestamp())
				.setExpiration(new Date(DateUtils.currentTimeMillis() + CoreConstant.LOGIN_USER_EXPIRE_IN))
				.signWith(SignatureAlgorithm.HS256, CoreConstant.JWT_SECRET).compact();

		return ConvertFactory.convertUseAuth(user, token);
	}

	
	@GetMapping("/auths/menus")
	@ResponseStatus(HttpStatus.OK)
	public List<ObjectMap> getUserMenus() {
		List<Menu>  childMenus = menuRepository.findByUserId(2);
		List<Menu>  parentMenus = menuRepository.findByType(TableConstant.MENU_TYPE_PARENT);
		return ConvertFactory.convertUserMenus(parentMenus, childMenus);
	}

	
	@PutMapping("/auths/password/change")
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

	@PutMapping("/auths/password/reset")
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
