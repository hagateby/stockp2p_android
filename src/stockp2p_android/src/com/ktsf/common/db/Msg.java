package com.ktsf.common.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Msg {
	private String userid;
	private String msg;
	private String date;
	private String inorout;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getInorout() {
		return inorout;
	}
	public void setInorout(String inorout) {
		this.inorout = inorout;
	}
	public Msg(){
		
	}
	public Msg(String userid, String msg, String date, String inorout) {
		this.userid = userid;
		this.msg = msg;
		this.date = date;
		this.inorout = inorout;
	}
	public static ArrayList<Msg> queryMsg(SQLiteDatabase db){
		Cursor cursor = db.query("form", new String[] {"userid", "msg", "date", "inorout"}, null, null, null, null, "date asc");
		int counts = cursor.getCount();
		if(counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<Msg> msgList = new ArrayList<Msg>();
		for (int i = 0; i < counts ; i++) {
			Msg msg = new Msg();
			msg.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
			msg.setMsg(cursor.getString(cursor.getColumnIndex("msg")));
			msg.setDate(cursor.getString(cursor.getColumnIndex("date")));
			msg.setInorout(cursor.getString(cursor.getColumnIndex("inorout")));
			msgList.add(msg);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return msgList;
	}
}
