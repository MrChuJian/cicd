package com.zzw.cicd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.zzw.cicd.model.Vo.DeployVo;

@Repository
public interface DeployMapper extends BaseMapper{
   
	 @Insert("INSERT INTO deploy (id, NAME, LANGUAGE, namespace, image_url , memory, cpu, replicas, ingress_domain, ingress_ip, metadata) "
	 		+ "VALUES (#{id},#{name}, #{language}, #{namespace}, #{container.imageUrl} ,#{memory},#{cpu},#{replicas}, #{ingressDomain}, #{ingressIp}, #{metadata})")
	 int insert(DeployVo deploy);
	 
	 @Update("UPDATE deploy SET NAME = #{name}, LANGUAGE = #{language}, namespace = #{namespace}, image_url = #{container.imageUrl}, memory = #{memory}, cpu = #{cpu},"
	 		+ " replicas = #{replicas}, ingress_domain = #{ingressDomain}, ingress_ip = #{ingressIp}, metadata = #{metadata} where id=#{id}")
	 int update(DeployVo deployVo);
	 
	 @Select("SELECT "
	 		+ "id, name, language, replicas, namespace, memory, cpu, ingress_domain as ingressDomain, ingress_ip as ingressIp, metadata"
	 		+ " from deploy WHERE id=#{id}")
	 DeployVo getById(String id);
	 
	 @Select("SELECT "
	 		+ "id, name, language, replicas, namespace, memory, cpu, ingress_domain as ingressDomain, ingress_ip as ingressIp, metadata"
	 		+ " from deploy WHERE name=#{name} and namespace=#{namespace}")
	 DeployVo getByNameAndNamespace(@Param("name") String name,@Param("namespace")String namespace);
	 
	 @Select("SELECT "
	 		+ "id, name, language, replicas, namespace, memory, cpu, ingress_domain as ingressDomain, ingress_ip as ingressIp, metadata"
	 		+ " from deploy WHERE namespace=#{namespace}")
	 List<DeployVo> getByNamespace(@Param("namespace")String namespace);
	 
	 @Delete("delete  from deploy WHERE name=#{name} and namespace=#{namespace}")
	 int deleteByNameAndNamespace(@Param("name") String name,@Param("namespace")String namespace);
	 
	 @Delete("delete  from deploy WHERE id=#{namespace}")
	 int deleteById(@Param("id") String id);
}