package com.stockp2p.common.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stockp2p.R;
import com.stockp2p.util.SYSTEMCONST;



public class DBOpenHelper extends SQLiteOpenHelper {

	/* DB NAME */
	private static final String DB_NAME = SYSTEMCONST.dbname;
	/* initial DB VERSION */
	private static final int DB_VERSION = 1;
	
	private static File dbFile = null ;
	
	private  Context mContext;
	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.mContext = context;
	}

	/**
	 * To check database is exist, if not, copy raw/xinhua.db to data/data/package/files folder.
	 */
	public void initDataBase() {
		try {
			if (mContext != null) {
				if( dbFile == null  ){
					dbFile = mContext.getDatabasePath(DB_NAME);
					dbFile.getParentFile().mkdirs();
				}
				if (!dbFile.exists()) {
					InputStream is = mContext.getResources().openRawResource(R.raw.xinhua_phone);
					FileOutputStream fos = new FileOutputStream(dbFile.getAbsolutePath());
					byte[] buffer = new byte[8 * 1024];
					int length = 0;
					while ((length = is.read(buffer)) > 0) {
						fos.write(buffer, 0, length);
					}
					fos.flush();
					is.close();
					fos.close();
					
				}
			}
		} catch (IOException e) {
			Log.e("DataBase", e.getMessage());
		}
	}
	
	public synchronized SQLiteDatabase getReadableDatabase() {
		initDataBase();
		return super.getReadableDatabase();
	}
	
	public synchronized SQLiteDatabase getWritableDatabase() {
		initDataBase();
		return super.getWritableDatabase();
	}
	
	public void onCreate(SQLiteDatabase db) {
		
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public static String getString(Cursor cursor, String columnName){
		return cursor.getString(cursor.getColumnIndex(columnName));
	}
	
	public static int getInt(Cursor cursor, String columnName){
		return cursor.getInt(cursor.getColumnIndex(columnName));
	}
	
	public static long getLong(Cursor cursor, String columnName){
		return cursor.getLong(cursor.getColumnIndex(columnName));
	}
	
	public static double getDouble(Cursor cursor, String columnName){
		return cursor.getDouble(cursor.getColumnIndex(columnName));
	}
	
	public static boolean getBoolean(Cursor cursor, String columnName){
		return Boolean.valueOf(getString(cursor, columnName));
	}


}
