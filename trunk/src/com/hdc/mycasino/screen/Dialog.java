package com.hdc.mycasino.screen;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.model.Command;

public abstract class Dialog {
	public Command left, center, right;

	public void paint(Graphics g) {
		// g.translate(-g.getTranslateX(), -g.getTranslateY());
		// g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		// PaintPopup.paintCmdBar(g, left, center, right);
	}

	public void keyPress(int keyCode) {
	}

	// public abstract void doBack();

	public void update() {
		// if (GameCanvas.isPointerClick) {
		// for (int i = 0; i < 3; i++) {
		// if (GameCanvas.isPointer(Screen.posCmd[i].x, Screen.posCmd[i].y, 40,
		// 22)) {
		// switch (i) {
		// case 0:
		// if (left != null && left.action != null)
		// left.action.perform();
		// break;
		// case 1:
		// if (center != null && center.action != null)
		// center.action.perform();
		// break;
		// case 2:
		// if (right != null && right.action != null)
		// right.action.perform();
		// break;
		// }
		// }
		// }
		// GameCanvas.isPointerClick = false;
		// }
		// if (GameCanvas.keyPressed[5]) {
		// GameCanvas.keyPressed[5] = false;
		// if (center != null && center.action != null)
		// center.action.perform();
		// }
		// if (GameCanvas.keyPressed[12]) {
		// GameCanvas.keyPressed[12] = false;
		// if (left != null && left.action != null)
		// left.action.perform();
		// }
		//
		// // BB: BB can't handle keyPress for soft2
		// if (GameCanvas.isBB) {
		// if (GameCanvas.keyReleased[13]) {
		// GameCanvas.keyReleased[13] = false;
		// if (right != null && right.action != null)
		// right.action.perform();
		// }
		// } else if (GameCanvas.keyPressed[13]) {
		// GameCanvas.keyPressed[13] = false;
		// if (right != null && right.action != null)
		// right.action.perform();
		// }
	}

	public abstract void show();
}
