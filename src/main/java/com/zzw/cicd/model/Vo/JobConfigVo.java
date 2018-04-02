package com.zzw.cicd.model.Vo;
public class JobConfigVo {

	private Boolean inQueue;
	private Boolean buildAble;
	private Integer nextBuildNumber;

	private JobBuildConfigVo lastBuild;
	private String jobType;

	public Boolean getInQueue() {
		return inQueue;
	}

	public Boolean getBuildAble() {
		return buildAble;
	}

	public Integer getNextBuildNumber() {
		return nextBuildNumber;
	}

	public JobBuildConfigVo getLastBuild() {
		return lastBuild;
	}

	public void setInQueue(Boolean inQueue) {
		this.inQueue = inQueue;
	}

	public void setNextBuildNumber(Integer nextBuildNumber) {
		this.nextBuildNumber = nextBuildNumber;
	}

	public void setLastBuild(JobBuildConfigVo lastBuild) {
		this.lastBuild = lastBuild;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
}
