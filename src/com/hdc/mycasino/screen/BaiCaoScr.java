package com.hdc.mycasino.screen;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;

public class BaiCaoScr extends PlayGameScr {
	static BaiCaoScr instance;
	Command cmd_openCard;
	Command cmd_openAllCard;
	boolean[] m_arrOpenCard;
	// private Card[][] m_arrOtherCards;
	Command cmdBack;
	private int idx_OpenOneCard = 0;
	private int idx_OpenAllCards = 0;
	private int idx_NanBai_1 = 0;
	private int idx_NanBai_2 = 0;

	// TODO degree (lật bài kiểu 1)
	int degree = -90;
	int degree1 = -90;
	int x1 = GameCanvas.hw;
	int y1 = GameCanvas.hh;
	int m_x = GameCanvas.hw;
	// TODO lật bài kiểu 2
	int a = GameCanvas.hw;
	int b = GameCanvas.hd3;
	int a1 = GameCanvas.hw;
	int b1 = GameCanvas.hh;
	int a2 = GameCanvas.hw;
	int b2 = GameCanvas.hd3;
	int count = -1;
	// TODO kieu lat bai
	int flagType = -1;
	// TODO finish
	boolean m_finish = false;

	// TODO update (lật bài kiểu 1)
	private void update_Type_1() {
		if (degree > -135) {
			degree -= 5;
		} else {
			if (degree1 < -45)
				degree1 += 5;
			else if (m_x < (GameCanvas.hw + 30))
				m_x += 5;
			else if (m_x >= (GameCanvas.hw + 30)) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				m_finish = true;
				reset_Type_1();
				cmd_openAllCard.action.perform();
			}
		}
	}

	// TODO paint lật bài kiểu 1
	private void paint_Type_1(Graphics g) {
		g.setColor(Color.BLACK);
		g.setAlpha(220);
		g.fillRectWithTransparent(0, 0, GameCanvas.w, GameCanvas.h);

		g.drawImageDegree(
				GameResource.instance.m_imgCards[Card.cardPaint[m_arrOtherCards[m_iMyPosInVt][2].m_id]],
				m_x, y1, degree1, Graphics.LEFT | Graphics.TOP);
		g.drawImageDegree(
				GameResource.instance.m_imgCards[Card.cardPaint[m_arrOtherCards[m_iMyPosInVt][1].m_id]],
				x1, y1, degree1, Graphics.LEFT | Graphics.TOP);
		g.drawImageDegree(
				GameResource.instance.m_imgCards[Card.cardPaint[m_arrOtherCards[m_iMyPosInVt][0].m_id]],
				x1, y1, -90, Graphics.LEFT | Graphics.TOP);
		g.drawImageDegree(GameResource.instance.imgTienLen_CardBack2, x1, y1, degree, Graphics.LEFT
				| Graphics.TOP);
	}

	// TODO reset lật bài kiểu 1
	private void reset_Type_1() {
		degree = -90;
		degree1 = -90;
		m_x = GameCanvas.hw;
		flagType = -1;
	}

	// TODO update (lật bài kiểu 2)
	private void update_Type_2() {
		if (a < (GameCanvas.hw + 30)) {
			a += 5;
			b += 5;
		} else {
			if (a1 < GameCanvas.hw + 30) {
				a1 += 5;
				b1 += 5;
				a += 5;
				b += 5;
			} else {
				if (a2 < GameCanvas.hw + 30) {
					a2 += 5;
					b2 += 5;
					a1 += 5;
					b1 += 5;
					a += 5;
					b += 5;
				} else {
					if (count < 11)
						count++;
					else {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						m_finish = true;
						reset_Type_2();
						cmd_openAllCard.action.perform();
					}
				}
			}
		}
	}

	// TODO paint lật bài kiểu 2
	private void paint_Type_2(Graphics g) {
		g.setColor(Color.BLACK);
		g.setAlpha(220);
		g.fillRectWithTransparent(0, 0, GameCanvas.w, GameCanvas.h);

		g.drawImage(
				GameResource.instance.m_imgCards[Card.cardPaint[m_arrOtherCards[m_iMyPosInVt][2].m_id]],
				x1, GameCanvas.hd3, Graphics.LEFT | Graphics.TOP);
		if (count == -1 || (count % 2 == 0))
			g.drawImage(GameResource.instance.imgBaiCao_IconHand, x1, GameCanvas.hd3, Graphics.LEFT
					| Graphics.TOP);

		g.drawImage(
				GameResource.instance.m_imgCards[Card.cardPaint[m_arrOtherCards[m_iMyPosInVt][1].m_id]],
				a2, b2, Graphics.LEFT | Graphics.TOP);
		if (count == -1 || (count % 2 == 0))
			g.drawImage(GameResource.instance.imgBaiCao_IconHand, a2, b2, Graphics.LEFT
					| Graphics.TOP);

		g.drawImage(
				GameResource.instance.m_imgCards[Card.cardPaint[m_arrOtherCards[m_iMyPosInVt][0].m_id]],
				a1, b1, Graphics.LEFT | Graphics.TOP);
		if (count == -1 || (count % 2 == 0))
			g.drawImage(GameResource.instance.imgBaiCao_IconHand, a1, b1, Graphics.LEFT
					| Graphics.TOP);

		g.drawImage(GameResource.instance.imgTienLen_CardBack2, a, b, Graphics.LEFT | Graphics.TOP);

	}

	// TODO reset lật bài kiểu 2
	private void reset_Type_2() {
		a = GameCanvas.hw;
		b = GameCanvas.hd3;
		a1 = GameCanvas.hw;
		b1 = GameCanvas.hd3;
		a2 = GameCanvas.hw;
		b2 = GameCanvas.hd3;
		count = -1;
		flagType = -1;
	}

	// Index points to current card of player.
	public static BaiCaoScr gI() {
		if (instance == null) {
			instance = new BaiCaoScr((byte) 4, (byte) 3);
		}
		return instance;
	}

	public static void clear() {
		if (instance != null) {
			instance.cmd_openCard = null;
			instance.cmd_openAllCard = null;
			instance.m_arrOpenCard = null;
			instance.m_arrOtherCards = null;
		}
		instance = null;
	}

	public BaiCaoScr(byte maxPlayer, byte maxCard) {
		super(maxPlayer, maxCard);

		cmdBack = new Command("Rời bàn", new IAction() {

			public void perform() {
				if (m_bIsPlaying) {
//					GameCanvas.startOKDlg(
//							"Nếu thoát khi đang chơi bạn sẽ bị xử thua. Bạn có muốn thoát",
//							new IAction() {
//								public void perform() {
//									GameCanvas.endDlg();
//									m_iStatusOutGame = 2;
//									doLeaveBoard();
//								}
//							});
					HDCGameMidlet.instance
					.showDialog_yes_no(
							"Thông báo",
							"Nếu thoát khi đang chơi bạn sẽ bị xử thua.\n Bạn có muốn thoát không ?",
							new IAction() {
								public void perform() {
									GameCanvas.endDlg();
									m_iStatusOutGame = 2;
									doLeaveBoard();
								}
							});					
				} else {
					m_iStatusOutGame = 0;
					doLeaveBoard();
				}
			}
		});

		cmd_openCard = new Command("Mở bài", new IAction() {
			public void perform() {
				m_selected++;
				if (m_selected < 3) {
					try {
						if (!m_arrOpenCard[m_selected]) {
							GlobalService.onFireCardWithIndex(boardId, m_selected);
//							m_selected++;
							m_cmdCenter = null;
							m_cmdRight = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (m_selected == 3){
					m_finish = true;
					m_selected = -1;
				}
			}
		});

		cmd_openAllCard = new Command("Mở hết", new IAction() {
			public void perform() {
				GlobalService.sendMessageOpenAllCards();
				m_cmdCenter = null;
				m_cmdRight = null;
			}
		});

		// int i, j;
		// m_arrOtherCards = new Card[m_iMaxPlayer][m_iMaxNumCard];
		// for (i = 0; i < m_iMaxPlayer; i++)
		// for (j = 0; j < m_iMaxNumCard; j++)
		// m_arrOtherCards[i][j] = new Card(-1);

		m_arrOpenCard = new boolean[m_iMaxNumCard];
	}

	public void switchToMe(String ownerString) {
		super.switchToMe(ownerString);
		Card.setCardType(Card.PHOM);
		initAllCard(false);
		// m_bIsStartDeliver = false;
		m_selected = -1;
		loadImg();
	}

	public void loadImg() {
		if (GameResource.instance.imgArrowBig == null)
			GameResource.instance.imgArrowBig = ImagePack.createImage(ImagePack.BIG_ARROW_PNG);
	}

	// TODO select button
	private boolean selectButton(int x, int y) {
		if (GameCanvas.isPointer(x, y, GameResource.instance.m_frameTienLen_Button.frameWidth,
				GameResource.instance.m_frameTienLen_Button.frameHeight)) {
			return true;
		}
		return false;
	}

	// TODO update key for button game
	private void updateKey_ButtonGame() {

		if (GameCanvas.isPointerDown) {
			// TODO button open one card
			if (selectButton(0, GameCanvas.h
					- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_OpenOneCard = 1;
			}
			// TODO button open all cards
			if (selectButton(GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_OpenAllCards = 1;
			}
			int w = (GameCanvas.hw - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;
			// TODO button nặn bài 1
			if (selectButton(w, GameCanvas.h
					- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_NanBai_1 = 1;
			}
			// TODO button nặn bài 2
			if (selectButton(w + GameCanvas.hw, GameCanvas.h
					- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_NanBai_2 = 1;
			}

		}

		if (GameCanvas.isPointerClick) {

			// TODO button mở bài
			if (selectButton(0, GameCanvas.h
					- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				idx_OpenOneCard = 0;
				cmd_openCard.action.perform();
			}
			// TODO button mở hết bài
			if (selectButton(GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				m_finish = true;
				idx_OpenAllCards = 0;
				cmd_openAllCard.action.perform();
				// m_cmdHit.action.perform();
			}
			int w = (GameCanvas.hw - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;
			// TODO button nặn bài 1
			if (selectButton(w, GameCanvas.h
					- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				idx_NanBai_1 = 0;
				flagType = 0;
			}
			// TODO button nặn bài 2
			if (selectButton(w + GameCanvas.hw, GameCanvas.h
					- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				idx_NanBai_2 = 0;
				flagType = 1;
			}

		}
	}

	public void startGame(Message message) {
		// read current player's cards
		super.startGame();

		m_selected = 0;

		flagStart = 4;
		m_finish = false;

		initAllCard(false);
		byte c[] = MessageIO.readBytes(message);
		int i;
		for (i = 0; i < c.length; i++) {
			m_arrOtherCards[m_iMyPosInVt][i].m_id = c[i];
			m_arrOpenCard[i] = false;
		}

		m_bIsPlaying = true;
		m_iGameTick = TURN_TIME_OUT;
		m_strWhoTurn = HDCGameMidlet.m_myPlayerInfo.itemName;

		m_selected = -1;
		m_cmdRight = null;
		m_cmdCenter = null;
		// m_bIsStartDeliver = true;
	}

	// TODO paint button
	private void paintButton(Graphics g) {
		// paint line button
		g.drawImage(GameResource.instance.imgTienLen_LineButton, 0, GameCanvas.h, Graphics.LEFT
				| Graphics.BOTTOM);

		int w = (GameCanvas.hw - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;

		// TODO paint button trái (Mở 1 lá)
		PaintPopup.gI().paintButton_Game(g, 0,
				GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight,
				idx_OpenOneCard, "Mở 1 lá");
		// TODO paint button phải (Mở hết)
		PaintPopup.gI().paintButton_Game(g,
				GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth,
				GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight,
				idx_OpenAllCards, "Mở hết");

		// TODO paint button nặn bài 1
		PaintPopup.gI().paintButton_Game(g, w,
				GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight,
				idx_NanBai_1, "Nặn bài 1");

		// TODO paint button nặn bài 2
		PaintPopup.gI().paintButton_Game(g, w + GameCanvas.hw,
				GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight,
				idx_NanBai_2, "Nặn bài 2");
	}

	private void initAllCard(boolean isHave) {
		int xCard = m_iMaxNumCard * m_iUseWCard;
		int yOwner = GameCanvas.h - GameResource.instance.m_imgCards[0].getHeight()
				- GameResource.instance.m_frameTienLen_Button.frameHeight;

		int i, j;
		for (i = 0; i < m_iMaxPlayer; i++) {
			for (j = 0; j < m_iMaxNumCard; j++) {
				m_arrOtherCards[i][j].m_id = 0;

				m_arrOtherCards[i][j].isUpCard = true;
				m_arrOtherCards[i][j].isCallMove = true;

				m_arrOtherCards[i][j].m_x = GameCanvas.hw;
				m_arrOtherCards[i][j].m_y = GameCanvas.ch;
				switch (m_arrPos[i].anchor) {
				case 0:
					m_arrOtherCards[i][j].setNextNewPosTo(GameCanvas.hw - xCard / 2 + j
							* m_iUseWCard, yOwner, 0, 1f);
					break;
				case 1:
					m_arrOtherCards[i][j].setNextNewPosTo(j * m_iUseWCard/2, m_arrPos[i].y
							- GameResource.instance.m_imgCards[0].getHeight(), 0, 1.5f);
					break;
				case 2:
					m_arrOtherCards[i][j].setNextNewPosTo(GameCanvas.hw - xCard / 2 + j
							* m_iUseWCard/2, m_arrPos[i].y, 0, 1.5f);
					break;
				default:
					m_arrOtherCards[i][j].setNextNewPosTo(GameCanvas.w - xCard + j * m_iUseWCard/2,
							m_arrPos[i].y - GameResource.instance.m_imgCards[0].getHeight(), 0,
							1.5f);
					break;
				}
				if (isHave) {
					m_arrOtherCards[i][j].m_x = m_arrOtherCards[i][j].xTo;
					m_arrOtherCards[i][j].m_y = m_arrOtherCards[i][j].yTo;
				}
			}
		}
	}

	@Override
	protected void updateForOrtherCard() {
		int i, j;
		for (i = 0; i < m_iMaxPlayer; i++)
			if (m_arrPlayers[i].itemId != -1)
				for (j = 0; j < m_iMaxNumCard; j++)
					if (m_arrOtherCards[i][j].m_id != -1 && !m_arrOtherCards[i][j].isCallMove) {
						if (m_bIsStartDeliver)
							m_arrOtherCards[i][j].translateNewArc(5);
						else
							m_arrOtherCards[i][j].translate(2);
					}
	}

	public void onAllCardPlayerFinish(Message message) {
		String nick = MessageIO.readString(message);
		byte c[] = MessageIO.readBytes(message);
		int pos = findPlayerPos(nick);
		if (pos < 0)
			return;
		int i;
		for (i = 0; i < c.length; i++) {
			m_arrOtherCards[pos][i].m_id = c[i];
			if (m_arrOtherCards[pos][i].isUpCard && !m_arrOtherCards[pos][i].isActionRotate) {
				m_arrOtherCards[pos][i].setActionRotate();
			}
		}
		nick = null;

		if (flagType == 0)
			reset_Type_1();
		else if (flagType == 1)
			reset_Type_2();
		flagType = -1;
	}

	public void openCard(String nick, int cardIndex, int cardValue) {
		int pos = findPlayerPos(nick);
		if (pos < 0)
			return;
		if (cardIndex >= 0 && cardIndex < 3) {
			if (m_arrOtherCards[pos][cardIndex].isUpCard)
				m_arrOtherCards[pos][cardIndex].setActionRotate();
			m_arrOtherCards[pos][cardIndex].m_id = cardValue;
			if (pos == m_iMyPosInVt) {
				m_arrOpenCard[cardIndex] = true;
			}
		}
		byte count = 0, i;
		for (i = 0; i < m_iMaxNumCard; i++)
			if (m_arrOpenCard[i])
				count++;

		m_cmdRight = cmd_openAllCard;
		m_cmdCenter = cmd_openCard;
		if (count >= m_iMaxNumCard) {
			m_cmdCenter = null;
			m_cmdRight = null;
		}
	}

	// private boolean m_bIsStartDeliver = false;

	// private void deliverCard() {
	// if (m_bIsStartDeliver) {
	// byte i, j = 0;
	// for (i = 0; i < m_arrPlayers.length; i++) {
	// if (m_arrPlayers[i].itemId != -1) {
	// for (j = 0; j < m_iMaxNumCard; j++) {
	// if (m_arrOtherCards[i][j].isCallMove) {
	// m_arrOtherCards[i][j].isCallMove = false;
	// break;
	// }
	// }
	//
	// }
	// }
	// if (i >= m_iMaxPlayer && j >= m_iMaxNumCard) {
	// m_bIsStartDeliver = false;
	// m_selected = 0;
	// m_cmdRight = cmd_openAllCard;
	// m_cmdCenter = cmd_openCard;
	// }
	// }
	// }

	public void update() {
		// deliverCard();

		// TODO update cho nặn bài
		if (flagType == 0 && !m_finish)
			update_Type_1();
		else if (flagType == 1)
			update_Type_2();

		// if (m_arrOtherCards != null) {
		// byte i, j;
		// for (i = 0; i < m_iMaxPlayer; i++) {
		// if (m_arrPlayers[i].itemId != -1) {
		// for (j = 0; j < m_iMaxNumCard; j++) {
		// if (m_arrOtherCards[i][j].m_id != -1 &&
		// !m_arrOtherCards[i][j].isCallMove) {
		// m_arrOtherCards[i][j].translate(2);
		// }
		// }
		// }
		// }
		// }
		super.update();
	}

	public void paintPlayGame(Graphics g) {

		// TODO paint title
		GameResource.instance.m_frameTienLen_Title.drawFrame(0, GameCanvas.w / 2,
				GameCanvas.h / 8 * 5, Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

		// TODO paint button
		if (flagStart == 4 && !m_finish) {
			paintButton(g);
		}

		if (m_arrOtherCards != null) {
			byte i, j;
			for (i = 0; i < m_iMaxPlayer; i++) {
				if (m_arrPlayers[i].itemId != -1) {
					for (j = 0; j < m_iMaxNumCard; j++) {
						if (m_arrOtherCards[i][j].m_id != -1 && !m_arrOtherCards[i][j].isCallMove) {
							m_arrOtherCards[i][j].paint(g);
						}
					}
				}
			}
		}

		// TODO vẽ lá bài trong khi nặn
		if (flagType == 0)
			paint_Type_1(g);
		else if (flagType == 1)
			paint_Type_2(g);

	}

	public void actionEndGame() {
		super.actionEndGame();
		for (byte i = 0; i < m_iMaxPlayer; i++)
			resetCard(m_arrOtherCards[i], -1);
	}

	public void onUserJoinTable(int tbID, String nick, boolean isChampion, int pos, long money,
			int level, int avatarId, int playerStatus) {
		super.onUserJoinTable(tbID, nick, isChampion, pos, money, level, avatarId, playerStatus);
		if (!m_bIsPlaying && playerStatus == 2) {
			initAllCard(true);

			byte i, j;
			for (i = 0; i < m_iMaxPlayer; i++) {
				if (m_arrPlayers[i].itemId != -1 && pos != i) {
					for (j = 0; j < m_iMaxNumCard; j++) {
						m_arrOtherCards[i][j].isCallMove = false;
					}
				}
			}
		}
	}

	public void updateKey() {
		// TODO updateKey for button game
		if (flagStart == 4 && !m_finish)
			updateKey_ButtonGame();

		if (!m_bIsStartDeliver) {
			if (GameCanvas.isPointerClick) {
				byte i = (byte) (m_iMaxNumCard - 1);
				int cW = GameResource.instance.imgCard.getWidth();
				int cH = GameResource.instance.imgCard.getHeight();
				for (; i >= 0; i--) {
					if (GameCanvas.isPointer(m_arrOtherCards[m_iMyPosInVt][i].m_x,
							m_arrOtherCards[m_iMyPosInVt][i].m_y, cW, cH)) {
						if (m_selected == i && m_cmdCenter != null)
							m_cmdCenter.action.perform();
						else
							m_selected = i;
						break;
					}
				}
			}

			if (GameCanvas.keyPressed[4] || GameCanvas.keyPressed[2]) {
				m_selected--;
				if (m_selected < 0)
					m_selected = m_iMaxNumCard - 1;
				GameCanvas.keyPressed[4] = false;
				GameCanvas.keyPressed[2] = false;
			} else if (GameCanvas.keyPressed[6] || GameCanvas.keyPressed[8]) {
				m_selected++;
				if (m_selected >= m_iMaxNumCard)
					m_selected = 0;
				GameCanvas.keyPressed[6] = false;
				GameCanvas.keyPressed[8] = false;
			}
		}
		super.updateKey();
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		if (m_cmdLeft != null)
			m_cmdLeft.action.perform();
		else
			GameCanvas.instance.menu.m_list = null;
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		if (cmdBack != null)
			cmdBack.action.perform();

	}
}
