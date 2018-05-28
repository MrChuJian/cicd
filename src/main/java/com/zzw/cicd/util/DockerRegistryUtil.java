package com.zzw.cicd.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DockerRegistryUtil {

	private static String bashUrl = CacheUtil.getRegistryUrl();
	
	public static List<String> getImagesTarget(String name){
		HttpClient client = new HttpClient();  
		String url = bashUrl + "/v2/" + name + "/tags/list";
		List<String> list = null;
		HttpMethod get = new GetMethod(url);
		try {
			int code = client.executeMethod(get);
			if(code >= 200 && code <400) {
				String body = get.getResponseBodyAsString();
				JSONObject json = JSONObject.parseObject(body);
				JSONArray jsons = json.getJSONArray("tags");
				list = jsons.toJavaList(String.class);
			} else {
				return null;
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
