package com.ktsf.common.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 网点地图
 * @author haix
 *
 */
public class Network {
	
	private int id;
	private String province;//省份
	private String city;//城市
	private String address;
	private String postcode;//邮编
	private String tel;
	private String latitude;//纬度
	private String longitude;//经度
	private String cityCode;//城市code
	private String provinceCode;//省份code
	private String workday;//工作日
	private String saturday;//周六
	private String sunday;//周日
	private String opeFlag;//操作标志c创建u更新d删除
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getWorkday() {
		return workday;
	}
	public void setWorkday(String workday) {
		this.workday = workday;
	}
	public String getSaturday() {
		return saturday;
	}
	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}
	public String getSunday() {
		return sunday;
	}
	public void setSunday(String sunday) {
		this.sunday = sunday;
	}
	public String getOpeFlag() {
		return opeFlag;
	}
	public void setOpeFlag(String opeFlag) {
		this.opeFlag = opeFlag;
	}
	public static ArrayList<Network> findByKey(SQLiteDatabase db, String key, String value){
		Cursor cursor = db.query("network", new String[] {"id", "province", "city", "address",
				"postcode", "tel", "latitude", "longitude", "cityCode", "provinceCode", "workday",
				"saturday", "sunday"}, key + "=" + value, null, null, null, null);
		int counts = cursor.getCount();
		if(counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<Network> networkList = new ArrayList<Network>();
		for (int i = 0; i < counts ; i++) {
			Network network = new Network();
			network.setId(cursor.getInt(cursor.getColumnIndex("id")));
			network.setProvince(cursor.getString(cursor.getColumnIndex("province")));
			network.setCity(cursor.getString(cursor.getColumnIndex("city")));
			network.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			network.setPostcode(cursor.getString(cursor.getColumnIndex("postcode")));
			network.setTel(cursor.getString(cursor.getColumnIndex("tel")));
			network.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
			network.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
			network.setCityCode(cursor.getString(cursor.getColumnIndex("cityCode")));
			network.setProvinceCode(cursor.getString(cursor.getColumnIndex("provinceCode")));
			network.setWorkday(cursor.getString(cursor.getColumnIndex("workday")));
			network.setSaturday(cursor.getString(cursor.getColumnIndex("saturday")));
			network.setSunday(cursor.getString(cursor.getColumnIndex("sunday")));
			networkList.add(network);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return networkList;
	}
	
	public static ArrayList<Network> findByKeyLike(SQLiteDatabase db, String key, String value){
		Cursor cursor = db.query("network", new String[] {"id", "province", "city", "address",
				"postcode", "tel", "latitude", "longitude", "cityCode", "provinceCode", "workday",
				"saturday", "sunday"}, key + " like " + value, null, null, null, null);
		int counts = cursor.getCount();
		if(counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<Network> networkList = new ArrayList<Network>();
		for (int i = 0; i < counts ; i++) {
			Network network = new Network();
			network.setId(cursor.getInt(cursor.getColumnIndex("id")));
			network.setProvince(cursor.getString(cursor.getColumnIndex("province")));
			network.setCity(cursor.getString(cursor.getColumnIndex("city")));
			network.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			network.setPostcode(cursor.getString(cursor.getColumnIndex("postcode")));
			network.setTel(cursor.getString(cursor.getColumnIndex("tel")));
			network.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
			network.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
			network.setCityCode(cursor.getString(cursor.getColumnIndex("cityCode")));
			network.setProvinceCode(cursor.getString(cursor.getColumnIndex("provinceCode")));
			network.setWorkday(cursor.getString(cursor.getColumnIndex("workday")));
			network.setSaturday(cursor.getString(cursor.getColumnIndex("saturday")));
			network.setSunday(cursor.getString(cursor.getColumnIndex("sunday")));
			networkList.add(network);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return networkList;
	}
	 
}