package com.zzw.cicd.model.Vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class PplTask {
	private Integer id;
	private String name;
	private String input;
	private String linuxScript;
	private String windowsScript;
	private Integer stageId;
	private String description;
	private List<PplTaskParam> taskParams;
	private String workspace;

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

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
		List<PplTaskParam> pplTaskParams = new ArrayList<>();
		JSONArray jsonArray = (JSONArray) JSONArray.parse(input);
		Iterator<Object> iterator = jsonArray.iterator();
		while (iterator.hasNext()) {
			JSONObject next = JSONObject.parseObject(iterator.next().toString());
			PplTaskParam pplTaskParam = new PplTaskParam();
			pplTaskParam.setKey(next.getString("key"));
			pplTaskParam.setValue(next.getString("value"));
			pplTaskParam.setDescription(next.getString("description"));
			boolean isGobal = false;
			if ("true".equals(next.getString("is_gobal"))) {
				isGobal = true;
			}
			pplTaskParam.setGobal(isGobal);
			pplTaskParams.add(pplTaskParam);
		}
		this.taskParams = pplTaskParams;

	}

	public String getLinuxScript() {
		return linuxScript;
	}

	public void setLinuxScript(String linuxScript) {
		this.linuxScript = linuxScript;
	}

	public String getWindowsScript() {
		return windowsScript;
	}

	public void setWindowsScript(String windowsScript) {
		this.windowsScript = windowsScript;
	}

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<PplTaskParam> getTaskParams() {
		return taskParams;
	}

	public void setTaskParams(List<PplTaskParam> taskParams) {
		this.taskParams = taskParams;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	@Override
	public String toString() {
		return "PplTask [id=" + id + ", name=" + name + ", input=" + input + ", linuxScript=" + linuxScript
				+ ", windowsScript=" + windowsScript + ", stageId=" + stageId + ", description=" + description
				+ ", taskParams=" + taskParams + ", workspace=" + workspace + "]";
	}
	
	

}