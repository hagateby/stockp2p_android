package com.stockp2p.common.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FrameWork_Code_DAO {

	
	public static ArrayList<FrameWork_Code> findAllId(SQLiteDatabase db) {
		Cursor cursor = db.query("FrameWork_Code", new String[] { "codetype", "code",
				"codename", "id" }, "codetype = \"credentialType\"", null,
				null, null, "code asc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<FrameWork_Code> idList = new ArrayList<FrameWork_Code>();
		for (int i = 0; i < counts; i++) {
			FrameWork_Code id = new FrameWork_Code();
			id.setCode(cursor.getString(cursor.getColumnIndex("code")));
			id.setCodeName(cursor.getString(cursor.getColumnIndex("codename")));
			id.setCodeType("0");
			idList.add(id);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return idList;
	}

	public static ArrayList<String> findAllId(SQLiteDatabase db,String codetype) {
		Cursor cursor = db.query("FrameWork_Code", new String[] { "codetype", "code",
				"codename", "id" }, "codetype = " + codetype, null,
				null, null, "id asc");
		
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<String> idList = new ArrayList<String>();
		for (int i = 0; i < counts; i++) {
			
			idList.add(cursor.getString(cursor.getColumnIndex("code")));
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return idList;
	}
	
	public static ArrayList<FrameWork_Code> findAllBank(SQLiteDatabase db,
			ArrayList<FrameWork_Code> bankList) {
		Cursor cursor = db.query("FrameWork_Code", new String[] { "code", "codename" },
				"codetype = \"bankType\"", null, null, null, "code asc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			// return null;
		}
		for (int i = 0; i < counts; i++) {
			FrameWork_Code bank = new FrameWork_Code();
			bank.setCode(cursor.getString(cursor.getColumnIndex("code")));
			bank.setCodeName(cursor.getString(cursor.getColumnIndex("codename")));
			bankList.add(bank);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return bankList;
	}

	public static FrameWork_Code findPolicyUrl(SQLiteDatabase db) {
		Cursor cursor = db.query("FrameWork_Code", new String[] { "codetype", "code",
				"codename" }, "codetype = \"casettePolicy\"", null, null, null,
				"code asc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		FrameWork_Code policyUrl = new FrameWork_Code();
		policyUrl.setCode(cursor.getString(cursor.getColumnIndex("code")));
		policyUrl.setCodeName(cursor.getString(cursor
				.getColumnIndex("codename")));
		policyUrl.setCodeType("casettePolicy");
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return policyUrl;
	}

	public static FrameWork_Code findProductZone(SQLiteDatabase db) {
		Cursor cursor = db.query("FrameWork_Code", new String[] { "codetype", "code",
				"codename" }, "codetype = \"productUrl\"", null, null, null,
				"code asc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		FrameWork_Code policyUrl = new FrameWork_Code();
		policyUrl.setCode(cursor.getString(cursor.getColumnIndex("code")));
		policyUrl.setCodeName(cursor.getString(cursor
				.getColumnIndex("codename")));
		policyUrl.setCodeType("casettePolicy");
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return policyUrl;
	}
}
