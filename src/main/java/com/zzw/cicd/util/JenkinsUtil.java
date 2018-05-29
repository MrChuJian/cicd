package com.zzw.cicd.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.json.XML;
import org.springframework.web.util.UriUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.zzw.cicd.model.GetRequest;
import com.zzw.cicd.model.HttpException;
import com.zzw.cicd.model.JenkinsConfig;
import com.zzw.cicd.model.JenkinsJob;
import com.zzw.cicd.model.JobBuild;
import com.zzw.cicd.model.PluginDependencies;
import com.zzw.cicd.model.Plugins;
import com.zzw.cicd.model.PostRequest;
import com.zzw.cicd.model.Result;
import com.zzw.cicd.model.WsConnector;
import com.zzw.cicd.model.WsResponse;
import com.zzw.cicd.model.Vo.ArtifactVo;
import com.zzw.cicd.model.Vo.ChangesetVo;
import com.zzw.cicd.model.Vo.CredentialVo;
import com.zzw.cicd.model.Vo.JobConfigVo;
import com.zzw.cicd.model.Vo.StageLogVo;
import com.zzw.cicd.model.Vo.WsDescribeVo;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class JenkinsUtil {
	private static final Log logger = LogFactory.getLog(JenkinsUtil.class);
	private static JenkinsConfig jenkinsInfo = null;
	private static JenkinsServer jenkinsServer = null;

	public static void setJenkinsInfo(JenkinsConfig info) {
		jenkinsInfo = info;
	}

	public static JenkinsServer getJenkinsServer() {

		if (jenkinsServer == null)
			try {
				jenkinsInfo = new JenkinsConfig();
				jenkinsInfo.setUsername("admin");
				jenkinsInfo.setPassword("admin");
				jenkinsInfo.setUrl(CacheUtil.getJenkinsUrl());

				jenkinsServer = new JenkinsServer(new URI(jenkinsInfo.getUrl()), jenkinsInfo.getUsername(),
						jenkinsInfo.getPassword());
			} catch (URISyntaxException e) {
				logger.info("获取jenkins 连接失败！");
				e.printStackTrace();
			}
		return jenkinsServer;
	}

	public static Map<String, Job> getJobs1() {
		Map<String, Job> jobs = null;
		try {
			jobs = getJenkinsServer().getJobs();
		} catch (IOException e) {
			logger.info("获取job信息失败");
			e.printStackTrace();
		}
		return jobs;
	}

	public static boolean deleteJob(String name) {
		try {
			getJenkinsServer().deleteJob(name);
			return true;
		} catch (IOException e) {
			logger.info("删除job失败");
			e.printStackTrace();
			return false;
		}
	}

	public static boolean createJob(String name, String jobXml) {
		try {
			getJenkinsServer().createJob(name, jobXml, true);
			return true;
		} catch (IOException e) {
			logger.info("创建job失败");
			e.printStackTrace();
			return false;
		}
	}

	public static boolean startJob(String name) {
		try {
			getJenkinsServer().enableJob(name);
			return true;
		} catch (IOException e) {
			logger.info("启动job失败");
			e.printStackTrace();
			return false;
		}
	}

	public static boolean enableJob(String name) {
		try {
			getJenkinsServer().enableJob(name);
			return true;
		} catch (IOException e) {
			logger.info("启动job失败");
			e.printStackTrace();
			return false;
		}
	}

	public static boolean stopJob(String name) {
		try {
			getJenkinsServer().disableJob(name);
			return true;
		} catch (IOException e) {
			logger.info("停止job失败");
			e.printStackTrace();
			return false;
		}
	}

	public static boolean disableJob(String name) {
		try {
			getJenkinsServer().disableJob(name);
			return true;
		} catch (IOException e) {
			logger.info("停止job失败");
			e.printStackTrace();
			return false;
		}
	}

	public static JobWithDetails getJobByName(String jobName) {
		JobWithDetails job = null;
		try {
			job = getJenkinsServer().getJob(jobName);
		} catch (IOException e) {
			logger.info("获取job信息失败：" + jobName);
			e.printStackTrace();
		}
		return job;
	}





	
	public static boolean updateJobConfig(String jobName, String configXml) throws UnsupportedEncodingException {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		boolean updateJobSuccess = false;
		if (getJobByName(jobName) == null) {
			createJob(jobName, configXml);
		}
		if (getJobByName(jobName) != null) {
			try {
				getJenkinsServer().updateJob(jobName, configXml);
				updateJobSuccess = true;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return updateJobSuccess;
	}

	

	public static boolean changePPLStatus(String pplId) throws UnsupportedEncodingException {
		return true;
	}

	public static boolean setPPLStatus(String pplId, String pplStatus) throws UnsupportedEncodingException {
		return true;
	}

	

	public static String genBuildUrl(String jobName, int number) throws UnsupportedEncodingException {
		// return path(ConfigUtil.getConfigString("jenkins.serverUri"), "job",
		// encodeUrlCompnent(jobName), number + "");
		return "";
	}


	public static String path(String... components) {
		String path = "";
		for (int i = 0; i < components.length; i++) {
			if (i == 0) {
				path += components[i];
			} else {
				path += "/" + components[i];
			}
		}
		return path;
	}

	private static String encodeUrlCompnent(String jobId) throws UnsupportedEncodingException {
		jobId = UriUtils.encodePathSegment(jobId, "utf-8");
		jobId = jobId.replaceAll("%2F", "/");
		jobId = jobId.replaceAll("%252F", "/");
		jobId = jobId.replaceAll("%253A", ":");
		return jobId;
	}









	public static boolean isXML(String value) {
		try {
			DocumentHelper.parseText(value);
		} catch (DocumentException e) {
			return false;
		}
		return true;
	}


	public static void test() {
		JenkinsServer server = getJenkinsServer();
		boolean isBuilding;
		try {
			JobWithDetails job = server.getJobs().get("cicd").details();
			Build buildByNumber = job.getBuildByNumber(5);
			if (null != buildByNumber) {
				BuildWithDetails details = buildByNumber.details();
				if (null != details) {
					isBuilding = details.isBuilding();
				} else {
					isBuilding = true;
				}
			} else {
				isBuilding = true;
			}
			if (isBuilding) {
				System.out.println("building");
			} else {
				System.out.println("builded");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 获取某次构建的日志信息
	public static String getJobLog(int buildNumber, String jobName) throws IOException {

		JenkinsServer jenkins = getJenkinsServer();
		JobWithDetails job = jenkins.getJob(jobName);
		JobWithDetails details = job.details();
		Build buildByNumber = details.getBuildByNumber(buildNumber);
		if(buildByNumber == null) {
			return null;
		}
		BuildWithDetails details2 = buildByNumber.details();
		String outputText = details2.getConsoleOutputText();
		return outputText;
	}

	// 获取某次构建的开始时间和持续时间
	public static Map<String, Long> getStartTImeAndEndTime(String jobName, int number) throws IOException {

		JenkinsServer jenkins = getJenkinsServer();
		Map<String, Job> jobs = jenkins.getJobs();
		JobWithDetails job = jobs.get(jobName).details();
		Build buildByNumber = job.getBuildByNumber(number);
		long startTime = buildByNumber.details().getTimestamp();
		long duration = buildByNumber.details().getDuration();

		Map<String, Long> data = new HashMap<>();
		data.put("startTime", startTime);
		data.put("duration", duration);
		return data;
	}

	// 通过获取构建的最终的结果来判断最终的结果
	// 返回结果：SUCCESS， FAILURE
	public static String isSuccess(String jobName, int number) throws IOException {
		JenkinsServer jenkins = getJenkinsServer();
		Map<String, Job> jobs = jenkins.getJobs();
		JobWithDetails job = jobs.get(jobName).details();
		Build buildByNumber = job.getBuildByNumber(number);
		BuildWithDetails details = buildByNumber.details();
		return details.getResult().toString();
	}

	// 判断job是否执行完
	public static boolean isFinished(int number, String jobName) {
		boolean isBuilding = false;
		if (number <= 0) {
			throw new IllegalArgumentException("jodId must greater than 0!");
		}
		try {
			JenkinsServer jenkins = getJenkinsServer();
			Map<String, Job> jobs = jenkins.getJobs();
			JobWithDetails job = jobs.get(jobName).details();
			Build buildByNumber = job.getBuildByNumber(number);
			if (null != buildByNumber) {
				BuildWithDetails details = buildByNumber.details();
				if (null != details) {
					isBuilding = details.isBuilding();
				} else {
					isBuilding = true;
				}
			} else {
				isBuilding = true;
			}

			return !isBuilding;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return false;
	}

}
