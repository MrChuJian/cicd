package com.zzw.cicd.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zzw.cicd.model.Vo.IngressVo;
import com.zzw.cicd.model.Vo.ServiceVo;

import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;

public class Deploy {

	/*
	 * 对部署的抽象描述
	 */
	private String deployId;// 部署Id
	private String name;// 部署名称serviceName
	private String language;// 部署语言
	private String code;// 部署编码
	private String imageUrl;// 镜像地址
	private String cpu;// cpu的限制量
	private String memory;// 内存的限制
	private String namespace = "default";// 租户code对应命名空间
	private Integer replicas = 1;// 副本数：默认为1
	private Date createDate;//
	private IngressVo ingressVo;// 代理对象
	private List<ContainerPort> ports;// 对应端口暴露
	private Map<String, String> labels;// replicas的标签
	private Map<String, String> envs;
	private List<Volume> volumes;
	private List<VolumeMount> volumeMounts;
	private ServiceVo serviceVo;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String tenantCode) {
		this.namespace = tenantCode;
	}

	public Integer getReplicas() {
		return replicas;
	}

	public void setReplicas(Integer replicas) {
		this.replicas = replicas;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<ContainerPort> getPorts() {
		return ports;
	}

	public void setPorts(List<ContainerPort> ports) {
		this.ports = ports;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public Map<String, String> getEnvs() {
		return envs;
	}

	public void setEnvs(Map<String, String> envs) {
		this.envs = envs;
	}

	public List<Volume> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<Volume> volumes) {
		this.volumes = volumes;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public IngressVo getIngressVo() {
		return ingressVo;
	}

	public void setIngressVo(IngressVo ingressVo) {
		this.ingressVo = ingressVo;
	}

	public ServiceVo getServiceVo() {
		return serviceVo;
	}

	public void setServiceVo(ServiceVo serviceVo) {
		this.serviceVo = serviceVo;
	}

	public List<VolumeMount> getVolumeMounts() {
		return volumeMounts;
	}

	public void setVolumeMounts(List<VolumeMount> volumeMounts) {
		this.volumeMounts = volumeMounts;
	}

}
