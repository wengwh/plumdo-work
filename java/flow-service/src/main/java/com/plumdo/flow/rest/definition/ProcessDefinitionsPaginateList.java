package com.plumdo.flow.rest.definition;

import java.util.List;

import com.plumdo.flow.rest.AbstractPaginateList;
import com.plumdo.flow.rest.RestResponseFactory;


/**
 * 流程定义分页结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class ProcessDefinitionsPaginateList extends AbstractPaginateList {

    protected RestResponseFactory restResponseFactory;

    public ProcessDefinitionsPaginateList(RestResponseFactory restResponseFactory) {
        this.restResponseFactory = restResponseFactory;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected List processList(List list) {
        return restResponseFactory.createProcessDefinitionResponseList(list);
    }
}
