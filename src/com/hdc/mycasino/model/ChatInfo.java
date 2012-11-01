package com.hdc.mycasino.model;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.screen.Screen;
import com.hdc.mycasino.utilities.FrameImage;

public class ChatInfo extends MyObj {

	public String content;
	public int color;
	public int heightItem;

	@Override
	public void paintItem(Graphics g, float x, float y, int m_IdFrame, int select,
			FrameImage m_frame) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paintInfo_Item(Graphics g, int x, int y, int width, int height, MyObj myObj,
			int type, int m_widthItem) {
		// TODO Auto-generated method stub

	}

	public ChatInfo(String content, int color) {
		this.content = content;
		this.color = color;
	}

	public void paintIcon(Graphics g, int x, int y) {

	}

	public void paintInRow(Graphics g, int x, int y, int width, int height) {
		Vector listContent = BitmapFont.splitStringByWidth(content, width, false);
		int perH = (Screen.ITEM_HEIGHT * 2 / 3);
		heightItem = (listContent.size() + 1) * perH;

		for (int i = 0; i < listContent.size(); i++) {
			BitmapFont.drawNormalFont(g, (String) listContent.elementAt(i), x, y + i * perH
					+ Screen.ITEM_HEIGHT / 3, color, Graphics.LEFT | Graphics.TOP);
		}
		listContent.removeAllElements();
		listContent = null;
	}

	public void paintInfo(Graphics g, int x, int y) {

	}

	public int getHeightItem(int width) {
		Vector listContent = BitmapFont.splitStringByWidth(content, width, false);
		heightItem = (listContent.size() + 1) * 10;
		listContent = null;
		return 0;
	}

	public void focusItem() {
	}
}
