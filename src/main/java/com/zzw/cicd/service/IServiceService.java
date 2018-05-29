package com.zzw.cicd.service;

import com.zzw.cicd.model.Vo.ServiceVo;

import io.fabric8.kubernetes.api.model.Service;

public interface IServiceService {

    boolean createService(ServiceVo serviceVo);
    boolean updateService(ServiceVo serviceVo);
    boolean deleteServiceByNameAndNamespace(String serviceName,String namespace); 
	Service getServiceByNameAndNamespace(String serviceName, String namespace);
}
