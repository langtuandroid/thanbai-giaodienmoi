package com.hdc.mycasino;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sound;
import com.hdc.mycasino.customcontrol.CustomGalary;
import com.hdc.mycasino.customcontrol.CustomGalleryImageAdapter;
import com.hdc.mycasino.gif.GifView;
import com.hdc.mycasino.messageHandler.GlobalMsgHandler;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.MailInfo;
import com.hdc.mycasino.model.PlayerInfo;
import com.hdc.mycasino.network.Session;
import com.hdc.mycasino.screen.LoginScr;
import com.hdc.mycasino.screen.MenuNhanh;
import com.hdc.mycasino.screen.XiToScr;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.FileManager;
import com.hdc.mycasino.utilities.GameResource;

public class HDCGameMidlet extends Activity {

	public void doConnect() {
		Session.gI().connect(IP, PORT);
	}

	public static int PROVIDER_ID = 25;
	// public static String IP = "localhost";
	// public static String IP = "192.168.1.10";

	// TODO server test
//	public static String IP = "210.211.97.9";
//	public static int PORT = 3210;

	// local
//	 public static String IP = "192.168.1.243";
//	 public static int PORT = 3210;

	// TODO server that
	public static String IP = "210.211.97.6";
	public static int PORT = 5001;

	public static String version = "1.0.3";
	public static String REFCODE = "";

	public static String Key = "12345678901234567890123456789012";

	public static GameCanvas gameCanvas;
	public static HDCGameMidlet instance;
	public static PlayerInfo m_myPlayerInfo;
	public static String m_strLinkUpdateVersion = "http://thegioigame.mobi";

	// public static boolean isLandscape;
	public static int widthScreen, heightScreen;
	public static boolean isTurnOnOffSound = true;

	// AssetManager
	public static AssetManager assets;
	// check connect internet
	public static boolean isConnect;
	// modify
	public static int width;
	public static int height;
	// flag time
	public int isNight = 0;
	// flagdevice = 0 : 480x800 tro len
	// flagdevice = 1 : 480x800 tro xuong
	public int flagDevice = 0;
	public static Sound sound;

	public static String m_strForumLink = null;

	Graphics graphics;
	WakeLock wakeLock;
	public static float scale = 1;
	// TODO chế độ rung
	public Vibrator m_viberator;
	// TODO gift cho game
	public static GifView gifView;

	public static void setGift(int index,int x,int y) {
		gifView = null;
		gifView = new GifView(GameResource.instance.inputEmotion[index]);
		gifView.setX(x);
		gifView.setY(y);
//		gifView.play();		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {

			super.onCreate(savedInstanceState);

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			instance = this;
			assets = getAssets();
			width = getWindowManager().getDefaultDisplay().getWidth();
			height = getWindowManager().getDefaultDisplay().getHeight();
			Toast(width + " - " + height);

			if (width < height) {
				int tmp = height;
				height = width;
				width = tmp;
			}

			widthScreen = width;
			heightScreen = height;
			// if (width < 800 && height < 480) {
			// flagDevice = 1;
			// scale = 2;
			// }
			// else if (width < 800 && height <= 480) {
			// flagDevice = 1;
			// scale = (float) 3 / 2;
			// }

			if (width == 480 && height == 320) {
				scale = (float) 3 / 2;
			} else if (width == 320 && height == 240) {
				scale = (float) 2;
			} else if (width < 800 && height <= 480) {
				scale = (float) 5 / 4;
			}

			// Date mDate = new Date();
			// if (mDate.getHours() > 17)
			// isNight = 1;// dem
			// else
			// isNight = 0;// ngay

			// isLandscape = getResources().getConfiguration().orientation ==
			// Configuration.ORIENTATION_SQUARE;
			// turn on sound
			isTurnOnOffSound = true;

			gameCanvas = new GameCanvas(this);
			gameCanvas.start();
			gameCanvas.framebuffer = Image.createImage(width, height);
			graphics = new Graphics(gameCanvas.framebuffer);
			sound = new Sound(this);

			getStartScreen();
			setContentView(gameCanvas);

			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connMgr.getActiveNetworkInfo() != null
					&& connMgr.getActiveNetworkInfo().isAvailable()
					&& connMgr.getActiveNetworkInfo().isConnected()) {
				isConnect = true;

			} else {
				Toast.makeText(this, "Bạn vui lòng kiểm tra \n kết nối Internet !!!",
						Toast.LENGTH_LONG).show();
				isConnect = false;
			}

			PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GLGame");

			// Init viberator
			m_viberator = (Vibrator) instance.getSystemService(Context.VIBRATOR_SERVICE);

			// showDialogReSult();
			// showDialog_yes_no();
			// showDialog_NapTien();
			// showDialog_DoiDina();
			// showDialog_DoiMatKhau();
			// showDialog_Okie("fdsf","fdfd");
			// showDialog_Waitting();
			// showDialog_GuiTinNhan("Tin nhắn từ Danh",
			// "s dfs df sdf sdf sd fsdf ",);
//			showDialog_Chat_Emoticion("Chat trong game", "fsdfsd");
			// showDialog_ChonGioiTinh();
		} catch (Exception e) {
			// Log.i("Exception", e.toString());
			e.printStackTrace();
		}
	}

	// public void CustomDialog(){
	// //alert dialog
	// HDCGameMidlet.instance.runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// LayoutInflater inflater =
	// (LayoutInflater)instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View customView = inflater.inflate(R.layout.news, null,false);
	// initListView();
	// AlertDialog.Builder builder = new AlertDialog.Builder(instance);
	// builder.setView(customView);
	// AlertDialog dialog = builder.create();
	// dialog.show();
	// }
	// });
	//
	// }

	public void setInfoDialog(View v, int id, String info, boolean flagColor) {
		// TODO row_1
		TextView name_1 = (TextView) v.findViewById(id);
		name_1.setText(info);
		if (flagColor)
			name_1.setTextColor(Color.parseColor("#ffb901"));
	}

	// Thông báo bằng Toast của Android
	public void showDialogReSult(final String[] name, final String[] rank, final String[] money) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.result, null, false);
				// Dialog dialog = new Dialog(instance);
				// dialog.setContentView(v);
				// dialog.show();

				int[] m_idName = { R.id.name_1, R.id.name_2, R.id.name_3, R.id.name_4 };
				int[] m_idRank = { R.id.rank_1, R.id.rank_2, R.id.rank_3, R.id.rank_4 };
				int[] m_idMoney = { R.id.money_1, R.id.money_2, R.id.money_3, R.id.money_4 };

				boolean flagColor = false;
				for (int i = 0; i < name.length; i++) {
					if (m_myPlayerInfo.itemName.trim().equals(name[i])) {
						flagColor = true;
					} else {
						flagColor = false;
					}

					setInfoDialog(v, m_idName[i], name[i], flagColor);
					setInfoDialog(v, m_idRank[i], rank[i], flagColor);
					setInfoDialog(v, m_idMoney[i], money[i], flagColor);

				}

				final Dialog d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(v);

				// final AlertDialog.Builder builder = new AlertDialog.Builder(
				// instance);
				// builder.setView(v);
				//
				// final AlertDialog alert = builder.create();

				Button btXacNhan = (Button) v.findViewById(R.id.xac_nhan);
				btXacNhan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// ((Dialog) alert).dismiss();
						d.dismiss();
					}
				});

				// alert.show();
				d.show();

			}
		});
	}

	// // Thông báo bằng Toast của Android
	// public void showDialog_NapTien() {
	// HDCGameMidlet.instance.runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// LayoutInflater inflater = (LayoutInflater) instance
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View v = inflater.inflate(R.layout.popup_napdina, null, false);
	//
	// Button bt1 = (Button)v.findViewById(R.id.bt_DongY);
	// bt1.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// Button bt2 = (Button)v.findViewById(R.id.bt_Huy);
	// bt2.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	//
	//
	// final Dialog d = new Dialog(instance,
	// android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	// d.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// d.setContentView(v);
	// d.show();
	// }
	// });
	// }

	Dialog d;

	public void showDialog_NapTien(final String title, final String smsContent) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_napdina_1, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO title
				TextView m_Title = (TextView) v.findViewById(R.id.title_naptien);
				m_Title.setText(title);
				// TODO Seri
				final EditText m_Seri = (EditText) v.findViewById(R.id.seri);
				// TODO Mã số thẻ
				final EditText m_MaSoThe = (EditText) v.findViewById(R.id.masothe);

				// TODO button đồng ý
				Button bt1 = (Button) v.findViewById(R.id.bt_DongY);
				bt1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (m_Seri.getText().toString().equals("")) {
							// GameCanvas.startOKDlg("Bạn phải nhập số seri.");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo",
									"Bạn phải nhập số seri.");
							return;
						}
						if (m_MaSoThe.getText().toString().equals("")) {
							// GameCanvas.startOKDlg("Bạn phải nhập mã số thẻ.");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo",
									"Bạn phải nhập mã số thẻ.");
							return;
						}

						GlobalService.doRequestChargeMoneySimCard(smsContent.toString(), m_Seri
								.getText().toString(), m_MaSoThe.getText().toString());

						d.dismiss();
					}
				});

				// TODO button hủy
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

	RadioButton r;
	public int idx_Gender = 0;
	public int flag_Gender = 0;
	public void showDialog_ChonGioiTinh(/*final int idx*/) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_chon_gioitinh, null, false);

				flag_Gender = 0;
				
				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// select giới tính
				final RadioGroup r1 = (RadioGroup) v.findViewById(R.id.radio_group1);
