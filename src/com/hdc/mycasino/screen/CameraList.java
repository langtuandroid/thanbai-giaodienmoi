package com.hdc.mycasino.screen;

import com.hdc.mycasino.GameCanvas;

public class CameraList {
	public int cmdy, cmvy, cmyLim, h, w, disY, disX, sizeW, sizeH, wOne, hOne, x, y, size;
	public int cmdx, cmvx, cmxLim;
	public static int cmy, cmtoY, cmx, cmtoX;
	public int selected;
	public boolean isShow = false;

	public void setInfo(int x, int y, int wOne, int hOne, int w, int h, int limX, int limY, int size) {
		this.x = x;
		this.y = y;

		sizeH = h / hOne;
		sizeW = w / wOne;
		this.size = size;
		this.wOne = wOne;
		this.hOne = hOne;
		this.h = h;
		this.w = w;
		disY = limY;
		disX = limX;
		selected = 0;
		cmy = cmtoY = 0;
		cmyLim = h - disY;
		if (cmyLim < 0)
			cmyLim = 0;
		cmx = cmtoX = 0;
		cmxLim = w - disX;
		if (cmxLim < 0)
			cmxLim = 0;
		isShow = true;
	}

	public void close() {
		isShow = false;
		System.gc();
	}

	public void setSelect(int se) {
		selected = se;
		setCam();
	}

	int pa = 0, pb = 0;

	public void updateKey() {

		if (GameCanvas.instance.menu.m_showMenu || GameCanvas.currentDialog != null)
			return;

		boolean changeFocus = false;

		if (GameCanvas.instance.hasPointerEvents() && GameCanvas.isPointer(x, y, w, disY)) {
			if (GameCanvas.isPointerDown) {
				pa = GameCanvas.pyLast - GameCanvas.py;
				int aa = (h / (disY) * 2);
				if (aa != 0)
					pa *= aa;
				pb = GameCanvas.pxLast - GameCanvas.px;
			}

			// move list menu
			if (GameCanvas.isPointerMove) {
				cmtoY = cmy + pa;
				if (cmtoY < -sizeH)
					cmtoY = -sizeH;
				if (cmtoY > cmyLim + sizeH)
					cmtoY = cmyLim + sizeH;
				cmtoX = cmx + pb;
				if (cmtoX < -sizeH)
					cmtoX = -sizeH;
				if (cmtoX > cmxLim + sizeH)
					cmtoX = cmxLim + sizeH;
				if (Math.abs(pa) < 10)
					changeFocus = true;
			}

			// if (GameCanvas.instance.isMove()) {
			if (GameCanvas.isPointerClick) {
				cmtoY = cmy + pa;
				if (cmtoY < -sizeH)
					cmtoY = -sizeH;
				if (cmtoY > cmyLim + sizeH)
					cmtoY = cmyLim + sizeH;
				cmtoX = cmx + pb;
				if (cmtoX < -sizeH)
					cmtoX = -sizeH;
				if (cmtoX > cmxLim + sizeH)
					cmtoX = cmxLim + sizeH;
				int aa = (cmtoY + GameCanvas.py - y) / (hOne);
				if (Math.abs(pa) < 10)
					changeFocus = true;

				selected = aa * sizeW;
				if (sizeW > 1)
					selected += (cmtoX + GameCanvas.px - x) / (wOne);
				if (selected < 0)
					selected = 0;
				if (selected >= sizeH * sizeW)
					selected = sizeH * sizeW - 1;

				// if (!GameCanvas.instance.isMove(x, y, w, disY))
				if (GameCanvas.isPointer(x, y, w, disY))
					GameCanvas.currentScreen.setSelected(selected);
			}

			// }
		}
		if (changeFocus)
			setCam();
	}

	private void setCam() {
		cmtoY = (selected / sizeW) * hOne - (disY / 2);
		if (cmtoY < 0)
			cmtoY = 0;
		if (cmtoY > cmyLim)
			cmtoY = cmyLim;
		if (selected / sizeW > sizeH - 1 || selected / sizeW == 0)
			cmy = cmtoY;
		cmtoX = (selected % sizeW) * wOne - (disX / 2);
		if (cmtoX < 0)
			cmtoX = 0;
		if (cmtoX > cmxLim)
			cmtoX = cmxLim;
		if (selected % sizeW > sizeW - 1 || selected % sizeW == 0)
			cmx = cmtoX;
	}

	public void moveCamera() {
		if (GameCanvas.instance.menu.m_showMenu || GameCanvas.currentDialog != null)
			return;
		if (!GameCanvas.instance.isMove() || GameCanvas.instance.isPointerDown) {
			if (cmy != cmtoY) {
				cmvy = (cmtoY - cmy) << 2;
				cmdy += cmvy;
				cmy += cmdy >> 4;
				cmdy = cmdy & 0xf;
			}
			if (cmx != cmtoX) {
				cmvx = (cmtoX - cmx) << 2;
				cmdx += cmvx;
				cmx += cmdx >> 4;
				cmdx = cmdx & 0xf;
			}
			if (Math.abs(cmtoY - cmy) < 15 && cmy < 0) {
				// HDCGameMidlet.instance.Toast("cmtoY :" + cmtoY + " - cmy : "
				// + cmy);
				cmtoY = 0;
			}
			if (Math.abs(cmtoY - cmy) < 10 && cmy > cmyLim) {
				// HDCGameMidlet.instance.Toast("cmtoY :" + cmtoY +
				// " - cmyLim : "
				// + cmyLim + " - cmy :" + cmy);
				cmtoY = cmyLim;
			}
			if (Math.abs(cmtoX - cmx) < 15 && cmx < 0) {
				cmtoX = 0;
			}
			if (Math.abs(cmtoX - cmx) < 10 && cmx > cmxLim) {
				cmtoX = cmxLim;
			}
		}
	}
}
