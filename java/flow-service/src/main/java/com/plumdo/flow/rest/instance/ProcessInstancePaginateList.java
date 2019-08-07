package com.plumdo.flow.rest.instance;

import com.plumdo.flow.rest.AbstractPaginateList;
import com.plumdo.flow.rest.RestResponseFactory;

import java.util.List;

/**
 * 流程实例分页结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class ProcessInstancePaginateList extends AbstractPaginateList {

    protected RestResponseFactory restResponseFactory;

    public ProcessInstancePaginateList(RestResponseFactory restResponseFactory) {
        this.restResponseFactory = restResponseFactory;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected List processList(List list) {
        return restResponseFactory.createHistoricProcessInstanceResponseList(list);
    }
}