//				r1.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v1) {
//						// TODO Auto-generated method stub
//						/* RadioButton */
//						r = (RadioButton) v1;
//						// Toast.makeText(instance, r.getText() + "fd",
//						// Toast.LENGTH_LONG).show();
//					}
//				});
				
				final RadioButton m_r1 = (RadioButton)v.findViewById(R.id.radioButton1);
				m_r1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(isChecked){
//							Toast.makeText(instance, "true - r1", Toast.LENGTH_LONG).show();
							idx_Gender = 0;
						}
					}
				});


				final RadioButton m_r2 = (RadioButton)v.findViewById(R.id.radioButton2);
				m_r2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(isChecked){
//							Toast.makeText(instance, "true - r2", Toast.LENGTH_LONG).show();
							idx_Gender = 1;
						}
					}
				});
				
				final RadioButton m_r3 = (RadioButton)v.findViewById(R.id.radioButton3);
				m_r3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(isChecked){
//							Toast.makeText(instance, "true - r3", Toast.LENGTH_LONG).show();
							idx_Gender = 2;
						}
					}
				});
				
				final RadioButton m_r4 = (RadioButton)v.findViewById(R.id.radioButton4);
				m_r4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(isChecked){
//							Toast.makeText(instance, "true - r4", Toast.LENGTH_LONG).show();
							idx_Gender = 3;
						}
					}
				});	
				
				final RadioButton m_r5 = (RadioButton)v.findViewById(R.id.radioButton5);
				m_r5.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(isChecked){
//							Toast.makeText(instance, "true - r5", Toast.LENGTH_LONG).show();
							idx_Gender = 4;
						}
					}
				});					

				// TODO button đồng ý
				Button bt1 = (Button) v.findViewById(R.id.bt_DongY);
				bt1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						Toast.makeText(instance,idx_Gender + " ", Toast.LENGTH_LONG).show();
