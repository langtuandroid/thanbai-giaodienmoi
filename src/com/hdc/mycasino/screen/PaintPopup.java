package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.utilities.GameResource;

public class PaintPopup {
	static PaintPopup instance;

	public static PaintPopup gI() {
		if (instance == null)
			instance = new PaintPopup();
		return instance;
	}

	private int m_h, x, y, numTab;
	public int focusTab, m_w, m_wTab, indexStart = 0, maxTab;
	public String name;

	public static void paintArrow(Graphics g, int index, int x, int y, int w, int deltaX) {
		Image img = GameResource.instance.imgArrow;
		g.drawRegion(img, 0, 0, img.getWidth(), img.getHeight(), Sprite.TRANS_NONE, x - deltaX - 5,
				y, 20);
		g.drawRegion(img, 0, 0, img.getWidth(), img.getHeight(), Sprite.TRANS_ROT180, x + w
				+ deltaX, y, 20);
	}

	public void setInfo(String na, int wp, int hp, int num) {
		name = na;
		m_w = wp;
		m_h = hp;
		numTab = num;
		m_wTab = BitmapFont.m_bmFont.stringWidth(na) + 10;

		if (m_wTab < 40)
			m_wTab = 40;
		x = GameCanvas.hw - m_w / 2;
		y = GameCanvas.hh - m_h / 2;
		focusTab = 0;

		maxTab = (m_w - m_wTab) / 10;
		indexStart = 0;
	}

	public void setNameAndFocus(String na, int focus) {
		name = na;
		int aa = BitmapFont.m_bmFont.stringWidth(name) + 10;

		if (aa > m_wTab) {
			m_wTab = aa;
			maxTab = (m_w - m_wTab) / 10;
		}
		focusTab = focus;
		if (focusTab >= maxTab && maxTab > 0) {
			indexStart = focusTab - (maxTab - 1);
		}
		if (focusTab < indexStart)
			indexStart = focusTab;
	}

	public void setNumTab(int num) {
		// colorTab=new int[num];
		numTab = num;
	}

	public static void paintInputScrBG(Graphics g, int x, int y, int w, int h, String title,
			String subTitle) {
		int delta = -25;
		if (subTitle != null && subTitle.length() > 0) {
			delta = 0;
		}
		g.setClip(x, y, w, h - delta);
		g.setColor(0x007733);

		g.fillRect(x, y, w - 1, h - 1 - delta);
		g.setColor(0xffb901);
		g.drawRect(x, y, w - 1, h - 1 - delta);
		if (subTitle != null && subTitle.length() > 0) {
			g.setColor(0xdcff6b);
			g.fillRect(x + 1, y + 25 - delta, w - 2, Screen.ITEM_HEIGHT);

			BitmapFont.drawNormalFont(g, subTitle, x + 10, y + 28 - delta, 0x000000, Graphics.LEFT
					| Graphics.TOP);
		}
		BitmapFont.drawBoldFont(g, title, x + 10, y + 3 - delta, 0xffffff, Graphics.LEFT
				| Graphics.TOP);
	}

	public static void paintBox(Graphics g, int x, int y, int w, int h, int lineHeight, int color1,
			int colorBoder) {
		g.setColor(colorBoder);
		g.fillRect(x, y, w, h);
		g.setColor(color1);
		g.fillRect(x + lineHeight, y + lineHeight, w - lineHeight * 2, h - lineHeight * 2);
	}

