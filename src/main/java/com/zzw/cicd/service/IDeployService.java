package com.zzw.cicd.service;

import java.util.List;

import com.zzw.cicd.model.Deploy;
import com.zzw.cicd.model.Vo.DeployVo;

import io.fabric8.kubernetes.api.model.extensions.Deployment;

public interface IDeployService {

    boolean addDeployment(Deploy deploy);
    boolean updateDeployment(Deploy deploy);
    boolean deleteDeployment(Deploy deploy);
    Deployment getDeploymentByName(String deployName,String namespace);
    Deployment getDeploymentById(String deployId);
    boolean addDeploy(DeployVo deployVo);
    boolean updateDeploy(DeployVo deployVo);
    DeployVo getDeployByNameAndNamespace(String name,String namespace);
    List<DeployVo> getDeployByNamespace(String namespace);
    boolean  deleteDeployByNameAndNamespace(String name,String namespace);
    boolean deleteDeployById(String id);
    
}