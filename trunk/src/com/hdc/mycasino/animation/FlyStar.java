package com.hdc.mycasino.animation;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.model.Position;
import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.GameResource;

public class FlyStar {

	public Vector<Position> m_posCenterEffect = new Vector<Position>();
	protected int m_iGameTick;
	int x, y, w, h;
	int maximum = 50;
	int m_tick[] = new int[maximum];

	public void startEffect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		m_iGameTick = 0;
		initNewPos();
	}

	public void update() {
		// TODO Auto-generated method stub
		m_iGameTick++;
		if (m_iGameTick % 2 == 0) {
			int j = CRes.random(0, 5);
			int ran;
			for (int k = 0; k < j; k++) {
				ran = CRes.random(0, maximum);
				if (m_tick[ran] == -1)
					m_tick[ran] = 0;
			}
			for (int i = 0; i < maximum; i++)
				if (m_tick[i] >= 0 && m_tick[i] <= 4) {
					m_tick[i]++;
					updateMoveStar(i);
				}
		}

		if (m_iGameTick > 100) {
			m_iGameTick = 0;
			initNewPos();
		}
	}

	private void updateMoveStar(int index) {
		// TODO Auto-generated method stub
		Position p = m_posCenterEffect.elementAt(index);
		switch (p.count) {
		case 0:
			p.x++;
			break;
		case 1:
			p.x--;
			break;
		case 2:
			p.y++;
			break;
		case 3:
			p.y--;
			break;
		case 4:
			p.x++;
			p.y++;
			break;
		case 5:
			p.x++;
			p.y--;
			break;
		case 6:
			p.x--;
			p.y--;
			break;
		case 7:
			p.x--;
			p.y++;
			break;
		}
	}

	private void initNewPos() {
		double ran;
		m_posCenterEffect.removeAllElements();
		Position p;
		for (int i = 0; i < maximum / 2; i++) {
			p = new Position();
			ran = ((float) CRes.random(0, 10)) / 10;
			p.x = (int) ((1 - ran) * (1 - ran) * (x - w) + 2 * (1 - ran) * ran * x + ran * ran * (x + w));
			p.y = (int) ((1 - ran) * (1 - ran) * y + 2 * (1 - ran) * ran * (y - h) + ran * ran * y);
			p.anchor = CRes.random(10);
			p.count = CRes.random(0, 8);
			m_posCenterEffect.addElement(p);
			// phia duoi
			p = new Position();
			ran = ((float) CRes.random(0, 10)) / 10;
			p.x = (int) ((1 - ran) * (1 - ran) * (x - w) + 2 * (1 - ran) * ran * x + ran * ran * (x + w));
			p.y = (int) ((1 - ran) * (1 - ran) * y + 2 * (1 - ran) * ran * (y + h) + ran * ran * y);
			p.anchor = CRes.random(10);
			p.count = CRes.random(0, 8);
			m_posCenterEffect.addElement(p);
		}

		for (int i = 0; i < maximum; i++)
			m_tick[i] = -1;
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		for (int i = 0; i < m_posCenterEffect.size(); i++)
			if (m_tick[i] != -1)
				GameResource.instance.frameFlyStar.drawFrame((m_posCenterEffect.elementAt(i).anchor > 5) ? m_tick[i]
						: 4 - m_tick[i], m_posCenterEffect.elementAt(i).x, m_posCenterEffect.elementAt(i).y, 0, Graphics.HCENTER|Graphics.VCENTER, g);
		GameResource.instance.m_frameWin_Icon.drawFrame(0, x, y, Sprite.TRANS_NONE, Graphics.HCENTER
				| Graphics.VCENTER, g);
	}

}
