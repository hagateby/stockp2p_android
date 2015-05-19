package com.ktsf.common.view;

public class MyListItem {
	
	private String code;	//省、市、区编码
	private String name;	//省、市、区名称
	private String pcode;	//省、市、区父编码
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName(){
		return name;
	}
	public String getPcode(){
		return pcode;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setPcode(String pcode){
		this.pcode=pcode;
	}
}
