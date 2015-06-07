package com.stockp2p.common.cache;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.stockp2p.R;
import com.stockp2p.common.ifinvoke.ServiceEngin;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.util.TipUitls;

/**
 * 认证信息单例
 * 
 * @author haix
 * 
 */
public class SertificatonInfor {

	private final static String TAG = "SertificatonInfor";
	private static FragmentActivity context;

	private static SertificatonInfor sertificatonInfor = null;

	private static Dialog commonDialog;

	static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(context, msg.obj.toString(), 0).show();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 获取认证信息
	 * 
	 * @return 请求失败返回 null
	 * @throws CacheException
	 */
	public static SertificatonInfor getInstance(FragmentActivity activity,
			ErrorCallBack callback) {
		context = activity;
		if (sertificatonInfor == null) {
			if (!UserInfoManager.getInstance().isLogin()) {
				return null;
			}
			sertificatonInfor = init(context, callback);

		}
		return sertificatonInfor;
	}

	private Thread thread;

	/**
	 * 获取姓别
	 * 
	 * @return
	 */
	public String getSex() {
		return CommonUtil.idRules(getIdNo(), 0);
	}

	/**
	 * 获取生日
	 * 
	 * @return
	 */
	public String getBirthday() {
		return CommonUtil.idRules(getIdNo(), 1);
	}

