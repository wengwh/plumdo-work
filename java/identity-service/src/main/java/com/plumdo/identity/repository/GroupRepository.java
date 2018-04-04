package com.plumdo.identity.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.identity.constant.TableConstant;
import com.plumdo.identity.domain.Group;

public interface GroupRepository extends BaseRepository<Group, Integer> {
	@Query("select a from Group a, UserGroup b where a.id = b.groupId and a.status="+TableConstant.ROLE_STATUS_NORMAL+" and b.userId = ?1 order by a.order asc")
	List<Group> findByUserId(int userId);

	List<Group> findByParentId(int parentId);

	List<Group> findByStatusOrderByOrderAsc(byte status);
}