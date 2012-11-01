package com.hdc.mycasino.messageHandler;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.network.CMD;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.screen.PhomScr;

public class PhomMsgHandler implements IMessageHandler {

	static PhomMsgHandler m_instance = null;

	public static PhomMsgHandler gI() {
		if (m_instance == null) {
			m_instance = new PhomMsgHandler();
		}
		return m_instance;
	}

	public static void onHandler() {
		GlobalMsgHandler.gI().miniGameMessageHandler = PhomMsgHandler.gI();
	}

	public void onMessage(Message message) {
		try {
			switch (message.getCommand()) {
			case CMD.CMD_PLAYER_START_GAME:
//				PhomScr.gI().openCmd();
				int by = MessageIO.readByte(message);
				if (by == 0) {
					String info = MessageIO.readString(message);
//					GameCanvas.startOKDlg(info);
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",info);
					info = null;
				} else {
					PhomScr.gI().startGame(message);
				}
				break;

			case CMD.CMD_DROP_PHOM:
				byte status = MessageIO.readByte(message);
				if (status == 0) {
//					PhomScr.gI().openCmd();
//					GameCanvas.startOKDlg(MessageIO.readString(message));
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",MessageIO.readString(message));
				} else {
					String nn = MessageIO.readString(message);
					byte arry[] = MessageIO.readBytes(message);
					int cdp[] = new int[arry.length];
					for (int i = 0; i < arry.length; i++) {
						cdp[i] = arry[i];
					}
					PhomScr.gI().onDropPhomSuccess(nn, cdp);
					nn = null;
					arry = null;
					cdp = null;
				}
				break;

			case CMD.CMD_EAT_CARD:
//				PhomScr.gI().openCmd();
				status = MessageIO.readByte(message);
				if (status == -1) {
//					GameCanvas.startOKDlg("Không thể ăn");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo","Không thể ăn");
				} else {
					PhomScr.gI().onEatCardSuccess(MessageIO.readString(message),
							MessageIO.readString(message), status);
				}
				break;

			case CMD.CMD_BALANCE:
				status = MessageIO.readByte(message);
				String from = MessageIO.readString(message);
				String to = MessageIO.readString(message);
				PhomScr.gI().onBalanceCard(from, to, status);
				from = null;
				to = null;
				break;

			case CMD.CMD_FIRE_CARD:
				status = MessageIO.readByte(message);
				if (status == -1) {
//					PhomScr.gI().openCmd();
//					GameCanvas.startOKDlg(MessageIO.readString(message));
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",MessageIO.readString(message));
				} else {
					from = MessageIO.readString(message);
					PhomScr.gI().onFireCard(from, new int[] { status });
					from = null;
				}
				break;

			case CMD.CMD_GET_NEW_CARD:
//				PhomScr.gI().openCmd();
				status = MessageIO.readByte(message);
				if (status == -1) {
//					GameCanvas.startOKDlg("Bạn không thể bốc bài được!");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo","Bạn không thể bốc bài được!");
				} else {
					from = MessageIO.readString(message);
					PhomScr.gI().onGetCardNocSuccess(from, status);
					from = null;
				}
				break;

			case CMD.CMD_MOM:
				from = MessageIO.readString(message);
				PhomScr.gI().onInfoMom(from);
				from = null;
				break;

			case CMD.CMD_ATTACH_CARD:
				PhomScr.gI().onAttachCard(message);
				break;

			case CMD.CMD_U:
				MessageIO.readByte(message);
				PhomScr.gI().onInfoU(MessageIO.readString(message));
				break;
			case CMD.CMD_VIEW_PHOM:
				PhomScr.gI().displayAllCardJoinGame(message);
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
