package com.plumdo.flow.rest.task;

import com.plumdo.flow.rest.AbstractPaginateList;
import com.plumdo.flow.rest.RestResponseFactory;

import java.util.List;

/**
 * 任务分页结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class TaskTodoPaginateList extends AbstractPaginateList {

    protected RestResponseFactory restResponseFactory;

    public TaskTodoPaginateList(RestResponseFactory restResponseFactory) {
        this.restResponseFactory = restResponseFactory;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected List processList(List list) {
        return restResponseFactory.createTaskResponseList(list);
    }
}
