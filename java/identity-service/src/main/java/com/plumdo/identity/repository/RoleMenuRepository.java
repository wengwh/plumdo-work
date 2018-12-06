package com.plumdo.identity.repository;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.domain.RoleMenu;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色菜单数据类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public interface RoleMenuRepository extends BaseRepository<RoleMenu, Integer> {

    /**
     * 根据角色ID删除
     *
     * @param roleId
     * @return
     */
    @Transactional
    int deleteByRoleId(int roleId);


    /**
     * 根据菜单ID和角色ID删除
     *
     * @param menuId
     * @param roleId
     * @return
     */
    @Transactional
    int deleteByMenuIdAndRoleId(int menuId, int roleId);

}