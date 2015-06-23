package com.stockp2p.framework.layoutmodules.chkboardmodule;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stockp2p.R;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.common.db.FrameWork_Frame;
import com.stockp2p.common.db.FrameWork_Frame_DAO;
import com.stockp2p.common.util.FilesUtil;
import com.stockp2p.common.view.LPPageControlView;
import com.stockp2p.common.view.LPScrollLayout;
import com.stockp2p.framework.baseframe.BaseFragment;
import com.stockp2p.framework.baseframe.Manager;

@SuppressLint("ValidFragment")
public class MenuColumn extends BaseFragment {

	List<FrameWork_Frame> menuList;
	private View thisView;
	final static int PAGE_SIZE = 12;// 每页显示菜单总数
	final static int NUM_LINES = 4;// 每页显示几行
	static boolean isLongpressed = false;// 不是编辑状态
	static int pagepressed = 0;// 点击了哪个页面

	@ViewInject(R.id.menu_column_lps_container)
	private LPScrollLayout sLayout;
	private FrameWork_Frame framework;
	/**
	 * 标题栏
	 */
	private RelativeLayout titleLayout;
	/**
	 * 分享
	 */
	private Button buttonMore;
	private SQLiteDatabase db;
	private Cursor c;
	private String code;
	private com.lidroid.xutils.BitmapUtils bitmapUtils;
	private BitmapDisplayConfig config;
	private String path;

	public MenuColumn() {
	}

