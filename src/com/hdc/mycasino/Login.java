package com.hdc.mycasino;

import java.util.ArrayList;

import com.hdc.mycasino.customcontrol.CustomDialog;
import com.hdc.mycasino.messageHandler.GlobalMsgHandler;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.network.Session;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.FileManager;
import com.hdc.mycasino.utilities.GameResource;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Login extends Activity implements OnClickListener {
	// TODO LOCAL VARIABLES
	// TODO server that
	public static String IP = "210.211.97.6";
	public static int PORT = 5001;
	public static String m_strLinkUpdateVersion = "http://thegioigame.mobi";
	public static String version = "1.0.3";
	public static String REFCODE = "";
	public static int PROVIDER_ID = 25;
	public static String Key = "12345678901234567890123456789012";
	// //////////////////////////////

	// TODO ImageView
	ImageView imgView_Thoat;
	ImageView imgView_QuenMK;
	ImageView imgView_GioiThieu;
	ImageView imgView_DienDan;
	ImageView imgView_CapNhat;
	ImageView imgView_XoaDuLieu;

	// TODO TextView
	TextView txt_QuenMK;
	TextView txt_GioiThieu;
	TextView txt_DienDan;
	TextView txt_CapNhat;
	TextView txt_XoaDuLieu;

	// TODO Button
	Button bt_DangNhap;
	Button bt_DangKy;

	// TODO Check box
	CheckBox check_NhoMK;

	// TODO Edit Text
	EditText edit_UserName;
	EditText edit_Pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);

		// TODO init all control
		initAllControls();

		// init dialog
		CustomDialog.instance.gI().setContext(this);

		// read file and init session
		getStartScreen();
	}

	// TODO init all controls
	private void initAllControls() {
		// TODO ImageView
		imgView_Thoat = (ImageView) findViewById(R.id.imgView_ThoatGame);
		imgView_Thoat.setOnClickListener(this);
		imgView_QuenMK = (ImageView) findViewById(R.id.imgView_QuenMK);
		imgView_QuenMK.setOnClickListener(this);
		imgView_GioiThieu = (ImageView) findViewById(R.id.imgView_GioiThieu);
		imgView_GioiThieu.setOnClickListener(this);
		imgView_DienDan = (ImageView) findViewById(R.id.imgView_DienDan);
		imgView_DienDan.setOnClickListener(this);
		imgView_CapNhat = (ImageView) findViewById(R.id.imgView_CapNhat);
		imgView_CapNhat.setOnClickListener(this);
		imgView_XoaDuLieu = (ImageView) findViewById(R.id.imgView_XoaDuLieu);
		imgView_XoaDuLieu.setOnClickListener(this);

		// TODO TextView
		txt_QuenMK = (TextView) findViewById(R.id.txtQuenMK);
		txt_QuenMK.setOnClickListener(this);
		txt_GioiThieu = (TextView) findViewById(R.id.txtGioiThieu);
		txt_GioiThieu.setOnClickListener(this);
		txt_DienDan = (TextView) findViewById(R.id.txt_DienDan);
		txt_DienDan.setOnClickListener(this);
		txt_CapNhat = (TextView) findViewById(R.id.txtCapNhat);
		txt_CapNhat.setOnClickListener(this);
		txt_XoaDuLieu = (TextView) findViewById(R.id.txtXoaDuLieu);
		txt_XoaDuLieu.setOnClickListener(this);

		// TODO Button
		bt_DangNhap = (Button) findViewById(R.id.bt_DangNhap);
		bt_DangNhap.setOnClickListener(this);
		bt_DangKy = (Button) findViewById(R.id.bt_DangKy);
		bt_DangKy.setOnClickListener(this);

		// TODO Edit Text
		edit_UserName = (EditText) findViewById(R.id.txtUserName);
		edit_Pass = (EditText) findViewById(R.id.txtPass);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// TODO thoát game
		if (v == imgView_Thoat) {
			// thực hiện thoát game
			THOAT_GAME();
		} else if (v == imgView_QuenMK || v == txt_QuenMK) {
			// quên mật khẩu
		} else if (v == imgView_GioiThieu || v == txt_GioiThieu) {
			// gioi thieu
			GIOI_THIEU();
		} else if (v == imgView_DienDan || v == txt_DienDan) {
			// dien dan
			DIEN_DAN();
		} else if (v == imgView_CapNhat || v == txt_CapNhat) {
			// cap nhat
			CAP_NHAT();
		} else if (v == imgView_XoaDuLieu || v == txt_XoaDuLieu) {
			// xóa dữ liệu
		} else if (v == bt_DangNhap) {
			doLogin();
		}

	}

	// TODO thoát
	private void THOAT_GAME() {
		CustomDialog.instance.gI().showDialog_yes_no("Thông báo", GameResource.doYouWantExit,
				new IAction() {
					public void perform() {
						finish();
					}
				});
	}

	// TODO gioi thieu
	private void GIOI_THIEU() {
		CustomDialog.instance.gI().showDialog_Okie("Giới thiệu",
				"Vua Bài Version " + HDCGameMidlet.version + "- Công ty HDC\nCSKH : 04.66.844.524");
	}

	// TODO Dien dan
	private void DIEN_DAN() {
		CustomDialog.instance.gI().showDialog_yes_no("Diễn dàn", GameResource.doYouWantGoToForum,
				new IAction() {

					public void perform() {
						try {
							// GameCanvas.endDlg();

							Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
									.parse("http://m.taigamejava.com"));
							startActivity(browserIntent);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	// TODO Cap nhat
	private void CAP_NHAT() {
		CustomDialog.instance.gI().showDialog_yes_no("Cập nhật", GameResource.doYouWantGoToUpdate,
				new IAction() {

					public void perform() {
						try {
							// GameCanvas.endDlg();

							Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
									.parse("http://thegioigame.mobi"));
							startActivity(browserIntent);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	protected void doLogin() {
		if (edit_UserName.getText().toString().length() == 0
				|| edit_Pass.getText().toString().length() == 0) {
			CustomDialog.instance.gI().showDialog_Okie("Thông báo", GameResource.plzInputInfo);
			return;
		}
		// GameCanvas.startWaitDlg();

		Key = "123455";
		doConnect();
		if (!Key.equals("")) {
			GlobalService.onLogin(edit_UserName.getText().toString(), edit_Pass.getText()
					.toString());
		}
		// savePass();
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

		Session.gI().setHandler(GlobalMsgHandler.gI());

		// GameCanvas.instance.initGameCanvas();

		// display login screen
		// GameCanvas.loginScr.switchToMe();

	}

	public void doConnect() {
		Session.gI().connect(IP, PORT);
	}
}
