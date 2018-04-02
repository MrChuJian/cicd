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

}