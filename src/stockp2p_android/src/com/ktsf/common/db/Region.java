/**   
 * @Title: Region.java
 * @Package com.newchinalife.network.domain
 * @Description: TODO
 * @author wang_xiaofeng 
 * @date 2013-7-12 上午11:12:55
 * @version V1.0   
 */
package com.ktsf.common.db;

/**
 * <p>
 * Title: Region.java
 * </p>
 * 
 * @author wang_xiaofeng
 * @date 2013-7-12 上午11:12:55
 */
public class Region {
	private String regionLevel;// 级别
	private String code;// 编码
	private String name;// 名称
	private String parentCode;
	private String shortName;// 简短名称
	private String opeFlag;// 操作类型c新建,u更新,d删除

	public String getRegionLevel() {
		return regionLevel;
	}

	public void setRegionLevel(String regionLevel) {
		this.regionLevel = regionLevel;
	}

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

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getOpeFlag() {
		return opeFlag;
	}

	public void setOpeFlag(String opeFlag) {
		this.opeFlag = opeFlag;
	}

}
