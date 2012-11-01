package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.animation.FlyText;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.GameResource;

public class BlackJackScr extends XiDachScr {

	static BlackJackScr instance;

	public static BlackJackScr getInstance() {
		if (instance == null) {
			instance = new BlackJackScr((byte) 4, (byte) 10);
		}
		return instance;
	}

	public boolean[] m_arrSplit;
	boolean isHaveInsurance;
	boolean isHaveSplit;

	Command cmdDouble;
	Command cmdSplit;
	Command cmdInsurance;

	public BlackJackScr(byte maxPlayer, byte maxNumCard) {
		super(maxPlayer, maxNumCard);
		// TODO Auto-generated constructor stub
		m_arrSplit = new boolean[maxPlayer];

		cmdDang.caption = "Stand";
		cmdRut.caption = "Hit";
		cmdMoHet.caption = "Open";

		cmdDouble = new Command("Double", new IAction() {

			public void perform() {
				// TODO Auto-generated method stub
				GlobalService.onSendDouble();
			}
		});

		cmdSplit = new Command("Split", new IAction() {

			public void perform() {
				// TODO Auto-generated method stub
				isHaveSplit = false;
				GlobalService.onSendSplit();
			}
		});

		cmdInsurance = new Command("Insurance", new IAction() {

			public void perform() {
				// TODO Auto-generated method stub
				isHaveInsurance = false;
				GlobalService.onSendInsurance();
			}
		});
	}

	public void switchToMe(String ownerString) {
		super.switchToMe(ownerString);
		for (int i = 0; i < m_iMaxPlayer; i++)
			m_arrSplit[i] = false;
		isHaveInsurance = false;
		isHaveSplit = false;
	}

