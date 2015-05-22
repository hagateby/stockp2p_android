package com.stockp2p.common.db;

/**
 * 版本
 * 
 * @author haix
 * 
 */
public class Version {

	private String versionCode;// 版本号
	private String versionDesc;// //版本描述
	private String opeFlag;// 1有更新0无更新
	private String uploadUrl;// 下载地址

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public String getOpeFlag() {
		return opeFlag;
	}

	public void setOpeFlag(String opeFlag) {
		this.opeFlag = opeFlag;
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

}
