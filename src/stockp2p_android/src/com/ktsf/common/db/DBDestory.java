package com.ktsf.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBDestory {

	public static void destory(SQLiteDatabase db, Cursor cursor) {

		destory(cursor);

		destory(db);
	}

	public static void destory(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}

	public static void destory(SQLiteDatabase db) {
		if (db != null) {
			db.close();
			db = null;
		}
	}
}
