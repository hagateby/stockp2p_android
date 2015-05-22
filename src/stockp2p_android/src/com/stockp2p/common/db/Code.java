package com.stockp2p.common.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Code {

	private String codeType;
	private String code;
	private String codeName;

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public static ArrayList<Code> findAllId(SQLiteDatabase db) {
		Cursor cursor = db.query("code", new String[] { "codetype", "code",
				"codename", "id" }, "codetype = \"credentialType\"", null,
				null, null, "code asc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<Code> idList = new ArrayList<Code>();
		for (int i = 0; i < counts; i++) {
			Code id = new Code();
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

	public static ArrayList<Code> findAllBank(SQLiteDatabase db,
			ArrayList<Code> bankList) {
		Cursor cursor = db.query("code", new String[] { "code", "codename" },
				"codetype = \"bankType\"", null, null, null, "code asc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			// return null;
		}
		for (int i = 0; i < counts; i++) {
			Code bank = new Code();
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

	public static Code findPolicyUrl(SQLiteDatabase db) {
		Cursor cursor = db.query("code", new String[] { "codetype", "code",
				"codename" }, "codetype = \"casettePolicy\"", null, null, null,
				"code asc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		Code policyUrl = new Code();
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

	public static Code findProductZone(SQLiteDatabase db) {
		Cursor cursor = db.query("code", new String[] { "codetype", "code",
				"codename" }, "codetype = \"productUrl\"", null, null, null,
				"code asc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		Code policyUrl = new Code();
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