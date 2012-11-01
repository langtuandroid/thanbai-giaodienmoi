package com.hdc.mycasino.animation;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.model.Position;

public abstract class Effect {
	protected Vector m_arrPoint = new Vector();

	public Position m_posCenterEffect = new Position();
	protected int m_iGameTick;

	public void startEffect(int x, int y) {
		m_posCenterEffect.x = x;
		m_posCenterEffect.y = y;
		m_iGameTick = 0;

		GameCanvas.m_arrEffect.addElement(this);
	}

	public abstract void update();

	public abstract void paint(Graphics g);
}