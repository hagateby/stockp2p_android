package com.stockp2p.components.myaccount;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stockp2p.R;
import com.stockp2p.common.cache.SertificatonInfor;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.ifinvoke.Des3;
import com.stockp2p.common.ifinvoke.EnginCallback;
import com.stockp2p.common.ifinvoke.ServiceEngin;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.framework.baseframe.BaseFragment;


/**
 * 未认证
 * 
 * @author haix
 * 
 */
@SuppressLint("ValidFragment")
public class NoVerify extends BaseFragment {

	
	private View thisView;
	/**
	 * 是否存在确认打款
	 */
	private String flag;
	private SertificatonInfor sertificatonInfor;

	public NoVerify(SertificatonInfor sertificatonInfor) {
		this.sertificatonInfor = sertificatonInfor;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		init(thisView, "我的账户");
		setListeners();
		return thisView;
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		// 第一栏
		
		
	}

	@Override
	public void init(View view, String title) {
		// TODO Auto-generated method stub
	
		
	}
}
