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
import com.plumdo.identity.domain.Group;
import com.plumdo.identity.repository.GroupRepository;

@RestController
public class GroupResource extends BaseResource {
	@Autowired
	private GroupRepository groupRepository;

	private Group getGroupFromRequest(Integer id) {
		Group group = groupRepository.findOne(id);
		if (group == null) {
			exceptionFactory.throwDefinedException(ErrorConstant.OBJECT_NOT_FOUND);
		}
		return group;
	}

	@GetMapping(value = "/groups")
	@ResponseStatus(value = HttpStatus.OK)
	public PageResponse<Group> getGroups(@RequestParam Map<String, String> requestParams) {
		Criteria<Group> criteria = new Criteria<Group>();
		criteria.add(Restrictions.eq("id", requestParams.get("id"), true));
		criteria.add(Restrictions.eq("status", requestParams.get("status"), true));
		criteria.add(Restrictions.eq("parentId", requestParams.get("parentId"), true));
		criteria.add(Restrictions.like("name", requestParams.get("name"), true));
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId"), true));
		return createPageResponse(groupRepository.findAll(criteria, getPageable(requestParams)));
	}

	@GetMapping(value = "/groups/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public Group getGroup(@PathVariable Integer id) {
		return getGroupFromRequest(id);
	}

	@PostMapping("/groups")
	@ResponseStatus(HttpStatus.CREATED)
	public Group createGroup(@RequestBody Group groupRequest) {
		return groupRepository.save(groupRequest);
	}

	@PutMapping(value = "/groups/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public Group updateGroup(@PathVariable Integer id, @RequestBody Group groupRequest) {
		Group group = getGroupFromRequest(id);
		group.setName(groupRequest.getName());
		group.setStatus(groupRequest.getStatus());
		group.setType(groupRequest.getType());
		group.setOrder(groupRequest.getOrder());
		group.setParentId(groupRequest.getParentId());
		group.setRemark(groupRequest.getRemark());
		group.setTenantId(groupRequest.getTenantId());
		return groupRepository.save(group);
	}

	@DeleteMapping(value = "/groups/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteGroup(@PathVariable Integer id) {
		Group group = getGroupFromRequest(id);
		groupRepository.delete(group);
	}
}
