package com.zzw.cicd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.zzw.cicd.model.Vo.PortVo;
import com.zzw.cicd.model.Vo.ServiceVo;
import com.zzw.cicd.service.IServiceService;
import com.zzw.cicd.util.KubernetesUtil;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.ServiceSpec;

@Service
public class ServiceServiceImpl implements IServiceService {
	private static Log logger = LogFactory.getLog(ServiceServiceImpl.class);

	io.fabric8.kubernetes.api.model.Service transformVoToService(ServiceVo serviceVo) {
		ObjectMeta serviceMeta = new ObjectMeta();
		serviceMeta.setName(serviceVo.getName());
		serviceMeta.setNamespace(serviceVo.getNamespace());
		io.fabric8.kubernetes.api.model.Service service = new io.fabric8.kubernetes.api.model.Service();
		service.setMetadata(serviceMeta);
		ServiceSpec serviceSpec = new ServiceSpec();
		serviceSpec.setSelector(serviceVo.getSelector());
		List<PortVo> portVos = serviceVo.getPorts();
		if(portVos != null) {
			List<ServicePort> ports = new ArrayList<>();
			for (PortVo portVo : portVos) {
				ServicePort servicePort = new ServicePort();
				servicePort.setName(portVo.getName());
				servicePort.setProtocol(portVo.getProtocol());
				servicePort.setPort(portVo.getPort());
				servicePort.setTargetPort(new IntOrString(portVo.getPort()));
				ports.add(servicePort);
			}
			serviceSpec.setPorts(ports);
		}
		
		service.setMetadata(serviceMeta);
		service.setSpec(serviceSpec);
		return service;
	}

	@Override
	public boolean createService(ServiceVo serviceVo) {
		io.fabric8.kubernetes.api.model.Service transformVoToService = transformVoToService(serviceVo);
		try {
			KubernetesUtil.createOrUpdateService(transformVoToService, "");
		} catch (Exception e) {
			logger.info("创建服务失败");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteServiceByNameAndNamespace(String serviceName, String namespace) {
		try {
			io.fabric8.kubernetes.api.model.Service service = new io.fabric8.kubernetes.api.model.Service();
			ObjectMeta metadata = new ObjectMeta();
			metadata.setName(serviceName);
			metadata.setNamespace(namespace);
			service.setMetadata(metadata);
			KubernetesUtil.deleteService(service);
			return true;
		} catch (Exception e) {
			logger.info("删除服务失败" + e);
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateService(ServiceVo serviceVo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public io.fabric8.kubernetes.api.model.Service getServiceByNameAndNamespace(String serviceName, String namespace) {
		ServiceVo serviceVo = new ServiceVo();
		serviceVo.setName(serviceName + "-service");
		serviceVo.setNamespace(namespace);
		io.fabric8.kubernetes.api.model.Service service = transformVoToService(serviceVo);
		try {
			return KubernetesUtil.getService(service);
		} catch (Exception e) {
			logger.info("获取服务失败" + e.toString());
			e.printStackTrace();
			return null;
		}
	}
}