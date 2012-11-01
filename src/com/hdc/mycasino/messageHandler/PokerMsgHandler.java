package com.hdc.mycasino.messageHandler;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.network.CMD;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.screen.PokerScr;

public class PokerMsgHandler implements IMessageHandler {

	static PokerMsgHandler m_instance = null;

	public static PokerMsgHandler gI() {
		if (m_instance == null) {
			m_instance = new PokerMsgHandler();
		}
		return m_instance;
	}

	public static void onHandler() {
		GlobalMsgHandler.gI().miniGameMessageHandler = PokerMsgHandler.gI();
	}

	public void onMessage(Message message) {
		try {
			switch (message.getCommand()) {
			case CMD.CMD_PLAYER_START_GAME:
//				PokerScr.getInstance().openCmd();
				int by = MessageIO.readByte(message);
				if (by == 0) {
//					GameCanvas.startOKDlg(MessageIO.readString(message));
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",MessageIO.readString(message));
				} else {
					PokerScr.getInstance().startGame(message);
				}
				break;
			case CMD.CMD_GET_NEW_CARD:
				PokerScr.getInstance().getMoreCard(message);
				break;
			case CMD.CMD_ACTION_PLAYER:
				PokerScr.getInstance().actionPlayer(message);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onConnectionFail() {
		// TODO Auto-generated method stub

	}

	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	public void onConnectOK() {
		// TODO Auto-generated method stub

	}

}
