package com.feicui.news.model.entity;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable{
	
	/** 头像 */
	private String portrait;
	/** 用户id */
	private String uid;
	/** 用户邮箱 */
	private String email;
	/** 用户积分 */
	private int UserIntegral;
	/** 评论数量 */
	private int comments;
	/** 登陆日志 */
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
