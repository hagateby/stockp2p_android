package com.stockp2p.common.view.scrolldatepicker;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

import com.stockp2p.R;
import com.stockp2p.common.view.wheel.OnWheelChangedListener;
import com.stockp2p.common.view.wheel.WheelView;

public class ScrollDatePicker extends Dialog implements OnTouchListener {
	private ImageButton btnClose;
	private ImageButton btnOk;
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";
	private Context mContext;
	private static int START_YEAR = 1900, END_YEAR = 2100;
	private WheelView yearWheel;
	private WheelView monthWheel;
	private WheelView dayWheel;
	private OnDateSetListener mCallBack;
	private int year;
	private int monthOfYear;
	private int dayOfMonth;

	public ScrollDatePicker(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		this(context, R.style.MyDialog, callBack, year, monthOfYear, dayOfMonth);
	}

	public ScrollDatePicker(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme);
		this.mContext = context;
		this.mCallBack = callBack;
		this.year = year;
		this.monthOfYear = monthOfYear;
		this.dayOfMonth = dayOfMonth;
	}

	/**
	 * 获得 指定年月的天数
	 * 
	 * @param year
	 *            指定年
	 * @param month
	 *            指定月
	 * @return
	 */
	private static int getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.common_dialog_datepicker);
		btnClose = (ImageButton) findViewById(R.id.imgbtnClose);
		btnOk = (ImageButton) findViewById(R.id.imgbtnOk);
		this.yearWheel = (WheelView) findViewById(R.id.year);
		yearWheel.setCyclic(true);
		yearWheel.setLabel("年");

		this.monthWheel = (WheelView) findViewById(R.id.month);
		monthWheel.setCyclic(true);
		monthWheel.setLabel("月");

		this.dayWheel = (WheelView) findViewById(R.id.day);
		dayWheel.setCyclic(true);
		dayWheel.setLabel("日");

		yearWheel.addChangingListener(wheelListener_year);
		monthWheel.addChangingListener(wheelListener_month);
		this.btnClose.setOnTouchListener(this);
		this.btnOk.setOnTouchListener(this);

		int textSize = 0;

		DisplayMetrics dm = new DisplayMetrics();

		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		textSize = dm.widthPixels / 20;

		dayWheel.TEXT_SIZE = textSize;
		monthWheel.TEXT_SIZE = textSize;
		yearWheel.TEXT_SIZE = textSize;
		init(year, monthOfYear, dayOfMonth);

	}

	OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {

			int year_num = newValue + START_YEAR;
			int month_num = monthWheel.getCurrentItem() + 1;
			int day_num = dayWheel.getCurrentItem() + 1;

			int lastDay = getLastDayOfMonth(year_num, month_num);
			dayWheel.setAdapter(new NumericWheelAdapter(1, lastDay));
			if (day_num > lastDay) {
				dayWheel.setCurrentItem(lastDay - 1);
			}
		}
	};
	OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			int month_num = newValue + 1;
			int year_num = yearWheel.getCurrentItem() + START_YEAR;
			int day_num = dayWheel.getCurrentItem() + 1;

			int lastDay = getLastDayOfMonth(year_num, month_num);
			dayWheel.setAdapter(new NumericWheelAdapter(1, lastDay));
			if (day_num > lastDay) {
				dayWheel.setCurrentItem(lastDay - 1);
			}
		}
	};

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(YEAR, yearWheel.getCurrentItem());
		state.putInt(MONTH, monthWheel.getCurrentItem());
		state.putInt(DAY, dayWheel.getCurrentItem());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		yearWheel.setCurrentItem(savedInstanceState.getInt(YEAR));
		monthWheel.setCurrentItem(savedInstanceState.getInt(MONTH));
		dayWheel.setCurrentItem(savedInstanceState.getInt(DAY));
	}

	private void init(int year, int month, int day) {

		yearWheel.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));
		yearWheel.setCurrentItem(year - START_YEAR);
		monthWheel.setAdapter(new NumericWheelAdapter(1, 12));
		monthWheel.setCurrentItem(month);
		int lastDay = getLastDayOfMonth(year, month);
		dayWheel.setAdapter(new NumericWheelAdapter(1, lastDay));
		if (day > lastDay) {
			dayWheel.setCurrentItem(lastDay - 1);
		} else {
			dayWheel.setCurrentItem(day - 1);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		if (v == this.btnClose) {
			this.cancel();
			return false;
		} else if (v == this.btnOk) {
			int year_num = yearWheel.getCurrentItem() + START_YEAR;
			int month_num = monthWheel.getCurrentItem();
			int day_num = dayWheel.getCurrentItem() + 1;
			if (mCallBack != null) {
				mCallBack.onDateSet(null, year_num, month_num, day_num);
			}
			this.cancel();
			return true;
		}
		return true;
	}
}
