package com.zzw.cicd.model;

import com.zzw.cicd.model.WsRequest.Method;

public class GetRequest extends BaseRequest<GetRequest> {
	public GetRequest(String path) {
		super(path);
	}

	@Override
	public Method getMethod() {
		return Method.GET;
	}
}
