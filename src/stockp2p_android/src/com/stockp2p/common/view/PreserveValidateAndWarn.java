package com.stockp2p.common.view;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

//import com.ktsf.components.policyinforquery.PolicyInforQueryActivity;

public class PreserveValidateAndWarn {
	
	private static PreserveValidateAndWarn pvaw = null;
	
	private PreserveValidateAndWarn(){}
	
	public static PreserveValidateAndWarn getInstance(){
		if(pvaw == null){
			pvaw = new PreserveValidateAndWarn();
		}
		return pvaw;
	}
	
	public String parseMap(Map resultMap, String somebody, String hasDuty) {
		String isSomebody = null;
		if (null!=hasDuty) {
			Map rcMap = (Map) resultMap.get(somebody);
			if (null!=rcMap) {				
				String flag = (null==rcMap.get(hasDuty))?"":(rcMap.get(hasDuty)).toString();
				return flag;
			}
		} else {			
			isSomebody = (String) resultMap.get(somebody);
		}
		
		return isSomebody;
	}
	
	public void showPolListDialog(final Context context, String msg, final String submit, final int submitType, final boolean closed) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		if (null!=submit) {
			builder.setMessage(submit);
		} else {			
			builder.setMessage(msg);
		}
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (0==submitType) {
					//((Activity)context).finish();
					return;
				}
				if (null!=submit) {
					
					return;
				} 
				if (!closed) {
					return;
				}
				//((Activity)context).finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
}
