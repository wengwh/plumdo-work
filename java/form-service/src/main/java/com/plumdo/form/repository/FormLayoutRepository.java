package com.plumdo.form.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.form.domain.FormLayout;

public interface FormLayoutRepository extends BaseRepository<FormLayout, Integer> {

	List<FormLayout> findByTableId(int tableId);

	FormLayout findFirstByKeyAndTableId(String key, int tableId);

	@Transactional
	int deleteByTableId(int tableId);
}