package com.feicui.news.model.entity;

import android.graphics.Bitmap;

public class News {
	private int type;
	private int nid;// ���ű��,
	private String stamp;// ����ʱ���,
	private String icon;// ͼ��·��,
	private String title;// ���ű���
	private String summary;// ����ժҪ
	private String link;// ��������

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public String getStamp() {
		return stamp;
	}

	public void setStamp(String stamp) {
		this.stamp = stamp;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
