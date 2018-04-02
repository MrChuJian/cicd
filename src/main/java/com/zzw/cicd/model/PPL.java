package com.zzw.cicd.model;

import java.io.Serializable;
import java.util.List;

import com.offbytwo.jenkins.model.Job;

@SuppressWarnings("serial")
public class PPL implements Serializable {

	private String pipeLineId;
	private String userId;
	private String userName;
	private String pipeLineName;

	private String pipeLineType;

	private String pipeLineFtl;

	private String jobArray;

	private List<Job> jobs;

	private List<PPL> ppls;

	private String outPath;

	private String logPath;

	private String buildId;

	private String version;

	private String platform;

	private String language;
	private String env;

	private String param;

	public String getPipeLineId() {
		return pipeLineId;
	}

	public void setPipeLineId(String pipeLineId) {
		this.pipeLineId = pipeLineId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPipeLineName() {
		return pipeLineName;
	}

	public void setPipeLineName(String pipeLineName) {
		this.pipeLineName = pipeLineName;
	}

	public String getPipeLineType() {
		return pipeLineType;
	}

	public void setPipeLineType(String pipeLineType) {
		this.pipeLineType = pipeLineType;
	}

	public String getPipeLineFtl() {
		return pipeLineFtl;
	}

	public void setPipeLineFtl(String pipeLineFtl) {
		this.pipeLineFtl = pipeLineFtl;
	}

	public String getJobArray() {
		return jobArray;
	}

	public void setJobArray(String jobArray) {
		this.jobArray = jobArray;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public List<PPL> getPpls() {
		return ppls;
	}

	public void setPpls(List<PPL> ppls) {
		this.ppls = ppls;
	}

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	private String codeInfo;

	public String getCodeInfo() {
		return codeInfo;
	}

	public void setCodeInfo(String codeInfo) {
		this.codeInfo = codeInfo;
	}

	private String scmInfo;

	public String getScmInfo() {
		return scmInfo;
	}

	public void setScmInfo(String scmInfo) {
		this.scmInfo = scmInfo;
	}

	private String envConfig;

	public String getEnvConfig() {
		return envConfig;
	}

	public void setEnvConfig(String envConfig) {
		this.envConfig = envConfig;
	}

	private String argConfig;

	public String getArgConfig() {
		return argConfig;
	}

	public void setArgConfig(String argConfig) {
		this.argConfig = argConfig;
	}
}
