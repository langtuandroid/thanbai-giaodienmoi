package com.hdc.mycasino.screen;

import java.util.Vector;

import android.R.bool;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Button;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.Footer;
import com.hdc.mycasino.model.Position;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.DetailImage;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;

public abstract class Screen {

	protected static final byte DISAPPEAR_TIME_OUT = 112;
	public static final byte ITEM_HEIGHT = (byte) (BitmapFont.m_bmFont.getHeight() + 10);
	public static final byte HAFT_ITEM_HEIGHT = (byte) (ITEM_HEIGHT / 2);
	public static Position[] posCmd = new Position[3];
	public static int newInboxMsgTick = DISAPPEAR_TIME_OUT;
	public static int newChatMsgTick = DISAPPEAR_TIME_OUT;
	public static Image m_imgBackground;
	public static Image m_imgLogin;
	public static boolean hasNewMail = false;
	public static boolean isChatMsg = false;
	public static int numberUnreadMail = 0;

	public Command m_cmdLeft, m_cmdCenter;
	public Command m_cmdRight;

	public String m_strTitle = "";
	protected int m_width;
	protected int m_height;
	protected int m_left, m_top;
	protected int limit = 0;
	public int cmtoY, cmy, cmdy, cmvy, yL;
	public int m_selected = 0;
	public Button mBt_Defaut;

	// TODO touch for game
	public boolean isTouchSpecial = false;
	public boolean isPointerDown = false;
	public boolean isPointerClick = false;
	public boolean isPointerMove = false;
	public boolean isPointerHold = false;

	// TODO mail
	// idxMail = 1 : icon disable
	// idxMail = 3 : icon enable
	protected int flagMail = 1;

	public Footer mFooter_GiaToc;
	public Footer mFooter_BanBe;
	public Footer mFooter_MuaSam;
	public Footer mFooter_XepHang;
	public Footer mFooter_ChoiGame;

	// TODO create footer in mainscr
	public void createFooter() {
		int x1 = GameCanvas.w / 5;
		int y1 = GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight();
		mFooter_GiaToc = new Footer(0, GameCanvas.h
				- GameResource.instance.imgHeaderTop.getHeight(), x1, y1, 0xffb901, 0xffffff, null,
				9, 23, "GIA TỘC");
		mFooter_BanBe = new Footer(x1, GameCanvas.h
				- GameResource.instance.imgHeaderTop.getHeight(), x1, y1, 0xffb901, 0xffffff, null,
				7, 21, "BẠN BÈ");
		mFooter_MuaSam = new Footer(x1 * 2, GameCanvas.h
				- GameResource.instance.imgHeaderTop.getHeight(), x1, y1, 0xffb901, 0xffffff, null,
				12, 26, "MUA SẮM");
		mFooter_XepHang = new Footer(x1 * 3, GameCanvas.h
				- GameResource.instance.imgHeaderTop.getHeight(), x1, y1, 0xffb901, 0xffffff, null,
				4, 18, "XẾP HẠNG");
		mFooter_ChoiGame = new Footer(x1 * 4, GameCanvas.h
				- GameResource.instance.imgHeaderTop.getHeight(), x1, y1, 0xffb901, 0xffffff, null,
				10, 24, "CHƠI GAME");
	}

	public void setSelected(int index) {
		m_selected = index;
	}

	public Screen() {
		// TODO Auto-generated constructor stub
		loadImg();
		createFooter();

	}

	static {
		posCmd[0] = new Position(0, GameCanvas.h - 24);
		posCmd[1] = new Position(GameCanvas.hw - 20, GameCanvas.h - 24);
		posCmd[2] = new Position(GameCanvas.w - 40, GameCanvas.h - 24);
	}

	public void switchToMe() {
		if (this instanceof PlayGameScr)
			freeImg();
		// loadImg();
		GameCanvas.currentScreen = this;

		if (!(GameCanvas.instance.currentScreen instanceof LoginScr)
				&& !(GameCanvas.instance.currentScreen instanceof MainScr)) {
			// TODO set command footer
			mFooter_BanBe.setCmd(MainScr.instance.m_cmdFriend);
			mFooter_ChoiGame.setCmd(MainScr.instance.m_cmdPlay);
			mFooter_GiaToc.setCmd(MainScr.instance.m_cmdClan);
			mFooter_MuaSam.setCmd(MainScr.instance.m_cmdShop);
			mFooter_XepHang.setCmd(MainScr.instance.m_cmdListLayer);
		}
	}

