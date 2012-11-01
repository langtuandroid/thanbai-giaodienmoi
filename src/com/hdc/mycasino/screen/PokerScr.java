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

public class PokerScr extends XiDachScr {

	static PokerScr instance;

	public static PokerScr getInstance() {
		if (instance == null) {
			instance = new PokerScr((byte) 4, (byte) 2);
		}
		return instance;
	}

	private Vector m_listOwnerCards;

	Command checkCmd;
	Command raiseCmd;
	Command foldCmd;

	int m_iHPerCard = 20;

	boolean m_bIsFirstRaise;

	public static void clear() {
		if (instance != null) {
			instance.m_arrOtherCards = null;
			instance.m_listOwnerCards = null;
			instance.checkCmd = null;
			instance.raiseCmd = null;
			instance.foldCmd = null;
		}
		instance = null;
	}

	public PokerScr(byte maxPlayer, byte maxNumCard) {
		super(maxPlayer, maxNumCard);
		// TODO Auto-generated constructor stub
		m_listOwnerCards = new Vector();

		checkCmd = new Command("Check", new IAction() {

			public void perform() {
				// TODO Auto-generated method stub

			}
		});

		raiseCmd = new Command("Raise", new IAction() {

			public void perform() {
				// TODO Auto-generated method stub

			}
		});

		foldCmd = new Command("Fold", new IAction() {

			public void perform() {
				// TODO Auto-generated method stub

			}
		});
	}

	public void switchToMe(String ownerString) {
		super.switchToMe(ownerString);
		m_bIsFirstRaise = false;
	}

	public void startGame(Message message) {
		// read current player's cards
		super.startGame();
		resetOtherCards(true);
		m_listOwnerCards.removeAllElements();

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

		m_strWhoTurn = "";
		m_iScorePlayer = 0;
		m_bIsStartDeliver = true;
		m_bIsFirstRaise = false;

		c = null;
		System.gc();
	}

	public void update() {
		if (m_listOwnerCards.size() > 0) {
			byte i;
			Card c;
			for (i = 0; i < m_listOwnerCards.size(); i++) {
				c = (Card) m_listOwnerCards.elementAt(i);
				if (c.m_id != -1 && !c.isCallMove)
					c.translate(2);
			}
			c = null;
		}
		super.update();
	}

	public void paintPlayGame(Graphics g) {
		// TODO Auto-generated method stub
		if (m_listOwnerCards.size() > 0) {
			byte i;
			Card c;
			for (i = 0; i < m_listOwnerCards.size(); i++) {
				c = (Card) m_listOwnerCards.elementAt(i);
				if (c.m_id != -1 && !c.isCallMove)
					c.paint(g);
			}
			c = null;
		}
		super.paintPlayGame(g);
	}

	public void onSetTurn(String nick, Message message) {
		try {
			m_strWhoTurn = nick;
			m_iGameTick = TURN_TIME_OUT;
			m_cmdCenter = null;
			m_cmdRight = null;
			m_cmdLeft = menuNormalCmd;
			if (nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
				// init command
				if (m_bIsFirstRaise)
					m_cmdLeft = checkCmd;
				m_cmdCenter = raiseCmd;
				m_cmdRight = foldCmd;
			}
		} catch (Exception e) {
		}
	}

	public void getMoreCard(Message message) {
		// TODO Auto-generated method stub
		m_bIsFirstRaise = true;
		byte c[] = MessageIO.readBytes(message);
		Card card;
		for (int i = 0; i < c.length; i++) {
			card = new Card(c[i]);
			card.m_x = GameCanvas.hw;
			card.m_y = GameCanvas.ch - m_iHPerCard;
			m_listOwnerCards.addElement(card);
		}
		int w = m_iUseWCard * m_listOwnerCards.size();
		for (int i = 0; i < m_listOwnerCards.size(); i++) {
			card = (Card) m_listOwnerCards.elementAt(i);
			card.yTo = (int)card.m_y;
			card.xTo = GameCanvas.hw - w + i * m_iUseWCard;
		}
		card = null;
	}

	public void actionPlayer(Message message) {
		// TODO Auto-generated method stub
		String nick = MessageIO.readString(message);
		// action nick
		// 0-check - 1-raise - 3 - fold
		byte action = MessageIO.readByte(message);
		long money = MessageIO.readLong(message);
		FlyText f;
		int pos = findPlayerPos(nick);

		switch (action) {
		case 0:
			m_bIsFirstRaise = false;
			f = new FlyText();
			f.startEffect(nick + " check", m_arrPos[pos].x, m_arrPos[pos].y, 0xffff00, 0x000000);
			break;
		case 1:
			f = new FlyText();
			f.startEffect(nick + " raise " + money, m_arrPos[pos].x, m_arrPos[pos].y, 0xffff00,
					0x000000);
			break;
		case 2:
			f = new FlyText();
			f.startEffect(nick + " hold", m_arrPos[pos].x, m_arrPos[pos].y, 0xffff00, 0x000000);
			break;
		default:
			break;
		}
	}

}
