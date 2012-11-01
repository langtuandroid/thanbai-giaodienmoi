package com.hdc.mycasino.customcontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hdc.mycasino.R;

public class CustomGalleryImageAdapter extends BaseAdapter {
	private Context mContext;
	private ImageView image;
	private LayoutInflater mInflater;
	private int count;

	public static int[] mImageID = { R.drawable.e_1, R.drawable.e_2, R.drawable.e_3, R.drawable.e_4,
			R.drawable.e_5, R.drawable.e_6, R.drawable.e_7, R.drawable.e_8, R.drawable.e_9 };

	public CustomGalleryImageAdapter(Context c) {
		mContext = c;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		count = mImageID.length;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View mview = convertView;

		if (mview == null) {
			mview = mInflater.inflate(R.layout.item_galary, null);
		}

		image = (ImageView) mview.findViewById(R.id.imageView1);

		image.setBackgroundResource(mImageID[position]);

		return mview;
	}
}
