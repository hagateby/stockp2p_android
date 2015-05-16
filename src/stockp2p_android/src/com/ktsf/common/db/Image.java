package com.ktsf.common.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 图片
 * 
 * @author haix
 * 
 */
public class Image {

	private int id;
	private String imageName;// 图片名称
	private String remark1;// 点击跳转路径
	private String remark2;// 图片下载路径
	private String remark3;// 广告栏扩展字段
	private int contentId;// 所属文章Id
	private String opeFlag;// 操作类型c新建,u更新,d删除

	public String getOpeFlag() {
		return opeFlag;
	}

	public void setOpeFlag(String opeFlag) {
		this.opeFlag = opeFlag;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public static ArrayList<Image> findByContentId(SQLiteDatabase db, int value) {
		Cursor cursor = db.query("image", new String[] { "contentId",
				"imageName", "remark1", "remark2", "remark3" }, "contentId="
				+ value, null, null, null, "remark3 desc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<Image> imageList = new ArrayList<Image>();
		for (int i = 0; i < counts; i++) {
			Image image = new Image();
			image.setContentId(cursor.getInt(cursor.getColumnIndex("contentId")));
			image.setImageName(cursor.getString(cursor
					.getColumnIndex("imageName")));
			image.setRemark1(cursor.getString(cursor.getColumnIndex("remark1")));
			image.setRemark2(cursor.getString(cursor.getColumnIndex("remark2")));
			image.setRemark3(cursor.getString(cursor.getColumnIndex("remark3")));
			imageList.add(image);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return imageList;
	}

	public static ArrayList<Image> findAllImage(SQLiteDatabase db) {
		Cursor cursor = db.rawQuery(
				"select  * from image where contentId !='3'", null);
		// Cursor cursor = db.query("image", new String[] { "contentId",
		// "imageName", "remark1", "remark2", "remark3" },
		// "contentId != '3'", null, null, null, null);
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<Image> imageList = new ArrayList<Image>();
		for (int i = 0; i < counts; i++) {
			Image image = new Image();
			image.setContentId(cursor.getInt(cursor.getColumnIndex("contentId")));
			image.setImageName(cursor.getString(cursor
					.getColumnIndex("imageName")));
			image.setRemark1(cursor.getString(cursor.getColumnIndex("remark1")));
			image.setRemark2(cursor.getString(cursor.getColumnIndex("remark2")));
			image.setRemark3(cursor.getString(cursor.getColumnIndex("remark3")));
			imageList.add(image);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return imageList;
	}
}