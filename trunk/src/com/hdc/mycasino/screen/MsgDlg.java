package com.hdc.mycasino.screen;

import java.util.Vector;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Button;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.Position;
import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.GameResource;

public class MsgDlg extends Dialog {
	// public boolean isShowText = false;
	public String[] info;
	@SuppressWarnings("rawtypes")
	public Vector list = new Vector();

	public int index = 0, m_width, m_height, m_left, m_top;
	public boolean isWaiting = false;
	public boolean isWaitingDialog = false;
	public boolean isNoClose = false;
	public boolean isPaintScore = false;

	public static int widthInfoEndGame[] = new int[4];
	int size = 0;
	// int posBold = 0;
	public Position delX;
	boolean m_bIsDetail = false;

	// TODO button
	// nếu có 1 button "OK"
	Button mButton_OK;
	// nếu có 2 button (ví dụ : "Có" hoặc "Không")
	Button mButton_Yes;
	Button mButton_No;
	// nếu là trạng thái "Xin chờ
	Button mButton_Waitting;
	int move = 20;

	public MsgDlg() {
		delX = new Position(GameCanvas.w / 2, GameCanvas.h / 2);
		// isShowText = true;
	}

	public void resetPositionTo() {
		delX.setPositionTo(0, 0);
	}

	public void show() {
		GameCanvas.currentDialog = this;
		// isShowText = true;
	}

	int m_iInterval = 0;
	boolean m_bEnableInterval = false;

	// TODO background cho msgDlg
	public Image imgBackground;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setInfo(String info, int interval, Command yes, Command no) {
		init(info, false, GameCanvas.w3d4);
		this.center = yes;

		Vector tmplist = new Vector();
		tmplist.addElement(yes);
		if (no != null)
			tmplist.addElement(no);
		CRes.cleanVector(this.list);
		this.list = tmplist;

		Command cmd = (Command) tmplist.elementAt(index);
		center.action = cmd.action;

		m_iCurrentTime = 0;
		m_iInterval = interval;
		m_bEnableInterval = true;

		size = this.list.size();

		m_height += 20;
		tmplist = null;
		cmd = null;
		move = 0;

		// int col = BitmapFont.m_bmNormalFont.stringWidth(info)
		// / GameResource.instance.imgPoupPanel.getHeight() + 4;

		imgBackground = PaintPopup.gI().createImgBackground_Popup(
				(GameCanvas.w - m_left * 4) / GameResource.instance.imgPoupPanel.getHeight(),
				(m_height / GameResource.instance.imgPoupPanel.getHeight()) + 5);

		m_left = m_left * 2;

		// m_left = (GameCanvas.w - col
		// * GameResource.instance.imgPoupPanel.getHeight()) / 2;
		// TODO set location
		setLocation();

		if (!isWaiting)
			createButton();

		delX.setPosition(0, 0);
		delX.setPositionTo(imgBackground.getWidth() / 2, imgBackground.getHeight() / 2);
		// delX.setPositionTo(GameCanvas.w / 2, GameCanvas.h / 2);
		this.info = BitmapFont.splitString(info,
				(int) (imgBackground.getWidth() - (20 / HDCGameMidlet.instance.scale)), false);

	}

	public void setWaitInfo(String info, Command center, Vector list) {
		init(info, true, GameCanvas.w3d4);

		this.center = null;
		this.center = center;
		index = 0;
		if (list != null) {
			CRes.cleanVector(this.list);
			this.list = list;
			Command cmd = (Command) list.elementAt(index);
			center.action = cmd.action;
		} else {
			this.list.removeAllElements();
		}
		size = this.list.size();

		m_iCurrentTime = 0;
		m_iInterval = 100;
		move = 0;

		// createImageBackground((GameCanvas.w - m_left * 2)
		// / GameResource.instance.imgPoupPanel.getHeight(),
		// (m_height / GameResource.instance.imgPoupPanel.getHeight()) + 3);
		// createButton();
		this.info = BitmapFont.splitString(info,
				(int) (imgBackground.getWidth() - (20 / HDCGameMidlet.instance.scale)), false);
		// TODO set location
		setLocation();

	}

