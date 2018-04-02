package com.zzw.cicd.service;

import java.util.List;

import com.zzw.cicd.model.Vo.PplTemplate;

public interface TemplateService {
	PplTemplate getById(Integer id);

	List<PplTemplate> getAll();
}