	public void startGame(Message message) {
		// TODO Auto-generated method stub
		super.startGame();

		resetOtherCards(true);
		// 2 la bai cua nguoi choi
		// neu la nha con thi them 1 la bai cua chu ban
		byte c[] = MessageIO.readBytes(message);
		byte card = -1;
		if (!m_strOwnerName.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
			card = MessageIO.readByte(message);
		int posOwner = findPlayerPos(m_strOwnerName);

		int i, j;
		for (i = 0; i < c.length; i++) {
			for (j = 0; j < m_iMaxPlayer; j++) {
				if (m_arrPlayers[j].itemId != -1) {
					m_arrOtherCards[j][i].m_id = 0;
					if (j == m_iMyPosInVt) {
						m_arrOtherCards[j][i].m_id = c[i];
						m_arrOtherCards[j][i].setActionRotate();
					}
					m_arrOtherCards[j][i].isUpCard = true;
					m_arrOtherCards[j][i].isCallMove = true;
				}
			}
		}

		// gan la bai xuat hien cho nguoi chu ban
		isHaveInsurance = false;
		if (card != -1 && posOwner != -1 && posOwner != m_iMyPosInVt) {
			m_arrOtherCards[posOwner][1].m_id = card;
			m_arrOtherCards[posOwner][1].setActionRotate();
			if (card % 13 == 0)
				isHaveInsurance = true;
		}

		for (i = 0; i < m_iMaxPlayer; i++) {
			if (m_arrPlayers[i].itemId != -1) {
				initPosPlayer(i);
			}
			m_arrSplit[i] = false;
		}

		// kiem tra co split card duoc hay ko
		isHaveSplit = false;
		if (c[0] % 13 == c[1] % 13)
			isHaveSplit = true;

		m_strWhoTurn = "";
		m_iScorePlayer = 0;
		m_bIsStartDeliver = true;

		c = null;
		System.gc();
	}

	public void onSetTurn(String nick, Message message) {
		try {
			m_strWhoTurn = nick;
			m_iGameTick = TURN_TIME_OUT;
			m_cmdCenter = null;
			m_cmdRight = null;
			m_cmdLeft = menuNormalCmd;
			// tra ve luot cua nguoi choi và số điểm hiện tại của người đó
			if (nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
				m_iScorePlayer = MessageIO.readByte(message);

				int pos = findPlayerPos(nick);
				FlyText f = new FlyText();
				f.startEffect(XiDachScr.gI().m_iScorePlayer + "", XiDachScr.gI().m_arrPos[pos].x,
						XiDachScr.gI().m_arrPos[pos].y, 0xffff00, 0x000000);

				initComand(true);
			}
		} catch (Exception e) {
		}
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

	}

	public void getNewCard(String nick, byte card, boolean left) {
		int pos = findPlayerPos(nick);
		if (pos < 0)
			return;

		int i, rank = 0;
		if (!left)
			rank = m_iMaxNumCard >> 1;

		for (i = 0 + rank; i < (m_iMaxNumCard >> 1) + rank; i++) {
			if (m_arrOtherCards[pos][i].m_id == -1) {
				m_arrOtherCards[pos][i].m_id = card;
				m_arrOtherCards[pos][i].isUpCard = true;
				break;
			}
		}
		if (pos == m_iMyPosInVt && m_arrOtherCards[pos][i].isUpCard
				&& !m_arrOtherCards[pos][i].isActionRotate) {
			m_arrOtherCards[pos][i].setActionRotate();
		}
		initPosPlayer(pos);
	}

	public void initComand(boolean isLeft) {
		m_cmdCenter = cmdRut;

		if (HDCGameMidlet.m_myPlayerInfo.itemName.equals(m_strOwnerName)) {
			if (m_iScorePlayer >= 17)
				m_cmdCenter = cmdMoHet;
		} else {
			m_cmdRight = cmdDang;

			int count = 0, i;
			int rank = 0;
			if (!isLeft)
				rank = m_iMaxNumCard >> 1;

			for (i = 0 + rank; i < (m_iMaxNumCard >> 1) + rank; i++)
				if (m_arrOtherCards[m_iMyPosInVt][i].m_id != -1)
					count++;
			if (count > 2)
				m_cmdCenter = cmdRut;
			else {
				final Vector vt = new Vector();
				if (isHaveInsurance) {
					m_cmdCenter = cmdInsurance;

					vt.addElement(cmdRut);
					if (isHaveSplit)
						vt.addElement(cmdSplit);
					vt.addElement(cmdDouble);

					m_cmdLeft = new Command("More", new IAction() {
						public void perform() {
							// TODO Auto-generated method stub
							GameCanvas.instance.menu.startAt(vt, 0);
						}
					});
				} else {
					m_cmdCenter = cmdRut;

					if (isHaveSplit) {
						vt.addElement(cmdSplit);
						vt.addElement(cmdDouble);

						m_cmdLeft = new Command("More", new IAction() {
							public void perform() {
								// TODO Auto-generated method stub
								GameCanvas.instance.menu.startAt(vt, 0);
							}
						});
					} else
						m_cmdLeft = cmdDouble;
				}
			}
		}

		if (m_iScorePlayer > 21) {
			m_cmdCenter = null;
			m_cmdRight = null;
			m_cmdLeft = menuNormalCmd;
		}
	}

	private void initPosPlayer(int posPlayer) {
		int j, i = posPlayer, numCardLeft = 0, numCardRight = 0;
		for (j = 0; j < m_iMaxNumCard; j++) {
			if (m_arrOtherCards[i][j].m_id != -1) {
				if (j < m_iMaxNumCard >> 1)
					numCardLeft++;
				else
					numCardRight++;
			}
		}
		System.out.println(numCardLeft + "          888888888      " + numCardRight);

		int xCenterLeft = (GameCanvas.w - numCardLeft * m_iUseWCard) >> 1;
		int yCenterLeft = GameCanvas.ch - ((numCardLeft * m_iHPerCard) >> 1);
		int xOwnerLeft = (GameCanvas.w - numCardLeft * m_iUseWCard) >> 1;

		int xCenterRight = (GameCanvas.w - numCardRight * m_iUseWCard) >> 1;
		int yCenterRight = GameCanvas.ch - ((numCardRight * m_iHPerCard) >> 1);
		int xOwnerRight = (GameCanvas.w - numCardRight * m_iUseWCard) >> 1;

		byte k = 0;
		for (j = 0; j < m_iMaxNumCard; j++) {
			if (m_arrOtherCards[i][j].m_id == -1)
				continue;

			switch (m_arrPos[i].anchor) {
			case 1:
			case 3:
				if (j < m_iMaxNumCard >> 1)
					m_arrOtherCards[i][j].yTo = yCenterLeft + k * m_iHPerCard;
				else
					m_arrOtherCards[i][j].yTo = xCenterRight + k * m_iHPerCard;
				break;
			case 2:
				if (j < m_iMaxNumCard >> 1)
					m_arrOtherCards[i][j].xTo = xCenterLeft + k * m_iUseWCard;
				else
					m_arrOtherCards[i][j].xTo = yCenterRight + k * m_iUseWCard;
				break;
			default:
				if (j < m_iMaxNumCard >> 1)
					m_arrOtherCards[i][j].xTo = xOwnerLeft + k * m_iUseWCard;
				else
					m_arrOtherCards[i][j].xTo = xOwnerRight + k * m_iUseWCard;
				break;
			}
			k++;
		}
	}

	protected void resetOtherCards(boolean isClear) {
		byte i, j;
		int NumCard = m_iMaxNumCard >> 1;
		int xCenter = (GameCanvas.w - NumCard * m_iUseWCard) >> 1;
		int yCenter = GameCanvas.ch - ((NumCard * m_iHPerCard) >> 1);
		int m_iy = GameCanvas.h - GameCanvas.hBottomBar - GameResource.instance.imgCard.getHeight()
				* 3 / 4;

		for (i = 0; i < m_iMaxPlayer; i++) {
			for (j = 0; j < m_iMaxNumCard; j++) {
				if (isClear)
					m_arrOtherCards[i][j].m_id = -1;

				m_arrOtherCards[i][j].isUpCard = true;
				m_arrOtherCards[i][j].isCallMove = false;

				m_arrOtherCards[i][j].m_x = GameCanvas.hw;
				m_arrOtherCards[i][j].m_y = GameCanvas.ch;
				switch (m_arrPos[i].anchor) {
				case 1:
					m_arrOtherCards[i][j].xTo = j / NumCard * m_iUseWCard;
					m_arrOtherCards[i][j].yTo = yCenter + j * m_iHPerCard;
					break;
				case 2:
					m_arrOtherCards[i][j].xTo = xCenter + j * m_iUseWCard;
					m_arrOtherCards[i][j].yTo = j / NumCard * m_iHPerCard;
					break;
				case 3:
					m_arrOtherCards[i][j].xTo = GameCanvas.w - ((j / NumCard + 1) * m_iUseWCard);
					m_arrOtherCards[i][j].yTo = yCenter + j * m_iHPerCard;
					break;
				default:
					m_arrOtherCards[i][j].xTo = xCenter + j * m_iUseWCard;
					m_arrOtherCards[i][j].yTo = m_iy - j / NumCard * m_iHPerCard;
					break;
				}
			}
		}
	}

	public void paintPlayGame(Graphics g) {
		// TODO Auto-generated method stub
		if (m_arrOtherCards != null) {
			byte i, j;
			for (i = 0; i < m_iMaxPlayer; i++) {
				if (m_arrPlayers[i].itemId != -1) {
					if (m_arrPos[i].anchor == 3 || m_arrPos[i].anchor == 0) {
						for (j = (byte) (m_iMaxNumCard >> 1); j < m_iMaxNumCard; j++)
							if (m_arrOtherCards[i][j].m_id != -1
									&& !m_arrOtherCards[i][j].isCallMove)
								m_arrOtherCards[i][j].paint(g);
						for (j = 0; j < m_iMaxNumCard >> 1; j++)
							if (m_arrOtherCards[i][j].m_id != -1
									&& !m_arrOtherCards[i][j].isCallMove)
								m_arrOtherCards[i][j].paint(g);
					} else {
						for (j = 0; j < m_iMaxNumCard; j++)
							if (m_arrOtherCards[i][j].m_id != -1
									&& !m_arrOtherCards[i][j].isCallMove)
								m_arrOtherCards[i][j].paint(g);
					}
				}
			}
		}
	}

	public void effectSplitCard(int pos) {
		// TODO Auto-generated method stub
		m_arrOtherCards[pos][m_iMaxNumCard >> 1].m_id = m_arrOtherCards[pos][1].m_id;
		m_arrOtherCards[pos][m_iMaxNumCard >> 1].isUpCard = false;
		m_arrOtherCards[pos][m_iMaxNumCard >> 1].m_x = m_arrOtherCards[pos][1].xTo;
		m_arrOtherCards[pos][m_iMaxNumCard >> 1].m_y = m_arrOtherCards[pos][1].yTo;

		for (byte i = 0; i < m_iMaxNumCard; i++)
			if (i != 0 && i != m_iMaxNumCard >> 1)
				m_arrOtherCards[pos][i].m_id = -1;
		initPosPlayer(pos);
	}
}