	public void paintTabs(Graphics g) {
		g.translate(-g.getTranslateX(), -g.getTranslateY());
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		paintRoundRect(g, x, y, m_w, m_h, 0x30725a);
		int nameLengPix = 0;
		int tmpW = 0;
		if (m_wTab > 45) {
			tmpW = m_wTab - 30;
			m_wTab = 40;
		}
		if (BitmapFont.m_bmNormalFont.stringWidth(name) >= 76 + tmpW)
			nameLengPix = BitmapFont.m_bmNormalFont.stringWidth(name) - (50 + tmpW);

		int ii = 0;
		for (int i = indexStart; i < focusTab; i++) {
			ii = i;

			PaintPopup.paintBox(g, x + 3 + ii * 10, y + 3, 30 + tmpW, 50, 15, 0x449977, 0x30725a);
			PaintPopup
					.paintRoundRect(g, x + 3 + ii * 10 + 1, y + 3 + 1, 60 + tmpW, 20, 0, 0x30725a);
		}
		int aa = numTab;
		if (aa >= maxTab)
			aa = maxTab + indexStart;
		int tmp;
		for (int i = aa - 1; i >= focusTab; i--) {
			ii = i - indexStart;
			tmp = x + 3 + m_wTab - 8 + ii * 10 - 7;
			if (i != focusTab) {
				PaintPopup.paintRoundRect(g, tmp - 20, y + 3, 71 + tmpW, 20, 0, 0x449977);
				PaintPopup.paintRoundRect(g, tmp - 20, y + 4, 70 + tmpW, 19, 0, 0x449977);
			}
			if (i == focusTab) {
				tmp = x + 3 + ii * 10;
				PaintPopup.paintRoundRect(g, tmp, y + 3, 76 + tmpW + nameLengPix, 20, 0, 0x255a47);
				PaintPopup.paintRoundRect(g, tmp + 1, y + 3 + 1, 74 + tmpW + nameLengPix, 20, 0,
						0x55bc93);
			}
		}

		paintRoundRect(g, x + 3, y + 18, m_w - 6, m_h - 21, 0x449977);
		paintRoundRect(g, x + 3, y + 18, 20, 20, 0, 0x449977);
		BitmapFont.drawBoldFont(g, name, x + 10, y + 6, 0xffffff, Graphics.LEFT | Graphics.TOP);

	}

	public static void paintRoundRect(Graphics g, int x, int y, int w, int h, int color1) {
		g.setColor(color1);
		g.fillRoundRect(x, y, w, h, 0, 0);
	}

	public static void paintRoundRect(Graphics g, int x, int y, int w, int h, int r, int color1) {
		g.setColor(color1);
		g.fillRoundRect(x, y, w, h, r, r);
	}

	public static void paintCell(Graphics g, int x, int y, int w) {
		g.setColor(0x6f0017);
		g.fillRect(x, y, w, w);
		g.setColor(0x449977);
		g.drawRect(x, y, w, w);
	}

	public static void paintCmdBar(Graphics g, Command left, Command center, Command right) {
		// g.setColor(0x143211);
		int y = GameCanvas.h - GameCanvas.hBottomBar;
		// g.fillRect(0, y, GameCanvas.w, GameCanvas.hBottomBar);

		int i;
		for (i = 0; i <= GameCanvas.w; i += GameResource.instance.imgMenuBar.getWidth()) {
			g.drawImage(GameResource.instance.imgMenuBar, i, GameCanvas.h - GameCanvas.hBottomBar,
					0);
		}

		g.setColor(0xab6501);
		g.drawLine(0, y + 1, GameCanvas.w, y + 1);

		g.setColor(0xfcb108);
		g.drawLine(0, y, GameCanvas.w, y);
		g.drawLine(0, y, GameCanvas.w, y);

		y += BitmapFont.m_bmFont.getHeight();
		if (left != null) {
			BitmapFont.drawBoldFont(g, left.caption, 5, y, 0xffb901, Graphics.LEFT
					| Graphics.VCENTER);
		}
		if (center != null) {
			BitmapFont.drawBoldFont(g, center.caption, GameCanvas.hw, y, 0xffb901, Graphics.HCENTER
					| Graphics.VCENTER);
		}
		if (right != null) {
			BitmapFont.drawBoldFont(g, right.caption, GameCanvas.w - 5, y, 0xffb901, Graphics.RIGHT
					| Graphics.VCENTER);
		}
	}

