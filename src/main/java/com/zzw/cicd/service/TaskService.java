package com.zzw.cicd.service;

import java.util.List;

import com.zzw.cicd.model.Vo.PplTask;

public interface TaskService {
	PplTask getTaskById(Integer id);
	
	List<PplTask> getAll();
}
