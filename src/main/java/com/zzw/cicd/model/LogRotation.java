package com.zzw.cicd.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LogRotation implements Serializable {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private int daysToKeep;

	public int getDaysToKeep() {
		return daysToKeep;
	}

	public void setDaysToKeep(int daysToKeep) {
		this.daysToKeep = daysToKeep;
	}

	private int numToKeep;

	public int getNumToKeep() {
		return numToKeep;
	}

	public void setNumToKeep(int numToKeep) {
		this.numToKeep = numToKeep;
	}

}