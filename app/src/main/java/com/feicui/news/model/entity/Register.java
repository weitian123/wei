package com.feicui.news.model.entity;

/**
 * �й�ע��͵�¼��entity
 */
public class Register {

	String result; 
	String token;  //�û����ƣ�������֤�û���ÿ�����󶼴��ݸ�������������ʱЧ�ڡ�
	String explain;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExplain() {
		return explain;
	}

	public void setLexplain(String explain) {
		this.explain = explain;
	}
}