	// public static void paintInputTf(Graphics g, int color1, int color2, int
	// color3, int x, int y, int w, int h) {
	//
	// g.setColor(color1);
	// g.fillRect(x + 1, y + 1, w - 1, h - 1);
	// g.setColor(color2);
	// g.drawLine(x + 1, y + 1, w - 2 + x + 1, y + 1);
	// g.drawLine(x + 1, y + 1 + h - 2, w - 2 + x + 1, y + 1 + h - 2);
	// g.setColor(color3);
	// g.drawLine(x + 1, y, w - 2 + x + 1, y);
	// }

	// TODO paint textfield
	public void paintInputTf(Graphics g, int x, int y, boolean focus) {
		if (!focus)
			g.drawImage(GameResource.instance.imgTextField_Disable, x, y, Graphics.LEFT
					| Graphics.TOP);
		else
			g.drawImage(GameResource.instance.imgTextField_Enable, x, y, Graphics.LEFT
					| Graphics.TOP);

	}

	public static void paintTopBarMenu(Graphics g) {
		for (int i = 0; i <= GameCanvas.w; i += GameResource.instance.imgMenuBar.getWidth()) {
			g.drawRegion(GameResource.instance.imgMenuBar, 0, 0,
					GameResource.instance.imgMenuBar.getWidth(),
					GameResource.instance.imgMenuBar.getHeight(), Sprite.TRANS_ROT180, i, 0, 0);
		}

		g.setColor(0xfed440);
		g.drawLine(0, GameCanvas.hBottomBar, GameCanvas.w, GameCanvas.hBottomBar);
		g.setColor(0xa47b3d);
		g.drawLine(0, GameCanvas.hBottomBar + 1, GameCanvas.w, GameCanvas.hBottomBar + 1);
		g.setColor(0x224400);
		g.drawLine(0, GameCanvas.hBottomBar + 2, GameCanvas.w, GameCanvas.hBottomBar + 2);
		g.setColor(0x254a00);
		g.drawLine(0, GameCanvas.hBottomBar + 3, GameCanvas.w, GameCanvas.hBottomBar + 3);
		g.setColor(0x2d5a00);
		g.drawLine(0, GameCanvas.hBottomBar + 4, GameCanvas.w, GameCanvas.hBottomBar + 4);
	}

	public static void paintGirl(Graphics g, int x, int y, boolean eyes, byte mouth) {
		// TODO Auto-generated method stub
		g.drawImage(GameResource.instance.imgGirl, x, y, Graphics.LEFT | Graphics.BOTTOM);
		g.drawImage(GameResource.instance.imgHandGirl, x + 69, y - 31, Graphics.LEFT
				| Graphics.BOTTOM);
		if (eyes) {
			g.drawImage(GameResource.instance.imgEyeGirl, x + 34, y - 135, Graphics.LEFT
					| Graphics.BOTTOM);
		}
		if (mouth < 2) {
			GameResource.instance.m_frameMouthGirl.drawFrame(mouth, x + 37, y - 120,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.BOTTOM, g);
		}
	}

	// Draw khung tren
	public static void paintFrameTop(Graphics g) {
		if (GameResource.instance.imgFrames == null)
			return;
		g.drawRegion(GameResource.instance.imgFrames, 0, 0, 41, 31, Sprite.TRANS_NONE, 0, 0, 0);
		g.drawRegion(GameResource.instance.imgFrames, 0, 0, 41, 31, Sprite.TRANS_MIRROR,
				GameCanvas.w - 41, 0, 0);
	}

	// ve khung duoi
	public static void paintFrameBot(Graphics g) {
		if (GameResource.instance.imgFrames == null)
			return;
		g.drawRegion(GameResource.instance.imgFrames, 0, 31, 14, 15, Sprite.TRANS_NONE, 0,
				GameCanvas.h - GameCanvas.hBottomBar - 15, 0);
		g.drawRegion(GameResource.instance.imgFrames, 0, 31, 14, 15, Sprite.TRANS_MIRROR,
				GameCanvas.w - 14, GameCanvas.h - GameCanvas.hBottomBar - 15, 0);
		g.drawRegion(GameResource.instance.imgFrames, 13, 36, 37, 9, Sprite.TRANS_MIRROR,
				GameCanvas.w / 2 - 18, GameCanvas.h - GameCanvas.hBottomBar - 9, 0);
	}

