package com.zzw.cicd.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
import com.zzw.cicd.model.Deploy;
import com.zzw.cicd.model.Entity;
import com.zzw.cicd.model.Vo.DeployVo;
import com.zzw.cicd.model.Vo.IngressVo;
import com.zzw.cicd.model.Vo.PortVo;
import com.zzw.cicd.model.Vo.ServiceVo;
import com.zzw.cicd.model.Vo.VolumeVo;
import com.zzw.cicd.service.IDeployService;
import com.zzw.cicd.service.IIngressService;
import com.zzw.cicd.service.IServiceService;
import com.zzw.cicd.util.CacheUtil;
import com.zzw.cicd.util.DockerRegistryUtil;

import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.HostPathVolumeSource;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "部署控制器", tags = { "部署控制器" })
@RestController
@RequestMapping("/api/v1/deploy")
public class DeployController {

	@Autowired
	private IDeployService deployService;
	@Autowired
	private IIngressService ingressService;
	@Autowired
	private IServiceService serviceService;

	/**
	 * 创建修改简易部署
	 * 
	 * @param deploy
	 * @return
	 */
	@ApiOperation(value = "创建修改部署", notes = "创建修改部署")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Entity<Boolean>> createDeployment(@RequestBody DeployVo deployVo) {
		// 创建部署，服务，代理
		Deploy deploy = new Deploy();

		IngressVo ingressVo = new IngressVo();
		ServiceVo serviceVo = new ServiceVo();

		deploy.setName(deployVo.getName());
		deploy.setCode(deployVo.getName() + "-code");
		deploy.setCpu(deployVo.getCpu());
		deploy.setNamespace(deployVo.getNamespace());
		deploy.setMemory(deployVo.getMemory());
		deploy.setLanguage(deployVo.getLanguage());
		deploy.setImageUrl(deployVo.getContainer().getImageUrl());
		deploy.setReplicas(deployVo.getReplicas());
		List<Volume> volumes = new ArrayList<>();
		List<VolumeMount> volumeMounts = new ArrayList<>();
		List<VolumeVo> volumeVos = deployVo.getContainer().getVolumes();
		for (VolumeVo volumeVo : volumeVos) {
			Volume volume = new Volume();
			volume.setName(volumeVo.getName());
			HostPathVolumeSource hostPath = new HostPathVolumeSource();
			hostPath.setPath(volumeVo.getTargetPath());
			volume.setHostPath(hostPath);
			volumes.add(volume);
			VolumeMount volumeMount = new VolumeMount();
			volumeMount.setMountPath(volumeVo.getPath());
			volumeMount.setName(volumeVo.getName());
			volumeMounts.add(volumeMount);
		}
		deploy.setVolumeMounts(volumeMounts);
		deploy.setVolumes(volumes);
		List<ContainerPort> ports = new ArrayList<>();
		List<PortVo> portVos = deployVo.getContainer().getPorts();
		for (PortVo portVo : portVos) {
			ContainerPort containerPort = new ContainerPort();
			containerPort.setContainerPort(portVo.getPort());
			containerPort.setProtocol(portVo.getProtocol());
			ports.add(containerPort);
		}
		deploy.setPorts(ports);

		ingressVo.setName(deployVo.getName() + "-ingress");
		ingressVo.setProxyName(deployVo.getName() + "-service");
		ingressVo.setDomain(deployVo.getIngressDomain());
		ingressVo.setNodeIp(deployVo.getIngressIp());
		ingressVo.setPorts(deployVo.getContainer().getPorts());
		ingressVo.setNamespace(deployVo.getNamespace());
		deploy.setIngressVo(ingressVo);

		serviceVo.setName(deployVo.getName() + "-service");
		serviceVo.setPorts(deployVo.getContainer().getPorts());
		serviceVo.setNamespace(deployVo.getNamespace());
		Map<String, String> labels = new HashMap<>();
		labels.put("app", deployVo.getName() + "-code");
		deploy.setLabels(labels);
		serviceVo.setSelector(labels);
		deploy.setServiceVo(serviceVo);

		if (!deployService.addDeployment(deploy)) {
			String msg = "";
			if (null == deployVo.getId() || "".equals(deployVo.getId())) {
				msg = "新增部署失败！";
			} else {
				msg = "修改部署失败！";
			}
			return Entity.failure(1, msg);
		} else {
//			 存到数据库中
			 if (null == deployVo.getId() || "".equals(deployVo.getId())) {
			 deployService.addDeploy(deployVo);
			 } else {
			 deployService.updateDeploy(deployVo);
			 }
		}
		;
		if (!ingressService.createIngress(deploy.getIngressVo())) {
			String msg = "";
			if (null == deployVo.getId() || "".equals(deployVo.getId())) {
				msg = "创建代理出错！";
			} else {
				msg = "修改代理出错！";
			}
			return Entity.failure(1, msg);
		}
		if (!serviceService.createService(serviceVo)) {
			String msg = "";
			if (null == deployVo.getId() || "".equals(deployVo.getId())) {
				msg = "创建服务出错出错！";
			} else {
				msg = "修改服务出错出错！";
			}
			return Entity.failure(1, msg);
		}
		return Entity.success(true);
	}

