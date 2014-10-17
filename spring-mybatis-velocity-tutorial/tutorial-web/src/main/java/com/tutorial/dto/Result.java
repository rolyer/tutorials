package com.tutorial.dto;

import java.io.Serializable;

public class Result implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean success;
	private Object data;

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
