package com.zzw.cicd.model.Vo;
public class FtlVo {

	private String displayName;
	private boolean hasTrigger;
	private String spec;
	private String script;
	private int daysToKeep;
	private int numToKeep;
	private int quietPeriod;
	private boolean hasTimeTrigger;
	private boolean hasSCMTrigger;
	private String ignoreHook;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean getHasTrigger() {
		return hasTrigger;
	}

	public void setHasTrigger(boolean hasTrigger) {
		this.hasTrigger = hasTrigger;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public int getDaysToKeep() {
		return daysToKeep;
	}

	public void setDaysToKeep(int daysToKeep) {
		this.daysToKeep = daysToKeep;
	}

	public int getNumToKeep() {
		return numToKeep;
	}

	public void setNumToKeep(int numToKeep) {
		this.numToKeep = numToKeep;
	}

	public int getQuietPeriod() {
		return quietPeriod;
	}

	public void setQuietPeriod(int quietPeriod) {
		this.quietPeriod = quietPeriod;
	}

	public boolean getHasTimeTrigger() {
		return hasTimeTrigger;
	}

	public void setHasTimeTrigger(boolean hasTimeTrigger) {
		this.hasTimeTrigger = hasTimeTrigger;
	}

	public boolean getHasSCMTrigger() {
		return hasSCMTrigger;
	}

	public void setHasSCMTrigger(boolean hasSCMTrigger) {
		this.hasSCMTrigger = hasSCMTrigger;
	}

	public String getIgnoreHook() {
		return ignoreHook;
	}

	public void setIgnoreHook(String ignoreHook) {
		this.ignoreHook = ignoreHook;
	}

}