	public void loadImg() {
		if (HDCGameMidlet.scale == 2f) {
			float scale;
			scale = (float) 2 / 5;
			GameResource.instance.imgListScr_Panel = ImagePack.createImage(
					ImagePack.LIST_SCREEN_PANEL, scale);
			// scale hight light
			GameResource.instance.imgListScr_HightLight = Image.scaleImage(
					GameResource.instance.imgListScr_HightLight,
					GameResource.instance.imgListScr_Panel.getWidth(),
					GameResource.instance.imgListScr_HightLight.getHeight());

			// scale hight light
			GameResource.instance.imgTabs_HightLightRow = Image.scaleImage(
					GameResource.instance.imgTabs_HightLightRow,
					GameResource.instance.imgListScr_Panel.getWidth(),
					GameResource.instance.imgTabs_HightLightRow.getHeight());
		}
	}

	public void freeImg() {
		GameResource.instance.imgCoinIcon = null;
		GameResource.instance.imgLogo = null;
		GameResource.instance.imgBox = null;
		GameResource.instance.imgBoxFocus = null;
		GameResource.instance.imgArrowBig = null;
		// GameResource.instance.imgGirl = null;
		GameResource.instance.imgHandGirl = null;
		GameResource.instance.imgEyeGirl = null;
		GameResource.instance.imgFrames = null;
		GameResource.instance.frameCheck = null;
		GameResource.instance.m_frameMouthGirl = null;
		GameResource.instance.m_frameIconMenu = null;
		GameResource.instance.m_frameIconGame = null;
		System.gc();
	}

	public abstract void close();

	// TODO paint header background
	@SuppressWarnings("static-access")
	private void paintHeaderBackground(Graphics g) {
		int col = GameCanvas.w / GameResource.instance.imgHeaderBg.getWidth() + 1;
		for (int i = 0; i < col; i++) {
			g.drawImage(GameResource.instance.imgHeaderBg,
					i * GameResource.instance.imgHeaderBg.getWidth(), 0, Graphics.LEFT
							| Graphics.TOP);
		}

		// TODO paint title
		if (m_strTitle != null)
			BitmapFont.m_bmNormalFont.drawBoldFont(g, m_strTitle, GameCanvas.w / 2,
					10 / HDCGameMidlet.instance.scale, 0xffb901, Graphics.HCENTER | Graphics.TOP);

	}

	// TODO paint header top
	protected void paintHeader_Top(Graphics g) {
		g.drawImage(GameResource.instance.imgHeaderTop, 0, 0, Graphics.LEFT | Graphics.TOP);
	}

	// TODO paint header bottom
	protected void paintHeader_Bottom(Graphics g) {
		g.drawImage(GameResource.instance.imgHeaderBottom, 0, GameCanvas.h, Graphics.LEFT
				| Graphics.BOTTOM);
	}

	private void paintFooter(Graphics g) {
		mFooter_GiaToc.paint(g);
		mFooter_BanBe.paint(g);
		mFooter_MuaSam.paint(g);
		mFooter_XepHang.paint(g);
		mFooter_ChoiGame.paint(g);
	}

	public void paint(Graphics g) {
		g.translate(-g.getTranslateX(), -g.getTranslateY());
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);

		if (GameCanvas.currentScreen instanceof LoginScr) {
			// TODO paint header top
			paintHeader_Top(g);
			// TODO paint header bottom
			paintHeader_Bottom(g);
		}

		// if (!(GameCanvas.currentScreen instanceof LoginScr)
		// && !(GameCanvas.currentScreen instanceof PlayGameScr)
		// && !(GameCanvas.currentScreen instanceof TienLenScr)
		// && !(GameCanvas.currentScreen instanceof XiToScr)
		// && !(GameCanvas.currentScreen instanceof PhomScr)
		// && !(GameCanvas.currentScreen instanceof BaiCaoScr)) {
		// paintFooter(g);
		// }

