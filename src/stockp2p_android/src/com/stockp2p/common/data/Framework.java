package com.stockp2p.common.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;

public class Framework implements Parcelable{

	// 主键 模块的 id号
		
		public String moduleId;
		// 父id 上一级的主键
		public String parentId;
		// 模块名称
		public String moduleName;
		// 添加列表点击后是不是显示下一级 0-显示 1-不显示
		public String isMenuItem;
		// 是否为添加列表显示的模块 0-否 1-是
		public String isAddMenuItem;
		// 模块类型 1为列表,有下一级菜单 2为详情 3为 webview 4为打开另一个程序
		public String moduleType;
		// 第三方应用包名
		public String packageName;
		// 图标名称
		public String iconName;
		// 小图标名称
		public String thumbnailName;
		// webview点击链接
		public String clickUrl;
		// 是否显示
		public String isVisible;
		// 模块后台规定的顺序
		public String moduleOrderby;
		// 首页主菜单排序字段 由后台维护
		public String isVisibleOrder;
		// 是否为首页主菜单固定菜单-后台维护
		public String fixedPage;
		// 是否需要登录
		public String isLogin;
		// 更新时间
		public String updateDate;
		// /模块状态 u:update ,c:create,d:delete
		public String opeFlag;
		// 分组编码
	    public String group_code;
		//分组类型
	    public String group_type;
	    
		public String getOpeFlag() {
			return opeFlag;
		}
		public void setOpeFlag(String opeFlag) {
			this.opeFlag = opeFlag;
		}
		public String getModuleId() {
			return moduleId;
		}
		public void setModuleId(String moduleId) {
			this.moduleId = moduleId;
		}
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		public String getModuleName() {
			return moduleName;
		}
		public void setModuleName(String moduleName) {
			this.moduleName = moduleName;
		}
		public String getIsMenuItem() {
			return isMenuItem;
		}
		public void setIsMenuItem(String isMenuItem) {
			this.isMenuItem = isMenuItem;
		}
		public String getIsAddMenuItem() {
			return isAddMenuItem;
		}
		public void setIsAddMenuItem(String isAddMenuItem) {
			this.isAddMenuItem = isAddMenuItem;
		}
		public String getModuleType() {
			return moduleType;
		}
		public void setModuleType(String moduleType) {
			this.moduleType = moduleType;
		}
		public String getPackageName() {
			return packageName;
		}
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		public String getIconName() {
			return iconName;
		}
		public void setIconName(String iconName) {
			this.iconName = iconName;
		}
		public String getThumbnailName() {
			return thumbnailName;
		}
		public void setThumbnailName(String thumbnailName) {
			this.thumbnailName = thumbnailName;
		}
		public String getClickUrl() {
			return clickUrl;
		}
		public void setClickUrl(String clickUrl) {
			this.clickUrl = clickUrl;
		}
		public String getIsVisible() {
			return isVisible;
		}
		public void setIsVisible(String isVisible) {
			this.isVisible = isVisible;
		}
		public String getModuleOrderby() {
			return moduleOrderby;
		}
		public void setModuleOrderby(String moduleOrderby) {
			this.moduleOrderby = moduleOrderby;
		}
		public String getIsVisibleOrder() {
			return isVisibleOrder;
		}
		public void setIsVisibleOrder(String isVisibleOrder) {
			this.isVisibleOrder = isVisibleOrder;
		}
		public String getFixedPage() {
			return fixedPage;
		}
		public void setFixedPage(String fixedPage) {
			this.fixedPage = fixedPage;
		}
		public String getIsLogin() {
			return isLogin;
		}
		public void setIsLogin(String isLogin) {
			this.isLogin = isLogin;
		}
		public String getUpdateDate() {
			return updateDate;
		}
		public void setUpdateDate(String updateDate) {
			this.updateDate = updateDate;
		}
		
	
		public String getGroup_code() {
			return group_code;
		}
		public void setGroup_code(String group_code) {
			this.group_code = group_code;
		}
		
		public String getGroup_type() {
			return group_type;
		}
		public void setGroup_type(String group_type) {
			this.group_type = group_type;
		}
		// 序列化实体
		public static final Parcelable.Creator<Framework> CREATOR = new Creator<Framework>() {
	
			@Override
			public Framework createFromParcel(Parcel source) {
				// 读和写的顺序要一样
				Framework framework = new Framework();
				framework.moduleId = source.readString();
				framework.parentId = source.readString();
				framework.moduleName = source.readString();
				framework.isMenuItem = source.readString();
				framework.isAddMenuItem = source.readString();
				framework.moduleType = source.readString();
				framework.packageName = source.readString();
				framework.iconName = source.readString();
				framework.thumbnailName = source.readString();
				framework.clickUrl = source.readString();
				framework.isVisible = source.readString();
				framework.isVisibleOrder = source.readString();
				framework.fixedPage = source.readString();
				framework.isLogin = source.readString();
				framework.moduleOrderby = source.readString();
				framework.updateDate = source.readString();
				framework.group_code = source.readString();
				framework.group_type = source.readString();			
				return framework;
			}
	
			@Override
			public Framework[] newArray(int size) {
				return new Framework[size];
			}
		};
	
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
	
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(moduleId);
			dest.writeString(parentId);
			dest.writeString(moduleName);
			dest.writeString(isMenuItem);
			dest.writeString(isAddMenuItem);
			dest.writeString(moduleType);
			dest.writeString(packageName);
			dest.writeString(iconName);
			dest.writeString(thumbnailName);
			dest.writeString(clickUrl);
			dest.writeString(isVisible);
			dest.writeString(isVisibleOrder);
			dest.writeString(fixedPage);
			dest.writeString(isLogin);
			dest.writeString(moduleOrderby);
			dest.writeString(updateDate);
			dest.writeString(group_code);
			dest.writeString(group_type);
		}
		
//		private Framework(Parcel dest)  
//	    {  
//			this.moduleId = dest.readString();
//			this.parentId = dest.readString();
//			this.moduleName = dest.readString();
//			this.isMenuItem = dest.readString();
//			this.isAddMenuItem = dest.readString();
//			this.moduleType = dest.readString();
//			this.packageName = dest.readString();
//			this.iconName = dest.readString();
//			this.thumbnailName = dest.readString();
//			this.clickUrl = dest.readString();
//			this.isVisible = dest.readString();
//			this.isVisibleOrder = dest.readString();
//			this.fixedPage = dest.readString();
//			this.isLogin = dest.readString();
//			this.updateDate = dest.readString(); 
//	    }  
//		private Framework(){
//			
//		}  
//
		
}
