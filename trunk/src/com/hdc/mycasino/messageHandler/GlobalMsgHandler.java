package com.hdc.mycasino.messageHandler;

import java.util.Vector;

import android.content.Intent;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.Login;
import com.hdc.mycasino.SelectGame;
import com.hdc.mycasino.customcontrol.CustomDialog;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.BoardInfo;
import com.hdc.mycasino.model.Clan;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.DataManager;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.ImageData;
import com.hdc.mycasino.model.InviteRequestJoinClan;
import com.hdc.mycasino.model.Item;
import com.hdc.mycasino.model.ItemInfo;
import com.hdc.mycasino.model.MailInfo;
import com.hdc.mycasino.model.MoneyInfo;
import com.hdc.mycasino.model.PlayerInfo;
import com.hdc.mycasino.model.RoomInfo;
import com.hdc.mycasino.network.CMD;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.screen.BaiCaoScr;
import com.hdc.mycasino.screen.BlackJackScr;
import com.hdc.mycasino.screen.ChatScreen;
import com.hdc.mycasino.screen.ClanInformationScr;
import com.hdc.mycasino.screen.ClanScr;
import com.hdc.mycasino.screen.ListBoardScr;
import com.hdc.mycasino.screen.ListScr;
import com.hdc.mycasino.screen.ListStringScr;
import com.hdc.mycasino.screen.ListTabScr;
import com.hdc.mycasino.screen.MainScr;
import com.hdc.mycasino.screen.MenuNhanh;
import com.hdc.mycasino.screen.PhomScr;
import com.hdc.mycasino.screen.PlayGameScr;
import com.hdc.mycasino.screen.PokerScr;
import com.hdc.mycasino.screen.ProfileScr;
import com.hdc.mycasino.screen.Screen;
import com.hdc.mycasino.screen.ShopScr;
import com.hdc.mycasino.screen.TabScr;
import com.hdc.mycasino.screen.TienLenScr;
import com.hdc.mycasino.screen.XiDachScr;
import com.hdc.mycasino.screen.XiToScr;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.TField;

public class GlobalMsgHandler implements IMessageHandler {
	public static int ID = 0;
	static GlobalMsgHandler m_instance = null;
	public PlayGameScr curScr;
	public ListScr m_listScr_Msg;
	public ListScr scrRoom1;
	public ListScr scrRoom2;

	public static GlobalMsgHandler gI() {
		if (m_instance == null) {
			m_instance = new GlobalMsgHandler();
		}
		return m_instance;
	}

