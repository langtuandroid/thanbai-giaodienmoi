package com.hdc.mycasino.animation;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.utilities.GameResource;

public class WinnerEffect extends Effect {

	int index = -1;
	boolean flagWin = false;

	public void startEffect(int x, int y, boolean m_flagWin, int m_index) {
		// TODO Auto-generated method stub
		super.startEffect(x, y);
		index = m_index;
		flagWin = m_flagWin;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		m_iGameTick++;
		if (m_iGameTick > 40)
			GameCanvas.instance.m_arrEffect.removeAllElements();
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (flagWin) {
			if (m_iGameTick % 2 == 0)
				GameResource.instance.m_frameWin_HighLightWin.drawFrame(0,
						m_posCenterEffect.x, m_posCenterEffect.y,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER,
						g);
			else
				GameResource.instance.m_frameWin_HighLightWin.drawFrame(1,
						m_posCenterEffect.x, m_posCenterEffect.y,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER,
						g);
		}

		GameResource.instance.m_frameWin_Icon.drawFrame(index,
				m_posCenterEffect.x, m_posCenterEffect.y, Sprite.TRANS_NONE,
				Graphics.HCENTER | Graphics.VCENTER, g);
	}
}
