package com.zzw.cicd.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzw.cicd.dao.PplStageMapper;
import com.zzw.cicd.model.Vo.PplStage;
import com.zzw.cicd.service.StageService;

@Service
public class StageServiceImpl implements StageService {
	
	private static Log logger = LogFactory.getLog(ServiceServiceImpl.class);
	@Autowired
	private PplStageMapper pplstageMapper;

	@Override
	public PplStage getStageById(Integer id) {
		PplStage pplStage = null;
		try {
			pplStage = pplstageMapper.getById(id);
		} catch (Exception e) {
			logger.debug(e.toString());
			e.printStackTrace();
		}
		return pplStage;
	}

	@Override
	public List<PplStage> getAll() {
		List<PplStage> stages = null;
		try {
			stages = pplstageMapper.get();
		} catch (Exception e) {
			logger.debug(e.toString());
			e.printStackTrace();
		}
		return stages;
	}

	
}
