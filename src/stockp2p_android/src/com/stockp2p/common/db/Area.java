package com.stockp2p.common.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Area {

	private String code;
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static ArrayList<Area> queryArea(SQLiteDatabase db) {
		Cursor cursor = db.query("code", new String[] { "codetype", "code",
				"codename", "id" }, "codetype = \"region\"", null, null, null,
				"code asc");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<Area> areaList = new ArrayList<Area>();
		// Area area1 = new Area();
		// area1.setName("请选择");
		// areaList.add(area1);
		for (int i = 0; i < counts; i++) {
			Area area = new Area();
			area.setCode(cursor.getString(cursor.getColumnIndex("code")));
			area.setName(cursor.getString(cursor.getColumnIndex("codename")));
			areaList.add(area);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return areaList;
	}
}
