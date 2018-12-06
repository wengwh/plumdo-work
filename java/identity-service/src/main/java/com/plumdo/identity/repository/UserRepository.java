package com.plumdo.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.domain.User;

/**
 * 用户数据类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public interface UserRepository extends BaseRepository<User, Integer> {
    /**
     * 根据账户查询用户
     *
     * @param account
     * @return
     */
    User findByAccount(String account);

    /**
     * 根据角色ID查询用户
     *
     * @param roleId
     * @return
     */
    @Query("select a from User a, UserRole b where a.id = b.userId and b.roleId = ?1 ")
    List<User> findByRoleId(int roleId);

    /**
     * 根据群组ID查询用户
     *
     * @param groupId
     * @return
     */
    @Query("select a from User a, UserGroup b where a.id = b.userId and b.groupId = ?1 ")
    List<User> findByGroupId(int groupId);
}