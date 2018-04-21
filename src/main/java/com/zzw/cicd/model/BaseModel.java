package com.zzw.cicd.model;

import com.offbytwo.jenkins.client.JenkinsHttpClient;

public class BaseModel {

	private String _class;
    
    public String get_class() {
        return _class;
    }

    //TODO: We should make this private
    protected JenkinsHttpClient client;

    public JenkinsHttpClient getClient() {
        return client;
    }

    public void setClient(JenkinsHttpClient client) {
        this.client = client;
    }
}
