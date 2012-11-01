package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.animation.FlyText;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.PlayerInfo;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.GameResource;

public class XiDachScr extends PlayGameScr {

	static XiDachScr instance;

	public static XiDachScr gI() {
		if (instance == null) {
			instance = new XiDachScr((byte) 4, (byte) 5);
		}
		return instance;
	}

	protected Card[][] m_arrOtherCards;
	public byte m_iScorePlayer;

	// protected boolean m_bIsStartDeliver;

	protected Command cmdDang;
	protected Command cmdRut;
	Command cmdMo;
	protected Command cmdMoHet;

	protected Vector m_arrPlayerInGame;
	protected int m_iHPerCard = 20;

	public XiDachScr(byte maxPlayer, byte maxNumCard) {
		super(maxPlayer, maxNumCard);
		int i, j;
		m_arrOtherCards = new Card[maxPlayer][maxNumCard];
		for (i = 0; i < maxPlayer; i++)
			for (j = 0; j < maxNumCard; j++)
				m_arrOtherCards[i][j] = new Card(-1);

		m_arrPlayerInGame = new Vector();

		cmdDang = new Command("Dằng", new IAction() {

			public void perform() {
				GlobalService.onsendHoldCard();
			}
		});

		cmdRut = new Command("Rút", new IAction() {
			public void perform() {
				GlobalService.sendMessageGetNewCard();
			}
		});

		cmdMo = new Command("Mở", new IAction() {
			public void perform() {
				Vector vt = new Vector();
				for (int i = 0; i < m_arrPlayerInGame.size(); i++) {
					final String str = ((PlayerInfo) m_arrPlayerInGame.elementAt(i)).itemName;
					if (!str.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
						vt.addElement(new Command(str, new IAction() {
							public void perform() {
								GlobalService.onSendOpenOne(str);
							}
						}));
					}
				}
				GameCanvas.instance.menu.startAt(vt, 0);
				vt = null;
			}
		});

		cmdMoHet = new Command("Mở hết", new IAction() {
			public void perform() {
				GlobalService.onSendOpenAll();
			}
		});
	}

	public void switchToMe(String ownerString) {
		super.switchToMe(ownerString);
		Card.setCardType(Card.PHOM);
		m_iScorePlayer = 0;
		// m_bIsStartDeliver = false;
		resetOtherCards(true);
	}

	public void update() {
		deliverCard();

		if (m_arrOtherCards != null) {
			byte i, j;
			for (i = 0; i < m_iMaxPlayer; i++)
				if (m_arrPlayers[i].itemId != -1)
					for (j = 0; j < m_iMaxNumCard; j++)
						if (m_arrOtherCards[i][j].m_id != -1 && !m_arrOtherCards[i][j].isCallMove)
							m_arrOtherCards[i][j].translate(2);
		}
		super.update();
	}

	// private void deliverCard() {
	// // TODO Auto-generated method stub
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
	// }
	// }
	// }

	public void paintPlayGame(Graphics g) {
		// TODO Auto-generated method stub
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
	}

