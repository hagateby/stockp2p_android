package com.stockp2p.common.db;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.lidroid.xutils.db.annotation.Id;

public class FrameWork_Frame implements Parcelable{

	// 主键 模块的 id号
		
		public String frameId;
	
		// 父id 上一级的主键
		public String parentId;
		// 模块名称
		public String frameName;
		// 添加列表点击后是不是显示下一级 0-显示 1-不显示
		public String isMenuItem;
		// 是否为添加列表显示的模块 0-否 1-是
		public String isAddMenuItem;
		// 模块类型 1为列表,有下一级菜单  2为详情  3为 webview 4 为打开另一个程序
		public String frameType;
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
		public String frameOrderby;
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
	    public String groupCode;     
	    
		// 图片在file的代码
		public String imagFileCode;	    
	    // 显示类型
	    public String remark;	    
	    //布局ID的名称
	    public String  layoutName;
	    // 显示类型
	    public String showType;    
	      
		public String getFrameId() {
			return frameId;
		}


		public void setFrameId(String frameId) {
			this.frameId = frameId;
		}


		public String getParentId() {
			return parentId;
		}


		public void setParentId(String parentId) {
			this.parentId = parentId;
		}


		public String getFrameName() {
			return frameName;
		}


		public void setFrameName(String frameName) {
			this.frameName = frameName;
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


		public String getFrameType() {
			return frameType;
		}


		public void setFrameType(String frameType) {
			this.frameType = frameType;
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


		public String getImagFileCode() {
			return imagFileCode;
		}


		public void setImagFileCode(String imagFileCode) {
			this.imagFileCode = imagFileCode;
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
			return frameOrderby;
		}


		public void setModuleOrderby(String moduleOrderby) {
			this.frameOrderby = moduleOrderby;
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


		public String getOpeFlag() {
			return opeFlag;
		}


		public void setOpeFlag(String opeFlag) {
			this.opeFlag = opeFlag;
		}


		public String getGroupCode() {
			return groupCode;
		}


		public void setGroupCode(String groupCode) {
			this.groupCode = groupCode;
		}


		public String getRemark() {
			return remark;
		}


		public void setRemark(String remark) {
			this.remark = remark;
		}


		public String getLayoutName() {
			return layoutName;
		}


		public void setLayoutName(String layoutName) {
			this.layoutName = layoutName;
		}


		public String getShowType() {
			return showType;
		}


		public void setShowType(String showType) {
			this.showType = showType;
		}

		public static Parcelable.Creator<FrameWork_Frame> getCreator() {
			return CREATOR;
		}
	
		
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}


		@Override
		public void writeToParcel(Parcel dest, int flags ) {
			dest.writeString(frameId);
			dest.writeString(parentId);
			dest.writeString(frameName);
			dest.writeString(isMenuItem);
			dest.writeString(isAddMenuItem);
			dest.writeString(frameType);
			dest.writeString(packageName);
			dest.writeString(iconName);
			dest.writeString(thumbnailName);
			dest.writeString(clickUrl);
			dest.writeString(isVisible);
			dest.writeString(isVisibleOrder);
			dest.writeString(fixedPage);
			dest.writeString(isLogin);
			dest.writeString(frameOrderby);
			dest.writeString(updateDate);
			dest.writeString(groupCode);

		}
//
		
		// 序列化实体
		public static final Parcelable.Creator<FrameWork_Frame> CREATOR = new Creator<FrameWork_Frame>() {

			@Override
			public FrameWork_Frame createFromParcel(Parcel source) {
				// 读和写的顺序要一样
				FrameWork_Frame framework = new FrameWork_Frame();
				framework.frameId = source.readString();
				framework.parentId = source.readString();
				framework.frameName = source.readString();
				framework.isMenuItem = source.readString();
				framework.isAddMenuItem = source.readString();
				framework.frameType = source.readString();
				framework.packageName = source.readString();
				framework.iconName = source.readString();
				framework.thumbnailName = source.readString();
				framework.clickUrl = source.readString();
				framework.isVisible = source.readString();
				framework.isVisibleOrder = source.readString();
				framework.fixedPage = source.readString();
				framework.isLogin = source.readString();
				framework.frameOrderby = source.readString();
				framework.updateDate = source.readString();
				framework.groupCode = source.readString();
					
				return framework;
			}

			@Override
			public FrameWork_Frame[] newArray(int size) {
				return new FrameWork_Frame[size];
			}
		};
		
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

}
