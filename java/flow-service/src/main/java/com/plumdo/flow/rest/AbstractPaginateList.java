package com.plumdo.flow.rest;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.query.Query;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.common.impl.AbstractQuery;
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
			while(orders.hasNext()) {
				Order order = orders.next();
				QueryProperty qp = properties.get(order.getProperty());
				if (qp == null) {
					throw new FlowableIllegalArgumentException("Value for param 'sort' is not valid, '" + sort + "' is not a valid property");
				}
				query.orderBy(qp);
				if(order.getDirection()==Direction.ASC) {
					query.asc();
				}else {
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
	public DataResponse paginateList(Map<String, String> requestParams, PaginateRequest paginateRequest, Query query, String defaultSort, Map<String, QueryProperty> properties) {

		if (paginateRequest == null) {
			paginateRequest = new PaginateRequest();
		}

		// In case pagination request is incomplete, fill with values found in
		// URL if possible
		if (paginateRequest.getPageNum() == null) {
			paginateRequest.setPageNum(RequestUtil.getInteger(requestParams, "pageNum", 1));
		}

		if (paginateRequest.getPageSize() == null) {
			paginateRequest.setPageSize(RequestUtil.getInteger(requestParams, "pageSize", 10));
		}

		if (paginateRequest.getSortOrder() == null) {
			paginateRequest.setSortOrder(requestParams.get("sortOrder"));
		}

		if (paginateRequest.getSortName() == null) {
			paginateRequest.setSortName(requestParams.get("sortName"));
		}

		// Use defaults for paging, if not set in the PaginationRequest, nor in
		// the URL
		Integer pageNum = paginateRequest.getPageNum();
		if (pageNum == null || pageNum <= 0) {
			pageNum = 1;
		}

		Integer pageSize = paginateRequest.getPageSize();
		if (pageSize == null || (pageSize != -1 && pageSize < 0)) {
			pageSize = 10;
		}

		String sort = paginateRequest.getSortName();
		if (sort == null) {
			sort = defaultSort;
		}
		String order = paginateRequest.getSortOrder();
		if (order == null) {
			order = "asc";
		}

		// Sort order
		if (sort != null && !properties.isEmpty()) {
			QueryProperty qp = properties.get(sort);
			if (qp == null) {
				throw new FlowableIllegalArgumentException("Value for param 'sort' is not valid, '" + sort + "' is not a valid property");
			}
			((AbstractQuery) query).orderBy(qp);
			if (order.equals("asc")) {
				query.asc();
			} else if (order.equals("desc")) {
				query.desc();
			} else {
				throw new FlowableIllegalArgumentException("Value for param 'order' is not valid : '" + order + "', must be 'asc' or 'desc'");
			}
		}

		// Get result and set pagination parameters
		List list = null;
		// size等于-1不做分页
		if (pageSize == -1) {
			list = processList(query.list());
		} else {
			list = processList(query.listPage((pageNum - 1) * pageSize, pageSize));
		}
		DataResponse response = new DataResponse();
		response.setData(list);
		response.setDataTotal(query.count());
		response.setPageSize(pageSize);
		response.setPageNum(pageNum);
		response.setStartNum((pageNum - 1) * pageSize + 1);
		if (response.getDataTotal() > pageNum * pageSize) {
			response.setEndNum(pageNum * pageSize);
		} else {
			response.setEndNum(response.getDataTotal());
		}
		if (response.getDataTotal() % pageSize == 0) {
			response.setPageTotal((int) (response.getDataTotal() / pageSize));
		} else {
			response.setPageTotal((int) (response.getDataTotal() / pageSize) + 1);
		}
		return response;
	}

	@SuppressWarnings("rawtypes")
	public DataResponse paginateList(Map<String, String> requestParams, Query query, String defaultSort, Map<String, QueryProperty> properties) {
		return paginateList(requestParams, null, query, defaultSort, properties);
	}

	@SuppressWarnings("rawtypes")
	protected abstract List processList(List list);
}
