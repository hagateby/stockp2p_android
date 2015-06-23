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
	
   static String[] fields =new String[] {"frameId",
			"parentId", "frameName", "isMenuItem", "isAddMenuItem",
			"frameType", "packageName", "iconName", "imagFileCode", "thumbnailName",
			"clickUrl", "frameOrderby", "isVisibleOrder", "fixedPage",
			"isVisible", "isLogin" , "groupCode","remark","layoutName","showType"} ;

   public static List<FrameWork_Frame> findByIsAddMenuItem(SQLiteDatabase db,
			String isAddMenuItem, FragmentActivity context) {
		List<FrameWork_Frame> framList = new ArrayList<FrameWork_Frame>();
		Cursor cursor = db.query("FrameWork_Frame", fields, "isVisible =0 and isAddMenuItem ="
				+ isAddMenuItem, null, null, null,
				"frameType,frameOrderby desc");
		// isMenuItem=1
		return setFrameworkdata(cursor,context);
	}

	public static List<FrameWork_Frame> findByParentId(SQLiteDatabase db,
			String parentId, FragmentActivity context) {
		List<FrameWork_Frame> framList = new ArrayList<FrameWork_Frame>();
		Cursor cursor = db.query("FrameWork_Frame", fields, "isVisible =0 and parentId" + "="
				+ parentId, null, null, null, "frameOrderby");
		
		return setFrameworkdata(cursor,context);
	}

	public static List<FrameWork_Frame> findByFixedPage(SQLiteDatabase db,
			String fixedPage, FragmentActivity context) {
		List<FrameWork_Frame> framList = new ArrayList<FrameWork_Frame>();
		Cursor cursor = db.query("FrameWork_Frame", fields, "isVisible =0 and fixedPage" + "="
				+ fixedPage + " and frameId!=99", null, null, null,
				"isVisibleOrder");
	
		 return  setFrameworkdata(cursor,context);
	}

	public static List<FrameWork_Frame> findByModuleId(SQLiteDatabase db,
			String moduleId, FragmentActivity context) {
		
		Cursor cursor = db.query("FrameWork_Frame", fields, "isVisible = 0 and  moduleId" + "="
				+ moduleId, null, null, null, "frameOrderby");
	
		return  setFrameworkdata(cursor,context);
	}

	public static List<FrameWork_Frame> findByGroupCode(SQLiteDatabase db,
			String group_code, FragmentActivity context) {
		
		Cursor cursor = db.query("FrameWork_Frame", fields, "isVisible = 0 and  groupCode" + "="
				+ group_code, null, null, null, "frameOrderby");
	
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
			framework.setFrameId(cursor.getString(cursor
					.getColumnIndex("frameId")));
			framework.setParentId(cursor.getString(cursor
					.getColumnIndex("parentId")));
			framework.setFrameName(cursor.getString(cursor
					.getColumnIndex("frameName")));
			framework.setIsMenuItem(cursor.getString(cursor
					.getColumnIndex("isMenuItem")));
			framework.setIsAddMenuItem(cursor.getString(cursor
					.getColumnIndex("isAddMenuItem")));
			framework.setFrameType(cursor.getString(cursor
					.getColumnIndex("frameType")));
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
					.getColumnIndex("frameOrderby")));
			framework.setIsVisibleOrder(cursor.getString(cursor
					.getColumnIndex("isVisibleOrder")));
			framework.setFixedPage(cursor.getString(cursor
					.getColumnIndex("fixedPage")));
			framework.setIsVisible(cursor.getString(cursor
					.getColumnIndex("isVisible")));
			framework.setIsLogin(cursor.getString(cursor
					.getColumnIndex("isLogin")));
			framework.setGroupCode(cursor.getString(cursor
					.getColumnIndex("groupCode")));

			framework.setImagFileCode(cursor.getString(cursor
					.getColumnIndex("imagFileCode")));			
			
			framework.setRemark(cursor.getString(cursor
					.getColumnIndex("remark")));		
			framework.setLayoutName(cursor.getString(cursor
					.getColumnIndex("layoutName")));	
			framework.setShowType(cursor.getString(cursor
					.getColumnIndex("showType")));
			framework.setGroupCode(cursor.getString(cursor
					.getColumnIndex("groupCode")));
					
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
