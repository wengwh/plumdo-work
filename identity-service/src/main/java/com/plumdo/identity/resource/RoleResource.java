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
import com.plumdo.identity.domain.Role;
import com.plumdo.identity.repository.RoleRepository;

@RestController
public class RoleResource extends BaseResource {
	@Autowired
	private RoleRepository roleRepository;

	private Role getRoleFromRequest(Integer id) {
		Role role = roleRepository.findOne(id);
		if (role == null) {
			exceptionFactory.throwDefinedException(ErrorCodeConstant.OBJECT_NOT_FOUND);
		}
		return role;
	}

	@GetMapping(value = "/roles")
	@ResponseStatus(value = HttpStatus.OK)
	public PageResponse<Role> getRoles(@ApiIgnore @RequestParam Map<String, String> requestParams) {
		Criteria<Role> criteria = new Criteria<Role>();
		criteria.add(Restrictions.eq("id", requestParams.get("id"), true));
		criteria.add(Restrictions.like("name", requestParams.get("name"), true));
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId"), true));
		return createPageResponse(roleRepository.findAll(criteria, getPageable(requestParams)));
	}

	@GetMapping(value = "/roles/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public Role getRole(@PathVariable Integer id) {
		return getRoleFromRequest(id);
	}

	@PostMapping("/roles")
	@ResponseStatus(HttpStatus.CREATED)
	public Role createRole(@RequestBody Role roleRequest) {
		return roleRepository.save(roleRequest);
	}

	@PutMapping(value = "/roles/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public Role updateRole(@PathVariable Integer id, @RequestBody Role roleRequest) {
		Role role = getRoleFromRequest(id);
		role.setName(roleRequest.getName());
		role.setRemark(roleRequest.getRemark());
		role.setTenantId(roleRequest.getTenantId());
		return roleRepository.save(role);
	}

	@DeleteMapping(value = "/roles/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteRole(@PathVariable Integer id) {
		Role role = getRoleFromRequest(id);
		roleRepository.delete(role);
	}
}
