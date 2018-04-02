package com.zzw.cicd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzw.cicd.dao.PplTaskMapper;
import com.zzw.cicd.model.Vo.PplTask;
import com.zzw.cicd.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private PplTaskMapper pplTaskMapper;

	@Override
	public PplTask getById(Integer id) {
		return pplTaskMapper.getById(id);
	}

}
