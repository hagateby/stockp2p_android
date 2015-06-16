package com.stockp2p.common.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.stockp2p.R;

public class FrameWork_Frame_DAO {
	
   static String[] fields =new String[] {"moduleId",
			"parentId", "moduleName", "isMenuItem", "isAddMenuItem",
			"moduleType", "packageName", "iconName", "imagfilecode", "thumbnailName",
			"clickUrl", "moduleOrderby", "isVisibleOrder", "fixedPage",
			"isVisible", "isLogin" , "group_code"} ;

   public static List<FrameWork_Frame> findByIsAddMenuItem(SQLiteDatabase db,
			String isAddMenuItem, FragmentActivity context) {
		List<FrameWork_Frame> framList = new ArrayList<FrameWork_Frame>();
		Cursor cursor = db.query("FrameWork_Frame", fields, "isVisible =0 and isAddMenuItem ="
				+ isAddMenuItem, null, null, null,
				"moduleType,moduleOrderby desc");
		// isMenuItem=1
		return setFrameworkdata(cursor,context);
	}

	public static List<FrameWork_Frame> findByParentId(SQLiteDatabase db,
			String parentId, FragmentActivity context) {
		List<FrameWork_Frame> framList = new ArrayList<FrameWork_Frame>();
		Cursor cursor = db.query("FrameWork_Frame", fields, "isVisible =0 and parentId" + "="
				+ parentId, null, null, null, "moduleOrderby");
		
		return setFrameworkdata(cursor,context);
	}

	public static List<FrameWork_Frame> findByFixedPage(SQLiteDatabase db,
			String fixedPage, FragmentActivity context) {
		List<FrameWork_Frame> framList = new ArrayList<FrameWork_Frame>();
		Cursor cursor = db.query("FrameWork_Frame", fields, "isVisible =0 and fixedPage" + "="
				+ fixedPage + " and moduleId!=99", null, null, null,
				"isVisibleOrder");
	
		 return  setFrameworkdata(cursor,context);
	}

	public static List<FrameWork_Frame> findByModuleId(SQLiteDatabase db,
			String moduleId, FragmentActivity context) {
		
		Cursor cursor = db.query("FrameWork_Frame", fields, "isVisible = 0 and  moduleId" + "="
				+ moduleId, null, null, null, "moduleOrderby");
	
		return  setFrameworkdata(cursor,context);
	}

	public static List<FrameWork_Frame> findByGroupCode(SQLiteDatabase db,
			String group_code, FragmentActivity context) {
		
		Cursor cursor = db.query("FrameWork_Frame", fields, "isVisible = 0 and  group_code" + "="
				+ group_code, null, null, null, "moduleOrderby");
	
		return  setFrameworkdata(cursor,context);
	}
	

	private static List<FrameWork_Frame> setFrameworkdata( Cursor cursor ,FragmentActivity context ){
		List<FrameWork_Frame> framList = new ArrayList<FrameWork_Frame>();
		
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			Toast.makeText(context,
					context.getResources().getString(R.string.datebase_error),
					0).show();
		}
		for (int i = 0; i < counts; i++) {
			FrameWork_Frame framework = new FrameWork_Frame();
			framework.setModuleId(cursor.getString(cursor
					.getColumnIndex("moduleId")));
			framework.setParentId(cursor.getString(cursor
					.getColumnIndex("parentId")));
			framework.setModuleName(cursor.getString(cursor
					.getColumnIndex("moduleName")));
			framework.setIsMenuItem(cursor.getString(cursor
					.getColumnIndex("isMenuItem")));
			framework.setIsAddMenuItem(cursor.getString(cursor
					.getColumnIndex("isAddMenuItem")));
			framework.setModuleType(cursor.getString(cursor
					.getColumnIndex("moduleType")));
			framework.setPackageName(cursor.getString(cursor
					.getColumnIndex("packageName")));
			framework.setIconName(cursor.getString(cursor
					.getColumnIndex("iconName")));
			framework.setIconName(cursor.getString(cursor
					.getColumnIndex("imagFileCode")));
			framework.setThumbnailName(cursor.getString(cursor
					.getColumnIndex("thumbnailName")));
			framework.setClickUrl(cursor.getString(cursor
					.getColumnIndex("clickUrl")));
			framework.setModuleOrderby(cursor.getString(cursor
					.getColumnIndex("moduleOrderby")));
			framework.setIsVisibleOrder(cursor.getString(cursor
					.getColumnIndex("isVisibleOrder")));
			framework.setFixedPage(cursor.getString(cursor
					.getColumnIndex("fixedPage")));
			framework.setIsVisible(cursor.getString(cursor
					.getColumnIndex("isVisible")));
			framework.setIsLogin(cursor.getString(cursor
					.getColumnIndex("isLogin")));
			framework.setIsLogin(cursor.getString(cursor
					.getColumnIndex("group_code")));

			
			framList.add(framework);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		   return framList;
 }
	

}
