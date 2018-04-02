package com.zzw.cicd.model.Vo;

import java.io.Serializable;

@SuppressWarnings("serial")

public class CredentialVo implements Serializable {

	private String credentialId;

	public String getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(String credentialId) {
		this.credentialId = credentialId;
	}

	private String credentialType;

	public String getCredentialType() {
		return credentialType;
	}

	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}

	private String credentialUser;

	public String getCredentialUser() {
		return credentialUser;
	}

	public void setCredentialUser(String credentialUser) {
		this.credentialUser = credentialUser;
	}

	private String credentialKey;

	public String getCredentialKey() {
		return credentialKey;
	}

	public void setCredentialKey(String credentialKey) {
		this.credentialKey = credentialKey;
	}

	private String credentialDesc;

	public String getCredentialDesc() {
		return credentialDesc;
	}

	public void setCredentialDesc(String credentialDesc) {
		this.credentialDesc = credentialDesc;
	}

	private String privateKey;

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
}