	// ve top bar
	public static void paintTopBar(Graphics g, String strHeader, byte numTab, Vector vtTabTitle,
			byte tabFocus, boolean isFocus, int select) {
		int i;
		for (i = 0; i <= GameCanvas.w; i += GameResource.instance.imgMenuBar.getWidth()) {
			g.drawRegion(GameResource.instance.imgMenuBar, 0, 0,
					GameResource.instance.imgMenuBar.getWidth(),
					GameResource.instance.imgMenuBar.getHeight(), Sprite.TRANS_ROT180, i, 0, 0);
		}
		// Draw upper Line
		g.setColor(0xc8b72f);
		g.drawLine(0, GameCanvas.hBottomBar, GameCanvas.w, GameCanvas.hBottomBar);

		// draw tab
		int x = -1;
		int width = (GameCanvas.w + (numTab - 1)) / numTab;
		for (i = 0; i < numTab; i++) {
			g.setColor(0xa47b3d);
			g.drawRect(x, GameCanvas.hBottomBar + 1, width, 20);

			if (i == tabFocus) {
				if (isFocus && select <= -1)
					g.setColor(0xff0000);
				else
					g.setColor(0x094f31);
				g.drawRect(x + 1, GameCanvas.hBottomBar + 2, width - 2, 18);
				g.setColor(0x007639);
				g.fillRect(x + 2, GameCanvas.hBottomBar + 3, width - 3, 17);
				BitmapFont.drawNormalFont(g, (String) vtTabTitle.elementAt(i),
						((x << 1) + width) >> 1, GameCanvas.hBottomBar + 12, 0xffffff,
						Graphics.HCENTER | Graphics.VCENTER);
			} else {
				g.setColor(0x163610);
				g.drawRect(x + 1, GameCanvas.hBottomBar + 2, width - 2, 18);
				g.setColor(0x234501);
				g.fillRect(x + 2, GameCanvas.hBottomBar + 3, width - 3, 17);
				BitmapFont.drawNormalFont(g, (String) vtTabTitle.elementAt(i),
						((x << 1) + width) >> 1, GameCanvas.hBottomBar + 12, 0xffff00,
						Graphics.HCENTER | Graphics.VCENTER);
			}
			x += width;
		}
		g.setColor(0x264b00);
		g.drawLine(0, GameCanvas.hBottomBar + 22, GameCanvas.w, GameCanvas.hBottomBar + 22);
		BitmapFont.drawBoldFont(g, strHeader, (GameCanvas.w >> 1), (GameCanvas.hBottomBar >> 1),
				0xffb901, Graphics.HCENTER | Graphics.VCENTER);

	}

	/**
	 * Paint background with title of page.
	 */
	public static void paintBackground(Graphics g, String pageTitle) {
		g.translate(-g.getTranslateX(), -g.getTranslateY());
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		g.drawImage(Screen.m_imgBackground, 0, 0, 0);
	}

