package com.feicui.news.model.entity;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable{
	
	/** ͷ�� */
	private String portrait;
	/** �û�id */
	private String uid;
	/** �û����� */
	private String email;
	/** �û����� */
	private int UserIntegral;
	/** �������� */
	private int comments;
	/** ��½��־ */
	private List<LoginLog> loginlog;

	public User(String uid, String email, int UserIntegral, int comments,
			String portrait, List<LoginLog> loginlog) {
		this.uid = uid;
		this.UserIntegral = UserIntegral;
		this.email = email;
		this.comments = comments;
		this.portrait = portrait;
		this.loginlog = loginlog;
	}

	public String getUid() {
		return uid;
	}

	public int getIntegration() {
		return UserIntegral;
	}

	public int getComnum() {
		return comments;
	}

	public String getPortrait() {
		return portrait;
	}

	public List<LoginLog> getLoginlog() {
		return loginlog;
	}
}