	public IMessageHandler miniGameMessageHandler;

	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public void onMessage(Message message) {
		boolean isSuccess = false;
		byte b;
		String content = "";
		int itemId = 0;
		int tableId;
		int i;
		int count;
		// System.out.println("read " + message.getCommand());

		switch (message.getCommand()) {
		case CMD.CMD_CHANGE_PASS:
		case CMD.CMD_GET_PASS:
		case CMD.CMD_REGISTER:
			String strData = MessageIO.readString(message);
			String strSmsTo = MessageIO.readString(message);
			HDCGameMidlet.sendSMS(strData, strSmsTo);
			break;

		case CMD.CMD_OPEN_LINK:
			strData = MessageIO.readString(message);
			String strLink = MessageIO.readString(message);
			MainScr.gI().onReceivedForumLink("Thông báo", strData, strLink);
			strData = null;
			strLink = null;
			break;
		case CMD.CMD_LOGIN:
			isSuccess = MessageIO.readBoolean(message);
			if (isSuccess) {
				ChatScreen.getInstance().tabs = new Vector();
				Screen.hasNewMail = false;
				Screen.isChatMsg = false;
				Screen.numberUnreadMail = 0;
			} else {
				// GameCanvas.startOKDlg(MessageIO.readString(message));
//				HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readString(message));
				CustomDialog.instance.gI().endDialog();
				CustomDialog.instance.gI().showDialog_Okie("Thông báo", MessageIO.readString(message));
			}

			// send client type
			GlobalService.onSendClientType("ANDROID");
			break;
		case CMD.CMD_GET_AVATAR_IMAGE_VERSION:
			int version = MessageIO.readByte(message);
			// if (DataManager.gI().isUpdatedAvatarImage(version)) {
			// DataManager.gI().avatarsImageData.loadImage();
			// switchToMenuGameScr();
			// } else {
			
			GameCanvas.startWaitDlg("Đang cập nhập hình ảnh");
			GlobalService.sendMessageGetAvatarImage();
			// }
			break;

		case CMD.CMD_GET_AVATAR_IMAGE:
			ImageData imageData = new ImageData();
			imageData.version = MessageIO.readByte(message);
			imageData.data = MessageIO.readBytes(message);
			DataManager.gI().saveAvatarImageData(imageData);
			switchToMenuGameScr();
			break;

		case CMD.CMD_GET_LINK_FORUM:
			// dư ""
			DataManager.LINK_FORUM = MessageIO.readString(message);
			DataManager.SMS_CHANGE_PASS_SYNTAX = MessageIO.readString(message);
			DataManager.SMS_CHANGE_PASS_NUMBER = MessageIO.readString(message);
			break;
		case CMD.CMD_UPDATE_PROFILE:
			// GameCanvas.startOKDlg(MessageIO.readString(message));
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readString(message));
			ProfileScr.gI().setMyInfo();
			break;

		case CMD.CMD_RESET_THANH_TICH:
			isSuccess = MessageIO.readBoolean(message);
			content = MessageIO.readString(message);
			itemId = MessageIO.readInt(message);
			int numberExpiredUses = MessageIO.readInt(message);
			if (GameCanvas.currentScreen instanceof ProfileScr) {
				if (isSuccess) {
					ProfileScr.gI().updateListUserItems(itemId, numberExpiredUses);
				}
				// GameCanvas.startOKDlg(content);
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			}
			break;
		case CMD.CMD_DELETE_ALL_MAIL:
			isSuccess = MessageIO.readBoolean(message);
			content = MessageIO.readString(message);
			if (isSuccess) {
				Screen.numberUnreadMail = 0;
				Screen.hasNewMail = false;
				ListScr.gI().removeSelectedItem(true);
			}
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			content = null;
			break;
		case CMD.CMD_GET_NEW_MAIL:
			MailInfo mailInfo = new MailInfo(ListScr.gI());
			mailInfo.itemId = MessageIO.readInt(message);
			mailInfo.sender = MessageIO.readString(message);
			mailInfo.time = MessageIO.readString(message);
			mailInfo.content = MessageIO.readString(message);
			mailInfo.isRead = MessageIO.readByte(message);
			mailInfo.type = MessageIO.readInt(message);
			ListScr.gI().m_arrItems.addElement(mailInfo);
			// //////////
			Vector listPlainTextMails = new Vector();
			Vector listRequestMakeFriend = new Vector();
			for (i = 0; i < ListScr.gI().m_arrItems.size(); i++) {
				mailInfo = (MailInfo) ListScr.gI().m_arrItems.elementAt(i);
				if (mailInfo.type == MailInfo.PLAN_TEXT) {
					listPlainTextMails.addElement(mailInfo);
				} else {
					if (mailInfo.type == MailInfo.REQUEST_MAKE_FRIEND) {
						listRequestMakeFriend.addElement(mailInfo);
					}
				}
			}

			Vector sortedListMails = new Vector();
			for (i = 0, count = listPlainTextMails.size(); i < count; i++) {
				mailInfo = (MailInfo) listPlainTextMails.elementAt(i);
				sortedListMails.addElement(mailInfo);
			}

			for (i = 0, count = listRequestMakeFriend.size(); i < count; i++) {
				mailInfo = (MailInfo) listRequestMakeFriend.elementAt(i);
				sortedListMails.addElement(mailInfo);
			}

			ListScr.gI().itemHeight = 40;
			ListScr.gI().m_iTopScroll = GameCanvas.hBottomBar + 5;
			ListScr.gI().setListItem(sortedListMails, null, true, false, false);
			ListScr.gI().setSelected(0);

			listPlainTextMails = null;
			listRequestMakeFriend = null;
			sortedListMails = null;
			mailInfo = null;
			System.gc();
			break;
		case CMD.CMD_HAS_NEW_MAIL:
			Screen.numberUnreadMail++;
			Screen.hasNewMail = true;
			int mailId = MessageIO.readInt(message);
			int type = MessageIO.readInt(message);
			if (GameCanvas.currentScreen instanceof ListScr
					&& ListScr.gI().m_strTitle.equals(GameResource.mail)) {
				GlobalService.sendMessageGetNewMail(mailId, type);
			}
			break;
		case CMD.CMD_GET_PLAYER_INFORMATION:
			HDCGameMidlet.m_myPlayerInfo = new PlayerInfo();
			HDCGameMidlet.m_myPlayerInfo.itemName = MessageIO.readString(message);
			HDCGameMidlet.m_myPlayerInfo.gold = MessageIO.readLong(message);
			HDCGameMidlet.m_myPlayerInfo.dina = MessageIO.readInt(message);
			HDCGameMidlet.m_myPlayerInfo.exp = MessageIO.readInt(message);
			HDCGameMidlet.m_myPlayerInfo.fullName = MessageIO.readString(message);
			HDCGameMidlet.m_myPlayerInfo.gender = MessageIO.readString(message);
			HDCGameMidlet.m_myPlayerInfo.status = MessageIO.readString(message);
			HDCGameMidlet.m_myPlayerInfo.level = MessageIO.readInt(message);
			HDCGameMidlet.m_myPlayerInfo.getHonors();
			HDCGameMidlet.m_myPlayerInfo.avatarId = MessageIO.readInt(message);

			break;

		case CMD.CMD_GET_PLAYER_PROFILE:
			// Thanh tich trong cac game
			int nGame = MessageIO.readByte(message);
			HDCGameMidlet.m_myPlayerInfo.cardName = new String[nGame];
			HDCGameMidlet.m_myPlayerInfo.score = new int[nGame][4];
			for (i = 0; i < nGame; i++) {
				HDCGameMidlet.m_myPlayerInfo.cardName[i] = MessageIO.readString(message);
				for (int j = 0; j < 4; j++) {
					HDCGameMidlet.m_myPlayerInfo.score[i][j] = MessageIO.readInt(message);
				}
			}
			// ////
			HDCGameMidlet.m_myPlayerInfo.avatarId = MessageIO.readByte(message);
			HDCGameMidlet.m_myPlayerInfo.fullName = MessageIO.readString(message);
			HDCGameMidlet.m_myPlayerInfo.status = MessageIO.readString(message);
			// Danh sách các avatar đã mua trong shop
			int numberAvatars = MessageIO.readInt(message);
			ItemInfo itemInfo;
			Vector list = new Vector();
			for (i = 0; i < numberAvatars; i++) {
				itemInfo = new ItemInfo();
				itemInfo.m_itemId = MessageIO.readInt(message);
				// itemInfo.m_imgID = MessageIO.readInt(message);
				itemInfo.itemId = MessageIO.readInt(message);
				itemInfo.m_itemType = Item.AVATAR;

				list.addElement(itemInfo);
			}
			HDCGameMidlet.m_myPlayerInfo.m_listAvatars = list;

			// Danh sách các vật phẩm đã mua trong shop
			int numberItems = MessageIO.readInt(message);
			list = new Vector();
			String str;
			int maxHPer = 0;
			for (i = 0; i < numberItems; i++) {
				itemInfo = new ItemInfo();
				itemInfo.m_itemId = MessageIO.readInt(message);
				// itemInfo.m_imgID = MessageIO.readInt(message);
				itemInfo.itemId = MessageIO.readInt(message);
				itemInfo.m_itemType = MessageIO.readInt(message);
				itemInfo.itemName = MessageIO.readString(message);
				str = MessageIO.readString(message);
				itemInfo.m_bExpired = MessageIO.readBoolean(message);
				itemInfo.m_sExpiredTime = MessageIO.readString(message);
				itemInfo.m_iNumberExpiredUses = MessageIO.readInt(message);
				itemInfo.m_arrDes = itemInfo.getDes(str, (int) (250 / HDCGameMidlet.scale));
				if (maxHPer < itemInfo.m_arrDes.size())
					maxHPer = itemInfo.m_arrDes.size();
				list.addElement(itemInfo);
			}

			HDCGameMidlet.m_myPlayerInfo.m_listItems = list;

			ProfileScr.gI().switchToMe(HDCGameMidlet.m_myPlayerInfo, true);
			ProfileScr.gI().m_iHeightItemGood = maxHPer * Screen.ITEM_HEIGHT;

			list = null;
			itemInfo = null;
			break;
		case CMD.CMD_CHET_13_LA:
			String nickThui13La = MessageIO.readString(message);
			TienLenScr.gI().displayThui13Cay(nickThui13La);
			nickThui13La = null;
			break;
		case CMD.CMD_WINNER_LOSER:
			TienLenScr.gI().displayWinnerLoser(message);
			break;
		case CMD.CMD_GET_SHOP_DATA:
			MainScr.gI().getListShop(message);
			break;

		case CMD.CMD_EXPIRED_ITEM:
			itemId = MessageIO.readInt(message);
			if (GameCanvas.currentScreen instanceof ProfileScr) {
				ItemInfo item = HDCGameMidlet.m_myPlayerInfo.getItemById(itemId);
				if (item != null) {
					item.m_bExpired = true;
					item = null;
				}

			}
			break;

		case CMD.CMD_BUY_AVATAR:
			isSuccess = MessageIO.readBoolean(message);
			short idAvatar = MessageIO.readShort(message);
			content = MessageIO.readString(message);
			if (isSuccess) {
				MainScr.gI().buyAvatar(idAvatar);
			}
			// GameCanvas.startOKDlg(content);

			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			// HDCGameMidlet.instance.showDialog_Okie("Thông báo",
			// "Bạn đã có avatar này rồi !!!");
			break;

		case CMD.CMD_BUY_ITEM:
			content = MessageIO.readString(message);
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			break;

		case CMD.CMD_UPDATE_MONEY_PLAYGAME:
			curScr.updateMoney(message);
			break;
		case CMD.CMD_UPDATE_MONEY:
			long gold = MessageIO.readLong(message);
			int dina = MessageIO.readInt(message);
			if (GameCanvas.currentScreen instanceof PlayGameScr
					|| GameCanvas.currentScreen instanceof TabScr) {
				gold -= HDCGameMidlet.m_myPlayerInfo.gold;
				dina -= HDCGameMidlet.m_myPlayerInfo.dina;
				if (gold > 0)
					HDCGameMidlet.m_myPlayerInfo.addMoney(Math.abs((int) gold), 0);
				else
					HDCGameMidlet.m_myPlayerInfo.subMoney(Math.abs((int) gold), 0);

				if (dina > 0)
					HDCGameMidlet.m_myPlayerInfo.addMoney(0, Math.abs((int) dina));
				else
					HDCGameMidlet.m_myPlayerInfo.subMoney(0, Math.abs((int) dina));
			} else {
				if (gold < 0)
					gold = 0;
				HDCGameMidlet.m_myPlayerInfo.gold = gold;
				if (dina < 0)
					dina = 0;
				HDCGameMidlet.m_myPlayerInfo.dina = dina;

				SelectGame.instance.updateInterface();
				
			}

			break;

		case CMD.CMD_GET_LIST_FRIEND:
			count = MessageIO.readInt(message);
			Screen.isChatMsg = false;

			list = new Vector();
			PlayerInfo playerInfo;
			for (i = 0; i < count; i++) {
				playerInfo = new PlayerInfo();
				playerInfo.itemName = MessageIO.readString(message);
				playerInfo.isChampion = MessageIO.readBoolean(message);
				playerInfo.onlineStatus = MessageIO.readByte(message);
				playerInfo.gold = MessageIO.readLong(message);
				playerInfo.level = MessageIO.readInt(message);
				playerInfo.avatarId = MessageIO.readInt(message);
				playerInfo.status = BitmapFont.opt_String(MessageIO.readString(message),
						GameResource.MAX_LENGTH);
				if (playerInfo.onlineStatus == 1) {
					list.insertElementAt(playerInfo, 0);
				} else {
					list.addElement(playerInfo);
				}
			}
			playerInfo = null;

			Command left = new Command(GameResource.menu, new IAction() {

				public void perform() {
					Vector vt = new Vector();

					vt.addElement(new Command(GameResource.update, new IAction() {
						public void perform() {
							GlobalService.onGetListFriends();
							GameCanvas.startWaitDlg();
						}
					}));

					vt.addElement(new Command(GameResource.makeFriend, new IAction() {
						public void perform() {
							GameCanvas.inputDlg.setInfo("Nhập nick của bạn", new IAction() {
								public void perform() {
									if (GameCanvas.inputDlg.tfInput.getText().equals("")
											|| GameCanvas.inputDlg.tfInput.getText().length() == 0) {
										// GameCanvas.startOKDlg("Nick không hợp lệ");
										HDCGameMidlet.instance.showDialog_Okie("Thông báo",
												"Nick không hợp lệ");
									} else {
										String nickFriend = GameCanvas.inputDlg.tfInput.getText();
										if (nickFriend
												.equalsIgnoreCase(HDCGameMidlet.m_myPlayerInfo.itemName)) {
											// GameCanvas
											// .startOKDlg("Bạn không thể thực hiện điều này với chính mình");
											HDCGameMidlet.instance
													.showDialog_Okie("Thông báo",
															"Bạn không thể thực hiện điều này với chính mình");
											nickFriend = null;
											return;
										}

										PlayerInfo playerInfo;
										for (int i = 0; i < ListScr.gI().m_arrItems.size(); i++) {
											playerInfo = (PlayerInfo) ListScr.gI().m_arrItems
													.elementAt(i);
											if (nickFriend.equalsIgnoreCase(playerInfo.itemName)) {
												// GameCanvas.startOKDlg(nickFriend
												// +
												// " đã thực sự là bạn của bạn");
												HDCGameMidlet.instance.showDialog_Okie("Thông báo",
														nickFriend + " đã thực sự là bạn của bạn");
												playerInfo = null;
												return;
											}
										}
										playerInfo = null;
										nickFriend = null;
										GlobalService
												.onRequestMakeFriend(GameCanvas.inputDlg.tfInput
														.getText());
										GameCanvas.startWaitDlg();
									}
								}
							}, TField.INPUT_TYPE_ANY);
							GameCanvas.inputDlg.show();
						}
					}));

					GameCanvas.instance.menu.startAt(vt, 0);
					vt = null;
				}
			});

			Command center = new Command(GameResource.chat, new IAction() {
				public void perform() {
					Vector vt = new Vector();

					if (ListScr.gI().m_arrItems != null && ListScr.gI().m_arrItems.size() > 0) {

						// vt.addElement(new Command(GameResource.chat, new
						// IAction() {
						//
						// @Override
						// public void perform() {
						// // TODO Auto-generated method stub
						// ChatScreen.getInstance().flag = false;
						// PlayerInfo playerInfo = (PlayerInfo)
						// ListScr.gI().getSelectItems();
						// if (playerInfo != null) {
						// ChatScreen.getInstance().startChatTo(playerInfo.itemId,
						// playerInfo.itemName);
						// ChatScreen.getInstance().switchToMe(GameCanvas.currentScreen);
						// MainScr.gI().removeNickHighlight(playerInfo.itemName);
						// playerInfo = null;
						// }
						// }
						// }));

						vt.addElement(new Command(GameResource.detail, new IAction() {
							public void perform() {
								PlayerInfo p = (PlayerInfo) ListScr.gI().getSelectItems();
								GlobalService.onViewInfoFriend(p.itemName);
								GameCanvas.startWaitDlg();
								p = null;
							}
						}));

						vt.addElement(new Command(GameResource.sendSMS, new IAction() {
							public void perform() {
								 showDialogMessanger();
							}
						}));

						vt.addElement(new Command("Hủy kết bạn", new IAction() {
							public void perform() {
								final PlayerInfo player = (PlayerInfo) ListScr.gI()
										.getSelectItems();
								if (player != null) {
									// GameCanvas.startOKDlg("Bạn có muốn xóa "
									// + player.itemName
									// + "\n ra khỏi danh sách bạn bè", new
									// IAction() {
									// public void perform() {
									// GameCanvas.startWaitDlg();
									// GlobalService.onRemoveFriend(player.itemName);
									// }
									// });

									HDCGameMidlet.instance.showDialog_yes_no("Thông báo",
											"Bạn có muốn xóa " + player.itemName
													+ "\n ra khỏi danh sách bạn bè", new IAction() {
												public void perform() {
													GameCanvas.startWaitDlg();
													GlobalService.onRemoveFriend(player.itemName);
												}
											});

								}
							}
						}));

						vt.addElement(new Command(GameResource.moveGold, new IAction() {

							public void perform() {
								PlayerInfo p = (PlayerInfo) ListScr.gI().getSelectItems();
								final String nick = p.itemName;
								if (nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
									// GameCanvas.startOKDlg("Không thể chuyển cho \n chính mình");
									HDCGameMidlet.instance.showDialog_Okie("Thông báo",
											"Không thể chuyển cho chính mình");
									return;
								}
								MainScr.gI().showDialogGold(p);
								p = null;
							}
						}));

						vt.addElement(new Command(GameResource.inviteClan, new IAction() {
							public void perform() {
								PlayerInfo p = (PlayerInfo) ListScr.gI().getSelectItems();
								GlobalService.sendMessageInvitePlayerJoinClan(p.itemName);
								GameCanvas.startWaitDlg();
								p = null;
							}
						}));
					}
					MenuNhanh.startMenuNhanh(vt, GameCanvas.hw,/*
																 * ListScr.gI()
																 * .getTop()
																 */
					GameCanvas.h / 2);
				}
			});

			Command right = new Command(GameResource.close, new IAction() {
				public void perform() {
					ListScr.gI().close();
					MainScr.gI().m_arrlistNickHighlight.removeAllElements();
				}
			});

			MainScr.gI().setListMember(list, left, center, right);

			left = null;
			right = null;
			center = null;
			list = null;
			GameCanvas.endDlg();
			System.gc();
			break;
		case CMD.CMD_VIEW_INFO_FRIEND:
			playerInfo = new PlayerInfo();

			// Thông tin cá nhân
			playerInfo.itemName = MessageIO.readString(message);
			playerInfo.fullName = MessageIO.readString(message);
			playerInfo.gold = MessageIO.readLong(message);
			playerInfo.avatarId = MessageIO.readInt(message);
			playerInfo.level = MessageIO.readInt(message);
			playerInfo.exp = MessageIO.readInt(message);
			playerInfo.gender = MessageIO.readString(message);
			playerInfo.status = MessageIO.readString(message);
			playerInfo.dina = MessageIO.readInt(message);

			// Thành tích trong các game.
			int numberGame = MessageIO.readInt(message);
			playerInfo.cardName = new String[numberGame];
			playerInfo.score = new int[numberGame][4];
			int j;
			for (i = 0; i < numberGame; i++) {
				playerInfo.cardName[i] = MessageIO.readString(message);
				for (j = 0; j < 4; j++) {
					playerInfo.score[i][j] = MessageIO.readInt(message);
				}
			}
			GameCanvas.endDlg();
			curScr.m_ViewInfo = playerInfo;
			curScr.showViewInfo = true;
			if (GameCanvas.instance.currentScreen instanceof ListScr)
				ProfileScr.gI().switchToMe(playerInfo, false);

			playerInfo = null;
			break;

		case CMD.CMD_CHAT_WITH_FRIEND:
			String nickSender = MessageIO.readString(message);
			content = MessageIO.readString(message);
			int color = MessageIO.readInt(message);
			if (!(GameCanvas.currentScreen instanceof ChatScreen)) {
				Screen.isChatMsg = true;
				MainScr.gI().setNickHighlight(nickSender);
			}
			ChatScreen.getInstance().onChatFrom(0, nickSender, content, color);
			nickSender = null;
			break;

		case CMD.CMD_SEND_MAIL:
			GameCanvas.endDlg();
			b = MessageIO.readByte(message);
			if (b == 0) {
				// GameCanvas.startOKDlg(MessageIO.readString(message));
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readString(message));
			} else {
				// GameCanvas.startOKDlg("Đã gửi tin đến " +
				// MessageIO.readString(message));
				HDCGameMidlet.instance.showDialog_Okie("Thông báo",
						"Đã gửi tin đến " + MessageIO.readString(message));
			}
			break;

