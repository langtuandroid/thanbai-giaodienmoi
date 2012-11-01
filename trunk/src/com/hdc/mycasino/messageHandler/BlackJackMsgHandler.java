package com.hdc.mycasino.messageHandler;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.animation.FlyText;
import com.hdc.mycasino.network.CMD;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.screen.BlackJackScr;
import com.hdc.mycasino.screen.XiDachScr;

public class BlackJackMsgHandler implements IMessageHandler {

	static BlackJackMsgHandler m_instance = null;

	public static BlackJackMsgHandler gI() {
		if (m_instance == null) {
			m_instance = new BlackJackMsgHandler();
		}
		return m_instance;
	}

	public static void onHandler() {
		GlobalMsgHandler.gI().miniGameMessageHandler = BlackJackMsgHandler.gI();
	}

	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			switch (message.getCommand()) {
			case CMD.CMD_PLAYER_START_GAME:
//				BlackJackScr.getInstance().openCmd();
				int by = MessageIO.readByte(message);
				if (by == 0) {
//					GameCanvas.startOKDlg(MessageIO.readString(message));
					HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readString(message));
				} else {
					BlackJackScr.getInstance().startGame(message);
				}
				break;
			case CMD.CMD_GET_NEW_CARD:
				// tên người lấy card - nếu mình là người lấy card thì trả về
				// id (và card slit ben trai hay phai)
				// card cho người đó và số điểm tổng cộng
				String nick = MessageIO.readString(message);// doc ten
				byte card = 0;
				int pos = BlackJackScr.getInstance().findPlayerPos(nick);

				boolean isLeft = MessageIO.readBoolean(message);// doc trai phai
				card = MessageIO.readByte(message);// doc card

				BlackJackScr.getInstance().m_iScorePlayer = MessageIO.readByte(message);// doc
																						// diem
				if (pos >= 0)
					BlackJackScr.getInstance().initComand(isLeft);
				FlyText f = new FlyText();
				f.startEffect(XiDachScr.gI().m_iScorePlayer + "", XiDachScr.gI().m_arrPos[pos].x,
						XiDachScr.gI().m_arrPos[pos].y, 0xffff00, 0x000000);

				BlackJackScr.getInstance().getNewCard(nick, card, isLeft);
				nick = null;
				break;
			case CMD.CMD_UPDATE_CARD_NUMBER:
				nick = MessageIO.readString(message);
				isLeft = MessageIO.readBoolean(message);// doc trai phai
				if (!nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
					BlackJackScr.getInstance().getNewCard(nick, (byte) 0, isLeft);
				nick = null;
				break;
			case CMD.CMD_SEND_SPLIT:
				// nguoi nao do muong split card
				nick = MessageIO.readString(message);
				pos = BlackJackScr.getInstance().findPlayerPos(nick);
				if (pos >= 0) {
					BlackJackScr.getInstance().m_arrSplit[pos] = true;
					BlackJackScr.getInstance().effectSplitCard(pos);
				}
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
