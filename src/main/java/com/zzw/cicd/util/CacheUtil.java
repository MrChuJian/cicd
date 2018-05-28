package com.zzw.cicd.util;

import java.util.HashMap;
import java.util.Map;

public class CacheUtil {

	private static Map<String, String> cache = new HashMap<>();
	private static boolean isInit = false;
	
	private static boolean init() {
		put("jenkinsUrl", "http://120.77.34.35:8001");
		put("kubernetesUrl", "https://192.168.52.130:6443");
		put("mysqlUrl", "http://120.77.34.35:3306");
		put("RegistryUrl", "http://120.77.34.35:5000");
		isInit = true;
		return isInit;
		
	}
	
	public static String getJenkinsUrl() {
		return get("jenkinsUrl");
	}
	
	public static String getKubernetesUrl() {
		return get("kubernetesUrl");
	}
	
	public static String getmysqlUrl() {
		return get("mysqlUrl");
	}
	
	public static String getRegistryUrl() {
		return get("RegistryUrl");
	}
	
	public static String get(String key) {
		if(!isInit) {
			init();
		}
		return cache.get(key);
	}
	
	public static void put(String key, String value) {
		cache.put(key, value);
	}
	
}
