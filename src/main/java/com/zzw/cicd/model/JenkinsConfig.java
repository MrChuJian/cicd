package com.zzw.cicd.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class JenkinsConfig implements Serializable {

	private String url;
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "JenkinsConfig [url=" + url + ", username=" + username + ", password=" + password + "]";
	}
	
	

}