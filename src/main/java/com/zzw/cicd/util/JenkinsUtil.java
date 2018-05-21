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
				jenkinsInfo.setUrl("http://120.77.34.35:8001");

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

	public static JobConfigVo findJobById(String jobId) {
		try {
			JobWithDetails job = getJenkinsServer().getJob(jobId);
			GetRequest get = new GetRequest(path("job", jobId, "api", "json"));
			get.setParam("depth", 1);
			get.setParam("tree",
					"inQueue,buildable,nextBuildNumber,lastBuild[building,displayName,id,timestamp,number]");
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String msg = response.content();
			JobConfigVo v = JSONObject.parseObject(msg, JobConfigVo.class);
			v.setJobType(JSON.parseObject(msg).getString("_class"));
			return v;
		} catch (HttpException e1) {
			if (e1.code() == 404) {
				return null;
			}
			throw new RuntimeException(e1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean checkJob(String jobId) {
		if (null == jobId) {
			return false;
		}
		if (jobId.equalsIgnoreCase("")) {
			return false;
		}
		List<WsDescribeVo> list = getWsRuns(jobId);
		if (null == list) {
			List<JobBuild> bulds = getJobBuild(jobId);
			if (null == bulds) {
				return true;
			} else {
				for (JobBuild build : bulds) {
					// System.out.println(JSON.toJSONString(build));
					if (null != build && 0 < build.getNumber()) {
						if (null == getWsDescribe(jobId, "" + build.getNumber())) {
							return delBuildNum(jobId, "" + build.getNumber());
						}
					}
				}
			}
		}
		return false;
	}

	public static WsDescribeVo getWsDescribe(String jobId, String buildNo, String node) {
		try {
			GetRequest get = new GetRequest(path("job", jobId, buildNo, "wfapi", "describe"));
			if (node != null) {
				get = new GetRequest(path("job", jobId, buildNo, "execution", "node", node, "wfapi", "describe"));
			}
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String msg = response.content();
			WsDescribeVo v = JSONObject.parseObject(msg, WsDescribeVo.class);
			return v;
		} catch (Exception e) {
		}
		return null;
	}

	public static WsDescribeVo getWsDescribe(String jobId, String buildNo) {
		try {
			GetRequest get = new GetRequest(path("job", jobId, buildNo, "wfapi", "describe"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String msg = response.content();
			WsDescribeVo v = JSONObject.parseObject(msg, WsDescribeVo.class);
			return v;
		} catch (Exception e) {
		}
		return null;
	}

	public static StageLogVo getStageLog(String jobId, String buildNo, String node) {
		try {
			GetRequest get = new GetRequest(path("job", jobId, buildNo, "execution", "node", node, "wfapi", "log"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String msg = response.content();
			StageLogVo v = JSONObject.parseObject(msg, StageLogVo.class);
			return v;
		} catch (HttpException e1) {
			if (e1.code() == 404) {
				return null;
			}
			throw new RuntimeException(e1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static List<WsDescribeVo> getWsRuns(String jobId) {
		try {
			GetRequest get = new GetRequest(path("job", jobId, "wfapi", "runs"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String msg = response.content();
			return JSONObject.parseArray(msg, WsDescribeVo.class);
		} catch (HttpException e1) {

		} catch (Exception e) {

		}
		return null;
	}

	public static List<ChangesetVo> getChangesets(String href) {
		try {
			GetRequest get = new GetRequest(href);
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String msg = response.content();

			return JSONObject.parseArray(msg, ChangesetVo.class);
		} catch (HttpException e1) {
			if (e1.code() == 404) {
				return null;
			}
			throw new RuntimeException(e1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static JSONArray getChangesets(String jobId, String buildNo) {
		try {
			GetRequest get = new GetRequest(path("job", jobId, buildNo, "api", "json"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String msg = response.content();
			return JSONObject.parseObject(msg).getJSONArray("changeSets");
		} catch (HttpException e1) {
			if (e1.code() == 404) {
				return null;
			}
			throw new RuntimeException(e1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static List<ArtifactVo> getArtifacts(String jobId, String buildNo) {
		try {
			GetRequest get = new GetRequest(path("job", jobId, buildNo, "api", "json"));
			get.setParam("tree", "artifacts[*]");
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String msg = response.content();
			// System.out.println(msg);
			JSONObject obj = JSON.parseObject(msg);
			return JSON.parseArray(obj.getString("artifacts"), ArtifactVo.class);
		} catch (HttpException e1) {
			if (e1.code() == 404) {
				return null;
			}
			throw new RuntimeException(e1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 获取所有jenkins的jobs
	public static List<JenkinsJob> getJobs() {
		try {
			GetRequest get = new GetRequest(path("api",
					"json?pretty=true&tree=jobs[name,lastBuild[number],builds[number,building,result,timestamp,duration,actions[remoteUrls],changeSets[kind,revisions[module],items[commitId,timestamp,msg,author[fullName]]]]]"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get);
			if (response.isSuccessful()) {
				String msg = response.content();
				JSONObject obj = JSON.parseObject(msg);
				return JSON.parseArray(obj.getString("jobs"), JenkinsJob.class);
			}
		} catch (Exception e) {

		}
		return null;
	}

	// 获取所有jenkins的job的所有build
	public static List<JobBuild> getJobBuild(String jobId) {
		try {
			GetRequest get = new GetRequest(path("job", jobId, "api", "json"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get);
			if (response.isSuccessful()) {
				String msg = response.content();
				JSONObject obj = JSON.parseObject(msg);
				return JSON.parseArray(obj.getString("builds"), JobBuild.class);
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @author chenjinfan
	 * @Description 判断job是否存在
	 * @param jobName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static boolean isJobExists(String jobName) throws UnsupportedEncodingException {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		GetRequest get = new GetRequest(path("job", jobName));
		try {
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get);
			return response.isSuccessful();
		} catch (Exception e) {
		}
		return false;

	}

	public static boolean createJob(String jobName) throws UnsupportedEncodingException {
		boolean isJobExists = isJobExists(jobName);
		checkArgument(!isNullOrEmpty(jobName), "JobId is required when create job.");
		jobName = encodeUrlCompnent(jobName);
		if (false == isJobExists) {

			PostRequest post = new PostRequest("createItem") {
				@Override
				public boolean setRequestProperties(Request.Builder okRequestBuilder) {
					okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""));
					okRequestBuilder.header("Content-Type", "application/xml");
					return true;
				}
			};
			post.setParam("name", jobName);
			// hudson.model.FreeStyleProject
			// hudson.maven.MavenModuleSet
			post.setParam("mode", "org.jenkinsci.plugins.workflow.job.WorkflowJob");
			try {
				WsConnector connector = getConnector();
				WsResponse response = connector.call(post).failIfNotSuccessful();
				return response.isSuccessful();
			} catch (Exception e) {

			}
		}
		return isJobExists;
	}

	public static boolean updateJobConfig(String jobName, String configXml) throws UnsupportedEncodingException {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		boolean updateJobSuccess = false;
		if (false == isJobExists(jobName)) {
			createJob(jobName);
		}
		if (true == isJobExists(jobName)) {
			PostRequest post = new PostRequest(path("job", jobName, "config.xml")) {
				@Override
				public boolean setRequestProperties(Request.Builder okRequestBuilder) {
					okRequestBuilder.post(RequestBody.create(MediaType.parse("application/xml"), configXml));
					return true;
				}
			};
			try {
				WsConnector connector = getConnector();
				WsResponse response = connector.call(post).failIfNotSuccessful();
				updateJobSuccess = response.isSuccessful();
			} catch (Exception e) {
			}

		}
		return updateJobSuccess;
	}

	public static boolean delJob(String jobName) throws UnsupportedEncodingException {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		boolean delJobSuccess = false;
		if (true == isJobExists(jobName)) {
			PostRequest post = new PostRequest(path("job", jobName, "doDelete")) {
				@Override
				public boolean setRequestProperties(Request.Builder okRequestBuilder) {
					okRequestBuilder.post(RequestBody.create(MediaType.parse("application/xml"), ""));
					return true;
				}
			};
			try {
				WsConnector connector = getConnector();
				WsResponse response = connector.call(post).failIfNotSuccessful();
				delJobSuccess = response.isSuccessful();
			} catch (Exception e) {
			}
		}
		return delJobSuccess;
	}

	public static boolean delBuildNum(String jobName, String buildId) {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		boolean delJobSuccess = false;
		try {
			if (true == isJobExists(jobName)) {
				PostRequest post = new PostRequest(path("job", jobName, buildId, "doDelete")) {
					@Override
					public boolean setRequestProperties(Request.Builder okRequestBuilder) {
						okRequestBuilder.post(RequestBody.create(MediaType.parse("application/xml"), ""));
						return true;
					}
				};
				WsConnector connector = getConnector();
				WsResponse response = connector.call(post).failIfNotSuccessful();
				delJobSuccess = response.isSuccessful();
			}
		} catch (UnsupportedEncodingException e) {

		} catch (Exception e) {
		}
		return delJobSuccess;
	}

	public static boolean getPPLStatus(String pplId) throws UnsupportedEncodingException {
		JobConfigVo pplStatus = findJobById(pplId);
		return pplStatus.getBuildAble();

	}

	public static boolean changePPLStatus(String pplId) throws UnsupportedEncodingException {
		return true;
	}

	public static boolean setPPLStatus(String pplId, String pplStatus) throws UnsupportedEncodingException {
		return true;
	}

	public static String build(String jobId) throws Exception {
		checkArgument(!isNullOrEmpty(jobId), "JobId is required.");
		jobId = encodeUrlCompnent(jobId);
		PostRequest post = new PostRequest(path("job", jobId, "build"));
		post.setParam("delay", "0sec");
		WsConnector connector = getConnector();
		WsResponse response = connector.call(post).failIfNotSuccessful();
		if (201 == response.code()) {
			String url = response.header("location");
			return url;
		}
		return "";
	}

	public static String genBuildUrl(String jobName, int number) throws UnsupportedEncodingException {
		// return path(ConfigUtil.getConfigString("jenkins.serverUri"), "job",
		// encodeUrlCompnent(jobName), number + "");
		return "";
	}

	/**
	 * 获取日志（文本格式）
	 * 
	 * @author chenjinfan
	 * @Description
	 * @param jobName
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public static String getConsole(String jobName, String number) throws Exception {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		GetRequest get = new GetRequest(path("job", jobName, number, "consoleText"));
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		return response.content();
	}

	/**
	 * @author chenjinfan
	 * @Description 增量获取日志（html格式�?
	 * @param buildUrl
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, Object> getLogStream(String jobName, Integer number, Integer start)
			throws HttpException, UnsupportedEncodingException {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		checkArgument(number != null, "Number is required.");
		String buildUrl = genBuildUrl(jobName, number);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNotNull(buildUrl)) {
			buildUrl += "/logText/progressiveHtml";
			GetRequest get = new GetRequest(buildUrl);
			get.setParam("start", start);

			try {
				WsConnector connector = getConnector();
				WsResponse response = connector.call(get).failIfNotSuccessful();
				String hasMoreString = response.header("X-More-Data");
				boolean hasMore = Boolean.parseBoolean(hasMoreString);
				String lineNumber = response.header("X-Text-Size");
				String conString = response.content();
				map.put("hasMore", hasMore);
				map.put("lineNumber", lineNumber);
				map.put("log", conString);
			} catch (Exception e) {

			}
		}
		return map;
	}

	public static void download(String jobName, String number, String path, OutputStream out) throws Exception {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		checkArgument(number != null, "number is required.");
		checkArgument(!isNullOrEmpty(path), "path is required.");
		GetRequest get = new GetRequest(path("job", jobName, number.toString(), "artifact", path));
		writeRequestToOutput(out, get);
	}

	private static void writeRequestToOutput(OutputStream out, GetRequest get) throws Exception {
		WsConnector connector = getConnector();
		InputStream contentStream = connector.call(get).failIfNotSuccessful().contentStream();
		byte[] buffer = new byte[10 * 1024];
		int c = 0;
		while ((c = contentStream.read(buffer)) > 0) {
			out.write(buffer, 0, c);
			out.flush();
		}
	}

	/**
	 * @author zhongshiliang
	 * @Description 获取所有slave
	 * @return slave信息
	 * @throws UnsupportedEncodingException
	 */
	public static List<Map<String, Object>> getNodes() {
		JSONObject nodes = null;
		GetRequest get = new GetRequest(path("computer", "api", "json?pretty=true"));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			nodes = JSONObject.parseObject(response.content());

			for (int i = 0; i < nodes.getJSONArray("computer").size(); i++) {
				JSONObject node = (JSONObject) nodes.getJSONArray("computer").get(i);
				JSONObject monitorData = (JSONObject) node.get("monitorData");

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", node.get("displayName"));
				map.put("jnlp", node.get("jnlpAgent"));
				map.put("executors", node.get("numExecutors"));
				map.put("temporarilyOffline", node.get("temporarilyOffline"));
				map.put("offline", node.get("offline"));
				map.put("OS", monitorData.get("hudson.node_monitors.ArchitectureMonitor"));
				list.add(map);
			}
		} catch (Exception e) {

		}
		return list;
	}

	public static Map<String, Object> getNode(String nodeName) {

		WsResponse response;
		try {
			GetRequest get = new GetRequest(path("computer", nodeName, "api", "json"));
			WsConnector connector = getConnector();
			response = connector.call(get).failIfNotSuccessful();
		} catch (Exception e) {
			return null;
		}
		JSONObject node = JSONObject.parseObject(response.content());
		JSONObject monitorData = (JSONObject) node.get("monitorData");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", node.get("displayName"));
		map.put("jnlp", node.get("jnlpAgent"));
		map.put("executors", node.get("numExecutors"));
		map.put("temporarilyOffline", node.get("temporarilyOffline"));
		map.put("offline", node.get("offline"));
		map.put("OS", monitorData.get("hudson.node_monitors.ArchitectureMonitor"));

		return map;
	}

	public static int nodeStatus(String nodeName) {
		int nodeStatus = 0;
		Map<String, Object> nodeMap = getNode(nodeName);
		if (nodeMap == null) {
			return 11;
		}
		if ((boolean) nodeMap.get("temporarilyOffline")) {
			nodeStatus = nodeStatus + 0;
		} else {
			nodeStatus = nodeStatus + 1;
		}
		if ((boolean) nodeMap.get("offline")) {
			nodeStatus = nodeStatus + 0;
		} else {
			nodeStatus = nodeStatus + 10;
		}
		if (nodeStatus > 0) {
			return nodeStatus;
		}
		return nodeStatus;
	}

	public static Map<String, Object> getNodeLabel(String nodeName) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!nodeName.equalsIgnoreCase("master")) {
			GetRequest get = new GetRequest(path("computer", nodeName, "config.xml"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String configXml = response.content();

			String label = "";
			String remoteFS = "";
			String ip = "";
			String port = "";
			String credentialsId = "";

			if (configXml.contains("<label>") && configXml.contains("</label>")) {
				int start = configXml.indexOf("<label>");
				int end = configXml.indexOf("</label>");
				label = configXml.substring(start + 7, end);

			}
			if (configXml.contains("<remoteFS>") && configXml.contains("</remoteFS>")) {
				int start = configXml.indexOf("<remoteFS>");
				int end = configXml.indexOf("</remoteFS>");
				remoteFS = configXml.substring(start + 10, end);

			}
			if (configXml.contains("<host>") && configXml.contains("</host>")) {
				int start = configXml.indexOf("<host>");
				int end = configXml.indexOf("</host>");
				remoteFS = configXml.substring(start + 6, end);

			}
			if (configXml.contains("<port>") && configXml.contains("</port>")) {
				int start = configXml.indexOf("<port>");
				int end = configXml.indexOf("</port>");
				remoteFS = configXml.substring(start + 6, end);

			}
			if (configXml.contains("<credentialsId>") && configXml.contains("</credentialsId>")) {
				int start = configXml.indexOf("<credentialsId>");
				int end = configXml.indexOf("</credentialsId>");
				remoteFS = configXml.substring(start + "<credentialsId>".length(), end);

			}
			map.put("label", label);
			map.put("remoteFS", remoteFS);
			map.put("ip", ip);
			map.put("port", port);
			map.put("credentialsId", credentialsId);
		}

		return map;

	}

	// public static boolean createNode(JenkinsNodeVo vo) throws Exception {
	// if (vo.getType().equalsIgnoreCase("hudson.slaves.DumbSlave")) {
	//
	// } else {
	// return false;
	// }
	//
	// if (null == getNode(vo.getName())) {
	// PostRequest post = new PostRequest("computer/doCreateItem") {
	// @Override
	// public boolean setRequestProperties(Request.Builder okRequestBuilder) {
	// okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
	// ""));
	// return true;
	// }
	// };
	// String msg = JSONObject.toJSONString(vo);
	// post.setParam("name", vo.getName());
	// post.setParam("type", vo.getType());
	// post.setParam("json", msg);
	// // System.out.println("************333=" + msg);
	// WsConnector connector = getConnector();
	// WsResponse response = connector.call(post).failIfNotSuccessful();
	// return response.isSuccessful();
	// }
	// return false;
	// }

	// public static boolean updateNode(JenkinsNodeVo vo) {
	// String url = path("computer", vo.getName(), "configSubmit");
	// PostRequest post = new PostRequest(url) {
	// @Override
	// public boolean setRequestProperties(Request.Builder okRequestBuilder) {
	// okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
	// ""));
	// return true;
	// }
	// };
	// String msg = JSONObject.toJSONString(vo);
	// post.setParam("type", vo.getType());
	// post.setParam("json", msg);
	//
	// try {
	// WsConnector connector = getConnector();
	// WsResponse response = connector.call(post).failIfNotSuccessful();
	// return response.isSuccessful();
	// } catch (Exception e) {
	//
	// }
	// return false;
	// }

	public static boolean deleteNode(String nodeName) throws Exception {
		if (null != getNode(nodeName)) {
			PostRequest post = new PostRequest(path("computer", nodeName, "doDelete")) {
				@Override
				public boolean setRequestProperties(Request.Builder okRequestBuilder) {
					okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""));
					return true;
				}
			};
			WsConnector connector = getConnector();
			WsResponse response = connector.call(post).failIfNotSuccessful();
			return response.isSuccessful();
		}
		return true;
	}

	public static boolean toggleOffline(String nodeName) {
		PostRequest post = new PostRequest(path("computer", nodeName, "toggleOffline")) {
			@Override
			public boolean setRequestProperties(Request.Builder okRequestBuilder) {
				okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""));
				return true;
			}
		};

		try {
			WsConnector connector = getConnector();
			post.setParam("json", "{\"offlineMessage\": \"\"}");
			WsResponse response = connector.call(post).failIfNotSuccessful();
			return response.isSuccessful();
		} catch (Exception e) {
		}
		return false;

	}

	public static String getAgentRunSecret(String nodeName) {
		try {
			GetRequest get = new GetRequest(path("computer", nodeName, "/"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String msg = response.content();
			Pattern pattern = Pattern.compile("(?<=-secret ).*?(?=</pre>)");
			Matcher matcher = pattern.matcher(msg);
			if (matcher.find()) {
				return matcher.group();
			} else {
				return null;
			}
		} catch (HttpException e1) {
			if (e1.code() == 404) {
				return null;
			}
			throw new RuntimeException(e1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @author zhongshiliang
	 * @Description 获取指定job的lastbuild记录,jenkins提供的接口可以获取为xml，json，python,
	 *              这里通过json获取.
	 * @param jobName
	 * @return json格式的lastBuild信息
	 * @throws UnsupportedEncodingException
	 */
	/**
	 * public static JSONObject getLastBuild(String jobName) throws
	 * UnsupportedEncodingException { checkArgument(!isNullOrEmpty(jobName),
	 * "Name is required."); JSONObject lastBuildJson = null ; jobName =
	 * encodeUrlCompnent(jobName); if (true == isJobExists(jobName)) {
	 * GetRequest get = new GetRequest(path("job", jobName, "lastBuild", "api",
	 * "json")); WsConnector connector = getConnector(); WsResponse response =
	 * connector.call(get).failIfNotSuccessful(); lastBuildJson =
	 * JSONObject.parseObject(response.content()); }
	 * 
	 * return lastBuildJson; }
	 * 
	 * @throws Exception
	 **/
	public static JSONObject getBuildByID(String jobName, String buildID) throws Exception {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		JSONObject lastBuildJson = null;
		jobName = encodeUrlCompnent(jobName);
		if (true == isJobExists(jobName)) {
			GetRequest get = new GetRequest(path("job", jobName, buildID, "api", "json"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			lastBuildJson = JSONObject.parseObject(response.content());
		}

		return lastBuildJson;
	}

	public static WsConnector getConnector() throws Exception {
		return getConnector(true);
	}

	/**
	 * public static String getLastBuildID(String jobName) throws
	 * UnsupportedEncodingException { checkArgument(!isNullOrEmpty(jobName),
	 * "Name is required."); jobName = encodeUrlCompnent(jobName); GetRequest
	 * get = new GetRequest(path("job", jobName, "lastBuild", "api", "xml"));
	 * WsConnector connector = getConnector(); WsResponse response =
	 * connector.call(get).failIfNotSuccessful(); return response.content(); }
	 * 
	 * @throws Exception
	 **/

	private static WsConnector getConnector(boolean isRedirct) throws Exception {
		/*
		 * String url = ""; String login = ""; String password = ""; if (null ==
		 * jenkinsInfo | (null != jenkinsInfo && (null == jenkinsInfo.getUrl() |
		 * null == jenkinsInfo.getUsername() | null == jenkinsInfo.getUrl()))) {
		 * // url = ConfigUtil.getConfigString("jenkins.serverUri"); // login =
		 * ConfigUtil.getConfigString("jenkins.username"); // password =
		 * ConfigUtil.getConfigString("jenkins.password"); } else { url =
		 * jenkinsInfo.getUrl(); login = jenkinsInfo.getUsername(); password =
		 * jenkinsInfo.getPassword(); }
		 * 
		 * // System.out.println("***********=" + url); Builder builder = null;
		 * 
		 * builder.url(url); builder.credentials(login, password); WsConnector
		 * wsConnector = builder.build(isRedirct);
		 * 
		 * GetRequest get = new GetRequest(path("manage")); if (200 ==
		 * wsConnector.call(get).code()) { return wsConnector; } else if (401 ==
		 * wsConnector.call(get).code()) { throw new
		 * Exception("jenkins用户名或者密码错误"); } else { throw new
		 * Exception("jenkins用户名或者密码错误"); }
		 */
		return null;
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

	/**
	 * @author zhongshiliang
	 * @Description 获取指定已经安装的插�?
	 * @return 已安装插件信�?
	 * @throws Exception
	 */

	public static List<Plugins> getInstalledPlugins() throws Exception {
		List<Plugins> pluginList = new ArrayList<Plugins>();
		if (200 == masterStatus()) {
			GetRequest get = new GetRequest(path("pluginManager", "api", "json?pretty=true&depth=2"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get);
			// System.out.println(response.code() + "," +
			// response.isSuccessful());

			if (response.isSuccessful()) {
				pluginList = JSON.parseObject(
						JSONObject.parseObject(response.content()).getJSONArray("plugins").toString(),
						new TypeReference<List<Plugins>>() {
						});
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for (Plugins plugin : pluginList) {
					String pluginName = plugin.getShortName();
					List<PluginDependencies> dependencies = plugin.getDependencies();
					for (PluginDependencies dependeny : dependencies) {
						String depName = dependeny.getShortName();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("pluginName", pluginName);
						map.put("depName", depName);

						list.add(map);
					}
				}
				for (Plugins plugin : pluginList) {
					String pluginName = plugin.getShortName();
					String depBy = "";
					for (Map<String, Object> depMap : list) {
						if (depMap.get("depName").toString().equalsIgnoreCase(pluginName)) {
							depBy = depMap.get("pluginName").toString() + "," + depBy;
						}
					}
					plugin.setDepBy(depBy);
				}
			}
		}
		return pluginList;
		/**
		 * System.out.println(JSON.toJSONString(list1)); List<Plugins> list =
		 * new ArrayList<Plugins>(); for (int i = 0; i <
		 * installedPlugins.getJSONArray("plugins").size(); i++) { JSONObject
		 * pluginJson = (JSONObject)
		 * installedPlugins.getJSONArray("plugins").get(i); Plugins plugin = new
		 * Plugins(); plugin.setActive((boolean)pluginJson.get("active"));
		 * plugin.setBackupVersion((String)pluginJson.get("backupVersion"));
		 * plugin.setBundled((boolean)pluginJson.get("bundled"));
		 * plugin.setDeleted((boolean)pluginJson.get("deleted"));
		 * plugin.setDowngradable((boolean)pluginJson.get("downgradable"));
		 * plugin.setEnabled((boolean)pluginJson.get("enabled"));
		 * plugin.setHasUpdate((boolean)pluginJson.get("hasUpdate"));
		 * plugin.setLongName((String)pluginJson.get("longName"));
		 * plugin.setPinned((boolean)pluginJson.get("pinned"));
		 * plugin.setRequiredCoreVersion((String)pluginJson.get(
		 * "requiredCoreVersion"));
		 * plugin.setShortName((String)pluginJson.get("shortName"));
		 * plugin.setSupportsDynamicLoad((String)pluginJson.get(
		 * "supportsDynamicLoad"));
		 * plugin.setUrl((String)pluginJson.get("url"));
		 * plugin.setVersion((String)pluginJson.get("version"));
		 * 
		 * List<PluginDependencies> dependencies = new ArrayList
		 * <PluginDependencies>(); for (int depNum = 0; depNum <
		 * pluginJson.getJSONArray("dependencies").size(); depNum++) {
		 * //System.out.println(pluginJson.getJSONArray("dependencies"));
		 * JSONObject dependenciesJson = (JSONObject)
		 * pluginJson.getJSONArray("dependencies").get(depNum);
		 * PluginDependencies dependeny = new PluginDependencies();
		 * dependeny.setOptional((boolean)dependenciesJson.get("optional"));
		 * dependeny.setShortName((String)dependenciesJson.get("shortName"));
		 * dependeny.setVersion((String)dependenciesJson.get("version"));
		 * dependencies.add(dependeny); } list.add(plugin);
		 * 
		 * } return list;
		 **/
	}

	/**
	 * @author zhongshiliang
	 * @Description 获取指可升级的插�?
	 * @return 可升级的插件信息
	 * @throws Exception
	 */

	public static List<Map<String, Object>> getUpdatePlugins() throws Exception {
		JSONObject plugins = null;
		GetRequest get = new GetRequest(path("updateCenter", "api", "json?pretty=true&depth=2"));
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		plugins = JSONObject.parseObject(response.content());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// System.out.println(plugins.getJSONArray("sites").size());
		for (int i = 0; i < plugins.getJSONArray("sites").size(); i++) {
			JSONObject sites = plugins.getJSONArray("sites").getJSONObject(i);
			// System.out.println("****=" + i + sites.getJSONArray("updates"));
			for (int j = 0; j < sites.getJSONArray("updates").size(); j++) {
				JSONObject plugin = (JSONObject) sites.getJSONArray("updates").getJSONObject(j);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", plugin.get("name"));
				map.put("version", plugin.get("version"));
				list.add(map);
			}
		}

		return list;
	}

	public static List<Map<String, Object>> getInstallingPlugins() throws Exception {
		JSONObject plugins = null;
		GetRequest get = new GetRequest(path("updateCenter", "api", "json?pretty=true&depth=2"));
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		plugins = JSONObject.parseObject(response.content());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// System.out.println(plugins.getJSONArray("sites").size());
		for (int i = 0; i < plugins.getJSONArray("jobs").size(); i++) {
			JSONObject jobs = plugins.getJSONArray("jobs").getJSONObject(i);
			// System.out.println("****=" + i + jobs);
			if (null != jobs.getJSONObject("plugin")) {
				// System.out.println("****=" + i +
				// jobs.getJSONObject("plugin"));
				JSONObject plugin = (JSONObject) jobs.getJSONObject("plugin");
				// System.out.println("****=" + i + plugin);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", plugin.get("name"));
				map.put("version", plugin.get("version"));
				list.add(map);
			}

		}

		return list;
	}

	/**
	 * @author zhongshiliang
	 * @Description 获取所有插�?
	 * @return 插件信息
	 * @throws Exception
	 */

	public static List<Map<String, Object>> getAvailablePlugins() throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (200 == masterStatus()) {
			JSONObject plugins = null;
			GetRequest get = new GetRequest(path("updateCenter", "api", "json?pretty=true&depth=2"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			plugins = JSONObject.parseObject(response.content());

			for (int i = 0; i < plugins.getJSONArray("availables").size(); i++) {
				JSONObject plugin = (JSONObject) plugins.getJSONArray("availables").get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", plugin.get("name"));
				map.put("version", plugin.get("version"));
				map.put("excerpt", plugin.get("excerpt"));
				list.add(map);

			}
		}
		return list;
	}

	/**
	 * @author zhongshiliang
	 * @Description 安装插件
	 * @return 安装是否成功
	 * @throws Exception
	 */
	public static boolean installingPlugin(String pluginName, boolean isUpdate) throws Exception {

		String pluginVer = "current";
		boolean hasUpdate = false;
		List<Map<String, Object>> installingPlugins = getInstallingPlugins();
		for (Map<String, Object> installingPlugin : installingPlugins) {
			// System.out.println(installingPlugin);
			if (pluginName.equalsIgnoreCase(installingPlugin.get("name").toString())) {
				return true;
			}
		}
		if (isUpdate) {
			List<Map<String, Object>> updatePlugins = getUpdatePlugins();
			for (Map<String, Object> updatePlugin : updatePlugins) {
				// System.out.println(JSON.toJSONString(updatePlugin));
				if (updatePlugin.get("name").toString().equals(pluginName)) {
					hasUpdate = true;
					pluginVer = updatePlugin.get("version").toString();
				}
			}
		}
		if ((hasUpdate && isUpdate) | (!hasUpdate && !isUpdate)) {
			if (null == pluginName | pluginName.equalsIgnoreCase("")) {
				return false;
			} else {
				String configXml = "<jenkins><install plugin=\"" + pluginName + "@" + pluginVer + "\"/></jenkins>";
				// System.out.println("**********=" + configXml);
				PostRequest post = new PostRequest(path("pluginManager", "installNecessaryPlugins")) {
					@Override
					public boolean setRequestProperties(Request.Builder okRequestBuilder) {
						okRequestBuilder.post(RequestBody.create(MediaType.parse("application/xml"), configXml));
						return true;
					}
				};
				WsConnector connector = getConnector();
				WsResponse response = connector.call(post).failIfNotSuccessful();

				return response.isSuccessful();
			}
		}
		return false;
	}

	public static Map<String, Object> getAvailablePlugin(String name) {
		List<Map<String, Object>> availablePlugin = new ArrayList<Map<String, Object>>();
		try {
			availablePlugin = JenkinsUtil.getAvailablePlugins();
		} catch (Exception e) {
		}
		for (int j = 0; j < availablePlugin.size(); j++) {
			if (availablePlugin.get(j).get("name").toString().equalsIgnoreCase(name)) {
				return availablePlugin.get(j);
			}
		}
		return null;
	}

	public static Plugins getInstalledPlugin(String name) {
		List<Plugins> pluginList = new ArrayList<Plugins>();
		try {
			pluginList = JenkinsUtil.getInstalledPlugins();
		} catch (Exception e) {
		}
		for (Plugins plugin : pluginList) {
			if (plugin.getShortName().equalsIgnoreCase(name)) {
				return plugin;
			}
		}
		return null;
	}

	public static boolean delPlugin(String name) throws Exception {
		Plugins plugin = getInstalledPlugin(name);
		// System.out.println(JSON.toJSONString(credentialVo.getCredentialId()));
		if (null != plugin) {
			PostRequest post = new PostRequest(path("pluginManager/plugin", name, "doUninstall")) {
				@Override
				public boolean setRequestProperties(Request.Builder okRequestBuilder) {
					okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""));
					return true;
				}
			};
			WsConnector connector = getConnector();
			WsResponse response = connector.call(post);
			boolean delPluginSuccess = response.isSuccessful();
			if (delPluginSuccess) {
				// 卸载插件要重启jenkins才能生效
				// safeRestart();
			}
			return delPluginSuccess;
		}
		return false;
	}

	/**
	 * @author zhongshiliang
	 * @Description 获取所有密�?
	 * @return list 密钥
	 * @throws UnsupportedEncodingException
	 */

	public static List<CredentialVo> getCredentials() {
		JSONObject credentials = null;
		GetRequest get = new GetRequest(path("credentials/store/system/domain/_/api/json?pretty=true&depth=3"));
		List<CredentialVo> list = new ArrayList<CredentialVo>();
		try {
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get);
			if (response.isSuccessful()) {
				credentials = JSONObject.parseObject(response.content());

				for (int i = 0; i < credentials.getJSONArray("credentials").size(); i++) {
					JSONObject plugin = (JSONObject) credentials.getJSONArray("credentials").get(i);
					CredentialVo credentialVo = new CredentialVo();
					credentialVo.setCredentialId(plugin.get("id").toString());
					credentialVo.setCredentialType(plugin.get("typeName").toString());
					credentialVo.setCredentialDesc(plugin.get("description").toString());

					list.add(credentialVo);

				}
			}
		} catch (Exception e) {

		}

		return list;
	}

	/**
	 * @author zhongshiliang
	 * @Description 获取单个密钥
	 * @return list 密钥
	 * @throws UnsupportedEncodingException
	 */

	public static CredentialVo getCredential(String id) {
		List<CredentialVo> list = getCredentials();
		CredentialVo credentialVo = new CredentialVo();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCredentialId().equalsIgnoreCase(id)) {
				credentialVo = list.get(i);
			}
		}
		return credentialVo;
	}

	public static boolean saveCredential(CredentialVo vo) throws Exception {
		// System.out.println("************=" + vo.getCredentialId());

		String url = path("credentials", "store", "system", "domain", "_", "createCredentials");
		PostRequest post = new PostRequest(url) {
			@Override
			public boolean setRequestProperties(Request.Builder okRequestBuilder) {
				okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""));
				return true;
			}
		};
		String msg = "";
		if (vo.getCredentialType().toString().equalsIgnoreCase("Username with password")) {
			msg = "{\"credentials\":{\"scope\":\"GLOBAL\",\"id\":\"";
			msg = msg + vo.getCredentialId();
			msg = msg + "\",\"username\":\"";
			msg = msg + vo.getCredentialUser();
			msg = msg + "\",\"password\":\"";
			msg = msg + vo.getCredentialKey();
			msg = msg + "\",\"description\":\"";
			msg = msg + vo.getCredentialDesc();
			msg = msg + "\",\"$class\":";
			msg = msg + "\"com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl\"}}";
		}
		if (vo.getCredentialType().toString().equalsIgnoreCase("SSH Username with private key")) {
			if (null == vo.getPrivateKey()) {
				return false;
			}
			msg = "{\"credentials\":{\"scope\":\"GLOBAL\",\"id\":\"";
			msg = msg + vo.getCredentialId();
			msg = msg + "\",\"username\":\"";
			msg = msg + vo.getCredentialUser();
			msg = msg + "\",\"password\":\"";
			msg = msg + "";
			msg = msg + "\",\"description\":\"";
			msg = msg + vo.getCredentialDesc();
			msg = msg + "\",\"stapler-class\":";
			msg = msg + "\"com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey\"";
			msg = msg + ",\"privateKeySource\":{";
			msg = msg + "\"stapler-class\":";
			msg = msg
					+ "\"com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey$DirectEntryPrivateKeySource\"";
			msg = msg + ",\"privateKey\":";
			msg = msg + JSON.toJSONString(vo.getPrivateKey());
			msg = msg + "}}}";
		}

		post.setParam("json", msg);
		WsConnector connector = getConnector();

		WsResponse response = connector.call(post);
		if (null != getCredential(vo.getCredentialId())) {
			return true;
		}
		return false;
	}

	public static boolean delCredential(String id) throws Exception {
		CredentialVo credentialVo = getCredential(id);
		// System.out.println(JSON.toJSONString(credentialVo.getCredentialId()));
		if (null != credentialVo.getCredentialId()) {
			PostRequest post = new PostRequest(path("credentials/store/system/domain/_/credential", id, "doDelete")) {
				@Override
				public boolean setRequestProperties(Request.Builder okRequestBuilder) {
					okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""));
					return true;
				}
			};
			WsConnector connector = getConnector();
			WsResponse response = connector.call(post).failIfNotSuccessful();
			return response.isSuccessful();
		}
		return false;

	}

	public static boolean needRestart() {
		try {
			JSONObject plugins = null;
			GetRequest get = new GetRequest(path("updateCenter", "api", "json?pretty=true"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			plugins = JSONObject.parseObject(response.content());
			return (boolean) plugins.get("restartRequiredForCompletion");
		} catch (Exception e) {

		}
		return false;
	}

	public static boolean safeRestart() {
		try {
			PostRequest post = new PostRequest(path("", "safeRestart")) {
				@Override
				public boolean setRequestProperties(Request.Builder okRequestBuilder) {
					okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""));
					return true;
				}
			};
			WsConnector connector = getConnector();
			WsResponse response = connector.call(post);
			// System.out.println(response.code());
			if (503 == response.code()) {
				return true;
			}
		} catch (Exception e) {

		}
		return false;

	}

	public static int masterStatus() {
		/**
		 * 503: "master正在重启" 401: "master正在重启,或者用户名/密码错误" 200:"master状态正�?
		 * -1:"master服务不可获取"
		 **/
		try {
			GetRequest get = new GetRequest(path("manage"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get);
			// System.out.println(response.code() + ";" + response.content());
			return response.code();

		} catch (Exception e) {
			return -1;
		}

	}

	public static JSONObject getCobertura(String jobName, String buildNum) {
		JSONObject utReport = new JSONObject();

		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		// System.out.println("************=" + utReport.toJSONString());
		try {
			jobName = encodeUrlCompnent(jobName);
			GetRequest get = new GetRequest(
					path("job", jobName, buildNum, "cobertura/api/json?pretty=true&depth=999999"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			utReport = (JSONObject) JSONObject.parseObject(response.content()).get("results");
		} catch (Exception e) {
		}

		return utReport;

	}

	public static JSONObject getJacoco(String jobName, String buildNum) {
		JSONObject utReport = new JSONObject();
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		try {
			jobName = encodeUrlCompnent(jobName);
			GetRequest get = new GetRequest(path("job", jobName, buildNum, "jacoco/api/json?pretty=true"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			utReport = (JSONObject) JSONObject.parseObject(response.content());
		} catch (Exception e) {
		}
		return utReport;
	}

	public static JSONObject getCsharpCobertura(String jobName, String buildNum) {
		JSONObject utReport = new JSONObject();
		// checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		try {
			jobName = encodeUrlCompnent(jobName);
			List<ArtifactVo> ArtifactList = JenkinsUtil.getArtifacts(jobName, buildNum);
			String realPath = "";
			for (ArtifactVo vo : ArtifactList) {
				if (vo.getRelativePath().contains("/test/ut/") && vo.getRelativePath().endsWith(".xml")) {
					realPath = vo.getRelativePath();
					break;
				}
			}
			if (!"".equals(realPath)) {
				GetRequest get = new GetRequest(JenkinsUtil.path("job", jobName, buildNum, "artifact", realPath));
				WsConnector connector = getConnector();
				WsResponse response = connector.call(get).failIfNotSuccessful();
				String tsXml = response.content();
				String tsJson = "";
				if (isXML(tsXml)) {
					tsJson = XML.toJSONObject(tsXml).toString(4);
					utReport = (JSONObject) JSON.parseObject(tsJson).get("results");
				}
			}

		} catch (Exception e) {
		}

		return utReport;

	}

	public static JSONObject getTestReport(String jobName, String buildNum) {
		JSONObject testReport = new JSONObject();
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		// System.out.println("************=" + testReport.toJSONString());
		try {
			jobName = encodeUrlCompnent(jobName);
			GetRequest get = new GetRequest(path("job", jobName, buildNum, "testReport/api/json?pretty=true"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			testReport = (JSONObject) JSONObject.parseObject(response.content());
		} catch (Exception e) {
		}

		return testReport;

	}

	public static String getDockerImageName(String jobId, String buildNo) {
		try {

			GetRequest get = new GetRequest(path("job", jobId, buildNo, "consoleText"));
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String msg = response.content();
			String s[] = msg.split("\n");

			String dockerIndex = "docker push ";
			for (int i = 0; i < s.length; i++) {
				if (s[i].contains(dockerIndex)) {
					return s[i].substring(s[i].indexOf(dockerIndex) + dockerIndex.length(), s[i].length());
				}
			}
		} catch (HttpException e1) {
			return "";
		} catch (Exception e) {
			return "";
		}
		return "";
	}

	public static boolean setNextBuildNum(String jobId, int buildNo) {
		try {
			PostRequest post = new PostRequest(path("job", jobId, "nextbuildnumber", "submit")) {
				@Override
				public boolean setRequestProperties(Request.Builder okRequestBuilder) {
					okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
							"nextBuildNumber=" + buildNo));
					return true;
				}
			};
			WsConnector connector = getConnector();
			WsResponse response = connector.call(post);

			// System.out.println(JSON.toJSONString(response));
			return response.isSuccessful();
		} catch (Exception e) {

		}
		return false;

	}

	public static boolean isXML(String value) {
		try {
			DocumentHelper.parseText(value);
		} catch (DocumentException e) {
			return false;
		}
		return true;
	}

	public static Result doReplay(String jobName, String mainScript) {
		Result result = new Result();
		if (null == mainScript) {
			result.setResult(false);
			result.setMsg("输入参数mainScript不能为null");
			return result;
		}
		if (null == jobName) {
			result.setResult(false);
			result.setMsg("输入参数jobName不能为null");
			return result;
		}
		JobConfigVo vo = findJobById(jobName);

		if (null == vo) {
			result.setResult(false);
			result.setMsg("jenkins不存在此工程");
			return result;
		}
		if (null == vo.getLastBuild()) {
			result.setResult(false);
			result.setMsg("jenkins该工程未有构建");
			return result;
		}
		if (null == vo.getJobType()) {
			result.setResult(false);
			result.setMsg("此jenkins工程不是pipeline类型，不能执行replay");
			return result;
		}
		if (vo.getJobType().equalsIgnoreCase("")
				| !vo.getJobType().equalsIgnoreCase("org.jenkinsci.plugins.workflow.job.WorkflowJob")) {
			result.setResult(false);
			result.setMsg("此jenkins工程不是pipeline类型，不能执行replay");
			return result;
		}

		String scriptCfg = "{\"mainScript\":\"" + mainScript + "\"}";
		String url = path("job", jobName, "lastBuild/replay/run");

		try {
			PostRequest post = new PostRequest(url) {
				@Override
				public boolean setRequestProperties(Request.Builder okRequestBuilder) {
					okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""));
					return true;
				}
			};
			WsConnector connector = getConnector();
			post.setParam("json", scriptCfg);
			WsResponse response = connector.call(post);
			if (!response.isSuccessful()) {

				JobConfigVo newVo = findJobById(jobName);
				if (null == newVo) {
					result.setResult(false);
					result.setMsg("jenkins不存在此工程");
					return result;
				}
				if (null == newVo.getLastBuild()) {
					result.setResult(false);
					result.setMsg("未启动replay构建");
					return result;
				}
				if (newVo.getLastBuild() == vo.getLastBuild()) {
					result.setResult(false);
					result.setMsg("未启动replay构建");
					return result;
				}
			}
		} catch (Exception e) {
			result.setResult(false);
			result.setMsg(e.getMessage());
			return result;
		} finally {

		}

		result.setResult(true);
		return result;

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
		    if(isBuilding) {
		    	System.out.println("building,");
		    } else {
		    	System.out.println("builded,");
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
