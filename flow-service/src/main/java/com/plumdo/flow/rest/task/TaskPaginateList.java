package com.plumdo.flow.rest.task;

import java.util.List;


import cn.starnet.flowable.rest.service.AbstractPaginateList;
import cn.starnet.flowable.rest.service.RestResponseFactory;

public class TaskPaginateList extends AbstractPaginateList {

	protected RestResponseFactory restResponseFactory;

	public TaskPaginateList(RestResponseFactory restResponseFactory) {
		this.restResponseFactory = restResponseFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List processList(List list) {
		return restResponseFactory.createTaskResponseList(list);
	}
}
