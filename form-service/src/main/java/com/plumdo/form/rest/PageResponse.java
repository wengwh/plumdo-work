package com.plumdo.form.rest;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PageResponse<T> {
	@ApiModelProperty(value = "查询数据")
	protected List<T> data;
	@ApiModelProperty(value = "当前页数")
	protected int pageNum;
	@ApiModelProperty(value = "每页条数")
	protected int pageSize;
	@ApiModelProperty(value = "总页数")
	protected int pageTotal;
	@ApiModelProperty(value = "总条数")
	protected long dataTotal;
	@ApiModelProperty(value = "开始条数")
	protected long startNum;
	@ApiModelProperty(value = "结束条数")
	protected long endNum;


	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}


	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public long getDataTotal() {
		return dataTotal;
	}

	public void setDataTotal(long dataTotal) {
		this.dataTotal = dataTotal;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getStartNum() {
		return startNum;
	}

	public void setStartNum(long startNum) {
		this.startNum = startNum;
	}

	public long getEndNum() {
		return endNum;
	}

	public void setEndNum(long endNum) {
		this.endNum = endNum;
	}


}