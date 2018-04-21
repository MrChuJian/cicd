package com.zzw.springboot;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.zzw"})
public class SampleTomcatApplication implements ServletContextInitializer {
	public static ConfigurableApplicationContext applicationContext = null;
	private static Log logger = LogFactory.getLog(SampleTomcatApplication.class);
	
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(SampleTomcatApplication.class, args);
//		PropertyUtil proprtyuUtil = new PropertyUtil();
//		proprtyuUtil.init();
		System.out.println("系统启动成功");
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
	}
}
