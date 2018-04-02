package com.zzw.cicd.model.Vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PplTaskParam implements Serializable {

	private String key;
	private String value;
	private String description;
	private boolean isGobal = false;

	public boolean isGobal() {
		return isGobal;
	}

	public void setGobal(boolean isGobal) {
		this.isGobal = isGobal;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "PplTaskParam [key=" + key + ", value=" + value + ", description=" + description + ", isGobal=" + isGobal
				+ "]";
	}
	
	

}