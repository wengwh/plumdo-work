package com.plumdo.identity.repository;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.domain.UserRole;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户角色数据类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public interface UserRoleRepository extends BaseRepository<UserRole, Integer> {
    /**
     * 根据用户ID删除
     *
     * @param userId
     * @return
     */
    @Transactional
    int deleteByUserId(int userId);


    /**
     * 根据角色ID和用户ID删除
     *
     * @param roleId
     * @param userId
     * @return
     */
    @Transactional
    int deleteByRoleIdAndUserId(int roleId, int userId);

}