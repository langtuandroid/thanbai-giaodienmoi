package com.hdc.mycasino.screen;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Button;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.TField;

public class InputScr extends Screen {

	private static InputScr instance;

	public static InputScr gI() {
		return (instance == null ? instance = new InputScr() : instance);
	}

	private TField[] list;
	private String[] name;
	public int selected = 0;
	private Screen lastScr;
	public String title, subTitle;

	// TODO image background for input dialog
	private Image imgBackground;
	private int col; // cột background
	private int row; // dòng background
	// title
	// private String m_title;
	// TODO button
	Button mButton_Agree;
	Button mButton_Disagree;

	@SuppressWarnings("static-access")
	public void setInfo(TField[] lis, String[] name, Command cmdCenter, Command cmdLeft,
			String title, String subTitle) {
		m_cmdCenter = cmdCenter;
		m_cmdRight = cmdLeft;

		this.title = title;
		this.subTitle = subTitle;
		this.name = name;
		m_height = 40 + lis.length * 45;
		m_width = 160;
		if (GameCanvas.w <= 128) {
			m_width = 100;
		}
		list = lis;
		selected = 0;
		for (int i = 0; i < list.length; i++) {
			list[i].width = m_width - 30;
			list[i].height = ITEM_HEIGHT + 2;
			list[i].y = GameCanvas.hh - m_height / 2 + 60 + i * 45;
			list[i].x = GameCanvas.hw - (list[i].width) / 2;
		}
		list[0].isFocus = true;
		// m_cmdRight = list[0].cmdClear;

		// TODO create image background
		col = GameResource.instance.imgTextField_Disable.getWidth()
				/ GameResource.instance.imgPoupPanel.getHeight() + 6;
		row = GameResource.instance.imgTextField_Disable.getHeight() * (list.length * 2 + 1)
				/ GameResource.instance.imgPoupPanel.getHeight() + 1;
		imgBackground = PaintPopup.gI().createImgBackground_Popup(col, row);
		Graphics g1 = imgBackground.getGraphics();
		for (int i = 0; i < col; i++) {
			g1.drawImage(GameResource.instance.imgPopupLine,
					i * GameResource.instance.imgPopupLine.getWidth(),
					GameResource.instance.imgPoupPanel.getHeight() / 2 * 3, Graphics.LEFT
							| Graphics.TOP);
		}

		// TODO paint title
		BitmapFont.m_bmNormalFont.drawNormalFont(g1, this.subTitle, imgBackground.getWidth() / 2,
				(10 / HDCGameMidlet.instance.scale), 0xffffff, Graphics.HCENTER | Graphics.TOP);

		// TODO set location
		// left - top
		setLocation();

		// TODO set textfield
		setTextField();

		// TODO create button
		createButton();
	}

	// TODO set textfield
	private void setTextField() {
		for (int i = 0; i < list.length; i++) {
			list[i].x = m_left
					+ (imgBackground.getWidth() - GameResource.instance.imgTextField_Disable
							.getWidth()) / 2;
			list[i].y = m_top + GameResource.instance.imgPoupPanel.getHeight() * (4 + i * 4);
			list[i].width = GameResource.instance.imgTextField_Disable.getWidth();
			list[i].height = GameResource.instance.imgTextField_Disable.getHeight();

		}
	}

	// TODO set Left & Top for dialog
	private void setLocation() {
		m_left = (GameCanvas.w - imgBackground.getWidth()) / 2;
		m_top = (GameCanvas.h - imgBackground.getHeight()) / 2;
	}

	// TODO create button
	// yes - no
	private void createButton() {

		// int m_height = GameResource.instance.imgTextField_Disable.getHeight()
		// * (list.length * 2);

		int m_height = imgBackground.getHeight()
				- (GameResource.instance.imgPoupButton.getHeight() / 4 * 5);

		mButton_Agree = new Button(GameResource.instance.imgPoupButton, GameResource.accept,
				m_cmdCenter);

		mButton_Disagree = new Button(GameResource.instance.imgPoupButton, GameResource.no,
				m_cmdRight);

		// // chọn chiều dài chữ dài nhất làm chuẩn cho 2 button
		mButton_Disagree.Calculate_Col(GameResource.accept);

		int w1 = mButton_Agree.w;
		int w2 = imgBackground.getWidth() / 2;

		// set vị trí button yes
		mButton_Agree.setXY(m_left + (w2 - w1) / 2, m_top + m_height);

		// set vị trí button no
		mButton_Disagree.setXY(m_left + w2 + (w2 - w1) / 2, m_top + m_height);

		mButton_Agree.startEffect();
	}

