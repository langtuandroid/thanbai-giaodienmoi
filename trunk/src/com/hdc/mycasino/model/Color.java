package com.hdc.mycasino.model;

import com.danh.standard.Graphics;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

public class Color extends MyObj {
	public int color;

	@Override
	public void paintIcon(Graphics g, int x, int y) {
		// TODO Auto-generated method stub
	}

	@Override
	public void paintInRow(Graphics g, int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paintInfo(Graphics g, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paintInfo_Item(Graphics g, int x, int y, int width, int height, MyObj myObj, int type, int m_widthItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusItem() {
		// TODO Auto-generated method stub

	}

	int h = GameResource.instance.imgAvatar_Khung.getWidth();

	@Override
	public void paintItem(Graphics g, float x, float y, int m_IdFrame, int select, FrameImage m_frame) {
		// TODO Auto-generated method stub
		if (select == 0)
			g.drawImage(GameResource.instance.imgAvatar_Khung, x, y, Graphics.LEFT | Graphics.VCENTER);
		else
			g.drawImage(GameResource.instance.imgAvatar_Khung_High_Light, x + h / 2, y, Graphics.HCENTER
					| Graphics.VCENTER);

		g.setColor(color);
		int h = GameResource.instance.imgAvatar_Khung.getHeight();
		int w = GameResource.instance.imgAvatar_Khung.getWidth();
		g.fillRoundRect((int) x, (int) y - h / 2, w, h, 10, 10);
	}
}
