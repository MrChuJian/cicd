package com.zzw.cicd.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PluginDependencies implements Serializable {

	private boolean optional;

	public boolean getOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	private String shortName;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}