	/**
	 * 认证状态 已认证返回 true
	 * 
	 * @return
	 */
	public boolean sertificationStatus() {
		if (RemoteCerStatus() || OtcCerStatus()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 远程认证状态
	 * 
	 * @return
	 */
	public boolean RemoteCerStatus() {
		if (RemoteCerStatus.equals("已认证")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 柜面认证状态
	 * 
	 * @return
	 */
	public boolean OtcCerStatus() {
		if (OtcCerStatus.equals("已认证")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否有核心客户 id 保单对应一个 id
	 */
	public void requestUniCustmorId() {
		// 如果已认证过才请求接口
		if (sertificationStatus() == true) {
			Map map = new HashMap<String, String>();
			map.put("cid", UserInfoManager.getInstance().getCid());
			map.put("loginId", UserInfoManager.getInstance().getUserName());
			map.put("sessionId", UserInfoManager.getInstance().getSessionId());
			map.put("name", sertificatonInfor.getUserName());
			map.put("sex", CommonUtil.idRules(sertificatonInfor.getIdNo(), 3));
			map.put("birthday",
					CommonUtil.idRules(sertificatonInfor.getIdNo(), 1));
			map.put("idtype", "0");// 身份证为0
			map.put("idno", sertificatonInfor.getIdNo());
			final String str = JSON.toJSONString(map);

			TipUitls.Log("SertificatonInfor", "str--核心客户 id--->" + str);
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String result = ServiceEngin.Request(context,
							"02_00_05_01_03_I01", "queryCoreIdPutPay", str);
					TipUitls.Log("SertificatonInfor", "result---核心客户 id-->"
							+ result);
					if (result != null) {
						JSONObject object = JSONObject.parseObject(result);
						String ResultCode = object.getString("ResultCode");
						String ResultMsg = object.getString("ResultMsg");
						if ("0".equals(ResultCode)) {
							String CoreCid = object.getString("CoreCid");
							sertificatonInfor.setUniCustmorId(CoreCid);
						} else {
							Message message = new Message();
							message.what = 0;
							message.obj = ResultMsg;
							handler.sendMessage(message);

						}

					}

				}
			});

			thread.start();

		}
	}

	/**
	 * 清空认证信息
	 */
	public static void clearSertificationInfor() {
		sertificatonInfor = null;
	}

	private static SertificatonInfor init(final FragmentActivity context,
			ErrorCallBack callback) {

		Map map = new HashMap<String, String>();
		map.put("cid", UserInfoManager.getInstance().getCid());
		map.put("loginId", UserInfoManager.getInstance().getUserName());
		map.put("sessionId", UserInfoManager.getInstance().getSessionId());
		String str = JSON.toJSONString(map);

		TipUitls.Log("SertificatonInfor", "str----->" + str);
		String result = ServiceEngin.Request(context, "01_00_01_01_01_I01",
				"getMyAccount", str);
		TipUitls.Log("SertificatonInfor", "result----->" + result);
		if (result != null && JSON.parseObject(result) != null) {
			JSONObject jsonObject = JSON.parseObject(result);
			String resultCode = jsonObject.getString("ResultCode");
			String resultMsg = jsonObject.getString("ResultMsg");

			if ("0".equals(resultCode)) {
				String myAccount = jsonObject.getString("MyAccount");
				sertificatonInfor = JSON.parseObject(myAccount,
						SertificatonInfor.class);

				

			} else if (!"99".equals(resultCode)) {

				if (callback != null) {
					callback.onFailure(1, resultMsg);
				}

			} else if ("99".equals(resultCode)) {
				if (callback != null) {
					callback.onFailure(99, resultMsg);
				}

			}

		} else {
			if (callback != null) {
				callback.onFailure(
						1,
						context.getResources().getString(
								R.string.request_failed));
			}

		}
		return sertificatonInfor;
	}

	public interface ErrorCallBack {
		public void onFailure(int i, String resultMsg);
	}

	private String MyAccount; // 我的账户
	private String userName; // 真实姓名
	private String accountBalance; // 账户余额
	private String undoneOrderCount; // 未完成订单数
	private String IdType; // 证件类型
	private String IdNo; // 证件号码
	private String Blockcode; // 账户锁定码
	private String MobileNo; // 手机号
	private String Email; // 邮箱
	private String RemoteCerStatus; // 远程认证状态
	private String RemoteCerDate; // 远程认证日期
	private String OtcCerStatus; // 柜面认证状态
	private String OtcCerDate; // 柜面认证日期
	private String TxnLimitAMT; // 认证对应交易限额
	private String RemitConfirmFlag; // 是否存在待确认打款
	private String RemitCardNo; // 打款卡号
	private String RemitUse; // 打款确认目的
	private String AcctID; // 支付平台账户
	private String UniCustmorId;// 新华客户ID//年期金返回第一被保险人 id,用来验证这个id 是否是第一被保险人 id
	private String SessionMAC; // 平台会话验证码

	/**
	 * 我的账户
	 * 
	 * @return
	 */
	public String getMyAccount() {
		return MyAccount;
	}

	/**
	 * 我的账户
	 * 
	 * @param myAccount
	 */
	public void setMyAccount(String myAccount) {
		MyAccount = myAccount;
	}

	/**
	 * /真实姓名
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * /真实姓名
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 账户余额
	 * 
	 * @param userName
	 */
	public String getAccountBalance() {
		return accountBalance;
	}

	/**
	 * 账户余额
	 * 
	 * @param userName
	 */
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

	/**
	 * 未完成订单数
	 * 
	 * @param userName
	 */
	public String getUndoneOrderCount() {
		return undoneOrderCount;
	}

	/**
	 * 未完成订单数
	 * 
	 * @param userName
	 */
	public void setUndoneOrderCount(String undoneOrderCount) {
		this.undoneOrderCount = undoneOrderCount;
	}

	/**
	 * 证件类型
	 * 
	 * @return
	 */
	public String getIdType() {
		return IdType;
	}

	/**
	 * 证件类型
	 * 
	 * @param idType
	 */
	public void setIdType(String idType) {
		IdType = idType;
	}

	/**
	 * 证件号码
	 * 
	 * @return
	 */
	public String getIdNo() {
		return IdNo;
	}

	/**
	 * 证件号码
	 * 
	 * @param idNo
	 */
	public void setIdNo(String idNo) {
		IdNo = idNo;
	}

	/**
	 * 账户锁定码
	 * 
	 * @return
	 */
	public String getBlockcode() {
		return Blockcode;
	}

	/**
	 * 账户锁定码
	 * 
	 * @param blockcode
	 */
	public void setBlockcode(String blockcode) {
		Blockcode = blockcode;
	}

	/**
	 * 手机号
	 * 
	 * @return
	 */
	public String getMobileNo() {
		return MobileNo;
	}

	/**
	 * 手机号
	 * 
	 * @param mobileNo
	 */
	public void setMobileNo(String mobileNo) {
		MobileNo = mobileNo;
	}

	/**
	 * 邮箱
	 * 
	 * @return
	 */
	public String getEmail() {
		return Email;
	}

	/**
	 * 邮箱
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		Email = email;
	}

	/**
	 * 远程认证状态
	 * 
	 * @return
	 */
	public String getRemoteCerStatus() {
		return RemoteCerStatus;
	}

	/**
	 * 远程认证状态
	 * 
	 * @param remoteCerStatus
	 */
	public void setRemoteCerStatus(String remoteCerStatus) {
		RemoteCerStatus = remoteCerStatus;
	}

	/**
	 * 远程认证日期
	 * 
	 * @return
	 */
	public String getRemoteCerDate() {
		return RemoteCerDate;
	}

	/**
	 * 远程认证日期
	 * 
	 * @param remoteCerDate
	 */
	public void setRemoteCerDate(String remoteCerDate) {
		RemoteCerDate = remoteCerDate;
	}

	/**
	 * 柜面认证状态
	 * 
	 * @return
	 */
	public String getOtcCerStatus() {
		return OtcCerStatus;
	}

	/**
	 * 柜面认证状态
	 * 
	 * @param otcCerStatus
	 */
	public void setOtcCerStatus(String otcCerStatus) {
		OtcCerStatus = otcCerStatus;
	}

	/**
	 * 柜面认证日期
	 * 
	 * @return
	 */
	public String getOtcCerDate() {
		return OtcCerDate;
	}

	/**
	 * 柜面认证日期
	 * 
	 * @param otcCerDate
	 */
	public void setOtcCerDate(String otcCerDate) {
		OtcCerDate = otcCerDate;
	}

	/**
	 * 认证对应交易限额
	 * 
	 * @return
	 */
	public String getTxnLimitAMT() {
		return TxnLimitAMT;
	}

	/**
	 * 认证对应交易限额
	 * 
	 * @param txnLimitAMT
	 */
	public void setTxnLimitAMT(String txnLimitAMT) {
		TxnLimitAMT = txnLimitAMT;
	}

	/**
	 * 是否存在待确认打款
	 * 
	 * @return
	 */
	public String getRemitConfirmFlag() {
		return RemitConfirmFlag;
	}

	/**
	 * 是否存在待确认打款
	 * 
	 * @param remitConfirmFlag
	 */
	public void setRemitConfirmFlag(String remitConfirmFlag) {
		RemitConfirmFlag = remitConfirmFlag;
	}

	/**
	 * 打款卡号
	 * 
	 * @return
	 */
	public String getRemitCardNo() {
		return RemitCardNo;
	}

	/**
	 * 打款卡号
	 * 
	 * @param remitCardNo
	 */
	public void setRemitCardNo(String remitCardNo) {
		RemitCardNo = remitCardNo;
	}

	/**
	 * 打款确认目的
	 * 
	 * @return
	 */
	public String getRemitUse() {
		return RemitUse;
	}

	/**
	 * 打款确认目的
	 * 
	 * @param remitUse
	 */
	public void setRemitUse(String remitUse) {
		RemitUse = remitUse;
	}

	/**
	 * 支付平台账户
	 * 
	 * @return
	 */
	public String getAcctID() {
		return AcctID;
	}

	/**
	 * 支付平台账户
	 * 
	 * @param acctID
	 */
	public void setAcctID(String acctID) {
		AcctID = acctID;
	}

	/**
	 * 新华客户ID 如果UniCustmorId为 null 就去请求接口
	 * 
	 * @return
	 */
	public String getUniCustmorId() {

		if ("".equals(UniCustmorId)) {
			requestUniCustmorId();
			try {
				thread.join();
			} catch (Exception e) {
				// TODO: handle exception
			}
			TipUitls.Log("SertificatonInfor", "UniCustmorId----->"
					+ UniCustmorId);
		}
		return UniCustmorId;
	}

	/**
	 * 新华客户ID
	 * 
	 * @param uniCustmorId
	 */
	public void setUniCustmorId(String uniCustmorId) {
		UniCustmorId = uniCustmorId;
	}

	/**
	 * 平台会话验证码
	 * 
	 * @return
	 */
	public String getSessionMAC() {
		return SessionMAC;
	}

	/**
	 * 平台会话验证码
	 * 
	 * @param sessionMAC
	 */
	public void setSessionMAC(String sessionMAC) {
		SessionMAC = sessionMAC;
	}

}
