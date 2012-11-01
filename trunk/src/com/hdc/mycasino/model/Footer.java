package com.hdc.mycasino.model;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.utilities.GameResource;

public class Footer {
	int x, y, width, heigh;
	boolean flagSelect = false;
	String text;
	int colorBefore;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeigh() {
		return heigh;
	}

	public void setHeigh(int heigh) {
		this.heigh = heigh;
	}

	public boolean isFlagSelect() {
		return flagSelect;
	}

	public void setFlagSelect(boolean flagSelect) {
		this.flagSelect = flagSelect;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getColorBefore() {
		return colorBefore;
	}

	public void setColorBefore(int colorBefore) {
		this.colorBefore = colorBefore;
	}

	public int getColorAfter() {
		return colorAfter;
	}

	public void setColorAfter(int colorAfter) {
		this.colorAfter = colorAfter;
	}

	public int getIdxBefore() {
		return idxBefore;
	}

	public void setIdxBefore(int idxBefore) {
		this.idxBefore = idxBefore;
	}

	public int getIdxAfter() {
		return idxAfter;
	}

	public void setIdxAfter(int idxAfter) {
		this.idxAfter = idxAfter;
	}

	public Command getCmd() {
		return cmd;
	}

	public void setCmd(Command cmd) {
		this.cmd = cmd;
	}

	int colorAfter;
	int idxBefore;
	int idxAfter;
	public Command cmd;

	public Footer(int x, int y, int w, int h, int colorBefore, int colorAfter, Command cmd,
			int idxBefore, int idxAfter, String text) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		this.width = w;
		this.heigh = h;
		this.colorAfter = colorAfter;
		this.colorBefore = colorBefore;
		this.cmd = cmd;
		this.idxBefore = idxBefore;
		this.idxAfter = idxAfter;
		this.flagSelect = false;
		this.text = text;
	}

	// TODO paint
	@SuppressWarnings("static-access")
	public void paint(Graphics g) {
		BitmapFont.setTextSize(14f/HDCGameMidlet.instance.scale);
		if (!flagSelect) {
			// item cập nhật
			GameResource.instance.m_frameRoom_IconRoom.drawFrame(idxBefore, x, y
					+ GameResource.instance.imgHeaderTop.getHeight() / 2, Sprite.TRANS_NONE,
					Graphics.LEFT | Graphics.VCENTER, g);
			// paint text Thoát
			BitmapFont.m_bmNormalFont.drawNormalFont_1(g, text, x
					+ GameResource.instance.m_frameRoom_IconRoom.frameWidth, y
					+ GameResource.instance.imgHeaderTop.getHeight() / 2, colorBefore,
					Graphics.LEFT | Graphics.VCENTER);
		} else {
			// item cập nhật
			GameResource.instance.m_frameRoom_IconRoom.drawFrame(idxAfter, x, y
					+ GameResource.instance.imgHeaderTop.getHeight() / 2, Sprite.TRANS_NONE,
					Graphics.LEFT | Graphics.VCENTER, g);
			// paint text Thoát
			BitmapFont.m_bmNormalFont.drawNormalFont_1(g, text, x
					+ GameResource.instance.m_frameRoom_IconRoom.frameWidth, y
					+ GameResource.instance.imgHeaderTop.getHeight() / 2, colorAfter, Graphics.LEFT
					| Graphics.VCENTER);
		}
	}

	public void updateKey() {
		// TODO down
		if (GameCanvas.isPointerDown) {
			if (GameCanvas.isPointer_Down(x, y, width, heigh)) {
				flagSelect = true;
			}
		}

		// TODO click
		if (GameCanvas.isPointerClick) {
			if (GameCanvas.isPointer(x, y, width, heigh)) {
				flagSelect = false;
				if (cmd != null)
					cmd.action.perform();
				// HDCGameMidlet.instance.Toast("Footer - update key");
			}
		}
	}

}
