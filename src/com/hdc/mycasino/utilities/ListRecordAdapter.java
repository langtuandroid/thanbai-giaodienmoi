package com.hdc.mycasino.utilities;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdc.mycasino.R;

public class ListRecordAdapter extends ArrayAdapter<Item> {

	private Context context;
	private int resourse;
	private ArrayList<Item> arraylist;
	private String link;

	public ListRecordAdapter(Context context, int textViewResourceId, ArrayList<Item> objects,
			String _link) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resourse = textViewResourceId;
		this.arraylist = objects;
		this.link = _link;
	}

	// get ItemDTO
	public Item getItems(int position) {
		return arraylist.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View v = convertView;
		if (v == null) {
			LayoutInflater layout = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = layout.inflate(R.layout.items, null);
		}

		Item item = arraylist.get(position);
		if (item != null) {
			TextView title = (TextView) v.findViewById(R.id.txtTitle);
			TextView date = (TextView) v.findViewById(R.id.txtDateTime);
			ImageView image = (ImageView) v.findViewById(R.id.imageView1);

			if (title != null) {
				title.setText(item.getTitle());
			}
			// if (date != null) {
			// date.setText(item.getInfo());
			// }
			// if (image != null) {
			// try {
			// image.setImageBitmap(item.getImg());
			// } catch (Exception e) {
			//
			// }
			// }
		}

		return v;
	}
}