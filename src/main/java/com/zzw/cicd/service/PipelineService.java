package com.zzw.cicd.service;

import com.zzw.cicd.model.Vo.PPL;

public interface PipelineService {
	boolean create(PPL ppl);

	boolean update(PPL ppl);

	boolean start(Integer id);

	boolean stop(Integer id);

	boolean delete(Integer id);

	PPL getById(Integer id);
}