package com.hdc.mycasino.model;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.animation.TextAnimation;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.utilities.GameResource;

public class Button {
	// public Button instance;
	private int status;// trạng thái 1: enable, 0: disable
	public int x, y, w, col;
	private String name;
	private Command cmd;
	private int hButton, wButton;
	public Image imgButton;
	public int anchors;
	// TODO
	// focus = 0 : chưa focus vào button
	// focus = 1 : focus vào button
	public int focus;
	public Image imgBackground;
	// move for hight light text
	public int move = 0;
	TextAnimation m_TextAnimation;
	public int count = 0;
	public int flagPointerDown = 0;

	// int opacity = 100;

	// TODO khởi tạo button cách 1
	public Button() {
		// instance = this;
	}

	// TODO khởi tạo button cách 2
	public Button(Image img, String name, Command cmd) {
		// instance = this;
		this.name = name;
		this.cmd = cmd;

		// set image
		imgButton = img;
		// chiều cao button
		hButton = imgButton.getHeight();
		// chiều dài button
		wButton = imgButton.getWidth() / 3;

		col = 0;
		col = (int) ((BitmapFont.m_bmNormalFont.charsWidth(name.toCharArray(), 0, name.length())))
				/ (wButton);

		if (GameCanvas.w < 800 && GameCanvas.h <= 480) {
			if (HDCGameMidlet.instance.scale == 2)
				col += 1;
			else
				col += 2;
		} else
			col += 3;

		if (col == 1)
			col++;

		w = col * wButton;

		// set focus for button
		this.focus = 0;

		// set image baclground
		this.imgBackground = createImageButton();

		// init text effect
		m_TextAnimation = new TextAnimation();
	}

	// TODO tính toán lại Column cho button
	public void Calculate_Col(String name) {
		col = (int) ((BitmapFont.m_bmNormalFont.charsWidth(name.toCharArray(), 0, name.length())))
				/ (wButton);

		if (GameCanvas.w <= 800 && GameCanvas.h < 480)
			col += 1;
		else
			col += 3;

		if (col == 1)
			col++;

		w = col * wButton;

		// set image baclground
		this.imgBackground = createImageButton();
	}

	// TODO cài đặt lại tọa độ X và Y
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// TODO cài đặt command cho button
	public void setCommand(Command cmd) {
		this.cmd = cmd;
	}

	// TODO start effect for button
	public void startEffect() {
		if (GameCanvas.w > 320 && GameCanvas.h > 240) {
			count++;
			if (count == 1) {
				if (GameCanvas.m_arrEffect != null) {
					GameCanvas.m_arrEffect.removeAllElements();
				}
				m_TextAnimation.startEffect(name,
						x + (w - BitmapFont.m_bmNormalFont.stringWidth(name)) / 2
								- GameResource.instance.imgHightLight_3.getWidth(), y,
						GameResource.instance.imgHightLight_3);
			}
		}
	}

	// TODO stop effect
	public void stopEffect() {
		if (GameCanvas.m_arrEffect != null) {
			count = 0;
			GameCanvas.m_arrEffect.removeAllElements();
		}
	}

	// TODO update key for button
	public void updateKey() {
		// nếu focus = 1 thì xử lý
		if (focus == 1) {
			if (cmd != null) {
				// flagPointerDown = 0;
				m_TextAnimation.removeEffect();
				cmd.action.perform();
			}
			focus = 0;
		}

		// if (GameCanvas.isPointerMove) {
		// if (GameCanvas.isPointer_Down(x, y, w, hButton)) {
		// flagPointerDown = 1;
		// }
		// }

		if (GameCanvas.isPointerClick) {
			if (GameCanvas.isPointer(x, y, w, hButton)) {
				// play sound
				HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.click);
				HDCGameMidlet.sound.play();
				if (focus == 0) {
					focus = 1;
					m_TextAnimation.removeEffect();
					if (GameCanvas.currentDialog != null) {
						if (GameCanvas.currentScreen.mBt_Defaut != null) {
							GameCanvas.currentScreen.mBt_Defaut.count = 0;
							GameCanvas.currentScreen.mBt_Defaut.startEffect();
						}
					}
				}
			}
		}

	}

	// TODO paint button
	public void paint(Graphics g) {

		if (focus == 0)
			// paintButton(g, x, y);
			g.drawImage(imgBackground, x, y, Graphics.LEFT | Graphics.TOP);
		else
			// paintButton(g, x + 1, y - 1);
			// if (GameCanvas.isPointerDown) {
			// g.fillRectWithTransparent(x + 1, y - 1, imgBackground.getWidth(),
			// imgBackground.getHeight(), 0x00CED1);
			// }
			g.drawImage(imgBackground, x + 1, y - 1, Graphics.LEFT | Graphics.TOP);

		// if (flagPointerDown == 0) {
		g.drawImage(GameResource.instance.imgButton_HighLight, x + w / 2,
				y + imgButton.getHeight(), Graphics.HCENTER | Graphics.TOP);

		// }

	}

	// TODO create image button
	@SuppressWarnings("static-access")
	private Image createImageButton() {
		// enable
		Image mFrame = null;
		mFrame = this.imgButton;

		// create image
		Image imgTemp = Image.createImage(imgButton.getWidth() * col, imgButton.getHeight());
		// create graphics
		Graphics g1 = imgTemp.getGraphics();

		int x = 0;
		int y = 0;

		// if (GameCanvas.instance.currentDialog == null) {
		// ráp hình button từ column
		for (int i = 0; i < col; i++) {
			if (i == 0) {
				g1.drawRegion(mFrame, 0, 0, this.wButton, mFrame.getHeight(), 0, x, y, 0);
			} else if (i == col - 1) {
				g1.drawRegion(mFrame, this.wButton * 2, 0, this.wButton, mFrame.getHeight(), 0, x
						+ i * this.wButton, y, 0);
			} else {
				g1.drawRegion(mFrame, this.wButton, 0, this.wButton, mFrame.getHeight(), 0, x + i
						* this.wButton, y, 0);
			}
		}
		// } else {
		// if (!GameCanvas.instance.msgdlg.isWaiting) {
		// for (int i = 0; i < col; i++) {
		// if (i == 0) {
		// g1.drawRegion(mFrame, 0, 0, this.wButton,
		// mFrame.getHeight(), 0, x, y, 0);
		// } else if (i == col - 1) {
		// g1.drawRegion(mFrame, this.wButton * 2, 0,
		// this.wButton, mFrame.getHeight(), 0, x + i
		// * this.wButton, y, 0);
		// } else {
		// g1.drawRegion(mFrame, this.wButton, 0, this.wButton,
		// mFrame.getHeight(), 0, x + i * this.wButton, y,
		// 0);
		// }
		// }
		// }
		// }

		// vẽ text lên button
		BitmapFont.drawItalicFont(g1, this.name, x + (col * this.wButton) / 2,
				y + this.hButton / 2, 0x828483, Graphics.VCENTER | Graphics.HCENTER);

		return imgTemp;
	}

}
