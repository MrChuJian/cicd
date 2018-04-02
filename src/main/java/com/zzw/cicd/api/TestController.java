package com.zzw.cicd.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zzw.cicd.model.Entity;
import com.zzw.cicd.model.Vo.PPL;
import com.zzw.cicd.model.Vo.PplTemplate;
import com.zzw.cicd.service.PipelineService;
import com.zzw.cicd.service.TemplateService;
import com.zzw.cicd.util.KubernetesUtil;

@Controller
public class TestController {
	
	@Autowired
	private PipelineService pipelineService;
	@Autowired
	private TemplateService templateService;

	@GetMapping(value = "/greeting")
	public ResponseEntity<Entity<String>> greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
		PplTemplate pplTemplate = templateService.getById(1);
		
		PPL ppl = new PPL();
		ppl.setName("test");
		ppl.setNode("master");
		ppl.setPplTemplate(pplTemplate);
		ppl.setMetadata("11");
		
		pipelineService.create(ppl);
		return Entity.success("hellow");
	}
	
	@RequestMapping(value = "/shabi", method = RequestMethod.GET)
	public String hello(Model model) {
		io.fabric8.kubernetes.api.Controller controller =  KubernetesUtil.getController("kube-system");
		return "greeting";
		
	}
	
	@RequestMapping(value = "cicd", method = RequestMethod.GET)
	public ResponseEntity<Entity<String>> test1() {
		return Entity.success("ojbk");
	}
	
	@RequestMapping(value = "cicd/ccc", method = RequestMethod.GET)
	public ResponseEntity<Entity<String>> test2() {
		return Entity.success("完全ojbk");
	}
}
