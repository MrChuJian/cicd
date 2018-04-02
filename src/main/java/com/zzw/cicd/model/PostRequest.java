package com.zzw.cicd.model;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Request;

public class PostRequest extends BaseRequest<PostRequest> {

	private final Map<String, Part> parts = new LinkedHashMap<>();

	public PostRequest(String path) {
		super(path);
	}

	@Override
	public Method getMethod() {
		return Method.POST;
	}

	public PostRequest setPart(String name, Part part) {
		this.parts.put(name, part);
		return this;
	}

	public Map<String, Part> getParts() {
		return parts;
	}

	public boolean setRequestProperties(Request.Builder okRequestBuilder) {
		return false;
	}

	public static class Part {
		private final String mediaType;
		private final File file;

		public Part(String mediaType, File file) {
			this.mediaType = mediaType;
			this.file = file;
		}

		public String getMediaType() {
			return mediaType;
		}

		public File getFile() {
			return file;
		}
	}

}