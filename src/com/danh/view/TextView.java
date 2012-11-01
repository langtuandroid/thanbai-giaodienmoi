package com.danh.view;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.font.BitmapFont;

public class TextView {
	private int x, y, width, heigh;
	private int widthLine, heighLine;
	private String[] m_listLine;
	private ScrollView m_ScrollView;
	private int color;
	public boolean isUpdate = true;

	public TextView() {
		// TODO Auto-generated constructor stub
		m_ScrollView = new ScrollView();
	}

	// TODO set info
	public void setInfo(int x, int y, int width, int heigh, int widthLine, int heighLine,
			String[] m_listLine) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.heigh = heigh;
		this.widthLine = widthLine;
		this.heighLine = heighLine;
		this.m_listLine = m_listLine;
		this.m_ScrollView.initScroll(x, y, width, heigh, true, this.m_listLine.length
				* this.heighLine - this.heigh / 2);
		if (this.m_listLine.length * this.heighLine - this.heigh / 2 < 0)
			isUpdate = false;
	}

	// TODO set color
	public void setColor(int color) {
		this.color = color;
	}

	// TODO paint
	public void paint(Graphics g) {
		g.translate(0, y);
		g.setClip(x, heighLine, width, heigh);
		g.translate(0, -m_ScrollView.cmY);
		// TODO paint list text
		for (int i = 0; i < m_listLine.length; i++) {
			BitmapFont.drawNormalFont(g, (String) m_listLine[i], x, y + i * heighLine, color,
					Graphics.LEFT | Graphics.TOP);
		}

		g.translate(-g.getTranslateX(), -g.getTranslateY());
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		// TODO paint scroll
		m_ScrollView.paint(g);
	}

	// TODO update key
	public void updateKey() {
		if (isUpdate)
			m_ScrollView.updateKey();
	}

	// TODO update
	public void update() {
		if (isUpdate)
			m_ScrollView.update();
	}
}
