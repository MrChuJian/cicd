package com.zzw.cicd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.zzw.cicd.model.Vo.IngressVo;
import com.zzw.cicd.model.Vo.PortVo;
import com.zzw.cicd.service.IIngressService;
import com.zzw.cicd.util.KubernetesUtil;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.extensions.HTTPIngressPath;
import io.fabric8.kubernetes.api.model.extensions.HTTPIngressRuleValue;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.fabric8.kubernetes.api.model.extensions.IngressBackend;
import io.fabric8.kubernetes.api.model.extensions.IngressRule;
import io.fabric8.kubernetes.api.model.extensions.IngressSpec;

@Service
public class IngressServiceImpl implements IIngressService {
	private static Log logger = LogFactory.getLog(IngressServiceImpl.class);

	@Override
	public boolean deleteIngressByNameAndNamespace(String ingressName, String namespace) {
		try {
			Ingress ingress = new Ingress();
			ObjectMeta metadata = new ObjectMeta();
			metadata.setName(ingressName);
			metadata.setNamespace(namespace);
			ingress.setMetadata(metadata);
			KubernetesUtil.deleteIngress(ingress);
			return true;
		} catch (Exception e) {
			logger.info("删除服务失败" + e.toString());
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Ingress getIngressByNameAndNamespace(String IngressName, String namespace) {
		IngressVo ingressVo = new IngressVo();
		ingressVo.setName(IngressName + "-ingress");
		ingressVo.setNamespace(namespace);
		Ingress ingress = transformIngressToVo(ingressVo);
		try {
			return KubernetesUtil.getIngress(ingress);
		} catch (Exception e) {
			logger.info("获取服务失败" + e.toString());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Ingress getIngressById(String ingressId) {
		return null;
	}

	@Override
	public boolean createIngress(IngressVo ingressVo) {
		Ingress ingress = transformIngressToVo(ingressVo);
		try {
			KubernetesUtil.createOrUpdateIngress(ingress, "");
			return true;
		} catch (Exception e) {
			logger.info(e.toString());
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateIngress(IngressVo ingressVo) {
		Ingress ingress = transformIngressToVo(ingressVo);
		try {
			KubernetesUtil.createOrUpdateIngress(ingress, "");
			return true;
		} catch (Exception e) {
			logger.info(e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 转化Ingress
	 * 
	 * @param ingressVo
	 * @return
	 */
	Ingress transformIngressToVo(IngressVo ingressVo) {
		ObjectMeta ingressMeta = new ObjectMeta();
		ingressMeta.setName(ingressVo.getName());
		ingressMeta.setNamespace(ingressVo.getNamespace());
		IngressSpec ingressSpec = new IngressSpec();
		List<IngressRule> rules = new ArrayList<>();
		HTTPIngressRuleValue ruleValue = new HTTPIngressRuleValue();
		List<PortVo> ports = ingressVo.getPorts();
		if (ports != null) {
			List<HTTPIngressPath> paths = new ArrayList<>();
			for (PortVo portVo : ports) {
				IntOrString port = new IntOrString(portVo.getPort());
				IngressBackend backend = new IngressBackend(ingressVo.getProxyName(), port);
				HTTPIngressPath httpIngressPath = new HTTPIngressPath(backend, portVo.getPath());
				paths.add(httpIngressPath);
			}
			ruleValue.setPaths(paths);
		}
		IngressRule rule = new IngressRule(ingressVo.getDomain(), ruleValue);
		rules.add(rule);
		ingressSpec.setRules(rules);
		Ingress ingress = new Ingress("extensions/v1beta1", "Ingress", ingressMeta, ingressSpec, null);
		return ingress;
	}

}