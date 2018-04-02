package com.zzw.cicd.model.Vo;

import java.util.ArrayList;
import java.util.List;

public class ChangesetVo {

	private String kind;
	private int commitCount;
	private String consoleUrl;
	List<ChangesetItemsVo> items = new ArrayList<ChangesetItemsVo>();

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public int getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(int commitCount) {
		this.commitCount = commitCount;
	}

	public String getConsoleUrl() {
		return consoleUrl;
	}

	public void setConsoleUrl(String consoleUrl) {
		this.consoleUrl = consoleUrl;
	}

	public List<ChangesetItemsVo> getItems() {

		return items;
	}

	public void setItems(List<ChangesetItemsVo> items) {
		this.items = items;
	}
}