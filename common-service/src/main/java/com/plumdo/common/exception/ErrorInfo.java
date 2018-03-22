package com.plumdo.common.exception;


public class ErrorInfo {

	private String ret;
	private String msg;

	public ErrorInfo(String ret, String msg) {
		this.ret = ret;
		this.msg = msg;
	}

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
