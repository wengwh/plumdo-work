package com.plumdo.common.resource;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plumdo.common.exception.ExceptionFactory;
import com.plumdo.common.utils.ObjectUtils;

public abstract class BaseResource {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	protected ExceptionFactory exceptionFactory;
	@Autowired
	protected ObjectMapper objectMapper;

	protected Pageable getPageable(Map<String, String> requestParams) {
		int page = 1;
		if (requestParams.containsKey("page")) {
			page = ObjectUtils.convertToInteger(requestParams.get("page"), 1);
		}
		int size = 10;
		if (requestParams.containsKey("limit")) {
			size = ObjectUtils.convertToInteger(requestParams.get("limit"), 10);
		}
		Order order = null;
		if (ObjectUtils.isNotEmpty(requestParams.get("order"))) {
			String orderBy = requestParams.get("order");
			if (orderBy.startsWith("-")) {
				order = new Order(Direction.DESC, orderBy.substring(1));
			} else {
				order = new Order(Direction.ASC, orderBy);
			}
		}

		if (order == null) {
			return new PageRequest(page - 1, size);
		} else {
			return new PageRequest(page - 1, size, new Sort(order));
		}
	}

	protected <T> PageResponse<T> createPageResponse(Page<T> page) {
		PageResponse<T> pageResponse = new PageResponse<T>();
		pageResponse.setData(page.getContent());
		pageResponse.setTotal(page.getTotalElements());
		return pageResponse;
	}

}
