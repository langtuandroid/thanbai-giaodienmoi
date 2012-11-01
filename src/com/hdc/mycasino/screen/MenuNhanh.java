package com.hdc.mycasino.screen;

import java.util.Vector;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Command;

public class MenuNhanh {
	public static Vector vCommand = new Vector();

	public static boolean showMenuNhanh;
	public static int menuH, x, y, menuW;
	public static int focus;
	static int col;
	static int row;
	static Image imgBackground;

	public static void startMenuNhanh(Vector c, int X, int Y) {

		for (byte i = 0; i < c.size(); i++)
			vCommand.add((Command) c.elementAt(i));
		showMenuNhanh = true;

		x = X;
		y = Y;

		//
		menuW = (int) (30 / HDCGameMidlet.instance.scale);
		Command cm;
		int w;
		for (byte i = 0; i < vCommand.size(); i++) {
			cm = (Command) vCommand.elementAt(i);
			w = BitmapFont.m_bmFont.stringWidth(cm.caption);
			if (menuW < w)
				menuW = w;

		}
		// menuW += (int) (60 / HDCGameMidlet.instance.scale);
		// menuW = (menuW / PaintPopup.w_hBoxButton) * PaintPopup.w_hBoxButton
		// + (int) (30 / HDCGameMidlet.instance.scale);

		// menuH = vCommand.size() * (int) (60 / HDCGameMidlet.instance.scale);
		// menuH = (menuH / PaintPopup.w_hBoxButton) * PaintPopup.w_hBoxButton
		// + (int) (30 / HDCGameMidlet.instance.scale);

		col = 12;
		row = vCommand.size() * 3;
		imgBackground = PaintPopup.gI().createImgBackground_Popup(col, row);

		menuW = imgBackground.getWidth();
		menuH = imgBackground.getHeight();

		if (x + menuW > GameCanvas.w)
			x = GameCanvas.w - menuW;
		if (y + menuH > GameCanvas.h)
			y = GameCanvas.h - menuH;
		focus = 0;

		// TODO set location Y
		y = (GameCanvas.h - imgBackground.getHeight()) / 2;
	}

	public static void updateKey() {
		if (!GameCanvas.isPointerClick)
			return;
		if (vCommand.size() == 1) {
			((Command) vCommand.elementAt(0)).action.perform();
			closeMenuNhanh();
			return;
		}
		// if (GameCanvas.isPointer(0, GameCanvas.h
		// - GameResource.instance.imgExit.getHeight(),
		// GameResource.instance.imgExit.getWidth(),
		// GameResource.instance.imgExit.getHeight())) {
		// closeMenuNhanh();
		// }
		int yy = y /* + (int) (12 / HDCGameMidlet.instance.scale) */;
		if (/* !GameCanvas.instance.isMove() */Math.abs(GameCanvas.instance.pyLast
				- GameCanvas.instance.py) < 5) {
			Command c;
			for (byte i = 0; i < vCommand.size(); i++) {
				c = (Command) vCommand.elementAt(i);
				if (GameCanvas.isPointer(x + 5 / HDCGameMidlet.instance.scale, yy
						+ (i * 60 / HDCGameMidlet.instance.scale), menuW - 10
						/ HDCGameMidlet.instance.scale, 60 / HDCGameMidlet.instance.scale)) {
					if (focus == i) {
						c.action.perform();
						closeMenuNhanh();
					} else
						focus = i;

				}
			}
		}

	}

	public static void closeMenuNhanh() {
		showMenuNhanh = false;
		vCommand.removeAllElements();

	}

	public static void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.setAlpha(120);
		g.fillRectWithTransparent(0, 0, GameCanvas.w, GameCanvas.h);

		if (vCommand.size() == 1)
			return;
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		// PaintPopup.paintBoxButtton(g, x, y, menuW / PaintPopup.w_hBoxButton,
		// menuH / PaintPopup.w_hBoxButton, 0);

		// TODO paint background
		g.drawImage(imgBackground, x, y, Graphics.LEFT | Graphics.TOP);

		int yy = y /* + (int) (12 / HDCGameMidlet.instance.scale) */;
		Command c;
		for (byte i = 0; i < vCommand.size(); i++) {
			c = (Command) vCommand.elementAt(i);
			// if (focus == i)
			// PaintPopup.paintSelect(g,
			// x + (int) (5 / HDCGameMidlet.instance.scale), yy, menuW
			// - (int) (10 / HDCGameMidlet.instance.scale),
			// (int) (60 / HDCGameMidlet.instance.scale));

			if (focus == i) {
				g.setColor(0x000000);
				g.setAlpha(120);
				g.fillRoundRectWithTransparenr(x + (int) (10 / HDCGameMidlet.instance.scale), yy,
						imgBackground.getWidth() - (int) (20 / HDCGameMidlet.instance.scale),
						imgBackground.getHeight() / vCommand.size(),
						(int) (8 / HDCGameMidlet.instance.scale),
						(int) (8 / HDCGameMidlet.instance.scale));

				BitmapFont.drawItalicFont(g, c.caption, x + 30 / HDCGameMidlet.instance.scale, yy
						+ (60 / HDCGameMidlet.instance.scale) / 2, 0xffb901, Graphics.LEFT
						| Graphics.VCENTER);
			} else {
				// draw title menu
				BitmapFont.drawBoldFont(g, c.caption, x + 30 / HDCGameMidlet.instance.scale, yy
						+ (60 / HDCGameMidlet.instance.scale) / 2, 0xcccccc, Graphics.LEFT
						| Graphics.VCENTER);
			}

			yy += 60 / HDCGameMidlet.instance.scale;
		}
	}

}
