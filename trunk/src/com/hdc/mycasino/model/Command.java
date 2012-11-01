package com.hdc.mycasino.model;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.hdc.mycasino.font.BitmapFont;

public class Command {
	public String caption;
	public IAction action;
	public Vector list = null;
	public int index = 0;

	public Command(String caption, IAction action) {
		this.caption = caption;
		this.action = action;
	}

	@SuppressWarnings("rawtypes")
	public Command(String caption, IAction action, Vector list) {
		this.caption = caption;
		this.action = action;
		this.list = list;
	}

	public Command(String caption, int index, IAction action) {
		this.caption = caption;
		this.action = action;
		this.index = index;
	}

	public void paint(Graphics g, float x, float y) {
		BitmapFont.drawNormalFont(g, caption, x, y, 0x3a001e, 2);
	}
}
