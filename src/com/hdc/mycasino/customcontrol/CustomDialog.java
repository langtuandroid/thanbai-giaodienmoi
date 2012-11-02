package com.hdc.mycasino.customcontrol;

import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.danh.customcontrol.MyCustomSpinner;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.MoneyInfo;
import com.hdc.mycasino.service.GlobalService;

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

	public void setContext(Context m_Context) {
		c = m_Context;
	}

	Dialog customDialog;

	public void endDialog() {
		if (customDialog != null)
			customDialog.dismiss();
	}

	// TODO dialog waiting
	public void showDialog_Waitting(String title) {
		customDialog = new Dialog(c,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.waitting_1, null, false);

		// TODO title
		TextView m_tittle = (TextView) layout.findViewById(R.id.txtTitle);
		m_tittle.setText(title);

		customDialog.setContentView(layout);
		customDialog.show();
	}

	public void showDialog_yes_no(final String title, final String content,
			final IAction yes) {
		((Activity) c).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) c
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_ketban, null, false);

//				if (d != null)
//					d.dismiss();

				/* final Dialog */d = new Dialog(c,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO Title
				TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
				txtTitle.setText(title.toString());
				// TODO Content
				TextView txtContent = (TextView) v
						.findViewById(R.id.txtContent);
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

//				if (d != null)
//					d.dismiss();

				/* final Dialog */d = new Dialog(c,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO Title
				TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
				txtTitle.setText(title.toString());
				// TODO Content
				TextView txtContent = (TextView) v
						.findViewById(R.id.txtContent);
				txtContent.setText(content.toString());

				Button bt2 = (Button) v.findViewById(R.id.bt_Huy);
				bt2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						d.dismiss();
						// GameCanvas.endDlg();
					}
				});

				d.setContentView(v);
				d.show();
			}
		});
	}

	public void showDialog_DoiDina() {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) c
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_doidina, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(c,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO Edittext "Sá»‘ dina"
				final EditText mEdittext = (EditText) v
						.findViewById(R.id.sodina);

				// TODO button Ä‘á»“ng Ã½
				Button bt1 = (Button) v.findViewById(R.id.bt_DongY);
				bt1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int dina = 0;

						try {
							dina = Integer.parseInt(mEdittext.getText()
									.toString());

							GlobalService.onChangeDinaToGold(dina);

							// GameCanvas.endDlg();
							d.dismiss();
						} catch (Exception ex) {
							// GameCanvas.startOKDlg("KhÃ´ng há»£p lá»‡");
							showDialog_Okie("Thông báo",
									"Không thể đổi dina !!!");
						}
					}
				});
				// TODO button há»§y
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

	int m_positonItem = -1;

	public void showDialog_NapTien(final String title, final Vector mMoneyInfo) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) c
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater
						.inflate(R.layout.popup_napdina_2, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(c,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO title
				TextView m_Title = (TextView) v
						.findViewById(R.id.title_naptien);
				m_Title.setText(title);

				// TODO Seri
				final EditText m_Seri = (EditText) v.findViewById(R.id.seri);
				final TextView txt_Seri = (TextView) v
						.findViewById(R.id.txt_SoSeri);

				// TODO Mã số thẻ
				final EditText m_MaSoThe = (EditText) v
						.findViewById(R.id.masothe);
				final TextView txt_MasoThe = (TextView) v
						.findViewById(R.id.txt_masothe);

				String[] data = new String[mMoneyInfo.size()];
				MoneyInfo m_info;
				for (int i = 0; i < data.length; i++) {
					m_info = (MoneyInfo) mMoneyInfo.elementAt(i);
					data[i] = m_info.info;
				}

				// TODO Spinner
				final Spinner m_Spinner = (Spinner) v
						.findViewById(R.id.spinner1);
				m_Spinner.setAdapter(new MyCustomSpinner(c,
						R.layout.item_spinner, data));

				m_Spinner
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int position, long id) {
								m_positonItem = position;
								if (position == 0 || position == 1) {
									// disable all seri and masothe
									txt_Seri.setVisibility(View.GONE);
									txt_MasoThe.setVisibility(View.GONE);
									m_Seri.setVisibility(View.GONE);
									m_MaSoThe.setVisibility(View.GONE);
								} else {
									// enable all seri and masothe
									txt_Seri.setVisibility(View.VISIBLE);
									txt_MasoThe.setVisibility(View.VISIBLE);
									m_Seri.setVisibility(View.VISIBLE);
									m_MaSoThe.setVisibility(View.VISIBLE);
								}
							}
						});

				// TODO button đồng ý
				Button bt1 = (Button) v.findViewById(R.id.bt_DongY);
				bt1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (m_positonItem == 0 || m_positonItem == 1) {
							showDialog_yes_no("Thông báo", "Bạn có muốn nạp tiền ko ?",
									new IAction() {

										@Override
										public void perform() {
											// TODO Auto-generated method stub
											HDCGameMidlet.sendSMS(((MoneyInfo)mMoneyInfo.elementAt(m_positonItem)).smsContent.toString() + HDCGameMidlet.m_myPlayerInfo.itemName,
													((MoneyInfo)mMoneyInfo.elementAt(m_positonItem)).smsTo.toString());
										}
									});
						} else {
							if (m_Seri.getText().toString().equals("")) {
								// GameCanvas.startOKDlg("Bạn phải nhập số seri.");
								showDialog_Okie("Thông báo",
										"Bạn phải nhập số seri.");
								return;
							}
							if (m_MaSoThe.getText().toString().equals("")) {
								// GameCanvas.startOKDlg("Bạn phải nhập mã số thẻ.");
								showDialog_Okie("Thông báo",
										"Bạn phải nhập mã số thẻ.");
								return;
							}

							GlobalService.doRequestChargeMoneySimCard(
									((MoneyInfo)mMoneyInfo.elementAt(m_positonItem)).smsContent.toString(), m_Seri.getText().toString(),
									m_MaSoThe.getText().toString());
							
							d.dismiss();							
						}
						
						
					}
				});

				// TODO button hủy
				Button bt2 = (Button) v.findViewById(R.id.bt_Huy);
				bt2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(c, "", Toast.LENGTH_SHORT).show();
						d.dismiss();
					}
				});

				d.setContentView(v);
				d.show();
			}
		});
	}

}
