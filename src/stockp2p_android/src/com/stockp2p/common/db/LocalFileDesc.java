package com.stockp2p.common.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 图片
 * 
 * @author haix
 * 
 */
public class LocalFileDesc {

	private int id;
	private int ContentId;// 所属文章Id
	private String GroupCode;// 分组号
	private String FileType;// 文件类型
	private String FileName;// 文件名称
	private String LocalPath;// 文件在本地的路径名
	private String ClickAdress;// 点击跳转路径
	private String NetAdress;// 图片下载路径
	private String OpeFlag;// 操作类型c新建,u更新,d删除
	private String AsynFlag ; //是否需要同布  "Y" "N"
	private String Version ;  // 此操作的版本号
	private String Order ; //排序
	private String remark;// 广告栏扩展字段

	public String getGroupCode() {
		return GroupCode;
	}

	public void setGroupCode(String GroupCode) {
		this.GroupCode = GroupCode;
	}
	
	public String getLocalPath() {
		return LocalPath;
	}

	public void setLocalPath(String LocalPath) {
		this.LocalPath = LocalPath;
	}
	
	public String getOrder() {
		return Order;
	}

	public void setOrder(String Order) {
		this.Order = Order;
	}
	
	public String getVersion() {
		return Version;
	}

	public void setVersion(String Version) {
		this.Version = Version;
	}
	
	public String getFileType() {
		return FileType;
	}

	public void setFileType(String FileType) {
		this.FileType = FileType;
	}
	
	public String getAsynFlag() {
		return AsynFlag;
	}

	public void setAsynFlag(String AsynFlag) {
		this.AsynFlag = AsynFlag;
	}
	
	public String getOpeFlag() {
		return OpeFlag;
	}

	public void setOpeFlag(String opeFlag) {
		this.OpeFlag = opeFlag;
	}

	public String getClickAdress() {
		return ClickAdress;
	}

	public void setClickAdress(String ClickAdress) {
		this.ClickAdress = ClickAdress;
	}

	public String getNetAdress() {
		return NetAdress;
	}

	public void setNetAdress(String NetAdress) {
		this.NetAdress = NetAdress;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String FileName) {
		this.FileName = FileName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getContentId() {
		return ContentId;
	}

	public void setContentId(int contentId) {
		this.ContentId = contentId;
	}


	
	public static ArrayList<LocalFileDesc> findByContentId(SQLiteDatabase db, int value) {
		Cursor cursor = db.rawQuery(
				"select  * from localfiledesc where  Contentid= ?" , new String[] { value +""});
		
	     
		
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<LocalFileDesc> imageList = new ArrayList<LocalFileDesc>();
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

	public static ArrayList<LocalFileDesc> findAllAsynFileByVervsion(SQLiteDatabase db) {
		Cursor cursor = db.rawQuery(
				"select  * from localfiledesc where  AsynFlag!='Y' " , null);
         //lwh   Vervsion
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<LocalFileDesc> imageList = new ArrayList<LocalFileDesc>();
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
	
	public static ArrayList<LocalFileDesc> findAllImage(SQLiteDatabase db) {
		Cursor cursor = db.rawQuery(
				"select  * from localfiledesc where contentId !='3'" , null);

		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<LocalFileDesc> imageList = new ArrayList<LocalFileDesc>();
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
	
	public static LocalFileDesc setLocalFileDescby(Cursor cursor)
	{
			
		LocalFileDesc filename = new LocalFileDesc();
		filename.setContentId(cursor.getInt(cursor.getColumnIndex("ContentId")));
		filename.setGroupCode(cursor.getString(cursor.getColumnIndex("GroupCode")));
		filename.setFileName(cursor.getString(cursor.getColumnIndex("FileName")));
		filename.setLocalPath(cursor.getString(cursor.getColumnIndex("LocalPath")));
		filename.setFileType(cursor.getString(cursor.getColumnIndex("FileType")));		
		filename.setClickAdress(cursor.getString(cursor.getColumnIndex("ClickAdress")));
		filename.setNetAdress(cursor.getString(cursor.getColumnIndex("NetAdress")));
		filename.setOpeFlag(cursor.getString(cursor.getColumnIndex("OpeFlag")));
		filename.setAsynFlag(cursor.getString(cursor.getColumnIndex("AsynFlag")));
		filename.setVersion(cursor.getString(cursor.getColumnIndex("Version")));
		filename.setOrder(cursor.getString(cursor.getColumnIndex("Order")));
		filename.setRemark(cursor.getString(cursor.getColumnIndex("Remark")));
			
		return filename;
	
	}
	
}