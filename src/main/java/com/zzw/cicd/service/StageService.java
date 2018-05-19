package com.zzw.cicd.service;

import java.util.List;

import com.zzw.cicd.model.Vo.PplStage;

public interface StageService {

	PplStage getStageById(Integer id);

	List<PplStage> getAll();

}
