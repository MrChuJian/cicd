package com.zzw.cicd.model;

import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

abstract class BaseRequest<SELF extends BaseRequest> implements WsRequest {

	private final String path;

	private String mediaType = MediaTypes.JSON;

	// keep the same order -> do not use HashMap
	private final Map<String, String> params = new LinkedHashMap<>();

	BaseRequest(String path) {
		this.path = path;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getMediaType() {
		return mediaType;
	}

	/**
	 * Expected media type of response. Default is {@link MediaTypes#JSON}.
	 */
	public SELF setMediaType(String s) {
		requireNonNull(s, "media type of response cannot be null");
		this.mediaType = s;
		return (SELF) this;
	}

	public SELF setParam(String key, Object value) {
		checkArgument(!isNullOrEmpty(key), "a WS parameter key cannot be null");
		if (value != null) {
			this.params.put(key, value.toString());
		}
		return (SELF) this;
	}

	@Override
	public Map<String, String> getParams() {
		return params;
	}
}