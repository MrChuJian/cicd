package com.zzw.springboot;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.zzw"})
public class Application implements ServletContextInitializer {
	public static ConfigurableApplicationContext applicationContext = null;

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(Application.class, args);
//		PropertyUtil proprtyuUtil = new PropertyUtil();
//		proprtyuUtil.init();
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
	}
}
