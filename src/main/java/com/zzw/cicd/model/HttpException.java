package com.zzw.cicd.model;
public class HttpException extends RuntimeException {

	//
	private static final long serialVersionUID = 1L;
	private final String url;
	private final int code;

	public HttpException(String url, int code) {
		super(String.format("Error %d on %s", code, url));
		this.url = url;
		this.code = code;
	}

	public String url() {
		return url;
	}

	/**
	 * @see java.net.HttpURLConnection constants
	 */
	public int code() {
		return code;
	}
}