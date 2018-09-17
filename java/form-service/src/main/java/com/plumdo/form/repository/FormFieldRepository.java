package com.plumdo.form.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.form.domain.FormField;

public interface FormFieldRepository extends BaseRepository<FormField, Integer>   {

	List<FormField> findByTableId(int tableId);

	@Transactional
	int deleteByTableId(int tableId);
}