package com.feicui.news.model.entity;

import java.util.List;

public class LabelGroup {
	private String group;// ������
	private int gid;// �����
	private List<LabelSub> subgrp;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public List<LabelSub> getSubgrp() {
		return subgrp;
	}

	public void setSubgrp(List<LabelSub> subgrp) {
		this.subgrp = subgrp;
	}
}
