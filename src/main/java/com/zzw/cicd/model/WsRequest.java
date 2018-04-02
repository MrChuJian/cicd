package com.zzw.cicd.model;

import java.util.Map;

public interface WsRequest {

	Method getMethod();

	String getPath();

	String getMediaType();

	Map<String, String> getParams();

	enum Method {
		GET, POST, PUT, DELETE
	}
}