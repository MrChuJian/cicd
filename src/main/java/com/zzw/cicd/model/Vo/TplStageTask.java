package com.zzw.cicd.model.Vo;

import java.util.List;

public class TplStageTask {
	private Integer stageId;
	private int[] taskIds;
	private String stageName;
	private List<PplTask> tasks;

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public int[] getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(int[] tasks) {
		this.taskIds = tasks;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public List<PplTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<PplTask> tasks) {
		this.tasks = tasks;
	}

}