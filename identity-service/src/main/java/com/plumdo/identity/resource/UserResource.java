package com.plumdo.identity.resource;

import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.plumdo.common.resource.BaseResource;
import com.plumdo.common.resource.PageResponse;
import com.plumdo.identity.constant.ErrorCodeConstant;
import com.plumdo.identity.domain.User;
import com.plumdo.identity.repository.UserRepository;

@RestController
public class UserResource extends BaseResource {
	@Autowired
	private UserRepository userRepository;

	private User getUserFromRequest(Integer id) {
		User user = userRepository.findOne(id);
		if (user == null) {
			exceptionFactory.throwDefinedException(ErrorCodeConstant.OBJECT_NOT_FOUND);
		}
		return user;
	}

	@GetMapping(value = "/users")
	@ResponseStatus(value = HttpStatus.OK)
	public PageResponse<User> getUsers(@ApiIgnore @RequestParam Map<String, String> requestParams) {
		Criteria<User> criteria = new Criteria<User>();
		criteria.add(Restrictions.eq("id", requestParams.get("id"), true));
		criteria.add(Restrictions.like("category", requestParams.get("category"), true));
		criteria.add(Restrictions.like("key", requestParams.get("key"), true));
		criteria.add(Restrictions.like("name", requestParams.get("name"), true));
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId"), true));
		return createPageResponse(userRepository.findAll(criteria, getPageable(requestParams)));
	}

	@GetMapping(value = "/users/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public User getUser(@PathVariable Integer id) {
		return getUserFromRequest(id);
	}

	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@RequestBody User userRequest) {
		return userRepository.save(userRequest);
	}

	@PutMapping(value = "/users/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public User updateUser(@PathVariable Integer id, @RequestBody User userRequest) {
		User user = getUserFromRequest(id);
		user.setName(userRequest.getName());
		user.setAccount(userRequest.getAccount());
		user.setTenantId(userRequest.getTenantId());
		return userRepository.save(user);
	}

	@DeleteMapping(value = "/users/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable Integer id) {
		User user = getUserFromRequest(id);
		userRepository.delete(user);
	}
}
