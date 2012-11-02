package com.danh.customcontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hdc.mycasino.R;

public class MyCustomSpinner extends ArrayAdapter<String> {

	Context mContext;
	int layout;
	String[] data;

	public MyCustomSpinner(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		// TODO Aut-generated constructor stub
		mContext = context;
		layout = textViewResourceId;
		data = objects;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// super.getView(position, convertView, parent);

		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// super.getView(position, convertView, parent);

		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// return super.getView(position, convertView, parent);

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(layout, parent, false);

		TextView label = (TextView) row.findViewById(R.id.txtRow);
		label.setText(data[position]);

		return row;
	}

}
