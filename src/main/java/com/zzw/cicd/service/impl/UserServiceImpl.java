package com.zzw.cicd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzw.cicd.dao.UserMapper;
import com.zzw.cicd.model.User;
import com.zzw.cicd.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User findUserByName(String name) {
		return userMapper.findUserByName(name);
	}

}
