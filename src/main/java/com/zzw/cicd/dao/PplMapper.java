package com.zzw.cicd.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.zzw.cicd.model.Vo.PPL;

@Repository
public interface PplMapper extends BaseMapper {

	@Select("select * from ppl where id=#{id}")
	PPL getById(Integer id);

	@Insert("INSERT INTO ppl (name,script,metadata,node) " + "VALUES (#{name}, #{script} ,#{metadata}, #{node})")
	int insert(PPL ppl);

	@Update("update ppl set name=#{name},script=#{script},metadata=#{metadata},node=#{node} where name=#{name}")
	int update(PPL ppl);

	@Delete("delete  from ppl WHERE id=#{id}")
	int deleteById(Integer id);
}
