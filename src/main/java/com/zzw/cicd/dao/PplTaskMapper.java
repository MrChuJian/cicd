package com.zzw.cicd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.zzw.cicd.model.Vo.PplTask;

@Repository
public interface PplTaskMapper extends BaseMapper {
	@Insert("INSERT INTO ppl_task (name,input,windows_script,linux_script,description,stage_id,workspace) "
			+ "VALUES (#{name}, #{input}, #{windowsScript} ,#{linuxScript},#{description},#{stageId},#{workspace})")
	int insert(PplTask pplTask);

	@Update("update ppl_task set name=#{name},input=#{input},windows_script=#{windowsScript},linux_script=#{linuxScript},description=#{description},stage_id=#{stageId},workspace=#{workspace} ")
	int update(PplTask pplTask);

	@Delete("delete  from ppl_task WHERE id=#{id}")
	int deleteById(Integer id);

	@Select("select * from ppl_task where id=#{id}")
	PplTask getById(Integer id);

	@Select("select * from ppl_task where id in ${ids}")
	List<PplTask> getByIds(@Param("ids") String ids);
	
	@Select("select * from ppl_task")
	List<PplTask> getAll();

}
