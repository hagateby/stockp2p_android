package com.stockp2p.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.stockp2p.common.view.scrolldatepicker.ScrollDatePicker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

public class LastYearDateSetOnClickListener implements OnClickListener {
	
	private Calendar cal = Calendar.getInstance();
	private FragmentActivity activity;
	
	public LastYearDateSetOnClickListener(FragmentActivity activity) {
		this.activity = activity;
		cal.add(Calendar.YEAR, -1); //年份减1  
	}
	
	@Override
	public void onClick(final View v) {
	    new ScrollDatePicker(activity, new DatePickerDialog.OnDateSetListener(){  
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {		 
				cal.set(Calendar.YEAR, arg1);		 
				cal.set(Calendar.MONTH, arg2);		 
				cal.set(Calendar.DAY_OF_MONTH, arg3);		 
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				((TextView)v).setText(simpleDateFormat.format(cal.getTime()));		 
			}			 
		}, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show();
	}	
}	
	
