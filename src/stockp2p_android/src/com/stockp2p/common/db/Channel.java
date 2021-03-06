package com.stockp2p.common.db;

import java.util.ArrayList;

import com.stockp2p.common.util.TipUitls;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Channel {

	private int channelId;// 栏目ID
	private String channelName;// 栏目名称
	private int parentId;// 上级栏目ID
	private String opeFlag;// 操作类型c新建,u更新,d删除

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getOpeFlag() {
		return opeFlag;
	}

	public void setOpeFlag(String opeFlag) {
		this.opeFlag = opeFlag;
	}

	public static ArrayList<Channel> findByParentId(SQLiteDatabase db,
			int parentId) {
		Cursor cursor = db.query("channel", new String[] { "channelId",
				"channelName", "parentId", "lastSynTime" }, "parentId" + "="
				+ parentId, null, null, null, "channelId asc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}

		ArrayList<Channel> channelList = new ArrayList<Channel>();
		for (int i = 0; i < counts; i++) {
			Channel channel = new Channel();
			channel.setChannelId(cursor.getInt(cursor
					.getColumnIndex("channelId")));
			channel.setChannelName(cursor.getString(cursor
					.getColumnIndex("channelName")));
			channel.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
			channelList.add(channel);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return channelList;
	}

	public static ArrayList<Channel> findByChannelId(SQLiteDatabase db,
			int parentId) {
		Cursor cursor = db.query("channel", new String[] { "channelId",
				"channelName", "parentId" }, "channelId" + "=" + parentId,
				null, null, null, null);
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<Channel> channelList = new ArrayList<Channel>();
		for (int i = 0; i < counts; i++) {
			Channel channel = new Channel();
			channel.setChannelId(cursor.getInt(cursor
					.getColumnIndex("channelId")));
			channel.setChannelName(cursor.getString(cursor
					.getColumnIndex("channelName")));
			channel.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
			channelList.add(channel);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return channelList;
	}
}