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
import com.plumdo.identity.domain.Group;
import com.plumdo.identity.domain.User;
import com.plumdo.identity.repository.GroupRepository;
import com.plumdo.identity.repository.UserGroupRepository;
import com.plumdo.identity.repository.UserRepository;

/**
 * 群组资源控制类
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
@RestController
public class GroupResource extends BaseResource {
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserGroupRepository userGroupRepository;

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
		criteria.add(Restrictions.eq("id", requestParams.get("id")));
		criteria.add(Restrictions.eq("status", requestParams.get("status")));
		criteria.add(Restrictions.eq("parentId", requestParams.get("parentId")));
		criteria.add(Restrictions.like("name", requestParams.get("name")));
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
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

	@PutMapping(value = "/groups/{id}/switch")
	@ResponseStatus(value = HttpStatus.OK)
	public Group switchStatus(@PathVariable Integer id) {
		Group group = getGroupFromRequest(id);
		if (group.getStatus() == TableConstant.GROUP_STATUS_NORMAL) {
			group.setStatus(TableConstant.GROUP_STATUS_STOP);
		} else {
			group.setStatus(TableConstant.GROUP_STATUS_NORMAL);
		}
		return groupRepository.save(group);
	}

	@GetMapping(value = "/groups/{id}/users")
	@ResponseStatus(value = HttpStatus.OK)
	public List<User> getGroupUsers(@PathVariable Integer id) {
		return userRepository.findByGroupId(id);
	}

	@DeleteMapping(value = "/groups/{id}/users/{userId}")
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteGroupUser(@PathVariable Integer id, @PathVariable(value = "userId") Integer userId) {
		userGroupRepository.deleteByGroupIdAndUserId(id, userId);
	}

	@DeleteMapping(value = "/groups/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteGroup(@PathVariable Integer id) {
		Group group = getGroupFromRequest(id);
		if (group.getType() == TableConstant.GROUP_TYPE_PARENT) {
			List<Group> children = groupRepository.findByParentId(group.getId());
			if (ObjectUtils.isNotEmpty(children)) {
				exceptionFactory.throwForbidden(ErrorConstant.GROUP_HAVE_CHILDREN);
			}
		} else {
			List<User> users = userRepository.findByGroupId(group.getId());
			if (ObjectUtils.isNotEmpty(users)) {
				exceptionFactory.throwForbidden(ErrorConstant.Group_ALREADY_USER_USE, users.get(0).getName());
			}
		}
		groupRepository.delete(group);
	}
}
