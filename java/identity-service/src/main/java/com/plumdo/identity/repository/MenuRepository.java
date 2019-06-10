package com.plumdo.identity.repository;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Menu;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 菜单数据类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public interface MenuRepository extends BaseRepository<Menu, Integer> {

    /**
     * 根据角色ID查询菜单
     *
     * @param roleId
     * @return
     */
    @Query("select a from Menu a, RoleMenu b where a.id = b.menuId and a.status=" + TableConstant.MENU_STATUS_NORMAL + " and b.roleId = ?1 order by a.order asc")
    List<Menu> findByRoleId(int roleId);

    /**
     * 根据用户ID查询菜单
     *
     * @param userId
     * @return
     */
    @Query("select distinct a from Menu a, RoleMenu b, UserRole c where a.id = b.menuId and b.roleId = c.roleId and a.status=" + TableConstant.MENU_STATUS_NORMAL + " and c.userId = ?1 order by a.order asc")
    List<Menu> findByUserId(int userId);

    /**
     * 根据类型和状态查询菜单
     *
     * @param type
     * @param status
     * @return
     */
    List<Menu> findByTypeAndStatusOrderByOrderAsc(byte type, byte status);

    /**
     * 根据父ID查询菜单
     *
     * @param parentId
     * @return
     */
    List<Menu> findByParentId(int parentId);

    /**
     * 根据状态查询菜单
     *
     * @param status
     * @return
     */
    List<Menu> findByStatusOrderByOrderAsc(byte status);

}