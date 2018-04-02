package com.zzw.cicd.model;
public interface WsConnector {

	/**
	 * Server base URL, always with trailing slash, for instance
	 * "http://localhost:9000/"
	 */
	String baseUrl();

	/**
	 * @throws IllegalStateException
	 *             if the request could not be executed due to a connectivity
	 *             problem or timeout. Because networks can fail during an
	 *             exchange, it is possible that the remote server accepted the
	 *             request before the failure
	 */
	WsResponse call(WsRequest wsRequest);

}
