package com.zzw.cicd.model.Vo;

import java.util.List;
import java.util.Map;

public class ServiceVo {
	private String id;// 服务Id
	private String name;// 服务名
	private Map<String, String> selector;
	private String namespace;// 命名空间
	private List<PortVo> ports;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getSelector() {
		return selector;
	}

	public void setSelector(Map<String, String> selector) {
		this.selector = selector;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public List<PortVo> getPorts() {
		return ports;
	}

	public void setPorts(List<PortVo> ports) {
		this.ports = ports;
	}

}