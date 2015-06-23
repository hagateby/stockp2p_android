package com.stockp2p.common.ifinvoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.util.PubFun;
import com.stockp2p.common.util.SYSTEMCONST;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.components.login.LoginActicity;
import com.stockp2p.framework.ExitApplication;
import com.stockp2p.framework.baseframe.BaseFragmentActivity;

public class EnginCallback extends RequestCallBack {

	private ProgressDialog pd;
	private Context context;
	private CommonDialog dialog;

	/**
	 * 构造函数,为公共进度条重写此回调
	 */
	public EnginCallback(Context context) {
		this.context = context;
	}

	@Override
	public void onFailure(HttpException arg0, String arg1) {
		// TODO Auto-generated method stub
		canclDialog();
		Log.e("失败异常", "HttpException:" + arg0 + "返回值" + arg1);
		Toast.makeText(context, "无法连接服务器!!", Toast.LENGTH_SHORT).show();
		// ((Activity) context).finish();
	}

	@Override
	public void onSuccess(ResponseInfo arg0) {

		// TODO Auto-generated method stub
		String result = "";
	
		result =PubFun.getEncryptString(arg0.result.toString());
		
		JSONObject obj = JSON.parseObject(result);
		if (obj.get("ResultCode") != null
				&& obj.get("ResultCode").toString().equals("99")) {// 请求成功

			dialog = new CommonDialog(context, 1, "确定", null,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							UserInfoManager.getInstance().exitLogin();
							ExitApplication.getInstance().exitLogin(context);
							Intent intent = new Intent(context,
									LoginActicity.class);
							((BaseFragmentActivity) context).finish();
							context.startActivity(intent);
							
						}
					}, null, "提示", obj.get("ResultMsg").toString());
			dialog.show();
		}
		canclDialog();
	}

	/**
	 * 取消请求对话框
	 */
	public void canclDialog() {
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (pd == null && context != null) {
			pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("正在请求服务器...");
			pd.setCancelable(false);
			pd.show();
		}
	}

}
