package com.feicui.news.model.entity;

import java.io.Serializable;

public class LabelSub implements Serializable {
	private int subid;// 子分类号，
	private String subgroup;// 子分类名

	public int getSubid() {
		return subid;
	}

	public void setSubid(int subid) {
		this.subid = subid;
	}

	public String getSubgroup() {
		return subgroup;
	}

	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}

}
