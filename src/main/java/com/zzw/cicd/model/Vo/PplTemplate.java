package com.zzw.cicd.model.Vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class PplTemplate {
	private Integer id;
	private String name;
	private String language;
	private String tplStageTask;
	private String defaultParams;
	private List<PplStage> stages;

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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDefaultParams() {
		return defaultParams;
	}

	public void setDefaults_params(String defaultParams) {
		this.defaultParams = defaultParams;
	}

	public List<PplStage> getStages() {
		return stages;
	}

	public void setStages(List<PplStage> stages) {
		this.stages = stages;
	}

	public String getTplStageTask() {
		return tplStageTask;
	}

	public List<TplStageTask> getTplStageTasks() {
		// 把input转换成PplTaskParam对象
		List<TplStageTask> tplStageTasks = new ArrayList<>();
		JSONArray jsonArray = (JSONArray) JSONArray.parse(getTplStageTask());
		Iterator<Object> iterator = jsonArray.iterator();
		while (iterator.hasNext()) {
			JSONObject next = JSONObject.parseObject(iterator.next().toString());
			TplStageTask tplStageTask = new TplStageTask();
			tplStageTask.setStageId(next.getInteger("stage_id"));
			String ss = next.getString("task_ids");
			String[] split = ss.split(",");
			int[] taskIds = new int[split.length];
			for (int i = 0; i < split.length; i++) {
				int parseInt = Integer.parseInt(split[i]);
				taskIds[i] = parseInt;

			}
			tplStageTask.setTaskIds(taskIds);

			tplStageTasks.add(tplStageTask);
		}
		return tplStageTasks;
	}

	public void setTplStageTask(String tplStageTask) {
		this.tplStageTask = tplStageTask;
	}

	@Override
	public String toString() {
		return "PplTemplate [id=" + id + ", name=" + name + ", language=" + language + ", tplStageTask=" + tplStageTask
				+ ", defaultParams=" + defaultParams + ", stages=" + stages + "]";
	}
}