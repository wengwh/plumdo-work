package com.plumdo.form.repository;

import com.plumdo.common.repository.BaseRepository;
import com.plumdo.form.domain.FormLayout;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 表单布局数据类
 *
 * @author wengwh
 * @date 2018/12/10
 */
public interface FormLayoutRepository extends BaseRepository<FormLayout, Integer> {

    /**
     * 根据表格ID获取布局
     *
     * @param tableId
     * @return
     */
    List<FormLayout> findByTableId(int tableId);

    /**
     * 根据key和表格ID获取布局
     *
     * @param key
     * @param tableId
     * @return
     */
    FormLayout findFirstByKeyAndTableId(String key, int tableId);

    /**
     * 根据表格ID删除布局
     *
     * @param tableId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByTableId(int tableId);
}