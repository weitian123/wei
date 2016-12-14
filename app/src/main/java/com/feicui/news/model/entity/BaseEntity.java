package com.feicui.news.model.entity;

import java.io.Serializable;


public class BaseEntity<T> implements Serializable{

	/**
	 * ������������
	 */
	private String message;
	/**
	 * ״̬
	 */
	private String status;
	/**
	 * ����(�����Ǽ��� Ҳ�����Ƕ���)
	 */
	private T data;
	
	public BaseEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}
