package com.hdc.mycasino.model;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.utilities.GameResource;

public class Position {

	public int x, y, xTo, yTo, anchor;
	public int count = 0;

	public Position() {
		this.x = 0;
		this.y = 0;
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setPositionTo(int xTo, int yTo) {
		this.xTo = xTo;
		this.yTo = yTo;
	}

	public void createPosInBoard(int pos, int countPlayer) {
		switch (pos) {
		case 0:
			createPosInBoard(0);
			break;
		default:

			switch (countPlayer) {
			case 2:
				createPosInBoard(2);
				break;
			default:
				switch (pos) {
				case 1:
					createPosInBoard(1);
					break;
				case 2:
					createPosInBoard(2);
					break;
				default:
					createPosInBoard(3);
					break;
				}
				break;
			}
			break;
		}
	}

	public void createPosInBoard(int anchor) {
		switch (anchor) {
		case 0:
			x = GameCanvas.hw;
			y = GameCanvas.h - GameResource.instance.imgTienLen_User.getHeight();
			// x = GameCanvas.hw;
			// y = GameCanvas.h -
			// GameResource.instance.m_frameTienLen_PanelInfo.frameHeight / 4 *
			// 5;
			this.anchor = 0;
			break;
		case 1:
			if (HDCGameMidlet.scale == 1)
				x = GameCanvas.w / 8;
			else
				x = GameResource.instance.imgTienLen_User.getWidth() / 2;
			y = (GameCanvas.h - GameResource.instance.imgTienLen_User.getWidth()) / 2;
			// x = GameCanvas.w / 8;
			// y = (GameCanvas.h -
			// GameResource.instance.m_frameTienLen_PanelInfo.frameHeight) / 2;
			this.anchor = 1;
			break;
		case 2:
			x = GameCanvas.hw;
			y = GameResource.instance.imgTienLen_User.getHeight() / 2;
			// x = GameCanvas.hw;
			// y = GameResource.instance.imgHeaderBg.getHeight() / 4 * 5;
			this.anchor = 2;
			break;
		case 3:
			if (HDCGameMidlet.scale == 1)
				x = GameCanvas.w / 8 * 7;
			else
				x = GameCanvas.w - GameResource.instance.imgTienLen_User.getWidth() / 2;
			y = (GameCanvas.h - GameResource.instance.imgTienLen_User.getWidth()) / 2;
			// x = GameCanvas.w / 8 * 7;
			// y = (GameCanvas.h -
			// GameResource.instance.m_frameTienLen_PanelInfo.frameHeight) / 2;
			this.anchor = 3;
			break;
		}
	}

	public int translate() {
		if (x == xTo && y == yTo)
			return -1;

		if (Math.abs((xTo - x) >> 1) <= 1 && Math.abs((yTo - y) >> 1) <= 1) {
			x = xTo;
			y = yTo;
			return 0;
		}
		if (x != xTo) {
			x += (xTo - x) >> 1;
			count++;
		}
		if (y != yTo) {
			y += (yTo - y) >> 1;
			count++;
		}
		return 1;
	}

	public boolean getCenter() {
		return (count == 2) ? true : false;
	}

}