		// TODO header
		if (!(GameCanvas.currentScreen instanceof LoginScr)
				&& !(GameCanvas.currentScreen instanceof PlayGameScr)) {
			// paint header background
			paintHeaderBackground(g);
			// paint header dina
			paintHeader_Dina(g);
		}

		if (!(GameCanvas.currentScreen instanceof PlayGameScr)
				&& !(GameCanvas.currentScreen instanceof LoginScr)) {
			// TODO menu
			// paint background menu
			g.drawImage(GameResource.instance.imgMenuBg, GameCanvas.w, 0, Graphics.RIGHT
					| Graphics.TOP);
			// paint icon menu
			if (GameCanvas.instance.menu.m_showMenu)
				GameResource.instance.m_frameMenuIcon.drawFrame(1, GameCanvas.w
						- DetailImage.imgMenuBg_w / 2, DetailImage.imgMenuBg_h / 2,
						Sprite.TRANS_NONE, Graphics.VCENTER | Graphics.HCENTER, g);
			else
				GameResource.instance.m_frameMenuIcon.drawFrame(3, GameCanvas.w
						- DetailImage.imgMenuBg_w / 2, DetailImage.imgMenuBg_h / 2,
						Sprite.TRANS_NONE, Graphics.VCENTER | Graphics.HCENTER, g);
		}

		if (!(GameCanvas.currentScreen instanceof PlayGameScr)
				&& !(GameCanvas.currentScreen instanceof LoginScr)) {
			// TODO exit
			g.drawRegion(GameResource.instance.imgMenuBg, 0, 0, DetailImage.imgMenuBg_w,
					DetailImage.imgMenuBg_h, Sprite.TRANS_MIRROR, 0, 0, Graphics.LEFT
							| Graphics.TOP);
			// icon exit
			GameResource.instance.m_frameMenuIcon.drawFrame(2, DetailImage.imgMenuBg_w / 2,
					DetailImage.imgMenuBg_h / 2, Sprite.TRANS_NONE, Graphics.VCENTER
							| Graphics.HCENTER, g);
		}

		if (GameCanvas.currentDialog != null && GameCanvas.currentDialog instanceof InputDlg)
			return;

		// if (isChatMsg && !(GameCanvas.currentScreen instanceof LoginScr)) {
		// paintIconChat(g);
		// }

		if (numberUnreadMail > 0 && !(GameCanvas.currentScreen instanceof LoginScr)
				&& !(GameCanvas.currentScreen instanceof PlayGameScr)
				&& !(GameCanvas.currentScreen instanceof TabScr)
				&& !(GameCanvas.currentScreen instanceof ListBoardScr)) {
			paintIconMail(g);
		}

