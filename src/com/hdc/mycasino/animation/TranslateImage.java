package com.hdc.mycasino.animation;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.model.Position;
import com.hdc.mycasino.utilities.GameResource;

public class TranslateImage extends Effect {

	Position mPos;
	Image m_img;
	boolean flagPaint = true;

	public void startEffect(int x, int y, int xTo, int yTo, Image img) {
		// TODO Auto-generated method stub
		super.startEffect(x, y);
		mPos = new Position();
		mPos.setPosition(x, y);
		mPos.setPositionTo(xTo, yTo);
		m_img = img;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		mPos.translate();
		if (mPos.x == mPos.xTo && mPos.y == mPos.yTo) {
			m_iGameTick++;
			if (m_iGameTick == 100)
				GameCanvas.instance.m_arrEffect.removeAllElements();
			if (m_iGameTick % 2 == 0)
				flagPaint = false;
		}
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (flagPaint)
			g.drawImage(m_img, mPos.x, mPos.y, Graphics.LEFT | Graphics.BOTTOM);
	}

}
