package com.zzw.cicd.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BuildTrigger implements Serializable {

	private String id;
	private String desc;
	private String cron;
	private boolean pollScm;
	private int quietPeriod;
	private boolean ignoreHook;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public boolean getPollScm() {
		return pollScm;
	}

	public void setPollScm(boolean pollScm) {
		this.pollScm = pollScm;
	}

	public int getQuietPeriod() {
		return quietPeriod;
	}

	public void setQuietPeriod(int quietPeriod) {
		this.quietPeriod = quietPeriod;
	}

	public boolean getIgnoreHook() {
		return ignoreHook;
	}

	public void setIgnoreHook(boolean ignoreHook) {
		this.ignoreHook = ignoreHook;
	}

}