	public static void paintBar(Graphics g, long min, long max, long rank, int x, int y, int w) {
		g.setColor(0x5d7b00);
		g.fillRoundRect(x, y, w, 5, 5, 5);
		float param = (float) (rank * 100 / max);
		int tmp = (int) (param * w) / 100;

		g.setColor(0xfab81b);
		g.fillRoundRect(x, y, tmp, 5, 5, 5);

		g.setColor(0xff0000);
		g.fillRoundRect(x + tmp - 2, y + 3 - Screen.ITEM_HEIGHT / 3, 5, Screen.ITEM_HEIGHT * 2 / 3,
				5, 5);

		BitmapFont.drawNormalFont(g, "" + min, x, y - 15, 0xffff00, Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawNormalFont(g, "" + max, x + w, y - 15, 0xffff00, Graphics.RIGHT
				| Graphics.TOP);
	}

	// TODO paint background
	@SuppressWarnings("static-access")
	public static void paintBackGround(Graphics g) {
		Image img = GameResource.instance.imgBackRound;
		if ((GameCanvas.w > GameResource.instance.imgBackRound.getWidth() && GameCanvas.h > GameResource.instance.imgBackRound
				.getHeight())
				|| (GameCanvas.w > GameResource.instance.imgBackRound.getWidth())
				|| (GameCanvas.h > GameResource.instance.imgBackRound.getHeight())) {
			img = new Image(g.BitmapResize(GameResource.instance.imgBackRound.getBitmap(),
					GameCanvas.w, GameCanvas.h));
		}

		// paint background
		g.drawImage(img, 0, 0, Graphics.LEFT | Graphics.TOP);

		// if (GameCanvas.h > GameResource.instance.imgBackRound.getHeight()
		// || GameCanvas.w > GameResource.instance.imgBackRound.getWidth()) {
		//
		// int col = GameCanvas.w
		// / GameResource.instance.imgBackRound_2.getWidth();
		// int row = GameCanvas.h
		// / GameResource.instance.imgBackRound_2.getHeight();
		//
		// for (int i = 0; i < row; i++) {
		// for (int j = 0; i < col; j++) {
		// g.drawImage(
		// GameResource.instance.imgBackRound_2,
		// j * GameResource.instance.imgBackRound_2.getWidth(),
		// GameResource.instance.imgBackRound.getHeight()
		// + i
		// * GameResource.instance.imgBackRound_2
		// .getHeight(), Graphics.LEFT
		// | Graphics.TOP);
		// }
		// }
		//
		// }

		// paint thêm background
		// nếu background trên ko fill hết màn hình
		// g.drawImage(GameResource.instance.imgBackRound, 0,
		// GameResource.instance.imgBackRound.getHeight(), Graphics.LEFT
		// | Graphics.TOP);

	}

	// TODO create image background popup
	// TODO create Image background
	public Image createImgBackground_Popup(int col, int row) {
		int w = col * GameResource.instance.imgPoupPanel.getWidth() / 9;
		int h = row * GameResource.instance.imgPoupPanel.getHeight();
		Image imgBackground = Image.createImage(w, h);

		Graphics g1 = imgBackground.getGraphics();
		// create panel for popup
		PaintPopup.gI().paintPopup_Panel(g1, row, col);

		return imgBackground;
	}

	// TODO paint popup game
	// flag = 0 : button disable
	// flag = 1 : button enable
	public void paintButton_Game(Graphics g, int x, int y, int flag, String text) {
		// TODO paint image button
		GameResource.instance.m_frameTienLen_Button.drawFrame(flag, x, y, Sprite.TRANS_NONE,
				Graphics.LEFT | Graphics.TOP, g);
		// TODO paint text for button
		BitmapFont.setTextSize(14f / HDCGameMidlet.instance.scale);
		BitmapFont.drawBoldFont_1(g, text, x
				+ GameResource.instance.m_frameTienLen_Button.frameWidth / 2, y
				+ GameResource.instance.m_frameTienLen_Button.frameHeight / 2, 0xffb901,
				Graphics.HCENTER | Graphics.VCENTER);
	}

	// TODO paint Popup Panel
	public void paintPopup_Panel(Graphics g, int row, int col) {
		int idx = 0;
		int w = GameResource.instance.imgPoupPanel.getWidth() / 9;
		int h = GameResource.instance.imgPoupPanel.getHeight();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				// tính toán chỉ số của panel
				if (i == 0) {
					if (j > 0 && j < col - 2)
						idx = 1;
					if (j == col - 1)
						idx = 2;
				} else if (i < (row - 1) && i > 0) {
					if (j == 0)
						idx = 3;
					if (j > 0 && j < col - 2)
						idx = 4;
					if (j == col - 1)
						idx = 5;
				} else if (i == (row - 1)) {
					if (j == 0)
						idx = 6;
					if (j > 0 && j < col - 2)
						idx = 7;
					if (j == col - 1)
						idx = 8;
				}
				g.drawRegion(GameResource.instance.imgPoupPanel, idx * w, 0, w, h,
						Sprite.TRANS_NONE, j * w, i * h, Graphics.LEFT | Graphics.TOP);

			}
		}

	}
}
