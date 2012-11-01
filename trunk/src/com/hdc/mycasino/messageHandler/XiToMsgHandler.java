package com.hdc.mycasino.messageHandler;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.network.CMD;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.screen.XiToScr;

public class XiToMsgHandler implements IMessageHandler {

	static XiToMsgHandler m_instance = null;

	public static XiToMsgHandler gI() {
		if (m_instance == null) {
			m_instance = new XiToMsgHandler();
		}
		return m_instance;
	}

	public static void onHandler() {
		GlobalMsgHandler.gI().miniGameMessageHandler = XiToMsgHandler.gI();
	}

	public void onMessage(Message message) {
		String nick = "";
		try {
			switch (message.getCommand()) {
			case CMD.CMD_PLAYER_START_GAME:
//				XiToScr.gI().openCmd();
				int by = MessageIO.readByte(message);
				if (by == 0) {
					String info = MessageIO.readString(message);
//					GameCanvas.startOKDlg(info);
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",info);
					info = null;
				} else {
					XiToScr.gI().startGame(message);
				}
				break;

			case CMD.CMD_TO:
				boolean isSuccess = MessageIO.readBoolean(message);
				if (isSuccess) {
					long betGold = MessageIO.readLong(message);
					nick = MessageIO.readString(message);
					long maxGoldCanBet = MessageIO.readLong(message);
					XiToScr.gI().onTo(nick, betGold, maxGoldCanBet);
					nick = null;
				} else {
					String content = MessageIO.readString(message);
//					GameCanvas.startOKDlg(content);
					HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);					
					XiToScr.gI().setCommandTo();
					content = null;
				}
				break;

			case CMD.CMD_FINISH:
				XiToScr.gI().onRankUser(MessageIO.readString(message), MessageIO.readByte(message));
				break;

			case CMD.CMD_SKIP_TURN:
				String nickSkip = MessageIO.readString(message);
				XiToScr.gI().onNickSkip(nickSkip);
				nickSkip = null;
				break;

			case CMD.CMD_THEO:
				long betGold = MessageIO.readLong(message);
				nick = MessageIO.readString(message);
				XiToScr.gI().onTheo(betGold, nick);
				nick = null;
				break;
			case CMD.CMD_TO_THEM:
				nick = MessageIO.readString(message);
				long betGoldFinal = MessageIO.readLong(message);
				long totalBetGoldFinal = MessageIO.readLong(message);
				long maxGoldCanBet = MessageIO.readLong(message);
				XiToScr.gI().onToThem(nick, betGoldFinal, totalBetGoldFinal, maxGoldCanBet);
				nick = null;
				break;
			case CMD.CMD_GET_NEW_CARD:
				byte card = MessageIO.readByte(message);
				if (card == -1) {
//					GameCanvas.startOKDlg("Không thể rút");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Không thể rút");					
				} else {
					XiToScr.gI().onGetCardNoc(MessageIO.readString(message), card);
				}
				break;
			case CMD.CMD_VIEW_XITO:
				XiToScr.gI().displayJoinGame(message);
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
