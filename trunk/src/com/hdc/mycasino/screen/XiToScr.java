package com.hdc.mycasino.screen;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.animation.FlyImage;
import com.hdc.mycasino.animation.TranslateImage;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.PlayerInfo;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.GameResource;

public class XiToScr extends PlayGameScr {
	public static XiToScr instance;

	public int nTurn;
	public long betGold = 0;
	public long lastCuoc = 0;
	public long maxGoldCanBet = 0;

	private Card[][] m_arrOtherCards;

	private Command cmdTheo;
	private Command cmdTo_ToThem;
	private Command cmdBo;
	Command cmdBack;

	// TODO index for button game
	private int idx_To = 0;
	private int idx_UpBo = 0;
	private String mText_Button = "";
	int betCount = 0;
	int flag_To_Theo = 0;

	public static XiToScr gI() {
		if (instance == null) {
			instance = new XiToScr((byte) 4, (byte) 5);
		}
		return instance;
	}

	public static void clear() {
		if (instance != null) {
			instance.m_arrOtherCards = null;
			instance.cmdTheo = null;
			instance.cmdTo_ToThem = null;
			instance.cmdBo = null;
		}
		instance = null;
	}

	public XiToScr(byte maxPlayer, byte maxNum) {
		super(maxPlayer, maxNum);

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

		byte i, j;
		m_arrOtherCards = new Card[maxPlayer][maxNum];

		for (i = 0; i < maxPlayer; i++)
			for (j = 0; j < maxNum; j++)
				m_arrOtherCards[i][j] = new Card(-1);

		cmdTheo = new Command("Theo", new IAction() {
			public void perform() {
				betCount++;
				TranslateImage f = new TranslateImage();
				createImageBetMoney();
				f.startEffect(m_arrPos[0].x, m_arrPos[0].y, GameCanvas.w / 2, GameCanvas.h / 2,
						imgBetMoney);

				if (nTurn < 4) {
					GlobalService.sendMessageTheo(boardId);
				} else {
					GlobalService.sendMessageToThem(0);
				}
				m_cmdLeft = menuNormalCmd;
				m_cmdCenter = null;
				m_cmdRight = null;
			}
		});

		cmdBo = new Command(GameResource.skip, new IAction() {
			public void perform() {
				GlobalService.onSendSkipTurn(roomId, boardId);
				m_cmdCenter = null;
				m_cmdRight = null;
				m_cmdLeft = menuNormalCmd;
			}
		});

		cmdTo_ToThem = new Command(GameResource.to_bai, new IAction() {
			public void perform() {
				if (maxGoldCanBet <= 0) {
					// GameCanvas.startOKDlg("Không thể tố");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Không thể tố");
					return;
				}
				long defMoney = betGoldOfTable;
				if (defMoney >= maxGoldCanBet)
					defMoney = maxGoldCanBet;
				//
				// GameCanvas.inputDlg.setInfo(GameResource.to_bai, new
				// IAction() {
				// public void perform() {
				// String value = GameCanvas.inputDlg.tfInput.getText()
				// .trim();
				// long betGold = 0;
				// try {
				// betGold = Long.parseLong(value);
				// } catch (Exception e) {
				// GameCanvas.startOKDlg(GameResource.invalid);
				// return;
				// }
				// if (betGold <= 0) {
				// GameCanvas.startOKDlg(GameResource.invalid);
				// return;
				// }
				// if (nTurn < 4) {
				// GlobalService.sendMessageTo(betGold);
				// } else {
				// GlobalService.sendMessageToThem(betGold);
				// }
				// GameCanvas.endDlg();
				// m_cmdCenter = null;
				// }
				// }, TField.INPUT_TYPE_NUMERIC, maxGoldCanBet, defMoney);
				// GameCanvas.inputDlg.show();

				HDCGameMidlet.instance.showDialog_ToTienCuoc(maxGoldCanBet, defMoney);
			}
		});
	}

	public void update() {
		byte i, j;
		for (i = 0; i < m_arrPlayers.length; i++) {
			if (m_arrPlayers[i] != null && m_arrPlayers[i].itemName.length() > 0) {
				for (j = 0; j < m_iMaxNumCard; j++) {
					if (m_arrOtherCards[i][j].m_id != -1) {
						m_arrOtherCards[i][j].translate(2);
					}
				}
			}
		}
		super.update();
	}

