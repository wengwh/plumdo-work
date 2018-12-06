package com.plumdo.identity.repository;

import org.springframework.transaction.annotation.Transactional;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.domain.UserGroup;

/**
 * 用户群组数据类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public interface UserGroupRepository extends BaseRepository<UserGroup, Integer> {

    /**
     * 根据用户ID删除
     *
     * @param userId
     * @return
     */
    @Transactional
    int deleteByUserId(int userId);

    /**
     * 根据群组ID和用户ID删除
     *
     * @param groupId
     * @param userId
     * @return
     */
    @Transactional
    int deleteByGroupIdAndUserId(int groupId, int userId);
}