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
public class FrameWork_LocalFileDesc {

	private int id;
	private String FileCode;// 所属文章Id
	private String GroupCode;// 分组号
	private String FileType;// 文件类型   0 在R文件中的文  1 image文件  2 其它文件
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

	public String getFileCode() {
		return FileCode;
	}

	public void setFileCode(String FileCode) {
		this.FileCode = FileCode;
	}


	
	
}