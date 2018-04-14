package com.plumdo.flow.rest.instance;

import java.util.List;

import com.plumdo.flow.rest.AbstractPaginateList;
import com.plumdo.flow.rest.RestResponseFactory;


public class ProcessInstancePaginateList extends AbstractPaginateList {

	protected RestResponseFactory restResponseFactory;

	public ProcessInstancePaginateList(
			RestResponseFactory restResponseFactory) {
		this.restResponseFactory = restResponseFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List processList(List list) {
		return restResponseFactory.createProcessInstanceResponseList(list);
	}
}
