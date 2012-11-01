package com.hdc.mycasino.customcontrol;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.model.IAction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog {
	public static CustomDialog instance = new CustomDialog();
	public Dialog d;
	public Context c;

	public CustomDialog gI() {
		if (instance == null) {
			instance = new CustomDialog();
		}
		return instance;
	}
	
	public void setContext(Context m_Context){
		c = m_Context;
	}

	public void showDialog_yes_no( final String title, final String content,
			final IAction yes) {
		((Activity) c).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) c
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_ketban, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(c,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO Title
				TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
				txtTitle.setText(title.toString());
				// TODO Content
				TextView txtContent = (TextView) v.findViewById(R.id.txtContent);
				txtContent.setText(content.toString());
				// TODO Button Đồng ý
				Button bt1 = (Button) v.findViewById(R.id.bt_DongY);
				bt1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						yes.perform();
						d.dismiss();
					}
				});
				// TODO button Hủy
				Button bt2 = (Button) v.findViewById(R.id.bt_Huy);
				bt2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						d.dismiss();
					}
				});

				d.setContentView(v);
				d.show();
			}
		});
	}

	public void showDialog_Okie(final String title, final String content) {
		((Activity) c).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) c
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_okie, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(c,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO Title
				TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
				txtTitle.setText(title.toString());
				// TODO Content
				TextView txtContent = (TextView) v.findViewById(R.id.txtContent);
				txtContent.setText(content.toString());

				Button bt2 = (Button) v.findViewById(R.id.bt_Huy);
				bt2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						d.dismiss();
//						GameCanvas.endDlg();
					}
				});

				d.setContentView(v);
				d.show();
			}
		});
	}

}
