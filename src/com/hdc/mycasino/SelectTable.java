package com.hdc.mycasino;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.danh.customcontrol.ListRecordAdapter;
import com.hdc.mycasino.customcontrol.CustomDialog;
import com.hdc.mycasino.model.RoomInfo;

public class SelectTable extends Activity{

	private TabHost mTabHost;

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
//		listView.setAdapter(new ListRecordAdapter(SelectTable.this, R.layout.item_listview, SelectGame.instance.m_LstRoomInfo));
		
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
	
}