	public void update() {
		for (int i = 0; i < list.length; i++) {
			list[i].update();
		}
		lastScr.update();
	}

	public void keyPress(int keyCode) {
		// for (int i = 0; i < list.length; i++) {
		// if (list[i].isFocus) {
		// list[i].keyPressed(keyCode);
		// break;
		// }
		// }
		super.keyPress(keyCode);
	}

	public void updateKey() {

		if (mButton_Agree != null)
			mButton_Agree.updateKey();
		if (mButton_Disagree != null)
			mButton_Disagree.updateKey();

		if (GameCanvas.isPointerClick) {
			if (GameCanvas.isPointer(list[0].x, list[0].y, list[0].width, list[0].height)) {
				list[0].isFocus = true;
				list[1].isFocus = false;
			}
			if (GameCanvas.isPointer(list[1].x, list[1].y, list[1].width, list[1].height)) {
				list[1].isFocus = true;
				list[0].isFocus = false;
			}
		}

		if (GameCanvas.keyPressed[8]) {
			GameCanvas.keyPressed[8] = false;
			selected++;
			if (selected >= list.length) {
				selected = 0;
			}
			setFocus();
		} else if (GameCanvas.keyPressed[2]) {
			GameCanvas.keyPressed[2] = false;
			selected--;
			if (selected < 0) {
				selected = list.length - 1;
			}
			setFocus();
		} else if (GameCanvas.keyPressed[4]) {
			GameCanvas.keyPressed[4] = false;
		} else if (GameCanvas.keyPressed[6]) {
			GameCanvas.keyPressed[6] = false;
		}
		super.updateKey();
	}

	private void setFocus() {
		for (int i = 0; i < list.length; i++) {
			if (list[i].isFocus) {
				list[i].isFocus = false;
				break;
			}
		}
		list[selected].isFocus = true;
		// m_cmdRight = list[selected].cmdClear;
	}

	@SuppressWarnings("static-access")
	public void paint(Graphics g) {
		lastScr.paint(g);
		g.translate(-g.getTranslateX(), -g.getTranslateY());
		g.setColor(Color.BLACK);
		g.setAlpha(120);
		g.fillRectWithTransparent(0, 0, GameCanvas.w, GameCanvas.h);
		// TODO paint background
		g.drawImage(imgBackground, m_left, m_top, Graphics.TOP | Graphics.LEFT);

		if (mButton_Agree != null)
			mButton_Agree.paint(g);
		if (mButton_Disagree != null)
			mButton_Disagree.paint(g);

		// TODO paint text
		for (int i = 0; i < list.length; i++) {
			BitmapFont.m_bmNormalFont.drawNormalFont(g, name[i], list[i].x, list[i].y, 0xffffff,
					Graphics.LEFT | Graphics.BOTTOM);
		}

		// TODO paint text field
		for (int i = 0; i < list.length; i++) {
			list[i].paint(g);
		}

		// PaintPopup.paintInputScrBG(g, GameCanvas.hw - m_width / 2 - 1,
		// GameCanvas.h / 2 - m_height / 2 - 1, m_width + 2, m_height + 2,
		// title, subTitle);
		// for (int i = 0; i < list.length; i++) {
		// g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		// BitmapFont.drawNormalFont(g, name[i], list[i].x, list[i].y - 15,
		// 0xffffff, Graphics.LEFT|Graphics.TOP);
		//
		// list[i].paint(g);
		// }
		super.paint(g);
	}

	public void switchToMe(Screen mainMenuScr) {
		lastScr = mainMenuScr;
		super.switchToMe();
	}

	public void close() {
		lastScr.switchToMe();
		list = null;
		name = null;
		m_cmdCenter = null;
		lastScr = null;
		GameCanvas.instance.m_arrEffect.removeAllElements();
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		close();
	}
}
