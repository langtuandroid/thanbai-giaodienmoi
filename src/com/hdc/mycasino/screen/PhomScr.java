package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.PlayerInfo;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.DetailImage;
import com.hdc.mycasino.utilities.GameResource;

public class PhomScr extends PlayGameScr {
	static PhomScr instance;

	private Command m_getCmd;// bóc
	Command m_haphomCmd;
	private Command m_eatCardCmd;// an
	private Command m_fireCardCmd;// dánh
	// private Command m_pushCmd;
	private Command m_sortPhom;// sx
	private Command m_getPhom;// chọn phỏm
	private Command m_attachCard;// gủi

	// TODO comman hạ phỏm và chọn phỏm
	private Command m_CmdHaPhom;
	// private Command m_CmdChonPhom;

	// private boolean m_bHaPhom;
	// private boolean m_bIsPushSort = false;
	private byte statusEatCard;// 1 la an - 2 la boc - 0 là only dánh
	private boolean m_bIsCanAttachCard = false;
	private byte m_bTypeSort = 0;
	// add more card for first player
	private boolean m_bIsFirstPlayer = false;

	private int[] m_arrPlayerMom;
	private int[] m_arrEarCardOwner;
	private Card[] m_arrHitCard;
	private int[] m_arrSelectCard;

	private int m_iHPerCard;
	private int m_iyUp = GameCanvas.h - GameResource.instance.m_imgCards[0].getHeight() / 2 * 3
			- GameResource.instance.m_frameTienLen_Button.frameHeight;
	// private int m_iyDown = GameCanvas.h - GameCanvas.hBottomBar -
	// GameResource.instance.imgCard.getHeight() / 2;
	private int m_iy = GameCanvas.h - GameResource.instance.m_imgCards[0].getHeight()
			- GameResource.instance.m_frameTienLen_Button.frameHeight;

	Command cmdBack;
	// TODO index for 4 buttons
	// Type 1 : Xếp bài - Ăn - Bốc - Đánh
	private int idx_XepBai = 0;
	private int idx_An = 0;
	private int idx_Boc = 0;
	private int idx_Danh = 0;

	// Type 2 : Hạ phỏm - chọn phỏm
	private int idx_HaPhom = 0;
	private int idx_ChonPhom = 0;

	// Type 3 : Gửi bài
	private int idx_GuiBai = 0;

	// TODO flag button
	private int flagButton = 0;

	// TODO hight light chon bai
	private int jump = 0;

	// TODO tọa dộ x - y của quân bài dánh cuối cùng(dành sử dụng ăn bài)
	private float x_lastCard, y_lastCard;

	public static void clear() {
		if (instance != null) {
			instance.m_arrSelectCard = null;
			instance.m_getCmd = null;
			// instance.m_haphomCmd = null;
			instance.m_eatCardCmd = null;
			instance.m_fireCardCmd = null;
			// instance.m_pushCmd = null;
			instance.m_sortPhom = null;
			instance.m_getPhom = null;
			instance.m_attachCard = null;
			instance.m_arrOtherCards = null;
			instance.m_arrPlayerMom = null;
			instance.m_arrEarCardOwner = null;
		}
		instance = null;
	}

	public static PhomScr gI() {
		if (instance == null) {
			instance = new PhomScr((byte) 4, (byte) 10);
		}
		return instance;
	}

	// init vị trí của những người choi gồm vị trí - rank(lá bài cầm tay - lá
	// bài phỏm)
	private void initPosPlayer(int posPlayer, int rank) {
		int j;
		int i = posPlayer;
		int numArr = 0;
		for (j = m_iMaxNumCard * rank; j < m_iMaxNumCard * (rank + 1); j++) {
			if (m_arrOtherCards[i][j].m_id != -1)
				numArr++;
		}

		int Center = (numArr * m_iHPerCard) >> 1;
		int Owner = (numArr * m_iUseWCard) >> 1;

		int k = 0;
		if (rank == 2) {
			// init

			int arr[] = getListPhom(m_arrOtherCards[i]);

			// //////
			int haftCardW = (int) (GameResource.instance.imgTienLen_CardBack2.getWidth() / 1.5 / 2);
			int haftUser = GameResource.instance.imgTienLen_User.getWidth() * 3 / 7;
			int haftCardH = (int) (GameResource.instance.imgTienLen_CardBack2.getHeight() / 1.5);// height

			int left = m_arrPos[i].x + haftUser + haftCardW * 2;// new left
			int right = m_arrPos[i].x - haftUser - haftCardW * 4 - haftCardW;// right
			int top = m_arrPos[i].y + haftCardW * 2;// new top
			k = m_iy - m_iUseWCard; // new bottom

			numArr = 0;
			for (j = 0; j < arr.length; j++)
				if (arr[j] != -1)
					numArr++;

			int numCard = 0;
			for (j = m_iMaxNumCard * 2; j < m_iMaxNumCard * 3; j++)
				if (m_arrOtherCards[i][j].m_id != -1)
					numCard++;

			int numPerPhom = 0, y = 0, nextPerPhom = 0;
			int maxWPhom = (numCard * m_iHPerCard + haftCardW * 2 * (numArr - 1)) >> 1;
			int maxWPerPhom = 0;

			for (j = m_iMaxNumCard * 2; j < m_iMaxNumCard * 3; j++) {
				if (m_arrOtherCards[i][j].m_id == -1)
					break;

				switch (m_arrPos[i].anchor) {
				case 1:
				case 3:
					switch (numArr) {
					case 1:// nam giua
						y = m_arrPos[i].y + haftUser / 2;
						break;
					case 2:
					case 3:// nam tren
						y = m_arrPos[i].y - haftCardH + haftUser / 2;
						break;
					default:// nam o duoi
						y = m_arrPos[i].y - (haftCardH >> 1) + haftUser / 2;
						break;
					}
					if (m_arrPos[i].anchor == 1)
						m_arrOtherCards[i][j].setPos(left + nextPerPhom * m_iHPerCard, y, 0, 1.5f);
					else {
						if (numPerPhom == 0)
							maxWPerPhom = arr[numPerPhom] * m_iHPerCard;
						else
							maxWPerPhom = (arr[numPerPhom] - arr[numPerPhom - 1] - 1) * m_iHPerCard;

						m_arrOtherCards[i][j].setPos(right + nextPerPhom * m_iHPerCard - maxWPerPhom, y, 0, 1.5f);
					}
					if (arr[numPerPhom] == j % m_iMaxNumCard) {
						nextPerPhom = -1;
						numPerPhom++;
						numArr -= 2;
					}
					break;
				default:
					if (m_arrPos[i].anchor == 2)
						m_arrOtherCards[i][j].setPos(m_arrPos[i].x - maxWPhom + y, top, 0, 1.5f);
					else
						m_arrOtherCards[i][j].setPos(m_arrPos[i].x - maxWPhom + y, k, 0, 1.5f);
					if (arr[numPerPhom] == j % m_iMaxNumCard) {
						nextPerPhom = 0;
						numPerPhom++;
						y += haftCardW * 2;
					}
					y += m_iHPerCard;
					break;
				}
				nextPerPhom++;
			}
			return;
		}

		for (j = m_iMaxNumCard * rank; j < m_iMaxNumCard * (rank + 1); j++) {
			if (m_arrOtherCards[i][j].m_id == -1)
				continue;

			if (m_arrPos[i].anchor == 1 || m_arrPos[i].anchor == 3)
				m_arrOtherCards[i][j].yTo = m_arrPos[i].y - Center + k * m_iHPerCard;
			else if (m_arrPos[i].anchor == 2)
				m_arrOtherCards[i][j].xTo = m_arrPos[i].x - Center + k * m_iHPerCard;
			else {
				if (j < m_iMaxNumCard) {
					m_arrOtherCards[i][j].xTo = m_arrPos[i].x - Owner + k * m_iUseWCard;
					m_arrOtherCards[i][j].yTo = m_iy;
				} else {
					m_arrOtherCards[i][j].xTo = m_arrPos[i].x - Center + k * m_iHPerCard;
					m_arrOtherCards[i][j].yTo = m_iy - m_iUseWCard / 2;
				}
			}
			k++;
		}
	}

