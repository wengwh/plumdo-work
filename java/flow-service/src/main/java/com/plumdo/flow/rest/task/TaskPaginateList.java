package com.plumdo.flow.rest.task;

import java.util.List;

import com.plumdo.flow.rest.AbstractPaginateList;
import com.plumdo.flow.rest.RestResponseFactory;

/**
 * 任务分页结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class TaskPaginateList extends AbstractPaginateList {

    protected RestResponseFactory restResponseFactory;

    public TaskPaginateList(RestResponseFactory restResponseFactory) {
        this.restResponseFactory = restResponseFactory;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected List processList(List list) {
        return restResponseFactory.createHistoricTaskResponseList(list);
    }
}
