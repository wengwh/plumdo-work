package com.plumdo.identity.resource;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
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

/**
 * 人员资源控制类
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
@RestController
public class UserResource extends BaseResource {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserResource(UserRepository userRepository, RoleRepository roleRepository, GroupRepository groupRepository, UserGroupRepository userGroupRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.userGroupRepository = userGroupRepository;
        this.userRoleRepository = userRoleRepository;
    }

    private User getUserFromRequest(Integer id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.USER_NOT_FOUND);
        }
        return user;
    }

    @GetMapping(value = "/users")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getUsers(@RequestParam Map<String, String> requestParams) {
        Criteria<User> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.like("phone", requestParams.get("phone")));
        criteria.add(Restrictions.eq("status", requestParams.get("status")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(userRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/users/match")
    @ResponseStatus(value = HttpStatus.OK)
    public List<User> matchUsers(@RequestParam(required = false) String filter) {
        if (filter == null || filter.isEmpty()) {
            return userRepository.findAll();
        }

        Criteria<User> criteria = new Criteria<>();
        criteria.add(Restrictions.or(Restrictions.like("name", filter), Restrictions.like("phone", filter)));
        return userRepository.findAll(criteria);
    }

    @GetMapping(value = "/users/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public User getUser(@PathVariable Integer id) {
        return getUserFromRequest(id);
    }

    @GetMapping(value = "/users/roles")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ObjectMap> getUserRoles(@RequestParam(required = false) Integer id) {
        List<Role> roleRoles = null;
        List<Role> allRoles = roleRepository.findByStatus(TableConstant.ROLE_STATUS_NORMAL);
        if (ObjectUtils.isNotEmpty(id)) {
            roleRoles = roleRepository.findByUserId(id);
        }
        return ConvertFactory.convertUseRoles(allRoles, roleRoles);
    }

    @GetMapping(value = "/users/groups")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ObjectMap> getUserGroups(@RequestParam(required = false) Integer id) {
        List<Group> roleGroups = null;
        List<Group> allGroups = groupRepository.findByStatusOrderByOrderAsc(TableConstant.GROUP_STATUS_NORMAL);
        if (ObjectUtils.isNotEmpty(id)) {
            roleGroups = groupRepository.findByUserId(id);
        }
        return ConvertFactory.convertUserGroups(allGroups, roleGroups);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public User createUser(@RequestBody ObjectMap userRequest) {
        String account = userRequest.getAsString("account");
        User user = userRepository.findByAccount(account);
        if (user != null) {
            exceptionFactory.throwConflict(ErrorConstant.USER_ACCOUNT_REPEAT);
        }
        return saveUserAndGroupAndRole(null, userRequest);
    }

    @PutMapping(value = "/users/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public User updateUser(@PathVariable Integer id, @RequestBody ObjectMap userRequest) {
        User user = getUserFromRequest(id);
        return saveUserAndGroupAndRole(user, userRequest);
    }

    private User saveUserAndGroupAndRole(User user, ObjectMap userRequest) {
        String phone = userRequest.getAsString("phone");
        if (user == null) {
            user = new User();
            user.setPwd(phone.substring(phone.length() - 6, phone.length()));
            user.setAccount(userRequest.getAsString("account"));
        }
        user.setName(userRequest.getAsString("name"));
        user.setSex(userRequest.getAsByte("sex"));
        user.setAvatar("http://wx.qlogo.cn/mmopen/fsFT5ibPNuBiaZGWzb7yT0yFy0ibaTENudO3LTia7fn4ibSc3mlma5alTpUDw39tx8EuCMrVqjCF9rMicak7H5MQ2tQ7LQTNt6cicv1/0");
        user.setEmail(userRequest.getAsString("email"));
        user.setPhone(phone);
        user.setStatus(userRequest.getAsByte("status"));
        user.setRemark(userRequest.getAsString("remark"));
        user.setTenantId(userRequest.getAsString("tenantId"));
        userRepository.save(user);

        userRoleRepository.deleteByUserId(user.getId());
        List<ObjectMap> roles = userRequest.getAsList("userRoles");
        for (ObjectMap role : roles) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(role.getAsInteger("id"));
            userRole.setUserId(user.getId());
            userRoleRepository.save(userRole);
        }

        userGroupRepository.deleteByUserId(user.getId());
        List<ObjectMap> groups = userRequest.getAsList("userGroups");
        for (ObjectMap group : groups) {
            UserGroup userGroup = new UserGroup();
            userGroup.setGroupId(group.getAsInteger("id"));
            userGroup.setUserId(user.getId());
            userGroupRepository.save(userGroup);
        }
        return user;
    }

    @DeleteMapping(value = "/users/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteUser(@PathVariable Integer id) {
        User user = getUserFromRequest(id);
        userRepository.delete(user);
        userRoleRepository.deleteByUserId(user.getId());
        userGroupRepository.deleteByUserId(user.getId());
    }
}