	// TODO paint button type 1
	// Xếp bài - Ăn Bài - Bốc bài - Đánh
	private void paintButton_Type_1(Graphics g) {
		int w = (GameCanvas.w / 2 - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;
		// TODO paint button trái (Xếp bài)
		PaintPopup.gI().paintButton_Game(g, 0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight,
				idx_XepBai, "Xếp bài");
		// if (m_strWhoTurn.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
		switch (statusEatCard) {
		case 0:
			// TODO paint button phải (Đánh)
			PaintPopup.gI().paintButton_Game(g, GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight, idx_Danh, "Đánh");
			break;
		case 1:// TODO paint button Ăn
			PaintPopup.gI().paintButton_Game(g, w,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight, idx_An, "Ăn bài");
		case 2:
			// TODO paint button Bốc bài
			PaintPopup.gI().paintButton_Game(g, w + GameCanvas.w / 2,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight, idx_Boc, "Bốc bài");
			break;
		default:
			break;
		}

		// }
	}

	// TODO paint button type 2
	// Hạ phỏm - Chọn phỏm
	private void paintButton_Type_2(Graphics g) {
		// TODO paint button trái (Hạ phỏm)
		PaintPopup.gI().paintButton_Game(g, 0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight,
				idx_HaPhom, "Hạ phỏm");
		// TODO paint button phải (Chọn phỏm)
		PaintPopup.gI().paintButton_Game(g, GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth,
				GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight, idx_ChonPhom, "Chọn phỏm");
	}

	// TODO paint button type 3
	// Gửi bài
	private void paintButton_Type_3(Graphics g) {
		// TODO paint button trái (Gửi bài)
		PaintPopup.gI().paintButton_Game(g, 0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight,
				idx_GuiBai, "Gửi bài");
	}

	// TODO paint button
	private void paintButton(Graphics g) {
		// paint line button
		g.drawImage(GameResource.instance.imgTienLen_LineButton, 0, GameCanvas.h, Graphics.LEFT | Graphics.BOTTOM);

		switch (flagButton) {
		case 0:
			paintButton_Type_1(g);
			break;
		case 1:
			if (m_strWhoTurn.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
				paintButton_Type_2(g);
			break;
		case 2:
			if (m_strWhoTurn.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
				paintButton_Type_3(g);
			break;
		default:
			break;
		}
	}

	// TODO select button
	private boolean selectButton(int x, int y) {
		if (GameCanvas.isPointer(x, y, GameResource.instance.m_frameTienLen_Button.frameWidth,
				GameResource.instance.m_frameTienLen_Button.frameHeight)) {

			return true;
		}
		return false;
	}

	// TODO updateKey button game Type 1
	// Xếp bài - Ăn bài - Bốc bài - Đánh
	private void updateKey_Button_Type_1() {
		if (GameCanvas.isPointerDown) {
			// TODO button Xếp bài
			if (selectButton(0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_XepBai = 1;
			}
			// TODO button "Đánh"
			int w = (GameCanvas.w / 2 - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;
			switch (statusEatCard) {
			case 0:
				if (selectButton(GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth, GameCanvas.h
						- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					idx_Danh = 1;
				}
				break;
			case 1:// TODO button Ăn bài
				if (selectButton(w, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					idx_An = 1;
				}
			case 2:// TODO button Bốc bài
				if (selectButton(w + GameCanvas.w / 2, GameCanvas.h
						- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					idx_Boc = 1;
				}
				break;
			default:
				break;
			}
		}

		if (GameCanvas.isPointerClick) {

			// TODO button xếp bài
			if (selectButton(0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				idx_XepBai = 0;
				m_sortPhom.action.perform();
			}

			int w = (GameCanvas.w / 2 - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;
			// TODO button Đánh
			switch (statusEatCard) {
			case 0:
				if (selectButton(GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth, GameCanvas.h
						- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					HDCGameMidlet.instance.m_viberator.vibrate(100);
					idx_Danh = 0;
					m_fireCardCmd.action.perform();
				}
				break;
			case 1:
				// TODO button Ăn bài
				if (statusEatCard == 1
						&& selectButton(w, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					HDCGameMidlet.instance.m_viberator.vibrate(100);
					idx_An = 0;
					statusEatCard = 0;
					m_eatCardCmd.action.perform();
				}
			case 2:
				// TODO button bốc bài
				if (selectButton(w + GameCanvas.w / 2, GameCanvas.h
						- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					HDCGameMidlet.instance.m_viberator.vibrate(100);
					idx_Boc = 0;
					m_getCmd.action.perform();
				}
				break;
			default:
				break;
			}
		}
	}

	// TODO updateKey button game Type 2
	// Hạ phỏm - chọn phỏm
	private void updateKey_Button_Type_2() {
		if (GameCanvas.isPointerDown) {
			// TODO button hạ phỏm
			if (selectButton(0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_HaPhom = 1;
			}
			// TODO button "Chọn phỏm"
			if (selectButton(GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth, GameCanvas.h
					- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_ChonPhom = 1;
			}
		}

		if (GameCanvas.isPointerClick) {

			// TODO button Hạ phỏm
			if (selectButton(0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				idx_HaPhom = 0;
				m_CmdHaPhom.action.perform();

			}
			// TODO button Chọn phỏm
			if (selectButton(GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth, GameCanvas.h
					- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				idx_ChonPhom = 0;
				m_getPhom.action.perform();
			}
		}
	}

	// TODO updateKey button game Type 3
	// Gửi bài
	private void updateKey_Button_Type_3() {
		if (GameCanvas.isPointerDown) {
			// TODO button Gửi bài
			if (selectButton(0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_GuiBai = 1;
			}
		}

		if (GameCanvas.isPointerClick) {

			// TODO button Gửi bài
			if (selectButton(0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				idx_GuiBai = 0;
				m_attachCard.action.perform();

			}
		}
	}

	private void updateKey_Button_Type4() {
		// TODO vi tri an bai - va vị trí boc bai
		if (GameCanvas.isPointerClick) {
			int h = (int) (GameResource.instance.imgTienLen_CardBack2.getHeight() / 1.5);
			int w = (int) (GameResource.instance.imgTienLen_CardBack2.getWidth() / 1.5);
			if (statusEatCard == 1 || statusEatCard == 2) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				if (GameCanvas.isPointer(GameCanvas.hw - w / 2, GameCanvas.hh - h / 2, w, h))
					m_getCmd.action.perform();
				if (statusEatCard == 1 && GameCanvas.isPointer(x_lastCard, y_lastCard, w, h))
					m_eatCardCmd.action.perform();
			}
		}
	}

	// TODO update key for button game
	private void updateKey_ButtonGame() {
		switch (flagButton) {
		case 0:
			updateKey_Button_Type_1();
			break;
		case 1:
			if (m_strWhoTurn.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
				updateKey_Button_Type_2();
			break;
		case 2:
			if (m_strWhoTurn.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
				updateKey_Button_Type_3();
			break;
		default:
			break;
		}
		updateKey_Button_Type4();
	}

	private void resetOtherCards(boolean isClear) {
		int i, j;
		int k = -1;
		int xCenter = (GameCanvas.w - m_iMaxNumCard * m_iUseWCard) >> 1;
		int yCenter = (m_iMaxNumCard - 1) * m_iHPerCard / 2;
		int xCenterPlayer = ((m_iMaxNumCard - 1) * m_iHPerCard) >> 1;
		int haftCardW = (int) (DetailImage.imgCard_w / 1.5 / 2);
		int haftUser = GameResource.instance.imgTienLen_User.getWidth() * 3 / 7;
		for (i = 0; i < m_iMaxPlayer; i++) {
			k = -1;
			for (j = 0; j < m_iMaxNumCard * 3; j++) {
				m_arrOtherCards[i][j].isPhom = false;
				m_arrOtherCards[i][j].isSpecial = false;
				m_arrOtherCards[i][j].isCallMove = false;
				if (isClear) {
					if (j < m_iMaxNumCard - 1) {
						m_arrOtherCards[i][j].m_id = 0;
						m_arrOtherCards[i][j].isUpCard = true;
						m_arrOtherCards[i][j].isCallMove = true;
					} else
						m_arrOtherCards[i][j].m_id = -1;
				}
				m_arrOtherCards[i][j].setScale(1.5f);

				if (j >= (m_iMaxNumCard << 1))
					m_arrOtherCards[i][j].isSpecial = true;

				if (j % m_iMaxNumCard == 0)
					k++;

				m_arrOtherCards[i][j].m_x = GameCanvas.hw - DetailImage.imgCard_w / 2;
				m_arrOtherCards[i][j].m_y = GameCanvas.ch - DetailImage.imgCard_h;
				if (m_arrPos[i].anchor == 1) {
					m_arrOtherCards[i][j].xTo = m_arrPos[i].x + k * haftCardW + haftUser;
					m_arrOtherCards[i][j].yTo = m_arrPos[i].y - yCenter + j % m_iMaxNumCard * m_iHPerCard;
				} else if (m_arrPos[i].anchor == 3) {
					m_arrOtherCards[i][j].xTo = m_arrPos[i].x - k * haftCardW - haftUser - haftCardW * 2;
					m_arrOtherCards[i][j].yTo = m_arrPos[i].y - yCenter + j % m_iMaxNumCard * m_iHPerCard;
				} else if (m_arrPos[i].anchor == 2) {
					m_arrOtherCards[i][j].xTo = m_arrPos[i].x - xCenterPlayer + j % m_iMaxNumCard * m_iHPerCard;
					m_arrOtherCards[i][j].yTo = m_arrPos[i].y + k * haftCardW;
				} else {
					m_arrOtherCards[i][j].xTo = xCenter + j % m_iMaxNumCard * m_iUseWCard;
					if (j < m_iMaxNumCard)
						m_arrOtherCards[i][j].yTo = m_iy;
					else if (j < m_iMaxNumCard * 2)
						m_arrOtherCards[i][j].yTo = GameCanvas.ch + m_iUseWCard;
					else
						m_arrOtherCards[i][j].yTo = GameCanvas.ch + m_iUseWCard + m_iHPerCard;
				}
			}
		}
	}

	// TODO phân ra những phỏm khác nhau
	private int[] getListPhom(Card[] listCard) {
		int arr[] = new int[m_iMaxNumCard];
		int arrLuu[] = new int[3];

		// reset het tất cả các phần tử
		CRes.resetArr(arr);
		CRes.resetArr(arrLuu);

		// chép vào
		for (int i = m_iMaxNumCard * 2; i < m_iMaxNumCard * 3; i++) {
			arr[i % m_iMaxNumCard] = listCard[i].m_id;
		}
		// lọc ra
		CRes.checkListPhom(arr, arrLuu);
		arr = null;
		return arrLuu;
	}

	public PhomScr(byte maxPlayer, byte maxNum) {
		super(maxPlayer, maxNum);

		cmdBack = new Command("Rời bàn", new IAction() {

			public void perform() {
				if (m_bIsPlaying) {
//					GameCanvas.startOKDlg("Nếu thoát khi đang chơi bạn sẽ bị xử thua. Bạn có muốn thoát",
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

		m_arrPlayerMom = new int[maxPlayer];
		m_arrOtherCards = new Card[maxPlayer][maxNum * 3];
		m_arrEarCardOwner = new int[maxNum];
		m_arrSelectCard = new int[maxNum];
		m_arrHitCard = new Card[maxNum];
		CRes.resetArr(m_arrSelectCard);
		int i, j;
		for (i = 0; i < maxPlayer; i++) {
			for (j = 0; j < maxNum * 3; j++) {
				m_arrOtherCards[i][j] = new Card(-1);
				if (i == 0 && j < maxNum)
					m_arrHitCard[j] = new Card(-1);
			}
		}

		m_iHPerCard = DetailImage.imgCard_w / 5;
		// int m_iMaxWCard = GameResource.instance.imgCard.getWidth() * 3 / 4;
		// if (GameCanvas.w < m_iMaxWCard * 13) {
		// m_iHPerCard = m_iUseWCard;
		// }

		// yCenter = GameCanvas.h / 2 - (m_iMaxNumCard * m_iHPerCard/2);

		m_fireCardCmd = new Command("Đánh", new IAction() {
			public void perform() {
				int count = 0;
				int param = 0;
				int i;
				for (i = 0; i < m_arrSelectCard.length; i++) {
					if (m_arrSelectCard[i] != -1) {
						count++;
						param = i;
					}
				}
				if (count == 1) {
					GlobalService.onFireCard(boardId, m_arrSelectCard[param]);
					// saveCmd(m_cmdCenter, m_cmdRight);
				} else if (count == 0) {
					// GameCanvas.startOKDlg("Chọn bài để đánh");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Chọn bài để đánh");
					return;

				} else if (count > 1) {
					// GameCanvas.startOKDlg("Chỉ được đánh 1 lá");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Chỉ được đánh 1 lá");
					return;
				}
			}
		});

		m_eatCardCmd = new Command("Ăn", new IAction() {
			public void perform() {
				GlobalService.sendMessageEatCard();
				// m_cmdCenter = m_fireCardCmd;
				statusEatCard = 0;
				// saveCmd(m_cmdCenter, m_cmdRight);
			}
		});

		m_attachCard = new Command("Gởi bài", new IAction() {
			public void perform() {
				GlobalService.sendMessageAttachCard();
				// m_cmdCenter = null;
				m_bIsCanAttachCard = false;
			}
		});

		m_CmdHaPhom = new Command("Hạ phỏm", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				flagButton = 0;
				GlobalService.onHaPhom(boardId);
				// saveCmd(m_cmdCenter, m_cmdRight);
			}
		});

		// m_CmdChonPhom = new Command("Chọn phỏm", new IAction() {
		//
		// @Override
		// public void perform() {
		// // TODO Auto-generated method stub
		// // m_bHaPhom = true;
		// m_cmdCenter = m_getPhom;
		// int i;
		// for (i = 0; i < m_iMaxNumCard; i++)
		// m_arrOtherCards[m_iMyPosInVt][i].yTo = m_iy;
		// CRes.resetArr(m_arrSelectCard);
		// }
		// });

		m_haphomCmd = new Command("Hạ Phỏm", new IAction() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void perform() {
				if (!isCheckMom()) {
					GlobalService.onHaPhom(boardId);
					// saveCmd(m_cmdCenter, m_cmdRight);
				} else {
					Vector vt = new Vector();
					vt.addElement(new Command("Hạ Phỏm", new IAction() {
						public void perform() {
							GlobalService.onHaPhom(boardId);
							// saveCmd(m_cmdCenter, m_cmdRight);
						}
					}));

					vt.addElement(new Command("Chọn Phỏm", new IAction() {
						public void perform() {
							// m_bHaPhom = true;
							// m_cmdCenter = m_getPhom;
							int i;
							for (i = 0; i < m_iMaxNumCard; i++)
								m_arrOtherCards[m_iMyPosInVt][i].yTo = m_iy;
							CRes.resetArr(m_arrSelectCard);
						}
					}));
					GameCanvas.instance.menu.startAt(vt, 2);
				}
			}
		});

		m_getCmd = new Command("Bốc", new IAction() {
			public void perform() {
				int i, count = 0;

				for (i = 0; i < m_iMaxNumCard; i++) {
					if (m_arrOtherCards[m_iMyPosInVt][i].m_id != -1) {
						count++;
					}
				}

				if (count >= m_iMaxNumCard) {
					// GameCanvas.startOKDlg("Bạn đủ lá số lá bài rùi!");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Bạn đủ lá số lá bài rùi!");
				} else {
					GlobalService.sendMessageGetNewCard();
				}
				// m_cmdCenter = m_fireCardCmd;
				// m_cmdRight = null;
				statusEatCard = 0;
				// saveCmd(m_cmdCenter, m_cmdRight);
			}
		});

		m_sortPhom = new Command("Sắp Xếp", new IAction() {
			public void perform() {
				// sortPhom(m_iMyPosInVt, 0);
				initPosPlayer(m_iMyPosInVt, 0);
				sortPhom(m_iMyPosInVt, 0, m_bTypeSort, true);
				m_bTypeSort = (byte) ((m_bTypeSort + 1) % 2);
				// m_cmdCenter = null;
				// m_cmdRight = null;
				resetTurnPlayer();
				CRes.resetArr(m_arrSelectCard);
			}
		});

		m_getPhom = new Command("Chọn", new IAction() {
			public void perform() {
				flagButton = 0;
				// m_bHaPhom = true;

				int count = 0;
				int i = 0;
				for (i = 0; i < m_arrSelectCard.length; i++) {
					if (m_arrSelectCard[i] != -1) {
						count++;
					}
				}
				int[] card = new int[count];
				count = 0;
				for (i = 0; i < m_arrSelectCard.length; i++) {
					if (m_arrSelectCard[i] != -1) {
						card[count] = m_arrSelectCard[i];
						count++;
					}
				}
				GlobalService.onSendArrayPhom(boardId, card);
				// saveCmd(m_cmdCenter, m_cmdRight);
			}
		});

		// m_pushCmd = new Command("Đặt", new IAction() {
		// public void perform() {
		// int i;
		// for (i = 0; i < m_iMaxNumCard; i++)
		// m_arrOtherCards[m_iMyPosInVt][i].yTo = m_iy;
		// CRes.resetArr(m_arrSelectCard);
		// m_bIsPushSort = false;
		// m_cmdCenter = null;
		// m_cmdRight = null;
		// resetTurnPlayer();
		// }
		// });
	}

	public void switchToMe(String ownerString) {
		super.switchToMe(ownerString);
		resetOtherCards(true);
		for (int i = 0; i < m_iMaxPlayer; i++)
			resetCard(m_arrOtherCards[i], -1);

		Card.setCardType(Card.PHOM);
		CRes.resetArr(m_arrSelectCard);
		// resetOtherCards(true);
		m_selected = -1;
		m_iGameTick = 0;
		m_bTypeSort = 0;
		// m_bIsStartDeliver = false;
		// m_bIsPushSort = false;
		m_bIsFirstPlayer = false;
		// loadImg();
	}

	// public void loadImg() {
	// if (GameResource.instance.imgArrowBig == null)
	// GameResource.instance.imgArrowBig =
	// ImagePack.createImage(ImagePack.BIG_ARROW_PNG);
	// }

	public void startGame(Message message) {
		super.startGame();

		flagStart = 4;

		resetOtherCards(true);
		// resetCard(m_arrHitCard, -1);
		CRes.resetArr(m_arrEarCardOwner);
		CRes.resetArr(m_arrSelectCard);

		// m_bHaPhom = false;
		// m_bIsStartDeliver = true;
		// m_bIsPushSort = false;
		m_bIsFirstPlayer = true;

		m_selected = 0;
		m_bTypeSort = 1;
		m_strWhoTurn = "";
		// read my cards
		byte c[] = MessageIO.readBytes(message);

		// sao chep du lieu vao của mình
		int i;
		for (i = 0; i < c.length; i++) {
			m_arrOtherCards[m_iMyPosInVt][i].m_id = c[i];
			m_arrOtherCards[m_iMyPosInVt][i].isUpCard = true;
			m_arrOtherCards[m_iMyPosInVt][i].isCallMove = true;
			m_arrOtherCards[m_iMyPosInVt][i].setActionRotate();
		}

		for (i = 0; i < m_iMaxPlayer; i++)
			m_arrPlayerMom[i] = m_arrPlayers[i].itemId;

		for (i = 0; i < m_iMaxNumCard; i++)
			m_arrOtherCards[m_iMyPosInVt][i].scaleTo = 1f;

		initPosPlayer(m_iMyPosInVt, 0);
		sortHandPhom();
		sortPhom(m_iMyPosInVt, 0, (byte) 0, false);

		System.gc();
	}

	// hạ phỏm
	public void onDropPhomSuccess(String nick, int arrayPhom[]) {
		flagButton = 0;
		// chức năng hạ phỏm cho người choi
		int i, j, l;
		int pos = findPlayerPos(nick);
		if (pos < 0)
			return;

		if (nick.toLowerCase().equals(HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
			for (j = 0; j < arrayPhom.length; j++) {
				for (i = 0; i < m_iMaxNumCard; i++) {
					if (m_arrOtherCards[m_iMyPosInVt][i].m_id == arrayPhom[j]) {
						m_arrOtherCards[m_iMyPosInVt][i].m_id = -1;// mảng cầm
																	// trên tay
																	// = -1
						for (l = (m_iMaxNumCard << 1); l < m_iMaxNumCard * 3; l++) {
							// gáng mảng phỏm = phỏm
							if (m_arrOtherCards[m_iMyPosInVt][l].m_id == -1) {
								m_arrOtherCards[m_iMyPosInVt][l].m_id = arrayPhom[j];
								break;
							}
						}
						// vị trí bắc đầu bay ra của những lá phỏm
						m_arrOtherCards[m_iMyPosInVt][i].m_x = m_arrOtherCards[m_iMyPosInVt][l].m_x;
						m_arrOtherCards[m_iMyPosInVt][i].m_y = m_arrOtherCards[m_iMyPosInVt][l].m_y;
						m_arrOtherCards[m_iMyPosInVt][i].isPhom = m_arrOtherCards[m_iMyPosInVt][l].isPhom;
						break;
					}
				}
			}
			// m_bHaPhom = false;
			// sort lại phỏm cầm trên tay
			// khoi tao vi tri đến của lá bài
			initPosPlayer(m_iMyPosInVt, 0);
			sortHandPhom();
			initPosPlayer(m_iMyPosInVt, 2);
			CRes.resetArr(m_arrSelectCard);
			m_selected = -1;
			for (i = 0; i < m_iMaxNumCard; i++) {
				if (m_arrOtherCards[m_iMyPosInVt][i].m_id != -1)
					break;
			}
			if (i <= m_iMaxNumCard)
				m_selected = i;

		} else {
			boolean isHave;
			for (j = 0; j < arrayPhom.length; j++) {
				// tìm phỏm cầm trên tay đã ăn
				isHave = false;
				for (i = 0; i < m_iMaxNumCard; i++) {
					if (m_arrOtherCards[pos][i].m_id == arrayPhom[j]) {
						m_arrOtherCards[pos][i].m_id = -1;
						isHave = true;
						break;
					}
				}
				// không có lá bài nào cầm trên tay cả
				if (i >= m_iMaxNumCard) {
					for (i = 0; i < m_iMaxNumCard; i++)
						if (m_arrOtherCards[pos][i].isUpCard && m_arrOtherCards[pos][i].m_id != -1) {
							m_arrOtherCards[pos][i].m_id = -1;
							break;
						}
				}

				// gáng mảng phỏm = phỏm
				for (l = 0; l < m_iMaxNumCard; l++) {
					if (m_arrOtherCards[pos][(m_iMaxNumCard << 1) + l].m_id == -1) {
						m_arrOtherCards[pos][(m_iMaxNumCard << 1) + l].m_id = arrayPhom[j];

						m_arrOtherCards[pos][(m_iMaxNumCard << 1) + l].isPhom = isHave;
						m_arrOtherCards[pos][(m_iMaxNumCard << 1) + l].m_x = m_arrOtherCards[pos][i].m_x;
						m_arrOtherCards[pos][(m_iMaxNumCard << 1) + l].m_y = m_arrOtherCards[pos][i].m_y;
						break;
					}
				}
			}
			// khoi tao vi tri bay ra la bai cua nguoi khac
			initPosPlayer(pos, 2);
			initPosPlayer(pos, 0);
		}
	}

	public void onEatCardSuccess(String from, String to, int card) {
		int posAn = findPlayerPos(from);
		int posBiAn = findPlayerPos(to);
		if (posAn < 0)
			return;
		if (posBiAn < 0)
			return;

		int i, j;

		if (from.toLowerCase().equals(HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
			for (i = 0; i < m_iMaxNumCard; i++) {
				if (m_arrEarCardOwner[i] == -1) {
					m_arrEarCardOwner[i] = card;
					break;
				}
			}
			// m_cmdRight = null;
		}

		// tìm vị trí của lá bài ăn mà thêm vào
		i = 0;
		for (; i < m_iMaxNumCard; i++)
			if (m_arrOtherCards[posAn][i].m_id == -1)
				break;
		m_arrOtherCards[posAn][i].m_id = card;
		m_arrOtherCards[posAn][i].isUpCard = false;
		m_arrOtherCards[posAn][i].isPhom = true;

		// tìm vị trí lá bài của nguoi bị(lá bài dánh ra)
		for (j = m_iMaxNumCard; j < (m_iMaxNumCard << 1); j++) {
			if (m_arrOtherCards[posBiAn][j].m_id == card) {
				m_arrOtherCards[posBiAn][j].m_id = -1;
				break;
			}
		}

		m_arrOtherCards[posAn][i].m_x = m_arrOtherCards[posBiAn][j].m_x;
		m_arrOtherCards[posAn][i].m_y = m_arrOtherCards[posBiAn][j].m_y;
		// /////
		initPosPlayer(posAn, 0);
		initPosPlayer(posBiAn, 0);
		initPosPlayer(posAn, 1);
		initPosPlayer(posBiAn, 1);
	}

	public void onInfoU(String nick) {
		// nếu là người khác ù
		int pos = findPlayerPos(nick);
		if (pos >= 0) {
			PlayerInfo p = findPlayer(nick);
			if (p == null)
				return;
			p.setRank(5);
			p = null;
		}

	}

	// nick bị móm
	public void onInfoMom(String nick) {
		int pos = findPlayerPos(nick);
		if (pos >= 0) {
			m_arrPlayerMom[pos] = -2;
			PlayerInfo p = findPlayer(nick);
			if (p == null)
				return;
			p.setRank(4);
			p = null;
		}
	}

	public void onBalanceCard(String from, String to, int card) {
		int i, j;
		int pFrom = findPlayerPos(from);
		int pTo = findPlayerPos(to);

		if (pFrom < 0)
			return;
		if (pTo < 0)
			return;

		for (i = m_iMaxNumCard; i < (m_iMaxNumCard << 1); i++)
			if (m_arrOtherCards[pFrom][i].m_id == card)
				break;

		m_arrOtherCards[pFrom][i].m_id = -1;
		initPosPlayer(pFrom, 1);

		for (j = m_iMaxNumCard; j < (m_iMaxNumCard << 1); j++)
			if (m_arrOtherCards[pTo][j].m_id == -1)
				break;

		m_arrOtherCards[pTo][j].m_id = card;
		initPosPlayer(pTo, 1);
		m_arrOtherCards[pTo][j].m_x = m_arrOtherCards[pFrom][i].m_x;
		m_arrOtherCards[pTo][j].m_y = m_arrOtherCards[pFrom][i].m_y;
	}

	// gui card
	public void onAttachCard(Message message) {
		String from = MessageIO.readString(message);
		String to = MessageIO.readString(message);

		int pFrom = findPlayerPos(from);
		int pTo = findPlayerPos(to);

		if (pFrom < 0)
			return;
		if (pTo < 0)
			return;

		byte c[] = MessageIO.readBytes(message);

		int card[] = new int[c.length];
		for (int i = 0; i < c.length; i++) {
			card[i] = c[i];
		}

		int i, j, l;
		for (i = 0; i < card.length; i++) {
			// nếu là người lấy thì gán id cho nó
			for (l = m_iMaxNumCard * 2; l < m_iMaxNumCard * 3; l++) {
				if (m_arrOtherCards[pTo][l].m_id == -1) {
					m_arrOtherCards[pTo][l].m_id = card[i];
					break;
				}
			}
			c[i] = (byte) l;
		}

		// sắp xếp lại trật tự của nó
		sortPhom(pTo, 2, (byte) 1, true);
		initPosPlayer(pTo, 2);

		// lấy vị trí mới của lá bài
		for (i = 0; i < card.length; i++) {
			// nếu là người lấy thì gán id cho nó
			for (l = m_iMaxNumCard * 2; l < m_iMaxNumCard * 3; l++)
				if (m_arrOtherCards[pTo][l].m_id == card[i])
					break;
			c[i] = (byte) l;
		}

		// nếu là người bị thì sẽ phải lấy lá bài trên tay
		if (from.toLowerCase().equals(HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
			for (i = 0; i < card.length; i++) {
				for (j = 0; j < m_iMaxNumCard; j++) {
					if (m_arrOtherCards[pFrom][j].m_id == card[i]) {
						m_arrOtherCards[pFrom][j].m_id = -1;
						break;
					}
				}
				m_arrOtherCards[pTo][c[i]].m_x = m_arrOtherCards[pFrom][j].m_x;
				m_arrOtherCards[pTo][c[i]].m_y = m_arrOtherCards[pFrom][j].m_y;
			}
			initPosPlayer(m_iMyPosInVt, 0);
			sortHandPhom();
		} else {
			for (i = 0; i < card.length; i++) {
				m_arrOtherCards[pTo][c[i]].m_x = m_arrPos[pFrom].x;
				m_arrOtherCards[pTo][c[i]].m_y = m_arrPos[pFrom].y;
			}
		}
		card = null;
		c = null;
	}

	protected void paintAfterPlayerInfo() {
		// TODO Auto-generated method stub

	}

	private void paintCardPlayer(Graphics g, int i, int rank) {
		for (int j = m_iMaxNumCard * rank; j < m_iMaxNumCard * (rank + 1); j++)
			if (m_arrOtherCards[i][j].m_id != -1 && !m_arrOtherCards[i][j].isCallMove)
				m_arrOtherCards[i][j].paint(g);
	}

	private int m_iTick = 0;
	private int m_iId = 0;

	public void paintPlayGame(Graphics g) {

		// TODO paint button
		if (m_bIsPlaying)
			paintButton(g);

		int i, j;
		for (i = 0; i < m_arrPlayers.length; i++) {
			if (m_arrPlayers[i] != null && m_arrPlayers[i].itemName.length() > 0) {
				if (m_arrPos[i].anchor == 3 || m_arrPos[i].anchor == 0) {
					paintCardPlayer(g, i, 2);
					paintCardPlayer(g, i, 1);
					paintCardPlayer(g, i, 0);
				} else {
					for (j = 0; j < m_iMaxNumCard * 3; j++)
						if (m_arrOtherCards[i][j].m_id != -1 && !m_arrOtherCards[i][j].isCallMove)
							m_arrOtherCards[i][j].paint(g);
				}
			}
		}

		// TODO paint all in center
		if (flagStart == 4) {
			g.drawImage(GameResource.instance.imgTienLen_AllCards, GameCanvas.hw, GameCanvas.hh, Graphics.VCENTER
					| Graphics.HCENTER);
			// if (flag_DanhBai_BocBai == 1) {
			if (statusEatCard == 1 || statusEatCard == 2) {
				if (jump >= 0 && jump < 20) {
					jump += 5;
					if (jump >= 20)
						jump = 0;
				} else if (jump >= 20) {
					jump -= 5;
					if (jump < 0)
						jump = 0;
				}
				// HDCGameMidlet.instance.Toast(jump + " lần");
				m_iTick++;
				if (m_iTick > 5) {
					m_iTick = 0;
					m_iId = (m_iId + 1) % 2;
				}

				GameResource.instance.m_framePhom_BocBai.drawFrame(m_iId, GameCanvas.hw, GameCanvas.hh,
						Sprite.TRANS_NONE, Graphics.VCENTER | Graphics.HCENTER, g);
				g.drawImage(GameResource.instance.imgPhom_HighLight_ChonBai, GameCanvas.hw, GameCanvas.hh
						- GameResource.instance.m_framePhom_BocBai.frameHeight / 2 * 3 - jump, Graphics.BOTTOM
						| Graphics.HCENTER);
				if (statusEatCard == 1) {
					GameResource.instance.m_framePhom_AnBai.drawFrame(m_iId, x_lastCard, y_lastCard
							+ (int) (DetailImage.imgCard_h / 1.5 / 2), Sprite.TRANS_NONE, Graphics.VCENTER
							| Graphics.LEFT, g);
					g.drawImage(GameResource.instance.imgPhom_HighLight_ChonBai, x_lastCard
							+ (int) (DetailImage.imgCard_h / 1.5 / 2), y_lastCard - jump, Graphics.BOTTOM
							| Graphics.HCENTER);
				}
			}
		}

		// Nhất - nhì - ba - tu
		for (i = 0; i < m_arrPlayers.length; i++) {
			if (m_arrPlayers[i] != null && m_arrPlayers[i].itemId != -1) {
				m_arrPlayers[i].paintRank(g, m_arrPos[i].anchor, 0);
			}
		}
	}

	@Override
	protected void updateForOrtherCard() {
		int i, j;
		for (i = 0; i < m_iMaxPlayer; i++)
			if (m_arrPlayers[i].itemId != -1)
				for (j = 0; j < m_iMaxNumCard * 3; j++)
					if (m_arrOtherCards[i][j].m_id != -1 && !m_arrOtherCards[i][j].isCallMove)
						m_arrOtherCards[i][j].translate(2);
	}

	public void updateKey() {

		// TODO update key for button
		if (m_bIsPlaying)
			updateKey_ButtonGame();

		if (m_bIsPlaying && !m_bIsDisable) {
			if (GameCanvas.instance.hasPointerEvents()) {
				if (GameCanvas.isPointerClick) {
					int i = m_iMaxNumCard - 1;
					int cW = GameResource.instance.m_imgCards[0].getWidth();
					int cH = GameResource.instance.m_imgCards[0].getHeight() / 4 * 3;
					int selected = -1;
					Card c;
					for (; i >= 0; i--) {
						c = (Card) m_arrOtherCards[m_iMyPosInVt][i];
						if (c.m_id != -1 && GameCanvas.isPointer(c.m_x, c.m_y, cW, cH)) {
							selected = i;
							if (flagButton != 1) {
								if (m_selected == -1) {
									m_selected = selected;
									if (m_arrOtherCards[m_iMyPosInVt][m_selected].yTo == m_iy) {
										m_arrOtherCards[m_iMyPosInVt][m_selected].yTo = m_iyUp;
										m_arrSelectCard[m_selected] = m_arrOtherCards[m_iMyPosInVt][m_selected].m_id;
									} else if (m_arrOtherCards[m_iMyPosInVt][m_selected].yTo == m_iyUp) {
										m_arrOtherCards[m_iMyPosInVt][m_selected].yTo = m_iy;
										m_arrSelectCard[m_selected] = -1;// huy
																			// chọn
									}
								} else {
									if (m_selected != selected) {
										//chọn
										m_arrOtherCards[m_iMyPosInVt][m_selected].yTo = m_iy;
										m_arrSelectCard[m_selected] = -1;// huy
																			// chọn

										m_arrOtherCards[m_iMyPosInVt][selected].yTo = m_iyUp;
										m_arrSelectCard[selected] = m_arrOtherCards[m_iMyPosInVt][selected].m_id;

										m_selected = selected;
									} else {
										//huy chọn
										m_arrOtherCards[m_iMyPosInVt][m_selected].yTo = m_iy;
										m_arrSelectCard[m_selected] = -1;// huy
																			// chọn
										m_selected = -1;
									}
								}

							} else {
								c = (Card) m_arrOtherCards[m_iMyPosInVt][selected];
								if (c.yTo == m_iy) {
									c.yTo = m_iyUp;
									if (m_arrSelectCard[selected] != c.m_id)
										m_arrSelectCard[selected] = c.m_id;
								} else if (c.yTo == m_iyUp) {
									c.yTo = m_iy;
									if (m_arrSelectCard[selected] == c.m_id)
										m_arrSelectCard[selected] = -1;
								}

								if (selected != -1)
									m_selected = selected;
							}
							break;
						}
					}
					c = null;
				}
			}
		}

		super.updateKey();
	}

	// private void swapCard(boolean left) {
	// // TODO Auto-generated method stub
	// if (CRes.countCard(m_arrOtherCards[m_iMyPosInVt]) <= 1)
	// return;
	// int i, j = m_selected;
	// int keep = m_selected;
	//
	// for (i = 0; i < m_iMaxNumCard; i++) {
	// if (left) {
	// j--;
	// if (j <= -1)
	// j = m_iMaxNumCard - 1;
	// } else {
	// j++;
	// if (j == m_iMaxNumCard)
	// j = 0;
	// }
	// if (m_arrOtherCards[m_iMyPosInVt][j].m_id != -1) {
	// break;
	// }
	// }
	//
	// if (!m_bIsPushSort)
	// m_selected = j;
	//
	// if (keep == j)
	// m_selected = -1;
	//
	// if (m_bIsPushSort && m_selected != -1) {
	// if (m_arrOtherCards[m_iMyPosInVt][m_selected].m_x ==
	// m_arrOtherCards[m_iMyPosInVt][m_selected].xTo) {
	// m_selected = j;
	//
	// Card c = new Card(-1);
	// c.CopyTo(m_arrOtherCards[m_iMyPosInVt][m_selected]);
	// m_arrOtherCards[m_iMyPosInVt][m_selected].CopyTo(m_arrOtherCards[m_iMyPosInVt][keep]);
	// m_arrOtherCards[m_iMyPosInVt][keep].CopyTo(c);
	// c = null;
	//
	// m_arrOtherCards[m_iMyPosInVt][m_selected].xTo = (int)
	// m_arrOtherCards[m_iMyPosInVt][keep].m_x;
	// m_arrOtherCards[m_iMyPosInVt][keep].xTo = (int)
	// m_arrOtherCards[m_iMyPosInVt][m_selected].m_x;
	// sortHandPhom();
	// } else
	// m_selected = keep;
	// }
	// }

	private void resetTurnPlayer() {
		if (!m_bIsPlaying) {
			return;
		}

		if (m_strWhoTurn.toLowerCase().equals(HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
			// m_iSelectLast = -1;
			// m_bIsPushSort = false;
			initPosPlayer(m_iMyPosInVt, 0);
			CRes.resetArr(m_arrSelectCard);

			// m_cmdCenter = m_fireCardCmd;
			// if (statusEatCard == 1) {
			// m_cmdCenter = m_eatCardCmd;
			// m_cmdRight = m_getCmd;
			// } else if (statusEatCard == 2) {
			// m_cmdRight = m_getCmd;
			// } else
			// m_cmdRight = null;
			// flag_DanhBai_BocBai = 1;

			int i, count = 0;
			for (i = 0; i < m_iMaxNumCard; i++)
				if (m_arrOtherCards[m_iMyPosInVt][i].m_id != -1)
					count++;

			if (count >= m_iMaxNumCard) {
				statusEatCard = 0;
			}

			// tim dieu kien ha phom
			count = 0;
			for (i = m_iMaxNumCard; i < m_iMaxNumCard * 2; i++) {
				if (m_arrOtherCards[m_iMyPosInVt][i].m_id != -1) {
					count++;
				}
			}

			if (count >= 4) {
				statusEatCard = -1;
				if (m_bIsCanAttachCard) {
					flagButton = 2;
					// m_cmdCenter = m_attachCard;
				} else {
					flagButton = 1;
					// m_cmdCenter = m_haphomCmd;
				}
				// m_cmdRight = null;
			}
		}
		// else {
		// if (m_cmdCenter != m_pushCmd ||
		// m_arrOtherCards[m_iMyPosInVt][m_selected].yTo != m_iyDown) {
		// m_cmdCenter = null;
		// m_cmdRight = null;
		// }
		// }
	}

	public void playerLeave(String strLeave) {
		int pos = findPlayerPos(strLeave);
		if (pos < 0)
			return;

		if (pos >= 0) {
			m_arrPlayerMom[pos] = -1;
		}
		super.playerLeave(strLeave);
	}

	public void onSetTurn(String nick, Message message) {
		// openCmd();
		// trang thái của nguoi choi
		if (MessageIO.readBoolean(message))
			statusEatCard = 1;// an
		else
			statusEatCard = 2;// boc

		// không phải là mình thì ẩn hết
		if (!nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
			statusEatCard = -1;

		m_bIsCanAttachCard = MessageIO.readBoolean(message);
		m_strWhoTurn = nick;
		m_iGameTick = TURN_TIME_OUT;

		resetTurnPlayer();
	}

	private void sortHandPhom() {
		int[] arr = new int[m_iMaxNumCard * 2];
		int[] getCard = new int[m_iMaxNumCard];
		CRes.resetArr(arr);
		int i, j;
		for (i = 0; i < m_iMaxNumCard; i++) {
			getCard[i] = m_arrOtherCards[m_iMyPosInVt][i].m_id;
		}
		int n = CRes.sortHandPhom(getCard, arr);
		sortBaloonOwner(m_iMyPosInVt);

		for (j = 0; j < m_iMaxNumCard; j++) {
			m_arrOtherCards[m_iMyPosInVt][j].isSpecial = false;
			m_arrOtherCards[m_iMyPosInVt][j].isPhom = false;
		}
		for (i = 0; i < n; i++) {
			for (j = 0; j < m_iMaxNumCard; j++) {
				if (m_arrOtherCards[m_iMyPosInVt][j].m_id == arr[i]) {
					m_arrOtherCards[m_iMyPosInVt][j].isSpecial = true;
					break;
				}
			}
		}

		for (i = 0; i < m_arrEarCardOwner.length; i++) {
			for (j = 0; j < m_iMaxNumCard; j++) {
				if (m_arrOtherCards[m_iMyPosInVt][j].m_id == m_arrEarCardOwner[i]) {
					m_arrOtherCards[m_iMyPosInVt][j].isPhom = true;
					break;
				}
			}
		}
		arr = null;
		getCard = null;
	}

	// sắp xếp phỏm
	private void sortPhom(int posPlayer, int posCard, byte typeSort, boolean isMove) {
		int[][] checkData = new int[m_iMaxNumCard][m_iMaxNumCard];
		int[] arrLuu = new int[m_iMaxNumCard];
		int i, j, k;

		for (i = 0; i < m_iMaxNumCard; i++)
			arrLuu[i] = m_arrOtherCards[posPlayer][posCard * m_iMaxNumCard + i].m_id;

		// tìm tất cả các phỏm có khả năng tạo thành
		CRes.getPhom(arrLuu, checkData, typeSort);

		int tmp;
		// nếu là của mình và có ăn phỏm rùi(check lại những trường hợp đặt biệt
		// loại bỏ nó ra khỏi danh sách và lấy cái thix hợp nhất)
		if (posPlayer == m_iMyPosInVt && m_arrEarCardOwner[0] != -1) {
			CRes.clearInArr(checkData, m_arrEarCardOwner);
		}
		CRes.clearPhom(checkData);
		// luu vào
		CRes.resetArr(arrLuu);
		for (i = 0; i < m_iMaxNumCard; i++) {
			if (CRes.countInArr(checkData[i]) < 3)
				continue;
			for (j = 0; j < m_iMaxNumCard; j++) {
				if (checkData[i][j] != -1) {
					for (k = 0; k < m_iMaxNumCard; k++)
						if (arrLuu[k] == -1) {
							arrLuu[k] = checkData[i][j];
							break;
						}
				}
			}
		}
		// copy old pos
		if (posCard == 0)
			sortBaloonOwner(posPlayer);

		Card[] skipCard = new Card[m_iMaxNumCard];
		for (i = 0; i < m_iMaxNumCard; i++) {
			skipCard[i] = new Card(-1);
			skipCard[i].CopyTo(m_arrOtherCards[posPlayer][m_iMaxNumCard * posCard + i]);
		}
		// push all card
		for (i = 0; i < m_iMaxNumCard; i++)
			if (arrLuu[i] == -1)
				break;
		tmp = i;
		for (j = 0; j < m_iMaxNumCard; j++) {
			for (k = 0; k < tmp; k++) {
				if (arrLuu[k] == m_arrOtherCards[posPlayer][m_iMaxNumCard * posCard + j].m_id)
					break;
			}
			if (k == tmp) {
				arrLuu[i] = m_arrOtherCards[posPlayer][m_iMaxNumCard * posCard + j].m_id;
				i++;
			}
		}
		// sx tang dan
		for (i = tmp; i < m_iMaxNumCard; i++) {
			for (j = tmp; j < m_iMaxNumCard; j++) {
				if (arrLuu[i] % 13 < arrLuu[j] % 13 || (arrLuu[i] < arrLuu[j] && arrLuu[i] % 13 == arrLuu[j] % 13)) {
					k = arrLuu[i];
					arrLuu[i] = arrLuu[j];
					arrLuu[j] = k;
				}
			}
		}
		// sx noi bot
		int count = 0;
		for (i = 0; i < m_iMaxNumCard - count; i++) {
			if (arrLuu[i] == -1) {
				for (j = i; j < m_iMaxNumCard - 1; j++) {
					k = arrLuu[j];
					arrLuu[j] = arrLuu[j + 1];
					arrLuu[j + 1] = k;
				}
				i--;
				count++;
			}
		}

		// bo vao card
		for (i = 0; i < m_iMaxNumCard; i++) {
			m_arrOtherCards[posPlayer][m_iMaxNumCard * posCard + i].m_id = arrLuu[i];
			m_arrOtherCards[posPlayer][m_iMaxNumCard * posCard + i].isSpecial = false;
			m_arrOtherCards[posPlayer][m_iMaxNumCard * posCard + i].isPhom = false;
			if (i < tmp)
				m_arrOtherCards[posPlayer][m_iMaxNumCard * posCard + i].isSpecial = true;
		}

		if (isMove) {
			// duy chuyển nó
			for (i = 0; i < m_iMaxNumCard; i++) {
				for (j = 0; j < m_iMaxNumCard; j++) {
					if (skipCard[j].m_id == m_arrOtherCards[posPlayer][m_iMaxNumCard * posCard + i].m_id) {
						m_arrOtherCards[posPlayer][m_iMaxNumCard * posCard + i].m_x = skipCard[j].m_x;
						m_arrOtherCards[posPlayer][m_iMaxNumCard * posCard + i].m_y = skipCard[j].m_y;
						break;
					}
				}
			}
		}
		// get draw card phom
		if (posPlayer != m_iMyPosInVt)
			return;
		for (i = 0; i < m_iMaxNumCard; i++) {
			if (m_arrEarCardOwner[i] != -1) {
				for (j = 0; j < m_iMaxNumCard; j++) {
					if (m_arrEarCardOwner[i] == m_arrOtherCards[m_iMyPosInVt][m_iMaxNumCard * posCard + j].m_id) {
						m_arrOtherCards[m_iMyPosInVt][m_iMaxNumCard * posCard + j].isPhom = true;
						break;
					}
				}
			}
		}
		skipCard = null;
		arrLuu = null;
		checkData = null;
	}

	// // hàm ăn phỏm
	private boolean isCheckMom() {
		int[][] checkData = new int[m_iMaxNumCard][m_iMaxNumCard];
		int[] arrLuu = new int[m_iMaxNumCard];
		int i;

		for (i = 0; i < m_iMaxNumCard; i++)
			arrLuu[i] = m_arrOtherCards[m_iMyPosInVt][i].m_id;

		CRes.getPhom(arrLuu, checkData, 0);

		if (checkData[0][0] != -1) {
			arrLuu = null;
			checkData = null;
			return true;
		}
		arrLuu = null;
		checkData = null;
		return false;
	}

	// card phỏm dánh ra từ ai và những lá bài gì
	public void onFireCard(String name, int[] c) {
		resetCard(m_arrHitCard, -1);
		int i, j, k;
		if (name.toLowerCase().equals(HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
			// chính là mình
			for (i = 0; i < c.length; i++) {
				m_arrHitCard[i].m_id = c[i];
				// tìm vị trí lá bài của mình mà xóa nó di
				for (j = 0; j < m_iMaxNumCard; j++) {
					if (c[i] == m_arrOtherCards[m_iMyPosInVt][j].m_id) {
						m_arrOtherCards[m_iMyPosInVt][j].m_id = -1;
						break;
					}
				}
				// khởi tạo ví trí bay ra cho lá bài đánh ra
				for (k = m_iMaxNumCard; k < (m_iMaxNumCard << 1); k++) {
					if (m_arrOtherCards[m_iMyPosInVt][k].m_id == -1) {
						m_arrOtherCards[m_iMyPosInVt][k].m_id = c[i];
						m_arrOtherCards[m_iMyPosInVt][k].m_x = m_arrOtherCards[m_iMyPosInVt][j].m_x;
						m_arrOtherCards[m_iMyPosInVt][k].m_y = m_arrOtherCards[m_iMyPosInVt][j].m_y;
						break;
					}
				}
				initPosPlayer(m_iMyPosInVt, 0);
				// tim vi tri trong mảng chọn và hủy nó di
				for (j = 0; j < m_arrSelectCard.length; j++) {
					if (c[i] == m_arrSelectCard[j]) {
						m_arrSelectCard[j] = -1;
					}
				}
				// init again position another card
				// sortPhom(m_iMyPosInVt, 0);
				sortHandPhom();
				initPosPlayer(m_iMyPosInVt, 1);
				initPosPlayer(m_iMyPosInVt, 0);
				// init again select card
				j = m_selected;
				if (m_selected != -1) {
					for (k = 0; k < m_iMaxNumCard; k++) {
						if (m_arrOtherCards[m_iMyPosInVt][j].m_id != -1) {
							m_selected = j;
							break;
						}
						j++;
						if (j == m_iMaxNumCard)
							j = 0;
					}
					if (k == m_iMaxNumCard) {
						m_selected = -1;
					}
				} else {
					m_selected = -1;
				}
			}
		} else {
			// không phải là mình
			int pos = findPlayerPos(name);
			if (pos < 0)
				return;

			for (i = 0; i < c.length; i++) {
				m_arrHitCard[i].m_id = c[i];
				for (j = 0; j < m_iMaxNumCard; j++) {
					if (m_arrOtherCards[pos][j].isUpCard && m_arrOtherCards[pos][j].m_id != -1) {
						// kiểm tra nếu là người choi đầu tiên thì đừng có xóa
						// bài của nó
						if (!m_bIsFirstPlayer)
							m_arrOtherCards[pos][j].m_id = -1;
						break;
					}
				}
				// khởi tạo ví trí bay ra cho lá bài đánh ra
				for (k = m_iMaxNumCard; k < (m_iMaxNumCard << 1); k++) {
					if (m_arrOtherCards[pos][k].m_id == -1) {
						m_arrOtherCards[pos][k].m_id = c[i];
						m_arrOtherCards[pos][k].isUpCard = true;
						m_arrOtherCards[pos][k].setActionRotate();
						m_arrOtherCards[pos][k].m_x = m_arrOtherCards[pos][j].m_x;
						m_arrOtherCards[pos][k].m_y = m_arrOtherCards[pos][j].m_y;
						break;
					}
				}
				// 0 : bai cua minh (bài ăn)
				// 1 : đánh ra
				// 2 : phỏm
				initPosPlayer(pos, 1);
				initPosPlayer(pos, 0);
				for (k = m_iMaxNumCard; k < (m_iMaxNumCard << 1); k++) {
					if (m_arrOtherCards[pos][k].m_id == c[i]) {
						x_lastCard = m_arrOtherCards[pos][k].xTo;
						y_lastCard = m_arrOtherCards[pos][k].yTo;
					}
				}
			}

		}
		// trả về trạng thái không xóa bài cho người đầu tiên
		m_bIsFirstPlayer = false;
	}

	private void sortBaloonOwner(int pos) {
		// sx noi bot
		int count = 0;
		int i, j;
		Card k = new Card(-1);
		for (i = 0; i < m_iMaxNumCard - count; i++) {
			if (m_arrOtherCards[pos][i].m_id == -1) {
				for (j = i; j < m_iMaxNumCard - 1; j++) {
					k.CopyTo(m_arrOtherCards[pos][j]);
					m_arrOtherCards[pos][j].CopyTo(m_arrOtherCards[pos][j + 1]);
					m_arrOtherCards[pos][j + 1].CopyTo(k);
				}
				i--;
				count++;
			}
		}
		k = null;
	}

	// lấy lá bài về
	public void onGetCardNocSuccess(String name, byte card) {
		int i;
		if (name.toLowerCase().equals(HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
			sortBaloonOwner(m_iMyPosInVt);
			for (i = 0; i < m_iMaxNumCard; i++) {
				if (m_arrOtherCards[m_iMyPosInVt][i].m_id == -1) {
					m_arrOtherCards[m_iMyPosInVt][i].m_id = card;
					// gán cho nó vị trí bay ra
					m_arrOtherCards[m_iMyPosInVt][i].setPos(
							GameCanvas.hw - GameResource.instance.imgTienLen_CardBack2.getWidth() / 2, GameCanvas.hh
									- GameResource.instance.imgTienLen_CardBack2.getHeight());
					// điều kiện xoay
					m_arrOtherCards[m_iMyPosInVt][i].isUpCard = true;
					m_arrOtherCards[m_iMyPosInVt][i].setActionRotate();
					// xet scale
					m_arrOtherCards[m_iMyPosInVt][i].setScale(1.5f);
					m_arrOtherCards[m_iMyPosInVt][i].scaleTo = 1f;
					break;
				}
			}
			sortHandPhom();
			initPosPlayer(m_iMyPosInVt, 0);
			for (i = 0; i < m_iMaxNumCard; i++) {
				if (m_arrSelectCard[i] != -1) {
					m_arrOtherCards[m_iMyPosInVt][i].yTo = m_iyUp;
					break;
				}
			}
		} else {
			int pos = findPlayerPos(name);
			for (i = 0; i < m_iMaxNumCard; i++) {
				if (m_arrOtherCards[pos][i].m_id == -1) {
					m_arrOtherCards[pos][i].m_id = 0;
					m_arrOtherCards[pos][i].isUpCard = true;
					m_arrOtherCards[pos][i].m_x = GameCanvas.hw - GameResource.instance.imgTienLen_CardBack2.getWidth()
							/ 2;
					m_arrOtherCards[pos][i].m_y = GameCanvas.hh
							- GameResource.instance.imgTienLen_CardBack2.getHeight();
					initPosPlayer(pos, 0);
					break;
				}
			}
		}
	}

	public void onAllCardPlayerFinish(Message message) {
		flagButton = 0;
		String nick = MessageIO.readString(message);
		byte c[] = MessageIO.readBytes(message);
		int i, card[] = new int[c.length];
		for (i = 0; i < c.length; i++) {
			card[i] = c[i];
		}

		if (!nick.toLowerCase().equals(HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
			int pos = findPlayerPos(nick);
			if (pos < 0)
				return;

			if (m_arrPlayerMom[pos] == -2)
				return;
			// reset all
			for (i = 0; i < m_iMaxNumCard; i++) {
				m_arrOtherCards[pos][i].m_id = -1;
			}
			// dua vao
			for (i = 0; i < card.length; i++) {
				m_arrOtherCards[pos][i].m_id = card[i];
				m_arrOtherCards[pos][i].m_x = m_arrPos[pos].x;
				m_arrOtherCards[pos][i].m_y = m_arrPos[pos].y;
				m_arrOtherCards[pos][i].isUpCard = true;
				m_arrOtherCards[pos][i].setActionRotate();
			}
			sortPhom(pos, 0, (byte) 1, true);
			initPosPlayer(pos, 0);
		}
		card = null;
		nick = null;
		
	}

	public void onUserJoinTable(int tbID, String nick, boolean isChampion, int pos, long money, int level,
			int avatarId, int playerStatus) {
		super.onUserJoinTable(tbID, nick, isChampion, pos, money, level, avatarId, playerStatus);
		if (!m_bIsPlaying) {
			resetCard(m_arrHitCard, -1);
			resetCard(m_arrOtherCards[pos], -1);
			boolean have = false;
			for (int i = 0; i < m_iMaxNumCard * 3; i++) {
				if (m_arrOtherCards[m_iMyPosInVt][i].m_id != -1) {
					have = true;
					break;
				}
			}
			if (!have)
				resetOtherCards(false);
		}
	}

	public void actionEndGame() {
		super.actionEndGame();
		resetCard(m_arrHitCard, -1);
		CRes.resetArr(m_arrSelectCard);
		m_selected = -1;
		for (int i = 0; i < m_arrOtherCards.length; i++)
			resetCard(m_arrOtherCards[i], -1);
	}

	public void displayAllCardJoinGame(Message message) {
		int total = MessageIO.readByte(message);
		int i, j;
		String nick;
		int pos;
		for (i = 0; i < total; i++) {
			nick = MessageIO.readString(message);
			pos = findPlayerPos(nick);
			readMessage(message, pos, 1);
			readMessage(message, pos, 0);
			for (j = 0; j < m_iMaxNumCard * 2; j++) {
				if (m_arrOtherCards[pos][j].m_id != -1) {
					m_arrOtherCards[pos][j].m_x = m_arrOtherCards[pos][j].xTo;
					m_arrOtherCards[pos][j].m_y = m_arrOtherCards[pos][j].yTo;
				}
			}
		}
	}

	private void readMessage(Message message, int pos, int rank) {
		byte[] b = MessageIO.readBytes(message);
		int k, j;
		for (k = 0; k < b.length; k++) {
			for (j = m_iMaxNumCard * rank; j < m_iMaxNumCard * (rank + 1); j++) {
				if (m_arrOtherCards[pos][j].m_id == -1) {
					m_arrOtherCards[pos][j].m_id = b[k];
					if (rank == 0)
						m_arrOtherCards[pos][j].isPhom = true;
					break;
				}
			}
		}
		if (rank == 0) {
			for (j = 0; j < m_iMaxNumCard - 1; j++) {
				if (m_arrOtherCards[pos][j].m_id == -1) {
					m_arrOtherCards[pos][j].m_id = 0;
					m_arrOtherCards[pos][j].isUpCard = true;
				}
			}
		}
		initPosPlayer(pos, rank);
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
