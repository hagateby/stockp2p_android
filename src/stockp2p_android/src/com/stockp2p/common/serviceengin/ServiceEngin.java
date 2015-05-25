package com.stockp2p.common.serviceengin;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.util.ExitApplication;
import com.stockp2p.common.util.NetUtil;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.framework.BaseFragmentActivity;
import com.stockp2p.framework.login.LoginActicity;

/**
 * 公共请求类
 * 
 * @author yilong.zhang
 * 
 */
public class ServiceEngin {

	private static HttpUtils httputil = new HttpUtils();
	// private static String LOCAL_HOST = Constants.PROTOCOL + Constants.HOST
	// + ":" + Constants.PORT + "/";
	// /** 回归环境 **/
	// private static String url = LOCAL_HOST
	// + "regressionmobileservice/ServiceEngin.do";
	// /** 生产环境 **/
	// private static String url = LOCAL_HOST
	// + "ncihmobileservice/ServiceEngin.do";
	// private static String
	// url="http://10.8.51.59:9090/ncihmobileservice/ServiceEngin.do";
	private static String RESULT;
	private static CommonDialog dialog;

	/**
	 * 异步请求方法,请自行在callback中处理返回结果(callbackde success中自行解析result)
	 * 
	 * @param bizId
	 *            业务场景编码
	 * @param serviceName
	 *            方法名
	 * @param servicePara
	 *            参数集合
	 * @param context
	 *            当前界面上下文
	 * @param callback
	 *            重写的EngineCallBack回调函数
	 */
	public static void Request(final Context context, String bizId,
			String serviceName, String servicePara, EnginCallback callback) {
		if (NetUtil.hasNetWork(context)) {
			TipUitls.Log("ServiceEngine", "请求URL:" + Constants.URL + "部分参数:"
					+ servicePara);
			// 参数拼接
			RequestParams params = new RequestParams();
			params.addBodyParameter("keyVer", "v1");
			params.addBodyParameter("appID", "1");
			EnginBean eb = new EnginBean();
			eb.setBizId(bizId);
			String deviceId = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			eb.setDeviceId(deviceId);
			eb.setClientOS("1");
			eb.setClientVer(CommonUtil.getClientVer());//
			try {
				eb.setAppVer(CommonUtil.getVersionName(context));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			eb.setServiceName(serviceName);
			eb.setServicePara(servicePara);
			String para = JSON.toJSON(eb) + "";
			try {
				para = Des3.encode(para);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				RESULT = "请求加密失败";
			}
			params.addBodyParameter("para", para);
			// 请求超时
			httputil.configTimeout(1000 * 20);
			// 发送请求
			httputil.send(HttpRequest.HttpMethod.POST, Constants.URL, params,
					callback);
		} else {
			dialog = new CommonDialog(context, 2, "确定", "取消",
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = null;
							// 判断手机系统的版本 即API大于10 就是3.0或以上版本
							if (android.os.Build.VERSION.SDK_INT > 10) {
								intent = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
							} else {
								intent = new Intent();
								ComponentName component = new ComponentName(
										"com.android.settings",
										"com.android.settings.WirelessSettings");
								intent.setComponent(component);
								intent.setAction("android.intent.action.VIEW");
							}
							context.startActivity(intent);
						}
					}, new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							((BaseFragmentActivity) context).finish();
						}
					}, "网络设置提示", "网络连接不可用,是否进行设置?");
			dialog.show();
		}
	}

	/**
	 * 同步请求方法,自动返回解密后的json类型数据(result解密后自动解析)
	 * 
	 * @param bizId
	 *            业务场景编码
	 * @param serviceName
	 *            方法名
	 * @param servicePara
	 *            参数集合
	 * @param context
	 *            当前界面上下文
	 * 
	 * @return 已解密json类型数据
	 */
	public static String Request(final Context context, String bizId,
			String serviceName, String servicePara) {

		TipUitls.Log("ServiceEngine", "请求URL:" + Constants.URL + "部分参数:"
				+ servicePara);
		String result = "";
		/* lwh
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		*/
		// 参数拼接
		RequestParams params = new RequestParams();
		params.addBodyParameter("keyVer", "v1");
		params.addBodyParameter("appID", "1");
		EnginBean eb = new EnginBean();
		eb.setBizId(bizId);
		String deviceId = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		// eb.setDeviceId(deviceId);
		eb.setDeviceId(deviceId);
		eb.setClientOS("1");
		eb.setClientVer(CommonUtil.getClientVer());//
		try {
			eb.setAppVer(CommonUtil.getVersionName(context));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eb.setServiceName(serviceName);
		eb.setServicePara(servicePara);
		String para = JSON.toJSON(eb) + "";
		try {
			para = Des3.encode(para);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			RESULT = "请求加密失败";
		}
		params.addBodyParameter("para", para);

		// 非xutils请求
		// try {
		// // 封装请求参数
		// List<NameValuePair> pair = new ArrayList<NameValuePair>();
		// pair.add(new BasicNameValuePair("keyVer", "v1"));
		// pair.add(new BasicNameValuePair("appID", "1"));
		// pair.add(new BasicNameValuePair("para", para));
		// // 把请求参数变成请求体部分
		// UrlEncodedFormEntity uee = new UrlEncodedFormEntity(pair, "utf-8");
		// // 使用HttpPost对象设置发送的URL路径
		// HttpPost post = new HttpPost(url);
		// post.setHeader("Accept-Encoding", "identity");
		// // 发送请求体
		// post.setEntity(uee);
		// // 创建一个浏览器对象，以把POST对象向服务器发送，并返回响应消息
		// DefaultHttpClient dhc = new DefaultHttpClient();
		// HttpResponse response = dhc.execute(post);
		// if (response.getStatusLine().getStatusCode() == 200) {
		// HttpEntity entity = response.getEntity();
		// String content = EntityUtils.toString(entity, "gbk");
		// if (content != null) {
		// result = Des3.decode(content);
		// }
		// }
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ClientProtocolException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// // 请求超时
		httputil.configTimeout(1000 * 20);
		BufferedReader in = null;
		try {
			ResponseStream receiveStream = httputil.sendSync(
					HttpRequest.HttpMethod.POST, Constants.URL, params);
			in = new BufferedReader(new InputStreamReader(receiveStream));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			in.close();
			result = Des3.decode(result);
			JSONObject jo = JSONObject.parseObject(result);
			if (jo.getString("resultCode") != null
					&& jo.getString("resultCode").equals("99")) {
				new CommonDialog(context, 1, "确定", null, new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						UserInfoManager.getInstance().exitLogin();
						ExitApplication.getInstance().exitLogin(context);
						Intent intent = new Intent(context, LoginActicity.class);
						intent.putExtra("isExit", "true");
						context.startActivity(intent);
					}
				}, null, "提示", "链接超时").show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		// 发送请求
		return result;
	}
}
