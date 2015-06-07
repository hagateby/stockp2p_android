package com.stockp2p.framework;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.stockp2p.R;
import com.stockp2p.common.ifinvoke.Des3;
import com.stockp2p.common.ifinvoke.EnginCallback;
import com.stockp2p.common.ifinvoke.ServiceEngin;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.framework.baseframe.BaseFragment;

/**
 * 意见反馈
 * 
 * @author haix
 * 
 */

public class Setting_FeedbackActivity extends BaseFragment {

	private static String TAG = "Setting_FeedbackActivity";
	private String opinionContent;
	private String contact;
	private EditText et_opinionContent;
	private EditText et_contact;
	private Button button_tijiao;
	private Spinner feedback_type;
	String[] feedback_string = { "增加新功能", "已有功能优化", "信息更新", "其它" };
	String[] feedback_number = { "1", "2", "3", "4" };
	String feedback_text;
	private View thisView;
	private CommonDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.set_feedback, null);
		init(thisView, "意见反馈");
		return thisView;
	}

	@Override
	public void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);
		initLayout();
		setOnClickListener();
	}

	private void initLayout() {
		et_opinionContent = (EditText) thisView
				.findViewById(R.id.et_opinionContent);
		et_contact = (EditText) thisView.findViewById(R.id.et_contact);
		button_tijiao = (Button) thisView.findViewById(R.id.button_tijiao);
		feedback_type = (Spinner) thisView.findViewById(R.id.spinner_type);
		feedback_type.setBackgroundResource(R.drawable.common_downlist_sanjiao_active);
		feedback_type.setPrompt("意见类型");
		feedback_type.setAdapter(new SpinnerAdaper(context));

		MyOnFocusChangeListener myOnFocusChangeListener = new MyOnFocusChangeListener();
		et_opinionContent.setOnFocusChangeListener(myOnFocusChangeListener);
		et_contact.setOnFocusChangeListener(myOnFocusChangeListener);

	}

	private void getData() {
		opinionContent = et_opinionContent.getText().toString().trim();
		contact = et_contact.getText().toString().trim();
	}

	private void setOnClickListener() {

		button_tijiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean check = checkData();
				if (check) {
					requestServer();
				}
			}
		});

		// 意见类型
		feedback_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				feedback_text = feedback_number[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	/**
	 * 检查数据
	 * 
	 * @return
	 */
	private boolean checkData() {
		getData();
		if (opinionContent != null && opinionContent.equals("")) {
			Toast.makeText(context, "请填写意见内容", 0).show();
			return false;
		} else if (500 < opinionContent.length()) {
			Toast.makeText(context, "意见内容在500字以内", 0).show();
			return false;
		} else if (contact != null && contact.equals("")) {
			Toast.makeText(context, "请填写联系方式", 0).show();
			return false;
		} else if (CommonUtil.isEmail(contact) || CommonUtil.isQQ(contact)
				|| CommonUtil.isMobileNO(contact)) {
			return true;
		} else {
			Toast.makeText(context, "联系方式格式错误，请重新输入", 0).show();
			return false;
		}

	}

	private void requestServer() {

		Map map = new HashMap();
		map.put("feedbackTxt", opinionContent);// 反馈内容
		map.put("contact", contact);
		map.put("feedbackType", feedback_text);// 意见反馈类型
		String servicePara = JSON.toJSONString(map);
		TipUitls.Log(TAG, "servicePara----->" + servicePara);
		ServiceEngin.Request(context, "04_I01", "webFeedback", servicePara,
				new EnginCallback(context) {

					private CommonDialog commonDialog;

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", getResources().getString(
										R.string.request_failed)).show();
					}

					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						String result = null;
						try {
							result = Des3.decode(arg0.result.toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						TipUitls.Log(TAG, "result------>" + result);
						if (result != null
								&& JSONObject.parseObject(result) != null) {
							JSONObject object = JSONObject.parseObject(result);
							String resultCode = object.getString("ResultCode");
							String resultMsg = object.getString("ResultMsg");
							if ("0".equals(resultCode)) {
								commonDialog = new CommonDialog(context, 1,
										"确定", null, new OnClickListener() {

											@Override
											public void onClick(View v) {

												commonDialog.dismiss();
												thisManager
														.popBackStackImmediate();
											}
										}, null, "提示", "提交成功!");
								commonDialog.show();
							} else if (!"99".equals(resultCode)) {
								new CommonDialog(context, 1, "确定", null, null,
										null, "提示", resultMsg).show();
							}
						}
					}

				});

	}

	private class MyOnFocusChangeListener implements OnFocusChangeListener {
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if (!hasFocus) {
				checkData();
			}
		}
	}

	class SpinnerAdaper extends BaseAdapter {

		LayoutInflater inflater;

		public SpinnerAdaper(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return feedback_string.length;
		}

		@Override
		public Object getItem(int position) {
			return feedback_string[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = inflater.inflate(R.layout.common_simple_spinner_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv1);
			tv.setTextSize(17f);

			tv.setText(feedback_string[position]);
			return view;
		}
	}
}
