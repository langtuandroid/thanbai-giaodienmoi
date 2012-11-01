package com.hdc.mycasino.animation;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.font.BitmapFont;

//fly text animation
public class FlyText extends Effect {
	String m_text;
	int m_contentColor;
	int m_boundColor;
	boolean select = true;

	public void startEffect(String text, int x, int y, int contentColor, int boundColor) {
		super.startEffect(x, y);
		m_text = text;
		m_boundColor = boundColor;
		m_contentColor = contentColor;
		select = true;
	}

	public void paint(Graphics g) {
		BitmapFont.drawOutlinedString(g, m_text, m_posCenterEffect.x, m_posCenterEffect.y,
				m_contentColor, m_boundColor, 2);
	}

	public void update() {
		m_posCenterEffect.y--;
		m_iGameTick++;
		if (m_iGameTick > 30) {
			GameCanvas.m_arrEffect.removeElement(this);
		}
	}
}