	public void setInfo(String info, Command center, Vector list) {
		init(info, false, GameCanvas.w3d4);

		this.center = null;
		this.center = center;
		index = 0;
		if (list != null) {
			CRes.cleanVector(this.list);
			this.list = list;
			Command cmd = (Command) list.elementAt(index);
			center.action = cmd.action;
		} else {
			this.list.removeAllElements();
		}
		size = this.list.size();

		m_iCurrentTime = 0;
		m_iInterval = 100;
		move = 0;

		// int col = BitmapFont.m_bmNormalFont.stringWidth(info)
		// / GameResource.instance.imgPoupPanel.getHeight() + 4;

		imgBackground = PaintPopup.gI().createImgBackground_Popup(
				(GameCanvas.w - m_left * 4) / GameResource.instance.imgPoupPanel.getHeight(),
				(m_height / GameResource.instance.imgPoupPanel.getHeight()) + 5);
		m_left = m_left * 2;

		// m_left = (GameCanvas.w - col
		// * GameResource.instance.imgPoupPanel.getHeight()) / 2;
		// TODO set location
		setLocation();

		if (!isWaiting) {
			createButton();
		} else {
			createButtonWaitting();
		}

		delX.setPosition(0, 0);
		delX.setPositionTo(imgBackground.getWidth() / 2, imgBackground.getHeight() / 2);

		this.info = BitmapFont.splitString(info,
				(int) (imgBackground.getWidth() - (20 / HDCGameMidlet.instance.scale)), false);

	}

	public void setInfo(String[] info, Command center, Vector list, int pos) {
		m_width = GameCanvas.w;
		isPaintScore = false;

		this.info = info;
		m_height = (this.info.length / 4 + 1) * Screen.ITEM_HEIGHT + 30;

		if (m_height < 55)
			m_height = 55;
		m_left = GameCanvas.hw - m_width / 2;
		m_top = GameCanvas.hh - m_height / 2;

		// /////////////////
		m_bIsDetail = false;
		isPaintScore = true;

		this.center = null;
		this.center = center;
		index = 0;
		if (list != null) {
			CRes.cleanVector(this.list);
			this.list = list;
			Command cmd = (Command) list.elementAt(index);
			center.action = cmd.action;
		} else {
			this.list.removeAllElements();
		}
		size = this.list.size();

		m_iCurrentTime = 0;
		m_iInterval = 100;
		move = 0;

		// int col = BitmapFont.m_bmNormalFont.stringWidth(info)
		// / GameResource.instance.imgPoupPanel.getHeight() + 2;

		imgBackground = PaintPopup.gI().createImgBackground_Popup(
				(GameCanvas.w - m_left * 4) / GameResource.instance.imgPoupPanel.getHeight(),
				(m_height / GameResource.instance.imgPoupPanel.getHeight()) + 4);
		m_left = m_left * 2;
		// m_left = (GameCanvas.w -
		// col*GameResource.instance.imgPoupPanel.getHeight())/2;

		if (!isWaiting) {
			createButton();
		}

		delX.setPosition(0, 0);
		delX.setPositionTo(imgBackground.getWidth() / 2, imgBackground.getHeight() / 2);

	}

	public void setDetailInfo(String info, Command center, Vector list) {
		init(info, false, GameCanvas.w - 10);
		m_bIsDetail = true;
		m_height = this.info.length * 20 + 60;

		this.center = null;
		this.center = center;
		index = 0;
		if (list != null) {
			CRes.cleanVector(this.list);
			this.list = list;
			Command cmd = (Command) list.elementAt(index);
			center.action = cmd.action;
		} else {
			this.list.removeAllElements();
		}
		size = this.list.size();

		m_iCurrentTime = 0;
		m_iInterval = 100;
		move = 0;

		imgBackground = PaintPopup.gI().createImgBackground_Popup(
				(GameCanvas.w - m_left * 4) / GameResource.instance.imgPoupPanel.getHeight(),
				(m_height / GameResource.instance.imgPoupPanel.getHeight()) + 4);
		m_left = m_left * 2;
		// TODO set location
		setLocation();

		if (!isWaiting) {
			createButton();
		}

		delX.setPosition(0, 0);
		delX.setPositionTo(imgBackground.getWidth() / 2, imgBackground.getHeight() / 2);
		// delX.setPositionTo(GameCanvas.w / 2, GameCanvas.h / 2);

		this.info = BitmapFont.splitString(info,
				(int) (imgBackground.getWidth() - (20 / HDCGameMidlet.instance.scale)), false);
	}

	// TODO set location for list item
	// m_left & m_top
	private void setLocation() {
		m_left = (GameCanvas.w - imgBackground.getWidth()) / 2;
		m_top = (int) (GameCanvas.h - imgBackground.getHeight()) / 2;

	}

