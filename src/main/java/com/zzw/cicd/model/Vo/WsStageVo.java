package com.zzw.cicd.model.Vo;

import java.sql.Timestamp;

public class WsStageVo {

	private String id;
	private String name;
	private String status;
	private Timestamp startTimeMillis;
	private long durationMillis;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getStartTimeMillis() {
		return startTimeMillis;
	}

	public void setStartTimeMillis(Timestamp startTimeMillis) {
		this.startTimeMillis = startTimeMillis;
	}

	public long getDurationMillis() {
		return durationMillis;
	}

	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}
}
