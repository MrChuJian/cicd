package com.zzw.cicd.util;

import org.springframework.util.StringUtils;

public abstract class StringUtil extends StringUtils {

	public static boolean isNull(String str) {
		if (str == null || str.trim().length() <= 0)
			return true;
		else
			return false;
	}

	public static boolean isNotNull(String str) {
		return !isNull(str);
	}

	public static String[] split(String str) {
		if (StringUtil.isNull(str))
			return null;
		else
			return str.split("\\s*[,;]\\s*");
	}
}
