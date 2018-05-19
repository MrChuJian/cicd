package com.zzw.cicd.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.zzw.cicd.model.Entity;
import com.zzw.cicd.model.Vo.PPL;
import com.zzw.cicd.model.Vo.PplStage;
import com.zzw.cicd.model.Vo.PplTask;
import com.zzw.cicd.model.Vo.PplTemplate;
import com.zzw.cicd.service.PipelineService;
import com.zzw.cicd.service.StageService;
import com.zzw.cicd.service.TaskService;
import com.zzw.cicd.service.TemplateService;
import com.zzw.cicd.util.JenkinsUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "构建控制器", tags = { "构建控制器" })
@RestController
@RequestMapping("/api/v1/build")
public class BuildController {
	@Autowired
	private PipelineService pipelineService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private StageService stageService;
	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "/name/{name}/job", method = RequestMethod.GET)
	public ResponseEntity<Entity<JobWithDetails>> getJobByName(@PathVariable String name) {
		JobWithDetails jobByName = JenkinsUtil.getJobByName(name);
		return Entity.success(jobByName);
	}

	@RequestMapping(value = "/jobs", method = RequestMethod.GET)
	public ResponseEntity<Entity<Map>> getJobs() {
		Map<String, Job> jobs1 = JenkinsUtil.getJobs1();
		return Entity.success(jobs1);
	}

	@ApiOperation(value = "添加构建项目", notes = "添加构建项目")
	@RequestMapping(value = "/ppl", method = RequestMethod.POST)
	public ResponseEntity<Entity<Boolean>> addPPl(@RequestBody PPL ppl) {
		boolean create = pipelineService.create(ppl);
		return Entity.success(create);
	}

	@ApiOperation(value = "修改构建项目", notes = "修改构建项目")
	@RequestMapping(value = "/ppl", method = RequestMethod.PUT)
	public ResponseEntity<Entity<Boolean>> updatePPl(@RequestBody PPL ppl) {
		boolean create = pipelineService.update(ppl);
		return Entity.success(create);
	}

	@ApiOperation(value = "获取构建项目", notes = "获取获取构建")
	@RequestMapping(value = "/ppl/{id}", method = RequestMethod.GET)
	public ResponseEntity<Entity<PPL>> getPPlById(@PathVariable Integer id) {
		PPL byId = pipelineService.getById(id);
		return Entity.success(byId);
	}

	@ApiOperation(value = "删除构建项目", notes = "删除构建项目")
	@RequestMapping(value = "/ppl/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Entity<Boolean>> deletePPl(@PathVariable Integer id) {
		boolean create = pipelineService.delete(id);
		return Entity.success(create);
	}

	@ApiOperation(value = "启动构建工程", notes = "启动构建工程")
	@RequestMapping(value = "/ppl/{id}/start", method = RequestMethod.GET)
	public ResponseEntity<Entity<Boolean>> startPPlById(@PathVariable Integer id) {
		boolean start = pipelineService.start(id);
		return Entity.success(start);
	}

	@ApiOperation(value = "停止构建工程", notes = "停止构建工程")
	@RequestMapping(value = "/ppl/{id}/stop", method = RequestMethod.PUT)
	public ResponseEntity<Entity<Boolean>> stopPPl(@PathVariable Integer id) {
		boolean create = pipelineService.stop(id);
		return Entity.success(create);
	}

	@ApiOperation(value = "获取所有的构建模板", notes = "获取所有的构建模板")
	@RequestMapping(value = "/ppl/template", method = RequestMethod.GET)
	public ResponseEntity<Entity<List<PplTemplate>>> getPPls() {
		List<PplTemplate> all = templateService.getAll();
		System.out.println(1);
		return Entity.success(all);
	}

	@ApiOperation(value = "通过Id获取构建模板", notes = "通过Id获取构建模板")
	@RequestMapping(value = "/ppl/template/{id}", method = RequestMethod.GET)
	public ResponseEntity<Entity<PplTemplate>> getTemplateById(@PathVariable Integer id) {
		PplTemplate byId = templateService.getById(id);
		return Entity.success(byId);
	}
	
	@ApiOperation(value = "通过Id获取步骤Stage", notes = "通过Id获取步骤Stage")
	@RequestMapping(value = "/ppl/stage/{id}", method = RequestMethod.GET)
	public ResponseEntity<Entity<PplStage>> getStageById(@PathVariable Integer id) {
		PplStage pplStage = null;
		pplStage = stageService.getStageById(id);
		if(pplStage == null) {
			return Entity.failure(1, "不存在id为" + id + "的stage");
		}
		return Entity.success(pplStage);
	}
	
	@ApiOperation(value = "获取步骤Stage", notes = "获取所有步骤Stage")
	@RequestMapping(value = "/ppl/stage", method = RequestMethod.GET)
	public ResponseEntity<Entity<List<PplStage>>> getStage() {
		List<PplStage> stages = null;
		stages = stageService.getAll();
		if(stages == null || stages.size() < 1) {
			return Entity.failure(2, "没有Stage");
		}
		return Entity.success(stages);
	}
	
	@ApiOperation(value = "通过Id获取步骤Task", notes = "通过Id获取步骤Task")
	@RequestMapping(value = "/ppl/task/{id}", method = RequestMethod.GET)
	public ResponseEntity<Entity<PplTask>> getTaskById(@PathVariable Integer id) {
		PplTask task = null;
		task = taskService.getTaskById(id);
		if(task == null) {
			return Entity.failure(1, "不存在id为" + id + "的task");
		}
		return Entity.success(task);
	}
	
	@ApiOperation(value = "获取任务Task", notes = "获取所有任务Task")
	@RequestMapping(value = "/ppl/task", method = RequestMethod.GET)
	public ResponseEntity<Entity<List<PplTask>>> getTask() {
		List<PplTask> tasks = null;
		tasks = taskService.getAll();
		if(tasks == null || tasks.size() < 1) {
			return Entity.failure(2, "没有task");
		}
		return Entity.success(tasks);
	}
}