	@Override
	public void updateKey() {
		// TODO Auto-generated method stub
		if (flagStart==4 && m_strWhoTurn.equals(HDCGameMidlet.instance.m_myPlayerInfo.itemName))
			updateKey_Button();

		super.updateKey();
	}

	private void resetOtherCards() {
		byte i, j;
		int HPerCard = 22;
		int left = ((GameCanvas.w - (m_iMaxNumCard + 1) * m_iUseWCard) >> 1);
		int MaxTop = GameCanvas.ch - (m_iMaxNumCard * HPerCard >> 1)
				- GameResource.instance.imgCard.getHeight();
		MaxTop = (MaxTop > 0) ? MaxTop : 0;
		int x = 0;
		for (i = 0; i < m_iMaxPlayer; i++) {
			x = m_arrPos[i].y + (m_iMaxNumCard * HPerCard >> 1)
					+ GameResource.instance.imgCard.getHeight() - HPerCard;
			x = (x > GameCanvas.h - GameCanvas.hBottomBar
					- (GameResource.instance.imgCard.getHeight() * 3 >> 2)) ? 0 : x;
			for (j = 0; j < m_iMaxNumCard; j++) {
				m_arrOtherCards[i][j].m_id = -1;

				if (j == 0) {
					m_arrOtherCards[i][j].isSpecial = true;
				}
				m_arrOtherCards[i][j].isUpCard = true;

				m_arrOtherCards[i][j].m_x = GameCanvas.hw;
				m_arrOtherCards[i][j].m_y = GameCanvas.ch;

				if (m_arrPos[i].anchor == 1) {
					m_arrOtherCards[i][j].xTo = ((x > 0) ? m_arrPos[i].x + 22 : 0);
					m_arrOtherCards[i][j].yTo = m_arrPos[i].y - (m_iMaxNumCard * HPerCard >> 1) + j
							* HPerCard;
				} else if (m_arrPos[i].anchor == 3) {
					m_arrOtherCards[i][j].xTo = ((x > 0) ? m_arrPos[i].x - 52 : GameCanvas.w
							- GameResource.instance.imgCard.getWidth() / 2);
					m_arrOtherCards[i][j].yTo = m_arrPos[i].y - (m_iMaxNumCard * HPerCard >> 1) + j
							* HPerCard;
				} else {
					m_arrOtherCards[i][j].xTo = left + j * m_iUseWCard;
					m_arrOtherCards[i][j].yTo = GameResource.instance.imgHeaderBg.getHeight();
				}
			}
		}
	}

	public void stopGame() {
		lastCuoc = 0;
		betGold = 0;
		resetOtherCards();
		super.stopGame();
	}

	public void switchToMe(String ownerString) {
		super.switchToMe(ownerString);
		Card.setCardType(Card.PHOM);
		lastCuoc = 0;
		betGold = 0;
		resetOtherCards();
	}

	public void startGame(Message message) {
		super.startGame();

		flagStart = 4;

		resetOtherCards();
		byte c[] = MessageIO.readBytes(message);
		int i;
		int left = ((GameCanvas.w - (c.length + 1) * m_iUseWCard) >> 1);
		int y = GameCanvas.h - GameResource.instance.m_imgCards[0].getHeight()
				- GameResource.instance.m_frameTienLen_Button.frameHeight;

		for (i = 0; i < c.length; i++) {
			m_arrOtherCards[m_iMyPosInVt][i].m_id = c[i];
			m_arrOtherCards[m_iMyPosInVt][i].setActionRotate();
			m_arrOtherCards[m_iMyPosInVt][i].xTo = left + i * m_iUseWCard;
			m_arrOtherCards[m_iMyPosInVt][i].yTo = y;
		}

		nTurn = 0;
		lastCuoc = 0;
		betGold = 0;

		GameCanvas.endDlg();
		System.gc();
	}

	public void onRankUser(String nick, byte rank) {

	}

