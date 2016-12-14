package com.feicui.news.common;

import com.feicui.news.model.entity.BaseEntity;
import com.feicui.news.model.entity.Register;
import com.feicui.news.model.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/** �����û�ģ��ķ������� */
public class ParserUser {
	/**
	 * �����û�ע�᷵����Ϣ
	 */
	public static BaseEntity<Register> parserRegister(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<BaseEntity<Register>>() {
		}.getType());
	}

	/**
	 * �����û���������
	 */
	public static BaseEntity<User> parserUser(String json) {
		return new Gson().fromJson(json, new TypeToken<BaseEntity<User>>() {
		}.getType());
	}

	/**
	 * �����ϴ��û�ͷ��
	 */
	public static BaseEntity<Register> parserUploadImage(String json) {
		return new Gson().fromJson(json, new TypeToken<BaseEntity<Register>>() {
		}.getType());
	}
}
