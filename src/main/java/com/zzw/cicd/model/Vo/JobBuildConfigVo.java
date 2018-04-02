package com.zzw.cicd.model.Vo;
public class JobBuildConfigVo {

	private Boolean building;

	private String displayName;

	private String id;

	private String timestamp;

	private Integer number;

	public Boolean getBuilding() {
		return building;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getId() {
		return id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public Integer getNumber() {
		return number;
	}

	public void setBuilding(Boolean building) {
		this.building = building;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}
