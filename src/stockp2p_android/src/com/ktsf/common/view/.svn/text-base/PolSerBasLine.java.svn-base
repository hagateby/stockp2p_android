package com.pactera.nci.common.view;

import com.pactera.nci.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PolSerBasLine extends LinearLayout {

	public PolSerBasLine(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.common_policy_server_base_line, this);
	}

	public void setValue(String left, String right) {
		TextView leftView = (TextView) findViewById(R.id.left);
		TextView rightView = (TextView) findViewById(R.id.right);
		String space = getResources().getString(R.string.cn_space);
		if (left.length() == 3) {
			left = left.substring(0, 1) + space + space + left.substring(1, 2)
					+ space + space + left.substring(2, 3) + space;
		} else if (left.length() == 2) {
			left = left.substring(0, 1) + space + space + space + space + space
					+ space + space + space + left.substring(1, 2) + space;
		} else if (left.length() == 4) {
			left = left + space;
		}
		leftView.setText(left + ":");
		rightView.setText(right);
	}

}
