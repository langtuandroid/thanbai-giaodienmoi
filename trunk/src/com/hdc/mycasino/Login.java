package com.hdc.mycasino;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
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

import com.hdc.mycasino.customcontrol.CustomDialog;
import com.hdc.mycasino.messageHandler.GlobalMsgHandler;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.network.Session;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.FileManager;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.TField;

public class Login extends Activity implements OnClickListener {
	// TODO LOCAL VARIABLES
	public static Login instance;
	// TODO server that
	// public static String IP = "210.211.97.6";
	// public static int PORT = 5001;
	
	 public static String IP = "210.211.97.9";
	 public static int PORT = 3210;
	
	
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
	Button bt_ReDangKy;
	Button bt_QuenMK;

	// TODO Check box
	CheckBox check_NhoMK;

	// TODO Edit Text
	EditText edit_UserName;
	EditText edit_Pass;
	EditText edit_RePass;

	//TODO flag state : 
	//Đăng nhập : 0,đăng ký : 1,quên mật khẩu : 2,đăng nhập thành công : 3
	public int flagState = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		
		instance = this;

		// TODO init all control
		initAllControls();
		
		//TODO load user and pass
		loadUser_Pass();

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
		bt_ReDangKy = (Button) findViewById(R.id.bt_ReDangKy);
		bt_ReDangKy.setOnClickListener(this);
		bt_QuenMK = (Button) findViewById(R.id.bt_QuenMatKhau);
		bt_QuenMK.setOnClickListener(this);

		// TODO Edit Text
		edit_UserName = (EditText) findViewById(R.id.txtUserName);
		edit_Pass = (EditText) findViewById(R.id.txtPass);
		edit_RePass = (EditText)findViewById(R.id.txtRePass);
		
		//TODO Check box 'Nhớ mật khẩu'
		check_NhoMK = (CheckBox)findViewById(R.id.nho_mat_khau);

	}
	
	// TODO save pass
	public void savePass() {
		if (check_NhoMK.isChecked()) {
			FileManager.saveUserAndPass(0,edit_UserName.getText().toString(),
					edit_Pass.getText().toString(), "user.txt");
		} else {
			FileManager.saveUserAndPass(1, "", "", "user.txt");
		}
	}
	
	// TODO loadUser & Pass
	private void loadUser_Pass() {
		// save status
		String[] mString = FileManager.loadUserAndPass("user.txt");
		int a = Integer.parseInt(mString[0]);
		if (a == 0) {
			check_NhoMK.setChecked(true);
		} else {
			check_NhoMK.setChecked(false);
		}
		// load username and pass
		edit_UserName.setText(mString[1]);
		edit_Pass.setText(mString[2]);
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
			flagState = 2;
			
			edit_Pass.setVisibility(View.GONE);
			edit_RePass.setVisibility(View.GONE);
			check_NhoMK.setVisibility(View.GONE);
			
			bt_QuenMK.setVisibility(View.VISIBLE);
			bt_DangNhap.setVisibility(View.GONE);
			bt_DangKy.setVisibility(View.GONE);
			bt_ReDangKy.setVisibility(View.GONE);
			
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
		}else if(v == bt_DangKy){
			check_NhoMK.setVisibility(CheckBox.GONE);
			edit_RePass.setVisibility(View.VISIBLE);
			bt_DangNhap.setVisibility(View.GONE);
			bt_DangKy.setVisibility(View.GONE);
			bt_ReDangKy.setVisibility(View.VISIBLE);
			//trạng thái Đăng ký
			flagState = 1;
		}else if(v == bt_ReDangKy){
			doRegister();
		}else if(v == bt_QuenMK){
			doResetPass();
		}
	}
	
	//TODO doResetPass
	private void doResetPass() {
		if (edit_UserName.getText().toString().equals("") || edit_UserName.getText().toString().length() == 0) {
			// GameCanvas.startOKDlg(GameResource.plzInputInfo);
			CustomDialog.instance.gI().showDialog_Okie("Thông báo", GameResource.plzInputInfo);
			return;
		}
		GameCanvas.startWaitDlg();
		Key = "123455";
		doConnect();
		if (!Key.equals("")) {
			GlobalService.onSetPass(edit_UserName.getText().toString());
		}
	}
	
	//TODO doRegister
	protected void doRegister() {
		if (edit_UserName.getText().toString().equals("") || edit_Pass.getText().toString().equals("")
				|| edit_RePass.getText().toString().equals("")) {
			// GameCanvas.startOKDlg(GameResource.plzInputInfo);
			CustomDialog.instance.gI().showDialog_Okie("Thông báo", GameResource.plzInputInfo);
			return;
		}
		char[] ch = edit_UserName.getText().toString().toCharArray();
		int a = ch.length;
		for (int i = 0; i < a; i++) {
			if (!TField.setNormal(ch[i])) {
				// GameCanvas.startOKDlg(GameResource.specialCharNotAllow);
				CustomDialog.instance.gI().showDialog_Okie("Thông báo",
						GameResource.specialCharNotAllow);
				return;
			}
		}

		if (!edit_Pass.getText().toString().equals(edit_RePass.getText().toString())) {
			// GameCanvas.startOKDlg(GameResource.incorrectPass);
			CustomDialog.instance.gI().showDialog_Okie("Thông báo", GameResource.incorrectPass);
			return;
		}
		Key = "123455";
		doConnect();
		if (!Key.equals("")) {
			GlobalService.onRegister(edit_UserName.getText().toString(), edit_Pass.getText().toString());
		}
	}	

	// TODO thoát
	private void THOAT_GAME() {
		CustomDialog.instance.gI().showDialog_yes_no("Thông báo", GameResource.doYouWantExit,
				new IAction() {
					public void perform() {
						instance.finish();
//						HDCGameMidlet.instance.onDestroy();
//						ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
//						manager.killBackgroundProcesses("com.hdc.mycasino");
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
		CustomDialog.instance.gI().showDialog_Waitting("Xin chờ ...");

		Key = "123455";
		doConnect();
		if (!Key.equals("")) {
			GlobalService.onLogin(edit_UserName.getText().toString(), edit_Pass.getText()
					.toString());
		}
		savePass();
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		switch (flagState) {
		case 0:
			//đăng nhập : hỏi thoát game
			THOAT_GAME();
			break;
		case 1:
		case 2:
			//đăng ký && quên mật khẩu : back trở về đăng nhập
			flagState = 0;
			//enable UserName and Password
			edit_UserName.setVisibility(View.VISIBLE);
			edit_Pass.setVisibility(View.VISIBLE);
			edit_RePass.setVisibility(View.GONE);
			//enable button DangNhap && DangKy
			bt_DangNhap.setVisibility(View.VISIBLE);
			bt_DangKy.setVisibility(View.VISIBLE);
			bt_ReDangKy.setVisibility(View.GONE);
			bt_QuenMK.setVisibility(View.GONE);
			//enable check box 'Nho Thong tin'
			check_NhoMK.setVisibility(View.VISIBLE);			
			break;
		case 3:
			//đăng nhập thành công
//			super.onBackPressed();			
			break;
		default:
			break;
		}
		
		
		
		

		
		
	}
}
