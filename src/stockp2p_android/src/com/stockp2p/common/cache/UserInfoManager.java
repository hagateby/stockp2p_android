package com.stockp2p.common.cache;

import com.alibaba.fastjson.JSONObject;

import android.support.v4.app.FragmentActivity;

/**
 * 用户单例
 * 
 * @author haix
 * 
 */
public class UserInfoManager {

	private String sessionId = "";
	private String cid = "";
	private String resultCode = "";
	private String resultMsg = "";
	private String userName = "";// 登陆账户名
	private String password = "";// 密码
	private String loginType = "";
	//
	private String bindingState = "";
	private String nickName = "";
	private String bindingMobile = "";// 绑定手机码
	private String sex;// 性
	private String birthday;// 生日
	private String email;// 邮件
	private String province;// 省/直辖市
	private String address;// 地址
	private String zipCode;// 邮政编码
	private String mobile;// 手机号
	private String telePhone;// 固话
	private String policyCount = "";// 已关联保单数量
	private String loginDate = "";// 登录时间
	
	private UserInfoManager() {

	}

	private static UserInfoManager loginManager = null;

	/**
	 * 获取登录信息
	 * 
	 * @return
	 */
	public static UserInfoManager getInstance() {
		if (loginManager == null) {
			loginManager = new UserInfoManager();
		}
		return loginManager;
	}

	

	public String getPolicyCount() {
		return policyCount;
	}

	public void setPolicyCount(String policyCount) {
		this.policyCount = policyCount;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	/**
	 * 已登录返回 true
	 * 
	 * @return
	 */
	public boolean isLogin() {
		if ("0".equals(resultCode)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 退出登录 退出登录和登录前调用,防止意外数据不被清理
	 * 
	 * @return
	 */
	public boolean exitLogin(){

		clearLoginStatus();


		return true;
	}

	/**
	 * 清除用户信息
	 */
	public void clearLoginStatus() {
		loginManager = null;
	}

	@Override
	public String toString() {
		return "LoginManager [sessionId=" + sessionId + ", cid=" + cid
				+ ", loginType=" + loginType + ", resultMsg=" + resultMsg + "]";
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

	/**
	 * 
	 * @param sessionId
	 */
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getBindingState() {
		return bindingState;
	}

	public void setBindingState(String bindingState) {
		this.bindingState = bindingState;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getBindingMobile() {
		return bindingMobile;
	}

	public void setBindingMobile(String bindingMobile) {
		this.bindingMobile = bindingMobile;
	}
	
	
	public static void setUserInfo(JSONObject user, String resultCode,
			String resultMsg) {

		UserInfoManager userInfo = UserInfoManager.getInstance();
		userInfo.setResultCode(resultCode);
		userInfo.setResultMsg(resultMsg);
		userInfo.setCid(user.getString("cid"));
		userInfo.setSessionId(user.getString("sessionId"));
		userInfo.setBindingMobile(user.getString("bindingMobile"));
		userInfo.setBindingState(user.getString("bindingState"));
		userInfo.setNickName(user.getString("nickName"));
		//userInfo.setPassword(passwordDigest);
		userInfo.setSex(user.getString("sex"));
		userInfo.setBirthday(user.getString("birthday"));
		userInfo.setEmail(user.getString("email"));
		userInfo.setProvince(user.getString("province"));
		userInfo.setAddress(user.getString("address"));
		userInfo.setZipCode(user.getString("zipCode"));
		userInfo.setMobile(user.getString("mobile"));
		userInfo.setTelePhone(user.getString("telePhone"));
		userInfo.setUserName(user.getString("userName"));
		userInfo.setLoginDate(user.getString("loginDate"));
		userInfo.setPolicyCount(user.getString("policyCount"));
		userInfo.setLoginType("m");
	
	}
	

}
