package com.zzw.cicd.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zzw.cicd.model.Entity;

@Controller
public class TestController {

	@GetMapping(value = "/greeting")
	public ResponseEntity<Entity<String>> greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
		return Entity.success("hellow");
	}
	
	@RequestMapping(value = "/shabi")
	public String hello(Model model) {
		model.addAttribute("name", "shabi");
		return "greeting";
		
	}
}
