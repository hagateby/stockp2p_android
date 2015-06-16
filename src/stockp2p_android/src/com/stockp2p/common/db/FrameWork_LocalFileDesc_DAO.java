package com.stockp2p.common.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FrameWork_LocalFileDesc_DAO {

	static String[] fields = new String[] { "id", "FileCode", "GroupCode",
			"FileType", "FileName", "LocalPath", "ClickAdress", "NetAdress",
			"OpeFlag", "AsynFlag", "Version", "Order", "remark" };

	public static ArrayList<FrameWork_LocalFileDesc> findByFileCode(
			SQLiteDatabase db, int value) {
		Cursor cursor = db.rawQuery(
				"select  * from FrameWork_LocalFileDesc where  FileCode= ?",
				new String[] { value + "" });

		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<FrameWork_LocalFileDesc> imageList = new ArrayList<FrameWork_LocalFileDesc>();
		for (int i = 0; i < counts; i++) {
			imageList.add(setLocalFileDescby(cursor));

			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return imageList;
	}

	public static ArrayList<FrameWork_LocalFileDesc> findAllAsynFileByVervsion(
			SQLiteDatabase db) {
		Cursor cursor = db.rawQuery(
				"select  * from FrameWork_LocalFileDesc where  AsynFlag!='Y' ",
				null);
		// lwh Vervsion
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<FrameWork_LocalFileDesc> imageList = new ArrayList<FrameWork_LocalFileDesc>();
		for (int i = 0; i < counts; i++) {
			imageList.add(setLocalFileDescby(cursor));
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return imageList;
	}

	public static ArrayList<FrameWork_LocalFileDesc> findAllFileCode(
			SQLiteDatabase db) {
		Cursor cursor = db.rawQuery(
				"select  * from FrameWork_LocalFileDesc ",
				null);

		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<FrameWork_LocalFileDesc> imageList = new ArrayList<FrameWork_LocalFileDesc>();
		for (int i = 0; i < counts; i++) {
			imageList.add(setLocalFileDescby(cursor));
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return imageList;
	}

	public static FrameWork_LocalFileDesc setLocalFileDescby(Cursor cursor) {

		FrameWork_LocalFileDesc filename = new FrameWork_LocalFileDesc();
		filename.setFileCode(cursor.getString(cursor.getColumnIndex("FileCode")));
		filename.setGroupCode(cursor.getString(cursor
				.getColumnIndex("GroupCode")));
		filename.setFileName(cursor.getString(cursor.getColumnIndex("FileName")));
		filename.setLocalPath(cursor.getString(cursor
				.getColumnIndex("LocalPath")));
		filename.setFileType(cursor.getString(cursor.getColumnIndex("FileType")));
		filename.setClickAdress(cursor.getString(cursor
				.getColumnIndex("ClickAdress")));
		filename.setNetAdress(cursor.getString(cursor
				.getColumnIndex("NetAdress")));
		filename.setOpeFlag(cursor.getString(cursor.getColumnIndex("OpeFlag")));
		filename.setAsynFlag(cursor.getString(cursor.getColumnIndex("AsynFlag")));
		filename.setVersion(cursor.getString(cursor.getColumnIndex("Version")));
		filename.setOrder(cursor.getString(cursor.getColumnIndex("Order")));
		filename.setRemark(cursor.getString(cursor.getColumnIndex("Remark")));

		return filename;

	}
}
