package com.hdc.mycasino.model;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.utilities.FrameImage;

public class SeperatorInfo extends MyObj {

	public static final byte itemHeight = 30;

	public void paintIcon(Graphics g, int x, int y) {
	}

	public void paintInRow(Graphics g, int x, int y, int width, int height) {
		BitmapFont.drawBoldFont(g, itemName, x, y + 18, 0xffff00, Graphics.VCENTER | Graphics.LEFT);
		g.setColor(0xffff00);
		g.drawLine(x, y + 26, x + GameCanvas.w, y + 26);
	}

	@Override
	public void paintInfo_Item(Graphics g, int x, int y, int width, int height, MyObj myObj,
			int type, int m_widthItem) {
		// TODO Auto-generated method stub

	}

	public void paintInfo(Graphics g, int x, int y) {
	}

	@Override
	public void paintItem(Graphics g, float x, float y, int m_IdFrame, int select,
			FrameImage m_frame) {
		// TODO Auto-generated method stub

	}

	public void focusItem() {
	}

}
