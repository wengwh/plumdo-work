package com.plumdo.identity.resource;

import java.util.List;
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
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.identity.constant.ErrorConstant;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Menu;
import com.plumdo.identity.domain.Role;
import com.plumdo.identity.repository.MenuRepository;
import com.plumdo.identity.repository.RoleMenuRepository;
import com.plumdo.identity.repository.RoleRepository;

@RestController
public class MenuResource extends BaseResource {
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private RoleMenuRepository roleMenuRepository;

	private Menu getMenuFromRequest(Integer id) {
		Menu menu = menuRepository.findOne(id);
		if (menu == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.MENU_NOT_FOUND);
		}
		return menu;
	}

	@GetMapping(value = "/menus")
	@ResponseStatus(value = HttpStatus.OK)
	public PageResponse<Menu> getMenus(@RequestParam Map<String, String> requestParams) {
		Criteria<Menu> criteria = new Criteria<Menu>();
		criteria.add(Restrictions.eq("id", requestParams.get("id")));
		criteria.add(Restrictions.eq("parentId", requestParams.get("parentId")));
		criteria.add(Restrictions.eq("status", requestParams.get("status")));
		criteria.add(Restrictions.like("name", requestParams.get("name")));
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
		return createPageResponse(menuRepository.findAll(criteria, getPageable(requestParams)));
	}

	@GetMapping(value = "/menus/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public Menu getMenu(@PathVariable Integer id) {
		return getMenuFromRequest(id);
	}

	@PostMapping("/menus")
	@ResponseStatus(HttpStatus.CREATED)
	public Menu createMenu(@RequestBody Menu menuRequest) {
		return menuRepository.save(menuRequest);
	}

	@PutMapping(value = "/menus/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public Menu updateMenu(@PathVariable Integer id, @RequestBody Menu menuRequest) {
		Menu menu = getMenuFromRequest(id);
		menu.setName(menuRequest.getName());
		menu.setStatus(menuRequest.getStatus());
		menu.setIcon(menuRequest.getIcon());
		menu.setOrder(menuRequest.getOrder());
		menu.setParentId(menuRequest.getParentId());
		menu.setType(menuRequest.getType());
		menu.setRoute(menuRequest.getRoute());
		menu.setRemark(menuRequest.getRemark());
		menu.setTenantId(menuRequest.getTenantId());
		return menuRepository.save(menu);
	}

	@PutMapping(value = "/menus/{id}/switch")
	@ResponseStatus(value = HttpStatus.OK)
	public Menu switchStatus(@PathVariable Integer id) {
		Menu menu = getMenuFromRequest(id);
		if (menu.getStatus() == TableConstant.MENU_STATUS_NORMAL) {
			menu.setStatus(TableConstant.MENU_STATUS_STOP);
		} else {
			menu.setStatus(TableConstant.MENU_STATUS_NORMAL);
		}
		return menuRepository.save(menu);
	}

	@GetMapping(value = "/menus/{id}/roles")
	@ResponseStatus(value = HttpStatus.OK)
	public List<Role> getMenuRoles(@PathVariable Integer id) {
		return roleRepository.findByMenuId(id);
	}

	@DeleteMapping(value = "/menus/{id}/roles/{roleId}")
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteMenuRole(@PathVariable Integer id, @PathVariable(value = "roleId") Integer roleId) {
		roleMenuRepository.deleteByMenuIdAndRoleId(id, roleId);
	}

	@DeleteMapping(value = "/menus/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteMenu(@PathVariable Integer id) {
		Menu menu = getMenuFromRequest(id);
		if (menu.getType() == TableConstant.MENU_TYPE_PARENT) {
			List<Menu> children = menuRepository.findByParentId(menu.getId());
			if (ObjectUtils.isNotEmpty(children)) {
				exceptionFactory.throwForbidden(ErrorConstant.MENU_HAVE_CHILDREN);
			}
		} else {
			List<Role> roles = roleRepository.findByMenuId(menu.getId());
			if (ObjectUtils.isNotEmpty(roles)) {
				exceptionFactory.throwForbidden(ErrorConstant.MENU_ALREADY_ROLE_USE, roles.get(0).getName());
			}
		}
		menuRepository.delete(menu);
	}
}
