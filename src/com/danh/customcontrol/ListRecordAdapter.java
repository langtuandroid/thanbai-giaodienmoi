package com.danh.customcontrol;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdc.mycasino.R;
import com.hdc.mycasino.model.RoomInfo;

public class ListRecordAdapter extends ArrayAdapter<RoomInfo> {

	private Context context;
	private int resourse;
	private ArrayList<RoomInfo> arraylist = new ArrayList<RoomInfo>();
	private String link;

	public ListRecordAdapter(Context context, int textViewResourceId,
			ArrayList<RoomInfo> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resourse = textViewResourceId;
		this.arraylist = objects;
	}

	public void setList(ArrayList<RoomInfo> objects){
		this.arraylist = objects;
	}
	
	// get ItemDTO
	public RoomInfo getItems(int position) {
		return arraylist.get(position);
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View v = convertView;
		if (v == null) {
			LayoutInflater layout = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = layout.inflate(resourse, null);

		}

		TextView t = (TextView)v.findViewById(R.id.room);
		t.setText("Phòng " + arraylist.get(position).itemName);

		ImageView img = (ImageView)v.findViewById(R.id.imgRoomStatus);
		int status = arraylist.get(position).status; 
		if(status == 0){
			img.setImageResource(R.drawable.icon_room_status_0);
		}else if(status == 1){
			img.setImageResource(R.drawable.icon_room_status_1);
		}else{
			img.setImageResource(R.drawable.icon_room_status_2);
		}
		
		return v;
	}
}