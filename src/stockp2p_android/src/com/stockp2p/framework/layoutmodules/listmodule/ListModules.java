package com.stockp2p.framework.layoutmodules.listmodule;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.http.Header;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.stockp2p.R;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.common.db.FrameWork_Frame;
import com.stockp2p.common.db.FrameWork_Frame_DAO;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.util.PubFun;
import com.stockp2p.components.login.LoginActicity;
import com.stockp2p.framework.baseframe.BaseFragment;
import com.stockp2p.framework.baseframe.Manager;

@SuppressLint("ValidFragment")
public class ListModules extends BaseFragment {

	private static final String TAG = "ListModules";
	@ViewInject(R.id.list_modules_lv_listview)
	private ListView listview;
	private View thisView;
	private List<FrameWork_Frame> moduleList;
	private MyAdapter adapter;
	private FrameWork_Frame framework;

	private SQLiteDatabase db;
	private String code;
	private String path;
	private BitmapDisplayConfig config;
	private com.lidroid.xutils.BitmapUtils bitmapUtils;
	private Cursor c;

	/**
	 * 布局背景
	 */
	@ViewInject(R.id.list_modules_ll_layout)
	private LinearLayout layoutBackground;
	/**
	 * 登录按钮
	 */
	private ImageView loginButton;
	/**
	 * 推送的信息数量
	 */
	private TextView infor;
	/**
	 * 推送的信息数量Layout
	 */
	private RelativeLayout inforLayout;
	
	public ListModules(){
		
	}
	
	public ListModules(FrameWork_Frame framework) {
		System.out.println("ListModules(Framework framework)");
		this.framework = framework;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("onCreateView");
		thisView = inflater.inflate(R.layout.addmenu_list_modules, null);
		
	    code =PubFun.getimageDownloadUrl();

		Log.e("获取数据库", code);
		
		//加载图片
		loadImage(bitmapUtils ,config); 
		
		return thisView;
	}
	
	//加载图片
	public  void loadImage (BitmapUtils bitmapUtils ,BitmapDisplayConfig config )
	{
		
		if (bitmapUtils == null) {
			bitmapUtils = ((MyApplication) getActivity().getApplication())
					.getBitmapUtils(getActivity());
		}
		if (config == null) {
			config = new BitmapDisplayConfig();
			config.setLoadFailedDrawable(getResources().getDrawable(
					R.drawable.new_icon_s_small));
			config.setLoadingDrawable(getResources().getDrawable(
					R.drawable.new_icon_s_small));
		}
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init(thisView, framework.getModuleName());
	}

