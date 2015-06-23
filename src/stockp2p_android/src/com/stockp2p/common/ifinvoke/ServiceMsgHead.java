package com.stockp2p.common.ifinvoke;

public class ServiceMsgHead {

	private String serviceName; // 服务名称
	private String servicePara; // 服务参数
	private String clientOS; // 客户端操作系统
	private String deviceId; // 设备Id
	private String clientVer; // 操作系统版本号
	private String appVer;// 应用版本号
	private String bizId; // 业务场景

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServicePara() {
		return servicePara;
	}

	public void setServicePara(String servicePara) {
		this.servicePara = servicePara;
	}

	public String getClientOS() {
		return clientOS;
	}

	public void setClientOS(String clientOS) {
		this.clientOS = clientOS;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getClientVer() {
		return clientVer;
	}

	public void setClientVer(String clientVer) {
		this.clientVer = clientVer;
	}

	public String getAppVer() {
		return appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}
}