	public void init(String info, boolean isWaiting, int width) {
		m_width = width;
		// isShowText = true;
		isPaintScore = false;

		this.info = BitmapFont.splitString(info, m_width, false);
		m_height = this.info.length * Screen.ITEM_HEIGHT + 30;

		if (m_height < 55)
			m_height = 55;
		m_left = GameCanvas.hw - m_width / 2;
		m_top = GameCanvas.hh - m_height / 2;

		delX.setPosition(0, 0);
		if (isWaiting)
			delX.setPosition(m_width / 2, 0);

		delX.setPositionTo(m_width / 2, 0);
		m_bIsDetail = false;
		m_bEnableInterval = false;
	}

	private void paintNormal(Graphics g) {
		int tmp = 0;

		if (!isWaiting) {

			g.translate(m_left, m_top);
			g.setClip(imgBackground.getWidth() / 2 - delX.x,
					imgBackground.getHeight() / 2 - delX.y, delX.x * 2, delX.y * 2);

			// paint background for popup
			g.drawImage(imgBackground, 0, 0, Graphics.LEFT | Graphics.TOP);
			g.translate(-g.getTranslateX(), -g.getTranslateY());
		}

		if (isWaiting) {
			GameResource.instance.m_frameWaiting.drawSeriFrame(
					(GameCanvas.w - GameResource.instance.m_frameWaiting.frameWidth) / 2,
					(GameCanvas.h - GameResource.instance.m_frameWaiting.frameHeight) / 2,
					Graphics.LEFT | Graphics.TOP, g);
		}

		tmp = m_top + Screen.HAFT_ITEM_HEIGHT;
		int a = info.length;
		int i;
		if (!isWaiting) {
			if (m_bIsDetail) {
				for (i = 0; i < info.length; i++) {
					if (info[i].length() > 0) {
						BitmapFont.drawItalicFont(g, info[i], 20, tmp, 0xffb901, Graphics.LEFT
								| Graphics.TOP);
						tmp += Screen.ITEM_HEIGHT;
					}
				}
			} else {
				for (i = 0; i < a; i++) {
					if (info[i].length() > 0) {
						BitmapFont.drawItalicFont(g, info[i], GameCanvas.hw, tmp, 0xffb901,
								Graphics.HCENTER | Graphics.TOP);
						tmp += Screen.ITEM_HEIGHT;
					}
				}
			}
		}

		if (m_bEnableInterval) {
			if (m_iInterval >= 0) {
				BitmapFont.drawItalicFont(g, "" + m_iInterval, GameCanvas.hw, tmp, 0xffb901,
						Graphics.HCENTER);
				tmp += Screen.ITEM_HEIGHT;
			}
		}

		// paint line
		if (!isWaiting) {
			tmp += (Screen.ITEM_HEIGHT / 4);
			int col = imgBackground.getWidth() / GameResource.instance.imgPoupPanel.getHeight();
			for (i = 0; i < col; i++) {
				g.drawImage(GameResource.instance.imgPopupLine, m_left + i
						* GameResource.instance.imgPopupLine.getWidth(), tmp, Graphics.LEFT
						| Graphics.TOP);
			}
		}

	}

	// private void paintScore(Graphics g) {
	// int h = info.length / widthInfoEndGame.length;
	// PaintPopup.paintRoundRect(g, m_left, m_top, m_width, m_height, 5,
	// 0x113e18);
	// int m_top = this.m_top + 7;
	// g.setColor(0xaa6600);
	// h = (h + 1) * Screen.ITEM_HEIGHT;
	// g.drawRect(m_left, m_top, m_width - 1, h);
	//
	// int i = 0, j;
	// for (j = 0; j < widthInfoEndGame.length; j++) {
	// i += widthInfoEndGame[j];
	// g.drawLine(m_left + i, m_top, m_left + i, m_top + h);
	// }
	//
	// h = info.length / widthInfoEndGame.length;
	// int tmp = 2;
	// int color = 0xaa6600;
	// for (i = -1; i < h; i++) {
	// g.setColor(0xaa6600);
	// g.drawLine(m_left, m_top + Screen.ITEM_HEIGHT * (i + 1), m_width,
	// m_top + Screen.ITEM_HEIGHT * (i + 1));
	// tmp = 2;
	// for (j = 0; j < widthInfoEndGame.length; j++) {
	// if (i == -1) {
	// BitmapFont.drawBoldFont(g, GameResource.infoEndGame[j], tmp
	// + (widthInfoEndGame[j] >> 1), m_top
	// + Screen.ITEM_HEIGHT * (i + 1) + Screen.ITEM_HEIGHT
	// / 2, 0xffb901, Graphics.HCENTER | Graphics.VCENTER);
	// g.setColor(0xffbb00);
	// } else {
	// color = 0xffffff;
	// if ((i * 4 + j) % 4 == 2) {
	// try {
	// if (Integer.parseInt(info[i * 4 + j]) > 0)
	// color = 0x00ff00;
	// else if (Integer.parseInt(info[i * 4 + j]) == 0)
	// color = 0xffffff;
	// else
	// color = 0xff0000;
	// } catch (Exception e) {
	// color = 0xffffff;
	// }
	// }
	// if (posBold == i)
	// BitmapFont.drawBoldFont(g, info[i * 4 + j], tmp, m_top
	// + Screen.ITEM_HEIGHT * (i + 1) + 3, color,
	// Graphics.LEFT | Graphics.TOP);
	// else
	// BitmapFont.drawNormalFont(g, info[i * 4 + j], tmp,
	// m_top + Screen.ITEM_HEIGHT * (i + 1) + 3,
	// color, Graphics.LEFT | Graphics.TOP);
	// }
	// tmp += widthInfoEndGame[j];
	// }
	// }
	// }

