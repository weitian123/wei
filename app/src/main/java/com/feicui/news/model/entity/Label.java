package com.feicui.news.model.entity;

import java.util.List;

public class Label {
	private String message;
	private int status;
	private List<LabelGroup> data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<LabelGroup> getData() {
		return data;
	}

	public void setData(List<LabelGroup> data) {
		this.data = data;
	}
}
