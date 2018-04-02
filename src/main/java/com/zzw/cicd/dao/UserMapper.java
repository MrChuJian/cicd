package com.zzw.cicd.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.zzw.cicd.model.User;

@Repository
public interface UserMapper extends BaseMapper {
	
	@Select("select * from user where name = #{name}")
	User findUserByName(@Param("name")String name);
}