	@SuppressWarnings("static-access")
	public void paint(Graphics g) {
		// if (isWaiting) {
		g.setColor(Color.BLACK);
		g.setAlpha(120);
		g.fillRectWithTransparent(0, 0, GameCanvas.w, GameCanvas.h);
		// }
		// if (isPaintScore)
		// paintScore(g);
		// else
		paintNormal(g);

		if (!isWaiting && center != null) {
			if (center.caption.equals("")) {
				if (list.size() == 1 && mButton_OK != null) {
					mButton_OK.paint(g);
				} else if (list.size() == 2) {
					mButton_Yes.paint(g);
					mButton_No.paint(g);
				}
			} else if (mButton_OK != null) {
				mButton_OK.paint(g);
			}
		} else {
			// mButton_Waitting.paint(g);

			BitmapFont.m_bmNormalFont.drawItalicFont(g, info[0],
					/*(GameCanvas.w - BitmapFont.m_bmNormalFont.stringWidth(info[0])) / 2*/GameCanvas.w/2,
					(GameCanvas.h - GameResource.instance.m_frameWaiting.frameHeight) / 2
							+ GameResource.instance.m_frameWaiting.frameHeight * 11 / 10, 0xffb901,
					Graphics.HCENTER | Graphics.TOP);
		}

		// int a, tmp;
		// if (size > 0 && isShowText) {
		// Command cmd = (Command) list.elementAt(index);
		// tmp = m_top + m_height - 20;
		//
		// BitmapFont.drawBoldFont(g, cmd.caption, GameCanvas.hw, tmp,
		// 0xffffff, Graphics.HCENTER | Graphics.TOP);
		// if (size > 1) {
		// a = BitmapFont.m_bmFont.stringWidth(cmd.caption);
		// PaintPopup.paintArrow(g, 0, GameCanvas.hw - a / 2 - 15, tmp,
		// 30 + a, deltaX);
		// deltaX = 2 - deltaX;
		// }
		// } else {
		// if (center != null && isShowText) {
		// tmp = m_top + m_height - 20;
		// BitmapFont.drawBoldFont(g, center.caption, GameCanvas.hw, tmp,
		// 0xffffff, Graphics.HCENTER | Graphics.TOP);
		// }
		// }

		// g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		// PaintPopup.paintCmdBar(g, null, null, null);

	}

	void moveRect(Graphics g, int x, int y, int w, int h) {
		g.setColor(0x99cc00);
		g.drawRect(x - delX.x, y, delX.x * 2, h);
		g.setColor(0x113e18);
		g.fillRect(x - delX.x + 1, y + 1, delX.x * 2 - 2, h - 2);

		g.drawRegion(GameResource.instance.imgIconCard, 9, 81, 16, 16, Sprite.TRANS_NONE, x
				+ delX.x - 8, y - 10, 0);
		g.drawRegion(GameResource.instance.imgIconCard, 9, 81, 16, 16, Sprite.TRANS_NONE, x
				+ delX.x - 8, y + h - 10, 0);
		g.drawRegion(GameResource.instance.imgIconCard, 9, 81, 16, 16, Sprite.TRANS_NONE, x
				- delX.x - 8, y - 10, 0);
		g.drawRegion(GameResource.instance.imgIconCard, 9, 81, 16, 16, Sprite.TRANS_NONE, x
				- delX.x - 8, y + h - 10, 0);

		g.setClip(x - delX.x, y, delX.x * 2, h);
	}

	private void setIndex(int in) {
		if (size > 0) {
			index += in;
			if (index < 0)
				index = size - 1;
			if (index >= size)
				index = 0;

			center.action = ((Command) list.elementAt(index)).action;
		}
	}

