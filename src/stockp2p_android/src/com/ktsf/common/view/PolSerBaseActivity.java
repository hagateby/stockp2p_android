package com.pactera.nci.common.view;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pactera.nci.R;
import com.pactera.nci.framework.BaseFragmentActivity;

public class PolSerBaseActivity extends BaseFragmentActivity {

	public LinearLayout mainLayout;
	public TextView mainTitle;
	public LinearLayout baseLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void setTitle(String title) {
		setContainerView(title, R.layout.policyinforquery_policy_server_main_layout);
		mainLayout = (LinearLayout) findViewById(R.id.policy_server_main_layout_ll_mainlayout);
		mainTitle = (TextView) findViewById(R.id.policy_server_main_layout_tv_maintitle);
		mainTitle.setText(title);
	}

	public void addContent(String title, String[][] property,
			Map<String, String> map, int arg) {
		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout baseLayout = (LinearLayout) inflater.inflate(
				R.layout.common_policy_server_base_layout, null);
		TextView titleView = (TextView) baseLayout.findViewById(R.id.title);
		TextView arrow = (TextView) baseLayout.findViewById(R.id.arrow);
		arrow.setVisibility(arg);
		if (title.equals("")) {
			titleView.setVisibility(View.GONE);
		} else {
			titleView.setText(title);
			// baseLayout.setPadding(0, 0, 0, 10);
		}
		LinearLayout contentLayout = (LinearLayout) baseLayout
				.findViewById(R.id.content);
		for (int i = 0; i < property.length; i++) {
			PolSerBasLine policyServerBaseLine = new PolSerBasLine(this);
			policyServerBaseLine.setValue(property[i][0],
					map.get(property[i][1]));
			contentLayout.addView(policyServerBaseLine);
		}
		mainLayout.addView(baseLayout);
	}

	public void addContentLineGone(String title, String[][] property,
			Map<String, String> map, int arg) {
		LayoutInflater inflater = LayoutInflater.from(context);
		baseLayout = (LinearLayout) inflater.inflate(
				R.layout.common_policy_server_base_layout, null);
		baseLayout.setPadding(0, 20, 0, 0);
		TextView titleView = (TextView) baseLayout.findViewById(R.id.title);
		LinearLayout content_title = (LinearLayout) baseLayout
				.findViewById(R.id.content_title);
		TextView arrow = (TextView) baseLayout.findViewById(R.id.arrow);
		arrow.setVisibility(arg);
		if (title.equals("")) {
			titleView.setVisibility(View.INVISIBLE);
			content_title.setVisibility(View.GONE);
		} else {
			titleView.setText(title);
			// baseLayout.setPadding(0, 0, 0, 10);
		}
		LinearLayout contentLayout = (LinearLayout) baseLayout
				.findViewById(R.id.content);
		for (int i = 0; i < property.length; i++) {
			PolSerBasLine policyServerBaseLine = new PolSerBasLine(context);
			policyServerBaseLine.setValue(property[i][0],
					map.get(property[i][1]));
			contentLayout.addView(policyServerBaseLine);
		}
		mainLayout.addView(baseLayout);
	}
	// public void addContent(LinearLayout linearLayout,String title, String[][]
	// property, Map<String, String> map) {
	// LayoutInflater inflater = LayoutInflater.from(this);
	// LinearLayout baseLayout =
	// (LinearLayout)inflater.inflate(R.layout.policy_server_base_layout, null);
	// TextView titleView = (TextView)baseLayout.findViewById(R.id.title);
	// if(title.equals("")) {
	// titleView.setVisibility(View.GONE);
	// } else {
	// titleView.setText(title);
	// baseLayout.setPadding(0, 0, 0, 20);
	// }
	// LinearLayout contentLayout =
	// (LinearLayout)baseLayout.findViewById(R.id.content);
	// for(int i = 0; i < property.length; i++) {
	// PolSerBasLine policyServerBaseLine = new PolSerBasLine(this);
	// policyServerBaseLine.setValue(property[i][0], map.get(property[i][0]));
	// contentLayout.addView(policyServerBaseLine);
	// }
	// mainLayout.addView(baseLayout);
	// }
}
