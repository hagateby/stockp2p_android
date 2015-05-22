package com.stockp2p.common.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.stockp2p.R;

public class ClearEditText extends EditText implements OnTouchListener,
		TextWatcher

{
	// 缓存上一次文本框内是否为空
	private boolean isnull = true;
	private Drawable mIconClear; // 搜索文本框清除文本内容图标

	public ClearEditText(Context context, AttributeSet attrs, int defStyle)

	{
		super(context, attrs, defStyle);
		init();
	}

	public ClearEditText(Context context, AttributeSet attrs) {

		super(context, attrs);
		init();
	}

	public ClearEditText(Context context) {

		super(context);
		init();
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	private void init() {
		final Resources res = getResources();
		mIconClear = res.getDrawable(R.drawable.common_clearedittext_del_icon1);
		this.addTextChangedListener(this);
		this.setOnTouchListener(this);
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (TextUtils.isEmpty(s)) {
			if (!isnull) {
				setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				isnull = true;
			}
		} else {
			if (isnull) {
				setCompoundDrawablesWithIntrinsicBounds(null, null, mIconClear,
						null);
				isnull = false;
			}
		}

	}

	/**
	 * 随着文本框内容改变动态改变列表内容
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int before,
			int count) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			int curX = (int) event.getX();
			if (curX > v.getWidth() - 38 && !TextUtils.isEmpty(getText())) {
				setText("");
				int cacheInputType = getInputType();// backup the input type
				setInputType(InputType.TYPE_NULL);// disable soft input
				onTouchEvent(event);// call native handler
				setInputType(cacheInputType);// restore input type
				return true;// consume touch even
			}
			break;
		}
		return false;
	}

}
