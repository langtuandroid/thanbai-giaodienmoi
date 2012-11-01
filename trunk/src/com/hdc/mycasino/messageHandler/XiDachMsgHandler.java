package com.hdc.mycasino.messageHandler;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.animation.FlyText;
import com.hdc.mycasino.network.CMD;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.screen.XiDachScr;

public class XiDachMsgHandler implements IMessageHandler {

	static XiDachMsgHandler m_instance = null;

	public static XiDachMsgHandler gI() {
		if (m_instance == null) {
			m_instance = new XiDachMsgHandler();
		}
		return m_instance;
	}

	public static void onHandler() {
		GlobalMsgHandler.gI().miniGameMessageHandler = XiDachMsgHandler.gI();
	}

	public void onMessage(Message message) {
		try {
			switch (message.getCommand()) {
			case CMD.CMD_PLAYER_START_GAME:
//				XiDachScr.gI().openCmd();
				int by = MessageIO.readByte(message);
				if (by == 0) {
//					GameCanvas.startOKDlg(MessageIO.readString(message));
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",MessageIO.readString(message));
				} else {
					XiDachScr.gI().startGame(message);
					GameCanvas.endDlg();
				}
				break;
			case CMD.CMD_GET_NEW_CARD:
				// tên người lấy card - nếu mình là người lấy card thì trả về id
				// card
				// cho người đó và số điểm tổng cộng
				String nick = MessageIO.readString(message);
				byte card = MessageIO.readByte(message);
				XiDachScr.gI().m_iScorePlayer = MessageIO.readByte(message);

				int pos = XiDachScr.gI().findPlayerPos(nick);
				FlyText f = new FlyText();
				f.startEffect(XiDachScr.gI().m_iScorePlayer + "", XiDachScr.gI().m_arrPos[pos].x,
						XiDachScr.gI().m_arrPos[pos].y, 0xffff00, 0x000000);
				XiDachScr.gI().initComand();

				XiDachScr.gI().getNewCard(nick, card);
				nick = null;
				break;
			case CMD.CMD_UPDATE_CARD_NUMBER:
				nick = MessageIO.readString(message);
				if (!nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
					XiDachScr.gI().getNewCard(nick, (byte) 0);
				nick = null;
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
