package com.feicui.news.model.entity;

import java.util.List;

public class NewsP {
	private int status;
	private String message;
	private List<News> data;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<News> getData() {
		return data;
	}

	public void setData(List<News> data) {
		this.data = data;
	}

}
