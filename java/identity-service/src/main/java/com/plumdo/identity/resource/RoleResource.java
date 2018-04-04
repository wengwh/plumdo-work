package com.plumdo.identity.resource;

import springfox.documentation.annotations.ApiIgnore;

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
import com.plumdo.identity.domain.Menu;
import com.plumdo.identity.domain.Role;
import com.plumdo.identity.domain.RoleMenu;
import com.plumdo.identity.domain.User;
import com.plumdo.identity.repository.MenuRepository;
import com.plumdo.identity.repository.RoleMenuRepository;
import com.plumdo.identity.repository.RoleRepository;
import com.plumdo.identity.repository.UserRepository;
import com.plumdo.identity.repository.UserRoleRepository;
import com.plumdo.identity.response.ConvertFactory;

@RestController
public class RoleResource extends BaseResource {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private RoleMenuRepository roleMenuRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;

	private Role getRoleFromRequest(Integer id) {
		Role role = roleRepository.findOne(id);
		if (role == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.ROLE_NOT_FOUND);
		}
		return role;
	}

	@GetMapping(value = "/roles")
	@ResponseStatus(value = HttpStatus.OK)
	public PageResponse<Role> getRoles(@ApiIgnore @RequestParam Map<String, String> requestParams) {
		Criteria<Role> criteria = new Criteria<Role>();
		criteria.add(Restrictions.eq("id", requestParams.get("id")));
		criteria.add(Restrictions.like("name", requestParams.get("name")));
		criteria.add(Restrictions.eq("status", requestParams.get("status")));
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
		return createPageResponse(roleRepository.findAll(criteria, getPageable(requestParams)));
	}

	@GetMapping(value = "/roles/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public Role getRole(@PathVariable Integer id) {
		return getRoleFromRequest(id);
	}

	@GetMapping(value = "/roles/menus")
	@ResponseStatus(value = HttpStatus.OK)
	public List<ObjectMap> getRoleMenus(@RequestParam(required = false) Integer id) {
		List<Menu> roleMenus = null;
		List<Menu> allMenus = menuRepository.findByStatus(TableConstant.MENU_STATUS_NORMAL);
		if (ObjectUtils.isNotEmpty(id)) {
			roleMenus = menuRepository.findByRoleId(id);
		}
		return ConvertFactory.convertRoleMenus(allMenus, roleMenus);
	}

	@PostMapping("/roles")
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public Role createRole(@RequestBody ObjectMap roleRequest) {
		return saveRoleAndMenu(null, roleRequest);
	}

	@PutMapping(value = "/roles/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional
	public Role updateRole(@PathVariable Integer id, @RequestBody ObjectMap roleRequest) {
		Role role = getRoleFromRequest(id);
		return saveRoleAndMenu(role, roleRequest);
	}

	private Role saveRoleAndMenu(Role role, ObjectMap roleRequest) {
		if (role == null) {
			role = new Role();
		}
		role.setName(roleRequest.getAsString("name"));
		role.setStatus(roleRequest.getAsByte("status"));
		role.setRemark(roleRequest.getAsString("remark"));
		role.setTenantId(roleRequest.getAsString("tenantId"));
		roleRepository.save(role);

		roleMenuRepository.deleteByRoleId(role.getId());
		List<ObjectMap> menus = roleRequest.getAsList("roleMenus");
		for (ObjectMap menu : menus) {
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setMenuId(menu.getAsInteger("id"));
			roleMenu.setRoleId(role.getId());
			roleMenuRepository.save(roleMenu);
		}
		return role;
	}

	@PutMapping(value = "/roles/{id}/switch")
	@ResponseStatus(value = HttpStatus.OK)
	public Role switchStatus(@PathVariable Integer id) {
		Role role = getRoleFromRequest(id);
		if (role.getStatus() == TableConstant.ROLE_STATUS_NORMAL) {
			role.setStatus(TableConstant.ROLE_STATUS_STOP);
		} else {
			role.setStatus(TableConstant.ROLE_STATUS_NORMAL);
		}
		return roleRepository.save(role);
	}

	@GetMapping(value = "/roles/{id}/users")
	@ResponseStatus(value = HttpStatus.OK)
	public List<User> getRoleUsers(@PathVariable Integer id) {
		return userRepository.findByRoleId(id);
	}

	@DeleteMapping(value = "/roles/{id}/users/{userId}")
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteRoleUser(@PathVariable Integer id, @PathVariable(value = "userId") Integer userId) {
		userRoleRepository.deleteByRoleIdAndUserId(id, userId);
	}

	@DeleteMapping(value = "/roles/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Transactional
	public void deleteRole(@PathVariable Integer id) {
		Role role = getRoleFromRequest(id);
		List<User> users = userRepository.findByRoleId(role.getId());
		if (ObjectUtils.isNotEmpty(users)) {
			exceptionFactory.throwForbidden(ErrorConstant.ROLE_ALREADY_USER_USE, users.get(0).getName());
		}
		roleRepository.delete(role);
		roleMenuRepository.deleteByRoleId(role.getId());
	}
}
