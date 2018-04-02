package com.zzw.cicd.model;

import java.io.InputStream;
import java.io.Reader;

public interface WsResponse {

	/**
	 * The absolute requested URL
	 */
	String requestUrl();

	/**
	 * HTTP status code
	 */
	int code();

	/**
	 * Returns true if the code is in [200..300), which means the request was
	 * successfully received, understood, and accepted.
	 */
	boolean isSuccessful();

	/**
	 * Throws a {@link HttpException} if {@link #isSuccessful()} is false.
	 */
	WsResponse failIfNotSuccessful();

	String contentType();

	boolean hasContent();

	InputStream contentStream();

	Reader contentReader();

	String content();

	String header(String name);
}