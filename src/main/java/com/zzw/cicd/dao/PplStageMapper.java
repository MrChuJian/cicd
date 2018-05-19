package com.zzw.cicd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.zzw.cicd.model.Vo.PplStage;

@Repository
public interface PplStageMapper extends BaseMapper {

	@Insert("insert into ppl_stage(name) values #{name} ")
	int create(PplStage pplTask);

	@Update("update ppl_stage set name=#{name} where id=#{id}")
	int update(PplStage pplTask);

	@Delete("delete from ppl_stage where id=#{id}")
	int deleteById(Integer id);

	@Select("select * from ppl_stage where id=#{id}")
	PplStage getById(Integer id);

	@Select("select * from ppl_stage where id in #{ids}")
	List<PplStage> geByIds(String ids);

	@Select("select * from ppl_stage")
	List<PplStage> get();

}