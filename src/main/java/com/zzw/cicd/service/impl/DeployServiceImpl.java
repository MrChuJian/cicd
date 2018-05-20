package com.zzw.cicd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzw.cicd.dao.DeployMapper;
import com.zzw.cicd.model.Deploy;
import com.zzw.cicd.model.Vo.DeployVo;
import com.zzw.cicd.service.IDeployService;
import com.zzw.cicd.util.KubernetesUtil;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentSpec;
import io.fabric8.kubernetes.api.model.extensions.DeploymentStatus;

@Service
public class DeployServiceImpl implements IDeployService {
	private static Log logger = LogFactory.getLog(DeployServiceImpl.class);
	@Autowired
	private DeployMapper deployMapper;

	@Override
	public boolean addDeployment(Deploy deploy) {
		Deployment deployment = transforDeployToDeployment(deploy);
		try {
			KubernetesUtil.createOrUpdateDeployment(deployment, "");
			return true;
		} catch (Exception e) {
			logger.info("创建部署失败" + e.toString());
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean updateDeployment(Deploy deploy) {
		Deployment deployment = transforDeployToDeployment(deploy);
		try {
			KubernetesUtil.createOrUpdateDeployment(deployment, "");
			return true;
		} catch (Exception e) {
			logger.info("更新部署失败" + e.toString());
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean deleteDeployment(Deploy deploy) {
		Deployment deployment = transforDeployToDeployment(deploy);
		try {
			KubernetesUtil.deleteDeployment(deployment);
			return true;
		} catch (Exception e) {
			logger.info("删除部署失败" + e.toString());
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Deployment getDeploymentByName(String deployName, String namespace) {
		Deploy deploy = new Deploy();
		deploy.setName(deployName);
		deploy.setNamespace(namespace);
		Deployment deployment = transforDeployToDeployment(deploy);
		try {
			return KubernetesUtil.getDeployment(deployment);
		} catch (Exception e) {
			logger.info("获取部署失败" + e.toString());
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Deployment getDeploymentById(String deployId) {
		Deploy deploy = new Deploy();
		deploy.setDeployId(deployId);
		Deployment deployment = transforDeployToDeployment(deploy);
		try {
			return KubernetesUtil.getDeployment(deployment);
		} catch (Exception e) {
			logger.info("获取部署失败" + e.toString());
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public boolean addDeploy(DeployVo deployVo) {
		try {
			deployMapper.insert(deployVo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
			return false;
		}
	}

	@Override
	public boolean updateDeploy(DeployVo deployVo) {
		try {
			deployMapper.update(deployVo);
			return true;
		} catch (Exception e) {
			logger.info(e);
		}
		return false;
	}

	@Override
	public DeployVo getDeployByNameAndNamespace(String name, String namespace) {
		return deployMapper.getByNameAndNamespace(name, namespace);
	}

	@Override
	public List<DeployVo> getDeployByNamespace(String namespace) {
		return deployMapper.getByNamespace(namespace);
	}

	@Override
	public boolean deleteDeployByNameAndNamespace(String name, String namespace) {
		try {
			deployMapper.deleteByNameAndNamespace(name, namespace);
			return true;
		} catch (Exception e) {
			logger.info(e);
		}
		return false;
	}

	@Override
	public boolean deleteDeployById(String id) {
		try {
			deployMapper.deleteById(id);
			return true;
		} catch (Exception e) {
			logger.info(e);
		}
		return false;
	}

	/**
	 * 转化deploy 到deployment
	 * 
	 * @param deploy
	 * @return
	 */
	Deployment transforDeployToDeployment(Deploy deploy) {
		ObjectMeta deployMeta = new ObjectMeta();
		deployMeta.setNamespace(deploy.getNamespace());
		DeploymentSpec deploymentSpec = new DeploymentSpec();
		deployMeta.setName(deploy.getName());

		deploymentSpec.setReplicas(deploy.getReplicas());
		PodTemplateSpec podTemplateSpec = new PodTemplateSpec();
		ObjectMeta podMetadata = new ObjectMeta();
		podMetadata.setLabels(deploy.getLabels());
		podTemplateSpec.setMetadata(podMetadata);
		PodSpec podSpec = new PodSpec();
		List<Container> containers = new ArrayList<>();
		Container container = new Container();
		container.setImage(deploy.getImageUrl());
		container.setPorts(deploy.getPorts());
		List<EnvVar> envVars = new ArrayList<>();
		if (envVars.size() > 0) {
			for (String key : deploy.getEnvs().keySet()) {
				EnvVar env = new EnvVar();
				env.setName(key);
				env.setValue(deploy.getEnvs().get(key));
				envVars.add(env);
			}
		}
		container.setVolumeMounts(deploy.getVolumeMounts());
		container.setEnv(envVars);
		container.setName(deploy.getCode());
		containers.add(container);
		podSpec.setContainers(containers);
		podSpec.setVolumes(deploy.getVolumes());
		podTemplateSpec.setSpec(podSpec);
		deploymentSpec.setTemplate(podTemplateSpec);
		DeploymentStatus status = new DeploymentStatus();
		Deployment deployment = new Deployment("extensions/v1beta1", "Deployment", deployMeta, deploymentSpec, status);
		return deployment;
	}

}
