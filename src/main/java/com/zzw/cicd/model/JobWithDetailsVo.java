package com.zzw.cicd.model;

import java.util.List;

public class JobWithDetailsVo {

	private String description;

    private String displayName;

    private boolean buildable;

    private List<BuildVo> builds;

    private BuildVo firstBuild;

    private BuildVo lastBuild;

    private BuildVo lastSuccessfulBuild;

    private int nextBuildNumber;

    private boolean inQueue;


    private List<JobVo> downstreamProjects;

    private List<JobVo> upstreamProjects;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isBuildable() {
		return buildable;
	}

	public void setBuildable(boolean buildable) {
		this.buildable = buildable;
	}

	public List<BuildVo> getBuilds() {
		return builds;
	}

	public void setBuilds(List<BuildVo> builds) {
		this.builds = builds;
	}

	public BuildVo getFirstBuild() {
		return firstBuild;
	}

	public void setFirstBuild(BuildVo firstBuild) {
		this.firstBuild = firstBuild;
	}

	public BuildVo getLastBuild() {
		return lastBuild;
	}

	public void setLastBuild(BuildVo lastBuild) {
		this.lastBuild = lastBuild;
	}


	public BuildVo getLastSuccessfulBuild() {
		return lastSuccessfulBuild;
	}

	public void setLastSuccessfulBuild(BuildVo lastSuccessfulBuild) {
		this.lastSuccessfulBuild = lastSuccessfulBuild;
	}


	public int getNextBuildNumber() {
		return nextBuildNumber;
	}

	public void setNextBuildNumber(int nextBuildNumber) {
		this.nextBuildNumber = nextBuildNumber;
	}

	public boolean isInQueue() {
		return inQueue;
	}

	public void setInQueue(boolean inQueue) {
		this.inQueue = inQueue;
	}

	public List<JobVo> getDownstreamProjects() {
		return downstreamProjects;
	}

	public void setDownstreamProjects(List<JobVo> downstreamProjects) {
		this.downstreamProjects = downstreamProjects;
	}

	public List<JobVo> getUpstreamProjects() {
		return upstreamProjects;
	}

	public void setUpstreamProjects(List<JobVo> upstreamProjects) {
		this.upstreamProjects = upstreamProjects;
	}

	@Override
	public String toString() {
		return "JobWithDetailsVo [description=" + description + ", displayName=" + displayName + ", buildable="
				+ buildable + ", builds=" + builds + ", firstBuild=" + firstBuild + ", lastBuild=" + lastBuild
				+ ", lastSuccessfulBuild=" + lastSuccessfulBuild + ", nextBuildNumber=" + nextBuildNumber + ", inQueue="
				+ inQueue + ", downstreamProjects=" + downstreamProjects + ", upstreamProjects=" + upstreamProjects
				+ "]";
	}

    
}
