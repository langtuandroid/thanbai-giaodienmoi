package com.hdc.mycasino.animation;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.utilities.GameResource;

public class FlyImage extends Effect {
	float scale;
	Image m_img;
	int w, h;

	public void startEffect(int x, int y, Image img) {
		// TODO Auto-generated method stub
		super.startEffect(x, y);
		this.m_img = img;
		w = m_img.getWidth();
		h = m_img.getHeight();
		scale = 0.5f;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (scale < 1.5f)
			scale += (float) 0.25f;
		m_iGameTick++;
		if (m_iGameTick > 50) {
			GameCanvas.m_arrEffect.removeElement(this);
		}

	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.drawScaleImage(m_img, m_posCenterEffect.x, m_posCenterEffect.y, Graphics.VCENTER | Graphics.HCENTER, w
				* scale, h * scale);
	}

}
