package com.hdc.mycasino.animation;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.model.Position;
import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

public class CardFall {
	FrameImage frmImg;
	int vx, vy;
	int curFrame;
	byte speed;
	int m_iGameTick;
	public Position m_posCenterEffect = new Position();

	public CardFall() {
		m_posCenterEffect.x = CRes.random(0, GameCanvas.w);
		m_posCenterEffect.y = 0;

		vx = CRes.random(2);
		vy = CRes.random(2, 4);
		curFrame = 0;
		speed = 0;
		m_iGameTick = 0;
		frmImg = GameResource.instance.m_frameLabai;
	}

	public void update() {
		updateFrame(3);

		if (m_iGameTick % 4 == 0)
			m_posCenterEffect.x += vx;
		m_posCenterEffect.y += vy;

		m_iGameTick++;
	}

	private void updateFrame(int maxFrame) {
		if (m_iGameTick % maxFrame == 0) {
			curFrame++;
		}
	}

	public void paint(Graphics g) {
		frmImg.drawFrame(curFrame % 6, m_posCenterEffect.x, m_posCenterEffect.y, 0,
				Graphics.HCENTER | Graphics.VCENTER, g);
	}
}
