package com.stockp2p.framework.baseframe;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.stockp2p.R;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.data.Framework;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.components.login.LoginActicity;
import com.stockp2p.framework.Frameworkdate;
import com.stockp2p.util.pubfun;

public class MainToolsBar extends BaseFragment {
	private static final String TAG = "MainToolsBar";
	private View thisView;
	private View inflateView = null;
	
	protected static int bottomBarStatus;
	public static final int MAINPAGE = 1;
	protected static final int NCIINTRODUCTION = 2;
	protected static final int VIPSERVICE = 3;
	protected static final int MYINSURANCE = 4;
	
	/**
	 * isExit != null时显示底栏 isExit 值为 true 返回退出应用 isExit 值为 false 返回上一页面
	 */
	protected String isExit = null;
	private String isExit2 = "true";
	protected Framework framework_;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)  {
		// TODO Auto-generated method stub
		
		framework_ = (Framework) getIntent().getParcelableExtra(
				"framework");
		
		thisView = inflater.inflate(R.layout.framework_base_activity, null);
	
		
		return thisView;
	}

	public void init(String url) {
	

	}

	private void initBottomBarStatus() {
		if (Constants.moduleList == null) {
			Constants.moduleList = Frameworkdate.findByParentId(
					myApplication.db, "0", context);
		}
        //如果是登录页面
		if ( pubfun.isLogin(context.getClass().getName().toString())) {
			
			ViewStub stub = (ViewStub) context.findViewById(R.id.bottom);
			
			inflateView = stub.inflate();
			
			initBottombar();

		} else {
			
			//给每一framework增加操作bottom
			addbottomtobar();
		}
	}
   private void addbottomtobar()
   {
	   List<Integer> index = new ArrayList<Integer>();
		//给每一framework增加操作bottom
		for (int i = 0; i < Constants.moduleList.size(); i++) {
			TipUitls.Log(TAG, "---moduleList-->"
					+ Constants.moduleList.get(i).getPackageName());
			if (( Constants.moduleList.get(i)
					.getPackageName()).equals(context.getClass().getName()
					.toString()))
			{
				index.add(i);
				ViewStub stub = (ViewStub) context
						.findViewById(R.id.bottom);
				if (stub != null) {
					inflateView = stub.inflate();
				}

			}
		}

		if (index.size() == 1) {
			setbottomBarStatus(index.get(0) + 1);
			initBottombar();
		} else {
			for (Integer num : index) {
			 if ( !Constants.moduleList.get(num).getModuleId()
								.equals(framework_.getParentId())) {
					isExit2  = "false";
				
				}
				setbottomBarStatus(num + 1);
				initBottombar();
			}

		}

		index.clear();
		index = null;
   }
	public void setbottomBarStatus(int i) {
		bottomBarStatus = i;
	}
   /**
	 * 底栏
	 */
	public void initBottombar() {
		
		TipUitls.Log(TAG, "initBottombar----->" + bottomBarStatus);
		int width = 0, height = 0;
		
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		
		Display display = wm.getDefaultDisplay();
		
		height = display.getHeight() / 12;
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, height);
		
		inflateView.setLayoutParams(lp);
		
		if (MAINPAGE == bottomBarStatus) {
			RadioButton tab_a = (RadioButton) inflateView
					.findViewById(R.id.base_activity_rb_taba);
//			if (tab_a.isChecked()) {
//				tab_a.setBackgroundResource(R.drawable.addmenu_selected);
//			}else{
//				tab_a.setBackgroundResource(R.drawable.activitynotfound);
//			}
			tab_a.setChecked(true);
		} else if (NCIINTRODUCTION == bottomBarStatus) {
			RadioButton tab_b = (RadioButton) inflateView
					.findViewById(R.id.base_activity_rb_tabb);
			tab_b.setChecked(true);
		} else if (VIPSERVICE == bottomBarStatus) {
			RadioButton tab_c = (RadioButton) inflateView
					.findViewById(R.id.base_activity_rb_tabc);
			tab_c.setChecked(true);
		} else if (MYINSURANCE == bottomBarStatus) {
			RadioButton tab_d = (RadioButton) inflateView
					.findViewById(R.id.base_activity_rb_tabd);
			tab_d.setChecked(true);
		}

		RadioGroup group = (RadioGroup) inflateView
				.findViewById(R.id.base_activity_rg_bottombar);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.base_activity_rb_taba:
					finish();
					if ("1".equals(Constants.moduleList.get(0).getIsLogin())
							&& !UserInfoManager.getInstance().isLogin()) {
						setbottomBarStatus(1);
						context.startActivity(new Intent(context,
								LoginActicity.class).putExtra("isExit", "true")
								.putExtra("framework",
										Constants.moduleList.get(0)));
						overridePendingTransition(0, 0);
					} else {
						Manager.branch(context, Constants.moduleList.get(0));
					}
					break;
				case R.id.base_activity_rb_tabb:
					finish();
					if ("1".equals(Constants.moduleList.get(1).getIsLogin())
							&& !UserInfoManager.getInstance().isLogin()) {
						setbottomBarStatus(2);
						context.startActivity(new Intent(context,
								LoginActicity.class).putExtra("isExit", "true")
								.putExtra("framework",
										Constants.moduleList.get(1)));
						overridePendingTransition(0, 0);
					} else {
						Manager.branch(context, Constants.moduleList.get(1));
					}
					break;
				case R.id.base_activity_rb_tabc:
					finish();
					if ("1".equals(Constants.moduleList.get(2).getIsLogin())
							&& !UserInfoManager.getInstance().isLogin()) {
						setbottomBarStatus(3);
						context.startActivity(new Intent(context,
								LoginActicity.class).putExtra("isExit", "true")
								.putExtra("framework",
										Constants.moduleList.get(2)));
						overridePendingTransition(0, 0);
					} else {
						Manager.branch(context, Constants.moduleList.get(2));
					}
					break;
				case R.id.base_activity_rb_tabd:
					finish();
					if ("1".equals(Constants.moduleList.get(3).getIsLogin())
							&& !UserInfoManager.getInstance().isLogin()) {
						setbottomBarStatus(4);
						context.startActivity(new Intent(context,
								LoginActicity.class).putExtra("isExit", "true")
								.putExtra("framework",
										Constants.moduleList.get(3)));
						overridePendingTransition(0, 0);
					} else {
						Manager.branch(context, Constants.moduleList.get(3));
					}
					
					break;

				}
			}
		});

	}   
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

}
