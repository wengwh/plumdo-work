package com.plumdo.flow.rest;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.query.Query;
import org.flowable.engine.common.api.query.QueryProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.plumdo.common.resource.PageResponse;

public abstract class AbstractPaginateList {

	@SuppressWarnings("rawtypes")
	public PageResponse paginateList(Pageable pageable, Query query, Map<String, QueryProperty> properties) {
		Sort sort = pageable.getSort();
		if (sort != null && !properties.isEmpty()) {
			Iterator<Order> orders = sort.iterator();
			while (orders.hasNext()) {
				Order order = orders.next();
				QueryProperty qp = properties.get(order.getProperty());
				if (qp == null) {
					throw new FlowableIllegalArgumentException("Value for param 'sort' is not valid, '" + sort + "' is not a valid property");
				}
				query.orderBy(qp);
				if (order.getDirection() == Direction.ASC) {
					query.asc();
				} else {
					query.desc();
				}
			}
		}

		List list = null;
		// size等于-1不做分页
		if (pageable.getPageSize() == -1) {
			list = processList(query.list());
		} else {
			list = processList(query.listPage(pageable.getOffset(), pageable.getPageSize()));
		}

		PageResponse response = new PageResponse();
		response.setData(list);
		response.setTotal(query.count());
		return response;
	}

	@SuppressWarnings("rawtypes")
	protected abstract List processList(List list);
}