	@ApiOperation(value = "根据名字查询部署", notes = "根据名字查询部署")
	@RequestMapping(value = "/name/{name}/deploy", method = RequestMethod.GET)
	public ResponseEntity<Entity<DeployVo>> getDeployByName(@PathVariable String name,
			@RequestParam(required = false) String namespace) {
		// 创建部署，服务，代理
		if ("".equals(namespace) || null == namespace) {
			namespace = "default";
		}
		DeployVo deployVo = deployService.getDeployByNameAndNamespace(name, namespace);

		return Entity.success(deployVo);
	}

	@ApiOperation(value = "根据命名空间获得部署", notes = "根据命名空间获得部署")
	@RequestMapping(value = "/namespace/{namespace}/deploy", method = RequestMethod.GET)
	public ResponseEntity<Entity<List>> getDeployListByNamespace(@PathVariable String namespace) {
		// 创建部署，服务，代理
		if ("".equals(namespace) || null == namespace) {
			namespace = "default";
		}
		List<DeployVo> deployVos = deployService.getDeployByNamespace(namespace);
		return Entity.success(deployVos);
	}

	@ApiOperation(value = "获得部署详情", notes = "获得部署详情")
	@RequestMapping(value = "/name/{name}/deployment", method = RequestMethod.GET)
	public ResponseEntity<Entity<Deployment>> getDeploymentByName(@PathVariable String name,
			@RequestParam(required = false) String namespace) {
		// 创建部署，服务，代理
		if ("".equals(namespace) || null == namespace) {
			namespace = "default";
		}
		Deployment deployment = deployService.getDeploymentByName(name, namespace);

		return Entity.success(deployment);
	}
	
	
	@ApiOperation(value = "获得服务详情", notes = "获得服务详情")
	@RequestMapping(value = "/name/{name}/service", method = RequestMethod.GET)
	public ResponseEntity<Entity<Service>> getServiceByName(@PathVariable String name,
			@RequestParam(required = false) String namespace) {
		if ("".equals(namespace) || null == namespace) {
			namespace = "default";
		}
		Service service = serviceService.getServiceByNameAndNamespace(name, namespace);

		return Entity.success(service);
	}
	
	@ApiOperation(value = "获得Ingress详情", notes = "获得Ingress详情")
	@RequestMapping(value = "/name/{name}/ingress", method = RequestMethod.GET)
	public ResponseEntity<Entity<Ingress>> getIngressByName(@PathVariable String name,
			@RequestParam(required = false) String namespace) {
		if ("".equals(namespace) || null == namespace) {
			namespace = "default";
		}
		Ingress ingress = ingressService.getIngressByNameAndNamespace(name, namespace);

		return Entity.success(ingress);
	}
	
	@ApiOperation(value = "获得镜像列表", notes = "获得镜像列表")
	@RequestMapping(value = "/name/{name}/ImagesTarget", method = RequestMethod.GET)
	public ResponseEntity<Entity<List<String>>> getImagesTarget(@PathVariable String name) {
		List<String> list = DockerRegistryUtil.getImagesTarget(name);
		if(list == null || list.size() <= 0) {
			Entity.failure(404, "没有该镜像");
		}
		Collections.sort(list);
		return Entity.success(list);
	}

	@ApiOperation(value = "删除部署", notes = "删除部署")
	@RequestMapping(value = "/name/{name}", method = RequestMethod.DELETE)
	public ResponseEntity<Entity<Boolean>> deleteDeploymentByName(@PathVariable String name,
			@RequestParam(required = false) String namespace) {
		// 创建部署，服务，代理
		if ("".equals(namespace) || null == namespace) {
			namespace = "default";
		}
		Deploy deploy = new Deploy();
		deploy.setName(name);
		deploy.setNamespace(namespace);
		String msg = "";
		boolean deleteResource = true;
		// 删除k8s上面的资源：deployment service ingress,删除失败后下面删除不在知行
		if (!deployService.deleteDeployment(deploy)) {
			deleteResource = false;
			msg = "部署资源删除失败";
		}
		;
		if (deleteResource && !ingressService.deleteIngressByNameAndNamespace(name + "-ingress", namespace)) {
			deleteResource = false;
			msg = "ingress删除失败";
		}
		;
		if (deleteResource && !serviceService.deleteServiceByNameAndNamespace(name + "-service", namespace)) {
			deleteResource = false;
			msg = "service 删除失败";
		}
		;
		// 资源删除成功后删除数据库中的记录
		if (deleteResource) {
			boolean result = deployService.deleteDeployByNameAndNamespace(name, namespace);
			if (result) {
				return Entity.success(true);
			} else {
				return Entity.failure(1, msg);
			}
		} else {
			return Entity.failure(1, "删除部署资源失败");
		}
	}
}
