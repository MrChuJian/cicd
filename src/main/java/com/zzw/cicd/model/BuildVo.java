package com.zzw.cicd.model;

public class BuildVo {

	private int number;
    private int queueId;
    private String url;
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getQueueId() {
		return queueId;
	}
	public void setQueueId(int queueId) {
		this.queueId = queueId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "BuildVo [number=" + number + ", queueId=" + queueId + ", url=" + url + "]";
	}
    
}
