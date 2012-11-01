package com.hdc.mycasino.screen;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.content.Intent;
import android.net.Uri;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.animation.CardFall;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.DataManager;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.ItemInfo;
import com.hdc.mycasino.model.MoneyInfo;
import com.hdc.mycasino.model.PlayerInfo;
import com.hdc.mycasino.model.Point;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.network.Session;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;
import com.hdc.mycasino.utilities.TField;

public class MainScr extends Screen {
	public final static byte PHOM = 0;
	public final static byte TLMN = 1;
	public final static byte XITO = 2;
	public final static byte TLMB = 3;
	public final static byte BAI_CAO = 4;
	public final static byte XI_DACH = 5;
	public final static byte BACK_JACK = 6;
	public final static byte POKER = 7;

	@SuppressWarnings("rawtypes")
	public Vector m_arrListMenu;
	@SuppressWarnings("rawtypes")
	// public Vector m_arrEffect;
	public Command m_cmdPlay;
	public Command m_cmdFriend;
	public Command m_cmdOwner;
	public Command m_cmdShop;
	public Command m_cmdMail;
	public Command m_cmdListLayer;
	public Command m_cmdSMS;
	public Command m_cmdForum;
	public Command m_cmdClan;

	private Command m_cmdPhom;
	private Command m_cmdXiTo;
	private Command m_cmdTienLenMN;
	private Command m_cmdTienLenMB;
	private Command m_cmdBaiCao;
	@SuppressWarnings("unused")
	private Command m_cmdPoker;
	@SuppressWarnings("unused")
	private Command m_cmdXiDach;
	@SuppressWarnings("unused")
	private Command m_cmdBackJack;

	private Command m_cmdMain;
	private Command m_cmdBack;

	private Command m_cmdMenu;

	// private int deltaMove;
	private int[][] m_iPositionConmand;
	private byte m_bMoveCommand = 0;

	// private int m_iMax;
	// private int m_iMin;
	// private int m_iSpeedX;
	// private int m_iSpeedY;
	private int m_iMidPosCommand;
	private boolean m_bAction = false;
	// private int m_iCallMoveCommand;

	// private int m_itickEyes = CRes.random(20, 30);
	// private boolean m_iEyes = false;
	private byte m_bmouth = 0;
	private int m_imounthTick = 0;
	private int m_iClickSelect;
	static MainScr instance = null;

	public static Timer m_timer = null;
	private static int m_iTimerPing = 0;

	// TODO image background for info
	private Image imgBackgroundInfo;

	// TODO Ellipse 1
	// dành cho 7 item
	Point[] m_Ellipse_1;
	public boolean isTranlate = false;
	public int idx_IconTranlate = 0;

	// TODO Ellipse 2
	// dành cho 5 item
	Point[] m_Ellipse_2;

	// TODO Ellipse để sử dụng update,paint
	Point[] m_Ellipse;

	//
	public int count = 0;
	// public int count_2 = 0;
	public boolean isLeft = true;

	// TODO góc quay cho hight light
	float degree = 0;

	// TODO biến dành cho back
	int flag = 0;

	public static MainScr gI() {
		if (instance == null) {
			instance = new MainScr();
		}
		return instance;
	}

	@SuppressWarnings("rawtypes")
	public MainScr() {

		if (m_timer == null) {
			m_timer = new Timer();
			m_timer.schedule(new TimerTask() {

				public void run() {
					if ((GameCanvas.currentScreen instanceof PlayGameScr) && PlayGameScr.m_iGameTick > 0) {
						PlayGameScr.m_iGameTick--;
					}
					m_iTimerPing++;
					if (!(GameCanvas.currentScreen instanceof LoginScr) && m_iTimerPing > 3) {
						m_iTimerPing = 0;
						GlobalService.sendMessagePing();
					}
				}
			}, 0, 1000);
		}

		if (GameResource.instance.imgBox == null)
			GameResource.instance.imgBox = ImagePack.createImage(ImagePack.BOX_PNG);
		m_arrListMenu = new Vector();
		// m_arrEffect = new Vector();

		initCommand();

		// deltaMove = 0;

		m_left = GameCanvas.hw;
		m_top = GameCanvas.hBottomBar;
		m_height = GameCanvas.h - GameCanvas.hBottomBar * 2;
		m_width = GameCanvas.hw;

//		m_iMin = GameCanvas.w - 20 - GameResource.instance.imgBox.getWidth() / 2;
//		m_iMax = m_iMin - GameResource.instance.imgBox.getWidth() / 2;// * 2;
//		m_iMidPosCommand = GameCanvas.ch;// GameResource.instance.imgBox.getWidth()
//											// * 3 +
//											// GameResource.instance.imgBox.getWidth()/2;
//		m_iCallMoveCommand = 0;
//
//		m_iSpeedX = 10;
//		m_iSpeedY = 15;

		m_cmdLeft = m_cmdMenu;
		m_cmdRight = m_cmdBack;
		m_cmdCenter = m_cmdMain;

		addMenuCommand();
		initPosCommand();

		// create background for info
		createImageBackground(14, 4);

		// TODO Ellipse 1
		initEllipse_1();

		// TODO clone ellipse 1
		cloneEllipse(m_Ellipse_1);

		// TODO set command footer
		mFooter_BanBe.setCmd(m_cmdFriend);
		mFooter_ChoiGame.setCmd(m_cmdPlay);
		mFooter_GiaToc.setCmd(m_cmdClan);
		mFooter_MuaSam.setCmd(m_cmdShop);
		mFooter_XepHang.setCmd(m_cmdListLayer);
	}

	/**
	 * Get name of game by gameId.
	 */
	public static String getGameNameById(int gameId) {
		switch (gameId) {
		case PHOM:
			return "Phỏm";
		case XITO:
			return "Xì Tố";
		case TLMB:
			return "TL.Miền Bắc";
		case TLMN:
			return "TL.Miền Nam";
		case BAI_CAO:
			return "Bài Cào";
		case POKER:
			return "Poker";
		case XI_DACH:
			return "Xì Dách";
		case BACK_JACK:
			return "Back Jack";
		default:
			return "";
		}
	}

	// TODO clone Ellipse
	private void cloneEllipse(Point[] mTmp) {
		m_Ellipse = new Point[mTmp.length];
		for (int i = 0; i < mTmp.length; i++) {
			m_Ellipse[i] = new Point();
			m_Ellipse[i] = mTmp[i];
		}
	}

	// TODO init Ellipse 1
	private void initEllipse_1() {
		// TODO Ellipse 1
		m_Ellipse_1 = new Point[7];
		// góc cho 7 item
		float Angle = (float) (2 * Math.PI / 7);
		// góc bắt đầu
		float Angle_Begin = (float) (Math.PI / 2);

		for (int i = 0; i < 7; i++) {
			m_Ellipse_1[i] = new Point();
			m_Ellipse_1[i].setInfo(280 / HDCGameMidlet.instance.scale
					+ (GameCanvas.w - 620 / HDCGameMidlet.instance.scale) / 2,
					(GameCanvas.h - (230 / HDCGameMidlet.instance.scale)) / 2,
					/* 400 / HDCGameMidlet.instance.scale */
					/*
					 * GameCanvas.w - ((300*6/7)/HDCGameMidlet.instance.scale)
					 */
					360 / HDCGameMidlet.instance.scale, 230 / HDCGameMidlet.instance.scale, Angle_Begin + i * Angle);
			m_Ellipse_1[i].setCommand((Command) m_arrListMenu.get(i));
			m_Ellipse_1[i].setIndex(m_Ellipse_1[i].getCommand().index);
		}
		m_Ellipse_1[0].isFocus = true;
	}

