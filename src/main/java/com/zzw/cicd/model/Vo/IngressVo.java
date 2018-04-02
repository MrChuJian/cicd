package com.zzw.cicd.model.Vo;

import java.util.List;

public class IngressVo {

	/*
	 * ingress的描述
	 */

	private String name;// 代理名称
	private String nodeIp;// 代理主机Ip
	private String domain;// 代理域名
	private List<PortVo> ports;// 代理端口
	private String proxyName;// 代理服务名称
	private String namespace;// 命名空间

	public void setName(String name) {
		this.name = name;
	}

	public List<PortVo> getPorts() {
		return ports;
	}

	public void setPorts(List<PortVo> ports) {
		this.ports = ports;
	}

	public String getNodeIp() {
		return nodeIp;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public String getName() {
		return name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}