//						idx_Gender = idx;
						flag_Gender = 1;
						d.dismiss();
					}
				});

				// TODO button hủy
				Button bt2 = (Button) v.findViewById(R.id.bt_Huy);
				bt2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						idx_Gender = idx;
						flag_Gender = 0;
						d.dismiss();
					}
				});

				d.setContentView(v);
				d.show();
			}
		});
	}

	// Thông báo bằng Toast của Android
	public void showDialog_Okie(final String title, final String content) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_okie, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
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
						GameCanvas.endDlg();
					}
				});

				d.setContentView(v);
				d.show();
			}
		});
	}

	public void showDialog_GuiTinNhan(final String title, final String content, final MailInfo p) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_traloi_tinnhan, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO Title
				TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
				txtTitle.setText(title.toString());
				// TODO Content
				TextView txtContent = (TextView) v.findViewById(R.id.txtContent);
				txtContent.setText(content.toString());

				// TODO Chat
				final EditText chat = (EditText) v.findViewById(R.id.chat);

				// TODO button close
				Button bt_Close = (Button) v.findViewById(R.id.bt_close);
				bt_Close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						d.dismiss();
					}
				});

				Button bt2 = (Button) v.findViewById(R.id.bt_Huy);
				bt2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						final String nick = p.sender;

						if (chat.getText().toString().length() > 0) {
							GameCanvas.startWaitDlg();
							GlobalService.onSendMessageToUser(
									HDCGameMidlet.m_myPlayerInfo.itemName, nick, chat.getText()
											.toString());
						} else {
							HDCGameMidlet.instance.showDialog_Okie("Thông báo",
									"Nhập văn bản cần gủi!");
						}

						d.dismiss();
						GameCanvas.endDlg();
					}
				});

				d.setContentView(v);
				d.show();
			}
		});
	}

	public String content_Chat = "";
	public void showDialog_Chat_Emoticion(final String title, final String content,final int x,final int y) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_chat_emoticion, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO Title
				TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
				txtTitle.setText(title.toString());
				// TODO Content
				// TextView txtContent = (TextView)
				// v.findViewById(R.id.txtContent);
				// txtContent.setText(content.toString());

				CustomGalary m_CustomGalary;
				m_CustomGalary = (CustomGalary) v.findViewById(R.id.galary);
				m_CustomGalary.setAdapter(new CustomGalleryImageAdapter(instance));
				m_CustomGalary.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						// TODO Auto-generated method stub
						// Toast.makeText(instance, "" + position,
						// Toast.LENGTH_LONG).show();
						content_Chat = GameResource.instance.list_symbols[position];
						setGift(position,x,y);
						d.dismiss();
					}
				});

				// TODO Chat
				final EditText chat = (EditText) v.findViewById(R.id.chat);

				// TODO button close
				Button bt_Close = (Button) v.findViewById(R.id.bt_close);
				bt_Close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						d.dismiss();
					}
				});

				Button bt2 = (Button) v.findViewById(R.id.bt_Gui);
				bt2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						content_Chat = chat.getText().toString();
						d.dismiss();

					}
				});

				d.setContentView(v);
				d.show();
			}
		});
	}

	public void showDialog_Okie_withCommand(final String title, final String content,
			final IAction okie) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_matketnoi, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
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
						okie.perform();
						d.dismiss();
					}
				});

				d.setContentView(v);
				d.show();
			}
		});
	}

	// Thông báo bằng Toast của Android
	public void showDialog_yes_no(final String title, final String content, final IAction yes) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_ketban, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
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

	// Thông báo bằng Toast của Android
	public void showDialog_ketban(final String title, final String content, final IAction yes,
			final IAction no) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_ketban, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
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
						no.perform();
						d.dismiss();
					}
				});

				d.setContentView(v);
				d.show();
			}
		});
	}

	// Thông báo bằng Toast của Android
	public void showDialog_DoiDina() {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_doidina, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO Edittext "Số dina"
				final EditText mEdittext = (EditText) v.findViewById(R.id.sodina);

				// TODO button đồng ý
				Button bt1 = (Button) v.findViewById(R.id.bt_DongY);
				bt1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int dina = 0;

						try {
							dina = Integer.parseInt(mEdittext.getText().toString());

							GlobalService.onChangeDinaToGold(dina);

							// GameCanvas.endDlg();
							d.dismiss();
						} catch (Exception ex) {
							// GameCanvas.startOKDlg("Không hợp lệ");
							showDialog_Okie("Thông báo", "Không thể đổi số dina này !!!");
						}
					}
				});
				// TODO button hủy
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

	public void showDialog_ChuyenGold(final String title,final IAction action) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_chuyengold, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				TextView m_title = (TextView)v.findViewById(R.id.title);
				m_title.setText(title);
				
				// TODO Edittext "Số dina"
				final EditText mEdittext = (EditText) v.findViewById(R.id.sodina);

				// TODO button đồng ý
				Button bt1 = (Button) v.findViewById(R.id.bt_DongY);
				bt1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						action.perform();
						d.dismiss();
					}
				});
				// TODO button hủy
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
	
	public void showDialog_DatTienCuoc() {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_dattiencuoc, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO Edittext "Số dina"
				final EditText mEdittext = (EditText) v.findViewById(R.id.tiencuoc);

				// TODO button đồng ý
				Button bt1 = (Button) v.findViewById(R.id.bt_DongY);
				bt1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						long gold;
						try {
							gold = Long.parseLong(mEdittext.getText().toString().trim());
						} catch (Exception ex) {
							// GameCanvas.startOKDlg(GameResource.invalid);
							HDCGameMidlet.instance.showDialog_Okie("Thông báo",
									GameResource.invalid);
							return;
						}
						if (gold >= 0)
							GlobalService.sendMessageSetBetGoldForTable(gold);
						else
							// GameCanvas.startOKDlg(GameResource.moneyInvalid);
							HDCGameMidlet.instance.showDialog_Okie("Thông báo",
									GameResource.moneyInvalid);
						d.dismiss();
					}
				});
				// TODO button hủy
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

	public void showDialog_DatMatKhau(final int boardId) {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_datmatkhau, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO Edittext "Số dina"
				final EditText mEdittext = (EditText) v.findViewById(R.id.matkhau);

				// TODO button đồng ý
				Button bt1 = (Button) v.findViewById(R.id.bt_DongY);
				bt1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (mEdittext.getText().toString().trim() != "") {
							GlobalService.onSetPasswordForTable(boardId, mEdittext.getText()
									.toString().trim());
						}
						d.dismiss();
					}
				});
				// TODO button hủy
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

	// Thông báo bằng Toast của Android
	public void showDialog_DoiMatKhau() {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) instance
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.popup_doimatkhau, null, false);

				if (d != null)
					d.dismiss();

				/* final Dialog */d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				// TODO EditText Mât khẩu cũ
				final EditText m_MatKhauCu = (EditText) v.findViewById(R.id.matkhau_cu);
				// TODO EditText Mât khẩu mới
				final EditText m_MatKhauMoi = (EditText) v.findViewById(R.id.matkhau_moi);
				// TODO EditText Mât khẩu nhập lại
				final EditText m_MatKhauNhapLai = (EditText) v.findViewById(R.id.nhapmatkhau_moi);

				// TODO Đồng ý
				Button bt1 = (Button) v.findViewById(R.id.bt_DongY);
				bt1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (m_MatKhauMoi.getText().toString().length() < 5) {
							HDCGameMidlet.instance.showDialog_Okie("Thông báo",
									"Mật khẩu phải nhiều hơn 5 kí tự");
							return;
						}
						if (!m_MatKhauMoi.getText().equals(m_MatKhauNhapLai.getText())) {
							// GameCanvas.startOKDlg("Mật khẩu nhập lại không khớp");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo",
									"Mật khẩu nhập lại không khớp");
							return;
						}

						// GameCanvas
						// .startOKDlg(
						// "Bạn chắc chắn muốn thay đổi mật khẩu ?. Một tin nhắn kết quả sẽ trả về cho bạn.",
						// new IAction() {
						// public void perform() {
						// GlobalService.doRequestChangePass(
						// m_MatKhauCu.getText().toString(),
						// m_MatKhauMoi.getText().toString());
						// InputScr.gI().close();
						// GameCanvas.startWaitDlg();
						// }
						// });

						HDCGameMidlet.instance
								.showDialog_yes_no(
										"Thông báo",
										"Bạn chắc chắn muốn thay đổi mật khẩu ?. Một tin nhắn kết quả sẽ trả về cho bạn.",
										new IAction() {
											public void perform() {
												GlobalService.doRequestChangePass(m_MatKhauCu
														.getText().toString(), m_MatKhauMoi
														.getText().toString());
												// InputScr.gI().close();
												d.dismiss();
												GameCanvas.startWaitDlg();
											}
										});
					}
				});

				// TODO Hủy
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

	// Thông báo bằng Toast của Android
	public void Toast(final String msg) {
//		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
//			}
//		});
	}

	public static void sendSMS(String data, String to) {
		sendSMS(data, to, new IAction() {

			public void perform() {

				// GameCanvas.startOKDlg(GameResource.smsSent);
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", GameResource.smsSent);
			}
		}, new IAction() {

			public void perform() {

				// GameCanvas.startOKDlg(GameResource.sendSMSFail);
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", GameResource.sendSMSFail);
			}
		});
	}

	public static void sendSMS(final String data, final String to, final IAction successAction,
			final IAction failAction) {
		final String address = to;

		new Thread(new Runnable() {
			public void run() {
				try {
					GameCanvas.startWaitDlg(GameResource.plzWait);

					String SENT = "SMS_SENT";
					String DELIVERED = "SMS_DELIVERED";
					PendingIntent sentPI = PendingIntent.getBroadcast(instance, 0,
							new Intent(SENT), 0);
					PendingIntent deliveredPI = PendingIntent.getBroadcast(instance, 0, new Intent(
							DELIVERED), 0);

					SmsManager sms = SmsManager.getDefault();
					// Log.e("SkyGardenGame", data + " --->>> " + address);
					instance.registerReceiver(new BroadcastReceiver() {
						@Override
						public void onReceive(Context arg0, Intent arg1) {
							// TODO Auto-generated method stub+
							switch (getResultCode()) {
							case Activity.RESULT_OK:
								// Toast.makeText(game.getBaseContext(),
								// "SMS sent", Toast.LENGTH_SHORT).show();
								successAction.perform();
								break;
							case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
								// Toast.makeText(game.getBaseContext(),
								// "Generic failure",
								// Toast.LENGTH_SHORT).show();
								failAction.perform();
								break;
							case SmsManager.RESULT_ERROR_NO_SERVICE:
								// Toast.makeText(game.getBaseContext(),
								// "No service", Toast.LENGTH_SHORT).show();
								failAction.perform();
								break;
							case SmsManager.RESULT_ERROR_NULL_PDU:
								// Toast.makeText(game.getBaseContext(),
								// "Null PDU", Toast.LENGTH_SHORT).show();
								failAction.perform();
								break;
							case SmsManager.RESULT_ERROR_RADIO_OFF:
								// Toast.makeText(game.getBaseContext(),
								// "Radio off", Toast.LENGTH_SHORT).show();
								failAction.perform();
								break;
							}
						}
					}, new IntentFilter(SENT));

					sms.sendTextMessage(address, null, data, sentPI, deliveredPI);

				} catch (Exception e) {
					e.printStackTrace();
					failAction.perform();
				}
			}
		}).start();
	}

	public void getStartScreen() {
		// read file from drawable
		ArrayList<String> aa = new ArrayList<String>();
		aa = FileManager.loadfileExternalStorage(this, R.drawable.provider);
		String[] temp;
		try {
			// provider id
			temp = aa.get(0).split("PROVIDER_ID");
			PROVIDER_ID = (byte) Integer.parseInt(temp[1].trim().toString());
			// link
			temp = aa.get(1).split("LINK");
			m_strLinkUpdateVersion = temp[1].trim().toString();

			// ref code
			temp = aa.get(2).split("REF_CODE");

			if (temp.length == 0) {
				REFCODE = "";
			} else {
				REFCODE = temp[1].trim().toString();
			}
		} catch (Exception e) {
		}

		// lưu file text để check image trong internal storage
		// if (!FileManager.fileTxtIsExits()) {
		// for (int i = 0; i < GameResource.instance.imgHD.length; i++) {
		// int version = FileManager.loadFile("server/hd" + i + ".png").length %
		// Short.MAX_VALUE;
		// // save file txt include in
		// // : id and version
		// FileManager.saveFileText((short) i, (short) version, "hd" + i +
		// ".txt");
		// }
		// }

		Session.gI().setHandler(GlobalMsgHandler.gI());

		GameCanvas.instance.initGameCanvas();

		// display login screen
		GameCanvas.loginScr.switchToMe();

	}

	public static void destroy() {
		CRes.removeSetting1();
		instance.finish();
		// System.gc();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		wakeLock.acquire();
		gameCanvas.resume();
		if (isTurnOnOffSound) {
			// this.mSound.openFile(midlet, R.raw.click);
			// gameCanvas.mSound.openFile(this, R.raw.mbackground);
			// gameCanvas.mSound.play();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		wakeLock.release();
		gameCanvas.pause();
		// gameCanvas.mSound.pause();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		Log.e("onLowMemory", "onLowMemory");
		super.onLowMemory();

	}

	// public static ListView listView;
	// private static ArrayList<Item> arrayitems = new ArrayList<Item>();
	// private static ListRecordAdapter listrecordarray;
	// private static ListView listItems;

	// // init ListView
	// public void initListView() {
	// // arrayitems = ConnectServer.instance.m_ListItem;
	// Item mItem;
	// for(int i = 0 ; i < 10;i++){
	// mItem = new Item();
	// mItem.setTitle("a " + i);
	// arrayitems.add(mItem);
	// }
	//
	// listrecordarray = new ListRecordAdapter(this, R.layout.items,
	// arrayitems, "http://vnexpress.net/");
	// listItems = (ListView) findViewById(R.id.listItems);
	// listItems.setAdapter(listrecordarray);
	// listItems.setTextFilterEnabled(true);
	// listItems.setFocusableInTouchMode(false);
	// listItems.setClickable(true);
	// // on click listview Item
	// listItems.setOnItemClickListener(new OnItemClickListener() {
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View v, int position,
	// long id) {
	//
	// // final int m_position = position;
	// //
	// // AlertDialog.Builder builder = new AlertDialog.Builder(
	// // MyListActivity.this);
	// // builder.setMessage(
	// // "Bạn có muốn nhắn tin \n để tải hình ảnh về không ?")
	// // .setCancelable(false)
	// // .setPositiveButton("Yes",
	// // new DialogInterface.OnClickListener() {
	// // public void onClick(DialogInterface dialog,
	// // int id) {
	// //
	// // String sms = ConnectServer.instance.m_Sms
	// // .getSyntax()
	// // + ConnectServer.SPACE
	// // + listrecordarray.getItems(
	// // m_position).getId()
	// // + ConnectServer.SPACE
	// // + ConnectServer.m_UserID
	// // + ConnectServer.SPACE
	// // + ConnectServer.instance.REF_CODE;
	// // // gửi tin nhắn
	// // sendSMS(sms,
	// // ConnectServer.instance.m_Sms
	// // .getNumber());
	// // }
	// // })
	// // .setNegativeButton("No",
	// // new DialogInterface.OnClickListener() {
	// // public void onClick(DialogInterface dialog,
	// // int id) {
	// // dialog.cancel();
	// // }
	// // });
	// // AlertDialog alert = builder.create();
	// // alert.show();
	// }
	// });
	//
	// }

	Dialog customDialog;

	public void endDialog() {
		if (customDialog != null)
			customDialog.dismiss();
	}

	// TODO dialog waiting
	public void showDialog_Waitting(String title) {
		customDialog = new Dialog(instance, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) instance
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.waitting_1, null, false);

		// TODO title
		TextView m_tittle = (TextView) layout.findViewById(R.id.txtTitle);
		m_tittle.setText(title);

		customDialog.setContentView(layout);
		customDialog.show();
	}

	// TODO show dialog
	public void showDialog() {
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(instance);
				builder.setMessage("Are you sure you want to exit?").setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								instance.finish();
							}
						}).setNegativeButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	// TODO show dialog "Tố tiền đặt cược"
	long money = 0;

	public void showDialog_ToTienCuoc(final long maxMoney, final long betMoney) {

		HDCGameMidlet.instance.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final Context context = HDCGameMidlet.instance;
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(com.hdc.mycasino.R.layout.popup,
						(ViewGroup) ((Activity) context)
								.findViewById(com.hdc.mycasino.R.id.viewgroup));

				final Dialog d = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(layout);
				// d.show();

				// TODO set max money
				TextView mMaxMoney = (TextView) layout.findViewById(R.id.txt_maxmoney);
				try {
					mMaxMoney.setText("$" + Long.toString(maxMoney));
				} catch (Exception e) {
					e.printStackTrace();
				}

				// TODO money
				final EditText mMoney = (EditText) layout.findViewById(R.id.editText1);
				mMoney.setText(Long.toString(betMoney));

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setView(layout);
				final AlertDialog alert = builder.create();

				// TODO button
				final Button mButton = (Button) layout.findViewById(R.id.button1);
				mButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						money = Long.parseLong(mMoney.getText().toString());
						if (XiToScr.instance.nTurn < 4) {
							GlobalService.sendMessageTo((long) money);
						} else {
							GlobalService.sendMessageToThem((long) money);
						}
						// ((Dialog) alert).cancel();
						d.dismiss();
					}
				});

				// alert.show();
				d.show();

				SeekBar mSeekBar = (SeekBar) layout.findViewById(com.hdc.mycasino.R.id.seekBar1);
				mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar arg0, int sum, boolean arg2) {
						// TODO Auto-generated method stub
						money = (long) (sum * maxMoney) / 100;
						mMoney.setText(Long.toString(money));
					}
				});
			}
		});
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		if (gameCanvas.currentScreen != null && !(gameCanvas.currentScreen instanceof LoginScr)) {
			gameCanvas.currentScreen.actionMenu();
		}
		return false;
	}

	@SuppressWarnings("static-access")
	@Override
	public void onBackPressed() {
		// TODO đóng menu
		if (gameCanvas.menu.m_showMenu) {
			gameCanvas.menu.m_showMenu = false;
			return;
		}
		if (MenuNhanh.showMenuNhanh) {
			MenuNhanh.closeMenuNhanh();
			return;
		}

		if (GameCanvas.instance.currentScreen != null) {
			GameCanvas.instance.currentScreen.doBack();
			return;
		}

		// showDialog();

	}
}
