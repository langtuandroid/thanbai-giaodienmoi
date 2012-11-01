package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.view.ListView;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.MyObj;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;

public class ListTabScr extends Screen {
	int i;
	public Vector m_arrItems = null;
	public int itemHeight = 50;
	int curHeight = 0; // current height of selected item
	int totalHeight = 0;
	boolean m_bIsPlayer = false;

	// TODO
	public int m_iHeightItem;// = (int) (70 / HDCGameMidlet.instance.scale);
	public int m_iTopScroll;// = (int) (70 / HDCGameMidlet.instance.scale);

	// TODO listview
	private ListView m_ListView;

	// TODO init list tab scr
	public ListTabScr() {
		// TODO Auto-generated constructor stub
		m_width = GameCanvas.w;
		m_height = GameCanvas.h - (int) (60 / HDCGameMidlet.instance.scale);
		m_left = 0;
		m_top = 0;
	}

	@Override
	public void setSelected(int index) {
		// TODO Auto-generated method stub
		if (index != m_selected) {
			super.setSelected(index);
		} else if (m_arrItems != null && m_arrItems.size() > 0) {
			if (m_cmdCenter != null)
				m_cmdCenter.action.perform();
		}
	}

	public void setListItem(Vector items, Command leftCmd, Command centerCmd, Command rightCmd,
			boolean isPlayer) {

		m_cmdLeft = leftCmd;
		m_cmdRight = rightCmd;
		m_cmdCenter = centerCmd;

		m_bIsPlayer = isPlayer;
		// m_left = 0;
		// m_top = GameCanvas.hBottomBar + 35;
		setLocation();
		m_width = GameCanvas.w;
		m_height = GameCanvas.h - GameCanvas.hBottomBar - m_top;

		m_arrItems = items;
		m_selected = -1;
		curHeight = 0;
		init();
		// if (m_arrItems != null && m_arrItems.size() > 0 &&
		// m_arrItems.elementAt(m_selected) instanceof SeperatorInfo) {
		// curHeight += SeperatorInfo.itemHeight;
		// m_selected++;
		// if (m_selected >= m_arrItems.size())
		// m_selected = 0;
		// setCam();
		// }

		// TODO set list view
		m_ListView = new ListView();
		m_ListView.setInfo(m_left, m_top + m_iTopScroll,
				GameResource.instance.imgListScr_Panel.getWidth(), GameCanvas.h - m_top - 2
						* m_iTopScroll, m_iHeightItem, m_arrItems);
		m_ListView.setCommand(m_cmdCenter);

	}

	// TODO set location for list item
	// m_left & m_top
	private void setLocation() {
		m_left = (GameCanvas.w - GameResource.instance.imgListScr_Panel.getWidth()) / 2;
		m_top = (int) ((GameCanvas.hBottomBar + 35) / HDCGameMidlet.instance.scale);

		// HDCGameMidlet.instance.Toast(m_left + " - " + m_top);
	}

	public void init() {
		// GameCanvas.cameraList.close();
		// Scroll.gI().init(m_height, m_arrItems.size() * itemHeight,
		// CameraList.cmy);
		// totalHeight = 0;
		// for (int i = 0; i < m_arrItems.size(); i++) {
		// if (m_arrItems.elementAt(i) instanceof SeperatorInfo)
		// totalHeight += SeperatorInfo.itemHeight;
		// else
		// totalHeight += itemHeight;
		// }
		//
		// Scroll.gI().init(m_height, totalHeight, CameraList.cmy);
		// limit = (m_height - itemHeight) - (totalHeight);
		//
		// if (limit > 0)
		// limit = 0;

		// Scroll.gI().init(m_height - m_iTopScroll,
		// m_arrItems.size() * m_iHeightItem, CameraList.cmy);
		// GameCanvas.cameraList.setInfo(m_left, m_top + m_iTopScroll
		// + m_iHeightItem, m_width, m_iHeightItem, m_width,
		// m_arrItems.size() * m_iHeightItem, m_width, m_height
		// - m_iHeightItem - m_iTopScroll, m_arrItems.size());
	}

