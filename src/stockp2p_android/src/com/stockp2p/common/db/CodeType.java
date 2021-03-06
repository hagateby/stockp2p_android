package com.stockp2p.common.db;

/**
 * 码表
 * 
 * @author haix
 * 
 */
public class CodeType {

	private String paramTypeId; // 类别标志
	private String valueCode; // 码值
	private String valueName; // 名称
	private String valueId; // 码表 id
	private String opeFlag;// 操作标志c创建u更新d删除

	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}

	public String getParamTypeId() {
		return paramTypeId;
	}

	public void setParamTypeId(String paramTypeId) {
		this.paramTypeId = paramTypeId;
	}

	public String getValueCode() {
		return valueCode;
	}

	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public String getOpeFlag() {
		return opeFlag;
	}

	public void setOpeFlag(String opeFlag) {
		this.opeFlag = opeFlag;
	}

}
