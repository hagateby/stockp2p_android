package com.stockp2p.components.myaccount;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stockp2p.R;
import com.stockp2p.common.cache.SertificatonInfor;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.serviceengin.Des3;
import com.stockp2p.common.serviceengin.EnginCallback;
import com.stockp2p.common.serviceengin.ServiceEngin;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.view.CommonDialog;

import com.stockp2p.framework.baseframe.BaseFragment;


@SuppressLint("ValidFragment")
public class Verified extends BaseFragment implements OnClickListener {

	private static String TAG = "Verified";

	

	private View thisView;// fragment视图
	private SertificatonInfor sertiInfor;// 认证信息对象
	private LinearLayout myacc_changeTel;// 修改认证手机
	private LinearLayout myacc_changePwd1;// 修改支付密码
	private LinearLayout myacc_changePwd2;// 设置交易密码
	private LinearLayout myacc_changeInfo;// 我的认证xin'xi
	private View popwindowView;// popwindow视图
	private PopupWindow mPopupWindow;// popwindow
	/**
	 * 下拉按钮
	 */
	private Button buttonMore;

	public Verified(SertificatonInfor sertiInfor) {
		this.sertiInfor = sertiInfor;
	}

	public Verified() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return thisView;
	}

	private void setViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);
		// 设置更多
		buttonMore = (Button) context
				.findViewById(R.id.base_activity_bt_base_title_more);
		buttonMore.setVisibility(View.VISIBLE);
	

	}

	/**
	 * 所有控件的点击方法
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	
	}

	/**
	 * 手机号修改前先生成定单
	 */
	private void phoneChangeRequest() {
	

	}

	/**
	 * 用户交易密码设置查询接口
	 */
	private void isHaveTradePwd() {
		String bizId = "未知bizId";
		String serviceName = "tradePwdPutQuery";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("cid", UserInfoManager.getInstance().getCid());
		map.put("loginId", UserInfoManager.getInstance().getUserName());
		map.put("sessionId", UserInfoManager.getInstance().getSessionId());
		map.put("name", SertificatonInfor.getInstance(context, null)
				.getUserName());
		if (SertificatonInfor.getInstance(context, null).getSex().equals("男")) {
			map.put("sex", "0");
		} else {
			map.put("sex", "1");
		}

		if (SertificatonInfor.getInstance(context, null).getIdType()
				.equals("I")) {
			map.put("idtype", "0");
		} else {
			map.put("idtype", "1");
		}
		map.put("birthday", SertificatonInfor.getInstance(context, null)
				.getBirthday());
		map.put("idno", SertificatonInfor.getInstance(context, null).getIdNo());

		String servicePara = JSON.toJSONString(map);
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						String result = "";
						try {
							result = Des3.decode(arg0.result.toString());// 服务器返回结果
							LogUtils.e("请求成功"
									+ Des3.decode(arg0.result.toString()));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ParseJson(result);
						super.onSuccess(arg0);
					}
				});
	}

	/**
	 * 解析json
	 * 
	 * @param ParseJson
	 */
	private void ParseJson(String ParseJson) {
	}
		
}
