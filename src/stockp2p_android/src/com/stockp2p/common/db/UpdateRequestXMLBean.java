package com.stockp2p.common.db;

import android.telephony.TelephonyManager;

public class UpdateRequestXMLBean {
	
	private String updatedDate;		//上次更新时间
	private String modelType;		//设备类型1手机、2pad
	private String versionCode;		//手机版本号
	private String versionType;		//版本类型1安卓2Ios、3pad、4ipad
	private String regionCode;       //所属地区
	private String deviceId;     //设备号
	
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionType() {
		return versionType;
	}
	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}
	
}
  