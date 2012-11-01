package com.hdc.mycasino.animation;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.model.Position;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

//explosion star animation
public class ExplosionStar extends Effect {
	public int m_iR = 40;
	public int m_iDeltaAlpha = 36;

	// private FrameImage[] m_arrFrameImg;

	public void startEffect(int x, int y, int alpha, int r) {
		super.startEffect(x, y);
		this.m_iDeltaAlpha = alpha;
		this.m_iR = r;
		m_arrPoint.removeAllElements();
		for (int i = 0; i <= 10; i++) {
			Position pos = new Position(x, y);
			pos.anchor = 2;
			m_arrPoint.addElement(pos);
		}

		// m_arrFrameImg = new FrameImage[4];
		for (int i = 0; i < 4; i++) {
			// m_arrFrameImg[i] = new
			// FrameImage(GameResource.instance.imgEffectFirework[i], 7, 7);
		}
	}

	public void paint(Graphics g) {
		int a = m_arrPoint.size();
		for (int i = 0; i < a; i++) {
			Position pos = (Position) m_arrPoint.elementAt(i);
			// m_arrFrameImg[i % 4].drawFrame(3, pos.x, pos.y,
			// Sprite.TRANS_NONE, g);
		}
	}

	public void update() {
		int a = m_arrPoint.size();
		for (int i = 0; i < a; i++) {
			Position pos = (Position) m_arrPoint.elementAt(i);
			int alpha = i * m_iDeltaAlpha;
			pos.x = (int) (Math.sin(alpha) * m_iGameTick + m_posCenterEffect.x);
			pos.y = (int) (Math.cos(alpha) * m_iGameTick + m_posCenterEffect.y);
		}

		m_iGameTick += 4;
		if (m_iGameTick > m_iR) {
			m_arrPoint.removeAllElements();
			m_arrPoint = null;
			// int b = m_arrFrameImg.length;
			// for (int i = 0; i < b; i++) {
			// m_arrFrameImg[i] = null;
			// }
			// m_arrFrameImg = null;

			GameCanvas.m_arrEffect.removeElement(this);
		}
	}

}
