package com.zzw.cicd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zzw.cicd.model.Vo.JobStatusVo;
import com.zzw.cicd.model.Vo.LastBuildVo;

@SuppressWarnings("serial")
public class JenkinsJob implements Serializable {

	private String name;

	private String url;
	private List<JobStatusVo> builds = new ArrayList<JobStatusVo>();
	private LastBuildVo lastBuild = new LastBuildVo();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<JobStatusVo> getBuilds() {
		return builds;
	}

	public void setBuilds(List<JobStatusVo> builds) {
		this.builds = builds;
	}

	public LastBuildVo getLastBuild() {
		return lastBuild;
	}

	public void setLastBuild(LastBuildVo lastBuild) {
		this.lastBuild = lastBuild;
	}

}