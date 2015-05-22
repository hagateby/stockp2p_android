package com.pactera.nci.common.view;

public class UserInfoManager {
	
	private UserInfoManager(){}
	
	private static UserInfoManager loginManager = null;
	
	public static UserInfoManager getInstance(){
		if(loginManager == null){
			loginManager = new UserInfoManager();
		}
		return loginManager;
	}

	private String sessionId = "";
	private String cid = "";
	private String resultCode = "";
    private String resultMsg = "";
    private String userName = "";
    private String password = "";
    private String nicheng = "";
    private String sex = "";
    private String birthday = "";
    private String name = "";
    private String deviceId;
    
    
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNicheng() {
		return nicheng;
	}

	public void setNicheng(String nicheng) {
		this.nicheng = nicheng;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	/**
	 * 判断登录是否成功
	 * @return
	 */
	public boolean isLogin(){
		if(resultCode.equals("0")){  //0正常 1出错
			return true;
		}
		return false; 
	}
	
	/**
	 * 退出登录
	 * @return
	 */
	public boolean exitLogin(){
		sessionId = "";
		cid = "";
		resultCode = "";
	    resultMsg = "";
	    userName = "";
	    password = "";
	    nicheng = "";
	    sex = "";
	    birthday = "";
	    name = "";
		return true;
	}

	@Override
	public String toString() {
		return "LoginManager [sessionId=" + sessionId + ", cid=" + cid
				+ ", resultCode=" + resultCode + ", resultMsg=" + resultMsg + "]";
	}
}
