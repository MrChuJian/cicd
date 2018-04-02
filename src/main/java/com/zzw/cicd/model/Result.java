package com.zzw.cicd.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Result implements Serializable {
	private boolean result;
	private String msg;

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}