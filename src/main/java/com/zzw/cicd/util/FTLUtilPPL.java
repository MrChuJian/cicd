package com.zzw.cicd.util;

import java.io.StringWriter;

import com.zzw.cicd.model.BuildTrigger;
import com.zzw.cicd.model.LogRotation;
import com.zzw.cicd.model.Vo.FtlVo;
import com.zzw.cicd.model.PPL;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FTLUtilPPL {

	private static Configuration cfg; // 模版配置对象

	private static void init() throws Exception {
		// 初始化FreeMarker配置
		// 创建一个Configuration实例
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		// 设置FreeMarker的模版文件夹位置
		cfg.setClassForTemplateLoading(FTLUtilPPL.class, "/META-INF/resources/ftl/");
	}

	public static String process(PPL ppl, BuildTrigger trigger, LogRotation logRotation, String pplScript)
			throws Exception {
		if (cfg == null) {
			FTLUtilPPL.init();
		}
		// 构造填充数据的Map
		FtlVo ftlVo = new FtlVo();
		if (null != trigger) {
			ftlVo.setHasTrigger(true);
			ftlVo.setSpec(trigger.getCron());
			ftlVo.setQuietPeriod(trigger.getQuietPeriod());
			// 由于模板不接受boolean写入，所以需要转换为字符
			if (trigger.getIgnoreHook()) {
				ftlVo.setIgnoreHook("true");
			} else {
				ftlVo.setIgnoreHook("false");
			}
			if (trigger.getPollScm()) {
				ftlVo.setHasSCMTrigger(trigger.getPollScm());

				ftlVo.setHasTimeTrigger(false);
			} else {
				ftlVo.setHasSCMTrigger(false);
				ftlVo.setHasTimeTrigger(true);
			}

		} else {
			ftlVo.setHasTrigger(false);
			ftlVo.setHasSCMTrigger(false);
			ftlVo.setHasSCMTrigger(false);
			ftlVo.setIgnoreHook("false");
			ftlVo.setSpec("");
			ftlVo.setQuietPeriod(0);
		}
		if (null != logRotation) {
			if (logRotation.getDaysToKeep() > 0) {
				ftlVo.setDaysToKeep(logRotation.getDaysToKeep());
			} else {
				ftlVo.setDaysToKeep(-1);
			}
			if (logRotation.getNumToKeep() > 0) {
				ftlVo.setNumToKeep(logRotation.getNumToKeep());
			} else {
				ftlVo.setNumToKeep(-1);
			}

		} else {
			ftlVo.setDaysToKeep(-1);
			ftlVo.setDaysToKeep(-1);
		}

		ftlVo.setDisplayName(ppl.getPipeLineName());

		ftlVo.setScript(pplScript);
		// 创建模版对象
		Template t = cfg.getTemplate("pplConfig.ftl");
		// 在模版上执行插值操作, 输出字符串
		StringWriter writer = new StringWriter();
		t.process(ftlVo, writer);
		return writer.toString();
	}

	/**
	 * 直接传入模板对象进行创建
	 * 
	 * @param ftlVo
	 * @return
	 * @throws Exception
	 */
	public static String processVo(FtlVo ftlVo) throws Exception {
		if (cfg == null) {
			FTLUtilPPL.init();
		}
		// 创建模版对象
		Template t = cfg.getTemplate("pplConfig.ftl");
		// 在模版上执行插值操作, 输出字符串
		StringWriter writer = new StringWriter();
		t.process(ftlVo, writer);
		return writer.toString();
	}
}