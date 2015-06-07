package com.stockp2p.common.ifinvoke;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.alibaba.fastjson.JSON;

public class JsonInvok {

/**
 * 发送短认验证码
 * */	
	
public static void invoksendsms(String userTelephone, final Context context, EnginCallback callback)
{
	Map map = new HashMap();
	
	map.put("mobile", userTelephone);
	
	String servicePara = JSON.toJSONString(map);
	
	ServiceEngin.Request(context, "00_01_I02", "getRandom" ,  servicePara,callback);
}

/**
 * login登陆
 * */	
	
public static void invoklogin(String msg, final Context context, EnginCallback callback)
{
	ServiceEngin.Request(context, "00_00_I01", "login" ,  msg,callback);

}
/**
 * 注册
 * */	
	
public static void invokregister(String msg, final Context context, EnginCallback callback)
{
	ServiceEngin.Request(context, "00_01_I01", "userToRegister",
			msg, new EnginCallback(context));

}

}
