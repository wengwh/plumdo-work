package com.plumdo.form.repository;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.form.domain.FormField;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 表单字段数据类
 *
 * @author wengwh
 * @date 2018/12/10
 */
public interface FormFieldRepository extends BaseRepository<FormField, Integer> {

    /**
     * 根据表格id获取字段
     *
     * @param tableId
     * @return
     */
    List<FormField> findByTableId(int tableId);

    /**
     * 根据表格id删除字段
     *
     * @param tableId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByTableId(int tableId);
}