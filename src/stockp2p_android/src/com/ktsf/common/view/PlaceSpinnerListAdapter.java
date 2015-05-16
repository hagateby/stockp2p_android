package com.pactera.nci.common.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pactera.nci.R;
import com.pactera.nci.common.db.Code;
import com.pactera.nci.common.db.Place;

public class PlaceSpinnerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Place> placeList = new ArrayList<Place>();
	private ArrayList<Code> bankList = new ArrayList<Code>();
	
	public PlaceSpinnerListAdapter( Context context, ArrayList<Place> placeList ){
		this.placeList = placeList;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return placeList.size();
	}

	@Override
	public Object getItem(int position) {
		return placeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate( context, R.layout.receivechange_place_spinner_item, null );
		TextView tv_spinner_name = (TextView) view.findViewById( R.id.tv_spinner_name );
		tv_spinner_name.setText( placeList.get(position).getShortname() );
		return view;
	}
    
}