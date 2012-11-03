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
import com.hdc.mycasino.model.BoardInfo;
import com.hdc.mycasino.model.RoomInfo;

public class ListTableAdapter extends ArrayAdapter<BoardInfo> {

	private Context context;
	private int resourse;
	private ArrayList<BoardInfo> arraylist = new ArrayList<BoardInfo>();
	private String link;

	public ListTableAdapter(Context context, int textViewResourceId,
			ArrayList<BoardInfo> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resourse = textViewResourceId;
		this.arraylist = objects;
	}

	public void setList(ArrayList<BoardInfo> objects) {
		this.arraylist = objects;
	}

	// get ItemDTO
	public BoardInfo getItems(int position) {
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

		// TODO bàn
		TextView txt_ban = (TextView) v.findViewById(R.id.txt_table);
		txt_ban.setText("Bàn " + arraylist.get(position).itemId);

		// TODO Khóa bàn
		ImageView imgLock = (ImageView) v.findViewById(R.id.imgLock);
		if (arraylist.get(position).isLock) {
			imgLock.setVisibility(View.VISIBLE);
		} else {
			imgLock.setVisibility(View.GONE);
		}

		// TODO tiền cược
		TextView txt_TienCuoc = (TextView) v.findViewById(R.id.txt_bet);
		txt_TienCuoc.setText(arraylist.get(position).betGold + " Gold");

		// TODO người chơi
		ImageView imgPlayer_1 = (ImageView) v.findViewById(R.id.img_player_1);
		ImageView imgPlayer_2 = (ImageView) v.findViewById(R.id.img_player_2);
		ImageView imgPlayer_3 = (ImageView) v.findViewById(R.id.img_player_3);
		ImageView imgPlayer_4 = (ImageView) v.findViewById(R.id.img_player_4);

		switch (arraylist.get(position).numberPlayer) {
		case 0:
			imgPlayer_1.setImageResource(R.drawable.icon_ghe_disable);
			imgPlayer_2.setImageResource(R.drawable.icon_ghe_disable);
			imgPlayer_3.setImageResource(R.drawable.icon_ghe_disable);
			imgPlayer_4.setImageResource(R.drawable.icon_ghe_disable);
			break;
		case 1:
			imgPlayer_1.setImageResource(R.drawable.icon_ghe_enable);
			imgPlayer_2.setImageResource(R.drawable.icon_ghe_disable);
			imgPlayer_3.setImageResource(R.drawable.icon_ghe_disable);
			imgPlayer_4.setImageResource(R.drawable.icon_ghe_disable);

			break;
		case 2:
			imgPlayer_1.setImageResource(R.drawable.icon_ghe_enable);
			imgPlayer_2.setImageResource(R.drawable.icon_ghe_enable);
			imgPlayer_3.setImageResource(R.drawable.icon_ghe_disable);
			imgPlayer_4.setImageResource(R.drawable.icon_ghe_disable);

			break;
		case 3:
			imgPlayer_1.setImageResource(R.drawable.icon_ghe_enable);
			imgPlayer_2.setImageResource(R.drawable.icon_ghe_enable);
			imgPlayer_3.setImageResource(R.drawable.icon_ghe_enable);
			imgPlayer_4.setImageResource(R.drawable.icon_ghe_disable);

			break;
		case 4:
			imgPlayer_1.setImageResource(R.drawable.icon_ghe_enable);
			imgPlayer_2.setImageResource(R.drawable.icon_ghe_enable);
			imgPlayer_3.setImageResource(R.drawable.icon_ghe_enable);
			imgPlayer_4.setImageResource(R.drawable.icon_ghe_enable);

			break;
		default:
			break;
		}

		return v;
	}
}