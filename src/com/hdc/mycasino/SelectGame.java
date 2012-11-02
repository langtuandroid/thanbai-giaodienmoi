package com.hdc.mycasino;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.danh.customcontrol.CustomGalary;
import com.danh.standard.Image;
import com.hdc.mycasino.customcontrol.CustomDialog;
import com.hdc.mycasino.model.DataManager;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.network.Session;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.GameResource;

public class SelectGame extends Activity implements OnItemClickListener,
		OnClickListener {

	public static SelectGame instance;

	// TODO galary to select game
	CustomGalary m_CustomGalary;

	// TODO ImageView
	ImageView imgBack;
	ImageView imgAvatar;
	ImageView imgMail;
	ImageView imgGiatoc;
	ImageView imgBanbe;
	ImageView imgMuasam;
	ImageView imgXepHang;
	ImageView imgChoiGame;

	// TODO Textview
	TextView txt_Nick;
	TextView txt_Level;
	public TextView txt_Gold;
	public TextView txt_Dina;
	TextView txt_GiaToc;
	TextView txt_BanBe;
	TextView txt_Muasam;
	TextView txt_XepHang;
	TextView txt_ChoiGame;

	public int flagState = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.select_game);

		instance = this;

		m_CustomGalary = (CustomGalary) findViewById(R.id.galary);
		m_CustomGalary.setAdapter(new CustomGalleryImageAdapter(this));
		m_CustomGalary.setOnItemClickListener(this);

		// TODO init all control
		initAllControl();

		// set context custom dialog
		CustomDialog.instance.gI().setContext(this);
	}

	// TODO init all control in select Game
	private void initAllControl() {
		// TODO image view
		imgBack = (ImageView) findViewById(R.id.img_back);
		imgAvatar = (ImageView) findViewById(R.id.img_avatar);
		imgMail = (ImageView) findViewById(R.id.img_mail);
		imgGiatoc = (ImageView) findViewById(R.id.imgView_Giatoc);
		imgBanbe = (ImageView) findViewById(R.id.imgView_Banbe);
		imgMuasam = (ImageView) findViewById(R.id.imgView_Muasam);
		imgXepHang = (ImageView) findViewById(R.id.imgView_Xephang);
		imgChoiGame = (ImageView) findViewById(R.id.imgView_ChoiGame);

		// TODO text view
		txt_Nick = (TextView) findViewById(R.id.txt_nick);
		txt_Level = (TextView) findViewById(R.id.txt_level);
		txt_Gold = (TextView) findViewById(R.id.txt_gold);
		txt_Dina = (TextView) findViewById(R.id.txt_dina);

		// TODO set on click for all control
		imgBack.setOnClickListener(this);
		imgAvatar.setOnClickListener(this);
		imgMail.setOnClickListener(this);
		imgGiatoc.setOnClickListener(this);
		imgBanbe.setOnClickListener(this);
		imgMuasam.setOnClickListener(this);
		imgXepHang.setOnClickListener(this);
		imgChoiGame.setOnClickListener(this);
		txt_Gold.setOnClickListener(this);
		txt_Dina.setOnClickListener(this);

		// TODO set information for nick,level,gold,dina
		txt_Nick.setText(HDCGameMidlet.m_myPlayerInfo.itemName);
		txt_Level.setText("Level : " + HDCGameMidlet.m_myPlayerInfo.level);
		txt_Gold.setText(HDCGameMidlet.m_myPlayerInfo.gold + "");
		txt_Dina.setText(HDCGameMidlet.m_myPlayerInfo.dina + "");
		imgAvatar.setImageBitmap(Image.createBitmapFromSrc(
				GameResource.instance.imgAvatar, 0,
				HDCGameMidlet.m_myPlayerInfo.avatarId * 64, 64, 64));

	}

	class CustomGalleryImageAdapter extends BaseAdapter {
		private Context mContext;
		private ImageView image;
		private LayoutInflater mInflater;
		private int count;

		private int[] mImageID = { R.drawable.icon_tienlen_mn,
				R.drawable.icon_tienlen_mb, R.drawable.icon_baicao,
				R.drawable.icon_phom, R.drawable.icon_xito,
				R.drawable.icon_gamekhac };

		public CustomGalleryImageAdapter(Context c) {
			mContext = c;
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View mview = convertView;

			if (mview == null) {
				mview = mInflater.inflate(R.layout.item_galary, null);
			}

			image = (ImageView) mview.findViewById(R.id.imageView1);

			image.setBackgroundResource(mImageID[position]);

			return mview;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "" + position, Toast.LENGTH_LONG).show();
	}

	// TODO BACK game
	private void BACK() {
		CustomDialog.instance.showDialog_yes_no("Thông báo",
				"Bạn có muốn thoát khỏi game không ?", new IAction() {
					public void perform() {
						GlobalService.sendMessageLogout();
						Session.gI().close();
						Login.instance.flagState = 0;
						tranfer_Activity();
					}
				});
	}

	// TODO tranfer activity to LOGIN
	private void tranfer_Activity() {
		HDCGameMidlet.instance.flagExit = true;
		// Intent intent = new Intent(SelectGame.this, Login.class);
		// startActivity(intent);
		flagState = 1;
		onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == imgBack) {
			BACK();
		} else if (v == imgAvatar) {
			// xem thông tin cá nhân
		} else if (v == imgMail) {
			// xem email
		} else if (v == txt_Gold) {
			// đổi gold
			CustomDialog.instance.gI().showDialog_DoiDina();
		} else if (v == txt_Dina) {
			// nạp dina
			CustomDialog.instance.showDialog_NapTien("Nạp tiền",
					DataManager.gI().m_vtSMS);
		} else if (v == txt_GiaToc || v == imgGiatoc) {
			// gia tộc
			// ko làm
		} else if (v == txt_BanBe || v == imgBanbe) {
			// bạn bè
		} else if (v == txt_Muasam || v == imgMuasam) {
			// mua sắm
		} else if (v == txt_XepHang || v == imgXepHang) {
			// xếp hạng
		} else if (v == txt_ChoiGame || v == imgChoiGame) {
			// chơi game
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (flagState == 0)
			BACK();
		else {
			CustomDialog.instance.setContext(Login.instance);
			super.onBackPressed();
		}
	}

	public void updateInterface() {
		instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				txt_Gold.setText(HDCGameMidlet.m_myPlayerInfo.gold + "");
				txt_Dina.setText(HDCGameMidlet.m_myPlayerInfo.dina + "");
			}
		});
	}

}
