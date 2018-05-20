package com.zzw.cicd.model.Vo;

import java.util.List;

public class ContainerVo {

	private String imageUrl;
	private List<PortVo> ports;
	private List<VolumeVo> volumes;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<PortVo> getPorts() {
		return ports;
	}

	public void setPorts(List<PortVo> ports) {
		this.ports = ports;
	}

	public List<VolumeVo> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<VolumeVo> volumes) {
		this.volumes = volumes;
	}

	public String getContainerPort() {
		StringBuffer containerPort = new StringBuffer();
		for (PortVo portVo : ports) {
			containerPort.append(portVo.getPort().toString() + "/" + portVo.getProtocol().toString() + ",");
		}
		return containerPort.substring(0, containerPort.length() - 1);
	}
	
	public String getProtocol() {
		StringBuffer containerPort = new StringBuffer();
		for (PortVo portVo : ports) {
			containerPort.append(portVo.getPort().toString() + "/" + portVo.getProtocol().toString() + ",");
		}
		return containerPort.substring(0, containerPort.length() - 1);
	}
}