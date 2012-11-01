package com.hdc.mycasino.messageHandler;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.network.CMD;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.screen.BaiCaoScr;

public class BaiCaoMsgHandler implements IMessageHandler {

	static BaiCaoMsgHandler m_instance = null;

	public static BaiCaoMsgHandler gI() {
		if (m_instance == null) {
			m_instance = new BaiCaoMsgHandler();
		}
		return m_instance;
	}

	public static void onHandler() {
		GlobalMsgHandler.gI().miniGameMessageHandler = BaiCaoMsgHandler.gI();
	}

	public void onMessage(Message message) {
		try {
			switch (message.getCommand()) {
			case CMD.CMD_PLAYER_START_GAME:
//				BaiCaoScr.gI().openCmd();
				int by = MessageIO.readByte(message);
				if (by == 0) {
					String info = MessageIO.readString(message);
//					GameCanvas.startOKDlg(info);
					HDCGameMidlet.instance.showDialog_Okie("Thông báo", info);
					info = null;
				} else {
					BaiCaoScr.gI().startGame(message);
				}
				break;
			case CMD.CMD_FIRE_CARD:
				String nick = MessageIO.readString(message);
				byte cardIndex = MessageIO.readByte(message);
				byte cardValue = MessageIO.readByte(message);
				BaiCaoScr.gI().openCard(nick, cardIndex, cardValue);
				nick = null;
				break;
			case CMD.CMD_OPEN_ALL_CARDS:
				nick = MessageIO.readString(message);
				int index,
				value;
				for (int i = 0; i < 3; i++) {
					index = MessageIO.readByte(message);
					value = MessageIO.readByte(message);
					BaiCaoScr.gI().openCard(nick, index, value);
				}
				nick = null;
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
