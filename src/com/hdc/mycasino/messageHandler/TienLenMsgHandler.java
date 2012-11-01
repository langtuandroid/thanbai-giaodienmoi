package com.hdc.mycasino.messageHandler;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.model.CardTest;
import com.hdc.mycasino.network.CMD;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.screen.Card;
import com.hdc.mycasino.screen.TienLenScr;
import com.hdc.mycasino.utilities.CRes;

public class TienLenMsgHandler implements IMessageHandler {
	static TienLenMsgHandler m_instance = null;

	public static TienLenMsgHandler gI() {
		if (m_instance == null) {
			m_instance = new TienLenMsgHandler();
		}
		return m_instance;
	}

	public static void onHandler() {
		GlobalMsgHandler.gI().miniGameMessageHandler = TienLenMsgHandler.gI();
	}

	public static CardTest convertByteToCard(int value) {
		int cardVal;
		char cardType = 0;
		int temp = 0;
		cardVal = (int) ((value % 13) + 3);
		if (cardVal > 13) {
			cardVal = cardVal - 13;
		}
		temp = (int) value / 13;
		switch (temp) {
		case 0:
			cardType = 'B';
			break;
		case 1:
			cardType = 'T';
			break;
		case 2:
			cardType = 'R';
			break;
		case 3:
			cardType = 'C';
			break;
		}
		return new CardTest(cardType, cardVal);
	}

	public void onMessage(Message message) {
		try {
			switch (message.getCommand()) {
			case CMD.CMD_PLAYER_START_GAME:
//				TienLenScr.gI().openCmd();
				int by = MessageIO.readByte(message);
				if (by == 0) {
					String info = MessageIO.readString(message);
//					GameCanvas.startOKDlg(info);
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",info);
					info = null;
				} else {
					TienLenScr.gI().startGame(message);
				}
				break;

			case CMD.CMD_FIRE_CARD:
				String nick = MessageIO.readString(message);
				if (nick.toLowerCase().equals("f")) {
//					TienLenScr.gI().openCmd();
					TienLenScr.gI().checkNoFireCard(message);
				} else {
					byte cardfire[] = MessageIO.readBytes(message);
					// System.err.println(cardfire[0]);
					//
					// CardTest cardTest = convertByteToCard(cardfire[0]);
					// HDCGameMidlet.instance.Toast(cardfire[0] +
					// cardTest.toString());

					CRes.SortCardTL(cardfire, (byte) 0);
					int data[] = new int[cardfire.length];
					for (int i = 0; i < data.length; i++) {
						data[i] = cardfire[i];
					}

					TienLenScr.gI().onFireCard(nick, data);
					cardfire = null;
					data = null;
				}
				nick = null;
				break;
			case CMD.CMD_VIEW_TIEN_LEN:
				TienLenScr.gI().displayCard(message);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onConnectionFail() {

	}

	public void onDisconnected() {

	}

	public void onConnectOK() {

	}
}
