package com.zzw.cicd.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.fabric8.kubernetes.api.Controller;
import io.fabric8.kubernetes.api.KubernetesHelper;
import io.fabric8.kubernetes.api.model.DoneableNamespace;
import io.fabric8.kubernetes.api.model.DoneablePod;
import io.fabric8.kubernetes.api.model.DoneableService;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DoneableDeployment;
import io.fabric8.kubernetes.api.model.extensions.DoneableIngress;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.PodResource;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.ScalableResource;

public class KubernetesUtil {
  
	private static Log logger = LogFactory.getLog(KubernetesUtil.class);
  	private static KubernetesClient client =null;
  	private static Config config=null;
  	private static Controller kubernetesController=null;
  	private static KubernetesHelper kubernetesHelper=null;
  	public static KubernetesClient getClient() {
		if(client==null){
			client = new DefaultKubernetesClient(getConfig());
		}
        return client;
	}
  	public static Config getConfig(){
  		if(config==null){
  			 config = new ConfigBuilder().withMasterUrl("https://192.168.52.130:6443").withTrustCerts(true).withUsername("admin").withPassword("123456").build();
  		}	
  		return config;
  	}
  	/**
  	 * 此controller为kubernetes操作类
  	 * @return
  	 */
  	
  	public static Controller getController(String namespace){
  		kubernetesController=new Controller(getClient()); 		
  		kubernetesController.setNamespace(namespace);
  		return kubernetesController;
  	}
  	
  	/**
  	 * 
  	 * @param sourceName 资源名
  	 * @param pod   对应的pod对象
  	 * @throws Exception
  	 */
  	public static void createOrUpdatePod(Pod pod,String sourceName) throws Exception{  	 
		getController(pod.getMetadata().getNamespace()).applyPod(pod, sourceName);
		
	}
  	
  	/**
  	 * 
  	 * @param pod pod里面包含name和namespace即可
  	 * @throws Exception
  	 */
	public static void deletePod(Pod pod) throws Exception{  	 
		PodResource<Pod, DoneablePod> withName = getClient().pods().inNamespace(pod.getMetadata().getNamespace()).withName(pod.getMetadata().getName());
		Pod oldPod = withName.get();
		if(oldPod!=null){
			withName.delete();
		}	
	}
  	
	/**
	 * 传入参数podName 和namespace
	 * @param pod
	 * @return
	 */
  	public static Pod getPod(Pod pod) {
		return getClient().pods().inNamespace(pod.getMetadata().getNamespace()).withName(pod.getMetadata().getName()).get();
  	}
  	
  	/**
  	 * 
  	 * {@link Service} Service的增删改查
  	 * 
  	 */
  	
  	/**
  	 * 
  	 * @param service
  	 * @param sourceName
  	 * @throws Exception
  	 */
  	public static void createOrUpdateService(Service service,String sourceName) throws Exception {
  		getController(service.getMetadata().getNamespace()).applyService(service, sourceName);
	}
  	
  	/**
  	 * 
  	 * @param service 传入name和namespace即可
  	 * @return
  	 */
  	public static Service getService(Service service) {
		Resource<Service, DoneableService> withName = getClient().services().inNamespace(service.getMetadata().getNamespace()).withName(service.getMetadata().getName());
		return withName.get();
		
	}
  	
  	public static void deleteService(Service service) {
		 Resource<Service, DoneableService> withName = getClient().services().inNamespace(service.getMetadata().getNamespace()).withName(service.getMetadata().getName());
		 Service oldService = withName.get();
		 if(null!=oldService){
			 withName.delete();
		 }
		
	}
  	
  	/**
  	 * deployment的增删改查
  	 * @throws Exception 
  	 */
  	public static void createOrUpdateDeployment(Deployment deployment,String sourceName) throws Exception {
		getController(deployment.getMetadata().getNamespace()).apply(deployment, sourceName);
	}
  	
  	public static Deployment getDeployment(Deployment deployment) {
		ScalableResource<Deployment, DoneableDeployment> withName = getClient().extensions().deployments().inNamespace(deployment.getMetadata().getNamespace()).withName(deployment.getMetadata().getName());
         return withName.get();
  	}
  	
	public static void deleteDeployment(Deployment deployment ) throws Exception {
		ScalableResource<Deployment, DoneableDeployment> withName = getClient().extensions().deployments().inNamespace(deployment.getMetadata().getNamespace()).withName(deployment.getMetadata().getName());
         Deployment oldDeployment = withName.get();
         if(null!=oldDeployment){
        	 withName.delete();
         }
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	
	public static void createOrUpdateIngress(Ingress ingress,String sourceName) throws Exception {
		getController(ingress.getMetadata().getNamespace()).apply(ingress, sourceName);
	}
	
	public static Ingress getIngress(Ingress ingress){
		Resource<Ingress, DoneableIngress> withName = getClient().extensions().ingresses().inNamespace(ingress.getMetadata().getNamespace()).withName(ingress.getMetadata().getName());
	    return withName.get();
	}
	
	public static void deleteIngress(Ingress ingress){
		Resource<Ingress, DoneableIngress> withName = getClient().extensions().ingresses().inNamespace(ingress.getMetadata().getNamespace()).withName(ingress.getMetadata().getName());
	     Ingress oldIngress = withName.get();
	     if(null!=oldIngress){
	    	  withName.delete();
	     }
	}
	
	/**
	 * 
	 */
	
	public static void createOrUpdateNamespace(Namespace namespace) {
		getController(namespace.getMetadata().getNamespace()).applyNamespace(namespace);
		
	}
	
	public static Namespace getNamespace(Namespace namespace) {	
		Resource<Namespace, DoneableNamespace> withName = getClient().namespaces().withName(namespace.getMetadata().getName());
	   return withName.get();
	}
	
	public static void deleteNamespace(Namespace namespace) {
		Resource<Namespace, DoneableNamespace> withName = getClient().namespaces().withName(namespace.getMetadata().getName());
		    Namespace oldNamespace = withName.get();
		    if(null!=oldNamespace){
		    	withName.delete();
		    }
	}
}