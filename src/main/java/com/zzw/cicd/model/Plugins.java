package com.zzw.cicd.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Plugins implements Serializable {

	private boolean active;

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	private String backupVersion;

	public String getBackupVersion() {
		return backupVersion;
	}

	public void setBackupVersion(String backupVersion) {
		this.backupVersion = backupVersion;
	}

	private boolean bundled;

	public boolean getBundled() {
		return bundled;
	}

	public void setBundled(boolean bundled) {
		this.bundled = bundled;
	}

	private boolean deleted;

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	private boolean downgradable;

	public boolean getDowngradable() {
		return downgradable;
	}

	public void setDowngradable(boolean downgradable) {
		this.downgradable = downgradable;
	}

	private boolean enabled;

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private boolean hasUpdate;

	public boolean getHasUpdate() {
		return hasUpdate;
	}

	public void setHasUpdate(boolean hasUpdate) {
		this.hasUpdate = hasUpdate;
	}

	private String longName;

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	private boolean pinned;

	public boolean getPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	private String requiredCoreVersion;

	public String getRequiredCoreVersion() {
		return requiredCoreVersion;
	}

	public void setRequiredCoreVersion(String requiredCoreVersion) {
		this.requiredCoreVersion = requiredCoreVersion;
	}

	private String shortName;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	private String supportsDynamicLoad;

	public String getSupportsDynamicLoad() {
		return supportsDynamicLoad;
	}

	public void setSupportsDynamicLoad(String supportsDynamicLoad) {
		this.supportsDynamicLoad = supportsDynamicLoad;
	}

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	private List<PluginDependencies> dependencies;

	public List<PluginDependencies> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<PluginDependencies> dependencies) {
		this.dependencies = dependencies;
	}

	private String depBy;

	public String getDepBy() {
		return depBy;
	}

	public void setDepBy(String depBy) {
		this.depBy = depBy;

	}

}