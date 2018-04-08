package com.plumdo.common.resource;

import java.util.List;

/**
 * 分页查询结果类
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
public class PageResponse<T> {
	protected List<T> data;
	protected long total;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}