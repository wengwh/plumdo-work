package com.plumdo.identity.repository;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Role;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色数据类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public interface RoleRepository extends BaseRepository<Role, Integer> {
    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    @Query("select a from Role a, UserRole b where a.id = b.roleId  and b.userId = ?1 and a.status= " + TableConstant.ROLE_STATUS_NORMAL)
    List<Role> findByUserId(int userId);

    /**
     * 根据菜单ID查询角色
     *
     * @param menuId
     * @return
     */
    @Query("select a from Role a, RoleMenu b where a.id = b.roleId and b.menuId = ?1 ")
    List<Role> findByMenuId(int menuId);

    /**
     * 根据状态查询角色
     *
     * @param status
     * @return
     */
    List<Role> findByStatus(byte status);
}