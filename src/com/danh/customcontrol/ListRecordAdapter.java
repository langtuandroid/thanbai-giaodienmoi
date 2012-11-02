package com.danh.customcontrol;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ListRecordAdapter extends ArrayAdapter<String> {

	private Context context;
	private int resourse;
	private ArrayList<String> arraylist;
	private String link;

	public ListRecordAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects, String _link) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resourse = textViewResourceId;
		this.arraylist = objects;
		this.link = _link;
	}

	public void setList(ArrayList<String> objects){
		this.arraylist = objects;
	}
	
	// get ItemDTO
	public String getItems(int position) {
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
//			v = layout.inflate(R.layout.items_new, null);
		}

//		final Item item = arraylist.get(position);
//		if (item != null) {
//			TextView title = (TextView) v.findViewById(R.id.txtTitle);
//			TextView date = (TextView) v.findViewById(R.id.txtDateTime);
//			TextView duration = (TextView) v.findViewById(R.id.txtDuration);
//			ImageView image = (ImageView) v.findViewById(R.id.imageView1);
//
//			if (title != null) {
//				title.setText(item.getTitle());
//			}
//			if (date != null) {
//				date.setText("Views : " + item.getDownload());
//			}
//			if(!item.getDuration().equals("null")){
//				duration.setText("Times: " + item.getDuration());
//			}else{
//				duration.setText(/*"Times: " + "01:00"*/"");
//			}
//			if (image != null && item.getImg()!=null) {
//				try {					
//					image.setImageBitmap(item.getImg());
//				} catch (Exception e) {
//				}
//			}else{
//				image.setImageDrawable(context.getResources().getDrawable(R.drawable.bg));
////				image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg));
//			}
//			
//
//		}

		return v;
	}
}