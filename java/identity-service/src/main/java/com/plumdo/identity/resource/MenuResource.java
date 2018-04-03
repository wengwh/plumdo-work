package com.plumdo.identity.resource;


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
import com.plumdo.identity.constant.ErrorConstant;
import com.plumdo.identity.domain.Menu;
import com.plumdo.identity.repository.MenuRepository;

@RestController
public class MenuResource extends BaseResource {
	@Autowired
	private MenuRepository menuRepository;

	private Menu getMenuFromRequest(Integer id) {
		Menu menu = menuRepository.findOne(id);
		if (menu == null) {
			exceptionFactory.throwDefinedException(ErrorConstant.OBJECT_NOT_FOUND);
		}
		return menu;
	}

	@GetMapping(value = "/menus")
	@ResponseStatus(value = HttpStatus.OK)
	public PageResponse<Menu> getMenus(@RequestParam Map<String, String> requestParams) {
		Criteria<Menu> criteria = new Criteria<Menu>();
		criteria.add(Restrictions.eq("id", requestParams.get("id"), true));
		criteria.add(Restrictions.eq("parentId", requestParams.get("parentId"), true));
		criteria.add(Restrictions.like("name", requestParams.get("name"), true));
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId"), true));
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
		menu.setCode(menuRequest.getCode());
		menu.setIcon(menuRequest.getIcon());
		menu.setOrder(menuRequest.getOrder());
		menu.setParentId(menuRequest.getParentId());
		menu.setType(menuRequest.getType());
		menu.setUrl(menuRequest.getUrl());
		menu.setRemark(menuRequest.getRemark());
		menu.setTenantId(menuRequest.getTenantId());
		return menuRepository.save(menu);
	}

	@DeleteMapping(value = "/menus/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteMenu(@PathVariable Integer id) {
		Menu menu = getMenuFromRequest(id);
		menuRepository.delete(menu);
	}
}
