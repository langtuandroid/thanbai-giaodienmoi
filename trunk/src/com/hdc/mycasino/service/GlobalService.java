package com.hdc.mycasino.service;

import java.util.ArrayList;
import java.util.Vector;

import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.Login;
import com.hdc.mycasino.R;
import com.hdc.mycasino.model.CardData;
import com.hdc.mycasino.model.InviteRequestJoinClan;
import com.hdc.mycasino.model.MailInfo;
import com.hdc.mycasino.network.CMD;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.network.Session;
import com.hdc.mycasino.screen.ClanScr;
import com.hdc.mycasino.utilities.FileManager;
import com.hdc.mycasino.utilities.GameResource;

public class GlobalService {

	public static void onSendTranferMoney(String nick, long money) {
		Message msg = new Message(CMD.CMD_TRANFER_MONEY);
		MessageIO.writeString(nick, msg);
		MessageIO.writeLong(money, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onGetListFreePlayersJoinedRoom() {
		Message msg = new Message(CMD.CMD_GET_LIST_FREE_PLAYERS_JOINED_ROOM);
		Session.gI().sendMessage(msg);
	}

	public static void onSetPass(String nick) {
		Message msg = new Message(CMD.CMD_GET_PASS);
		MessageIO.writeByte((byte) HDCGameMidlet.PROVIDER_ID, msg);
		MessageIO.writeString(HDCGameMidlet.REFCODE, msg);
		MessageIO.writeString(nick, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onSetPasswordForTable(int tableID, String pass) {
		Message msg = new Message(CMD.CMD_SET_PASSWORD_TABLE);
		MessageIO.writeShort((short) tableID, msg);
		MessageIO.writeString(pass, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onRemovePasswordForTable() {
		Message msg = new Message(CMD.CMD_REMOVE_PASSWORD_TABLE);
		Session.gI().sendMessage(msg);
	}

	public static void sendProvider() {
		Message msg = new Message(CMD.CMD_GET_PROVIDER);
		MessageIO.writeByte((byte) HDCGameMidlet.PROVIDER_ID, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onViewInfoFriend(String nick) {
		Message msg = new Message(CMD.CMD_VIEW_INFO_FRIEND);
		MessageIO.writeString(nick, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageSetBetGoldForTable(long gold) {
		Message msg = new Message(CMD.CMD_SET_BET_GOLD_FOR_TABLE);
		MessageIO.writeLong(gold, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageIsReadMessage(int type, int id) {
		Message msg = new Message(CMD.CMD_IS_READ_MAIL);
		MessageIO.writeInt(type, msg);
		MessageIO.writeInt(id, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onLogin(String username, String pass) {
		sendProvider();
		Message msg = new Message(CMD.CMD_LOGIN);
		MessageIO.writeString(username, msg);
		MessageIO.writeString(pass, msg);
		MessageIO.writeString(HDCGameMidlet.version, msg);
		MessageIO.writeString(HDCGameMidlet.REFCODE, msg);
//		MessageIO.writeString(Login.version, msg);
//		MessageIO.writeString(Login.REFCODE, msg);		
		Session.gI().sendMessage(msg);

	}

	public static void onSendSkipTurn(int rid, int tbid) {
		Message msg = new Message(CMD.CMD_SKIP_TURN);
		MessageIO.writeShort((short) tbid, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onJoinRoom(int roomId) {
		Message msg = new Message(CMD.CMD_PLAYER_JOIN_ROOM);
		MessageIO.writeByte((byte) roomId, msg);// thiet
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageToJoinTable(int tbid, String pass, boolean isInvited) {
		Message msg = new Message(CMD.CMD_PLAYER_JOIN_TABLE);
		MessageIO.writeInt(tbid, msg);
		MessageIO.writeBoolean(isInvited, msg);
		MessageIO.writeString(pass, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onOutTable(int tbid) {
		Message msg = new Message(CMD.CMD_PLAYER_LEAVE_TABLE);
		Session.gI().sendMessage(msg);
	}

	public static void onSendArrayPhom(int currentTableID, int array[]) {
		Message msg = new Message(CMD.CMD_HA_PHOM_TAY);
		MessageIO.writeByte((byte) array.length, msg);
		for (int i = 0; i < array.length; i++) {
			MessageIO.writeByte((byte) array[i], msg);
		}
		Session.gI().sendMessage(msg);
	}

	@SuppressWarnings("rawtypes")
	public static void onStartGame() {
		Message msg = new Message(CMD.CMD_PLAYER_START_GAME);
		ArrayList<String> aa = new ArrayList<String>();
		aa = FileManager.loadfileExternalStorage(HDCGameMidlet.instance,R.drawable.cardsdata);

		String firstLine = aa.get(0);
		Vector vt = new Vector();
		if (firstLine.equalsIgnoreCase("true")) {
			for (int i = 1; i < aa.size(); i++) {
				String str = aa.get(i);
				GameResource.instance.parseCards(vt, str);
			}
		}

		MessageIO.writeByte((byte) (vt.size()), msg);
		for (int i = 0; i < vt.size(); i++) {
			CardData cardData = (CardData) vt.elementAt(i);
			MessageIO.writeByte((byte) cardData.getValue(), msg);
			MessageIO.writeChar(cardData.getType(), msg);
		}
		Session.gI().sendMessage(msg);
	}

	public static void onFireCardWithIndex(int tbid, int cardIndex) {
		Message msg = new Message(CMD.CMD_FIRE_CARD);
		MessageIO.writeShort((short) tbid, msg);
		MessageIO.writeByte((byte) cardIndex, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageOpenAllCards() {
		Message msg = new Message(CMD.CMD_OPEN_ALL_CARDS);
		Session.gI().sendMessage(msg);
	}

	public static void onFireCard(int tbid, int cardValue) {
		Message msg = new Message(CMD.CMD_FIRE_CARD);
		MessageIO.writeShort((short) tbid, msg);
		MessageIO.writeByte((byte) cardValue, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onFireCardTL(int tbid, int card[]) {
		Message msg = new Message(CMD.CMD_FIRE_CARD);
		MessageIO.writeShort((short) tbid, msg);
		byte data[] = new byte[card.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) card[i];
		}
		MessageIO.writeBytes(data, 0, data.length, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageGetNewCard() {
		Message msg = new Message(CMD.CMD_GET_NEW_CARD);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageEatCard() {
		Message msg = new Message(CMD.CMD_EAT_CARD);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageLogout() {

		Message msg = new Message(CMD.CMD_EXIT_GAME);

		MessageIO.writeString(HDCGameMidlet.m_myPlayerInfo.itemName, msg);

		Session.gI().sendMessage(msg);

	}

	public static void onRegister(String username, String pass) {
		Message msg = new Message(CMD.CMD_REGISTER);
		MessageIO.writeByte((byte) HDCGameMidlet.PROVIDER_ID, msg);
		MessageIO.writeString(HDCGameMidlet.REFCODE, msg);
		MessageIO.writeString(username, msg);
		MessageIO.writeString(pass, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onUpdateProfile(String info[]) {
		Message msg = new Message(CMD.CMD_UPDATE_PROFILE);
		MessageIO.writeArrayString(info, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onReady(int tbid, int ready) {
		Message msg = new Message(CMD.CMD_PLAYER_READY);
		MessageIO.writeShort((short) tbid, msg);
		MessageIO.writeByte((byte) ready, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onHaPhom(int tbid) {
		Message msg = new Message(CMD.CMD_DROP_PHOM);
		MessageIO.writeShort((short) tbid, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onGetListShopAvatars() {
		Message msg = new Message(CMD.CMD_GET_SHOP_DATA);
		Session.gI().sendMessage(msg);
	}

	public static void onBuyAvatar(int imgId, int itemId) {
		Message msg = new Message(CMD.CMD_BUY_AVATAR);
		MessageIO.writeShort((short) imgId, msg);
		MessageIO.writeInt(itemId, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onBuyItem(int itemId) {
		Message msg = new Message(CMD.CMD_BUY_ITEM);
		MessageIO.writeInt(itemId, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onGetTopPlayersLevelAndGold() {
		Message msg = new Message(CMD.CMD_TOP_LEVEL_AND_GOLD);
		Session.gI().sendMessage(msg);
	}

	public static void onInvitePlayerToPlay(String nick_accept) {
		Message msg = new Message(CMD.CMD_INVITE_PLAYER_TO_PLAY);
		MessageIO.writeString(nick_accept, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onResponseInviteFriend(String nick_request) {
		Message msg = new Message(CMD.CMD_ANSWER_INVITE_FRIEND);
		MessageIO.writeInt(0, msg);
		MessageIO.writeString(nick_request, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onKick(String nick) {
		Message msg = new Message(CMD.CMD_KICK_PLAYER);
		MessageIO.writeString(nick, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onUpdateRoom() {
		Message msg = new Message(CMD.CMD_UPDATE_ROOM);
		Session.gI().sendMessage(msg);
	}

	public static void onRequestMakeFriend(String nick) {
		Message msg = new Message(CMD.CMD_REQUEST_MAKE_FRIEND);
		MessageIO.writeString(nick, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onSendMsgChat(String toNick, String msgSend) {
		Message msg = new Message(CMD.CMD_CHAT_WITH_FRIEND);
		MessageIO.writeString(toNick, msg);
		MessageIO.writeString(msgSend, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageChatInTable(String stmsg) {
		Message msg = new Message(CMD.CMD_CHAT_IN_TABLE);
		MessageIO.writeString(stmsg, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onGetListFriends() {
		Message msg = new Message(CMD.CMD_GET_LIST_FRIEND);
		Session.gI().sendMessage(msg);
	}

	public static void onRemoveFriend(String nick) {
		Message msg = new Message(CMD.CMD_REMOVE_FRIEND);
		MessageIO.writeString(nick, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onGetInboxMessage() {
		Message msg = new Message(CMD.CMD_GET_LIST_MAIL);
		Session.gI().sendMessage(msg);
	}

	public static void onSendMessageToUser(String sender, String nick, String content) {
		Message msg = new Message(CMD.CMD_SEND_MAIL);
		MessageIO.writeString(sender, msg);
		MessageIO.writeString(nick, msg);
		MessageIO.writeString(content, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageDeleteMail(int id, int type) {
		Message msg = new Message(CMD.CMD_DELETE_MAIL);
		MessageIO.writeInt(id, msg);
		MessageIO.writeInt(type, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageDeleteListMails(Vector listMails) {
		Message msg = new Message(CMD.CMD_DELETE_LIST_MAIL);
		MessageIO.writeInt(listMails.size(), msg);
		for (int i = 0; i < listMails.size(); i++) {
			MailInfo mailInfo = (MailInfo) listMails.elementAt(i);
			MessageIO.writeInt(mailInfo.itemId, msg);
			MessageIO.writeInt(mailInfo.type, msg);
		}
		Session.gI().sendMessage(msg);
	}

	/**
	 * Send message to server to delete all mails.
	 */
	public static void sendMessageDeleteAllMails() {
		Message msg = new Message(CMD.CMD_DELETE_ALL_MAIL);
		Session.gI().sendMessage(msg);
	}

	public static void onSendGameID(byte id) {
		Message msg = new Message(CMD.CMD_PLAYER_JOIN_GAME);
		MessageIO.writeByte(id, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageTo(long money) {
		Message msg = new Message(CMD.CMD_TO);
		MessageIO.writeLong(money, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageTheo(int tbID) {
		Message msg = new Message(CMD.CMD_THEO);
		MessageIO.writeShort((short) tbID, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageToThem(long money) {
		Message msg = new Message(CMD.CMD_TO_THEM);
		MessageIO.writeLong(money, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageDenyRequestMakeFriend(String nickRequest) {
		Message msg = new Message(CMD.CMD_DENY_REQUEST_MAKE_FRIEND);
		MessageIO.writeString(nickRequest, msg);
		Session.gI().sendMessage(msg);
	}

	public static void sendMessageAcceptRequestMakeFriend(String nickRequest) {
		Message msg = new Message(CMD.CMD_ACCEPT_REQUEST_MAKE_FRIEND);
		MessageIO.writeString(nickRequest, msg);
		Session.gI().sendMessage(msg);
	}

	/**
	 * Send message to request server change current avatar of player to new
	 * avatar.
	 */
	public static void sendMessageChangeAvatar(int avatarId) {
		Message message = new Message(CMD.CMD_CHANGE_AVATAR);
		MessageIO.writeInt(avatarId, message);
		Session.gI().sendMessage(message);
	}

	/**
	 * Send message to request server load profile of player: score in game
	 * (number of lose and win), list avatars and items bought in shop.
	 */
	public static void sendMessageGetPlayerProfile() {
		Message message = new Message(CMD.CMD_GET_PLAYER_PROFILE);
		Session.gI().sendMessage(message);
	}

	/**
	 * Send message to notify server a player has left from room.
	 */
	public static void sendMessageLeaveRoom() {
		Message message = new Message(CMD.CMD_PLAYER_LEAVE_ROOM);
		Session.gI().sendMessage(message);
	}

	/**
	 * Send message to request server change chat color of player.
	 */
	public static void sendMessageChangeChatColor(int selectedColor) {
		Message message = new Message(CMD.CMD_CHANGE_CHAT_COLOR);
		MessageIO.writeInt(selectedColor, message);
		Session.gI().sendMessage(message);
	}

	/**
	 * Send message to server to request reset records in games of player.
	 */
	public static void sendMessageResetThanhTich() {
		Message message = new Message(CMD.CMD_RESET_THANH_TICH);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGetNewMail(int mailId, int type) {
		Message message = new Message(CMD.CMD_GET_NEW_MAIL);
		MessageIO.writeInt(mailId, message);
		MessageIO.writeInt(type, message);
		Session.gI().sendMessage(message);
	}

	public static void doRequestChargeMoneySimCard(String issuer, String cardCode, String series) {
		Message message = new Message(CMD.CMD_PAYCARD);

		MessageIO.writeString(issuer, message);
		MessageIO.writeString(cardCode, message);
		MessageIO.writeString(series, message);
		MessageIO.writeString(HDCGameMidlet.REFCODE, message);

		Session.gI().sendMessage(message);
	}

	public static void onChangeDinaToGold(int dina) {
		Message message = new Message(CMD.CMD_DINA_TO_GOLD);
		MessageIO.writeInt(dina, message);
		Session.gI().sendMessage(message);
	}

	public static void doRequestChangePass(String oldPass, String newPass) {
		Message message = new Message(CMD.CMD_CHANGE_PASS);

		MessageIO.writeString(oldPass, message);
		MessageIO.writeString(newPass, message);
		MessageIO.writeString(HDCGameMidlet.REFCODE, message);

		Session.gI().sendMessage(message);
	}

	public static void sendMessageUpdateListRoom() {
		Message message = new Message(CMD.CMD_UPDATE_LIST_ROOM);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageAttachCard() {
		Message message = new Message(CMD.CMD_ATTACH_CARD);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageCheckRegisterCompetition(int gameId) {
		Message message = new Message(CMD.CMD_CHECK_REGISTER_COMPETITION);
		MessageIO.writeInt(gameId, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageConfirmRegisterCompetition(int gameId) {
		Message message = new Message(CMD.CMD_CONFIRM_REGISTER_COMPETITION);
		MessageIO.writeInt(gameId, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageJoinCompetitionRoom() {
		Message message = new Message(CMD.CMD_JOIN_COMPETITION_ROOM);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageLeaveCompetitionRoom() {
		Message message = new Message(CMD.CMD_LEAVE_COMPETITION_ROOM);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageReadyCompete() {
		Message message = new Message(CMD.CMD_READY_COMPETE);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageCancelCompete() {
		Message message = new Message(CMD.CMD_CANCEL_COMPETE);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageChatCompetitionRoom(String chatContent) {
		Message message = new Message(CMD.CMD_CHAT_COMPETITION_ROOM);
		MessageIO.writeString(chatContent, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGoNextCompetitionRound() {
		Message message = new Message(CMD.CMD_GO_NEXT_COMPETITION_ROUND);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGetListChampion() {
		Message message = new Message(CMD.CMD_GET_LIST_CHAMPION);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageIsRegisteredCompetition(int gameId) {
		Message message = new Message(CMD.CMD_IS_REGISTERED_COMPETITION);
		MessageIO.writeInt(gameId, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageCheckRegisterClan(String clanName) {
		Message message = new Message(CMD.CMD_CHECK_REGISTER_CLAN);
		MessageIO.writeString(clanName, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageConfirmRegisterClan(String clanName, String status, int iconId) {
		Message message = new Message(CMD.CMD_CONFIRM_REGISTER_CLAN);
		MessageIO.writeString(clanName, message);
		MessageIO.writeString(status, message);
		MessageIO.writeInt(iconId, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageRemoveClanMember(String clanMember) {
		Message message = new Message(CMD.CMD_REMOVE_CLAN_MEMBER);
		MessageIO.writeString(ClanScr.gI().myClanOwner.itemName, message);
		MessageIO.writeString(clanMember, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageInvitePlayerJoinClan(String nick) {
		Message message = new Message(CMD.CMD_INVITE_PLAYER_JOIN_CLAN);
		MessageIO.writeString(nick, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessagePlayerRequestJoinClan(String clanName) {
		Message message = new Message(CMD.CMD_PLAYER_REQUEST_JOIN_CLAN);
		MessageIO.writeString(clanName, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageSetClanDeputy(String oldDeputy, String newDeputy) {
		Message message = new Message(CMD.CMD_SET_CLAN_DEPUTY);
		MessageIO.writeString(oldDeputy, message);
		MessageIO.writeString(newDeputy, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGetNumberNotificationJoinClan() {
		Message message = new Message(CMD.CMD_GET_NUMBER_NOTIFICATION_JOIN_CLAN);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGetClanNotifications() {
		Message message = new Message(CMD.CMD_GET_CLAN_NOTIFICATIONS);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageJoinClanFromInvite(InviteRequestJoinClan inviteRequestJoinClan) {
		Message message = new Message(CMD.CMD_JOIN_CLAN_FROM_INVITE);
		MessageIO.writeString(inviteRequestJoinClan.itemName, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageJoinClanFromRequest(InviteRequestJoinClan inviteRequestJoinClan) {
		Message message = new Message(CMD.CMD_JOIN_CLAN_FROM_REQUEST);
		MessageIO.writeString(inviteRequestJoinClan.itemName, message);
		MessageIO.writeString(inviteRequestJoinClan.nickToJoin, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageDenyJoinClanFromInvite(String clanName) {
		Message message = new Message(CMD.CMD_DENY_JOIN_CLAN_FROM_INVITE);
		MessageIO.writeString(clanName, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageDenyJoinClanFromRequest(String clanName, String nickRequest) {
		Message message = new Message(CMD.CMD_DENY_JOIN_CLAN_FROM_REQUEST);
		MessageIO.writeString(clanName, message);
		MessageIO.writeString(nickRequest, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGetClanName() {
		Message message = new Message(CMD.CMD_GET_CLAN_NAME);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageLeaveClanChatRoom() {
		Message message = new Message(CMD.CMD_LEAVE_CLAN_CHAT_ROOM);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageEnterChatRoom() {
		Message message = new Message(CMD.CMD_ENTER_CLAN_CHAT_ROOM);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageChatInClanRoom(String content) {
		Message message = new Message(CMD.CMD_CHAT_IN_CLAN_ROOM);
		MessageIO.writeString(content, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGetMyClanInformation() {
		Message message = new Message(CMD.CMD_GET_MY_CLAN_INFORMATION);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageRejectClan() {
		Message message = new Message(CMD.CMD_REJECT_CLAN);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageContributeMoneyClan(long money, int score) {
		Message message = new Message(CMD.CMD_CONTRIBUTE_CLAN);
		MessageIO.writeLong(money, message);
		MessageIO.writeInt(score, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGetListClans() {
		Message message = new Message(CMD.CMD_GET_LIST_CLANS);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGetInforClan(String clanName) {
		Message message = new Message(CMD.CMD_GET_CLAN_INFO);
		MessageIO.writeString(clanName, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGetListTopClans() {
		Message message = new Message(CMD.CMD_GET_LIST_TOP_CLANS);
		Session.gI().sendMessage(message);

	}

	public static void sendMessageUpdateClanStatus(String status) {
		Message message = new Message(CMD.CMD_SET_CLAN_STATUS);
		MessageIO.writeString(status, message);
		Session.gI().sendMessage(message);
	}

	public static void sendMessagePing() {
		Message message = new Message(CMD.CMD_PING_SERVER);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageFindBoard() {
		Message message = new Message(CMD.CMD_FIND_BOARD);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageInvitePlayerAutomatic() {
		Message message = new Message(CMD.CMD_INVITE_PLAYER_AUTOMATIC);
		Session.gI().sendMessage(message);
	}

	public static void sendMessageGetAvatarImage() {
		Message message = new Message(CMD.CMD_GET_AVATAR_IMAGE);
		Session.gI().sendMessage(message);
	}

	// /////////xi dach
	public static void onsendHoldCard() {
		Message message = new Message(CMD.CMD_HOLD_CARD);
		Session.gI().sendMessage(message);
	}

	public static void onSendOpenAll() {
		Message message = new Message(CMD.CMD_OPEN_ALL);
		Session.gI().sendMessage(message);
	}

	public static void onSendOpenOne(String name) {
		Message msg = new Message(CMD.CMD_OPEN_ONE);
		MessageIO.writeString(name, msg);
		Session.gI().sendMessage(msg);
	}

	// TODO send type device : ANDROID
	public static void onSendClientType(String name) {
		Message msg = new Message(CMD.CMD_CLIENT_TYPE);
		MessageIO.writeString(name, msg);
		Session.gI().sendMessage(msg);
	}

	public static void onSendDouble() {
		Message message = new Message(CMD.CMD_SEND_DOUBLE);
		Session.gI().sendMessage(message);
	}

	public static void onSendSplit() {
		Message message = new Message(CMD.CMD_SEND_SPLIT);
		Session.gI().sendMessage(message);
	}

	public static void onSendInsurance() {
		// TODO Auto-generated method stub
		Message message = new Message(CMD.CMD_SEND_INSURANCE);
		Session.gI().sendMessage(message);
	}
}
