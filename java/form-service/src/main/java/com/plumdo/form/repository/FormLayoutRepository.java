package com.plumdo.form.repository;

import org.springframework.transaction.annotation.Transactional;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.form.domain.FormLayout;

public interface FormLayoutRepository extends BaseRepository<FormLayout, Integer> {

	@Transactional
	int deleteByTableId(int tableId);
}