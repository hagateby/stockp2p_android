package com.stockp2p.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.util.LogUtils;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.data.Framework;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.common.db.Channel;
import com.stockp2p.common.db.CodeType;
import com.stockp2p.common.db.Content;
import com.stockp2p.common.db.DBManager;
import com.stockp2p.common.db.Image;
import com.stockp2p.common.db.Network;
import com.stockp2p.common.db.Region;
import com.stockp2p.common.db.Version;
import com.stockp2p.common.serviceengin.ServiceEngin;


public class JsonToDb {

	private static String TAG = "JsonToDb";
	private SQLiteDatabase db;

	/**
	 * 栏目表
	 */
	protected List<Channel> channel;
	/**
	 * 码表
	 */
	protected List<CodeType> codeType;
	/**
	 * 内容表
	 */
	protected List<Content> content;

	/**
	 * 图片表
	 */
	protected List<Image> image;
	/**
	 * 框架表
	 */
	protected List<Framework> moduleList;
	/**
	 * 网点表
	 */
	protected List<Network> network;
	/**
	 * 省、市、区表
	 */
	protected List<Region> region;
	/**
	 * 版本
	 */
	protected Version version;
	/**
	 * 存放了更新时间和标志
	 */
	protected SharedPreferences sp;

	public void convert(final Context context) {

		MyApplication myApplication = (MyApplication) context
				.getApplicationContext();
		if (myApplication.db != null) {
			db = myApplication.db;
		} else {
			DBManager dBManager = new DBManager(context);
			db = dBManager.openDatabase();
			myApplication.db = db;
		}

		// String versionName = null;
		// try {
		// versionName = CommonUtil.getVersionName(context);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		TipUitls.Log(TAG,
				"JsonToDb 更------新--------开---------始------------------->");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 初始化两个SharedPreferences
				sp = context.getSharedPreferences("userprefs",
						Context.MODE_PRIVATE);
				SharedPreferences addressPreferences = context
						.getSharedPreferences("positionaddress",
								Context.MODE_PRIVATE);

				// 请求参数
				Map map = new HashMap<String, String>();
				// map.put("pointDate", "2013-01-01 00:00:01");// 上次请求时间
				map.put("pointDate", sp.getString("updateDate",
						Constants.UPDATEDATEORIGINAL));// 上次请求时间
				map.put("modelType", "1");// 设备类型1手机、2pad
				map.put("versionType", "1");// 版本类型1安卓2Ios、3pad、4ipad
				map.put("regionCode",
						addressPreferences.getString("regionCode", null));// 所属地区码，格式（省code--城市code--四级机构code）

				String servicePara = JSON.toJSONString(map);
				TipUitls.Log(TAG, "提交的参数servicePara---->" + servicePara);

