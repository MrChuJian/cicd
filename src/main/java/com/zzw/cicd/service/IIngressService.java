package com.zzw.cicd.service;

import com.zzw.cicd.model.Vo.IngressVo;

import io.fabric8.kubernetes.api.model.extensions.Ingress;

public interface IIngressService {

    boolean createIngress(IngressVo ingressVo);
    boolean updateIngress(IngressVo ingressVo);
    boolean deleteIngressByNameAndNamespace(String  ingressName,String namespace);
    Ingress getIngressByName(String IngressName);
    Ingress getIngressById(String ingressId);
}
