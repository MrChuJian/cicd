package com.zzw.cicd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzw.cicd.dao.PplMapper;
import com.zzw.cicd.model.Vo.FtlVo;
import com.zzw.cicd.model.Vo.PPL;
import com.zzw.cicd.model.Vo.PplStage;
import com.zzw.cicd.model.Vo.PplTask;
import com.zzw.cicd.model.Vo.PplTaskParam;
import com.zzw.cicd.model.Vo.PplTemplate;
import com.zzw.cicd.service.PipelineService;
import com.zzw.cicd.util.FTLUtilPPL;
import com.zzw.cicd.util.JenkinsUtil;

@Service
public class PipelineServiceImpl implements PipelineService {

	@Autowired
	private PplMapper pplMapper;

	@Override
	public boolean create(PPL ppl) {
		// 传过来的是配置好的pipeline，需要执行三个步骤：
		// 1,创建空的jenkins工程 2,生成job.xml并创建jenkins完整工程
		// 3,存库
		FtlVo ftlVo = new FtlVo();
		ftlVo.setDisplayName(ppl.getName() + System.currentTimeMillis());// 工程名需要定义：代码仓名+分支名
		ftlVo.setHasTrigger(false);
		ftlVo.setHasSCMTrigger(false);
		ftlVo.setDaysToKeep(2);
		ftlVo.setNumToKeep(2);
		ftlVo.setQuietPeriod(5);
		ftlVo.setIgnoreHook("true");
		ftlVo.setSpec("");
		String pplScript = getPplScript(ppl.getPplTemplate(), ppl.getNode());
		ppl.setScript(pplScript);
		ftlVo.setScript(pplScript);
		try {
			String jobXml = FTLUtilPPL.processVo(ftlVo);
			boolean createJob = JenkinsUtil.createJob(ppl.getName(), jobXml);
			if (createJob) {
				pplMapper.insert(ppl);
			}
			System.out.println(ppl.getName() + "流水线创建成功");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 根据传过来的ppl生成对应的stageScript
	 * 
	 * @param pplTemplate
	 * @return
	 */
	public String getPplScript(PplTemplate ppl, String node) {
		// 1.拼凑linuxStageScript
		// 2.拼凑windowsStageScript
		// 3.全局变量
		// 4.局部变量
		// 5.整体拼凑
		
		StringBuffer script = new StringBuffer();
		script.append("\n");
		script.append("node('" + node + "'){");
		script.append("\n");
		StringBuffer gobalVar = new StringBuffer();
		List<PplStage> stages = ppl.getStages();
		System.out.println(stages);
		StringBuffer windowsStageScript = new StringBuffer();
		StringBuffer linuxStageScript = new StringBuffer();
		for (PplStage pplStage : stages) {
			String stageName = pplStage.getName();
			linuxStageScript.append("stage('" + stageName + "'){");
			linuxStageScript.append("\n");
			windowsStageScript.append("stage('" + stageName + "'){");
			windowsStageScript.append("\n");
			List<PplTask> tasks = pplStage.getTasks();
			for (PplTask pplTask : tasks) {
				List<PplTaskParam> taskParams = pplTask.getTaskParams();
				if (!"".equals(pplTask.getWorkspace()) && pplTask.getWorkspace() != null) {
					linuxStageScript.append("dir(\"" + pplTask.getWorkspace() + "\"){");
					linuxStageScript.append("\n");
					windowsStageScript.append("dir(\"" + pplTask.getWorkspace() + "\"){");
					windowsStageScript.append("\n");
				}
				linuxStageScript.append("parallel '" + pplTask.getName() + "' : {");
				linuxStageScript.append("\n");
				windowsStageScript.append("parallel '" + pplTask.getName() + "' : {");
				windowsStageScript.append("\n");
				for (PplTaskParam pplTaskParam : taskParams) {
					// 拼凑全局变量
					if (pplTaskParam.isGobal()) {
						gobalVar.append("def " + pplTaskParam.getKey() + "= '" + pplTaskParam.getValue() + "'");
						gobalVar.append("\n");
					} else {
						// 局部变量
						linuxStageScript.append("def " + pplTaskParam.getKey() + "= '" + pplTaskParam.getValue() + "'");
						linuxStageScript.append("\n");
						windowsStageScript
								.append("def " + pplTaskParam.getKey() + "= '" + pplTaskParam.getValue() + "'");
						windowsStageScript.append("\n");
					}
				}
				linuxStageScript.append(pplTask.getLinuxScript());
				linuxStageScript.append("\n");
				linuxStageScript.append("}");
				linuxStageScript.append("\n");
				windowsStageScript.append(pplTask.getWindowsScript());
				windowsStageScript.append("\n");
				windowsStageScript.append("}");
				windowsStageScript.append("\n");
				if (!"".equals(pplTask.getWorkspace()) && pplTask.getWorkspace() != null) {
					linuxStageScript.append("}");
					linuxStageScript.append("\n");
					windowsStageScript.append("}");
					windowsStageScript.append("\n");
				}
			}
			linuxStageScript.append("}");
			linuxStageScript.append("\n");
			windowsStageScript.append("}");
			windowsStageScript.append("\n");
		}
		script.append(gobalVar);
		script.append("if (isUnix()){");
		script.append("\n");
		script.append(linuxStageScript);
		script.append("}");
		script.append("else {");
		script.append("\n");
		script.append(windowsStageScript);
		script.append("}");// windows脚本结尾
		script.append("\n");
		script.append("}");// node的结尾
		return script.toString();
	}

	@Override
	public PPL getById(Integer id) {
		return pplMapper.getById(id);
	}

	@Override
	public boolean start(Integer id) {
		PPL byId = pplMapper.getById(id);
		boolean startJob = JenkinsUtil.startJob(byId.getName());
		return startJob;

	}

	@Override
	public boolean stop(Integer id) {
		PPL byId = pplMapper.getById(id);
		boolean stopJob = JenkinsUtil.startJob(byId.getName());
		return stopJob;
	}

	@Override
	public boolean delete(Integer id) {
		PPL byId = pplMapper.getById(id);
		boolean deleteJob = JenkinsUtil.deleteJob(byId.getName());
		pplMapper.deleteById(id);
		return deleteJob;
	}

	@Override
	public boolean update(PPL ppl) {
		FtlVo ftlVo = new FtlVo();
		ftlVo.setDisplayName(ppl.getName());// 工程名需要定义：代码仓名+分支名
		ftlVo.setHasTrigger(false);
		ftlVo.setHasSCMTrigger(false);
		ftlVo.setDaysToKeep(2);
		ftlVo.setNumToKeep(2);
		ftlVo.setQuietPeriod(5);
		ftlVo.setIgnoreHook("true");
		ftlVo.setSpec("");
		String pplScript = getPplScript(ppl.getPplTemplate(), ppl.getNode());
		ppl.setScript(pplScript);
		ftlVo.setScript(pplScript);
		try {
			String jobXml = FTLUtilPPL.processVo(ftlVo);
			boolean updateJob = JenkinsUtil.updateJobConfig(ppl.getName(), jobXml);
			if (updateJob) {
				pplMapper.update(ppl);
			}
			System.out.println(ppl.getName() + "流水线更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}