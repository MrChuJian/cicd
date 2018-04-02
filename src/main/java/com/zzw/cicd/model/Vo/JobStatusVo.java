package com.zzw.cicd.model.Vo;

import java.util.ArrayList;
import java.util.List;

public class JobStatusVo {

	private Integer number;
	private boolean building;
	private String result;

	private long timestamp;
	private long duration;

	private List<ChangesetVo> changeSets = new ArrayList<ChangesetVo>();

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public boolean getBuilding() {
		return building;
	}

	public void setBuilding(boolean building) {
		this.building = building;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public List<ChangesetVo> getChangeSets() {
		return changeSets;
	}

	public void setChangeSets(List<ChangesetVo> changeSets) {
		this.changeSets = changeSets;
	}

	public String toString() {
		return "{number:" + number + ",building:" + building + ",result:" + result + "}";
	}
}