package com.zzw.cicd.model.Vo;

import com.zzw.cicd.model.Href;

public class LinksVo {

	private Href self;
	private Href artifacts;
	private Href changesets;

	public Href getSelf() {
		return self;
	}

	public void setSelf(Href self) {
		this.self = self;
	}

	public Href getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(Href artifacts) {
		this.artifacts = artifacts;
	}

	public Href getChangesets() {
		return changesets;
	}

	public void setChangesets(Href changesets) {
		this.changesets = changesets;
	}
}