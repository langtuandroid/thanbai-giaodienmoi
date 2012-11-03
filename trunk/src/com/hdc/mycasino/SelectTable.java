package com.hdc.mycasino;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.danh.customcontrol.ListRoomAdapter;
import com.danh.customcontrol.ListTableAdapter;
import com.hdc.mycasino.customcontrol.CustomDialog;
import com.hdc.mycasino.messageHandler.GlobalMsgHandler;
import com.hdc.mycasino.model.BoardInfo;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.RoomInfo;
import com.hdc.mycasino.screen.PlayGameScr;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.GameResource;

public class SelectTable extends Activity implements OnItemClickListener{

	public static SelectTable instance;
	
	private TabHost mTabHost;

	ListTableAdapter listAdapter ;
	
	private void setupTabHost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// construct the tabhost
		setContentView(R.layout.select_table);

		instance = this;
		
		//set context for dialog
		CustomDialog.instance.gI().setContext(this);
		
		setupTabHost();
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		
//		TextView t1 = new TextView(this);
//		t1.setText("dadsa");

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = (View)inflater.inflate(R.layout.tabcontent_1, null, false);
		
		ArrayList<String> aa = new ArrayList<String>();
//		ArrayList<RoomInfo> data = new ArrayList<RoomInfo>();
//		data = SelectGame.instance.m_LstRoomInfo;
//		for(int i = 0 ; i <  data.size(); i++){
//			aa.add("Phòng " + data.get(i).itemName);
//		}
		
		ListView listView = (ListView)v.findViewById(R.id.listView);
		listAdapter = new ListTableAdapter(SelectTable.this, R.layout.item_listview_table, SelectRoom.instance.m_listBoardInfo);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);
		
//		TextView t2 = new TextView(this);
//		t2.setText("dadsa fdfsd sd");
//		TextView t3 = new TextView(this);
//		t3.setText("dadsa f fghhgh");

		setupTab(listView, "TIẾN LÊN MIỀN NAM");
//		setupTab(t2, "Gia tộc");
//		setupTab(t3, "Thần bài");
	}

	private void setupTab(final View view, final String tag) {
		View tabview = createTabView(mTabHost.getContext(), tag);

		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(new TabContentFactory() {
					public View createTabContent(String tag) {
						return view;
					}
				});
		mTabHost.addTab(setContent);

	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		CustomDialog.instance.setContext(SelectRoom.instance);
		super.onBackPressed();
	}
	
	public class UpdateListView extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
//			Looper.prepare();
			super.onPreExecute();
			
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			update_BanChoi();
//			CustomDialog.instance.gI().endDialog();
			Looper.getMainLooper().quit();
		}
	}
	
	public void update(){
//		Looper.prepare();
//		new UpdateListView().execute();
//		CustomDialog.instance.gI().endDialog();
//		Looper.loop();
//		Looper.getMainLooper().quit();
	}
	
	public void doJoinBoard(int position) {
		BoardInfo selectedBoard = (BoardInfo) SelectRoom.instance.m_listBoardInfo.get(position);

		boolean isFull = selectedBoard.isFull();
		if (isFull) {
			// GameCanvas.startOKDlg("Bàn đã đầy");
			CustomDialog.instance.gI().showDialog_Okie("Thông báo", "Bàn đã đầy");

		}
		if (selectedBoard.betGold > HDCGameMidlet.m_myPlayerInfo.gold) {
			// GameCanvas.startOKDlg("Bạn không đủ gold để vào bàn!");
			CustomDialog.instance.gI().showDialog_Okie("Thông báo", "Bạn không đủ gold để vào bàn!");
			return;
		}
		if (selectedBoard.isLock) {
//			GameCanvas.inputDlg.setInfo(GameResource.password, new IAction() {
//				public void perform() {
//					BoardInfo selectedBoard = (BoardInfo) m_arrItems.elementAt(m_selected);
//					GlobalService.sendMessageToJoinTable(selectedBoard.itemId, GameCanvas.inputDlg.tfInput.getText(),
//							false);
//					GameCanvas.startWaitDlg();
//					GameCanvas.endDlg();
//				}
//			}, 0);
//			GameCanvas.inputDlg.show();
		} else {
			GlobalService.sendMessageToJoinTable(selectedBoard.itemId, "", false);
			PlayGameScr.boardId = selectedBoard.itemId;
			GlobalMsgHandler.gI().curScr.m_strTitle = (" - Bàn " + PlayGameScr.boardId);
//			GameCanvas.startWaitDlg();
			CustomDialog.instance.gI().showDialog_Waitting("Xin chờ ...");
		}
	}
	
	
	public void update_BanChoi() {
		instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

//				Looper.prepare();
				
				if (SelectRoom.instance.m_listBoardInfo != null) {
					ArrayList<BoardInfo> aa = new ArrayList<BoardInfo>();
					aa = SelectRoom.instance.m_listBoardInfo;
					
					listAdapter.clear();
					
					for (int i = 0; i < aa.size(); i++) {
						listAdapter.add(aa.get(i));
					}
					listAdapter.notifyDataSetChanged();
					
				} 
				
//				Looper.loop();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		doJoinBoard(position);
	}	
}
