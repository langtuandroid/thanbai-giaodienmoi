package com.danh.view;

import java.util.Vector;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.MyObj;
import com.hdc.mycasino.utilities.GameResource;

public class ListView {
	private int x, y, width, heigh;
	private int m_HeighItem;
	private Vector m_ListItem;
	private ScrollView m_ScrollView;
	private Command m_Cmd;
	public int index = 0;
	public boolean isUpdate = true;
	public Image imgLine;

	public ListView() {
		// TODO Auto-generated constructor stub
		m_ScrollView = new ScrollView();
	}

	// TODO setInfo
	public void setInfo(int x, int y, int width, int heigh, int heighItem, Vector listItem) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.heigh = heigh;
		this.m_HeighItem = heighItem;
		this.m_ListItem = listItem;
		m_ScrollView.initScroll(x, y, width, heigh, true, this.m_ListItem.size() * this.m_HeighItem
				- this.heigh);

		// TODO set update for game
		// neu so luong item it hon chieu cao dc setClip thi
		// khong update ListView
		if (this.m_ListItem.size() * this.m_HeighItem - this.heigh < 0)
			isUpdate = false;

		// TODO create image line
		createImageLine();
	}

	// TODO init image Line
	private void createImageLine() {
		imgLine = Image.createImage(width, GameResource.instance.imgPopupLine.getHeight());
		Graphics g1 = imgLine.getGraphics();
		int col = width / GameResource.instance.imgPopupLine.getWidth();
		for (int i = 0; i < col; i++) {
			g1.drawImage(GameResource.instance.imgPopupLine,
					i * GameResource.instance.imgPopupLine.getWidth(), 0, Graphics.LEFT
							| Graphics.TOP);
		}
	}

	// TODO paint
	public void paint(Graphics g) {
		if (m_ListItem != null) {
			g.translate(0, y);
			g.setClip(x, 0, width, heigh);
			g.translate(0, -m_ScrollView.cmY);

			for (int i = 0; i < m_ListItem.size(); i++) {
				if (i == index) {
					g.drawImage(GameResource.instance.imgTabs_HightLightRow, x, i * m_HeighItem,
							Graphics.LEFT | Graphics.TOP);
				}
				// TODO paint line
				g.drawImage(imgLine, x, i * m_HeighItem, Graphics.LEFT | Graphics.VCENTER);

				((MyObj) m_ListItem.elementAt(i)).paintInRow(g, x
						+ (int) (30 / HDCGameMidlet.instance.scale), i * m_HeighItem, width,
						m_HeighItem);
			}

			g.translate(-g.getTranslateX(), -g.getTranslateY());
			g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
			// TODO paint scroll bar
			m_ScrollView.paint(g);
		}
	}

	// TODO update
	public void update() {
		if (isUpdate)
			m_ScrollView.update();
	}

	// TODO updatekey
	public void updateKey() {
		if (isUpdate)
			m_ScrollView.updateKey();
		if (GameCanvas.isPointerClick) {
			if (GameCanvas.isPointer(x, y, width, heigh)
					&& Math.abs((GameCanvas.instance.pyLast - GameCanvas.instance.py)) < 5) {

				setSelect((int) (Math.abs(m_ScrollView.cmY + (GameCanvas.instance.py - y)) / m_HeighItem));
			}
		}

	}

	// TODO set select
	public void setSelect(int select) {
		if (index != select) {
			HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.dropdown);
			HDCGameMidlet.sound.play();
			index = select;
		} else if (m_ListItem != null && m_ListItem.size() > 0) {
			if (m_Cmd != null)
				m_Cmd.action.perform();
		}
	}

	// TODO get index
	public int getIndex() {
		return index;
	}

	// TODO set command
	public void setCommand(Command cmd) {
		m_Cmd = cmd;
	}

}
