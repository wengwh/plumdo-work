package com.plumdo.form.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plumdo.form.util.RequestUtil;

public abstract class BaseResource {

	@Autowired
	protected ResponseFactory responseFactory;	
	@Autowired
	protected ObjectMapper objectMapper;


	public Pageable getPageable(Map<String, String> requestParams) {
		int page = RequestUtil.getInteger(requestParams, "pageNum", 1) - 1;
		int size = RequestUtil.getInteger(requestParams, "pageSize", 10);
		String[] orders = RequestUtil.getArray(requestParams, "sortOrder");
		String[] sorts = RequestUtil.getArray(requestParams, "sortName");

		List<Order> sortOrders = new ArrayList<Order>();
		for (int i = 0; i < sorts.length; i++) {
			String sort = sorts[i];
			String order = orders[i];
			if (order.equals("asc")) {
				sortOrders.add(new Order(Direction.ASC, sort));
			} else if (order.equals("desc")) {
				sortOrders.add(new Order(Direction.DESC, sort));
			} else {
				throw new IllegalArgumentException("Value for param 'order' is not valid : '" + order + "', must be 'asc' or 'desc'");
			}
		}

		if (sortOrders.isEmpty()) {
			return new PageRequest(page, size);
		} else {
			return new PageRequest(page, size, new Sort(sortOrders));
		}
	}

}
