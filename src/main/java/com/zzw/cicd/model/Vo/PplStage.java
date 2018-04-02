package com.zzw.cicd.model.Vo;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class PplStage implements Serializable {

	private Integer id;
	private String name;
	// 额外属性
	private List<PplTask> tasks;

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

	public List<PplTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<PplTask> tasks) {
		this.tasks = tasks;
	}

	@Override
	public String toString() {
		return "PplStage [id=" + id + ", name=" + name + ", tasks=" + tasks + "]";
	}
	
	
}