	// public MenuColumn(Framework framework) {
	// this.framework = framework;
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.quicksrv_menu_column, null);

		init(thisView, "菜单删除");
		db = myApplication.db;
		if (c == null) {
			c = db.rawQuery("select * from code where codetype=?",
					new String[] { "imageDownloadUrl" });
			if (c.moveToFirst()) {
				code = c.getString(c.getColumnIndex("code"));
			}
		}
		Log.e("获取数据库", code);
		if (bitmapUtils == null) {
			bitmapUtils = ((MyApplication) getActivity().getApplication())
					.getBitmapUtils(getActivity());
		}
		if (config == null) {
			config = new BitmapDisplayConfig();
			config.setLoadFailedDrawable(getResources().getDrawable(
					R.drawable.new_icon_b_big));
			config.setLoadingDrawable(getResources().getDrawable(
					R.drawable.new_icon_b_big));
		}
		addListeners();
		return thisView;
	}

	private void addListeners() {
		// 完成
		buttonMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				titleLayout.setVisibility(View.GONE);
				isLongpressed = false;
				refreshFragment();
			}
		});
	}

	@Override
	public void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);

		// topbar设置
		titleLayout = (RelativeLayout) context
				.findViewById(R.id.base_activity_rl_titlelayout);
		buttonMore = (Button) context
				.findViewById(R.id.base_activity_bt_base_title_more);
		titleLayout.setVisibility(View.GONE);
		if (isLongpressed == true) {
			titleLayout.setVisibility(View.VISIBLE);
			context.findViewById(R.id.base_activity_bt_titleback)
					.setVisibility(View.GONE);
			buttonMore.setBackgroundDrawable(new BitmapDrawable());
			buttonMore.setText("完成");
			buttonMore.setTextSize(14);
			
			
		}
		// 加载数据
		menuList = FrameWork_Frame_DAO
				.findByFixedPage(myApplication.db, "1", context);
		if (menuList == null) {
			return;
		}
     /*lwh
		String strs = FilesUtil.readFiles(getActivity(), "module");
		
		if (strs != null && !"[]".equals(strs)) {
			
			menuList.addAll(JSON.parseArray(strs, Framework.class));

			// 退出程序后 perlist 清0了 所以要重新赋值 而且必须是
			// myApplication.perList.add(JSON.toJSON(framework)) 把framwork转换成
			// json 再 add
			myApplication.perList.clear();
			for (Framework framework : JSON.parseArray(strs, Framework.class)) {
				myApplication.perList.add(JSON.toJSON(framework));
			}

		}
		*/
		menuList.addAll(FrameWork_Frame_DAO.findByModuleId(myApplication.db, "99",
				context));

		// 一共要显示 pageNo 页 每页为一个 gridView
		int pageNo = (int) Math.ceil(menuList.size() / (float) PAGE_SIZE);
		for (int i = 0; i < pageNo; i++) {
			final int index = i;
			GridView gridView = new GridView(getActivity());
			gridView.setNumColumns(PAGE_SIZE / NUM_LINES);
//			gridView.setLayoutParams(new LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			//gridView.setGravity(Gravity.CENTER);
			gridView.setPadding(10, 10, 10, 10);
			gridView.setHorizontalSpacing(10);
			//gridView.setVerticalSpacing(10);
			gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			gridView.setVerticalScrollBarEnabled(false);
			MenuAdapter myadapter = new MenuAdapter(i);
			gridView.setAdapter(myadapter);
			sLayout.addView(gridView);

			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int position = arg2 + PAGE_SIZE * index;// 点击的菜单所在页面
					if (isLongpressed == true
							&& !"1".equals(menuList.get(position).fixedPage)) {
						FrameWork_Frame framework = menuList.get(position);
						myApplication.perList.remove(JSON.toJSON(framework));
						FilesUtil.writeFiles(getActivity(), "module",
								JSON.toJSONString(myApplication.perList));
						pagepressed = index;
						refreshFragment();
					} else {
						// framework = menuList.get(position);
						// 在跳转到添加菜单时 finish 和去动画处理 否则会不刷新

						if ("99".equals(menuList.get(position).getFrameId())) {
							if (isLongpressed != true) {
								context.finish();
								context.overridePendingTransition(0, 0);
								Manager.branch(getActivity(),
										menuList.get(position));
							}
						} else {
							Manager.branch(getActivity(),
									menuList.get(position));
						}

					}
				}
			});

			gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					isLongpressed = true;
					pagepressed = index;
					refreshFragment();
					return true;
				}

			});

		}

		// 得到点的布局
		LPPageControlView pageControlView = (LPPageControlView) thisView
				.findViewById(R.id.menu_column_lpp_dot);
		pageControlView.bindScrollLayout(sLayout, 100);
		// 仿止点击后回到第一页面
		if (isLongpressed == true) {
			sLayout.setToScreen(pagepressed);
			pageControlView.generatePageControl(pagepressed);
		}

	}

	/**
	 * 用来代替 adapter.notifyDataSetChanged() 用 LPScrollLayout 控件时刷新不管用
	 */
	private void refreshFragment() {
		thisManager.beginTransaction()
				.replace(R.id.home_page_rl_menucontainer, new MenuColumn())
				.commit();
	}

	private class MenuAdapter extends BaseAdapter {

		private ArrayList<FrameWork_Frame> modules;
		private Bitmap bitmap;
		private InputStream is;

		public MenuAdapter(int pageNo) {
			// 每页 modules 只取PAGE_SIZE个菜单
			modules = new ArrayList<FrameWork_Frame>();
			int i = pageNo * PAGE_SIZE;
			int iEnd = i + PAGE_SIZE;
			while ((i < menuList.size()) && (i < iEnd)) {
				modules.add(menuList.get(i));
				i++;
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return modules.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return modules.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.quicksrv_item_grid, null);
				viewHolder = new ViewHolder();
				viewHolder.itemPicture = (ImageView) convertView
						.findViewById(R.id.item_grid_iv_picture);
				viewHolder.itemPicture.setScaleType(ScaleType.FIT_XY);
				viewHolder.itemTxt = (TextView) convertView
						.findViewById(R.id.item_grid_tv_txt);
				viewHolder.itemDel = (ImageView) convertView
						.findViewById(R.id.item_grid_iv_del);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// path = code + modules.get(position).getIconName();
			// bitmapUtils.display(viewHolder.itemPicture, path, config,null);
			// try {
			// InputStream is = getResources().getAssets().open(
			// modules.get(position).getIconName());
			// bitmap = BitmapFactory.decodeStream(is);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			//
			// //
			// if (bitmap == null) {
			// viewHolder.itemPicture.setImageResource(R.drawable.icon_b_10);
			// } else {
			// viewHolder.itemPicture.setImageBitmap(bitmap);
			// }
			// viewHolder.itemTxt.setText(modules.get(position).getModuleName());
			try {
				is = getResources().getAssets().open(
						modules.get(position).getIconName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.e("is", "" + is);
			if (is != null) {
				bitmap = BitmapFactory.decodeStream(is);
				Log.e("bitmap", "" + bitmap);
				if (bitmap == null) {
					viewHolder.itemPicture
							.setImageResource(R.drawable.new_icon_b_big);
				} else {
					viewHolder.itemPicture.setImageBitmap(bitmap);
				}
			} else {
				path = code + modules.get(position).getIconName();

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

			// 图标下文字暂时不用,xml 中设为 gone
			viewHolder.itemTxt.setText(modules.get(position).frameName);
			// 是否显示叉
			if (isLongpressed == true
					&& !"1".equals(modules.get(position).fixedPage)) {
				viewHolder.itemDel.setVisibility(View.VISIBLE);
			} else {
				viewHolder.itemDel.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}
	}

	private class ViewHolder {
		private TextView itemTxt;
		private ImageView itemPicture;
		private ImageView itemDel;
	}

}
