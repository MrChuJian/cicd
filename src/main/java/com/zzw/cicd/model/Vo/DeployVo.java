package com.zzw.cicd.model.Vo;
public class DeployVo {
	private Integer id;//
	private String name;// 部署名 serviceName
	private String language;// 部署对应的编程语言
	private Integer replicas = 1;// 副本数
	private String namespace;// 租户code,对应namespace
	private String memory = "215";// 内存
	private String cpu = "0.25";// cpu核心数
	private String ingressDomain;// 代理域名
	private String ingressIp;// 代理主机Ip
	private ContainerVo container;// 容器

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

	public Integer getReplicas() {
		return replicas;
	}

	public void setReplicas(Integer replicas) {
		this.replicas = replicas;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getIngressDomain() {
		return ingressDomain;
	}

	public String getIngressIp() {
		return ingressIp;
	}

	public void setIngressIp(String ingressIp) {
		this.ingressIp = ingressIp;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setIngressDomain(String ingressDomain) {
		this.ingressDomain = ingressDomain;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ContainerVo getContainer() {
		return container;
	}

	public void setContainer(ContainerVo container) {
		this.container = container;
	}

}