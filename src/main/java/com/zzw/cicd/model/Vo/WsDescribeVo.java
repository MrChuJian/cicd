package com.zzw.cicd.model.Vo;

import java.sql.Timestamp;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class WsDescribeVo {

	private String id;
	private String name;
	private String status;
	private Timestamp startTimeMillis;
	private long endTimeMillis;
	private long durationMillis;
	private long queueDurationMillis;
	@JSONField(name = "_links")
	private LinksVo links;
	private List<WsStageVo> stages;
	private List<WsStageVo> stageFlowNodes;
	private ChangesetVo changeset;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getStartTimeMillis() {
		return startTimeMillis;
	}

	public void setStartTimeMillis(Timestamp startTimeMillis) {
		this.startTimeMillis = startTimeMillis;
	}

	public long getEndTimeMillis() {
		return endTimeMillis;
	}

	public void setEndTimeMillis(long endTimeMillis) {
		this.endTimeMillis = endTimeMillis;
	}

	public long getDurationMillis() {
		return durationMillis;
	}

	public void setDurationMillis(int durationMillis) {
		this.durationMillis = durationMillis;
	}

	public long getQueueDurationMillis() {
		return queueDurationMillis;
	}

	public void setQueueDurationMillis(int queueDurationMillis) {
		this.queueDurationMillis = queueDurationMillis;
	}

	public LinksVo getLinks() {
		return links;
	}

	public void setLinks(LinksVo links) {
		this.links = links;
	}

	public List<WsStageVo> getStages() {
		return stages;
	}

	public void setStages(List<WsStageVo> stages) {
		this.stages = stages;
	}

	public List<WsStageVo> getStageFlowNodes() {
		return stageFlowNodes;
	}

	public void setStageFlowNodes(List<WsStageVo> stageFlowNodes) {
		this.stageFlowNodes = stageFlowNodes;
	}

	public ChangesetVo getChangeset() {
		return changeset;
	}

	public void setChangeset(ChangesetVo changeset) {
		this.changeset = changeset;
	}
}