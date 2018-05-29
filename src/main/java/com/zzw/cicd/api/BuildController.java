package com.zzw.cicd.api;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.zzw.cicd.model.BuildVo;
import com.zzw.cicd.model.Entity;
import com.zzw.cicd.model.JobVo;
import com.zzw.cicd.model.JobWithDetailsVo;
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

	@RequestMapping(value = "/name/{name}/job/detail", method = RequestMethod.GET)
	public ResponseEntity<Entity<JobWithDetailsVo>> getJobByName(@PathVariable String name) {
		JobWithDetails jobByName = JenkinsUtil.getJobByName(name);
		JobWithDetailsVo details = new JobWithDetailsVo();
		if (jobByName != null) {
			BeanUtils.copyProperties(jobByName, details);
			List<Build> jlist = jobByName.getBuilds();
			List<BuildVo> vlist = new LinkedList<>();
			BuildVo vBuild = null;

			for (Build build : jlist) {
				vBuild = new BuildVo();
				BeanUtils.copyProperties(build, vBuild);
				vlist.add(vBuild);
			}
			details.setBuilds(vlist);

			List<Job> jjlist = jobByName.getUpstreamProjects();
			List<JobVo> vjlist = new LinkedList<>();
			JobVo vjJob = null;
			for (Job job : jjlist) {
				vjJob = new JobVo();
				BeanUtils.copyProperties(job, vjJob);
				vjlist.add(vjJob);
			}
			details.setUpstreamProjects(vjlist);

			jjlist = jobByName.getDownstreamProjects();
			vjlist = new LinkedList<>();
			for (Job job : jjlist) {
				vjJob = new JobVo();
				BeanUtils.copyProperties(job, vjJob);
				vjlist.add(vjJob);
			}
			details.setDownstreamProjects(vjlist);

			vBuild = new BuildVo();
			BeanUtils.copyProperties(jobByName.getLastBuild(), vBuild);
			details.setLastBuild(vBuild);

			vBuild = new BuildVo();
			BeanUtils.copyProperties(jobByName.getFirstBuild(), vBuild);
			details.setFirstBuild(vBuild);

			vBuild = new BuildVo();
			BeanUtils.copyProperties(jobByName.getLastSuccessfulBuild(), vBuild);
			details.setLastSuccessfulBuild(vBuild);

			System.out.println(details);
			return Entity.success(details);
		} else {
			return Entity.failure(1, "获得Jobdetail失败,请确保CI Server已启动");
		}
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
	@RequestMapping(value = "/ppl/id/{id}", method = RequestMethod.GET)
	public ResponseEntity<Entity<PPL>> getPPlById(@PathVariable Integer id) {
		PPL byId = pipelineService.getById(id);
		return Entity.success(byId);
	}

	@ApiOperation(value = "获取构建项目", notes = "获取获取构建")
	@RequestMapping(value = "/ppl/name/{name}", method = RequestMethod.GET)
	public ResponseEntity<Entity<PPL>> getPPlByName(@PathVariable String name) {
		PPL byName = pipelineService.getByName(name);
		return Entity.success(byName);
	}

	@ApiOperation(value = "删除构建项目", notes = "删除构建项目")
	@RequestMapping(value = "/ppl/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Entity<Boolean>> deletePPl(@PathVariable Integer id) {
		boolean create = pipelineService.delete(id);
		return Entity.success(create);
	}

	@ApiOperation(value = "打开项目", notes = "打开项目")
	@RequestMapping(value = "/ppl/{id}/enable", method = RequestMethod.GET)
	public ResponseEntity<Entity<Boolean>> enablePPlById(@PathVariable Integer id) {
		boolean start = pipelineService.enableJob(id);
		JenkinsUtil.test();
		return Entity.success(null);
	}

	@ApiOperation(value = "启动项目", notes = "启动项目")
	@RequestMapping(value = "/ppl/{id}/start", method = RequestMethod.GET)
	public ResponseEntity<Entity<Boolean>> startPPlById(@PathVariable Integer id) {
		boolean start = pipelineService.start(id);
		if (start) {
			return Entity.success(start);
		} else {
			return Entity.failure(1, "报错拉");
		}
	}

	@ApiOperation(value = "判断构建是否执行完", notes = "判断构建是否执行完")
	@RequestMapping(value = "/ppl/isFinished/name/{name}/num/{num}", method = RequestMethod.GET)
	public ResponseEntity<Entity<String>> isFinished(@PathVariable Integer num, @PathVariable String name) {
		boolean isFinished = JenkinsUtil.isFinished(num, name);
		if (isFinished) {
			return Entity.success("构建完成");
		}
		return Entity.success("构建中");
	}

	@ApiOperation(value = "获得构建日志", notes = "获得构建日志")
	@RequestMapping(value = "/ppl/log/name/{name}/num/{num}", method = RequestMethod.GET)
	public ResponseEntity<Entity<String>> getJobLog(@PathVariable Integer num, @PathVariable String name) {
		String log = null;
		try {
			log = JenkinsUtil.getJobLog(num, name);
		} catch (IOException e) {
			e.printStackTrace();
			return Entity.failure(2, "获得构建日志失败");
		}
		if (log != null && !log.equals("")) {
			return Entity.success(log);
		}
		return Entity.failure(1, "无构建日志");
	}

	@ApiOperation(value = "禁用项目", notes = "禁用项目")
	@RequestMapping(value = "/ppl/{id}/disable", method = RequestMethod.PUT)
	public ResponseEntity<Entity<Boolean>> disablePPl(@PathVariable Integer id) {
		boolean create = pipelineService.disableJob(id);
		return Entity.success(create);
	}

	@ApiOperation(value = "获取所有的构建模板", notes = "获取所有的构建模板")
	@RequestMapping(value = "/ppl/template", method = RequestMethod.GET)
	public ResponseEntity<Entity<List<PplTemplate>>> getPPls() {
		List<PplTemplate> all = templateService.getAll();
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
		if (pplStage == null) {
			return Entity.failure(1, "不存在id为" + id + "的stage");
		}
		return Entity.success(pplStage);
	}

	@ApiOperation(value = "获取步骤Stage", notes = "获取所有步骤Stage")
	@RequestMapping(value = "/ppl/stage", method = RequestMethod.GET)
	public ResponseEntity<Entity<List<PplStage>>> getStage() {
		List<PplStage> stages = null;
		stages = stageService.getAll();
		if (stages == null || stages.size() < 1) {
			return Entity.failure(2, "没有Stage");
		}
		return Entity.success(stages);
	}

	@ApiOperation(value = "通过Id获取步骤Task", notes = "通过Id获取步骤Task")
	@RequestMapping(value = "/ppl/task/{id}", method = RequestMethod.GET)
	public ResponseEntity<Entity<PplTask>> getTaskById(@PathVariable Integer id) {
		PplTask task = null;
		task = taskService.getTaskById(id);
		if (task == null) {
			return Entity.failure(1, "不存在id为" + id + "的task");
		}
		return Entity.success(task);
	}

	@ApiOperation(value = "获得最后一次构建DockerImageName", notes = "通过Id获取步骤Task获得最后一次构建DockerImageName")
	@RequestMapping(value = "/ppl/job/{jobName}/dockerImageName", method = RequestMethod.GET)
	public ResponseEntity<Entity<String>> getDockerImageName(@PathVariable String jobName) {

		JobWithDetails jobWithDetails = JenkinsUtil.getJobByName(jobName);
		int num = jobWithDetails.getLastBuild().getNumber();
		try {
			String log = JenkinsUtil.getJobLog(num, jobName);
			String s[] = log.split("\r\n");
			String dockerIndex = "docker push ";
			for (int i = 0; i < s.length; i++) {
				if (s[i].contains(dockerIndex)) {
					return Entity
							.success(s[i].substring(s[i].indexOf(dockerIndex) + dockerIndex.length(), s[i].length()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Entity.success(null);
	}

	@ApiOperation(value = "获取任务Task", notes = "获取所有任务Task")
	@RequestMapping(value = "/ppl/task", method = RequestMethod.GET)
	public ResponseEntity<Entity<List<PplTask>>> getTask() {
		List<PplTask> tasks = null;
		tasks = taskService.getAll();
		if (tasks == null || tasks.size() < 1) {
			return Entity.failure(2, "没有task");
		}
		return Entity.success(tasks);
	}
}