package com.stockp2p.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Content {
	
	private int channelId;//栏目ID
	private String channelName;//栏目名称
	private int contentId;//文章ID
	private String title;//文章标题
	private String opeFlag;//操作类型c新建,u更新,d删除
	private String body;//内容文本"(CXYWY与业务员号之间使用空格)"
	private String type;//文本类型 黄金和白金卡有 type 字段
	private String createTime;//创建时间
	private String lastSynTime;//更新时间
	private String body2;//内容文本2"可获得该保单服务人员的姓名、联系方式信息"
	
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getOpeFlag() {
		return opeFlag;
	}
	public void setOpeFlag(String opeFlag) {
		this.opeFlag = opeFlag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastSynTime() {
		return lastSynTime;
	}
	public void setLastSynTime(String lastSynTime) {
		this.lastSynTime = lastSynTime;
	}
	public String getBody2() {
		return body2;
	}
	public void setBody2(String body2) {
		this.body2 = body2;
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	
	public static Content findByContentId(SQLiteDatabase db, int value){
		Cursor cursor = db.query("content", new String[] {"channelId", "title", "body" ,"type", "body2", "remark1", "remark2"}, "contentId" + "=" + value, null, null, null, null);
		int counts = cursor.getCount();
		if(counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		Content content = new Content();
		for (int i = 0; i < counts ; i++) {
			content.setChannelId(cursor.getInt(cursor.getColumnIndex("channelId")));
			content.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			content.setBody(cursor.getString(cursor.getColumnIndex("body")));
			content.setType(cursor.getString(cursor.getColumnIndex("type")));
			content.setBody2(cursor.getString(cursor.getColumnIndex("body2")));
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return content;
	}
	
	public static Content findByChannelId(SQLiteDatabase db, int value){
		Cursor cursor = db.query("content", new String[] {"contentId", "channelId", "title", "body" ,"type", "body2", "remark1", "remark2"}, "channelId" + "=" + value, null, null, null, null);
		int counts = cursor.getCount();
		if(counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		Content content = new Content();
		for (int i = 0; i < counts ; i++) {
			content.setChannelId(cursor.getInt(cursor.getColumnIndex("channelId")));
			content.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			content.setBody(cursor.getString(cursor.getColumnIndex("body")));
			content.setType(cursor.getString(cursor.getColumnIndex("type")));
			content.setBody2(cursor.getString(cursor.getColumnIndex("body2")));
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return content;
	}
}