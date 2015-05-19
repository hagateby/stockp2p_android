package com.ktsf.framework;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.stockp2p.R;
import com.ktsf.common.data.Constants;
import com.ktsf.common.data.MyApplication;
import com.ktsf.common.db.DBManager;
import com.ktsf.common.db.PositionPlace;
import com.ktsf.common.service.SyncService;
import com.ktsf.framework.Framework;
import com.ktsf.framework.Frameworkdate;
import com.ktsf.framework.Manager;

public class PositionActivity extends FragmentActivity {

	private MyApplication app;//
	private LinearLayout layout_float;
	private TextView commit;
	private ListView listview_child;
	private ListView listview_father;
	private SQLiteDatabase db;
	private ArrayList<PositionPlace> father_list;
	private ArrayList<PositionPlace> child_list;
	private ArrayList<PositionPlace> child_child_list;
	private String provice = null;
	private String city = null;
	private String street = null;
	private PositionPlace positionPlace;
	private ListView listview_child_child;
	private MyAdapter myChildChildAdapter;
	private View view;
	private View view1;
	private int height;
	private int width;
	private View view2;
	private float startX;
	private float newX;
	private LinearLayout layout_listview;
	private boolean isFirst = true;
	private PopupWindow popwindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.position_activity);
		app = ((MyApplication) getApplicationContext());

		initDatabase();
		init();

		positionPlace = new PositionPlace();
		father_list = positionPlace.findByLevel(db, "organization_level", "2");
		final MyAdapter myFatherAdapter = new MyAdapter(this, father_list, 1);// false为父列表
		// for (int i = 0; i < father_list.size(); i++) {
		// child_list = positionPlace.findByParent_id(db, father_list.get(i)
		// .getOrganization_id(), "4");
		// // if (child_list != null) {
		// // myFatherAdapter.haveSubItem(i);
		// // }
		// }

		listview_father.setAdapter(myFatherAdapter);
		listview_father.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				provice = null;
				city = null;
				street = null;
				myFatherAdapter.selectedPosition(arg2);
				myFatherAdapter.notifyDataSetChanged();
				provice = father_list.get(arg2).getOrganization_id();
				child_list = positionPlace.findByParent_id(db,
						father_list.get(arg2).getOrganization_id(), "3");// 3为第二级列表在数据里的标志
				final MyAdapter myChildAdapter = new MyAdapter(
						PositionActivity.this, child_list, 2);// 2为第二级列表
				listview_child.setAdapter(myChildAdapter);
				listview_child.setVisibility(View.VISIBLE);
				System.out.println("listview_child");
				// if (child_child_list == null) {
				// city = child_list.get(0)
				// .getOrganization_id();
				// }
				listview_child
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								child_child_list = positionPlace
										.findByParent_id(db,
												child_list.get(arg2)
														.getOrganization_id(),
												"4");// 4为第三级列表的标志
								myChildAdapter.selectedPosition(arg2);
								System.out.println("arg2---->" + arg2);
								myChildAdapter.notifyDataSetChanged();
								city = child_list.get(arg2)
										.getOrganization_id();

								myChildChildAdapter = new MyAdapter(
										PositionActivity.this,
										child_child_list, 3);// 3为第三级列表
								listview_child_child
										.setAdapter(myChildChildAdapter);

								if (child_child_list != null) {// 当第三级列表为空不跳转到第三页面
									listview_father.setVisibility(View.GONE);
									listview_child_child
											.setVisibility(View.VISIBLE);
									dialog();// toast 的滑动提示信息,只在第一次进入第二页面时显示
								}
								listview_child_child
										.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												// TODO Auto-generated method
												// stub

												myChildChildAdapter
														.selectedPosition(position);
												myChildChildAdapter
														.notifyDataSetChanged();
												street = child_child_list.get(
														position)
														.getOrganization_id();
												System.out
														.println("listview_child_child----->"
																+ street);
											}
										});
							}

						});
			}
		});

		commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("next", "PositionActivity------" + provice + "/" + city
						+ "/" + street);
				if (listview_father.getVisibility() == View.GONE) {
					if (provice == null || city == null) {
						Toast.makeText(PositionActivity.this, "请选择完整地区", 0)
								.show();
					} else {
						sumitMethod();
					}
				} else {
					if (provice == null || city == null) {
						Toast.makeText(PositionActivity.this, "请选择完整地区", 0)
								.show();
					} else {
						sumitMethod();
					}

				}

			}
		});
	}

	private void dialog() {
		if (isFirst == true) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("提示");
			dialog.setMessage("向右滑动,可以重新选择");
			dialog.setCancelable(false);
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			AlertDialog alert = dialog.create();
			alert.show();

			isFirst = false;

		}
	}

	private void sumitMethod() {
		String deviceId = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
				.getDeviceId();
		SharedPreferences sPreferences = PositionActivity.this
				.getSharedPreferences("positionaddress", Context.MODE_PRIVATE);
		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(provice).append("--");
		sbBuffer.append(city).append("--");
		sbBuffer.append(street);
		String str = sbBuffer.toString();
		sPreferences.edit().putString("regionCode", str).commit();
		sPreferences.edit().putString("deviceId", deviceId).commit();
		Intent syncService = new Intent(PositionActivity.this,
				SyncService.class);
		startService(syncService);
		Log.i("定位页面", "开启同步服务");

		finish();
		Manager.branch(this, Constants.moduleList.get(0));
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	private void initDatabase() {
		DBManager dBManager = new DBManager(this);
		db = dBManager.openDatabase();
		app.db = db;
	}

	private void init() {
		// TODO Auto-generated method stub

		layout_float = (LinearLayout) findViewById(R.id.layout_float);
		layout_float.setBackgroundColor(getResources().getColor(
				R.color.listgrey));
		commit = (TextView) findViewById(R.id.textview_commit);
		listview_child = (ListView) findViewById(R.id.listview_child);
		listview_child.setBackgroundColor(getResources().getColor(
				R.color.listgrey));
		listview_father = (ListView) findViewById(R.id.listview_father);
		listview_father.setBackgroundColor(getResources().getColor(
				R.color.listgrey));
		listview_child_child = (ListView) findViewById(R.id.listview_child_child);
		listview_child_child.setBackgroundColor(getResources().getColor(
				R.color.listgrey));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return super.onKeyDown(keyCode, event);
	}

	private class MyAdapter extends BaseAdapter implements OnTouchListener {

		private List<PositionPlace> list = new ArrayList<PositionPlace>();
		private List<Integer> listItem = new ArrayList<Integer>();
		private Boolean isHaveSub = true;
		private int selectedPosition = -1;
		private int isSub;
		private Context context;
		private GestureDetector gestureDetector;

		public MyAdapter(Context context, List list, int bl) {
			this.context = context;
			gestureDetector = new GestureDetector(context,
					new MyGestureListener());
			if (list != null) {
				this.list = list;
			} else {
			}

			this.isSub = bl;
		}

		private void selectedPosition(int position) {
			this.selectedPosition = position;
		}

		// private void haveSubItem(int item) {
		// this.listItem.add(item);
		// }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			arg1 = View.inflate(context, R.layout.position_activity_item, null);
			View viewRed = (View) arg1.findViewById(R.id.view_item);
			viewRed.setBackgroundColor(getResources().getColor(R.color.listred));
			TextView viewContent = (TextView) arg1
					.findViewById(R.id.textview_item);
			viewContent.setText(list.get(arg0).getOrganization_name());
			ImageView viewGuide = (ImageView) arg1
					.findViewById(R.id.view_guide);

			// // 有子类的item 显示图标
			// for (int i = 0; i < listItem.size(); i++) {
			// if (arg0 == listItem.get(i)) {
			// viewGuide.setVisibility(View.VISIBLE);
			// }
			// }
			// 父列表设置
			if (isSub == 1) {
				viewRed.setVisibility(View.INVISIBLE);
				viewContent.setTextColor(Color.BLACK);
				arg1.setBackgroundColor(getResources().getColor(R.color.white));
				if (arg0 == selectedPosition) {

					viewRed.setVisibility(View.VISIBLE);
					viewContent.setTextColor(getResources().getColor(
							R.color.listred));
					arg1.setBackgroundColor(getResources().getColor(
							R.color.listgrey));
				}
			}
			// 二级列表设置

			if (isSub == 2 && listview_father.getVisibility() == View.VISIBLE
					&& child_child_list != null) {// 第二列表第一页面状态
				viewContent.setTextColor(Color.BLACK);
				viewRed.setVisibility(View.INVISIBLE);
				listview_child.setBackgroundColor(getResources().getColor(
						R.color.listgrey));
				if (arg0 == selectedPosition) {
					viewRed.setVisibility(View.INVISIBLE);
				}
				notifyDataSetChanged();
			} else if (isSub == 2
					&& listview_father.getVisibility() == View.GONE
					&& child_child_list != null) {// 跳转第三级列表后状态
				viewContent.setTextColor(Color.BLACK);
				viewRed.setVisibility(View.INVISIBLE);
				listview_child.setBackgroundColor(getResources().getColor(
						R.color.white));

				if (arg0 == selectedPosition) {
					arg1.setBackgroundColor(getResources().getColor(
							R.color.listgrey));
					viewRed.setVisibility(View.VISIBLE);
				}
				notifyDataSetChanged();
			} else if (isSub == 2 && child_child_list == null) {// 当没有第三级列表时不会跳转第三页面
				viewContent.setTextColor(Color.BLACK);
				viewRed.setVisibility(View.INVISIBLE);
				listview_child.setBackgroundColor(getResources().getColor(
						R.color.listgrey));

				if (arg0 == selectedPosition) {
					arg1.setBackgroundColor(getResources().getColor(
							R.color.listred));
					viewRed.setVisibility(View.VISIBLE);
				}
				notifyDataSetChanged();
			}

			// 三级列表设置
			if (isSub == 3) {
				viewRed.setVisibility(View.INVISIBLE);
				viewContent.setTextColor(Color.BLACK);
				arg1.setBackgroundColor(getResources().getColor(
						R.color.listgrey));

				if (list.size() == 1) {
					viewContent.setTextColor(Color.WHITE);
					arg1.setBackgroundColor(getResources().getColor(
							R.color.listred));
				}
				if (arg0 == selectedPosition) {
					viewContent.setTextColor(Color.WHITE);
					arg1.setBackgroundColor(getResources().getColor(
							R.color.listred));
				}
				notifyDataSetChanged();
			}

			arg2.setOnTouchListener(this);

			return arg1;
		}

		private class MyGestureListener extends SimpleOnGestureListener {

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if (e2 == null || e1 == null) {// 一个手指点中一二级列表就会报空
					return false;
				}
				// e2.getX() - e1.getX() > 20 &&
				if (e2.getX() - e1.getX() > 20 && e1.getY() - e2.getY() < 200
						&& listview_father.getVisibility() == View.GONE) {
					listview_father.setVisibility(View.VISIBLE);
					listview_child_child.setVisibility(View.GONE);
					// 返回时二三级列表都为不选中状态,要把数据置为空
					city = null;
					street = null;
				}
				// TODO Auto-generated method stub
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return gestureDetector.onTouchEvent(event);
		}

	}

}