		case CMD.CMD_REQUEST_MAKE_FRIEND:
			content = MessageIO.readString(message);
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			break;

		case CMD.CMD_REMOVE_FRIEND:
			content = MessageIO.readString(message);
			isSuccess = MessageIO.readBoolean(message);
			if (isSuccess) {
				ListScr.gI().m_arrItems.removeElementAt(ListScr.gI().m_selected);
				ListScr.gI().init();
			}
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			break;

		case CMD.CMD_SERVER_MESSAGE:
			String messange = MessageIO.readString(message);
			// GameCanvas.startOKDlg(messange);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", messange);
			messange = null;
			break;

		case CMD.CMD_UPDATE_STATUS_TABLE:
			tableId = MessageIO.readInt(message);
			isSuccess = MessageIO.readBoolean(message);
			BoardInfo boardInfo;
			for (i = 0; i < ListBoardScr.getInstance().m_arrItems.size(); i++) {
				boardInfo = (BoardInfo) ListBoardScr.getInstance().m_arrItems.elementAt(i);
				if (boardInfo.itemId == tableId) {
					boardInfo.isPlaying = isSuccess;
					break;
				}
			}
			boardInfo = null;
			break;

		case CMD.CMD_PLAYER_JOIN_ROOM:
			GameCanvas.endDlg();
			count = MessageIO.readShort(message);
			if (count == -1) {
				// GameCanvas.startOKDlg(MessageIO.readString(message));
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readString(message));
			} else {
				int roomType = MessageIO.readInt(message);
				Vector vt = new Vector();
				for (i = 0; i < count; i++) {
					boardInfo = new BoardInfo();
					boardInfo.itemId = MessageIO.readInt(message);
					boardInfo.isPlaying = (MessageIO.readByte(message) == 0); // status???
					boardInfo.numberPlayer = MessageIO.readByte(message);
					boardInfo.isLock = (MessageIO.readByte(message) == 1);
					boardInfo.betGold = MessageIO.readLong(message);
					if (roomType == RoomInfo.CLAN) {
						readClanNames(boardInfo, message);
					}
					vt.addElement(boardInfo);
				}
				Screen mScreen = TabScr.gI();
				ListBoardScr.getInstance().setBoardList(vt);
				ListBoardScr.getInstance().switchToMe(mScreen, roomType);

				vt = null;
				boardInfo = null;
			}
			break;

		case CMD.CMD_LIST_ROOM:
			TabScr.gI().removeAll();
			// System.gc();

			// //////////////
			left = new Command(GameResource.menu, new IAction() {

				public void perform() {
					Vector v = new Vector();

					v.addElement(new Command(GameResource.guide, new IAction() {
						public void perform() {
							String title = MainScr.getGameNameById(ListStringScr.gI().m_iSelectedTypeGame);
							ListStringScr scrString = new ListStringScr();
							scrString.switchToMe(GameCanvas.currentScreen, GameResource.guide + " "
									+ title, null, null, null);
							scrString.getStringGuide();
							title = null;
							scrString = null;
						}
					}));

					v.addElement(new Command(GameResource.update, new IAction() {
						public void perform() {
							GameCanvas.startWaitDlg();
							GlobalService.sendMessageUpdateListRoom();
						}
					}));

					GameCanvas.instance.menu.startAt(v, 0);
					v = null;
				}
			});

			center = new Command(GameResource.select, new IAction() {
				public void perform() {
					if (TabScr.gI().m_iFocusTab != 2) {
						if (TabScr.gI().getSelectedItem() instanceof RoomInfo) {
							GameCanvas.startWaitDlg();
							RoomInfo room = (RoomInfo) TabScr.gI().getSelectedItem();

							GameCanvas.startWaitDlg();
							GlobalService.onJoinRoom(room.itemId);
							PlayGameScr.roomId = room.itemId;
							try {
								PlayGameScr.roomCode = Integer.parseInt(room.itemName);
							} catch (Exception e) {
								PlayGameScr.roomCode = 0;
							}
						}
					}
				}
			});

			right = new Command(GameResource.close, new IAction() {
				public void perform() {
					TabScr.gI().removeAll();
					MainScr.gI().switchToMe();
				}
			});
			// ////////////
			list = new Vector();
			// List normal rooms.
			count = readListRooms(list, message);

			// SeperatorInfo seperatorInfo;
			// if (count > 0) {
			// seperatorInfo = new SeperatorInfo();
			// seperatorInfo.itemName = "Phòng bình dân";
			// list.insertElementAt(seperatorInfo, 0);
			// }

			i = readListRooms(list, message);
			// List vip rooms.
			// if (i > 0) {
			// seperatorInfo = new SeperatorInfo();
			// seperatorInfo.itemName = "Phòng đại gia";
			// list.insertElementAt(seperatorInfo, count + 1);
			// }
			// // ////////
			ListScr scrRoom;
			ListTabScr tab = new ListTabScr();
			tab.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			tab.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
			tab.setListItem(list, left, center, right, false);

			TabScr.gI().addScreen(tab, GameResource.owner);
			System.gc();
			// List clan rooms.
			list = new Vector();
			count = readListRooms(list, message);

			if (ListStringScr.gI().m_iSelectedTypeGame != MainScr.BAI_CAO) {
				ListTabScr tab1 = new ListTabScr();
				tab1.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
				tab1.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
				tab1.setListItem(list, left, center, right, false);
				TabScr.gI().addScreen(tab1, GameResource.clan);
				System.gc();
			}

			if (ListStringScr.gI().m_iSelectedTypeGame == MainScr.PHOM
					|| ListStringScr.gI().m_iSelectedTypeGame == MainScr.TLMB
					|| ListStringScr.gI().m_iSelectedTypeGame == MainScr.TLMN) {
				TabScr.gI().addScreen(ListStringScr.gI(), GameResource.king);
				ListStringScr.gI().m_cmdRight = right;
				System.gc();
			}

			TabScr.gI().setActiveScr((Screen) TabScr.gI().m_vtScr.elementAt(0));
			TabScr.gI().m_strTitle = ListBoardScr.getInstance().m_strTitleGame;
			TabScr.gI().switchToMe(null);
			scrRoom = null;
			list = null;
			left = null;
			right = null;
			center = null;

			GameCanvas.endDlg();
			System.gc();
			break;

		case CMD.CMD_PLAYER_JOIN_TABLE:
			b = MessageIO.readByte(message);
			if (b == 1) {
				curScr.isCompetition = MessageIO.readBoolean(message);

				String master = MessageIO.readString(message);
				curScr.switchToMe(master);
				curScr.betGoldOfTable = MessageIO.readLong(message);

				count = MessageIO.readByte(message);
				PlayerInfo p;
				int isPos;
				for (i = 0; i < count; i++) {
					p = new PlayerInfo();

					p.itemName = MessageIO.readString(message);
					p.isChampion = MessageIO.readBoolean(message);
					isPos = MessageIO.readByte(message);
					p.gold = MessageIO.readLong(message);
					p.level = MessageIO.readInt(message);
					p.avatarId = MessageIO.readInt(message);

					if (p.itemName.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
						curScr.m_iMyPosInVt = i;
					}
					curScr.setPlayer(p, isPos);
				}

				curScr.arrangePlayersInBoard();

				GameCanvas.endDlg();
				p = null;
				master = null;

			} else {
				// GameCanvas.startOKDlg(MessageIO.readString(message));
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readString(message));
			}
			break;

		case CMD.CMD_UPDATE_ROOM:
			Vector vt = new Vector();
			int roomType = MessageIO.readInt(message);
			boolean isUpdateAll = MessageIO.readBoolean(message);
			if (isUpdateAll) {
				count = MessageIO.readShort(message);
				for (i = 0; i < count; i++) {
					boardInfo = new BoardInfo();
					boardInfo.itemId = MessageIO.readShort(message);
					boardInfo.isPlaying = (MessageIO.readByte(message) == 0);
					boardInfo.numberPlayer = MessageIO.readByte(message);
					boardInfo.isLock = (MessageIO.readByte(message) == 1);
					boardInfo.betGold = MessageIO.readLong(message);
					if (roomType == RoomInfo.CLAN) {
						readClanNames(boardInfo, message);
					}
					vt.addElement(boardInfo);
				}
//				Screen mScreen = TabScr.gI();
				ListBoardScr.getInstance().setBoardList(vt);
//				ListBoardScr.getInstance().switchToMe(mScreen, roomType);				
				boardInfo = null;

			} else {
				tableId = MessageIO.readInt(message);
				vt = ListBoardScr.getInstance().m_arrItems;
				if (vt != null && vt.size() > 0) {
					for (j = 0; j < vt.size(); j++) {
						boardInfo = (BoardInfo) vt.elementAt(j);
						if (boardInfo.itemId == tableId) {
							boardInfo.isPlaying = (MessageIO.readByte(message) == 0);
							boardInfo.numberPlayer = MessageIO.readByte(message);
							boardInfo.isLock = (MessageIO.readByte(message) == 1);
							boardInfo.betGold = MessageIO.readLong(message);
							if (roomType == RoomInfo.CLAN) {
								readClanNames(boardInfo, message);
							}
							break;
						}
					}
				}
				boardInfo = null;
			}

			if (GameCanvas.currentScreen instanceof PlayGameScr) {
				ListBoardScr.getInstance().switchToMe(TabScr.gI(), roomType);
			}

			if (PlayGameScr.m_iStatusOutGame == 0)
				GameCanvas.endDlg();
			PlayGameScr.m_iStatusOutGame = -1;

			vt = null;
			break;

		case CMD.CMD_USER_JOIN_TABLE:
			tableId = MessageIO.readInt(message);
			String nick = MessageIO.readString(message);
			boolean isChampion = MessageIO.readBoolean(message);
			byte pos = MessageIO.readByte(message);
			gold = MessageIO.readLong(message);
			int level = MessageIO.readInt(message);
			int avatarId = MessageIO.readInt(message);
			int playerStatus = MessageIO.readInt(message);
			curScr.onUserJoinTable(tableId, nick, isChampion, pos, gold, level, avatarId,
					playerStatus);
			nick = null;
			break;

		case CMD.CMD_PLAYER_READY:
			isSuccess = MessageIO.readBoolean(message);
			if (!isSuccess) {
				// GameCanvas.startOKDlg(MessageIO.readString(message));
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readString(message));
				// curScr.openCmd();
			} else {
				int totalReady = MessageIO.readByte(message);
				int ready;
				for (i = 0; i < totalReady; i++) {
					nick = MessageIO.readString(message);
					ready = MessageIO.readByte(message);
					curScr.setReady(nick, (byte) ready);
					// if (nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
					// curScr.openCmd();
				}
				nick = null;
			}
			break;

		case CMD.CMD_PLAYER_LEAVE_TABLE:
			String master = MessageIO.readString(message);
			nick = MessageIO.readString(message);
			curScr.playerLeave(nick);
			curScr.setOwner(master);
			master = null;
			nick = null;
			break;

		case CMD.CMD_CHAT_IN_TABLE:
			String name = MessageIO.readString(message);
			String ms = MessageIO.readString(message);
			curScr.showChatInTable(name, ms);
			ms = null;
			name = null;
			break;

		case CMD.CMD_SET_TURN:
			nick = MessageIO.readString(message);
			curScr.onSetTurn(nick, message);
			GameCanvas.endDlg();
			nick = null;
			break;

		case CMD.CMD_ALLCARD_FINISH:
			curScr.onAllCardPlayerFinish(message);
			break;

		case CMD.CMD_GAMEOVER:
			curScr.onFinishGame(message);
			break;

		case CMD.CMD_KICK_PLAYER:
			b = MessageIO.readByte(message);
			switch (b) {
			case 0:
				// GameCanvas.startOKDlg("Không thể đuổi...");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Không thể đuổi...");
				break;
			case 1:
				// GameCanvas.startOKDlg("Bị đuổi khỏi bàn");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Bị đuổi khỏi bàn");
				PlayGameScr.m_iStatusOutGame = 1;
				curScr.stopGame();
				break;
			case 2:
				// GameCanvas.startOKDlg("Không đủ tiền. Vui lòng nạp thêm tiền.");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo",
						"Không đủ tiền. Vui lòng nạp thêm tiền.");
				PlayGameScr.m_iStatusOutGame = 1;
				curScr.stopGame();
				break;
			case 3:
				// GameCanvas.startOKDlg("Không thể đuổi. Người này có thẻ chống kick.");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo",
						"Không thể đuổi. Người này có thẻ chống kick.");
				break;
			}
			break;

		case CMD.CMD_SET_BET_GOLD_FOR_TABLE:
			tableId = MessageIO.readInt(message);
			long betGold = MessageIO.readLong(message);
			if (GameCanvas.currentScreen instanceof ListBoardScr) {
				ListBoardScr.getInstance().setBetGoldOfTable(tableId, betGold);
			} else {
				curScr.setBetGoldOfTable(betGold);
			}
			GameCanvas.endDlg();
			break;

		case CMD.CMD_PLAYER_JOIN_GAME:
			byte gameId = MessageIO.readByte(message);
			playerJoinGame(gameId);
			break;

		case CMD.CMD_GET_LIST_FREE_PLAYERS_JOINED_ROOM:
			GameCanvas.endDlg();
			count = MessageIO.readShort(message);
			list = new Vector();
			for (i = 0; i < count; i++) {
				playerInfo = new PlayerInfo();
				playerInfo.itemName = MessageIO.readString(message);
				playerInfo.avatarId = MessageIO.readInt(message);
				playerInfo.isChampion = MessageIO.readBoolean(message);
				playerInfo.level = MessageIO.readInt(message);
				playerInfo.gold = MessageIO.readLong(message);
				playerInfo.onlineStatus = 1;
				playerInfo.status = "";
				playerInfo.m_bIsShowMoneyAndLevel = true;
				list.addElement(playerInfo);
			}
			ListScr.gI().itemHeight = 50;
			ListScr.gI().m_iTopScroll = GameCanvas.hBottomBar + 5;
			ListScr.gI().setListItem(list, null, true, false, true);
			ListScr.gI().m_iTopScroll = GameCanvas.hBottomBar + 5;

			center = null;
			if (list.size() > 0) {
				center = new Command("Mời", new IAction() {
					public void perform() {
						PlayerInfo playerInfo = (PlayerInfo) ListScr.gI().getSelectItems();
						if (playerInfo != null) {
							GlobalService.onInvitePlayerToPlay(playerInfo.itemName);
							// GameCanvas.startOKDlg("Bạn đã mời " +
							// playerInfo.itemName);
							HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Bạn đã mời "
									+ playerInfo.itemName);
						}
						playerInfo = null;
					}
				});
			}
			ListScr.gI().switchToMe(GameCanvas.currentScreen, "Danh sách", null, center, null);
			playerInfo = null;
			list = null;
			break;

		case CMD.CMD_CHANGE_AVATAR:
			isSuccess = MessageIO.readBoolean(message);
			avatarId = MessageIO.readInt(message);
			content = MessageIO.readString(message);

			if (isSuccess) {
				HDCGameMidlet.m_myPlayerInfo.avatarId = avatarId;
			}
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			break;

		case CMD.CMD_INVITE_PLAYER_TO_PLAY:
			boolean isJoinSuccess = MessageIO.readBoolean(message);
			if (!isJoinSuccess) {
				// GameCanvas.startOKDlg("Không thể chơi lúc này.");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Không thể chơi lúc này.");
			} else {
				Vector listCommand = new Vector();
				final String nickInvite = MessageIO.readString(message);
				final int idTable = MessageIO.readInt(message);
				listCommand.addElement(new Command(GameResource.yes, new IAction() {
					public void perform() {
						GlobalService.sendMessageToJoinTable(idTable, "", true);
						GameCanvas.endDlg();
					}
				}));

				listCommand.addElement(new Command(GameResource.no, new IAction() {
					public void perform() {
						GlobalService.onResponseInviteFriend(nickInvite);
						GameCanvas.endDlg();
					}
				}));

				GameCanvas.startOKDlg("Bạn có muốn đánh chung với " + nickInvite + " không?",
						listCommand);
			}
			break;
		case CMD.CMD_ANSWER_INVITE_FRIEND:
			// GameCanvas.startOKDlg(MessageIO.readInt(message) + "\n" +
			// MessageIO.readString(message)
			// + " không muốn đánh.");
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readInt(message) + "\n"
					+ MessageIO.readString(message) + " không muốn đánh.");
			break;
		case CMD.CMD_RESPONSE_INVITE:
			break;
		case CMD.CMD_GET_LIST_MAIL:
			TabScr.gI().removeAll();
			// final Screen scrBack = GameCanvas.instance.currentScreen;
			scrRoom1 = new ListScr();

			// ListMailScr.getInstance().switchToMe(message);
			left = new Command(GameResource.menu, new IAction() {
				public void perform() {
					Vector vt = new Vector();

					vt.addElement(new Command(GameResource.remove, new IAction() {
						public void perform() {
							MailInfo mailInfo = (MailInfo) scrRoom1.getSelectItems();
							if (mailInfo != null) {
								m_listScr_Msg = scrRoom1;
								GameCanvas.startWaitDlg();
								GlobalService.sendMessageDeleteMail(mailInfo.itemId, mailInfo.type);
							}
							mailInfo = null;
						}
					}));

					vt.addElement(new Command(GameResource.removeList, new IAction() {
						public void perform() {
							m_listScr_Msg = scrRoom1;
							Vector listDeletedMails = new Vector();
							for (int i = 0; i < scrRoom1.m_arrItems.size(); i++) {
								MailInfo mailInfo = (MailInfo) scrRoom1.m_arrItems.elementAt(i);
								if (mailInfo.isRead == 1) {
									listDeletedMails.addElement(mailInfo);
								}
							}

							if (listDeletedMails.size() > 0) {
								GameCanvas.startWaitDlg();
								GlobalService.sendMessageDeleteListMails(listDeletedMails);
							} else {
								// GameCanvas.startOKDlg("Không có tin nhắn chưa đọc");

							}
							listDeletedMails = null;
						}
					}));

					vt.addElement(new Command(GameResource.removeAll, new IAction() {
						public void perform() {
							m_listScr_Msg = scrRoom1;
							if (scrRoom1.m_arrItems.size() > 0) {
								GameCanvas.startWaitDlg();
								GlobalService.sendMessageDeleteAllMails();
							} else {
								// GameCanvas.startOKDlg(GameResource.emptyMail);
								HDCGameMidlet.instance.showDialog_Okie("Thông báo",
										GameResource.emptyMail);
							}
						}
					}));
					GameCanvas.instance.menu.startAt(vt, 0);
					vt = null;
				}
			});

			right = new Command(GameResource.close, new IAction() {
				public void perform() {
					// GameCanvas.startWaitDlg();
					// Screen mScrBack = TabScr.gI().backScr;
					// TabScr.gI().removeAll();
					// mScrBack.switchToMe();
					// scrBack.switchToMe();
					TabScr.gI().close();
				}
			});

			Command readPlainTextMessage = new Command(GameResource.read, new IAction() {
				public void perform() {
					final MailInfo messageInfo = (MailInfo) scrRoom1.getSelectItems();
					if (messageInfo != null) {
						if (messageInfo.isRead == 0) {
							GlobalService.sendMessageIsReadMessage(MailInfo.PLAN_TEXT,
									messageInfo.itemId);
							messageInfo.isRead = 1;
						}

						if (Screen.numberUnreadMail > 0) {
							Screen.numberUnreadMail--;
						}

						// Vector vt = new Vector();

						if (!messageInfo.sender.toLowerCase().equals("system")
								&& !messageInfo.sender.toLowerCase().equals(
										HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
							// vt.addElement(new Command("Trả lời", new
							// IAction() {
							// public void perform() {
							// showDialogMessanger(scrRoom1);
							// }
							// }));
							HDCGameMidlet.instance.showDialog_GuiTinNhan("Từ " + messageInfo.sender
									+ " " + messageInfo.time, messageInfo.content,
									(MailInfo) scrRoom1.getSelectItems());
						} else {
							HDCGameMidlet.instance.showDialog_Okie("Tin nhắn " + "từ: "
									+ messageInfo.sender + " " + messageInfo.time,
									messageInfo.content);
						}

						// vt.addElement(new Command(GameResource.close, new
						// IAction() {
						// public void perform() {
						// GameCanvas.endDlg();
						// }
						// }));

						// GameCanvas.startMsgDlg("Từ: " + messageInfo.sender +
						// "   "
						// + messageInfo.time + "\n" + messageInfo.content, vt);

						// vt = null;
					}
				}
			});

			list = new Vector();
			// Read mail plain text.
			count = MessageIO.readInt(message);
			for (i = 0; i < count; i++) {
				mailInfo = new MailInfo(ListScr.gI());
				mailInfo.itemId = MessageIO.readInt(message);
				mailInfo.sender = MessageIO.readString(message);
				mailInfo.time = MessageIO.readString(message);
				mailInfo.content = MessageIO.readString(message);
				mailInfo.isRead = MessageIO.readByte(message);
				mailInfo.type = MessageIO.readInt(message);
				list.addElement(mailInfo);
			}

			scrRoom1.m_cmdLeft = left;
			scrRoom1.m_cmdRight = right;
			scrRoom1.m_cmdCenter = readPlainTextMessage;
			scrRoom1.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			scrRoom1.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
			scrRoom1.setListItem(list, null, false, false, false);
			TabScr.gI().addScreen(scrRoom1, "Hộp thư");

			list = new Vector();
			// Read request make friend.
			count = MessageIO.readInt(message);
			for (i = 0; i < count; i++) {
				mailInfo = new MailInfo(ListScr.gI());
				mailInfo.itemId = MessageIO.readInt(message);
				mailInfo.sender = MessageIO.readString(message);
				mailInfo.time = MessageIO.readString(message);
				mailInfo.content = MessageIO.readString(message);
				mailInfo.isRead = MessageIO.readByte(message);
				mailInfo.type = MessageIO.readInt(message);
				list.addElement(mailInfo);
			}

			scrRoom2 = new ListScr();

			Command readRequestMakeFriendMessage = new Command(GameResource.read, new IAction() {
				@SuppressWarnings("rawtypes")
				public void perform() {
					final MailInfo messageInfo = (MailInfo) scrRoom2.getSelectItems();
					if (messageInfo != null) {
						if (messageInfo.isRead == 0) {
							GlobalService.sendMessageIsReadMessage(MailInfo.REQUEST_MAKE_FRIEND,
									messageInfo.itemId);
							messageInfo.isRead = 1;
						}

						if (Screen.numberUnreadMail > 0) {
							Screen.numberUnreadMail--;
						}

						// Vector vt = new Vector();
						// vt.addElement(new Command(GameResource.accept, new
						// IAction() {
						// public void perform() {
						// GlobalService
						// .sendMessageAcceptRequestMakeFriend(messageInfo.sender);
						// GameCanvas.startWaitDlg();
						// }
						// }));
						//
						// vt.addElement(new Command(GameResource.deni, new
						// IAction() {
						// public void perform() {
						// GlobalService.sendMessageDenyRequestMakeFriend(messageInfo.sender);
						// GameCanvas.startWaitDlg();
						// }
						// }));

						// vt.addElement(new Command(GameResource.close, new
						// IAction() {
						// public void perform() {
						// GameCanvas.endDlg();
						// }
						// }));

						// GameCanvas.startMsgDlg("Từ: " + messageInfo.sender +
						// "   "
						// + messageInfo.time + "\n" + messageInfo.content, vt);

						HDCGameMidlet.instance.showDialog_ketban("Tin nhắn " + "Từ: "
								+ messageInfo.sender + " " + messageInfo.time, messageInfo.content,
								new IAction() {
									public void perform() {
										GlobalService
												.sendMessageAcceptRequestMakeFriend(messageInfo.sender);
										GameCanvas.startWaitDlg();
									}
								}, new IAction() {
									public void perform() {
										GlobalService
												.sendMessageDenyRequestMakeFriend(messageInfo.sender);
										GameCanvas.startWaitDlg();
									}
								});

						// vt = null;
					}
				}
			});

			left = new Command(GameResource.menu, new IAction() {
				public void perform() {
					Vector vt = new Vector();

					vt.addElement(new Command(GameResource.remove, new IAction() {
						public void perform() {
							m_listScr_Msg = scrRoom2;
							MailInfo mailInfo = (MailInfo) scrRoom2.getSelectItems();
							if (mailInfo != null) {
								GameCanvas.startWaitDlg();
								GlobalService.sendMessageDeleteMail(mailInfo.itemId, mailInfo.type);
							}
							mailInfo = null;
						}
					}));

					vt.addElement(new Command(GameResource.removeList, new IAction() {
						public void perform() {
							m_listScr_Msg = scrRoom2;
							Vector listDeletedMails = new Vector();
							for (int i = 0; i < scrRoom2.m_arrItems.size(); i++) {
								MailInfo mailInfo = (MailInfo) scrRoom1.m_arrItems.elementAt(i);
								if (mailInfo.isRead == 1) {
									listDeletedMails.addElement(mailInfo);
								}
							}

							if (listDeletedMails.size() > 0) {
								GameCanvas.startWaitDlg();
								GlobalService.sendMessageDeleteListMails(listDeletedMails);
							} else {
								// GameCanvas.startOKDlg("Không có tin nhắn chưa đọc");
								HDCGameMidlet.instance.showDialog_Okie("Thông báo",
										"Không có tin nhắn chưa đọc");
							}
							listDeletedMails = null;
						}
					}));

					vt.addElement(new Command(GameResource.removeAll, new IAction() {
						public void perform() {
							m_listScr_Msg = scrRoom2;
							if (scrRoom2.m_arrItems.size() > 0) {
								GameCanvas.startWaitDlg();
								GlobalService.sendMessageDeleteAllMails();
							} else {
								// GameCanvas.startOKDlg(GameResource.emptyMail);
								HDCGameMidlet.instance.showDialog_Okie("Thông báo",
										GameResource.emptyMail);
							}
						}
					}));
					GameCanvas.instance.menu.startAt(vt, 0);
					vt = null;
				}
			});

			scrRoom2.m_cmdLeft = left;
			scrRoom2.m_cmdRight = right;
			scrRoom2.m_cmdCenter = readRequestMakeFriendMessage;
			scrRoom2.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			scrRoom2.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
			scrRoom2.setListItem(list, null, false, false, false);
			TabScr.gI().addScreen(scrRoom2, "Kết bạn");

			TabScr.gI().setActiveScr((Screen) TabScr.gI().m_vtScr.elementAt(0));
			TabScr.gI().m_strTitle = "Hòm thư";
			// TabScr.gI()
			// .switchToMe(
			// !(GameCanvas.instance.currentScreen instanceof TabScr) ?
			// GameCanvas.instance.currentScreen
			// : TabScr.getBackScr());

			if (!(GameCanvas.instance.currentScreen instanceof ListBoardScr))
				TabScr.gI().switchToMe(GameCanvas.instance.currentScreen);

			scrRoom = null;
			list = null;

			GameCanvas.endDlg();

			break;
		case CMD.CMD_ACCEPT_REQUEST_MAKE_FRIEND:
			content = MessageIO.readString(message);
			isSuccess = MessageIO.readBoolean(message);
			if (isSuccess) {
				scrRoom2.removeSelectedItem(false);
			}
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			content = null;
			break;

		case CMD.CMD_DENY_REQUEST_MAKE_FRIEND:
			content = MessageIO.readString(message);
			isSuccess = MessageIO.readBoolean(message);
			if (isSuccess) {
				scrRoom2.removeSelectedItem(false);
			}
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			content = null;
			break;
		case CMD.CMD_DELETE_MAIL:
			// ListMailScr.getInstance().deleteMail(message);
			boolean isDeleted = MessageIO.readBoolean(message);
			if (!isDeleted) {
				// GameCanvas.startOKDlg(MessageIO.readString(message));
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readString(message));
			} else {
				mailInfo = (MailInfo) m_listScr_Msg.getSelectItems();
				if (Screen.numberUnreadMail > 0 && mailInfo.isRead == 0) {
					Screen.numberUnreadMail--;
				}
				m_listScr_Msg.removeSelectedItem(false);
				// GameCanvas.startOKDlg("Đã xóa thư");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Đã xóa thư");
				mailInfo = null;
			}
			break;
		case CMD.CMD_DELETE_LIST_MAIL:
			isSuccess = MessageIO.readBoolean(message);
			if (isSuccess) {
				list = new Vector();
				count = MessageIO.readInt(message);
				for (i = 0; i < count; i++) {
					mailInfo = new MailInfo(m_listScr_Msg);
					mailInfo.itemId = MessageIO.readInt(message);
					mailInfo.type = MessageIO.readInt(message);
					list.addElement(mailInfo);
				}

				// Delete list mails.
				MailInfo deletedMailInfo;
				for (i = ListScr.gI().m_arrItems.size() - 1; i >= 0; i--) {
					mailInfo = (MailInfo) m_listScr_Msg.m_arrItems.elementAt(i);
					for (j = 0; j < list.size(); j++) {
						deletedMailInfo = (MailInfo) list.elementAt(j);
						if (deletedMailInfo.itemId == mailInfo.itemId
								&& deletedMailInfo.type == mailInfo.type) {
							m_listScr_Msg.m_arrItems.removeElementAt(i);
						}
					}
				}

				// If delete all mails, reset mail icon on toolbar to 0.
				if (m_listScr_Msg.m_arrItems.size() == 0) {
					Screen.numberUnreadMail = 0;
					Screen.hasNewMail = false;
				}

				mailInfo = null;
				deletedMailInfo = null;
				list = null;

				// GameCanvas.startOKDlg("Đã xóa");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Đã xóa !!!");
			} else {
				// GameCanvas.startOKDlg(MessageIO.readString(message));
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readString(message));
			}
			break;
		case CMD.CMD_GET_NUMBER_UNREAD_MAIL:
			Screen.numberUnreadMail = MessageIO.readInt(message);
			if (Screen.numberUnreadMail > 0) {
				Screen.hasNewMail = true;
			}
			break;

		case CMD.CMD_SMS:
			Vector vtSms = new Vector();
			count = MessageIO.readShort(message);
			MoneyInfo moneyInfo;
			for (i = 0; i < count; i++) {
				moneyInfo = new MoneyInfo();
				moneyInfo.info = MessageIO.readString(message);
				moneyInfo.smsContent = MessageIO.readString(message);
				moneyInfo.smsTo = MessageIO.readString(message);

				vtSms.addElement(moneyInfo);
			}

			DataManager.gI().m_vtSMS = vtSms;
			moneyInfo = null;
			vtSms = null;
			break;

		case CMD.CMD_TOP_LEVEL_AND_GOLD:
			TabScr.gI().removeAll();
			// //////
			center = new Command("Xem", new IAction() {
				public void perform() {
					PlayerInfo playerInfo = (PlayerInfo) TabScr.gI().getSelectedItem();
					GlobalService.onViewInfoFriend(playerInfo.itemName);
					GameCanvas.startWaitDlg();
					playerInfo = null;
				}
			});

			right = new Command(GameResource.close, new IAction() {
				public void perform() {
					TabScr.gI().close();
					TabScr.gI().removeAll();
				}
			});
			// /////
			list = new Vector();
			readListPlayers(message, list);
			tab = new ListTabScr();
			tab.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			tab.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
			tab.setListItem(list, null, center, right, true);
			TabScr.gI().addScreen(tab, GameResource.topPlayer);

			list = new Vector();
			readListPlayers(message, list);
			tab = new ListTabScr();
			tab.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			tab.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
			tab.setListItem(list, null, center, right, true);
			TabScr.gI().addScreen(tab, GameResource.topMoney);

			TabScr.gI().setActiveScr((Screen) TabScr.gI().m_vtScr.elementAt(0));
			TabScr.gI().m_strTitle = GameResource.top;
			TabScr.gI().switchToMe(GameCanvas.currentScreen);

			list = null;
			scrRoom = null;
			center = null;
			left = null;
			right = null;
			GameCanvas.endDlg();
			System.gc();
			break;

		case CMD.CMD_GET_CLAN_NAME:
			String clanName = MessageIO.readString(message);
			ClanScr.gI().myClanOwner.itemName = clanName;
			ClanScr.gI().hasMyClan = false;
			if (!clanName.toLowerCase().equals("") && clanName.toLowerCase().length() > 0)
				ClanScr.gI().hasMyClan = true;
			ClanScr.gI().updateStatusClan();
			break;
		case CMD.CMD_GET_NUMBER_NOTIFICATION_JOIN_CLAN:
			i = MessageIO.readInt(message);
			j = MessageIO.readInt(message);
			ClanScr.gI().updateLetterClans(i + j);
			ClanScr.gI().switchToMe();
			GameCanvas.endDlg();
			break;
		case CMD.CMD_CHECK_REGISTER_CLAN:
			// ClanRegisterScr.getInstance().checkRegisterClan(message);
			isSuccess = MessageIO.readBoolean(message);
			content = MessageIO.readString(message);

			if (isSuccess) {
				if (!ShopScr.gI().tfClanName.getText().equals("")
						&& ShopScr.gI().tfClanName.getText().length() > 0) {
					GameCanvas.startOKDlg(content, new IAction() {
						public void perform() {
							GlobalService.sendMessageConfirmRegisterClan(
									ShopScr.gI().tfClanName.getText(),
									ShopScr.gI().tfClanStatus.getText(), ShopScr.gI().m_selectItem);
						}
					});
				} else {
					// GameCanvas.startOKDlg("Tên gia tộc bạn muốn đăng ký \n không được bỏ trống");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",
							"Tên gia tộc bạn muốn đăng ký \n không được bỏ trống");
				}
			} else {
				// GameCanvas.startOKDlg(content);
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			}
			content = null;
			break;
		case CMD.CMD_ENTER_CLAN_CHAT_ROOM:
			list = new Vector();
			int size = MessageIO.readInt(message);
			for (i = 0; i < size; i++) {
				str = MessageIO.readString(message);
				list.addElement(str);
			}
			if (ClanScr.gI().m_scrChatRoom == null)
				ClanScr.gI().m_scrChatRoom = new ListScr();

			// ClanScr.gI().m_scrChatRoom.itemHeight = Screen.ITEM_HEIGHT;
			ClanScr.gI().m_scrChatRoom.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			ClanScr.gI().m_scrChatRoom.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow
					.getHeight());

			ClanScr.gI().m_scrChatRoom.setListItem(new Vector(), list, false, true, false);
			list = null;
			str = null;
			break;
		case CMD.CMD_CHAT_IN_CLAN_ROOM:
			ClanScr.gI().addChatRoom(message);
			break;
		case CMD.CMD_UPDATE_CLAN_CHAT_ROOM:
			boolean isLeft = MessageIO.readBoolean(message);
			String member = MessageIO.readString(message);
			if (isLeft) {
				ClanScr.gI().removeMember(member);
			} else {
				ClanScr.gI().addMember(member);
			}
			member = null;
			break;
		case CMD.CMD_REMOVE_CLAN_MEMBER:
			isSuccess = MessageIO.readBoolean(message);
			content = MessageIO.readString(message);
			member = MessageIO.readString(message);

			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			if (isSuccess) {
				list = TabScr.gI().getListItems();
				if (list != null) {
					for (i = 0; i < list.size(); i++) {
						playerInfo = (PlayerInfo) list.elementAt(i);
						if (playerInfo.itemName.toLowerCase().equals(member.toLowerCase())) {
							list.removeElementAt(i);
							break;
						}
					}
					list = null;
					playerInfo = null;
				}
			}
			member = null;
			content = null;
			GameCanvas.endDlg();
			break;
		case CMD.CMD_KICK_CLAN_MEMBER:
			content = MessageIO.readString(message);
			if ((GameCanvas.currentScreen instanceof TabScr)
					&& (TabScr.gI().backScr instanceof ClanScr)
					|| GameCanvas.currentScreen instanceof ClanScr) {
				if (!(GameCanvas.currentScreen instanceof ClanScr))
					ClanScr.gI().switchToMe();

				ClanScr.gI().hasMyClan = false;
				ClanScr.gI().updateStatusClan();
			}
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			break;
		case CMD.CMD_SET_CLAN_DEPUTY:
			isSuccess = MessageIO.readBoolean(message);
			content = MessageIO.readString(message);
			member = MessageIO.readString(message);

			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			if (isSuccess) {
				list = TabScr.gI().getListItems();
				for (i = 0; i < list.size(); i++) {
					playerInfo = (PlayerInfo) list.elementAt(i);
					if (playerInfo.itemName.toLowerCase().equals(member.toLowerCase())) {
						playerInfo.role = PlayerInfo.ROLE_DEPUTY;
					} else {
						if (playerInfo.role != PlayerInfo.ROLE_HEADMAN) {
							playerInfo.role = PlayerInfo.ROLE_MEMBER;
						}
					}
				}
				list = null;
				playerInfo = null;
			}
			member = null;
			content = null;
			GameCanvas.endDlg();
			System.gc();
			break;
		case CMD.CMD_GET_MY_CLAN_INFORMATION:
			isSuccess = MessageIO.readBoolean(message);
			if (isSuccess) {
				ClanScr.gI().showMyClanScr(message);
			} else {
				// GameCanvas.startOKDlg("Bạn không còn là thành viên gia tộc này, thử cập nhập lại!");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo",
						"Bạn không còn là thành viên gia tộc này, thử cập nhập lại!");
			}
			break;
		case CMD.CMD_REJECT_CLAN:
			isSuccess = MessageIO.readBoolean(message);
			content = MessageIO.readString(message);
			if (isSuccess && GameCanvas.currentScreen instanceof TabScr) {
				ClanScr.gI().hasMyClan = false;
				ClanScr.gI().updateStatusClan();
				ClanScr.gI().switchToMe();
			}
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			content = null;
			break;
		case CMD.CMD_CONTRIBUTE_CLAN:
			ClanInformationScr.gI().updateGoldAndScore(message);
			break;

		case CMD.CMD_CONFIRM_REGISTER_CLAN:
			if (MessageIO.readBoolean(message)) {
				content = MessageIO.readString(message);
				// GameCanvas.startOK(content, new IAction() {
				// public void perform() {
				// ClanScr.gI().refeshClanScr();
				// }
				// });

				HDCGameMidlet.instance.showDialog_Okie_withCommand("Thông báo", content,
						new IAction() {
							public void perform() {
								ClanScr.gI().refeshClanScr();
							}
						});
			} else
				displayInfo(message);
			break;
		case CMD.CMD_FINISH_COMPETITION_WITH_LAST_COMPETITOR:
		case CMD.CMD_CANCEL_COMPETE:
		case CMD.CMD_INVITE_PLAYER_JOIN_CLAN:
		case CMD.CMD_PLAYER_REQUEST_JOIN_CLAN:
			displayInfo(message);
			break;
		case CMD.CMD_GET_CLAN_NOTIFICATIONS:
			list = new Vector();
			count = MessageIO.readInt(message);
			// count = 10;
			InviteRequestJoinClan item;
			for (i = 0; i < count; i++) {
				item = new InviteRequestJoinClan();
				item.itemName = MessageIO.readString(message);
				item.nickToJoin = MessageIO.readString(message);
				// item.itemName = "a " + i;
				// item.nickToJoin = "b " + i;
				item.isInviteType = true;
				list.addElement(item);
			}

			size = MessageIO.readInt(message);
			// size = 10;
			for (i = 0; i < size; i++) {
				item = new InviteRequestJoinClan();
				item.itemName = MessageIO.readString(message);
				item.nickToJoin = MessageIO.readString(message);
				// item.itemName = "c " + i;
				// item.nickToJoin = "d " + i;
				item.isInviteType = false;
				list.addElement(item);
			}

			// ListScr.gI().m_iTopScroll = GameCanvas.hBottomBar + 5;
			// ListScr.gI().itemHeight = 30;

			ListScr.gI().m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			ListScr.gI().m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow
					.getHeight());

			ListScr.gI().setListItem(list, null, true, false, false);
			if (!(GameCanvas.currentScreen instanceof ListScr)) {
				center = new Command(GameResource.accept, new IAction() {
					public void perform() {
						GameCanvas.startOKDlg("Bạn có muốn chấp nhận lời mời này", new IAction() {
							public void perform() {
								InviteRequestJoinClan item = (InviteRequestJoinClan) ListScr.gI()
										.getSelectItems();
								if (item.isInviteType) {
									GlobalService.sendMessageJoinClanFromInvite(item);
								} else {
									GlobalService.sendMessageJoinClanFromRequest(item);
								}
								GameCanvas.startWaitDlg();
								item = null;
							}
						});
					}
				});

				right = new Command(GameResource.close, new IAction() {
					public void perform() {
						ListScr.gI().close();
						ClanScr.gI().refeshClanScr();
					}
				});

				left = new Command(GameResource.deni, new IAction() {
					public void perform() {
						GameCanvas.startOKDlg("Bạn có muốn từ chối lời mời này", new IAction() {
							public void perform() {
								InviteRequestJoinClan item = (InviteRequestJoinClan) ListScr.gI()
										.getSelectItems();
								if (item.isInviteType) {
									GlobalService.sendMessageDenyJoinClanFromInvite(item.itemName);
								} else {
									GlobalService.sendMessageDenyJoinClanFromRequest(item.itemName,
											item.nickToJoin);
								}
								GameCanvas.startWaitDlg();
								item = null;
							}
						});
					}
				});

				ListScr.gI().switchToMe(ClanScr.gI(), "Thông báo", left, center, right);

				left = null;
				center = null;
				right = null;
			}
			item = null;
			list = null;
			GameCanvas.endDlg();
			System.gc();
			break;

		case CMD.CMD_JOIN_CLAN_FROM_INVITE:
			isSuccess = MessageIO.readBoolean(message);
			content = MessageIO.readString(message);

			if (isSuccess) {
				InviteRequestJoinClan items;
				for (j = ListScr.gI().m_arrItems.size() - 1; j >= 0; j--) {
					items = (InviteRequestJoinClan) ListScr.gI().m_arrItems.elementAt(j);
					if (items != null && items.isInviteType) {
						ListScr.gI().m_arrItems.removeElement(items);
						j = ListScr.gI().m_arrItems.size();
					}
				}
				items = null;
				if (ListScr.gI().m_arrItems.size() == 0) {
					ListScr.gI().m_cmdLeft = null;
					ListScr.gI().m_cmdCenter = null;
				}
				ListScr.gI().init();
			}

			GameCanvas.endDlg();
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			content = null;
			break;

		case CMD.CMD_JOIN_CLAN_FROM_REQUEST:
		case CMD.CMD_DENY_JOIN_CLAN_FROM_REQUEST:
		case CMD.CMD_DENY_JOIN_CLAN_FROM_INVITE:
			isSuccess = MessageIO.readBoolean(message);
			content = MessageIO.readString(message);
			if (isSuccess) {
				ListScr.gI().m_arrItems.removeElementAt(ListScr.gI().m_selected);
				if (ListScr.gI().m_arrItems.size() == 0) {
					ListScr.gI().m_cmdLeft = null;
					ListScr.gI().m_cmdCenter = null;
				} else {
					ListScr.gI().m_selected = 0;
				}
				ListScr.gI().init();
			}
			GameCanvas.endDlg();
			// GameCanvas.startOKDlg(content);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			content = null;
			break;

		case CMD.CMD_CHECK_REGISTER_COMPETITION:
			isSuccess = MessageIO.readBoolean(message);
			final int m_igameId = MessageIO.readInt(message);
			content = MessageIO.readString(message);

			IAction acceptAction = new IAction() {
				public void perform() {
					GlobalService.sendMessageConfirmRegisterCompetition(m_igameId);
					GameCanvas.endDlg();
				}
			};

			if (isSuccess) {
				GameCanvas.startOKDlg(content, acceptAction);
			} else {
				// GameCanvas.startOKDlg(content);
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
			}
			break;
		case CMD.CMD_CONFIRM_REGISTER_COMPETITION:
			ListStringScr.gI().setCompetition(MessageIO.readString(message));
			break;
		case CMD.CMD_NOTIFY_BEFORE_COMPETITION:
			notifyBeforeCompetition(message);
			break;
		case CMD.CMD_JOIN_COMPETITION_ROOM:
			center = new Command(GameResource.chat, new IAction() {
				public void perform() {
					GameCanvas.inputDlg.setInfo("Nội dung chat: ", new IAction() {
						public void perform() {
							if (GameCanvas.inputDlg.tfInput.getText().length() > 0) {
								GlobalService
										.sendMessageChatCompetitionRoom(GameCanvas.inputDlg.tfInput
												.getText());
								GameCanvas.inputDlg.tfInput.setText("");
							}
							GameCanvas.endDlg();
						}
					}, TField.INPUT_TYPE_ANY);
					GameCanvas.inputDlg.show();
				}
			});

			right = new Command("Rời phòng", new IAction() {
				public void perform() {
					GlobalService.sendMessageLeaveCompetitionRoom();
					MainScr.gI().switchToMe();
				}
			});

			list = new Vector();
			String gameName = MainScr.getGameNameById(MessageIO.readInt(message));
			String[] competitorNicks = MessageIO.readArrayString(message);
			for (i = 0; i < competitorNicks.length; i++) {
				list.addElement(competitorNicks[i]);
			}

			ListScr.gI().m_iTopScroll = GameCanvas.hBottomBar + 5;
			ListScr.gI().itemHeight = Screen.ITEM_HEIGHT;
			ListScr.gI().setListItem(new Vector(), list, true, true, false);
			ListScr.gI().switchToMe(null, "Phòng đấu " + gameName, null, center, right);

			center = null;
			right = null;
			gameName = null;
			competitorNicks = null;
			list = null;
			GameCanvas.endDlg();
			System.gc();
			break;
		case CMD.CMD_UPDATE_COMPETITION_ROOM:
			isSuccess = MessageIO.readBoolean(message);
			member = MessageIO.readString(message);
			if (ListScr.gI().m_arrListMembers != null) {
				if (isSuccess) {
					if (!ListScr.gI().m_arrListMembers.contains(member)) {
						ListScr.gI().m_arrListMembers.insertElementAt(member, 0);
						ListScr.gI().addChatContent(member + GameResource.joinCPTT, 0xFFFFFF);
					}
				} else {
					ListScr.gI().m_arrListMembers.removeElement(member);
					ListScr.gI().addChatContent(member + GameResource.outCPTT, 0xFFFFFF);
				}
			}
			member = null;
			break;
		case CMD.CMD_CHAT_COMPETITION_ROOM:
			content = MessageIO.readString(message);
			i = MessageIO.readInt(message);
			if (ListScr.gI().m_arrListMembers != null)
				ListScr.gI().addChatContent(content, i);
			content = null;
			break;
		case CMD.CMD_NOTIFY_COMPETITION:
			notifyCompetition(message);
			break;
		case CMD.CMD_FREEZE_COMPETITION_ROOM:
			ListScr.gI().m_cmdLeft = null;
			ListScr.gI().m_cmdCenter = null;
			ListScr.gI().m_cmdRight = null;
			break;
		case CMD.CMD_TIME_COUNT_DOWN:
			content = MessageIO.readString(message);
			if (ListScr.gI().m_arrListMembers != null)
				ListScr.gI().addChatContent(content, 0xFFFF00);
			content = null;
			break;
		case CMD.CMD_INITIALIZE_COMPETITION_DATA:
			initializeCompetitionData(message);
			break;
		case CMD.CMD_WIN_COMPETITOR:
			winCompetitor(message);
			break;
		case CMD.CMD_LOSE_COMPETITOR:
			loseCompetitor(message);
			break;
		case CMD.CMD_GAME_CHAMPION:
			gameChampion(message);
			break;
		case CMD.CMD_GET_LIST_CHAMPION:
			// ListChampionScr.getInstance().switchToMe(message);
			count = MessageIO.readByte(message);
			list = new Vector();
			for (i = 0; i < count; i++) {
				playerInfo = new PlayerInfo();
				playerInfo.itemName = MessageIO.readString(message);
				playerInfo.onlineStatus = MessageIO.readByte(message);
				playerInfo.gold = MessageIO.readLong(message);
				playerInfo.avatarId = MessageIO.readInt(message);
				playerInfo.status = MessageIO.readString(message);
				playerInfo.competitionGameId = MessageIO.readInt(message);

				if (playerInfo.onlineStatus == 1) {
					list.insertElementAt(playerInfo, 0);
				} else {
					list.addElement(playerInfo);
				}
			}
			if (list.size() > 0) {
				ListScr.gI().m_iTopScroll = GameCanvas.hBottomBar + 5;
				ListScr.gI().itemHeight = 50;
				ListScr.gI().setListItem(list, null, true, false, true);
				ListScr.gI().switchToMe(GameCanvas.currentScreen, "Thần bài tuần trước", null,
						null, null);
				GameCanvas.endDlg();
			} else {
				// GameCanvas.startOKDlg("Tuần trước, không có thần bài nào");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo",
						"Tuần trước, không có thần bài nào");
			}
			playerInfo = null;
			list = null;
			break;
		case CMD.CMD_IS_REGISTERED_COMPETITION:
			// ListStringScr m_listStringScr = new ListStringScr();
			// m_listStringScr.setInfoRegister(message);
			ListStringScr.gI().setInfoRegister(message);
			// Screen mScreen = TabScr.gI().getScreen(2);
			// String title = TabScr.gI().getTitle(2);
			// ListStringScr.gI().m_cmdRight = mScreen.m_cmdRight;
			// ListStringScr.gI().m_cmdLeft = mScreen.m_cmdLeft;
			// ListStringScr.gI().m_cmdCenter = mScreen.m_cmdCenter;
			// TabScr.gI().removeScreen(2);
			// TabScr.gI().addScreen(ListStringScr.gI(), title);
			break;
		case CMD.CMD_CLEAR_FIRED_CARD:
			curScr.onClearFiredCards();
			break;
		case CMD.CMD_GET_CLAN_INFO:
			GameCanvas.endDlg();
			ClanInformationScr.gI().setStatus(MessageIO.readString(message));
			ClanInformationScr.gI().nickHeadman = MessageIO.readString(message);
			level = MessageIO.readInt(message);
			ClanInformationScr.gI().colorName = ClanInformationScr.gI().getColor(level);
			ClanInformationScr.gI().switchToMe(GameCanvas.currentScreen);
			break;
		case CMD.CMD_GET_LIST_CLANS:
			list = new Vector();
			final ListScr m_ListScr = new ListScr();
			addTabTopClan(message, list);
			// ListScr.gI().itemHeight = 50;
			m_ListScr.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			m_ListScr.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow
					.getHeight());

			m_ListScr.m_cmdCenter = new Command("", new IAction() {

				@Override
				public void perform() {
					// TODO Auto-generated method stub
					Vector vt = new Vector();
					vt.addElement(new Command(GameResource.join, new IAction() {
						public void perform() {
							Clan selectedClan = (Clan) m_ListScr.getSelectItems();
							if (selectedClan != null) {
								final String clanName = selectedClan.itemName;
								GameCanvas.startOKDlg("Bạn có muốn gia nhập gia tộc " + clanName,
										new IAction() {
											public void perform() {
												GameCanvas.startWaitDlg();
												GlobalService
														.sendMessagePlayerRequestJoinClan(clanName);
											}
										});
							}
							selectedClan = null;
						}
					}));

					vt.addElement(new Command("Chi tiết", new IAction() {

						public void perform() {
							// TODO Auto-generated method stub
							GameCanvas.startWaitDlg();
							Clan selectedClan = (Clan) m_ListScr.getSelectItems();
							GlobalService.sendMessageGetInforClan(selectedClan.itemName);
							ClanInformationScr.gI().setClanInfo(selectedClan);
							selectedClan = null;
						}
					}));

					MenuNhanh.startMenuNhanh(vt, GameCanvas.hw, GameCanvas.h / 2);
				}
			});

			m_ListScr.m_cmdRight = new Command(GameResource.close, new IAction() {
				public void perform() {
					GameCanvas.startWaitDlg();
					m_ListScr.close();
					ClanScr.gI().refeshClanScr();
					ClanScr.gI().switchToMe();
				}
			});

			m_ListScr.m_cmdLeft = new Command("Chi tiết", new IAction() {

				public void perform() {
					// TODO Auto-generated method stub
					GameCanvas.startWaitDlg();
					Clan selectedClan = (Clan) m_ListScr.getSelectItems();
					GlobalService.sendMessageGetInforClan(selectedClan.itemName);
					ClanInformationScr.gI().setClanInfo(selectedClan);
					selectedClan = null;
				}
			});
			m_ListScr.setListItem(list, null, true, false, false);
			m_ListScr.switchToMe(null, "Danh sách gia tộc", null, m_ListScr.m_cmdCenter,
					m_ListScr.m_cmdRight);

			GameCanvas.endDlg();
			break;
		case CMD.CMD_GET_LIST_TOP_CLANS:
			TabScr.gI().removeAll();
			// ///////
			left = null;

			right = new Command(GameResource.close, new IAction() {
				public void perform() {
					GameCanvas.startWaitDlg();
					TabScr.gI().removeAll();
					ClanScr.gI().refeshClanScr();
					ClanScr.gI().switchToMe();
				}
			});

			center = new Command("", new IAction() {

				@Override
				public void perform() {
					// TODO Auto-generated method stub
					Vector vt = new Vector();
					vt.addElement(new Command(GameResource.join, new IAction() {
						public void perform() {
							Clan selectedClan = (Clan) TabScr.gI().getSelectedItem();
							if (selectedClan != null) {
								final String clanName = selectedClan.itemName;
								GameCanvas.startOKDlg("Bạn có muốn gia nhập gia tộc " + clanName,
										new IAction() {
											public void perform() {
												GameCanvas.startWaitDlg();
												GlobalService
														.sendMessagePlayerRequestJoinClan(clanName);
											}
										});
							}
							selectedClan = null;
						}
					}));

					vt.addElement(new Command("Chi tiết", new IAction() {

						public void perform() {
							// TODO Auto-generated method stub
							GameCanvas.startWaitDlg();
							Clan selectedClan = (Clan) TabScr.gI().getSelectedItem();
							GlobalService.sendMessageGetInforClan(selectedClan.itemName);
							ClanInformationScr.gI().setClanInfo(selectedClan);
							selectedClan = null;
						}
					}));

					MenuNhanh.startMenuNhanh(vt, GameCanvas.hw, GameCanvas.h / 2);

				}
			});
			// /////
			list = new Vector();
			addTabTopClan(message, list);
			scrRoom = new ListScr();
			scrRoom.m_cmdCenter = null;
			scrRoom.m_cmdLeft = left;
			scrRoom.m_cmdRight = right;
			scrRoom.m_cmdCenter = center;
			scrRoom.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			scrRoom.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
			scrRoom.setListItem(list, null, false, false, false);
			TabScr.gI().addScreen(scrRoom, "Thành viên");

			list = new Vector();
			addTabTopClan(message, list);
			scrRoom = new ListScr();
			scrRoom.m_cmdCenter = null;
			scrRoom.m_cmdLeft = left;
			scrRoom.m_cmdRight = right;
			scrRoom.m_cmdCenter = center;
			scrRoom.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			scrRoom.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
			scrRoom.setListItem(list, null, false, false, false);
			TabScr.gI().addScreen(scrRoom, "Cấp độ");

			list = new Vector();
			addTabTopClan(message, list);
			scrRoom = new ListScr();
			scrRoom.m_cmdCenter = null;
			scrRoom.m_cmdLeft = left;
			scrRoom.m_cmdRight = right;
			scrRoom.m_cmdCenter = center;
			scrRoom.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
			scrRoom.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
			scrRoom.setListItem(list, null, false, false, false);
			TabScr.gI().addScreen(scrRoom, "Gold");

			TabScr.gI().setActiveScr((Screen) TabScr.gI().m_vtScr.elementAt(0));
			TabScr.gI().m_strTitle = "Thập đại " + GameResource.clan;
			TabScr.gI().switchToMe(GameCanvas.instance.currentScreen);

			list = null;
			scrRoom = null;
			GameCanvas.endDlg();
			System.gc();
			break;
		default:
			if (miniGameMessageHandler != null) {
				miniGameMessageHandler.onMessage(message);
			}
			break;
		}

	}

	public void onConnectOK() {
	}

	public void onConnectionFail() {
		// GameCanvas.startOKDlg(GameResource.checkConnection);
		HDCGameMidlet.instance.showDialog_Okie("Thông báo", GameResource.checkConnection);
	}

	public void onDisconnected() {
		// GameCanvas.startOK(GameResource.disconnect, new IAction() {
		// public void perform() {
		// GameCanvas.loginScr.switchToMe();
		// GameCanvas.endDlg();
		// }
		// });

		HDCGameMidlet.instance.showDialog_Okie_withCommand("Thông báo", GameResource.disconnect,
				new IAction() {
					public void perform() {
						GameCanvas.loginScr.switchToMe();
						GameCanvas.endDlg();
					}
				});
	}

	private void displayInfo(Message message) {
		String content = MessageIO.readString(message);
		// GameCanvas.startOKDlg(content);
		HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);
	}

	private void notifyBeforeCompetition(Message message) {
		String content = MessageIO.readString(message);
		IAction okAction = new IAction() {
			public void perform() {
				GameCanvas.msgdlg.isNoClose = false;
				GlobalService.sendMessageJoinCompetitionRoom();
			}
		};

		IAction noAction = new IAction() {
			public void perform() {
				GameCanvas.msgdlg.isNoClose = false;
				GameCanvas.endDlg();
			}
		};

		GameCanvas.startOKDlg(content, 5, okAction, noAction);
	}

	private void notifyCompetition(Message message) {
		String content = MessageIO.readString(message);
		IAction yesAction = new IAction() {
			public void perform() {
				GameCanvas.msgdlg.isNoClose = false;
				GlobalService.sendMessageReadyCompete();
			}
		};

		IAction noAction = new IAction() {
			public void perform() {
				GameCanvas.msgdlg.isNoClose = false;
				GlobalService.sendMessageCancelCompete();
			}
		};

		GameCanvas.startOKDlg(content, 5, yesAction, noAction);
	}

	public void initializeCompetitionData(Message message) {
		int tableId = MessageIO.readInt(message);
		int roomId = MessageIO.readInt(message);
		int gameId = MessageIO.readInt(message);

		playerJoinGame((byte) gameId);
		PlayGameScr.boardId = tableId;
		PlayGameScr.roomId = roomId;
	}

	private void winCompetitor(Message message) {
		String content = MessageIO.readString(message);
		IAction yesAction = new IAction() {
			public void perform() {
				GameCanvas.msgdlg.isNoClose = false;
				GlobalService.sendMessageGoNextCompetitionRound();
			}
		};

		IAction noAction = new IAction() {
			public void perform() {
				GameCanvas.msgdlg.isNoClose = false;
				GlobalService.sendMessageCancelCompete();
				MainScr.gI().switchToMe();
				System.gc();
			}
		};

		GameCanvas.startOKDlg(content, 5, yesAction, noAction);
	}

	public void loseCompetitor(Message message) {
		String content = MessageIO.readString(message);
		Command cmdClose = new Command("Đóng", new IAction() {
			public void perform() {
				MainScr.gI().switchToMe();
			}
		});
		Vector listCmds = new Vector();
		listCmds.addElement(cmdClose);
		GameCanvas.startOKDlg(content, listCmds);
	}

	public void gameChampion(Message message) {
		String content = MessageIO.readString(message);
		Command cmdClose = new Command("Đóng", new IAction() {
			public void perform() {
				MainScr.gI().switchToMe();
				PlayGameScr.isFireWork = false;
			}
		});

		Vector listCmds = new Vector();
		listCmds.addElement(cmdClose);
		GameCanvas.startOKDlg(content, listCmds);
		PlayGameScr.isFireWork = true;
	}

	public void playerJoinGame(byte gameId) {
		System.gc();
		TienLenScr.clear();
		PhomScr.clear();
		XiToScr.clear();
		BaiCaoScr.clear();
		XiDachScr.clear();
		PokerScr.clear();
		BlackJackScr.clear();

		switch (gameId) {
		case MainScr.PHOM:
			PhomMsgHandler.onHandler();
			curScr = PhomScr.gI();
			break;
		case MainScr.TLMN:
		case MainScr.TLMB:
			TienLenMsgHandler.onHandler();
			curScr = TienLenScr.gI();
			break;
		case MainScr.XITO:
			XiToMsgHandler.onHandler();
			curScr = XiToScr.gI();
			break;
		case MainScr.BAI_CAO:
			BaiCaoMsgHandler.onHandler();
			curScr = BaiCaoScr.gI();
			break;
		case MainScr.XI_DACH:
			XiDachMsgHandler.onHandler();
			curScr = XiDachScr.gI();
			break;
		case MainScr.BACK_JACK:
			BlackJackMsgHandler.onHandler();
			curScr = BlackJackScr.gI();
			break;
		case MainScr.POKER:
			PokerMsgHandler.onHandler();
			curScr = PokerScr.gI();
			break;
		case -1:
			// GameCanvas.startOKDlg("Game đang tạm đóng");
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Game đang tạm đóng");
			break;
		}
	}

	private static void readClanNames(BoardInfo board, Message message) {
		Vector listClanNames = new Vector();
		for (int j = 0; j < board.numberPlayer; j++) {
			String clanName = MessageIO.readString(message);
			if (clanName.length() > 6) {
				clanName = clanName.substring(0, 6) + "..";
			}
			listClanNames.addElement(clanName);
		}
		board.m_listClanNames = listClanNames;
	}

	private void showDialogMessanger() {
		PlayerInfo p = (PlayerInfo) ListScr.gI().getSelectItems();
		final String nick = p.itemName;
		
//		HDCGameMidlet.instance.showDialog_ChuyenGold("Gửi tin nhắn tới " + nick, new IAction() {
//			public void perform() {
//				if (GameCanvas.inputDlg.tfInput.getText().length() > 0) {
//					GameCanvas.startWaitDlg();
//					GlobalService.onSendMessageToUser(HDCGameMidlet.m_myPlayerInfo.itemName, nick,
//							GameCanvas.inputDlg.tfInput.getText());
//				} else {
//					GameCanvas.startOK("Nhập văn bản cần gủi!", new IAction() {
//						public void perform() {
//							showDialogMessanger();
//						}
//					});
//				}
//			}
//		});
		
		GameCanvas.inputDlg.setInfo("Gửi tin nhắn tới " + nick, new IAction() {
			public void perform() {
				if (GameCanvas.inputDlg.tfInput.getText().length() > 0) {
					GameCanvas.startWaitDlg();
					GlobalService.onSendMessageToUser(HDCGameMidlet.m_myPlayerInfo.itemName, nick,
							GameCanvas.inputDlg.tfInput.getText());
				} else {
					GameCanvas.startOK("Nhập văn bản cần gủi!", new IAction() {
						public void perform() {
							showDialogMessanger();
						}
					});
				}
			}
		}, TField.INPUT_TYPE_ANY);
		GameCanvas.inputDlg.show();
		p = null;
	}
	
	private void showDialogMessanger(final ListScr m_ListScr) {
		MailInfo p = (MailInfo) m_ListScr.getSelectItems();
		final String nick = p.sender;
		GameCanvas.inputDlg.setInfo("Gửi tin nhắn tới " + nick, new IAction() {
			public void perform() {
				if (GameCanvas.inputDlg.tfInput.getText().length() > 0) {
					GameCanvas.startWaitDlg();
					GlobalService.onSendMessageToUser(HDCGameMidlet.m_myPlayerInfo.itemName, nick,
							GameCanvas.inputDlg.tfInput.getText());
				} else {
					// GameCanvas.startOK("Nhập văn bản cần gủi!", new IAction()
					// {
					// public void perform() {
					// showDialogMessanger(m_ListScr);
					// }
					// });
					HDCGameMidlet.instance.showDialog_Okie_withCommand("Thông báo",
							"Nhập văn bản cần gủi!", new IAction() {
								public void perform() {
									showDialogMessanger(m_ListScr);
								}
							});
				}
			}
		}, TField.INPUT_TYPE_ANY);
		GameCanvas.inputDlg.show();
		p = null;
	}

	private int readListRooms(Vector vt, Message message) {
		int numberRoom = MessageIO.readInt(message);
		RoomInfo room;
		for (int i = 0; i < numberRoom; i++) {
			room = new RoomInfo();
			room.type = MessageIO.readInt(message);
			room.itemName = MessageIO.readString(message);
			room.itemId = MessageIO.readInt(message);
			room.goldJoinRoom = MessageIO.readLong(message);
			room.status = MessageIO.readByte(message);
			room.minBetGold = MessageIO.readLong(message);
			vt.addElement(room);
		}
		return numberRoom;
	}

	private void readListPlayers(Message message, Vector vt) {
		int total = MessageIO.readByte(message);
		PlayerInfo playerInfo;
		for (int i = 0; i < total; i++) {
			playerInfo = new PlayerInfo();
			playerInfo.itemName = MessageIO.readString(message);
			playerInfo.isChampion = MessageIO.readBoolean(message);
			playerInfo.gold = MessageIO.readLong(message);
			playerInfo.level = MessageIO.readInt(message);
			playerInfo.exp = MessageIO.readInt(message);
			playerInfo.status = MessageIO.readString(message);
			playerInfo.avatarId = MessageIO.readInt(message);
			playerInfo.onlineStatus = MessageIO.readByte(message);
			playerInfo.m_bIsShowMoneyAndLevel = true;
			vt.addElement(playerInfo);
		}
		playerInfo = null;
	}

	private void addTabTopClan(Message message, Vector list) {
		int count = MessageIO.readInt(message);
		Clan clan;
		int i;
		for (i = 0; i < count; i++) {
			clan = new Clan();
			clan.itemName = MessageIO.readString(message);
			clan.money = MessageIO.readLong(message);
			clan.score = MessageIO.readInt(message);
			clan.itemId = MessageIO.readInt(message);
			clan.level = MessageIO.readInt(message);
			clan.totalMembers = MessageIO.readInt(message);
			list.addElement(clan);
		}
		clan = null;
	}

	private void switchToMenuGameScr() {
		DataManager.gI().loadFrameImage();
//		GameCanvas.endDlg();
		
		CustomDialog.instance.gI().endDialog();
		
		MainScr.gI().setSelected(-1);
		MainScr.gI().switchToMe();
		
		Login.instance.flagState = 3;
		Intent intent = new Intent(Login.instance,SelectGame.class);
		Login.instance.startActivity(intent);
		
//		Login.instance.onBackPressed();
	}
}
