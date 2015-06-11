package com.stockp2p.common.util;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingSharedPreferences {
	/** 
     * 向SharedPreferences中存储数据 
     * @param context 
     * @param fileName 
     * @param key 
     * @param value 
     */  
    public static void setSharedPreferences(Context context, String fileName,  
            String key, Object value) {  
        if (context != null && fileName != null) {  
  
            SharedPreferences sp = context.getSharedPreferences(fileName,  
                    Context.MODE_PRIVATE);  
            Editor editor = sp.edit();  
            if (value instanceof Boolean) {  
                editor.putBoolean(key, (Boolean) value);  
            } else if (value instanceof Float) {  
                editor.putFloat(key, (Float) value);  
            } else if (value instanceof Integer) {  
                editor.putInt(key, (Integer) value);  
            } else if (value instanceof Long) {  
                editor.putLong(key, (Long) value);  
            } else if (value instanceof String) {  
                editor.putString(key, (String) value);  
            }  
            editor.commit();  
        }  
    }  
  
    /** 
     * 获取SharedPreferences中存储的数据 
     * @param context 
     * @param fileName 
     * @param key 
     * @param clazz 
     * @return 
     */  
    public static Object getSharedPreferences(Context context, String fileName,  
            String key, Class<?> clazz) {  
        Object object = null;  
  
        if (context != null && fileName != null && clazz != null) {  
            SharedPreferences sp = context.getSharedPreferences(fileName,  
                    Context.MODE_PRIVATE);  
            String name = clazz.getName().substring(10);  
            if (name.equals("Boolean")) {  
                object = sp.getBoolean(key, false);  
            } else if (name.equals("Float")) {  
                object = sp.getFloat(key, -1);  
            } else if (name.equals("Integer")) {  
                object = sp.getInt(key, -1);  
            } else if (name.equals("Long")) {  
                object = sp.getLong(key, -1);  
            } else if (name.equals("String")) {  
                object = sp.getString(key, null);  
            }  
        }  
        return object;  
    }  
}
