package com.plumdo.flow.rest.model;

import java.util.List;

import com.plumdo.flow.rest.AbstractPaginateList;
import com.plumdo.flow.rest.RestResponseFactory;


/**
 * 模型分页结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class ModelsPaginateList extends AbstractPaginateList {

    protected RestResponseFactory restResponseFactory;

    public ModelsPaginateList(RestResponseFactory restResponseFactory) {
        this.restResponseFactory = restResponseFactory;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected List processList(List list) {
        return restResponseFactory.createModelResponseList(list);
    }
}