	// TODO init Ellipse 2
	private void initEllipse_2() {
		// TODO Ellipse 1
		m_Ellipse_2 = new Point[5];
		// góc cho 7 item
		float Angle = (float) (2 * Math.PI / 5);
		// góc bắt đầu
		float Angle_Begin = (float) (Math.PI / 2);

		for (int i = 0; i < 5; i++) {
			m_Ellipse_2[i] = new Point();
			m_Ellipse_2[i].setInfo(280 / HDCGameMidlet.instance.scale
					+ (GameCanvas.w - 620 / HDCGameMidlet.instance.scale) / 2,
					(GameCanvas.h - (250 / HDCGameMidlet.instance.scale)) / 2, 340 / HDCGameMidlet.instance.scale,
					250 / HDCGameMidlet.instance.scale, Angle_Begin + i * Angle);
			m_Ellipse_2[i].setCommand((Command) m_arrListMenu.get(i));
			m_Ellipse_2[i].setIndex(m_Ellipse_2[i].getCommand().index);
		}
		m_Ellipse_2[0].isFocus = true;
	}

	@SuppressWarnings("unchecked")
	public void addMenuCommand() {
		m_arrListMenu.removeAllElements();

		m_arrListMenu.addElement(m_cmdPlay);
		m_arrListMenu.addElement(m_cmdClan);
		m_arrListMenu.addElement(m_cmdOwner);
		m_arrListMenu.addElement(m_cmdFriend);
		m_arrListMenu.addElement(m_cmdShop);
		m_arrListMenu.addElement(m_cmdMail);
		m_arrListMenu.addElement(m_cmdListLayer);
		// m_arrListMenu.addElement(m_cmdSMS);
		// m_arrListMenu.addElement(m_cmdForum);

		m_strTabName = new String[] { "Chơi Bài", "Gia Tộc", "Cá Nhân", "Bạn Bè", "Shop", "Tin Nhắn", "Cao thủ",
				"Nạp Tiền", "Forum", "Thi Đấu" };
		m_cmdBack.caption = "Thoát";
	}

	@SuppressWarnings("unchecked")
	private void addPlayGameCommand() {
		m_arrListMenu.removeAllElements();

		m_arrListMenu.addElement(m_cmdPhom);
		m_arrListMenu.addElement(m_cmdTienLenMN);
		m_arrListMenu.addElement(m_cmdTienLenMB);
		m_arrListMenu.addElement(m_cmdXiTo);
		m_arrListMenu.addElement(m_cmdBaiCao);

		// m_arrListMenu.addElement(m_cmdPoker);
		// m_arrListMenu.addElement(m_cmdBackJack);
		// m_arrListMenu.addElement(m_cmdXiDach);

		m_strTabName = new String[] { getGameNameById(PHOM), getGameNameById(TLMN), getGameNameById(TLMB),
				getGameNameById(XITO), getGameNameById(BAI_CAO), getGameNameById(POKER), getGameNameById(BACK_JACK),
				getGameNameById(XI_DACH) };

		m_cmdBack.caption = GameResource.close;
	}

	public void initPosCommand() {
//		if (GameResource.instance.imgBoxFocus == null)
//			GameResource.instance.imgBoxFocus = ImagePack.createImage(ImagePack.BOX_FOCUS_PNG);
//		int i;
//		m_iPositionConmand = new int[m_arrListMenu.size()][3];
//		int y = 0, mid = (m_arrListMenu.size() - 1) / 2;
//		if (m_arrListMenu.size() > 8)
//			mid = 4;
//		int width = GameResource.instance.imgBoxFocus.getHeight() * 5 / 4 - 10;

//		for (i = 0; i < m_arrListMenu.size(); i++) {
//			m_iPositionConmand[i][0] = m_iMin;// x cua command
//			if (i == 0) {
//				y = m_iMidPosCommand;// GameCanvas.hh;
//			} else {
//				if (i <= mid) {
//					y = m_iMidPosCommand - i * width;
//				} else {
//					y = m_iMidPosCommand + (m_arrListMenu.size() - i) * width;
//				}
//			}
//			m_iPositionConmand[i][1] = y;
//			m_iPositionConmand[i][2] = y;
//		}
//		m_bMoveCommand = 0;

//		m_bAction = false;
//		m_selected = 0;
	}

	private void initCommand() {
		m_cmdMenu = new Command(GameResource.menu, new IAction() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void perform() {
				// TODO Auto-generated method stub

			}
		});