		// if (ChatTextField.gI().isShow) {
		// PaintPopup.paintCmdBar(g, ChatTextField.gI().left,
		// ChatTextField.gI().center, ChatTextField.gI().right);
		// } else {
		// if (!GameCanvas.menu.m_showMenu) {
		// if (GameCanvas.currentDialog == null)
		// PaintPopup.paintCmdBar(g, m_cmdLeft, m_cmdCenter,
		// m_cmdRight);
		// else
		// PaintPopup.paintCmdBar(g, null, null, null);
		// }
		// }
	}

	protected void paintIconChat(Graphics g) {
		if (newChatMsgTick < DISAPPEAR_TIME_OUT) {
			newChatMsgTick++;
		} else {
			newChatMsgTick = 0;
		}
		// int imgIndex = (newChatMsgTick / 8) % 2;
		// GameResource.instance.imgChatNotify.drawFrame(imgIndex,
		// GameCanvas.w - 43, GameCanvas.hBottomBar - 21, 0,
		// Graphics.RIGHT | Graphics.TOP, g);

	}

	protected void paintIconMail(Graphics g) {
		// if (hasNewMail) {
		// hasNewMail = false;
		// newInboxMsgTick = 0;
		// }
		//
		if (newInboxMsgTick < DISAPPEAR_TIME_OUT) {
			newInboxMsgTick++;
		}
		//
		int imgIndex = (newInboxMsgTick / 8) % 2;

		if (hasNewMail || numberUnreadMail > 0) {

			if (imgIndex == 1) {
				flagMail = 3;
			} else {
				flagMail = 1;
			}

			GameResource.instance.m_frameHeaderMsgIcon
					.drawFrame(
							this.flagMail,
							GameCanvas.w - (GameResource.instance.imgMenuEnable.getWidth() / 2),
							(GameResource.instance.imgHeaderBg.getHeight()
									- GameResource.instance.m_frameHeaderMsgIcon.frameHeight - 8 / HDCGameMidlet.scale) / 2,
							0, Graphics.RIGHT | Graphics.TOP, g);

		}

	}

	// TODO paint Header - Dina
	@SuppressWarnings("static-access")
	private void paintHeader_Dina(Graphics g) {
		g.translate(
				2 * GameResource.instance.imgMenuBg.getWidth(),
				(GameResource.instance.imgHeaderBg.getHeight() - (int) (8 / HDCGameMidlet.instance.scale)) / 2);

		// paint background dina
		g.drawImage(GameResource.instance.imgHeaderDinaBg, 0, 0, Graphics.LEFT | Graphics.VCENTER);
		// paint icon
		// g.drawImage(GameResource.instance.imgHeaderDinaIcon, 0, 0,
		// Graphics.LEFT | Graphics.VCENTER);
		GameResource.instance.m_frameHeaderIcon_DinaGold.drawFrame(0, 0, 0, Sprite.TRANS_NONE,
				Graphics.LEFT | Graphics.VCENTER, g);

		// paint text
		BitmapFont.m_bmNormalFont.setTextSize(12f);
		BitmapFont.m_bmNormalFont.drawNormalFont_1(g, "Nạp Dina",
				GameResource.instance.m_frameHeaderIcon_DinaGold.frameWidth * 3 / 2, 0, 0xffffff,
				Graphics.LEFT | Graphics.VCENTER);

		g.translate(-g.getTranslateX(), -g.getTranslateY());
	}

	public abstract void update();

	@SuppressWarnings("static-access")
	public void actionMenu() {

		if (!GameCanvas.instance.menu.m_showMenu) {
			doMenu();
			if (GameCanvas.instance.menu.m_list != null) {
				// play sound
				// HDCGameMidlet.sound.openFile(HDCGameMidlet.instance,
				// R.raw.click);
				// HDCGameMidlet.sound.play();
				HDCGameMidlet.instance.m_viberator.vibrate(100);

				GameCanvas.instance.menu.m_showMenu = true;
				GameCanvas.instance.menu.resetMenu();
				GameCanvas.instance.m_arrEffect.removeAllElements();
			}
		}
		// else {
		// GameCanvas.instance.menu.m_showMenu = false;
		// }
	}

	// TODO update key mainscr
	// private void updateKey_Footer() {
	// mFooter_GiaToc.updateKey();
	// mFooter_BanBe.updateKey();
	// mFooter_MuaSam.updateKey();
	// mFooter_XepHang.updateKey();
	// mFooter_ChoiGame.updateKey();
	// }

	@SuppressWarnings("static-access")
	public void updateKey() {

		if (!(GameCanvas.instance.currentScreen instanceof PlayGameScr)) {
			if (GameCanvas.isPointerDown) {
				// mail
				if (!(GameCanvas.instance.currentScreen instanceof TabScr)
						&& !(GameCanvas.currentScreen instanceof ListBoardScr)
						&& !(GameCanvas.currentScreen instanceof LoginScr)) {

					if (GameCanvas.isPointer_Down(GameCanvas.w
							- (GameResource.instance.imgMenuEnable.getWidth() / 2)
							- GameResource.instance.m_frameHeaderMsgIcon.frameWidth / 2 * 3, 0,
							GameResource.instance.m_frameHeaderMsgIcon.frameWidth * 2,
							GameResource.instance.imgHeaderBg.getHeight())) {
						flagMail = 3;
					}
				}

			}

			if (GameCanvas.isPointerClick) {

				// mail
				if (!(GameCanvas.instance.currentScreen instanceof TabScr)
						&& !(GameCanvas.currentScreen instanceof ListBoardScr)
						&& !(GameCanvas.currentScreen instanceof LoginScr)) {
					if (GameCanvas
							.isPointer(
									GameCanvas.w
											- (GameResource.instance.imgMenuEnable.getWidth() / 2)
											- GameResource.instance.m_frameHeaderMsgIcon.frameWidth
											/ 2 * 3, 0,
									GameResource.instance.m_frameHeaderMsgIcon.frameWidth * 2,
									GameResource.instance.imgHeaderBg.getHeight())) {
						GameCanvas.startWaitDlg();
						GlobalService.onGetInboxMessage();
					}
				}

				if (!(GameCanvas.instance.currentScreen instanceof PlayGameScr)
						&& !(GameCanvas.instance.currentScreen instanceof LoginScr)) {
					if (GameCanvas.isPointer(
							GameCanvas.w - GameResource.instance.imgMenuBg.getWidth(), 0,
							GameResource.instance.imgMenuBg.getWidth(),
							GameResource.instance.imgMenuBg.getHeight())) {
						HDCGameMidlet.instance.gameCanvas.currentScreen.actionMenu();
					}
				}

				if (GameCanvas.isPointer(0, 0, GameResource.instance.imgMenuBg.getWidth() / 2 * 3,
						GameResource.instance.imgMenuBg.getHeight() / 2 * 3)
						&& Math.abs(GameCanvas.instance.pyLast - GameCanvas.instance.py) < 5) {
					HDCGameMidlet.instance.m_viberator.vibrate(100);
					HDCGameMidlet.instance.onBackPressed();
				}

				// TODO nạp dina
				if (!(GameCanvas.currentScreen instanceof LoginScr))
					if (GameCanvas.isPointer(2 * GameResource.instance.imgMenuBg.getWidth(), 0,
							GameResource.instance.imgHeaderDinaBg.getWidth(),
							GameResource.instance.imgHeaderDinaBg.getHeight() / 2 * 3)) {
						if (!(GameCanvas.instance.currentScreen instanceof TabScr))
							MainScr.gI().sms();
						else {
							HDCGameMidlet.instance
									.Toast("Không thể nạp dina \n ở màn hình này !!!");
						}
					}
			}

			if (GameCanvas.keyPressed[5]) {
				GameCanvas.keyPressed[5] = false;
				if (ChatTextField.gI().isShow) {
					ChatTextField.gI().center.action.perform();
				} else if (m_cmdCenter != null && m_cmdCenter.action != null) {
					m_cmdCenter.action.perform();
				}
			}

			if (GameCanvas.keyPressed[12]) {
				GameCanvas.keyPressed[12] = false;
				if (ChatTextField.gI().isShow)
					ChatTextField.gI().left.action.perform();
				else if (m_cmdLeft != null && m_cmdLeft.action != null)
					m_cmdLeft.action.perform();
			}

			// BB: BB can't handle keyPress of soft2
			if (GameCanvas.isBB) {
				if (GameCanvas.keyReleased[13]) {
					GameCanvas.keyReleased[13] = false;
					if (m_cmdRight != null && m_cmdRight.action != null)
						m_cmdRight.action.perform();

				}
			} else if (GameCanvas.keyPressed[13]) {
				GameCanvas.keyPressed[13] = false;
				if (ChatTextField.gI().isShow)
					ChatTextField.gI().right.action.perform();
				else if (m_cmdRight != null && m_cmdRight.action != null)
					m_cmdRight.action.perform();

			}
		}
	}

	public void keyPress(int keyCode) {
	}

	public abstract void doMenu();

	public abstract void doBack();

	// TODO set button default
	public void setButtonDefault(Button mButton) {
		mBt_Defaut = mButton;
		mBt_Defaut.count = 0;
	}

	public void moveCamera() {

		if (GameCanvas.instance.menu.m_showMenu || GameCanvas.currentDialog != null)
			return;
		if (cmy > 0) {
			cmtoY = 0;
		}
		if (cmy < limit) {
			cmtoY = limit;
		}

		if (cmy != cmtoY) {
			cmvy = (cmtoY - cmy) << 2;
			cmdy += cmvy;
			cmy += cmdy >> 4;
			cmdy = cmdy & 0xf;
		}
	}
}
