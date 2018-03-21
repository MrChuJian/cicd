package com.zzw.cicd.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;

public class PropertyUtil extends PropertiesLoaderSupport{
	
	private Properties config = null;
	
	public void init() {
		try {
			String configLocation = "/application.properties";
			List<Resource> resources = new ArrayList<Resource>();
			String[] cls = StringUtil.split(configLocation);
			for (int i = 0; i < cls.length; i++) {
				resources.addAll(Arrays.asList(ResourceUtil.getResources(cls[i])));
			}
			this.setLocations(resources.toArray(new Resource[0]));
			this.setLocations(resources.toArray(new Resource[0]));
			this.config = this.mergeProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