		m_cmdPlay = new Command("Vào Casino làm vài ván \n kiếm tiền...", 0, new IAction() {
			public void perform() {
				addPlayGameCommand();
				initPosCommand();
				// TODO init Ellipse 2
				initEllipse_2();

				// TODO clone ellipse
				cloneEllipse(m_Ellipse_2);

				flag = 1;


				//NEW
//				MainScr.gI().switchToMe();
			}
		}) {
			public void paint(Graphics g, int x, int y) {
				// GameResource.instance.m_frameIconMenu.drawFrame(0, x, y,
				// Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER,
				// g);

				GameResource.instance.m_frameEffectIcon_Text_Menu.drawFrame(0, x, y,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

			}
		};

		m_cmdFriend = new Command("Tìm bạn và chat chit với \n bạn bè của mình...", 4,
				new IAction() {
					public void perform() {
						GameCanvas.startWaitDlg();
						GlobalService.onGetListFriends();
					}
				}) {
			public void paint(Graphics g, int x, int y) {
				// GameResource.instance.m_frameIconMenu.drawFrame(2, x, y,
				// Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER,
				// g);

				GameResource.instance.m_frameEffectIcon_Text_Menu.drawFrame(4, x, y,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

			}
		};

		m_cmdOwner = new Command("Vào xem thông tin cá nhân \n và bảng thành tích \n của mình...",
				2, new IAction() {
					public void perform() {
						GlobalService.sendMessageGetPlayerProfile();
						GameCanvas.startWaitDlg();
					}
				}) {
			public void paint(Graphics g, int x, int y) {
				// GameResource.instance.m_frameIconMenu.drawFrame(1, x, y,
				// Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER,
				// g);

				GameResource.instance.m_frameEffectIcon_Text_Menu.drawFrame(6, x, y,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

			}
		};

		m_cmdShop = new Command("Mua những vật phẩm và \n avatar kute nhất \n để chơi game...", 3,
				new IAction() {
					public void perform() {
						GameCanvas.startWaitDlg();
						GlobalService.onGetListShopAvatars();
					}
				}) {
			public void paint(Graphics g, int x, int y) {
				// GameResource.instance.m_frameIconMenu.drawFrame(4, x, y,
				// Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER,
				// g);

				GameResource.instance.m_frameEffectIcon_Text_Menu.drawFrame(3, x, y,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

			}
		};

		m_cmdMail = new Command("Xem có ai gửi thư \n cho mình không nhỉ...", 5, new IAction() {

			public void perform() {
				GameCanvas.startWaitDlg();
				GlobalService.onGetInboxMessage();
			}
		}) {
			public void paint(Graphics g, int x, int y) {
				// GameResource.instance.m_frameIconMenu.drawFrame(5, x, y,
				// Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER,
				// g);

				GameResource.instance.m_frameEffectIcon_Text_Menu.drawFrame(5, x, y,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

			}
		};

		m_cmdListLayer = new Command("Vào xem danh sách cao thủ \n và đại gia mới nhất...", 6,
				new IAction() {

					public void perform() {
						GameCanvas.startWaitDlg();
						GlobalService.onGetTopPlayersLevelAndGold();
					}
				}) {
			public void paint(Graphics g, int x, int y) {
				// GameResource.instance.m_frameIconMenu.drawFrame(6, x, y,
				// Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER,
				// g);

				GameResource.instance.m_frameEffectIcon_Text_Menu.drawFrame(6, x, y,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

			}
		};

		m_cmdSMS = new Command("Nạp tiền chơi game \n nào bạn...", new IAction() {

			public void perform() {
				sms();
			}
		}) {
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameIconMenu.drawFrame(7, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			}
		};

		m_cmdForum = new Command("Vào diễn đàn xem những tin tức nóng hổi...", new IAction() {
			public void perform() {
				String url = DataManager.LINK_FORUM;
				if (url.indexOf("http://", 1) > 0) {
					url = url.substring(7, DataManager.LINK_FORUM.length());
				}

				onReceivedForumLink("Diễn đàn","Bạn có thực sự muốn chuyển đến forum không ?", url);
			}
		}) {
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameIconMenu.drawFrame(8, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			}
		};

		m_cmdClan = new Command("Đóng góp và xây dựng \n gia tộc cho mình...", 1, new IAction() {
			public void perform() {
				ClanScr.gI().refeshClanScr();
			}
		}) {
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameEffectIcon_Text_Menu.drawFrame(1, x, y,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

			}
		};

		m_cmdPhom = new Command("U` vài ván là giàu to ngay.\n Chính là Phỏm", 11, new IAction() {

			public void perform() {
				ListBoardScr.getInstance().m_strTitleGame = getGameNameById(PHOM);
				ListStringScr.gI().m_iSelectedTypeGame = PHOM;
				GameCanvas.startWaitDlg();
				GlobalService.sendMessageIsRegisteredCompetition(PHOM);
				GlobalService.onSendGameID((byte) PHOM);
			}
		}) {
			@SuppressWarnings("unused")
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameIconGame.drawFrame(0, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			}
		};

		m_cmdXiTo = new Command(
				"Thử trở thành điệp viên 007 \n vua Xì Tố trong phim Canh \n Bạc Hoàng Gia nhé!",
				9, new IAction() {

					public void perform() {
						ListBoardScr.getInstance().m_strTitleGame = getGameNameById(XITO);
						ListStringScr.gI().m_iSelectedTypeGame = XITO;
						GameCanvas.startWaitDlg();
						GlobalService.onSendGameID((byte) XITO);
					}
				}) {
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameIconGame.drawFrame(3, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			}
		};

		m_cmdTienLenMN = new Command("Thử sức với Tiến Lên Miền \n Nam vài ván nào!", 7,
				new IAction() {

					public void perform() {
						ListBoardScr.getInstance().m_strTitleGame = getGameNameById(TLMN);
						ListStringScr.gI().m_iSelectedTypeGame = TLMN;
						GameCanvas.startWaitDlg();
						GlobalService.sendMessageIsRegisteredCompetition(TLMN);
						GlobalService.onSendGameID((byte) TLMN);
					}
				}) {
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameIconGame.drawFrame(1, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			}
		};

		m_cmdTienLenMB = new Command("Bạn đã chơi thử Tiến Lên \n Miền Bắc chưa, hay lắm đấy!", 8,
				new IAction() {

					public void perform() {
						ListBoardScr.getInstance().m_strTitleGame = getGameNameById(TLMB);
						ListStringScr.gI().m_iSelectedTypeGame = TLMB;
						GameCanvas.startWaitDlg();
						GlobalService.sendMessageIsRegisteredCompetition(TLMB);
						GlobalService.onSendGameID((byte) TLMB);
					}
				}) {
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameIconGame.drawFrame(2, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			}
		};

		m_cmdBaiCao = new Command("Thắng nhanh, giàu nhanh \n chỉ có bài cào.", 10, new IAction() {

			public void perform() {
				ListBoardScr.getInstance().m_strTitleGame = getGameNameById(BAI_CAO);
				ListStringScr.gI().m_iSelectedTypeGame = BAI_CAO;
				GameCanvas.startWaitDlg();
				GlobalService.onSendGameID((byte) BAI_CAO);
			}
		}) {
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameIconGame.drawFrame(4, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			}
		};

		m_cmdXiDach = new Command("Xi Dach", 5, new IAction() {

			public void perform() {
				ListBoardScr.getInstance().m_strTitleGame = getGameNameById(XI_DACH);
				ListStringScr.gI().m_iSelectedTypeGame = XI_DACH;
				GameCanvas.startWaitDlg();
				GlobalService.onSendGameID((byte) XI_DACH);
			}
		}) {
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameIconGame.drawFrame(5, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			}
		};

		m_cmdPoker = new Command("Poker", 5, new IAction() {

			public void perform() {
				ListBoardScr.getInstance().m_strTitleGame = getGameNameById(POKER);
				ListStringScr.gI().m_iSelectedTypeGame = POKER;
				GameCanvas.startWaitDlg();
				GlobalService.onSendGameID((byte) POKER);
			}
		}) {
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameIconGame.drawFrame(6, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			}
		};

		m_cmdBackJack = new Command("Back Jack", 5, new IAction() {

			public void perform() {
				ListBoardScr.getInstance().m_strTitleGame = getGameNameById(BACK_JACK);
				ListStringScr.gI().m_iSelectedTypeGame = BACK_JACK;
				GameCanvas.startWaitDlg();
				GlobalService.onSendGameID((byte) BACK_JACK);
			}
		}) {
			public void paint(Graphics g, int x, int y) {
				GameResource.instance.m_frameIconGame.drawFrame(7, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			}
		};

		m_cmdMain = new Command(GameResource.accept, new IAction() {

			public void perform() {
				Command com = (Command) getSelectedItem();
				com.action.perform();
			}
		});

		m_cmdBack = new Command("Thoát", new IAction() {
			public void perform() {
				if (m_arrListMenu.contains(m_cmdPlay)) {
					GameCanvas.startOKDlg("Bạn có muốn thoát khỏi game?", new IAction() {
						public void perform() {
							close();
						}
					});
				} else {
					GameCanvas.msgdlg.delX.setPosition(0, 0);
					GameCanvas.msgdlg.delX.setPositionTo(GameCanvas.msgdlg.m_width / 2, 0);
					addMenuCommand();
					initPosCommand();
				}
			}
		});
	}

	public void switchToMe() {
		GameCanvas.endDlg();
		super.switchToMe();

		GameCanvas.cameraList.close();
		cmy = 0;
		m_iClickSelect = -1;

		// TODO clone ellipse 1
		cloneEllipse(m_Ellipse_1);
	}

	@SuppressWarnings("static-access")
	public void paint(Graphics g) {
		g.translate(-g.getTranslateX(), -g.getTranslateY());
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);

		// paint background
		g.drawImage(m_imgBackground, 0, 0, 0);

		// paint girl
		g.drawImage(GameResource.instance.imgGirl, 0, GameCanvas.h, Graphics.LEFT | Graphics.BOTTOM);

		// paint ellipse
		// g.drawOval(GameCanvas.w/2, GameCanvas.h/2, 400, 200);
		// g.drawOval(GameCanvas.w/4, GameCanvas.h/6, 400, 200);
		for (int i = 0; i < m_Ellipse.length; i++) {
			m_Ellipse[i].paint(g);

			if (m_Ellipse[i].isFocus) {
				m_Ellipse[i].paintHightLight_Text(g, degree);
				// paint info
				g.drawImage(imgBackgroundInfo, 20 / HDCGameMidlet.instance.scale, GameCanvas.h
						- GameResource.instance.imgGirl.getHeight(), Graphics.LEFT | Graphics.BOTTOM);

				String[] info = BitmapFont.splitString(m_Ellipse[i].getCommand().caption, imgBackgroundInfo.getWidth(),
						false);

				for (int j = 0; j < info.length; j++) {
					BitmapFont.m_bmNormalFont.drawItalicFont(g, info[j], 24 / HDCGameMidlet.instance.scale,
							GameCanvas.h - GameResource.instance.imgGirl.getHeight() - imgBackgroundInfo.getHeight()
									+ j * 20 / HDCGameMidlet.instance.scale + 6 / HDCGameMidlet.instance.scale,
							0xffb901, Graphics.LEFT | Graphics.TOP);
				}
			}
		}

		//
		// int i;
		// for (i = 0; i < m_arrEffect.size(); i++) {
		// ((CardFall) m_arrEffect.elementAt(i)).paint(g);
		// }
		//
		// g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		// PaintPopup.paintGirl(g, 0, GameCanvas.h - GameCanvas.hBottomBar + 2,
		// m_iEyes, m_bmouth);
		// paintListItem(g);
		// g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		// paintBoxChat(g, 20, GameCanvas.h - GameCanvas.hBottomBar -
		// GameResource.instance.imgGirl.getHeight() - 20);
		//
		// PaintPopup.paintTopBarMenu(g);
		// PaintPopup.paintFrameTop(g);
		//
		// if (m_selected >= 0)
		// BitmapFont.drawBoldFont(g, m_strTabName[m_selected], GameCanvas.w /
		// 2, GameCanvas.hBottomBar / 2, 0xffb901,
		// Graphics.HCENTER | Graphics.VCENTER);
		//
		// g.translate(-g.getTranslateX(), -g.getTranslateY());
		// PaintPopup.paintFrameBot(g);


		super.paint(g);
	}
	int curLength = 0;


	private void paintBoxChat(Graphics g, int x, int y) {
		Command c = (Command) m_arrListMenu.elementAt(m_selected);

		int w = GameCanvas.hw;
		String[] str;
		if (curLength >= c.caption.length())
			str = BitmapFont.m_bmNormalFont.splitFontBStrInLine(c.caption, w);
		else {
			str = BitmapFont.m_bmNormalFont.splitFontBStrInLine(c.caption.substring(0, curLength), w);
			curLength++;
		}
		int h = str.length * BitmapFont.m_bmNormalFont.getHeight();

		g.setColor(0xe8ffa7);
		g.fillRect(x - 5, y - h - 15, w + 10, h + 10);

		g.setColor(0xaa6600);
		g.drawRect(x - 5, y - h - 15, w + 10, h + 10);
		g.drawRect(x - 6, y - h - 16, w + 12, h + 12);

		int i;
		for (i = 0; i < str.length; i++) {

			BitmapFont.drawNormalFont(g, str[i], x, y - 10 - h + i * BitmapFont.m_bmNormalFont.getHeight(), 0x0c4a01,
					Graphics.LEFT | Graphics.TOP);
		}

		g.setColor(0xe8ffa7);
		g.fillTriangle(x + 65, y - 10, x + 85, y - 10, x + 45, y + 30);
	}

	private void updateFrame() {
		Command c = (Command) m_arrListMenu.elementAt(m_selected);
		if (curLength > 0 && curLength < c.caption.length()) {
			m_imounthTick++;
			if (m_imounthTick % 3 == 0) {
				m_bmouth = (byte) ((m_bmouth >= 2) ? CRes.random(0, 2) : ((m_bmouth == 1) ? 0 : 1));
			}
		} else {
			m_bmouth = 2;
		}
	}

	static String[] m_strTabName;
	private int m_iMoveText = 0;
	private int m_iWidthText = 0;

	private void paintListItem(Graphics g) {
		g.setClip(0, GameCanvas.hBottomBar + 4, GameCanvas.w, GameCanvas.h);
		int i;
		Command c;

		for (i = 0; i < m_arrListMenu.size(); i++) {
			c = (Command) m_arrListMenu.elementAt(i);
			if (i == m_selected && m_bMoveCommand == 0) {
				if (!m_bAction) {
					m_bAction = true;
					m_imounthTick = 0;
					m_iMoveText = 0;
					m_iWidthText = 0;
				}

				if (m_iMoveText > -50)
					m_iMoveText -= 10;
				else {
					if (m_iWidthText < BitmapFont.m_bmFont.stringWidth(m_strTabName[i]))
						m_iWidthText += 30;
					g.setClip(m_iPositionConmand[i][0] - 70 - m_iMoveText, m_iPositionConmand[i][1] - 20, m_iWidthText,
							40);
					BitmapFont.drawBoldFont(g, m_strTabName[i], m_iPositionConmand[i][0] - 70 - m_iMoveText,
							m_iPositionConmand[i][1] - 5, 0xffffff, Graphics.LEFT | Graphics.TOP);
					g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
					g.drawImage(GameResource.instance.imgArrowBig, m_iPositionConmand[i][0] - 70 - m_iMoveText
							+ m_iWidthText, m_iPositionConmand[i][1] - 10, 0);
				}

				g.drawImage(GameResource.instance.imgBoxFocus, m_iPositionConmand[i][0] + m_iMoveText,
						m_iPositionConmand[i][1], Graphics.HCENTER | Graphics.VCENTER);
				c.paint(g, m_iPositionConmand[i][0] + m_iMoveText, m_iPositionConmand[i][1]);
			} else {
				g.drawImage(GameResource.instance.imgBox, m_iPositionConmand[i][0], m_iPositionConmand[i][1],
						Graphics.VCENTER | Graphics.HCENTER);
				c.paint(g, m_iPositionConmand[i][0], m_iPositionConmand[i][1]);
			}
		}
	}

	private void scrollUp() {
		if (m_iMidPosCommand - GameResource.instance.imgBoxFocus.getHeight() / 2 < m_iPositionConmand[m_selected][1]) {

			m_selected = (m_selected + 1) % m_arrListMenu.size();

			int i, tmp = m_iPositionConmand[m_arrListMenu.size() - 1][2];
			int max = 0, min = 0;
			for (i = m_arrListMenu.size() - 1; i >= 0; i--) {
				if (i != 0)
					m_iPositionConmand[i][2] = m_iPositionConmand[i - 1][2];
				if (m_iPositionConmand[max][1] < m_iPositionConmand[i][1])
					max = i;
				if (m_iPositionConmand[min][1] > m_iPositionConmand[i][1])
					min = i;
			}
			m_iPositionConmand[0][2] = tmp;
			m_iPositionConmand[max][1] = m_iPositionConmand[min][1] - GameResource.instance.imgBoxFocus.getHeight() * 5
					/ 4 + 10;
		}
		m_bMoveCommand = 1;
		m_bAction = false;
	}

	private void scrollDown() {
		if (m_iMidPosCommand + GameResource.instance.imgBoxFocus.getHeight() / 2 > m_iPositionConmand[m_selected][1]) {
			if (m_selected == 0)
				m_selected = m_arrListMenu.size() - 1;
			else
				m_selected--;

			int i, tmp = m_iPositionConmand[0][2];
			int max = 0, min = 0;
			for (i = 0; i < m_arrListMenu.size(); i++) {
				if (i != m_arrListMenu.size() - 1)
					m_iPositionConmand[i][2] = m_iPositionConmand[i + 1][2];
				if (m_iPositionConmand[max][1] < m_iPositionConmand[i][1])
					max = i;
				if (m_iPositionConmand[min][1] > m_iPositionConmand[i][1])
					min = i;
			}
			m_iPositionConmand[m_arrListMenu.size() - 1][2] = tmp;
			m_iPositionConmand[min][1] = m_iPositionConmand[max][1] + GameResource.instance.imgBoxFocus.getHeight() * 5
					/ 4 - 10;
		}
		m_bMoveCommand = 2;
		m_bAction = false;
	}

	// TODO updatekey for button Back
	// private void updateKey_Back() {
	// if (GameCanvas.isPointer(0, 0,
	// GameResource.instance.imgMenuBg.getWidth(),
	// GameResource.instance.imgMenuBg.getHeight())) {
	// doBack();
	// }
	//
	// }

	public void updateKey() {
		// move cho icon
		// if (GameCanvas.instance.isMove()) {
		// HDCGameMidlet.sound
		// .openFile(HDCGameMidlet.instance, R.raw.dropdown);
		// HDCGameMidlet.sound.play();
		//
		// MainScr.gI().isTranlate = true;
		// if (m_Ellipse.length > 5)
		// //TODO mac dinh icon choi game
		// MainScr.gI().idx_IconTranlate = 6;
		// else
		// //TODO mac dinh icon tien len mien bac
		// MainScr.gI().idx_IconTranlate = 7;
		// MainScr.gI().resetFocus();
		//
		// if (GameCanvas.instance.px > m_Ellipse[0].mCenter.getX()) {
		// isLeft = true;
		// } else {
		// isLeft = false;
		// }
		//
		// GameCanvas.instance.px = GameCanvas.instance.pxLast = 0;
		// }

		// update key for ellipse
		for (int i = 0; i < m_Ellipse.length; i++) {
			if(m_Ellipse[i].updateKey())
				return;
		}

		// TODO update key for back
		// updateKey_Back();

		// if (GameCanvas.instance.hasPointerEvents()) {
		// if (GameCanvas.isPointerDown) {
		// m_iCallMoveCommand = (GameCanvas.pyLast - GameCanvas.py) / 20;
		// if (m_iCallMoveCommand > GameCanvas.hh)
		// m_iCallMoveCommand = GameCanvas.hh;
		// }
		// if (GameCanvas.isPointerClick) {
		// if (GameCanvas.isPointer(0, GameCanvas.hBottomBar,
		// GameCanvas.w, GameCanvas.h - 2 * GameCanvas.hBottomBar)) {
		// int i;
		// Command c;
		// for (i = 0; i < m_arrListMenu.size(); i++) {
		// if (i == m_selected
		// && GameCanvas.isPointer(
		// m_iPositionConmand[i][0] - 29
		// + m_iMoveText,
		// m_iPositionConmand[i][1] - 29,
		// 58 - m_iMoveText, 58)) {
		// c = (Command) m_arrListMenu.elementAt(i);
		// c.action.perform();
		// } else if (GameCanvas.isPointer(
		// m_iPositionConmand[i][0] - 29,
		// m_iPositionConmand[i][1] - 29, 58, 58))
		// m_iClickSelect = i;
		// }
		// }
		// }
		// }

		super.updateKey();
	}

	public void close() {
		GlobalService.sendMessageLogout();

		GameCanvas.loginScr.switchToMe();

		Session.gI().close();

		GameCanvas.endDlg();
	}

	// private void updateCommand() {
	//
	// for (int i = 0; i < m_arrListMenu.size(); i++) {
	// if (i == m_selected) {
	// if (m_iPositionConmand[i][0] > m_iMax) {
	// if (m_iPositionConmand[i][0] - m_iSpeedX < m_iMax)
	// m_iPositionConmand[i][0] = m_iMax;
	// else
	// m_iPositionConmand[i][0] -= m_iSpeedX;
	// } else if (m_iPositionConmand[i][0] < m_iMax) {
	// if (m_iPositionConmand[i][0] + m_iSpeedX > m_iMax)
	// m_iPositionConmand[i][0] = m_iMax;
	// else
	// m_iPositionConmand[i][0] += m_iSpeedX;
	// }
	// } else {
	// if (m_iPositionConmand[i][0] < m_iMin) {
	// if (m_iPositionConmand[i][0] + m_iSpeedX > m_iMin)
	// m_iPositionConmand[i][0] = m_iMin;
	// else
	// m_iPositionConmand[i][0] += m_iSpeedX;
	// }
	//
	// }
	// if (m_bMoveCommand != 0) {
	// if (m_bMoveCommand == 1) {
	// if (m_iPositionConmand[i][1] < m_iPositionConmand[i][2]) {
	// if (m_iPositionConmand[i][1] + m_iSpeedY > m_iPositionConmand[i][2])
	// m_iPositionConmand[i][1] = m_iPositionConmand[i][2];
	// else
	// m_iPositionConmand[i][1] += m_iSpeedY;
	// }
	// } else {
	// if (m_iPositionConmand[i][1] > m_iPositionConmand[i][2]) {
	//
	// if (m_iPositionConmand[i][1] - m_iSpeedY < m_iPositionConmand[i][2])
	// m_iPositionConmand[i][1] = m_iPositionConmand[i][2];
	// else
	// m_iPositionConmand[i][1] -= m_iSpeedY;
	// }
	// }
	// }
	//
	// }
	// if (m_iPositionConmand[m_selected][1] ==
	// m_iPositionConmand[m_selected][2]
	// && m_iPositionConmand[m_selected][0] == m_iMax) {
	// updateFrame();
	// m_bMoveCommand = 0;
	// }
	// }

	public Command getSelectedItem() {
		if (m_selected >= 0 && m_selected < m_arrListMenu.size()) {
			return (Command) m_arrListMenu.elementAt(m_selected);
		}
		return null;
	}

	public void update() {

		// TODO update icon
		if (isTranlate) {
			// item ben trai
			if (isLeft) {
				count -= 2;
			} else {
				// item ben phai
				count += 2;
			}

			for (int j = 0; j < m_Ellipse.length; j++) {
				m_Ellipse[j].setSpeed((float) Math.PI / 50 * count);
				m_Ellipse[j].Caculate_Location();
				m_Ellipse[j].setScale(m_Ellipse[j].Caculate_Scale());

				if ((m_Ellipse[j].getScale() > 0.998f) && m_Ellipse[j].getScale() <= 1.0f) {
					if (idx_IconTranlate == m_Ellipse[j].getIndex()) {
						isTranlate = false;
						m_Ellipse[j].isFocus = true;
						// HDCGameMidlet.instance.Toast("count :" + count);
					}
				}
			}
		}

		// for (int j = 0; j < m_Ellipse_1.length; j++)
		// m_Ellipse_1[j].update();

		// TODO update for hight light
		degree += 20;
		if (degree == 360)
			degree = 0;

		// if (m_iCallMoveCommand != 0) {
		// if (m_iCallMoveCommand > 0) {
		// m_iCallMoveCommand--;
		// scrollDown();
		// } else {
		// m_iCallMoveCommand++;
		// scrollUp();
		// }
		// }
//		if (m_iClickSelect != -1) {
//			if (m_iPositionConmand[m_selected][1] > m_iPositionConmand[m_iClickSelect][1])
//				scrollUp();
//			else if (m_iPositionConmand[m_selected][1] < m_iPositionConmand[m_iClickSelect][1])
//				scrollDown();
//			else
//				m_iClickSelect = -1;
//		}

		// CardFall c;
		// for (int i = 0; i < m_arrEffect.size(); i++) {
		// c = (CardFall) m_arrEffect.elementAt(i);
		// c.update();
		// if (c.m_posCenterEffect.y > GameCanvas.h - 20)
		// m_arrEffect.removeElementAt(i);
		// }
		// c = null;
		// updateMoveBG();
		// updateCommand();

//		m_itickEyes--;
//		if (m_itickEyes < 2) {
//			m_iEyes = true;
//		}
//		if (m_itickEyes <= 0) {
//			m_itickEyes = CRes.random(20, 30);
//			m_iEyes = false;
//		}
	}

	// @SuppressWarnings("unchecked")
	// private void updateMoveBG() {
	// deltaMove++;
	// if (deltaMove >= GameResource.instance.imgBackRound.getHeight()) {
	// deltaMove = 0;
	// m_arrEffect.addElement(new CardFall());
	// }
	// }

	public void sms() {
		// ListScr.gI().m_iTopScroll = (int) (40 /
		// HDCGameMidlet.instance.scale);
		// ListScr.gI().m_iHeightItem = (int) (70 /
		// HDCGameMidlet.instance.scale);
		final ListScr m_listScr = new ListScr();
		m_listScr.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
		m_listScr.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());

		Command napTienCmd = new Command(GameResource.select, new IAction() {

			public void perform() {

				final MoneyInfo mInfo = (MoneyInfo) m_listScr.getSelectItems();
				if (mInfo != null) {
					String lowerStr = mInfo.smsTo.toLowerCase();
					if (lowerStr.indexOf("card") >= 0) {
						// final TField[] list = new TField[2];
						// String[] name = new String[2];
						// name[0] = "Mã số thẻ:";
						// name[1] = "Số seri:";
						// for (int i = 0; i < 2; i++) {
						// list[i] = new TField();
						// }
						// list[0].setIputType(TField.INPUT_TYPE_NUMERIC);
						// list[1].setIputType(TField.INPUT_TYPE_ANY);
						// Command cmd = new Command("Gửi", new IAction() {
						// public void perform() {
						// if (list[1].getText().equals("")) {
						// // GameCanvas.startOKDlg("Bạn phải nhập số seri.");
						// HDCGameMidlet.instance.showDialog_Okie("Thông báo",
						// "Bạn phải nhập số seri.");
						// return;
						// }
						// if (list[0].getText().equals("")) {
						// // GameCanvas.startOKDlg("Bạn phải nhập mã số thẻ.");
						// HDCGameMidlet.instance.showDialog_Okie("Thông báo",
						// "Bạn phải nhập mã số thẻ.");
						// return;
						// }
						//
						// GlobalService.doRequestChargeMoneySimCard(mInfo.smsContent,
						// list[0].getText(), list[1].getText());
						// }
						// });
						//
						// Command close = new Command(GameResource.close, new
						// IAction() {
						//
						// public void perform() {
						// InputScr.gI().close();
						// }
						// });
						//
						// InputScr.gI().setInfo(list, name, cmd, close,
						// "Nạp thẻ cào", mInfo.info);
						// InputScr.gI().switchToMe(m_listScr);
						HDCGameMidlet.instance.showDialog_NapTien(mInfo.info, mInfo.smsContent);
					} else {
						// GameCanvas.startOKDlg("Bạn có muốn nạp tiền ko ?",
						// new IAction() {
						//
						// @Override
						// public void perform() {
						// // TODO Auto-generated method stub
						// HDCGameMidlet.sendSMS(mInfo.smsContent
						// + HDCGameMidlet.m_myPlayerInfo.itemName,
						// mInfo.smsTo);
						// }
						// });

						HDCGameMidlet.instance.showDialog_yes_no("Thông báo", "Bạn có muốn nạp tiền ko ?",
								new IAction() {

									@Override
									public void perform() {
										// TODO Auto-generated method stub
										HDCGameMidlet.sendSMS(mInfo.smsContent + HDCGameMidlet.m_myPlayerInfo.itemName,
												mInfo.smsTo);
									}
								});
					}
				}
			}
		});

		GameCanvas.endDlg();
		m_listScr.m_cmdCenter = napTienCmd;
		m_listScr.setListItem(DataManager.gI().m_vtSMS, null, true, false, false);

		m_listScr.switchToMe(GameCanvas.currentScreen, GameResource.chargeMoney, null, m_listScr.m_cmdCenter, null);
	}

	TabScr m_sTabShop;
	ShopScr m_sShop;
	ShopScr m_sItems;

	public void buyAvatar(int idAvatar) {
		HDCGameMidlet.m_myPlayerInfo.avatarId = idAvatar;
		ItemInfo item = (ItemInfo) m_sShop.getSelectItems();
		if (GameCanvas.currentScreen instanceof TabScr) {
			long gold = item.m_gold;
			int dina = item.m_dina;
			HDCGameMidlet.m_myPlayerInfo.subMoney(Math.abs((int) gold), (int) dina);
		} else {
			HDCGameMidlet.m_myPlayerInfo.gold = HDCGameMidlet.m_myPlayerInfo.gold - item.m_gold;
			HDCGameMidlet.m_myPlayerInfo.dina = HDCGameMidlet.m_myPlayerInfo.dina - item.m_dina;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getListShop(Message message) {
		// //////////
		m_sTabShop = new TabScr();
		m_sShop = new ShopScr();
		// m_sItems = new ListScr();
		// //////////
		int count = MessageIO.readShort(message);
		Vector list = new Vector();
		int i;
		ItemInfo itemInfo;
		for (i = 0; i < count; i++) {
			itemInfo = new ItemInfo();
			itemInfo.m_itemId = MessageIO.readInt(message);
			// itemInfo.m_imgID = MessageIO.readInt(message);
			itemInfo.itemId = MessageIO.readInt(message);
			itemInfo.itemName = MessageIO.readString(message);
			itemInfo.m_gold = (int) MessageIO.readLong(message);
			if (i < GameResource.listNameAvatar.length)
				itemInfo.itemName = GameResource.listNameAvatar[i];
			list.addElement(itemInfo);
		}

		m_sShop.m_cmdCenter = new Command(GameResource.buy, new IAction() {
			public void perform() {
				ItemInfo avata = (ItemInfo) m_sShop.getSelectItems();
				final int imgId = avata.m_imgID;
				final int itemId = avata.m_itemId;
				// GameCanvas.startOKDlg("Bạn muốn mua avatar này với giá " +
				// avata.m_gold
				// + " gold không?", new IAction() {
				// public void perform() {
				// GlobalService.onBuyAvatar(imgId, itemId);
				// GameCanvas.endDlg();
				// }
				// });

				HDCGameMidlet.instance.showDialog_yes_no("Thông báo", "Bạn có muốn mua avatar với giá " + avata.m_gold
						+ " gold không ?", new IAction() {
					public void perform() {
						GlobalService.onBuyAvatar(imgId + 1, itemId);
						GameCanvas.endDlg();
					}
				});
			}
		});
		m_sShop.m_cmdLeft = null;
		m_sShop.m_cmdRight = new Command(GameResource.close, new IAction() {
			public void perform() {
				m_sTabShop.close();
				m_sTabShop.removeAll();
				m_sTabShop = null;
				m_sShop = null;
				m_sItems = null;
			}
		});
		m_sShop.setListItems(list, (byte) 2, 0, GameCanvas.hBottomBar + 45, GameCanvas.w, GameCanvas.h
				- (GameCanvas.hBottomBar >> 1) - 45, "", 40, 18);
		m_sTabShop.addScreen(m_sShop, "Avatar");
		// ///////////////////////////////////////////
		m_sItems = new ShopScr();
		count = MessageIO.readShort(message);
		Vector listItem = new Vector();
		int maxHPerItem = 0;
		for (i = 0; i < count; i++) {
			itemInfo = new ItemInfo();
			itemInfo.m_bPaintRowForShopScr = true;
			itemInfo.m_itemId = MessageIO.readInt(message);
			// itemInfo.m_imgID = MessageIO.readInt(message);
			itemInfo.itemId = MessageIO.readInt(message);
			itemInfo.itemName = MessageIO.readString(message);
			itemInfo.m_gold = (int) MessageIO.readLong(message);
			itemInfo.m_dina = MessageIO.readInt(message);
			itemInfo.m_numberUseDays = MessageIO.readInt(message);
			itemInfo.m_numberUses = MessageIO.readInt(message);
			itemInfo.m_arrDes = itemInfo.getDes(MessageIO.readString(message), (int) (350 / HDCGameMidlet.scale));

			if (maxHPerItem < itemInfo.m_arrDes.size())
				maxHPerItem = itemInfo.m_arrDes.size();

			listItem.addElement(itemInfo);
		}

		m_sItems.m_cmdLeft = null;
		m_sItems.m_cmdRight = new Command(GameResource.close, new IAction() {
			public void perform() {
				m_sTabShop.close();
				m_sTabShop.removeAll();
				m_sTabShop = null;
				m_sShop = null;
				m_sItems = null;
			}
		});
		m_sItems.m_cmdCenter = new Command(GameResource.buy, new IAction() {
			public void perform() {
				ItemInfo items = (ItemInfo) m_sItems.getSelectItems();
				final int itemId = items.m_itemId;
				String strCost = "";
				if (items.m_gold > 0) {
					strCost = items.m_gold + " gold không?";
				}
				if (items.m_dina > 0) {
					strCost = items.m_dina + " dina không?";
				}

				GameCanvas.startOKDlg("Bạn muốn mua vật phẩm này với giá " + strCost, new IAction() {
					public void perform() {
						GlobalService.onBuyItem(itemId);
						GameCanvas.endDlg();
					}
				});
				strCost = null;
			}
		});
		m_sItems.setListItems(listItem, (byte) 3, 0, GameCanvas.hBottomBar + 45, GameCanvas.w, GameCanvas.h
				- (GameCanvas.hBottomBar >> 1) - 45, "", 40, 18);

		m_sTabShop.addScreen(m_sItems, "Vật phẩm");
		m_sTabShop.m_strTitle = GameResource.shop;
		m_sTabShop.setActiveScr(m_sShop);
		m_sTabShop.switchToMe(GameCanvas.currentScreen);

		list = null;
		itemInfo = null;
		GameCanvas.endDlg();
		// System.gc();
	}

	@SuppressWarnings("rawtypes")
	public Vector m_arrlistNickHighlight = new Vector();;

	private void highlightNickChatting() {
		if (m_arrListFriend != null) {
			PlayerInfo playerInfo;
			String nickHighlight;
			for (int i = 0; i < m_arrListFriend.size(); i++) {
				playerInfo = (PlayerInfo) m_arrListFriend.elementAt(i);
				for (int j = 0; j < m_arrlistNickHighlight.size(); j++) {
					nickHighlight = (String) m_arrlistNickHighlight.elementAt(j);
					if (playerInfo.itemName.equals(nickHighlight)) {
						playerInfo.isHighlight = true;
					}
				}
			}
			playerInfo = null;
			nickHighlight = null;
		}
	}

	@SuppressWarnings("unchecked")
	public void setNickHighlight(String nick) {
		if (!m_arrlistNickHighlight.contains(nick)) {
			m_arrlistNickHighlight.addElement(nick);
			PlayerInfo playerInfo;
			if (m_arrListFriend != null) {
				for (int i = 0; i < m_arrListFriend.size(); i++) {
					playerInfo = (PlayerInfo) m_arrListFriend.elementAt(i);
					if (playerInfo.itemName.equals(nick)) {
						playerInfo.isHighlight = true;
					}
				}
			}
			playerInfo = null;
		}
	}

	public void removeNickHighlight(String nick) {
		m_arrlistNickHighlight.removeElement(nick);
		if (ListScr.gI().m_arrItems != null) {
			PlayerInfo playerInfo;
			for (int i = 0; i < ListScr.gI().m_arrItems.size(); i++) {
				playerInfo = (PlayerInfo) ListScr.gI().m_arrItems.elementAt(i);
				if (playerInfo.itemName.equals(nick)) {
					playerInfo.isHighlight = false;
				}
			}
			playerInfo = null;
		}
	}

	public void showDialogGold(final PlayerInfo p) {
		final String nick = p.itemName;

		// HDCGameMidlet.instance.showDialog_ChuyenGold("Chuyển gold tới " +
		// nick, new IAction() {
		// public void perform() {
		// try {
		// if (GameCanvas.inputDlg.tfInput.getText().length() > 0) {
		// int money = Integer.parseInt(GameCanvas.inputDlg.tfInput.getText());
		// if (money > 0) {
		// GameCanvas.startWaitDlg();
		// GlobalService.onSendTranferMoney(nick, money);
		// } else {
		// HDCGameMidlet.instance.showDialog_Okie_withCommand("Thông báo","Số tiền chuyển lớn hơn 0!"
		// ,new IAction() {
		// public void perform() {
		// showDialogGold(p);
		// }
		// });
		//
		// }
		// } else {
		// HDCGameMidlet.instance.showDialog_Okie_withCommand("Thông báo","Nhập số gold cần chuyển!",
		// new IAction() {
		// public void perform() {
		// showDialogGold(p);
		// }
		// });
		// }
		// } catch (Exception ex) {
		// GameCanvas.startOK("Chỉ nhập số nguyên dương!", new IAction() {
		// public void perform() {
		// showDialogGold(p);
		// }
		// });
		// }
		// }
		// });

		GameCanvas.inputDlg.setInfo("Chuyển gold tới " + nick, new IAction() {
			public void perform() {
				try {
					if (GameCanvas.inputDlg.tfInput.getText().length() > 0) {
						int money = Integer.parseInt(GameCanvas.inputDlg.tfInput.getText());
						if (money > 0) {
							GameCanvas.startWaitDlg();
							GlobalService.onSendTranferMoney(nick, money);
						} else {
							HDCGameMidlet.instance.showDialog_Okie_withCommand("Thông báo",
									"Số tiền chuyển lớn hơn 0!", new IAction() {
										public void perform() {
											showDialogGold(p);
										}
									});

						}
					} else {
						HDCGameMidlet.instance.showDialog_Okie_withCommand("Thông báo", "Nhập số gold cần chuyển!",
								new IAction() {
									public void perform() {
										showDialogGold(p);
									}
								});
					}
				} catch (Exception ex) {
					GameCanvas.startOK("Chỉ nhập số nguyên dương!", new IAction() {
						public void perform() {
							showDialogGold(p);
						}
					});
				}
			}
		}, TField.INPUT_TYPE_NUMERIC);
		GameCanvas.inputDlg.show();
	}

	@SuppressWarnings("rawtypes")
	Vector m_arrListFriend = new Vector();

	@SuppressWarnings("rawtypes")
	public void setListMember(Vector list, Command left, Command center, Command right) {
		m_arrListFriend = list;

		// ListScr m_listScr = new ListScr();

		ListScr.gI().m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
		ListScr.gI().m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
		ListScr.gI().m_cmdCenter = center;
		ListScr.gI().setListItem(m_arrListFriend, null, true, false, true);
		highlightNickChatting();
		if (!(GameCanvas.currentScreen instanceof ListScr) && !ListScr.gI().m_strTitle.equals(GameResource.friend)) {
			ListScr.gI().switchToMe(GameCanvas.currentScreen, GameResource.friend, left, center, right);
		}
	}

	public void onReceivedForumLink(String title, String strData, final String strLink) {
		// GameCanvas.startOKDlg(strData, new IAction() {
		//
		// public void perform() {
		// // try {
		// // HDCGameMidlet.instance.platformRequest(strLink);
		// // } catch (ConnectionNotFoundException e) {
		// // e.printStackTrace();
		// // }
		// // HDCGameMidlet.instance.notifyDestroyed();
		// HDCGameMidlet.m_strForumLink = strLink;
		// try {
		// GameCanvas.endDlg();
		//
		// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
		// Uri.parse(strLink));
		// HDCGameMidlet.instance.startActivity(browserIntent);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// });

		HDCGameMidlet.instance.showDialog_yes_no(title, strData, new IAction() {

			public void perform() {
				// try {
				// HDCGameMidlet.instance.platformRequest(strLink);
				// } catch (ConnectionNotFoundException e) {
				// e.printStackTrace();
				// }
				// HDCGameMidlet.instance.notifyDestroyed();
				HDCGameMidlet.m_strForumLink = strLink;
				try {
					GameCanvas.endDlg();

					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strLink));
					HDCGameMidlet.instance.startActivity(browserIntent);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// TODO reset focus to tranlate points on ellipse
	public void resetFocus() {
		for (int i = 0; i < m_Ellipse.length; i++) {
			m_Ellipse[i].isFocus = false;
		}
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		switch (flag) {
		// trạng thái màn hình Mainscr bình thường
		case 0:
			// GameCanvas.startOKDlg("Bạn có muốn thoát khỏi game?", new
			// IAction() {
			// public void perform() {
			// close();
			// }
			// });
			HDCGameMidlet.instance.showDialog_yes_no("Thông báo", "Bạn có muốn thoát khỏi game không ?", new IAction() {
				public void perform() {
					close();
				}
			});
			break;
		// trạng thái sau khi click vào item "Chơi game"
		case 1:
			flag = 0;
			cloneEllipse(m_Ellipse_1);
			break;
		default:
			break;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		Vector vt = new Vector();
		vt.addElement(new Command("Đổi dina", new IAction() {

			public void perform() {
				// GameCanvas.inputDlg.setInfo("Đổi dina sang gold", new
				// IAction() {
				//
				// public void perform() {
				// int dina = 0;
				//
				// try {
				// dina =
				// Integer.parseInt(GameCanvas.inputDlg.tfInput.getText());
				//
				// GlobalService.onChangeDinaToGold(dina);
				//
				// GameCanvas.endDlg();
				// } catch (Exception ex) {
				// GameCanvas.startOKDlg("Không hợp lệ");
				// }
				// }
				// }, TField.INPUT_TYPE_NUMERIC);
				// GameCanvas.inputDlg.show();
				HDCGameMidlet.instance.showDialog_DoiDina();
			}
		}));
		vt.addElement(new Command("Đổi mật khẩu", new IAction() {

			public void perform() {
				// final TField[] list = new TField[3];
				// String[] name = new String[3];
				// name[0] = "Mật khẩu cũ:";
				// name[1] = "Mật khẩu mới:";
				// name[2] = "Nhập lại:";
				//
				// for (int i = 0; i < list.length; i++) {
				// list[i] = new TField();
				// }
				// list[0].setIputType(TField.INPUT_ALPHA_NUMBER_ONLY);
				// list[1].setIputType(TField.INPUT_TYPE_PASSWORD);
				// list[2].setIputType(TField.INPUT_TYPE_PASSWORD);
				//
				// Command cmd = new Command("Gửi", new IAction() {
				// public void perform() {
				// if (list[1].getText().length() < 5) {
				// // GameCanvas.startOKDlg("Mật khẩu phải nhiều hơn 5 kí tự");
				// HDCGameMidlet.instance.showDialog_Okie("Thông báo",
				// "Mật khẩu phải nhiều hơn 5 kí tự");
				// return;
				// }
				// if (!list[1].getText().equals(list[2].getText())) {
				// // GameCanvas.startOKDlg("Mật khẩu nhập lại không khớp");
				// HDCGameMidlet.instance.showDialog_Okie("Thông báo",
				// "Mật khẩu nhập lại không khớp");
				// return;
				// }
				//
				// GameCanvas
				// .startOKDlg(
				// "Bạn chắc chắn muốn thay đổi mật khẩu ?. Một tin nhắn kết quả sẽ trả về cho bạn.",
				// new IAction() {
				// public void perform() {
				// GlobalService.doRequestChangePass(
				// list[0].getText(), list[1].getText());
				// InputScr.gI().close();
				// GameCanvas.startWaitDlg();
				// }
				// });
				//
				// }
				// });
				//
				// Command close = new Command(GameResource.close, new IAction()
				// {
				//
				// public void perform() {
				// InputScr.gI().close();
				// }
				// });
				//
				// InputScr.gI().setInfo(list, name, cmd, close, "",
				// GameResource.changePass);
				// InputScr.gI().switchToMe(MainScr.gI());
				HDCGameMidlet.instance.showDialog_DoiMatKhau();
			}
		}));

		GameCanvas.instance.menu.startAt(vt, 0);
	}

	// TODO create Image background
	public void createImageBackground(int col, int row) {
		int w = col * GameResource.instance.imgPoupPanel.getWidth() / 9;
		int h = row * GameResource.instance.imgPoupPanel.getHeight();
		imgBackgroundInfo = Image.createImage(w, h + GameResource.instance.imgPopupArrow.getHeight());

		Graphics g1 = imgBackgroundInfo.getGraphics();
		// create panel for popup
		PaintPopup.gI().paintPopup_Panel(g1, row, col);
		// paint arrow
		g1.drawImage(GameResource.instance.imgPopupArrow, w / 3, h, Graphics.LEFT | Graphics.TOP);
	}

}
