package com.zzw.cicd.service;

import com.zzw.cicd.model.User;

public interface UserService {

	public User findUserByName(String name);
}