				// 开始请求接口
				String result = ServiceEngin.Request(context, "03_I01",
						"updateData", servicePara);
				TipUitls.Log("JsonToDb", "接口返回的result---->" + result);
				if (result != null && JSON.parseObject(result) != null) {
					JSONObject object = JSON.parseObject(result);
					String resultCode = object.getString("ResultCode");
					String resultMsg = object.getString("ResultMsg");

					if ("0".equals(resultCode)) {
						// 保存 date
						sp.edit()
								.putString("updateDate",
										object.getString("NewDate")).commit();
						// Channel表
						channel = JSONArray.parseArray(
								object.getString("Channel"), Channel.class);
						// CodeType表
						codeType = JSONArray.parseArray(
								object.getString("CodeType"), CodeType.class);
						// Content表
						content = JSONArray.parseArray(
								object.getString("Content"), Content.class);
						// Image表
						image = JSONArray.parseArray(object.getString("Image"),
								Image.class);
						// Framework表
						moduleList = JSONArray.parseArray(
								object.getString("ModuleList"), Framework.class);
						// Network表
						network = JSONArray.parseArray(
								object.getString("Network"), Network.class);
						// Region表
						region = JSONArray.parseArray(
								object.getString("Region"), Region.class);
						// 版本表
						version = (Version) JSON.parseObject(
								object.getString("Version"), Version.class);

						// 开始事务
						startDb(context);

					} else {

					}
				}
			}
		}).start();

	}

	/**
	 * 事务
	 * 
	 * @param context
	 */
	private void startDb(Context context) {
		db.beginTransaction();// 开始事务
		try {
			TipUitls.Log(TAG, "版本更新标记：" + version.getOpeFlag());
			// 进入应用,Billboards类中版本更新时用到
			sp.edit().putString("updatedFlag", version.getOpeFlag()).commit();
			if ("1".equals(version.getOpeFlag())) {
				sp.edit().putString("versionCode", version.getVersionCode())
						.commit();
				sp.edit().putString("versionDesc", version.getVersionDesc())
						.commit();
				sp.edit().putString("uploadUrl", version.getUploadUrl())
						.commit();
			}

			TipUitls.Log(TAG, "channel------>" + channel);
			if (channel != null) {
				for (Channel channelObject : channel) {
					ContentValues cv = new ContentValues();
					cv.put("channelId", channelObject.getChannelId());
					cv.put("channelName", channelObject.getChannelName());
					cv.put("parentId", channelObject.getParentId());
					if (channelObject.getOpeFlag().equals("c")) {
						db.delete("channel", "channelId = ?",
								new String[] { channelObject.getChannelId()
										+ "" });
						db.insert("channel", null, cv);
					} else if (channelObject.getOpeFlag().equals("u")) {
						db.update("channel", cv, "channelId = ?",
								new String[] { channelObject.getChannelId()
										+ "" });
					} else if (channelObject.getOpeFlag().equals("d")) {
						db.delete("channel", "channelId = ?",
								new String[] { channelObject.getChannelId()
										+ "" });
					}
				}
			}

			TipUitls.Log(TAG, "codeType------>" + codeType);
			if (codeType != null) {
				for (CodeType codeTypeObject : codeType) {
					ContentValues cv = new ContentValues();
					cv.put("codetype", codeTypeObject.getParamTypeId());
					cv.put("code", codeTypeObject.getValueCode());
					cv.put("codename", codeTypeObject.getValueName());
					cv.put("id", codeTypeObject.getValueId());
					if (codeTypeObject.getOpeFlag().equals("c")) {
						db.delete("code", "codename = ?",
								new String[] { codeTypeObject.getValueName()
										+ "" });
						db.insert("code", null, cv);
					} else if (codeTypeObject.getOpeFlag().equals("u")) {
						db.update("code", cv, "id = ?",
								new String[] { codeTypeObject.getValueName()
										+ "" });
					} else if (codeTypeObject.getOpeFlag().equals("d")) {
						db.delete("code", "codename = ?",
								new String[] { codeTypeObject.getValueName()
										+ "" });
					}
				}
			}

			TipUitls.Log(TAG, "content------>" + content);
			if (content != null) {
				for (Content contentObject : content) {
					ContentValues cv = new ContentValues();
					cv.put("channelId", contentObject.getChannelId());
					cv.put("contentId", contentObject.getContentId());
					cv.put("title", contentObject.getTitle());
					cv.put("body",
							(contentObject.getBody() == null) ? ""
									: (contentObject.getBody().replaceAll(
											Constants.IMGURLPATH,
											"file://"
													+ Environment
															.getExternalStorageDirectory()
													+ "/nci/")));
					cv.put("type", contentObject.getType());
					cv.put("createTime", contentObject.getCreateTime());
					cv.put("lastSynTime", contentObject.getLastSynTime());
					cv.put("body2", contentObject.getBody2());
					if (contentObject.getOpeFlag().equals("c")) {
						db.delete("content", "contentId = ?",
								new String[] { contentObject.getContentId()
										+ "" });
						db.insert("content", null, cv);
					} else if (contentObject.getOpeFlag().equals("u")) {
						db.update("content", cv, "contentId = ? ",
								new String[] { contentObject.getContentId()
										+ "" });
					} else if (contentObject.getOpeFlag().equals("d")) {
						db.delete("content", "contentId = ?",
								new String[] { contentObject.getContentId()
										+ "" });
					}
				}
			}

			

			TipUitls.Log(TAG, "image------>" + image);
			if (image != null) {
				for (Image imageObject : image) {
					ContentValues cv = new ContentValues();
					cv.put("id", imageObject.getId());
					cv.put("imageName", imageObject.getImageName());
					cv.put("remark1", imageObject.getRemark1());
					cv.put("remark2", imageObject.getRemark2());
					cv.put("remark3", imageObject.getRemark3());
					cv.put("contentId", imageObject.getContentId());
					if (imageObject.getOpeFlag().equals("c")) {
						db.delete("image", "imageName = ?",
								new String[] { imageObject.getImageName() });
						db.insert("image", null, cv);
					} else if (imageObject.getOpeFlag().equals("u")) {
						db.update("image", cv, "imageName = ?",
								new String[] { imageObject.getImageName() });
					} else if (imageObject.getOpeFlag().equals("d")) {
						db.delete("image", "imageName = ?",
								new String[] { imageObject.getImageName() });
					}
				}
			}

			TipUitls.Log(TAG,
					"Framework------>" + JSON.toJSONString(moduleList));
			if (moduleList != null) {
				for (Framework framework : moduleList) {
					ContentValues cv = new ContentValues();
					cv.put("iconName", framework.getIconName());
					cv.put("thumbnailName", framework.getThumbnailName());
					cv.put("isVisible", framework.getIsVisible());
					cv.put("clickUrl", framework.getClickUrl());
					cv.put("moduleOrderby", framework.getModuleOrderby());
					cv.put("isVisibleOrder", framework.getIsVisibleOrder());
					cv.put("moduleId", framework.getModuleId());
					cv.put("fixedPage", framework.getFixedPage());
					cv.put("isLogin", framework.getIsLogin());
					cv.put("isMenuItem", framework.getIsMenuItem());
					cv.put("isAddMenuItem", framework.getIsAddMenuItem());
					cv.put("packageName", framework.getPackageName());
					cv.put("moduleName", framework.getModuleName());
					cv.put("moduleType", framework.getModuleType());
					cv.put("parentId", framework.getParentId());
					cv.put("updateDate", framework.getUpdateDate());
					cv.put("group_code", framework.getGroup_code());
	
					if ("c".equals(framework.getOpeFlag())) {
						db.delete("framework", "moduleId = ?",
								new String[] { framework.getModuleId() });
						db.insert("framework", null, cv);
					} else if ("u".equals(framework.getOpeFlag())) {
						db.update("framework", cv, "moduleId = ?",
								new String[] { framework.getModuleId() });
					} else if ("d".equals(framework.getOpeFlag())) {
						db.delete("framework", "moduleId = ?",
								new String[] { framework.getModuleId() });
					}
				}
			}

			TipUitls.Log(TAG, "network------>" + network);
			if (network != null) {
				for (Network networkObject : network) {
					ContentValues cv = new ContentValues();
					cv.put("id", networkObject.getId());
					cv.put("province", networkObject.getProvince());
					cv.put("city", networkObject.getCity());
					cv.put("address", networkObject.getAddress());
					cv.put("postcode", networkObject.getPostcode());
					cv.put("tel", networkObject.getTel());
					cv.put("latitude", networkObject.getLatitude());
					cv.put("longitude", networkObject.getLongitude());
					cv.put("cityCode", networkObject.getCityCode());
					cv.put("provinceCode", networkObject.getProvinceCode());
					cv.put("workday", networkObject.getWorkday());
					cv.put("saturday", networkObject.getSaturday());
					cv.put("sunday", networkObject.getSunday());
					if (networkObject.getOpeFlag().equals("c")) {
						db.delete("network", "id = ?",
								new String[] { networkObject.getId() + "" });
						db.insert("network", null, cv);
					} else if (networkObject.getOpeFlag().equals("u")) {
						db.update("network", cv, "id = ?",
								new String[] { networkObject.getId() + "" });
					} else if (networkObject.getOpeFlag().equals("d")) {
						db.delete("network", "id = ?",
								new String[] { networkObject.getId() + "" });
					}
				}
			}

			TipUitls.Log(TAG, "region------>" + region);
			if (region != null) {
				for (Region regionObject : region) {
					ContentValues cv = new ContentValues();
					cv.put("PLACETYPE", regionObject.getRegionLevel());
					cv.put("PLACECODE", regionObject.getCode());
					cv.put("PLACENAME", regionObject.getName());
					cv.put("UPPLACECODE", regionObject.getParentCode());
					cv.put("REM", regionObject.getShortName());
					if (regionObject.getOpeFlag().equals("c")) {
						db.insert("PLACECODE", null, cv);
					} else if (regionObject.getOpeFlag().equals("u")) {
						db.update("PLACECODE", cv, "PLACECODE = ?",
								new String[] { regionObject.getCode() + "" });
					} else if (regionObject.getOpeFlag().equals("d")) {
						db.delete("PLACECODE", "PLACECODE = ?",
								new String[] { regionObject.getCode() + "" });
					}
				}
			}
			db.setTransactionSuccessful();// 调用此方法会在执行到endTransaction()
			// 时提交当前事务，如果不调用此方法会回滚事务
			TipUitls.Log(TAG,
					"JsonToDb更----新----完-----成---------------------->");
			syncDone(context);
			new AsyncImageLoader(context); // 下载图片
		} catch (Exception e) {
			e.printStackTrace();
			db.endTransaction();
		} finally {
			db.endTransaction();// 由事务的标志决定是提交事务，还是回滚事务
		}
	}

	protected void syncDone(final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					// 保证注册完后再发送广播
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent();
				intent.setAction("syncDone");
				context.sendBroadcast(intent);
				TipUitls.Log(TAG, "更新完毕后发送了一个广播");
			}
		}).start();

	}

}
