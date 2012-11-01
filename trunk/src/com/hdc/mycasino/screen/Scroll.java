package com.hdc.mycasino.screen;

import com.danh.standard.Graphics;
import com.hdc.mycasino.utilities.GameResource;

public class Scroll {
	static Scroll instance;

	public static Scroll gI() {
		if (instance == null)
			instance = new Scroll();
		return instance;
	}

	public int limit = 0, dis, yScroll, hScroll = 100, h;
	public static boolean Disvisible = false, isAble = false;

	public void init(int dis, int size, int cmy) {
		this.dis = dis;
		hScroll = 100;
		limit = size - dis;
		if (size > dis) {
			hScroll = dis * 100 / size;
			if (hScroll < 2)
				hScroll = 2;
			h = cmy * 100 / limit;
			int per = dis - (dis) * hScroll / 100;
			yScroll = h * (per) / 100;
		}
		Disvisible = (limit + dis < dis);
		isAble = true;
	}

	public void updateScroll(int cmy, int cmtoY) {
		if (Disvisible)
			return;
		h = cmy * 100 / limit;
		int per = dis - (dis) * hScroll / 100;
		yScroll = h * (per) / 100;
		if (yScroll > dis - 3)
			yScroll = dis - 3;
		if (cmy != cmtoY)
			Scroll.isAble = true;
		else
			Scroll.isAble = false;
	}

	public void paintScroll(Graphics g, int x, int y) {
		if (Disvisible || !isAble)
			return;
		// g.setClip(x, y, 4, dis);
		// g.setColor(0x541c07);
		// g.fillRect(x, y + 1 + yScroll, 4, (dis) * hScroll / 100 - 2);

		g.drawImage(GameResource.instance.imgScrollBar, x, y + 1 + yScroll, Graphics.LEFT
				| Graphics.TOP);

	}
}
