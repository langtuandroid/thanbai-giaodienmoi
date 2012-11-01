package com.hdc.mycasino.model;

import com.danh.standard.Graphics;
import com.hdc.mycasino.font.BitmapFont;

public class MoneyInfo extends ItemInfo {

	public String info;
	public String smsContent;
	public String smsTo;

	// paint in list SMS
	public void paintInRow(Graphics g, int x, int y, int width, int height) {

		String arr[] = BitmapFont.m_bmFont.splitFontBStrInLine(info, width - 4);
		int tmpY = y + (height >> 1);
		if (arr.length >= 2) {
			tmpY -= 7;
			int i;
			for (i = 0; i < arr.length; i++) {
				BitmapFont.drawItalicFont(g, arr[i], x + 5, tmpY, 0xffb901, Graphics.VCENTER
						| Graphics.LEFT);
				tmpY += 15;
			}
		} else {
			BitmapFont.drawItalicFont(g, info, x + 2, tmpY, 0xffb901, Graphics.VCENTER
					| Graphics.LEFT);
		}
		arr = null;
	}
}
