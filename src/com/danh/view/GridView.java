package com.danh.view;

import java.util.ArrayList;
import java.util.Vector;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.model.BoardInfo;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.ItemInfo;
import com.hdc.mycasino.model.MyObj;
import com.hdc.mycasino.screen.Screen;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

public class GridView {
	private Vector m_listItem;
	private int x, y, width, heigh;
	private int wCell, hCell;
	private int row, col, page;
	private ScrollView m_ScrollView;
	private int row_select, col_select;
	private FrameImage m_frame;
	private int m_selectItem;
	public boolean isUpdate = true;
	private ArrayList<Vector> m_ItemPage;
	private boolean isPaintInfo = false;
	private int isType;
	Vector vt;
	MyObj myObj;
	int pageCurrent = 0;
	int idxInPage = 0;
	private int width_Info, height_Info;
	private Command command;

	// TODO init gridview
	public GridView() {
		// TODO Auto-generated constructor stub
		m_ScrollView = new ScrollView();
	}

	// TODO setinfo
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setInfo(int x, int y, int width, int heigh, int wCell, int hCell, Vector listItem, FrameImage frame) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.heigh = heigh;
		this.wCell = wCell;
		this.hCell = hCell;
		this.m_listItem = listItem;
		this.col = (int) this.width / this.wCell;
		this.row = (int) this.heigh / this.hCell;
		
		//tính lại x cho các items gridview được canh giữa
		this.x = this.x + (this.width - this.col*this.wCell)/2;
		
		int count = m_listItem.size();
		if (count % (row * col) != 0) {
			page = (int) (count / (row * col) + 1);
		} else {
			page = count / (row * col);
		}
		this.m_frame = frame;
		// TODO init scroll
		m_ScrollView.setPage(page);
		m_ScrollView.initScroll(x, y, width, heigh, false, (page - 1) * width);
		if (page == 1)
			isUpdate = false;

		// TODO set list for each page
		m_ItemPage = new ArrayList<Vector>();
		for (int i = 0; i < page; i++) {
			vt = new Vector();
			for (int j = i * row * col; j < m_listItem.size(); j++) {
				vt.addElement((MyObj) m_listItem.elementAt(j));
				if ((j + 1) % (row * col) == 0) {
					m_ItemPage.add(vt);
					break;
				} else if (j == (m_listItem.size() - 1)) {
					m_ItemPage.add(vt);
					break;
				}
			}
		}
		// TODO set index = 0
		m_selectItem = 0;
		myObj = (MyObj) m_ItemPage.get(pageCurrent).elementAt(idxInPage);
	}

	// TODO set command
	public void setCommand(Command cmd) {
		this.command = cmd;
	}

	// TODO select item (avartar - vật phẩm)
	private void selectItem() {
		// if (GameCanvas.isPointer(x, y, width, heigh)) {
		int m_col = (int) (GameCanvas.px - x) / wCell;
		int m_row = (int) (GameCanvas.py - y) / hCell;
		int m_select = m_row * col + m_col + (m_ScrollView.cmX / width * (row * col));

		if (m_select < m_listItem.size()) {
			if (m_select != m_selectItem) {
				m_selectItem = m_select;
				row_select = m_row;
				col_select = m_col;
				pageCurrent = m_selectItem / (row * col);
				idxInPage = m_selectItem % (row * col);
				myObj = (MyObj) m_ItemPage.get(pageCurrent).elementAt(idxInPage);

				// TODO sound
				HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.box_plan);
				HDCGameMidlet.sound.play();
			} else if (command != null) {
				command.action.perform();
			}
		}
		// }
	}

	// TODO get index item when selected
	public int getIndex() {
		// HDCGameMidlet.instance.Toast("index " + m_selectItem);
		return m_selectItem;
	}

	// TODO paint
	public void paint(Graphics g) {
		g.translate(x, 0);
		g.setClip(0, y, width, heigh);
		g.translate(-m_ScrollView.cmX, 0);

		// TODO paint item
		try {
			for (int i = 0; i < m_ItemPage.size(); i++) {
				vt = m_ItemPage.get(i);
				for (int j = 0; j < vt.size(); j++) {
					((MyObj) vt.elementAt(j)).paintItem(g, wCell * (j % col)
							+ GameResource.instance.imgAvatar_Khung.getWidth() / 4 + i * width, y
							+ hCell * (j / col) + hCell / 2, ((MyObj) vt.elementAt(j)).itemId,
							(col_select == (j % col) && row_select == (j / col)) ? 1 : 0,
							this.m_frame);
//					if((MyObj) vt.elementAt(j) instanceof BoardInfo){
//						BoardInfo b =(BoardInfo) vt.elementAt(j);
//						System.out.println(b.numberPlayer);
//					}
				}
			}

			// TODO paint info
			if (isPaintInfo) {
				g.setClip(pageCurrent * width, 0, width, GameCanvas.h);
				int yTmp = GameResource.instance.imgAvatar_Khung.getHeight() >> 1;
				if(isType == 3){//items
					yTmp = yTmp* 2 + Screen.ITEM_HEIGHT;
				}
				myObj.paintInfo_Item(g, GameResource.instance.imgAvatar_Khung.getWidth() / 4 * 3
						+ wCell * col_select + pageCurrent * width, y + hCell * row_select
						+  yTmp, width_Info,
						height_Info, myObj, isType, wCell);
			}
			g.translate(-g.getTranslateX(), -g.getTranslateY());
			g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
			// TODO paint scroll bar
			m_ScrollView.paint(g);
		} catch (Exception e) {

		}
	}

	// TODO update
	public void update() {
		if (isUpdate)
			m_ScrollView.update();
	}

	// TODO updateKey
	public void updateKey() {
		if (isUpdate)
			m_ScrollView.updateKey();
		if (GameCanvas.isPointerClick) {
			if (GameCanvas.isPointer(x, y, width, heigh)
					&& Math.abs((GameCanvas.instance.pyLast - GameCanvas.instance.py)) < 10) {

				// setSelect((int) (Math.abs(m_ScrollView.cmY
				// + (GameCanvas.instance.py - y)) / m_HeighItem));
				selectItem();
			}
		}
	}

	// TODO set isPaintInfo
	public void setPaintInfo(boolean m_IsPaintInfo) {
		this.isPaintInfo = m_IsPaintInfo;
	}

	// TODO set type info
	public void setTypeInfo(int type) {
		this.isType = type;
	}

	public void setWidthHeigh_Info(int m_widthInfo, int m_heigh_Info) {
		this.width_Info = m_widthInfo;
		this.height_Info = m_heigh_Info;
	}

}
