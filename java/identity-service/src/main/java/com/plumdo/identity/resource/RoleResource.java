package com.plumdo.identity.resource;

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
import com.plumdo.identity.repository.*;
import com.plumdo.identity.response.ConvertFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色资源控制类
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
@RestController
public class RoleResource extends BaseResource {
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final RoleMenuRepository roleMenuRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public RoleResource(RoleRepository roleRepository, MenuRepository menuRepository, RoleMenuRepository roleMenuRepository, UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.menuRepository = menuRepository;
        this.roleMenuRepository = roleMenuRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    private Role getRoleFromRequest(Integer id) {
        Role role = roleRepository.findOne(id);
        if (role == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.ROLE_NOT_FOUND);
        }
        return role;
    }

    @GetMapping(value = "/roles")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getRoles(@RequestParam Map<String, String> requestParams) {
        Criteria<Role> criteria = new Criteria<>();
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
        List<Menu> allMenus = menuRepository.findByStatusOrderByOrderAsc(TableConstant.MENU_STATUS_NORMAL);
        if (ObjectUtils.isNotEmpty(id)) {
            roleMenus = menuRepository.findByRoleId(id);
        }
        return ConvertFactory.convertRoleMenus(allMenus, roleMenus);
    }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Role createRole(@RequestBody ObjectMap roleRequest) {
        return saveRoleAndMenu(null, roleRequest);
    }

    @PutMapping(value = "/roles/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