	public void startGame(Message message) {
		super.startGame();

		resetOtherCards(true);
		byte c[] = MessageIO.readBytes(message);
		// /2 lá bài của nguoi choi
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
		for (i = 0; i < m_iMaxPlayer; i++) {
			if (m_arrPlayers[i].itemId != -1) {
				initPosPlayer(i);
				m_arrPlayerInGame.addElement(m_arrPlayers[i]);
			}
		}

		m_strWhoTurn = "";
		m_iScorePlayer = 0;
		// m_bIsStartDeliver = true;

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
			if (nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
				m_iScorePlayer = MessageIO.readByte(message);
				int pos = findPlayerPos(nick);
				FlyText f = new FlyText();
				f.startEffect(m_iScorePlayer + "", m_arrPos[pos].x, m_arrPos[pos].y, 0xffff00,
						0x000000);
				initComand();
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

	public void getNewCard(String nick, byte card) {
		int pos = findPlayerPos(nick);
		if (pos < 0)
			return;

		int i;
		for (i = 0; i < m_iMaxNumCard; i++) {
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

	public void initComand() {
		m_cmdCenter = cmdRut;
		if (HDCGameMidlet.m_myPlayerInfo.itemName.equals(m_strOwnerName) && m_iScorePlayer >= 15) {
			if (m_arrPlayerInGame.size() > 2)
				m_cmdLeft = cmdMo;
			m_cmdRight = cmdMoHet;
		} else if (m_iScorePlayer >= 16)
			m_cmdRight = cmdDang;

		if (m_iScorePlayer >= 21) {
			m_cmdCenter = null;
			m_cmdRight = null;
			m_cmdLeft = menuNormalCmd;
		}
	}

	private void initPosPlayer(int posPlayer) {
		int j;
		int i = posPlayer;
		int numCard = 0;
		for (j = 0; j < m_iMaxNumCard; j++) {
			if (m_arrOtherCards[i][j].m_id != -1)
				numCard++;
		}

		int xCenter = (GameCanvas.w - numCard * m_iUseWCard) >> 1;
		int yCenter = GameCanvas.ch - ((numCard * m_iHPerCard) >> 1);
		int xOwner = (GameCanvas.w - numCard * m_iUseWCard) >> 1;

		int k = 0;
		for (j = 0; j < m_iMaxNumCard; j++) {
			if (m_arrOtherCards[i][j].m_id == -1)
				continue;
			switch (m_arrPos[i].anchor) {
			case 1:
			case 3:
				m_arrOtherCards[i][j].yTo = yCenter + k * m_iHPerCard;
				break;
			case 2:
				m_arrOtherCards[i][j].xTo = xCenter + k * m_iUseWCard;
				break;
			default:
				m_arrOtherCards[i][j].xTo = xOwner + k * m_iUseWCard;
				break;
			}
			k++;
		}
	}

	protected void resetOtherCards(boolean isClear) {
		int i, j;
		int xCenter = (GameCanvas.w - m_iMaxNumCard * m_iUseWCard) >> 1;
		int yCenter = GameCanvas.ch - ((m_iMaxNumCard * m_iHPerCard) >> 1);
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
					m_arrOtherCards[i][j].xTo = 0;
					m_arrOtherCards[i][j].yTo = yCenter + j * m_iHPerCard;
					break;
				case 2:
					m_arrOtherCards[i][j].xTo = xCenter + j * m_iUseWCard;
					m_arrOtherCards[i][j].yTo = 0;
					break;
				case 3:
					m_arrOtherCards[i][j].xTo = GameCanvas.w - m_iUseWCard;
					m_arrOtherCards[i][j].yTo = yCenter + j * m_iHPerCard;
					break;
				default:
					m_arrOtherCards[i][j].xTo = xCenter + j * m_iUseWCard;
					m_arrOtherCards[i][j].yTo = m_iy;
					break;
				}
			}
		}
	}

	public void actionEndGame() {
		super.actionEndGame();
		for (byte i = 0; i < m_iMaxPlayer; i++)
			resetCard(m_arrOtherCards[i], -1);
	}

	public void onUserJoinTable(int tbID, String nick, boolean isChampion, int pos, long money,
			int level, int avatarId, int playerStatus) {
		super.onUserJoinTable(tbID, nick, isChampion, pos, money, level, avatarId, playerStatus);
		if (!m_bIsPlaying) {
			boolean have = false;
			for (int i = 0; i < m_iMaxNumCard; i++) {
				if (m_arrOtherCards[m_iMyPosInVt][i].m_id != -1) {
					have = true;
					break;
				}
			}
			resetCard(m_arrOtherCards[pos], -1);
			if (!have)
				resetOtherCards(false);
		}
	}

	public void playerLeave(String strLeave) {
		int pos = findPlayerPos(strLeave);
		if (pos >= 0 && pos < m_iMaxPlayer) {
			m_arrPlayerInGame.removeElementAt(pos);
		}
		super.playerLeave(strLeave);
	}

	public static void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub

	}
}
