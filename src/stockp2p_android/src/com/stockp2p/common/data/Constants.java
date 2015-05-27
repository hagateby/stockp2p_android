package com.stockp2p.common.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Constants {

	// webservice连接配置
	public static int runCount = 0;
	public static final int TIMEOUT = 10000;// setting the timeout for 10

	public static final String NAMESPACE = "zsxh.newchinalife.com";// the
																					// single																	// access
	public static final String NAMESPACEWSSE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	public static final String NAMESPACEWSU = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
	public static final String NAMESPACEENV = "http://schemas.xmlsoap.org/soap/envelope/";
	public static final String SERVICE = "/ncihservice/ws/tradeService?wsdl";
	public static final String METHOD_NAME = "commonQuery";
	public static final String SOAP_ACTION = NAMESPACE + METHOD_NAME;

	// 接口地址配置
	
	public static String DOMIN_NAME = "";
	public static String URLHTML5 = "";
	
	public static final boolean ISDEBUG =true ; // 0-- 开发 1--生产
												// 具体定义在BlankActivity.java里面
	public static String PROTOCOL;
	public static String HOST;
	public static int PORT;
	public static String URL = "";
	

	// webview里面需要替换的图片网络地址--JasonToDb.java
	public static final String IMGURLPATH = "file:///android_asset/";// 生产
	// 初始的最后更新时间
	public static final String UPDATEDATEORIGINAL = "2015-04-09 1:27:55";

	// 在线客服配置 --废弃
	public static final String ONLINECUSTOMERSERVICEIP = "10.8.53.11";
	public static final String ONLINECUSTOMERSERVICEHOSTNAME = "transituser@guopengfei-pc";

	// 各模块根节点id
	public static final int TELLERSERVICE = 2; // 柜面服务 channelId
	public static final int NEGOTIATE = 110; // 注册协议 contentId
	public static final int TELSERVICE = 40; // 电话服务
	public static final int SMSSERVICE = 46; // 短信服务
	public static final int WEBSERVICE = 71; // 网站服务
	public static final int ACCRULE = 111; // 账号规则 contentId

	public static final int BAOQUANYINGBEI = 89; // 保全应备文件
	public static final int LIPEIYINGBEI = 96; // 理赔应备文件

	public static final int RIGHTMANUAL = 107; // 权益手册 channelId
	public static final int RIGHTMANUALMAINTAIN = 108; // 保全 channelId
	public static final int RIGHTMANUALCLAIM = 109; // 理赔 channleId

	public static final int COMMONQUESTION = 55; // 常见问题 channleId

	public static final int COUNTERSERVEDESC = 66; // 服务介绍 柜面服务 channelId
	public static final int PHONESERVEDESC = 67; // 服务介绍 电话服务 channleId
	public static final int SMSSERVEDESC = 68; // 服务介绍 短信服务 channelId
	public static final int WEBSITESERVEDESC = 69; // 服务介绍 网站服务 channelId

	public static final int FIRMDESC = 73; // 实力新华 新华公司介绍 channleId
	public static final int PRODUCTSYS = 74; // 实力新华 新华产品体系 channelId
	public static final int SERVESYS = 75; // 实力新华 新华服务体系 channelId
	public static final int ORGSITE = 76; // 实力新华 新华机构网点 channelId
	public static final int HOLDHONOR = 77; // 实力新华 新华获得荣誉 channelId

	public static final int CAR = 78;
	public static final int MEDICAL = 79;
	public static final int TOOTH = 80;
	public static final int INFO = 81;
	public static final int HOMEDOCTOR = 82;
	public static final int GLOBALGUEST = 83;
	public static final int DOUBLETREAT = 84;
	public static final int PORTGUEST = 85;
	public static final int HEALTH = 86;
	public static final int FESTIVAL = 87;
	public static final int INSINSTRUCTION = 105;
	public static final int USEHONORSERVICE = 106;

	// 我的保险模块调用保单列表的模块名称
	public static final int POLQUE = 1;
	public static final int ACCQUE = 2;
	public static final int PAYQUE = 3;
	public static final int SHAQUE = 4;
	public static final int RECQUE = 5;
	public static final int APPINFOCHANGE = 6;
	public static final int INSINFOCHANGE = 7;
	public static final int INSGETSTYLECHANGE = 8;

	// 网点or医院
	public static final int NETWORK = 0;
	public static final int HOSPITAL = 1;
	
	/**
	 * E管家显示的数量
	 */
	public static String Enum;
	/**
	 * 底栏要显示的模块
	 */
	public static List<Framework> moduleList;
	/**
	 * 记录从哪登录的 当用户被挤掉后用到
	 */
	public static Framework staticFramework;
	/**
	 * 记录是否版本更新过
	 */
	public static boolean isVersonUpdated = false;

	/**
	 * 没有设置交易密码 为isTradePwd的值
	 */
	public final static String NoTradePwd = "0";
	/**
	 * 已设置交易密码
	 */
	public final static String HaveTradePwd = "1";

	/**
	 * 我的定单 已完成
	 */
	public final static String StatusFinished = "4";
	/**
	 * 我的定单 未完成
	 */
	public final static String StatusUndone = "1";
	/**
	 * 我的定单 已失效
	 */
	public final static String StatusFailed = "2";
	/**
	 * 我的定单 所有
	 */
	public final static String StatusAll = "5";

	/**
	 * 保单列表 登录用户可以做的保全项目
	 */
	public final static String Policyholder = "投保人";
	public final static String Insured = "被保人";
	public final static String PolicyholderWithInsured = "既是投保人又是被保险人";

	public final static String ApplicationTelChange = "投保人电话变更";
	public final static String AccountInforQuery = "账户信息查询";
	public final static String AccumulatedInterestReceive = "累计生息部分领取";
	public final static String AnnuityReceive = "年金满期金领取";
	public final static String ApplicantInforChange = "投保人资料变更";
	public final static String BonusInforQuery = "分红信息查询";
	public final static String InsurerInforChange = "被保险人资料变更";
	public final static String PayInforQuery = "交费信息查询";
	public final static String PolicyInforQuery = "保单信息查询";
	public final static String PolicyLoanCredit = "贷款续贷";
	public final static String PolicyLoanRepayment = "贷款清偿";
	public final static String PolicyPledgeLoans = "保单质押贷款";
	public final static String PolicyReinstatement = "保单复效";
	public final static String PolicySurrender = "整单退保";
	public final static String Receivechange = "领取形式变更";
	public final static String ReceiveInforQuery = "生存/满期领取查询";
	public final static String Renewalpayment = "续期缴费";
	public final static String RenewalpaymentInforChange = "续期交费信息变更";
	public final static String UniversalInsuranceReceive = "万能险领取";
	public final static String PolicyLossActivity = "保单挂失";
	public final static String WayChangeActivity = "领取方式变更";
	public final static String PolicyTransferActivity = "保单迁移";
	public final static String MainRisksRenewalActivity = "主险续保";
	public final static String DateChangeActivity = "领取日期变更";
	public final static String AgeChangeActivity = "领取年龄变更";
	public final static String AccessoryRiskNotRenewalActivity = "附加险满期不续保";
	public final static String AccessoryRiskLowCoverageRenewalActivity = "附加险满期低保额续保";

	// =====支付平台用到的参数对应的值==================================///
	// ========================== 商品类型地址 ==========================//
	// / -> 年金／满期金领取
	public final static String AnnuityReceiveProductType = "101";
	// / -> 累计生息领取
	public final static String AccumulatedInterestReceiveProductType = "102";
	// / -> 万能险领取
	public final static String UniversalInsuranceReceiveProductType = "103";
	// / -> 保单复效
	public final static String PolicyReinstatementProductType = "204";
	// / -> 保单退保
	public final static String PolicySurrenderProductType = "105";
	// / -> 保单质押贷款
	public final static String PolicePledgeLoanProductType = "106";
	// / -> 贷款续贷（收费时）
	public final static String LoanCreditReceiveProductType = "207";
	// / -> 贷款续贷（付费时）
	public final static String LoanCreditPayProductType = "107";
	// / -> 贷款清偿
	public final static String LoanPaymentProductType = "208";
	// / -> 续期交费
	public final static String RenewalPayProductType = "209";
	// / -> 电话号码变更
	public final static String ApplicationTelChangeProductType = "010";
	// / -> 远程认证
	public final static String RemoteauthenticationProductType = "011";
	// / -> 交易密码设置
	public final static String PasswordConfigProductType = "012";

	// ========================== 交易类型地址 ==========================//
	// / -> 年金／满期金领取
	public final static String AnnuityReceiveTxnType = "C01";
	// / -> 累计生息领取
	public final static String AccumulatedInterestReceiveTxnType = "C01";
	// / -> 万能险领取
	public final static String UniversalInsuranceReceiveTxnType = "C01";
	// / -> 保单复效
	public final static String PolicyReinstatementTxnType = "D01";
	// / -> 保单退保
	public final static String PolicySurrenderTxnType = "C01";
	// / -> 保单质押贷款
	public final static String PolicePledgeLoanTxnType = "C01";
	// / -> 贷款续贷（收费时）
	public final static String LoanCreditReceiveTxnType = "D01";
	// / -> 贷款续贷（付费时）
	public final static String LoanCreditPayTxnType = "C01";
	// / -> 贷款清偿
	public final static String LoanPaymentTxnType = "D01";
	// / -> 续期交费
	public final static String RenewalPayTxnType = "D01";
	// / -> 电话号码变更
	public final static String ApplicationTelChangeTxnType = "OMM";
	// / -> 远程认证
	public final static String RemoteauthenticationTxnType = "OSR";
	// / -> 交易密码设置
	public final static String PasswordConfigTxnType = "OVP";

	// 前一年当天日期
	public static final String LASTYEARCURRENTDATE = new SimpleDateFormat(
			"yyyy-MM-dd").format(lastDate()).toString();

	private static Date lastDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		return calendar.getTime();
	}

	// 当天日期
	public static final String CURRENTDATE = new SimpleDateFormat("yyyy-MM-dd")
			.format(new Date()).toString();

	/**
	 * 分享处理
	 */
	public static final int MSG_SHARE_HANDLE = 1000;
}