	@Override
	public void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);
		
		//查询此窗口下的子窗口
		moduleList = FrameWork_Frame_DAO.findByParentId(myApplication.db,
				framework.getModuleId(), context);
		
		adapter = new MyAdapter(getActivity());
		
		System.out.println("listview.getHeaderViewsCount(--->"+listview.getHeaderViewsCount());
		
		if (listview.getHeaderViewsCount() == 0) {
			
			listview.addHeaderView(listTopState());
		}
		
		listview.setAdapter(adapter);
	}
	
	/**
	 * 列表头图片状态
	 */
	private View listTopState() {
		View headView = LayoutInflater.from(context).inflate(
				R.layout.framework_listmodules_head, null);
		// 广告图
		RelativeLayout picture = (RelativeLayout) headView
				.findViewById(R.id.list_modules_iv_picture);
		// 广告图上的登录按钮
		loginButton = (ImageView) headView
				.findViewById(R.id.list_modules_iv_login);
		// 登录后广告位置显示的布局
		LinearLayout pictureLayout = (LinearLayout) headView
				.findViewById(R.id.list_modules_iv_pictureafter);
		// 登录后显示的用户名
		TextView userText = (TextView) headView
				.findViewById(R.id.list_modules_tv_user);
		// 登录后显示的按钮左边
		Button leftButton = (Button) headView
				.findViewById(R.id.list_modules_bt_num);
		leftButton.setText("已关联保单"+UserInfoManager.getInstance().getPolicyCount());
		// 登录后显示的按钮右边
		Button rightButton = (Button) headView
				.findViewById(R.id.list_modules_bt_time);
		rightButton.setText("登录时间"+UserInfoManager.getInstance().getLoginDate());
		// 推送信息的数量 默认不显示
		inforLayout = (RelativeLayout) headView
				.findViewById(R.id.list_modules_tv_x4);
		// 推送信息的数量 默认不显示
		infor = (TextView) headView.findViewById(R.id.list_modules_tv_infor);

		if ("4".equals(framework.getModuleId())) {
			// 在我的保险中
			if (!UserInfoManager.getInstance().isLogin()) {
				// 登录前
				picture.setVisibility(View.VISIBLE);
				pictureLayout.setVisibility(View.GONE);
				layoutBackground.setBackgroundResource(R.drawable.framework_bk);
				if ("2".equals(framework.getModuleId())
						|| "2".equals(framework.getParentId())) {
					picture.setBackgroundResource(R.drawable.common_picture1);
				} else if ("4".equals(framework.getModuleId())
						|| "4".equals(framework.getParentId())) {
					picture.setBackgroundResource(R.drawable.common_picture2);
				}
			} else {
				// 登录后
				picture.setVisibility(View.GONE);
				pictureLayout.setVisibility(View.VISIBLE);
				layoutBackground
						.setBackgroundResource(R.drawable.framework_listmodules_bk);
				userText.setText("帐号: "
						+ UserInfoManager.getInstance().getUserName());
			}
		} else {
			// 不在我的保险中
			picture.setVisibility(View.VISIBLE);
			loginButton.setVisibility(View.GONE);
			pictureLayout.setVisibility(View.GONE);
			layoutBackground.setBackgroundResource(R.drawable.framework_bk);
			if ("2".equals(framework.getModuleId())
					|| "2".equals(framework.getParentId())) {
				picture.setBackgroundResource(R.drawable.common_picture1);
			} else if ("4".equals(framework.getModuleId())
					|| "4".equals(framework.getParentId())) {
				picture.setBackgroundResource(R.drawable.common_picture2);
			}

		}
		addListeners();
		return headView;
	}

	private void addListeners() {

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, LoginActicity.class);
				intent.putExtra("framework", framework);
				intent.putExtra("isExit", "false");
				context.startActivity(intent);
			}
		});

		inforLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				Intent intent = new Intent(context,
						BaidupushMessageCenterActivity.class);
				context.startActivity(intent);
				*/
			}
		});
	}
   //点击item 项后的动作
	@OnItemClick(R.id.list_modules_lv_listview)
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// 由于添加了 head
		if (position == 0) {

		} else {
			position = position - 1;
			//1为列表,有下一级菜单  2为详情  3为 webview 4 为打开另一个程序
			if ("1".equals(moduleList.get(position).getModuleType())) {
				thisManager
						.beginTransaction()
						.replace(R.id.tab_container,
								new ListModules(moduleList.get(position)))
						.addToBackStack(TAG).commit();
			} else {
				Manager.branch(context, moduleList.get(position));
			}

		}

	}

	private class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private Bitmap bitmap;
		private InputStream is;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return moduleList == null ? 0 : moduleList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return moduleList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//ITEM对象
			ViewHolder viewHolder;
			
			if (convertView == null) {
				
				viewHolder = new ViewHolder();
				//设置每个TIEM样式
				convertView = mInflater.inflate(R.layout.framework_item_list,
						null);
				
				viewHolder.itemPicture = (ImageView) convertView
						.findViewById(R.id.item_list_iv_picture);
				
				viewHolder.itemTxt = (TextView) convertView
						.findViewById(R.id.item_list_tv_txt);
				
				viewHolder.itemLine = convertView
						.findViewById(R.id.item_list_line);
				
				viewHolder.itemInfor = (TextView) convertView
						.findViewById(R.id.item_list_tv_infor);
				
				convertView.setTag(viewHolder);
				
			} else {
				
				viewHolder = (ViewHolder) convertView.getTag();
				
			}
			try {
				
				is = getResources().getAssets().open(
						moduleList.get(position).getThumbnailName());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.e("is", "" + is);
			if (is != null) {
				bitmap = BitmapFactory.decodeStream(is);
				Log.e("bitmap", "" + bitmap);
				if (bitmap == null) {
					viewHolder.itemPicture
							.setImageResource(R.drawable.new_icon_s_small);
				} else {
					viewHolder.itemPicture.setImageBitmap(bitmap);
				}
			} else {
				path = code + moduleList.get(position).getThumbnailName();

				bitmapUtils.display(viewHolder.itemPicture, path, config, null);

			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			is = null;

			viewHolder.itemTxt.setText(moduleList.get(position).moduleName);
			if (position == 0) {
				viewHolder.itemLine.setVisibility(View.GONE);
			} else {
				viewHolder.itemLine.setVisibility(View.INVISIBLE);
			}
		
				viewHolder.itemInfor.setVisibility(View.GONE);
			
			return convertView;
		}
	}

	private class ViewHolder {
		private TextView itemTxt;
		private ImageView itemPicture;
		private View itemLine;
		private TextView itemInfor;
		private ImageView itemSelected;
	}

}