	public void onSetTurn(String nick, Message message) {
		if (!m_bIsPlaying) {
			return;
		}

		byte info = MessageIO.readByte(message);

		m_strWhoTurn = nick;
		m_iGameTick = TURN_TIME_OUT;
		if (nick.toLowerCase().equals(HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
			nTurn++;
			if (info == 1) {// nguoi dc quyen to
				mText_Button = "Tố";
				m_cmdCenter = cmdTo_ToThem;
				m_cmdRight = null;

				if (lastCuoc == 0) {

				} else {
					// mText_Button = "Theo";
					flag_To_Theo = 1;
					m_cmdLeft = cmdTheo;
					m_cmdRight = cmdBo;
				}
			} else {
				mText_Button = "Theo";
				m_cmdCenter = cmdTheo;
				m_cmdRight = cmdBo;
			}
		} else {
			m_cmdLeft = menuNormalCmd;
			m_cmdRight = null;
			m_cmdCenter = null;
		}
	}

	public void onGetCardNoc(String nick, int card) {
		int i = findPlayerPos(nick);
		if (i < 0)
			return;
		int j;
		if (card < 0)
			return;

		int k = 1;
		if (m_iMyPosInVt == i)
			k = 0;
		else {
			m_arrOtherCards[i][0].m_id = 0;
		}

		for (j = k; j < m_iMaxNumCard; j++) {
			if (m_arrOtherCards[i][j].m_id == -1) {
				m_arrOtherCards[i][j].m_id = card;
				m_arrOtherCards[i][j].setActionRotate();
				break;
			}
		}
		System.gc();
	}

	public void onToThem(String nick, long money, long totalMoney, long maxGoldCanBet) {
		this.maxGoldCanBet = maxGoldCanBet;
		PlayerInfo p;
		for (byte i = 0; i < m_arrPlayers.length; i++) {

			if (m_arrPlayers[i] != null) {
				if (m_arrPlayers[i].itemName.toLowerCase().equals(nick.toLowerCase())) {
					if (money > lastCuoc) {

						if (nick.toLowerCase().equals(
								HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
							m_cmdLeft = menuNormalCmd;
						}
						lastCuoc = money;
					}

					// strTalk = nick + " tố thêm " + money + "$";
					p = (PlayerInfo) findPlayer(nick);
					if (p != null) {
						betCount++;
						TranslateImage f = new TranslateImage();
						createImageBetMoney();
						f.startEffect(m_arrPos[i].x, m_arrPos[i].y, GameCanvas.w / 2,
								GameCanvas.h / 2, imgBetMoney);

						FlyImage f1 = new FlyImage();
						f1.startEffect(m_arrPos[i].x, m_arrPos[i].y,
								GameResource.instance.imgWin_ToThem);

						p.setString("Tố thêm: " + money + "$");
						p = null;
						f1 = null;
					}
					return;
				}
			}
		}
		p = null;
	}

	public void onTheo(long gold, String nick) {
		if (gold > 0) {
			if (nick.toLowerCase().equals(HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
				m_cmdLeft = menuNormalCmd;
			}

			int i;
			for (i = 0; i < m_arrPlayers.length; i++) {
				if (m_arrPlayers[i].itemId != -1) {
					if (m_arrPlayers[i].itemName.equals(nick)) {
						break;
					}
				}
			}
			PlayerInfo p = (PlayerInfo) findPlayer(nick);
			if (p != null)
				p.setString("Theo: " + gold + "$");
			betGold += gold;

			FlyImage f1 = new FlyImage();
			f1.startEffect(m_arrPos[i].x, m_arrPos[i].y, GameResource.instance.imgWin_Theo);
			f1 = null;

		}
	}

	public void onNickSkip(String nick) {
		if (nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
			m_cmdLeft = menuNormalCmd;
			m_cmdRight = null;
			m_cmdCenter = null;
		}

		int pos = findPlayerPos(nick);
		if (pos < 0)
			return;
		PlayerInfo p = (PlayerInfo) findPlayer(nick);
		if (p != null) {
			FlyImage f = new FlyImage();
			f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y, GameResource.instance.imgWin_UpBo);
			f = null;

			p.setString("Úp bài");
			int i;
			for (i = 0; i < m_arrOtherCards[pos].length; i++) {
				if (!m_arrOtherCards[pos][i].isUpCard && m_arrOtherCards[pos][i].m_id != -1)
					m_arrOtherCards[pos][i].setActionRotate();
			}
			p = null;
		}
	}

	Image imgBetMoney;

	// TODO create image betMoney
	private void createImageBetMoney() {
		imgBetMoney = Image.createImage(GameResource.instance.m_frameWin_Chip.frameWidth * 2,
				GameResource.instance.m_frameWin_Chip.frameHeight * 3);
		Graphics g1 = imgBetMoney.getGraphics();
		for (int i = 0; i < 6; i++)
			GameResource.instance.m_frameWin_Chip.drawFrame(0, 0, imgBetMoney.getHeight()
					- GameResource.instance.m_frameWin_Chip.frameHeight / 4 * i, Sprite.TRANS_NONE,
					Graphics.LEFT | Graphics.BOTTOM, g1);

		for (int j = 0; j < 4; j++)
			GameResource.instance.m_frameWin_Chip.drawFrame(1,
					GameResource.instance.m_frameWin_Chip.frameWidth, imgBetMoney.getHeight()
							- GameResource.instance.m_frameWin_Chip.frameHeight / 4 * j,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.BOTTOM, g1);

	}

	public void onTo(String nick, long money, long maxGoldCanBet) {
		this.maxGoldCanBet = maxGoldCanBet;
		PlayerInfo p;
		for (byte i = 0; i < m_arrPlayers.length; i++) {

			if (m_arrPlayers[i] != null) {
				if (m_arrPlayers[i].itemName.toLowerCase().equals(nick.toLowerCase())) {

					// strTalk = nick + " tố " + money + "$";
					p = (PlayerInfo) findPlayer(nick);
					if (p != null) {
						betCount++;
						TranslateImage f = new TranslateImage();
						createImageBetMoney();
						f.startEffect(m_arrPos[i].x, m_arrPos[i].y, GameCanvas.w / 2,
								GameCanvas.h / 2, imgBetMoney);

						FlyImage f1 = new FlyImage();
						f1.startEffect(m_arrPos[i].x, m_arrPos[i].y,
								GameResource.instance.imgWin_To);

						p.setString("Tố: " + money + "$");
						p = null;
						f = null;
						f1 = null;
					}
					if (nick.toLowerCase().equals(
							HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
						m_cmdLeft = menuNormalCmd;
					}
					betGold += money;
					return;
				}
			}
		}
		p = null;
	}

	// TODO select button
	private boolean selectButton(int x, int y) {
		if (GameCanvas.isPointer(x, y, GameResource.instance.m_frameTienLen_Button.frameWidth,
				GameResource.instance.m_frameTienLen_Button.frameHeight)) {
			return true;
		}
		return false;
	}

	// TODO paint button game
	private void paintButton(Graphics g) {
		// paint line button
		g.drawImage(GameResource.instance.imgTienLen_LineButton, 0, GameCanvas.h, Graphics.LEFT
				| Graphics.BOTTOM);

		int w = (GameCanvas.w / 2 - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;

		// TODO paint button trái (Tố hoặc theo)
		PaintPopup.gI().paintButton_Game(g, 0,
				GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight, idx_To,
				mText_Button);

		if (flag_To_Theo == 1) {
			// TODO paint button giữa (Theo)
			PaintPopup.gI().paintButton_Game(g, w,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight, 0,
					"Theo");
		}

		// TODO paint button phải (Úp bỏ)
		PaintPopup.gI().paintButton_Game(g,
				GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth,
				GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight, idx_UpBo,
				"Úp bỏ");
	}

	// TODO update button game
	private void updateKey_Button() {
		if (GameCanvas.isPointerDown) {
			// TODO button "Tố" hoặc "Theo"
			if (selectButton(0, GameCanvas.h
					- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_To = 1;

			}
			// TODO button "Úp bỏ"
			if (selectButton(GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_UpBo = 1;
			}
		}

		if (GameCanvas.isPointerClick) {

			// TODO button "tố" hoặc "theo"
			if (selectButton(0, GameCanvas.h
					- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				idx_To = 0;
				if (mText_Button.equals("Tố")) {
					cmdTo_ToThem.action.perform();
				} else {
					cmdTheo.action.perform();
				}
				// cmd_openCard.action.perform();
			}

			if (flag_To_Theo == 1) {
				int w = (GameCanvas.w / 2 - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;
				// TODO button "tố" hoặc "theo"
				if (selectButton(w, GameCanvas.h
						- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					HDCGameMidlet.instance.m_viberator.vibrate(100);
					// idx_To = 0;
					// if (mText_Button.equals("Tố")) {
					// cmdTo_ToThem.action.perform();
					// } else {
					cmdTheo.action.perform();
					// }
					// // cmd_openCard.action.perform();
				}
			}

			// TODO button "úp bỏ"
			if (selectButton(GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				// m_finish = true;
				idx_UpBo = 0;
				cmdBo.action.perform();
				// cmd_openAllCard.action.perform();

			}
		}
	}

	public void paintPlayGame(Graphics g) {

		// // TODO paint hộp bài
		// g.drawImage(
		// GameResource.instance.imgXiTo_Box,
		// (GameCanvas.w - imgTable.getWidth()) / 4 * 3,
		// GameResource.instance.imgHeaderBg.getHeight()
		// + (GameCanvas.h - imgTable.getHeight()) / 2, Graphics.LEFT
		// | Graphics.VCENTER);

		// TODO paint title
		GameResource.instance.m_frameTienLen_Title.drawFrame(3, GameCanvas.w / 2,
				GameCanvas.h / 8 * 5, Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

		// TODO paint button
		if (flagStart == 4 && m_strWhoTurn.equals(HDCGameMidlet.instance.m_myPlayerInfo.itemName))
			paintButton(g);

		// TODO paint card
		byte i, j;
		for (i = 0; i < m_arrPlayers.length; i++) {
			if (m_arrPlayers[i] != null && m_arrPlayers[i].itemName.length() > 0) {
				for (j = 0; j < m_iMaxNumCard; j++) {
					if (m_arrOtherCards[i][j].m_id != -1) {
						m_arrOtherCards[i][j].paint(g);
					}
					if (j == 0
							&& m_arrPlayers[i].itemName
									.equals(HDCGameMidlet.instance.m_myPlayerInfo.itemName)
							&& flagStart == 4) {
						// g.setColor(Color.BLACK);
						// g.setAlpha(120);
						// g.fillRectWithTransparent(m_arrOtherCards[i][0].m_x,
						// m_arrOtherCards[i][0].m_y,GameResource.instance.m_imgCards[0].getWidth()
						// ,GameResource.instance.m_imgCards[0].getHeight() );
						g.drawImage(GameResource.instance.imgCard_HighLight,
								m_arrOtherCards[i][0].m_x, m_arrOtherCards[i][0].m_y, Graphics.LEFT
										| Graphics.TOP);
					}
				}

			}
		}

		// TODO paint icon bet
		if (flagStart == 4 && betCount != 0) {
			for (i = 0; i < betCount; i++) {
				if (i < 5)
					GameResource.instance.m_frameWin_Chip.drawFrame(2, GameCanvas.w / 2,
							GameCanvas.h / 2 - GameResource.instance.m_frameWin_Chip.frameHeight
									/ 4 * i, Sprite.TRANS_NONE, Graphics.LEFT | Graphics.BOTTOM, g);
				else if (i >= 5 && i < 8) {
					GameResource.instance.m_frameWin_Chip.drawFrame(1, GameCanvas.w / 2
							+ GameResource.instance.m_frameWin_Chip.frameWidth, GameCanvas.h / 2
							- GameResource.instance.m_frameWin_Chip.frameHeight / 4 * (i - 5),
							Sprite.TRANS_NONE, Graphics.LEFT | Graphics.BOTTOM, g);
				} else {
					GameResource.instance.m_frameWin_Chip.drawFrame(0, GameCanvas.w / 2
							- GameResource.instance.m_frameWin_Chip.frameWidth, GameCanvas.h / 2
							- GameResource.instance.m_frameWin_Chip.frameHeight / 4 * (i - 8),
							Sprite.TRANS_NONE, Graphics.LEFT | Graphics.BOTTOM, g);
				}

			}

			// TODO paint tiền đặt cược
			if ((betGold + lastCuoc) > 0)
				BitmapFont.drawItalicFont(g, "Tổng tiền : $" + (betGold + lastCuoc), GameCanvas.hw,
						GameCanvas.h / 2, 0xffff00, Graphics.HCENTER | Graphics.VCENTER);
			else
				BitmapFont.drawItalicFont(g, "Tổng tiền : $" + betGoldOfTable, GameCanvas.hw,
						GameCanvas.h / 2, 0xffff00, Graphics.HCENTER | Graphics.VCENTER);

		}

		PlayerInfo p;
		for (i = 0; i < m_arrPlayers.length; i++) {
			if (m_arrPlayers[i] != null && m_arrPlayers[i].itemId != -1) {
				p = m_arrPlayers[i];
				if (m_arrPos[i].anchor == 0)
					p.paintStrTalk(g, m_arrPos[i].x, m_arrPos[i].y - 25, Graphics.HCENTER
							| Graphics.VCENTER, false);
				else if (m_arrPos[i].anchor == 1)
					p.paintStrTalk(g, m_arrPos[i].x - 15, m_arrPos[i].y - 30, Graphics.LEFT
							| Graphics.TOP, true);
				else if (m_arrPos[i].anchor == 2)
					p.paintStrTalk(g, m_arrPos[i].x, m_arrPos[i].y + 48, Graphics.HCENTER
							| Graphics.VCENTER, false);
				else
					p.paintStrTalk(g, m_arrPos[i].x + 15, m_arrPos[i].y - 30, Graphics.RIGHT
							| Graphics.TOP, true);
			}
		}
		p = null;

	}

	public void onAllCardPlayerFinish(Message message) {
		String nick = MessageIO.readString(message);
		int pos = findPlayerPos(nick);
		if (pos < 0)
			return;

		byte c[] = MessageIO.readBytes(message);
		int i;

		for (i = 0; i < c.length; i++) {
			m_arrOtherCards[pos][i].m_id = c[i];
		}
		c = null;
		for (i = 0; i < m_iMaxNumCard; i++) {
			if (m_arrOtherCards[pos][i].isUpCard && !m_arrOtherCards[pos][i].isActionRotate) {
				m_arrOtherCards[pos][i].setActionRotate();
			}
		}

		betCount = 0;
		nick = null;
		flag_To_Theo = 0;

		// super.onAllCardPlayerFinish(message);
	}

	public void onUserJoinTable(int tbID, String nick, boolean isChampion, int pos, long money,
			int level, int avatarId, int playerStatus) {
		super.onUserJoinTable(tbID, nick, isChampion, pos, money, level, avatarId, playerStatus);
		resetCard(m_arrOtherCards[pos], -1);
		if (!m_bIsPlaying) {
			boolean have = false;
			for (byte i = 0; i < m_iMaxNumCard; i++) {

				if (m_arrOtherCards[m_iMyPosInVt][i].m_id != -1) {
					have = true;
					break;
				}
			}
			if (!have)
				resetOtherCards();
		}
	}

	public void setCommandTo() {
		mText_Button = "Tố";
		m_cmdCenter = cmdTo_ToThem;
	}

	public void actionEndGame() {
		super.actionEndGame();
		resetOtherCards();
		betGold = 0;
		lastCuoc = 0;
	}

	public void displayJoinGame(Message message) {
		int total = MessageIO.readByte(message);
		byte[] b;
		int i, j, k, pos;
		for (i = 0; i < total; i++) {
			pos = findPlayerPos(MessageIO.readString(message));
			b = MessageIO.readBytes(message);
			if (pos < 0 && pos > m_iMaxPlayer)
				return;
			m_arrOtherCards[pos][0].m_id = 0;
			for (j = 0; j < b.length; j++) {
				for (k = 0; k < m_iMaxNumCard; k++) {
					if (m_arrOtherCards[pos][k].m_id == -1) {
						m_arrOtherCards[pos][k].m_id = b[j];
						m_arrOtherCards[pos][k].isUpCard = false;
						break;
					}
				}
			}
		}

		for (i = 0; i < m_iMaxPlayer; i++) {
			for (j = 0; j < m_iMaxNumCard; j++) {
				if (m_arrOtherCards[i][j].m_id != -1) {
					m_arrOtherCards[i][j].m_x = m_arrOtherCards[i][j].xTo;
					m_arrOtherCards[i][j].m_y = m_arrOtherCards[i][j].yTo;
				}
			}
		}
		b = null;
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
