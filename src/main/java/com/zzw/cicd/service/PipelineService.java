package com.zzw.cicd.service;

import com.zzw.cicd.model.Vo.PPL;

public interface PipelineService {
	boolean create(PPL ppl);

	boolean update(PPL ppl);

	boolean start(Integer id);

	boolean delete(Integer id);

	PPL getById(Integer id);

	boolean enableJob(Integer id);

	boolean disableJob(Integer id);

	PPL getByName(String name);
}