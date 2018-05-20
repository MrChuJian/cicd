package com.zzw.cicd.api;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zzw.cicd.model.Entity;
import com.zzw.cicd.model.Test;
import com.zzw.cicd.model.User;
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
	
	@RequestMapping(value = "ojbk", method = RequestMethod.GET)
	public ResponseEntity<Entity<String>> test1() {
		String hostAddress = null;
		try {
			InetAddress address = InetAddress.getLocalHost();
			hostAddress = address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return Entity.success("本服务器ip:" + hostAddress);
	}
	
	@RequestMapping(value = "wqojbk", method = RequestMethod.POST)
	public ResponseEntity<Entity<String>> test2(@RequestBody Test test) {
		System.out.println(test);
		return Entity.success("完全ojbk");
	}
}
