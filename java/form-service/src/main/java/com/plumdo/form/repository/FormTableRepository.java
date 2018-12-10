package com.plumdo.form.repository;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.form.domain.FormTable;

/**
 * 表格数据类
 *
 * @author wengwh
 * @date 2018/12/10
 */
public interface FormTableRepository extends BaseRepository<FormTable, Integer> {
    /**
     * 根据key获取表格
     *
     * @param key
     * @return FormTable
     */
    FormTable findByKey(String key);
}