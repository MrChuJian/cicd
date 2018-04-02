package com.zzw.cicd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzw.cicd.dao.PplStageMapper;
import com.zzw.cicd.dao.PplTaskMapper;
import com.zzw.cicd.dao.PplTemplateMapper;
import com.zzw.cicd.model.Vo.PplStage;
import com.zzw.cicd.model.Vo.PplTask;
import com.zzw.cicd.model.Vo.PplTemplate;
import com.zzw.cicd.model.Vo.TplStageTask;
import com.zzw.cicd.service.TemplateService;

@Service
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private PplTemplateMapper pplTemplateMapper;
	@Autowired
	private PplTaskMapper pplTaskMapper;
	@Autowired
	private PplStageMapper pplStageMapper;

	@Override
	public PplTemplate getById(Integer id) {
		PplTemplate pplTemplate = pplTemplateMapper.getById(id);
		List<TplStageTask> tplStageTasks = pplTemplate.getTplStageTasks();
		List<PplStage> stages = new ArrayList<>();
		for (TplStageTask tplStageTask : tplStageTasks) {
			PplStage pplStage = pplStageMapper.getById(tplStageTask.getStageId());
			String taskIds = "(";
			for (int i = 0; i < tplStageTask.getTaskIds().length; i++) {
				if (i == 0) {
					taskIds += tplStageTask.getTaskIds()[i] + "";
				} else {
					taskIds = "," + tplStageTask.getTaskIds()[i];
				}
			}
			taskIds += ")";
			List<PplTask> geByIds = pplTaskMapper.geByIds(taskIds);
			pplStage.setTasks(geByIds);
			stages.add(pplStage);
		}
		pplTemplate.setStages(stages);
		return pplTemplate;
	}

	@Override
	public List<PplTemplate> getAll() {
		List<PplTemplate> all = pplTemplateMapper.getAll();
		return all;
	}

}