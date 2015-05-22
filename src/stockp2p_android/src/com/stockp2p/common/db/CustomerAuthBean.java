package com.stockp2p.common.db;

/**
 * 客户信息认证查徇到的信息
 * @author haix
 *
 */
public class CustomerAuthBean {
	private String birthday;          //生日
	private String idno;     //证件号码
	private String idtype;  //证件类型
	private String idtypeName;   //证件类型名称
	private String name;  //姓名
	private String sex;       //性别码
	private String sexName; //女
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getIdtypeName() {
		return idtypeName;
	}
	public void setIdtypeName(String idtypeName) {
		this.idtypeName = idtypeName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	
	
	
}
