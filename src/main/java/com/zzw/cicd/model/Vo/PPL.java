package com.zzw.cicd.model.Vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
public class PPL implements Serializable {
	private Integer id;
	private String name;
	private String script;
	private String metadata;
	private PplTemplate pplTemplate;
	private String node;
	private String authToken;
	private boolean webhook = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public PplTemplate getPplTemplate() {
		if(metadata != null) {
			pplTemplate = JSONObject.parseObject(metadata, PplTemplate.class);
		}
		return pplTemplate;
	}

	public void setPplTemplate(PplTemplate pplTemplate) {
		this.pplTemplate = pplTemplate;
		if (null != pplTemplate) {
			String jsonString = JSONObject.toJSONString(pplTemplate);
			setMetadata(jsonString);
		}
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public boolean isWebhook() {
		return webhook;
	}

	public void setWebhook(boolean webhook) {
		this.webhook = webhook;
	}
	
	

}