	public int m_iCurrentTime = 0;

	public void update() {
		if (move < (imgBackground.getWidth() / 2 - 15))
			move += 15;

		// TODO update key for button
		if (!isWaiting && center != null) {
			if (center.caption.equals("")) {
				if (list.size() == 1) {
					mButton_OK.updateKey();
				} else if (list.size() == 2) {
					mButton_Yes.updateKey();
					mButton_No.updateKey();
				}
			} else {
				mButton_OK.updateKey();
			}
			// GameCanvas.currentScreen.mBt_Defaut.setButtonDefaut(true);
		} else {
			// mButton_Waitting.updateKey();
		}

		delX.translate();
		if (delX.x == 0 && delX.xTo == 0) {
			GameCanvas.currentDialog = null;
		}
		if (delX.x == 0)
			GameCanvas.currentDialog = null;

		if (delX.y == 0 && delX.yTo == 0) {
			GameCanvas.currentDialog = null;
		}
		if (delX.y == 0)
			GameCanvas.currentDialog = null;

		if (m_bEnableInterval) {
			m_iCurrentTime++;
			if (m_iCurrentTime % 20 == 0) {
				m_iInterval--;
				if (m_iInterval == -1) {
					if (this.list != null) {
						Command cmd = (Command) this.list.lastElement();
						cmd.action.perform();
					}
				}
			}
		}

		if (GameCanvas.isPointerClick) {
			int ww = 0;
			if (list != null && list.size() > 0) {
				Command cmd = (Command) list.elementAt(index);
				ww = BitmapFont.m_bmFont.stringWidth(cmd.caption) + 17;
			} else if (center != null) {
				ww = BitmapFont.m_bmFont.stringWidth(center.caption) + 17;
			}

			int tmp = m_top + m_height - 20;
			if (center != null && GameCanvas.isPointer(GameCanvas.hw - (ww >> 1), tmp, ww, 20)) {
				center.action.perform();
			} else if (GameCanvas.isPointer(m_left, tmp, m_width, 20)) {
				int px = GameCanvas.hw - GameCanvas.px;
				if (px > (ww >> 1)) {
					setIndex(-1);
				} else if (px < -(ww >> 1))
					setIndex(1);
			}
		}

		super.update();
	}

	// TODO create button
	public void createButton() {
		m_height = imgBackground.getHeight() - GameResource.instance.imgPoupButton.getHeight() / 4
				* 5;
		if (center.caption.equals("")) {
			// nếu có 1 button
			if (list.size() == 1) {
				Command c = (Command) list.get(0);
				mButton_OK = new Button(GameResource.instance.imgPoupButton, c.caption, c);

				mButton_OK.setXY(m_left + (imgBackground.getWidth() - mButton_OK.w) / 2, m_top
						+ m_height);

//				mButton_OK.startEffect();

			} else if (list.size() == 2) {
				Command c = (Command) list.get(0);
				String caption_1 = c.caption;
				mButton_Yes = new Button(GameResource.instance.imgPoupButton, c.caption, c);
				c = (Command) list.get(1);
				String caption_2 = c.caption;
				mButton_No = new Button(GameResource.instance.imgPoupButton, c.caption, c);

				// chọn chiều dài chữ dài nhất làm chuẩn cho 2 button
				if (caption_1.length() > caption_2.length()) {
					mButton_No.Calculate_Col(caption_1);
				} else if (caption_1.length() < caption_2.length()) {
					mButton_Yes.Calculate_Col(caption_2);
				}

				int w1 = mButton_Yes.w;
				int w2 = imgBackground.getWidth() / 2;

				// set vị trí button yes
				mButton_Yes.setXY(m_left + (w2 - w1) / 2, m_top + m_height);

				// set vị trí button no
				mButton_No.setXY(m_left + w2 + (w2 - w1) / 2, m_top + m_height);

//				mButton_Yes.startEffect();
			}
		} else {
			mButton_OK = new Button(GameResource.instance.imgPoupButton, center.caption, center);

			mButton_OK.setXY(m_left + (imgBackground.getWidth() - mButton_OK.w) / 2, m_top
					+ m_height);

//			mButton_OK.startEffect();
		}
	}

	public void createButtonWaitting() {
		// nếu là trạng thái xin chờ
		if (isWaiting) {
			GameCanvas.m_arrEffect.removeAllElements();
		}
	}

	// @Override
	// public void doBack() {
	// // TODO Auto-generated method stub
	// GameCanvas.currentDialog = null;
	// GameCanvas.instance.m_arrEffect.removeAllElements();
	// }

}