	@Override
	public void loadImg() {
		// TODO Auto-generated method stub
		super.loadImg();

		if (HDCGameMidlet.instance.scale == 2f) {

			float scale;
			scale = (float) 2 / 5;
			GameResource.instance.imgListScr_Panel = ImagePack.createImage(
					ImagePack.LIST_SCREEN_PANEL, scale);
			// scale hight light
			GameResource.instance.imgListScr_HightLight = Image.scaleImage(
					GameResource.instance.imgListScr_HightLight,
					GameResource.instance.imgListScr_Panel.getWidth(),
					GameResource.instance.imgListScr_HightLight.getHeight());
			// HDCGameMidlet.instance.Toast(GameResource.instance.imgListScr_Panel
			// .getWidth()
			// + " - "
			// + GameResource.instance.imgListScr_Panel.getHeight());

		}

	}

	public void paint(Graphics g) {

		m_ListView.paint(g);
	}

	public void updateKey() {
		m_ListView.updateKey();

		if (GameCanvas.instance.menu.m_showMenu || GameCanvas.currentDialog != null)
			return;

		boolean changeFocus = false;

		// if (GameCanvas.instance.hasPointerEvents() &&
		// GameCanvas.isPointer(m_left, m_top, m_width, m_height)) {
		//
		// if (GameCanvas.isPointerDown) {
		// int pa = GameCanvas.pyLast - GameCanvas.py;
		// curHeight += pa;
		// changeFocus = true;
		// }
		// if (GameCanvas.isPointerClick) {
		// int heightClick = GameCanvas.py - m_top - cmy;
		// int i;
		// int tmpHeight = 0;
		// int h = 0;
		// for (i = 0; i < m_arrItems.size(); i++) {
		// h = itemHeight;
		// if (m_arrItems.elementAt(i) instanceof SeperatorInfo) {
		// h = SeperatorInfo.itemHeight;
		// if (heightClick >= tmpHeight + h / 2 && heightClick <= tmpHeight + h)
		// {
		// if (i == m_selected && m_cmdCenter != null) {
		// m_cmdCenter.action.perform();
		// break;
		// }
		//
		// m_selected = i + 1;
		// if (m_selected >= m_arrItems.size())
		// m_selected = i - 1;
		// changeFocus = true;
		// curHeight = tmpHeight + h;
		// break;
		// } else if (heightClick > tmpHeight && heightClick <= tmpHeight + h /
		// 2) {
		// if (i == m_selected && m_cmdCenter != null) {
		// m_cmdCenter.action.perform();
		// break;
		// }
		//
		// m_selected = i - 1;
		// if (m_selected < 0)
		// m_selected = i + 1;
		// changeFocus = true;
		// curHeight = tmpHeight + h;
		// break;
		// }
		// } else if (heightClick >= tmpHeight && heightClick <= tmpHeight + h)
		// {
		// if (i == m_selected && m_cmdCenter != null) {
		// m_cmdCenter.action.perform();
		// break;
		// }
		//
		// m_selected = i;
		// changeFocus = true;
		// curHeight = tmpHeight + h;
		// break;
		// }
		// tmpHeight += h;
		// }
		// }
		// }
		// if (changeFocus)
		// setCam();

	}

	// private void setCam() {
	// // set scroll for camera
	// int tmp = m_height - itemHeight;
	// cmtoY = tmp - curHeight;
	//
	// if (cmtoY > 0)
	// cmtoY = 0;
	// if (cmtoY < limit)
	// cmtoY = limit;
	// }

	public void update() {
		m_ListView.update();
		if (m_selected != m_ListView.index)
			m_selected = m_ListView.index;
	}

	public void close() {
	}

	public MyObj getSelectItems() {
		if (m_selected >= 0 && m_selected < m_arrItems.size()) {
			return (MyObj) m_arrItems.elementAt(m_selected);
		}

		return null;
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		if (m_cmdLeft == null) {
			GameCanvas.instance.menu.m_list = null;
		} else {
			m_cmdLeft.action.perform();
		}
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		if (m_cmdRight != null)
			m_cmdRight.action.perform();
	}
}
