package com.ktsf.common.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author haix 定位用的数据
 */
public class PositionPlace {

	private String organization_id;// 城市码
	private String organization_name;// 城市名
	private String parent_id;// 二级关联码
	private String created_date;// 间时
	private String updated_date;
	private String organization_level;// 级别

	public String getOrganization_id() {
		return organization_id;
	}

	public void setOrganization_id(String organization_id) {
		this.organization_id = organization_id;
	}

	public String getOrganization_name() {
		return organization_name;
	}

	public void setOrganization_name(String organization_name) {
		this.organization_name = organization_name;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}

	public String getOrganization_level() {
		return organization_level;
	}

	public void setOrganization_level(String organization_level) {
		this.organization_level = organization_level;
	}

	/**
	 * @param db
	 * @param organization_level
	 *            //传递进的数据库字段
	 * @param value
	 *            字段等于的值
	 * @return 根据organization_level 的值查找所有的城市
	 */
	public static ArrayList<PositionPlace> findByLevel(SQLiteDatabase db,
			String organization_level, String value) {
		Cursor cursor = db.query("organization", new String[] {
				"organization_id", "organization_name", "parent_id",
				"created_date", "updated_date", "organization_level" },
				organization_level + "=" + value, null, null, null,
				"organization_id");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<PositionPlace> placeList = new ArrayList<PositionPlace>();
		for (int i = 0; i < counts; i++) {
			PositionPlace positionPlace = new PositionPlace();
			positionPlace.setOrganization_id(cursor.getString(cursor
					.getColumnIndex("organization_id")));
			positionPlace.setOrganization_name(cursor.getString(cursor
					.getColumnIndex("organization_name")));
			positionPlace.setParent_id(cursor.getString(cursor
					.getColumnIndex("parent_id")));
			positionPlace.setCreated_date(cursor.getString(cursor
					.getColumnIndex("created_date")));
			positionPlace.setUpdated_date(cursor.getString(cursor
					.getColumnIndex("updated_date")));
			positionPlace.setOrganization_level(cursor.getString(cursor
					.getColumnIndex("organization_level")));

			placeList.add(positionPlace);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return placeList;
	}

	public static ArrayList<PositionPlace> findByLevel(SQLiteDatabase db,
			String organization_level, String value,
			ArrayList<PositionPlace> placeList) {
		Cursor cursor = db.query("organization", new String[] {
				"organization_id", "organization_name", "parent_id",
				"created_date", "updated_date", "organization_level" },
				organization_level + "=" + value, null, null, null,
				"organization_id");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		for (int i = 0; i < counts; i++) {
			PositionPlace positionPlace = new PositionPlace();
			positionPlace.setOrganization_id(cursor.getString(cursor
					.getColumnIndex("organization_id")));
			positionPlace.setOrganization_name(cursor.getString(cursor
					.getColumnIndex("organization_name")));
			positionPlace.setParent_id(cursor.getString(cursor
					.getColumnIndex("parent_id")));
			positionPlace.setCreated_date(cursor.getString(cursor
					.getColumnIndex("created_date")));
			positionPlace.setUpdated_date(cursor.getString(cursor
					.getColumnIndex("updated_date")));
			positionPlace.setOrganization_level(cursor.getString(cursor
					.getColumnIndex("organization_level")));

			placeList.add(positionPlace);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return placeList;
	}

	/**
	 * @param db
	 * @param value
	 *            parent_id的值
	 * @return 根据parent_id的值查找所有下级列表的城市
	 */
	public static ArrayList<PositionPlace> findByParent_id(SQLiteDatabase db,
			String value, String level) {
		Cursor cursor = db.query("organization", new String[] {
				"organization_id", "organization_name", "parent_id",
				"created_date", "updated_date", "organization_level" },
				"parent_id =" + value + " and organization_level =" + level,
				null, null, null, "order_by");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		ArrayList<PositionPlace> placeList = new ArrayList<PositionPlace>();
		for (int i = 0; i < counts; i++) {
			PositionPlace positionPlace = new PositionPlace();
			positionPlace.setOrganization_id(cursor.getString(cursor
					.getColumnIndex("organization_id")));
			positionPlace.setOrganization_name(cursor.getString(cursor
					.getColumnIndex("organization_name")));
			positionPlace.setParent_id(cursor.getString(cursor
					.getColumnIndex("parent_id")));
			positionPlace.setCreated_date(cursor.getString(cursor
					.getColumnIndex("created_date")));
			positionPlace.setUpdated_date(cursor.getString(cursor
					.getColumnIndex("updated_date")));
			positionPlace.setOrganization_level(cursor.getString(cursor
					.getColumnIndex("organization_level")));

			placeList.add(positionPlace);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return placeList;
	}

	public static ArrayList<PositionPlace> findByParent_id(SQLiteDatabase db,
			String value, ArrayList<PositionPlace> placeList) {
		Cursor cursor = db.query("organization", new String[] {
				"organization_id", "organization_name", "parent_id",
				"created_date", "updated_date", "organization_level" },
				"parent_id =" + value + " and organization_level =" + 3, null,
				null, null, "order_by");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		for (int i = 0; i < counts; i++) {
			PositionPlace positionPlace = new PositionPlace();
			positionPlace.setOrganization_id(cursor.getString(cursor
					.getColumnIndex("organization_id")));
			positionPlace.setOrganization_name(cursor.getString(cursor
					.getColumnIndex("organization_name")));
			positionPlace.setParent_id(cursor.getString(cursor
					.getColumnIndex("parent_id")));
			positionPlace.setCreated_date(cursor.getString(cursor
					.getColumnIndex("created_date")));
			positionPlace.setUpdated_date(cursor.getString(cursor
					.getColumnIndex("updated_date")));
			positionPlace.setOrganization_level(cursor.getString(cursor
					.getColumnIndex("organization_level")));

			placeList.add(positionPlace);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return placeList;
	}

	public static ArrayList<PositionPlace> findByLevel_writing(
			SQLiteDatabase db, String organization_level, String value,
			ArrayList<PositionPlace> placeList) {
		Cursor cursor = db.query("organization", new String[] {
				"organization_id", "organization_name", "parent_id",
				"created_date", "updated_date", "organization_level" },
				organization_level + "=" + value, null, null, null,
				"organization_id");
		int counts = cursor.getCount();
		if (counts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		for (int i = 0; i < counts; i++) {
			PositionPlace positionPlace = new PositionPlace();
			positionPlace.setOrganization_id(cursor.getString(cursor
					.getColumnIndex("organization_id")));
			positionPlace.setOrganization_name(cursor.getString(cursor
					.getColumnIndex("organization_name")));
			positionPlace.setParent_id(cursor.getString(cursor
					.getColumnIndex("parent_id")));
			positionPlace.setCreated_date(cursor.getString(cursor
					.getColumnIndex("created_date")));
			positionPlace.setUpdated_date(cursor.getString(cursor
					.getColumnIndex("updated_date")));
			positionPlace.setOrganization_level(cursor.getString(cursor
					.getColumnIndex("organization_level")));

			placeList.add(positionPlace);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return placeList;
	}

	public static ArrayList<PositionPlace> findByParent_id_writing(
			SQLiteDatabase db, ArrayList<PositionPlace> placeList) {
		System.out.println("placeList.get(0).getOrganization_id()"+placeList.get(0).getOrganization_id());
			Cursor cursor = db
					.query("organization",
							new String[] { "organization_id",
									"organization_name" },
							" organization_id in ('862302', '862309', '862611', '862613', '863500', '863600', '864600','864700', '867400') or organization_level ='2'",
							null, null, null,
							"organization_level, organization_id");
			int counts = cursor.getCount();
			if (counts == 0 || !cursor.moveToFirst()) {
				return null;
			}
			for (int i = 0; i < counts; i++) {
				PositionPlace positionPlace = new PositionPlace();
				positionPlace.setOrganization_id(cursor.getString(cursor
						.getColumnIndex("organization_id")));
				positionPlace.setOrganization_name(cursor.getString(cursor
						.getColumnIndex("organization_name")));
				placeList.add(positionPlace);
				cursor.moveToNext();
			}
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		return placeList;
	}

}
