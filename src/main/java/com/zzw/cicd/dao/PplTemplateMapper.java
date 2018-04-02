package com.zzw.cicd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.zzw.cicd.model.Vo.PplTemplate;

@Repository
public interface PplTemplateMapper extends BaseMapper {

	@Select("select * from ppl_template where id=#{id}")
	PplTemplate getById(Integer id);

	@Select("select * from ppl_template")
	List<PplTemplate> getAll();

	@Insert("INSERT INTO ppl_template (name,description,language,tpl_stage_task,default_params) "
			+ "VALUES (#{name}, #{description}, #{language} ,#{tplStageTask},#{description},#{defaultsarams})")
	int insert(PplTemplate pplTemplate);

	@Update("update ppl_template set name=#{name},description=#{description},language=#{language},tpl_stage_task=#{tplStageTask},default_params=#{defaultsarams} ")
	int update(PplTemplate pplTemplate);

	@Delete("delete  from ppl_template WHERE id=#{id}")
	int deleteById(Integer id);
}
