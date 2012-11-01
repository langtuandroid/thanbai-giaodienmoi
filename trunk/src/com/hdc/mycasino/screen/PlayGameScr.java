package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.animation.ExplosionStar;
import com.hdc.mycasino.animation.FlyImage;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.gif.GifView;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.PlayerInfo;
import com.hdc.mycasino.model.Position;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.DetailImage;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.TField;

public abstract class PlayGameScr extends Screen implements IChatable {
	public static final byte TURN_TIME_OUT = 30;

	public PlayerInfo[] m_arrPlayers;

	public Position[] m_arrPos;

	public byte m_iMaxPlayer = 2;
	public byte m_iMaxNumCard = 2;
	public byte m_bMyReady = 0;

	public boolean m_bIsPlaying = false;
	public boolean m_bIsWaitingBeforPlay = true;
	public boolean isCompetition;
	public static boolean isFireWork = false;
	public boolean m_bIsDisable = false;
	boolean paintSubFire = false;

	public int m_iUseWCard;
	private int m_iTickFireWork = 0;
	public static int m_iGameTick;
	private int m_iTickSetMoney = 0;
	public int m_iColorMoney = 0xffff00;

	public static int roomId;
	public static int boardId;
	public static int roomCode;
	public static byte m_iStatusOutGame = -1;// 0 là tự người choi out
												// 1 là bị kick
												// 2 nguoi choi tu thoat khi
												// dang
												// choi game

	public String m_strOwnerName;

	public int m_iMyPosInVt;
	public long betGoldOfTable;

	public Command giveUpCmd;
	protected Command beginCmd;
	protected Command readyCmd;
	protected Command menuNormalCmd;
	protected Command menuOwnerCmd;
	protected Command makeFriendCmd;
	protected Command helpCmd;
	protected Command waitingCmd;
	protected Command waitCmd;

	// public Command centerCmd;
	// public Command rightCmd;

	protected TField m_tfChatFromMe;

	String m_strWhoTurn = "";

	Position[] posFire = new Position[100];
	Position pos = new Position();
	ExplosionStar[] star = new ExplosionStar[10];

	long lastTimeChat;
	long now;
	int countTime_Chat = 0;

	// TODO index icon start or ready
	int idxIcon_Start_Ready;
	// TODO flag "Xin chờ - sẵn sàng - bắt đầu - không sẵn sàng"
	// flag = 0 : xin chờ
	// flag = 1 : bắt đầu
	// flag = 2 : sẵn sàng
	// flag = 3 : không sẵn sàng
	int flagStart;
	FrameImage imgStart = GameResource.instance.m_frameTienLen_Start_Ready_2;
	Image imgTable;

	// khởi tạo mảng card cho tất cả nguoi trong game;
	public Card[][] m_arrOtherCards;
	// kiểm tra tình trạng chia bài
	protected boolean m_bIsStartDeliver = false;
	protected boolean m_bIsEndDeliver = false;
	// diếm các dối tuợng trong vị trí
	protected byte m_bCountAnchor = 0;

	// TODO menu bg
	Image imgMenuBg;
	int imgMenuBg_w, imgMenuBg_h;
	int typeMenu;
	int idx_MenuButton = -1;
	// TODO context menu
	int flag_ContextMenu = 0;
	int xContextMenu = 0;
	int yContextMenu = 0;
	int countTime_ContextMenu = -1;
	int idx_ContextMenu = -1;
	int idx_player = -1;
	// TODO view info
	public static PlayerInfo m_ViewInfo;
	public static boolean showViewInfo = false;
	public int flagContextMenu_Close = 1;

	// TODO hide menubutton
	protected boolean showMenuButton = true;

	// TODO button back
	public int flagButton_Back = 0;

	// NEW
	Command m_cmdMail;
	Command m_cmdChat;
	Command m_cmdDatTienCuoc;
	Command m_cmdDatMatKhau;
	Command m_cmdBoMatKhau;
	Command m_cmdTuDongTimNgChoi;

	// Image Context menu
	Image imgContextMenu;

	int degree = 0;

	// TODO hight light chon bai
	private int jump = 0;

	public GifView[] m_arrGif;
	public int[] m_countTime_Chat;

	public PlayGameScr(byte maxPlayer, byte maxNumCard) {

		m_cmdRight = new Command("Rời bàn", new IAction() {

			public void perform() {
				if (m_bIsPlaying) {
					GameCanvas.startOKDlg(
							"Nếu thoát khi đang chơi bạn sẽ bị xử thua. Bạn có muốn thoát",
							new IAction() {
								public void perform() {
									GameCanvas.endDlg();
									m_iStatusOutGame = 2;
									doLeaveBoard();
								}
							});
				} else {
					m_iStatusOutGame = 0;
					doLeaveBoard();
				}
			}
		});

		helpCmd = new Command("Hướng dẫn", new IAction() {

			public void perform() {

			}
		});

		makeFriendCmd = new Command(GameResource.makeFriend, new IAction() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void perform() {
				Vector v = new Vector();

				int i;
				PlayerInfo p;
				for (i = 0; i < m_arrPlayers.length; i++) {
					if (m_arrPlayers[i].itemId != -1) {
						p = m_arrPlayers[i];
						if (!p.itemName.equals(HDCGameMidlet.m_myPlayerInfo.itemName)
								&& p.itemName.length() > 0) {
							final String playerName = p.itemName;
							v.addElement(new Command(playerName, new IAction() {
								public void perform() {
									GlobalService.onRequestMakeFriend(playerName);
								}
							}));
						}
					}
				}

				if (v.size() > 0)
					GameCanvas.instance.menu.startAt(v, 0);
				else
					// GameCanvas.startOKDlg("Không có người chơi");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Không có người chơi");
				v = null;
				p = null;
			}
		});

		giveUpCmd = new Command("Bỏ cuộc", new IAction() {
			public void perform() {
				GameCanvas.startOKDlg("Bạn có muốn bỏ cuộc?", new IAction() {
					public void perform() {
						stopGame();
						GlobalService.onOutTable(boardId);
						MainScr.gI().switchToMe();
					}
				});
			}
		});

		// init command
		beginCmd = new Command("Bắt đầu", new IAction() {
			public void perform() {
				// saveCmd(m_cmdCenter, m_cmdRight);
				flagStart = 4;
				GlobalService.onStartGame();
			}
		});

		readyCmd = new Command("Sẵn sàng", new IAction() {
			public void perform() {
				if (m_bIsPlaying)
					// GameCanvas.startOKDlg("Ván đang chơi, không thể sẵn sàng");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",
							"Ván đang chơi, không thể sẵn sàng");
				else {
					if (readyCmd.caption.equals("Sẵn sàng")) {
						flagStart = 3;
					} else {
						flagStart = 2;
					}
					// saveCmd(m_cmdCenter, m_cmdRight);
					actionEndGame();
					m_bMyReady = (byte) (1 - m_bMyReady);
					GlobalService.onReady(boardId, m_bMyReady);
				}
			}
		});

		// NEW
		m_cmdMail = new Command("Mail", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				GameCanvas.startWaitDlg();
				GlobalService.onGetInboxMessage();
			}
		});

		m_cmdChat = new Command("Chat", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				int m_Chat_x = 0;
				int m_Chat_y = 0;
				for (int i = 0; i < m_arrPos.length; i++) {
					if (m_arrPos[i].anchor == 0) {
						m_Chat_x = m_arrPos[i].x;
						m_Chat_y = m_arrPos[i].y;
						break;
					}
				}

				HDCGameMidlet.instance.showDialog_Chat_Emoticion("Chat", "", m_Chat_x, m_Chat_y);
				countTime_Chat = 1;

				// m_tfChatFromMe.setText(HDCGameMidlet.instance.content_Chat);
				// m_tfChatFromMe.update();
			}
		});

		m_cmdDatTienCuoc = new Command("Đặt tiền cược", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				HDCGameMidlet.instance.showDialog_DatTienCuoc();
			}
		});

		m_cmdDatMatKhau = new Command("Đặt mật khẩu", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				HDCGameMidlet.instance.showDialog_DatMatKhau(boardId);
			}
		});

		m_cmdBoMatKhau = new Command("Bỏ mật khẩu", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				GlobalService.onRemovePasswordForTable();
			}
		});

		m_cmdTuDongTimNgChoi = new Command("Tìm ng chơi", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				GlobalService.sendMessageInvitePlayerAutomatic();
			}
		});

		// END NEW

		menuNormalCmd = new Command(GameResource.menu, new IAction() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void perform() {
				Vector v = new Vector();

				if (countPlayer() > 1)
					v.addElement(makeFriendCmd);

				v.addElement(new Command(GameResource.mail, new IAction() {
					public void perform() {
						GameCanvas.startWaitDlg();
						GlobalService.onGetInboxMessage();
					}
				}));

				v.addElement(new Command("Xem thông tin", new IAction() {

					public void perform() {
						int i;
						Vector vt = new Vector();
						PlayerInfo p;
						for (i = 0; i < m_arrPlayers.length; i++) {
							if (m_arrPlayers[i].itemId != -1) {
								p = m_arrPlayers[i];
								final String strName = p.itemName;
								if (!strName.equals("") && strName.length() > 0
										&& !strName.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
									Command c = new Command(p.itemName, new IAction() {
										public void perform() {
											GlobalService.onViewInfoFriend(strName);
										}
									});
									vt.addElement(c);
								}
							}
						}
						if (vt.size() > 0)
							GameCanvas.instance.menu.startAt(vt, 0);
						else
							// GameCanvas.startOKDlg("Không có người chơi");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo",
									"Không có người chơi");
						vt = null;
						p = null;
					}
				}));

				v.addElement(new Command("Rời bàn", new IAction() {

					public void perform() {
						if (m_bIsPlaying) {
							GameCanvas.startOKDlg(
									"Nếu thoát khi đang chơi bạn sẽ bị xử thua. Bạn có muốn thoát",
									new IAction() {
										public void perform() {
											GameCanvas.endDlg();
											m_iStatusOutGame = 2;
											doLeaveBoard();
										}
									});
						} else {
							m_iStatusOutGame = 0;
							doLeaveBoard();
						}
					}
				}));

				GameCanvas.instance.menu.startAt(v, 0);
				v = null;
			}
		});

		menuOwnerCmd = new Command(GameResource.menu, new IAction() {

			@SuppressWarnings("unchecked")
			public void perform() {
				Vector listCmd = new Vector();

				listCmd.addElement(new Command(GameResource.mail, new IAction() {
					public void perform() {
						GameCanvas.startWaitDlg();
						GlobalService.onGetInboxMessage();
					}
				}));

				listCmd.addElement(new Command("Đặt tiền cược", new IAction() {
					public void perform() {
						GameCanvas.inputDlg.setInfo("Tiền cược", new IAction() {
							public void perform() {
								long gold;
								try {
									gold = Long.parseLong(GameCanvas.inputDlg.tfInput.getText());
								} catch (Exception ex) {
									// GameCanvas.startOKDlg(GameResource.invalid);
									HDCGameMidlet.instance.showDialog_Okie("Thông báo",
											GameResource.invalid);
									return;
								}
								if (gold >= 0)
									GlobalService.sendMessageSetBetGoldForTable(gold);
								else
									// GameCanvas.startOKDlg(GameResource.moneyInvalid);
									HDCGameMidlet.instance.showDialog_Okie("Thông báo",
											GameResource.moneyInvalid);
							}
						}, TField.INPUT_TYPE_NUMERIC);
						GameCanvas.inputDlg.tfInput.setText("100");
						GameCanvas.inputDlg.show();
					}
				}));

				listCmd.addElement(new Command("Đặt mật khẩu", new IAction() {
					public void perform() {
						GameCanvas.inputDlg.setInfo("Mật khẩu", new IAction() {
							public void perform() {
								if (GameCanvas.inputDlg.tfInput.getText() != "") {
									GlobalService.onSetPasswordForTable(boardId,
											GameCanvas.inputDlg.tfInput.getText());
									GameCanvas.inputDlg.tfInput.setText("");
									GameCanvas.inputDlg.tfInput.setIputType(TField.INPUT_TYPE_ANY);
								}
								GameCanvas.endDlg();
							}
						}, TField.INPUT_TYPE_PASSWORD);
						GameCanvas.inputDlg.show();
					}
				}));

				listCmd.addElement(new Command("Bỏ mật khẩu", new IAction() {
					public void perform() {
						GlobalService.onRemovePasswordForTable();
					}
				}));

				// listCmd.addElement(new Command("Cài đặt bàn", new IAction() {
				// public void perform() {
				// }
				// }, room));
				// room = null;

				listCmd.addElement(new Command("Tự động", new IAction() {
					public void perform() {
						GlobalService.sendMessageInvitePlayerAutomatic();
					}
				}));

				// listCmd.addElement(new Command("Theo danh sách", new
				// IAction() {
				// public void perform() {
				// GameCanvas.startWaitDlg();
				// GlobalService.onGetListFreePlayersJoinedRoom();
				// }
				// }));

				if (countPlayer() > 1) {
					listCmd.addElement(makeFriendCmd);
				}

				if (!m_bIsPlaying) {
					listCmd.addElement(new Command("Kick", new IAction() {
						public void perform() {
							int i;
							Vector vt = new Vector();
							PlayerInfo p;
							for (i = 0; i < m_arrPlayers.length; i++) {
								if (m_arrPlayers[i].itemId != -1) {
									p = m_arrPlayers[i];
									final String strName = p.itemName;
									if (!strName.equals("") && strName.length() > 0
											&& !strName.equals(m_strOwnerName)) {
										Command c = new Command(p.itemName, new IAction() {
											public void perform() {
												GlobalService.onKick(strName);
											}
										});

										vt.addElement(c);
									}
								}
							}
							if (vt.size() > 0)
								GameCanvas.instance.menu.startAt(vt, 0);
							else
								// GameCanvas.startOKDlg("Không có người chơi");
								HDCGameMidlet.instance.showDialog_Okie("Thông báo",
										"Không có người chơi");
							vt = null;
							p = null;
						}
					}));
				}

				listCmd.addElement(new Command("Rời bàn", new IAction() {

					public void perform() {
						if (m_bIsPlaying) {
							GameCanvas.startOKDlg(
									"Nếu thoát khi đang chơi bạn sẽ bị xử thua. Bạn có muốn thoát",
									new IAction() {
										public void perform() {
											m_iStatusOutGame = 2;
											GameCanvas.endDlg();
											doLeaveBoard();
										}
									});
						} else {
							m_iStatusOutGame = 0;
							doLeaveBoard();
						}
					}

				}));

				GameCanvas.instance.menu.startAt(listCmd, 0);
				listCmd = null;
			}
		});

		waitingCmd = new Command("Chờ", new IAction() {
			public void perform() {
				actionEndGame();
			}
		});

		waitCmd = new Command("Chờ", null);

		m_iMaxPlayer = maxPlayer;
		m_iMaxNumCard = maxNumCard;

		m_arrPos = new Position[m_iMaxPlayer];
		m_arrPlayers = new PlayerInfo[m_iMaxPlayer];

		int i;
		for (i = 0; i < m_iMaxPlayer; i++) {
			m_arrPlayers[i] = new PlayerInfo();
			m_arrPlayers[i].itemId = -1;
			m_arrPlayers[i].itemName = "";

			m_arrPos[i] = new Position();
		}

		m_bIsPlaying = false;

		m_iUseWCard = DetailImage.imgCard_w / 3 * 2;

		m_tfChatFromMe = new TField();
		m_tfChatFromMe.x = /*
							 * GameResource.instance.imgTienLen_Chat.getWidth()
							 * / 4
							 */GameCanvas.w - GameResource.instance.m_frameMenuBgGame.frameWidth;
		m_tfChatFromMe.y = /*
							 * GameResource.instance.imgHeaderBg.getHeight() / 4
							 * * 5
							 */GameResource.instance.m_frameMenuBgGame.frameHeight;
		m_tfChatFromMe.width = /*
								 * GameResource.instance.imgTienLen_Chat.getWidth
								 * () / 2 * 3
								 */GameResource.instance.m_frameMenuBgGame.frameWidth;
		m_tfChatFromMe.height = // GameResource.instance.imgTienLen_Chat
		// .getHeight()
		// + GameResource.instance.imgHeaderBg.getHeight()
		// / 2
		GameResource.instance.m_frameMenuBgGame.frameHeight;
		m_tfChatFromMe.isFocus = true;
		m_tfChatFromMe.setIputType(TField.INPUT_TYPE_ANY);
		m_tfChatFromMe.setText("");

		lastTimeChat = System.currentTimeMillis();

		// init all card in game
		int j;
		m_arrOtherCards = new Card[maxPlayer][maxNumCard];
		for (i = 0; i < maxPlayer; i++)
			for (j = 0; j < maxNumCard; j++) {
				m_arrOtherCards[i][j] = new Card(-1);
				m_arrOtherCards[i][j].setScale(1.5f);
			}
	}

	public PlayerInfo findPlayer(String playerName) {
		int i, count = m_arrPlayers.length;
		PlayerInfo p;
		for (i = 0; i < count; i++) {
			if (m_arrPlayers[i].itemId != -1) {
				p = m_arrPlayers[i];
				if (p.itemName.equals(playerName))
					return p;
			}
		}
		return null;
	}

	public int findPlayerPos(String playerName) {
		int i, count = m_arrPlayers.length;
		PlayerInfo p;
		for (i = 0; i < count; i++) {
			if (m_arrPlayers[i].itemId != -1) {
				p = m_arrPlayers[i];
				if (p.itemName.equals(playerName))
					return i;
			}
		}
		return -1;
	}

	public void close() {
		int i;
		for (i = 0; i < m_arrPlayers.length; i++) {
			m_arrPlayers[i].itemId = -1;
			m_arrPlayers[i].itemName = "";
		}
	}

	// chuyển dổi trạng thái vị trí nằm trên hình vẻ sang vị trí trong mảng
	protected int switchAnchorToPos(int anchor) {
		for (int i = 0; i < m_iMaxPlayer; i++) {
			if (m_arrPos[i].anchor == anchor)
				return i;
		}
		return -1;
	}

	// update duy chuyển cho tất cả quân bài khi chia bài
	protected void updateForOrtherCard() {
		int i, j;
		for (i = 0; i < m_iMaxPlayer; i++)
			for (j = 0; j < m_iMaxNumCard; j++)
				if (m_arrOtherCards[i][j].m_id != -1 && !m_arrOtherCards[i][j].isCallMove)
					m_arrOtherCards[i][j].translate(2);
	}

	// chia bài cho tất cả các game
	protected void deliverCard() {
		updateForOrtherCard();

		if (!m_bIsStartDeliver)
			return;

		int i, j, pos;
		for (i = 0; i < m_iMaxPlayer; i++) {
			pos = switchAnchorToPos(m_bCountAnchor);
			m_bCountAnchor--;
			if (m_bCountAnchor < 0)
				m_bCountAnchor = (byte) (m_iMaxPlayer - 1);

			if (pos < 0)
				continue;
			for (j = 0; j < m_iMaxNumCard; j++) {
				if (m_arrOtherCards[pos][j].m_id < 0 || !m_arrOtherCards[pos][j].isCallMove)
					continue;
				m_arrOtherCards[pos][j].isCallMove = false;
				return;
			}
		}
		m_bIsStartDeliver = false;
	}

	@SuppressWarnings("static-access")
	public void update() {

		// if (countTime_Chat >= 400) {
		// countTime_Chat = 0;
		// // HDCGameMidlet.instance.gifView.stop();
		// HDCGameMidlet.instance.gifView.in = null;
		// }

		if (m_countTime_Chat != null) {
			for (int i = 0; i < m_countTime_Chat.length; i++) {
				if (m_countTime_Chat[i] >= 300) {
					// m_arrGif[i].stop();
					m_countTime_Chat[i] = 0;
					m_arrGif[i].in = null;
				}
			}
		}
		// TODO update show/hide for context menu
		if (countTime_ContextMenu != -1) {
			countTime_ContextMenu++;
			if (countTime_ContextMenu > 50) {
				countTime_ContextMenu = -1;
				flag_ContextMenu = 0;
			}
		}

		HDCGameMidlet.m_myPlayerInfo.update();
		int i;
		for (i = 0; i < m_arrPlayers.length; i++) {
			m_arrPlayers[i].update();
		}
		pos.translate();

		// hiệu ứng chia bài
		deliverCard();

		// hiệu ứng trong thần bài
		if (isFireWork) {
			m_iTickFireWork++;
			if (m_iTickFireWork >= 30) {
				m_iTickFireWork = 0;
				pos.setPosition(CRes.random(0, 240), 600);
				pos.setPositionTo(pos.x, CRes.random(0, 100));
				paintSubFire = false;
			}
		}

		// hiệu ứng sét lại gold trong bàn chơi
		if (m_iTickSetMoney > 0) {
			m_iTickSetMoney--;
			if (m_iTickSetMoney % 3 == 0)
				m_iColorMoney = (0xff0000 != m_iColorMoney) ? 0xff0000 : 0xffff00;
			if (m_iTickSetMoney <= 0)
				m_iColorMoney = 0xffff00;
		}

		// chat from me
		now = System.currentTimeMillis();
		if (now - lastTimeChat > 1000 && !HDCGameMidlet.instance.content_Chat.equals("")) {
			// onChatFromMe(m_tfChatFromMe.getText());
			// m_tfChatFromMe.setText("");
			onChatFromMe(HDCGameMidlet.instance.content_Chat);
			HDCGameMidlet.instance.content_Chat = "";
			lastTimeChat = now;
		}

	}

	// TODO update key for button "start - ready"
	private void updateKey_Start_Ready() {
		if (GameCanvas.isPointerDown) {
			if (GameCanvas.isPointer(GameCanvas.w / 2
					- GameResource.instance.m_frameTienLen_Start_Ready_2.frameWidth / 4 * 3,
					GameCanvas.h / 2
							- GameResource.instance.m_frameTienLen_Start_Ready_2.frameHeight / 4
							* 3,
					GameResource.instance.m_frameTienLen_Start_Ready_2.frameWidth / 2 * 3,
					GameResource.instance.m_frameTienLen_Start_Ready_2.frameHeight / 2 * 3)) {
				imgStart = GameResource.instance.m_frameTienLen_Start_Ready_1;
			}
		}

		if (GameCanvas.isPointerClick) {
			if (GameCanvas.isPointer(GameCanvas.w / 2
					- GameResource.instance.m_frameTienLen_Start_Ready_2.frameWidth / 4 * 3,
					GameCanvas.h / 2
							- GameResource.instance.m_frameTienLen_Start_Ready_2.frameHeight / 4
							* 3,
					GameResource.instance.m_frameTienLen_Start_Ready_2.frameWidth / 2 * 3,
					GameResource.instance.m_frameTienLen_Start_Ready_2.frameHeight / 2 * 3)) {
				if (m_cmdCenter != null) {
					imgStart = GameResource.instance.m_frameTienLen_Start_Ready_2;
					HDCGameMidlet.instance.m_viberator.vibrate(100);
					m_cmdCenter.action.perform();
				}
			}
		}
	}

	// TODO update key for menu button
	private void updateKey_MenuButton() {

		if (GameCanvas.isPointerDown) {
			int y = /* (GameCanvas.h - imgMenuBg_h) / 2 */0;
			if (GameCanvas.isPointer_Down(GameCanvas.w - imgMenuBg_w, y, imgMenuBg_w, imgMenuBg_h)) {
				idx_MenuButton = (GameCanvas.py - y)
						/ GameResource.instance.m_frameMenuBgGame.frameHeight;
			}
		}

		if (GameCanvas.isPointerClick) {
			int y = /* (GameCanvas.h - imgMenuBg_h) / 2 */0;
			if (GameCanvas.isPointer(GameCanvas.w - imgMenuBg_w, y, imgMenuBg_w, imgMenuBg_h)) {
				idx_MenuButton = (GameCanvas.py - y)
						/ GameResource.instance.m_frameMenuBgGame.frameHeight;
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				if (typeMenu == 0) {
					if (idx_MenuButton == 0) {
						// mail
						m_cmdMail.action.perform();
					} else {
						// chat
						m_cmdChat.action.perform();
					}
				} else {
					switch (idx_MenuButton) {
					case 0:
						// mail
						m_cmdMail.action.perform();
						break;
					case 1:
						// chat
						m_cmdChat.action.perform();
						break;
					case 2:
						// dat tien cuoc
						m_cmdDatTienCuoc.action.perform();
						break;
					case 3:
						// dat mat khau
						m_cmdDatMatKhau.action.perform();
						break;
					case 4:
						// bo mat khau
						m_cmdBoMatKhau.action.perform();
						break;
					case 5:
						// tu dong tim ng choi
						m_cmdTuDongTimNgChoi.action.perform();
						break;
					default:
						break;
					}
				}
				idx_MenuButton = -1;
			}
		}
	}

	// TODO updateKey for context menu
	private void updateKey_ContextMenu() {
		if (GameCanvas.isPointerClick) {
			for (int i = 0; i < m_arrPlayers.length; i++) {
				if (GameCanvas.isPointer(m_arrPos[i].x - DetailImage.imgTienLen_KhungUser_w,
						m_arrPos[i].y - DetailImage.imgTienLen_KhungUser_h,
						DetailImage.imgTienLen_KhungUser_w * 2,
						DetailImage.imgTienLen_KhungUser_h * 2)
						&& m_arrPos[i].anchor != 0) {
					idx_player = i;
					countTime_ContextMenu++;
					flag_ContextMenu = 1;
					xContextMenu = m_arrPos[i].x + DetailImage.imgTienLen_KhungUser_w / 2;
					yContextMenu = m_arrPos[i].y;
					break;
				}
			}
		}

		// if(count==0){
		// flag_ContextMenu = 0;
		// countTime_ContextMenu = -1;
		// }
	}

	public void updateKey() {

		// TODO update key for button back
		updateButton_Back();

		// TODO update key for "Start - Ready"
		if (flagStart != 0 && flagStart != 4) {
			updateKey_Start_Ready();

		}

		// context menu
		if (flag_ContextMenu == 1)
			updateKey_ItemContextMenu();
		if (showViewInfo)
			updateKey_ViewPlayerInfo();

		updateKey_ContextMenu();

		// update menu button
		updateKey_MenuButton();

		if (GameCanvas.isPointerClick) {
			if (GameCanvas.isPointer(DetailImage.imgTienLen_Chat_w / 4,
					DetailImage.imgHeaderBg_h / 4 * 5, DetailImage.imgTienLen_Chat_w / 2 * 3,
					DetailImage.imgTienLen_Chat_h + DetailImage.imgHeaderBg_h / 2)) {
				// ChatTextField.gI().startChat(this);
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				m_tfChatFromMe.update();
			}
		}
		super.updateKey();
	}

	public void keyPress(int keyCode) {
		// ChatTextField.gI().startChat(this);
		super.keyPress(keyCode);
	}

	/**
	 * Paint box which displays second is counting down.
	 */
	private void paintTimerBox(Graphics g, float x, float y) {
		g.drawImage(GameResource.instance.imgTienLen_Time_Bg, x
				- (DetailImage.imgTienLen_Time_Bg_w - DetailImage.imgTienLen_Time_Line_w) / 2, y
				- (DetailImage.imgTienLen_Time_Bg_h - DetailImage.imgTienLen_Time_Line_h) / 2,
				Graphics.LEFT | Graphics.TOP);
		g.setClip(x, y, (float) (DetailImage.imgTienLen_Time_Line_w / 30 * m_iGameTick),
				DetailImage.imgTienLen_Time_Line_h);
		g.drawImage(GameResource.instance.imgTienLen_Time_Line, x, y, Graphics.LEFT | Graphics.TOP);
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);

		g.drawImage(GameResource.instance.imgTienLen_TimeOutLight, x
				+ (float) (DetailImage.imgTienLen_Time_Line_w / 30 * m_iGameTick), y
				+ DetailImage.imgTienLen_Time_Line_h / 2, Graphics.HCENTER | Graphics.VCENTER);
	}

	// TODO paint "xin chờ - bắt đầu - sẵn sàn - không sẵn sàng"
	private void paintStart(Graphics g) {
		switch (flagStart) {
		case 0:
			// TODO trạng thái xin chờ
			BitmapFont.setTextSize(30 / HDCGameMidlet.scale);
			BitmapFont.drawItalicFont_1(g, "Xin chờ ...", GameCanvas.hw, GameCanvas.hh, 0xffb901,
					Graphics.VCENTER | Graphics.HCENTER);
			break;
		default:
			// TODO trạng thái bắt đầu
			imgStart.drawFrame((flagStart - 1), GameCanvas.hw, GameCanvas.hh, Sprite.TRANS_NONE,
					Graphics.HCENTER | Graphics.VCENTER, g);

			break;
		}
	}

	// TODO paint gold and dina
	private void paintGold_Dina(Graphics g) {
		if (imgMenuBg != null) {
			// TODO gold
			int w = GameCanvas.w - imgMenuBg.getWidth();
			int h = GameResource.instance.m_frameMenuIconGame.frameHeight / 2;
			BitmapFont.drawNormalFont(g, " " + HDCGameMidlet.m_myPlayerInfo.gold, w, h, 0xffb901,
					Graphics.RIGHT | Graphics.VCENTER);
			w -= BitmapFont.m_bmNormalFont.stringWidth(" " + HDCGameMidlet.m_myPlayerInfo.gold);
			GameResource.instance.m_frameMenuIconGame.drawFrame(5, w, 0, Sprite.TRANS_NONE,
					Graphics.RIGHT | Graphics.TOP, g);
			// TODO Dina
			w -= GameResource.instance.m_frameMenuIconGame.frameWidth * 2;
			BitmapFont.drawNormalFont(g, " " + HDCGameMidlet.m_myPlayerInfo.dina, w, h, 0xffb901,
					Graphics.RIGHT | Graphics.VCENTER);
			w -= BitmapFont.m_bmNormalFont.stringWidth(" " + HDCGameMidlet.m_myPlayerInfo.dina);
			GameResource.instance.m_frameMenuIconGame.drawFrame(12, w, 0, Sprite.TRANS_NONE,
					Graphics.RIGHT | Graphics.TOP, g);
		}
	}

	// TODO paint thông tin phòng chơi và bàn chơi
	private void paintRoom_BoardInfo(Graphics g) {
		BitmapFont.drawNormalFont(g, "Phòng " + roomCode + " | " + "Bàn " + boardId,
				GameResource.instance.m_frameMenu_ButtonBack.frameWidth * 2, 0, 0xffb901,
				Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawNormalFont(g, "Mức cược : " + betGoldOfTable + "$",
				GameResource.instance.m_frameMenu_ButtonBack.frameWidth * 2,
				26 / HDCGameMidlet.scale, 0xffb901, Graphics.LEFT | Graphics.TOP);
	}

	// TODO paint button Back
	private void paintButton_Back(Graphics g) {
		GameResource.instance.m_frameMenu_ButtonBack.drawFrame(flagButton_Back,
				GameResource.instance.m_frameMenu_ButtonBack.frameWidth / 2,
				GameResource.instance.m_frameMenu_ButtonBack.frameHeight / 2, Sprite.TRANS_NONE,
				Graphics.LEFT | Graphics.TOP, g);
	}

	private void updateButton_Back() {
		// TODO move
		if (GameCanvas.isPointerDown) {
			if (GameCanvas.isPointer_Down(0, 0,
					GameResource.instance.m_frameMenu_ButtonBack.frameWidth * 2,
					GameResource.instance.m_frameMenu_ButtonBack.frameHeight * 2)) {
				flagButton_Back = 1;
			}
		}

		// TODO click
		if (GameCanvas.isPointerClick) {
			if (GameCanvas.isPointer(0, 0,
					GameResource.instance.m_frameMenu_ButtonBack.frameWidth * 2,
					GameResource.instance.m_frameMenu_ButtonBack.frameHeight * 2)) {
				flagButton_Back = 0;
				doBack();
			}
		}
	}

	private void paintPlayerBoard(Graphics g) {
		int i, count = m_iMaxPlayer;
		PlayerInfo p;
		for (i = 0; i < count; i++) {
			if (m_arrPlayers[i] != null && m_arrPlayers[i].itemId != -1) {
				p = m_arrPlayers[i];
				p.paintInBoard(g, m_arrPos[i].x, m_arrPos[i].y,
						(HDCGameMidlet.m_myPlayerInfo.itemName.equals(p.itemName)));

				if (m_strOwnerName.equals(p.itemName)) {
					GameResource.instance.m_frameTienLen_IconUser.drawFrame(4, m_arrPos[i].x,
							m_arrPos[i].y, Sprite.TRANS_NONE, Graphics.TOP | Graphics.LEFT, g);
				} else if (!m_bIsPlaying && p.isReady == 1) {
					// TODO paint ready
					GameResource.instance.m_frameTienLen_IconUser.drawFrame(5, m_arrPos[i].x,
							m_arrPos[i].y, Sprite.TRANS_NONE, Graphics.TOP | Graphics.LEFT, g);
				}
			}
		}
		p = null;
	}

	// TODO paint header top
	protected void paintHeader_Top(Graphics g) {
	}

	// TODO paint header bottom
	protected void paintHeader_Bottom(Graphics g) {
	}

	public void paint(Graphics g) {
		// TODO paint background
		PaintPopup.paintBackground(g, m_strTitle);

		// TODO paint button back
		paintButton_Back(g);

		// TODO paint table
		g.drawImage(imgTable, GameCanvas.hw, GameCanvas.hh, Graphics.HCENTER | Graphics.VCENTER);

		// TODO paint room and board info
		paintRoom_BoardInfo(g);

		// TODO paint gold and dina
		paintGold_Dina(g);

		// // TODO paint tien dat cuoc
		// BitmapFont.drawItalicFont(g, "" + betGoldOfTable, GameCanvas.w,
		// GameResource.instance.imgHeaderBg.getHeight() / 2 * 3,
		// 0xffb901, Graphics.RIGHT | Graphics.TOP);
		// GameResource.instance.m_frameWin_Chip.drawFrame(0, GameCanvas.w
		// - BitmapFont.m_bmNormalFont.stringWidth(" " + betGoldOfTable),
		// GameResource.instance.imgHeaderBg.getHeight() / 2 * 3,
		// Sprite.TRANS_NONE, Graphics.RIGHT | Graphics.TOP, g);

		// TODO paint backgroung menu
		g.drawImage(imgMenuBg, GameCanvas.w, 0, Graphics.RIGHT | Graphics.TOP);
		if (typeMenu == 0) {
			paintMenu_Button(g, 2, GameCanvas.w - imgMenuBg_w, /*
																 * (GameCanvas.h
																 * -
																 * imgMenuBg_h)
																 * / 2
																 */0);
		} else {
			paintMenu_Button(g, 6, GameCanvas.w - imgMenuBg_w, /*
																 * (GameCanvas.h
																 * -
																 * imgMenuBg_h)
																 * / 2
																 */0);
		}

		// TODO paint icon "start  -  ready"
		if (flagStart != 4)
			paintStart(g);

		// TODO paint card after player info
		paintAfterPlayerInfo();

		// TODO paint player info
		paintPlayerBoard(g);

		// // TODO paint icon chat
		// g.drawImage(GameResource.instance.imgTienLen_Chat,
		// GameResource.instance.imgTienLen_Chat.getWidth() / 2,
		// GameResource.instance.imgHeaderBg.getHeight() / 2 * 3,
		// Graphics.LEFT | Graphics.TOP);
		// TODO paint game
		paintPlayGame(g);

		// TODO paint content chat
		paintContentChat(g);

		// TODO paint context menu
		paintContextMenu(g);

		// TODO paint player info
		if (showViewInfo)
			paintPlayerInfo(g);

		if (countTime_Chat < 300 && HDCGameMidlet.gifView != null) {
			HDCGameMidlet.gifView.onDraw(g);
			countTime_Chat++;
		}

		// if (m_arrGif != null) {
		// for (int i = 0; i < m_arrGif.length; i++) {
		// if (m_arrGif[i].in != null && countTime_Chat < 100) {
		// m_arrGif[i].onDraw(g);
		// countTime_Chat++;
		// }
		// }
		// }

		super.paint(g);
	}

	private void paintContentChat(Graphics g) {
		// TODO Auto-generated method stub
		int i;
		PlayerInfo p;
		for (i = 0; i < m_iMaxPlayer; i++) {
			if (m_arrPlayers[i] != null && m_arrPlayers[i].itemId != -1) {
				p = m_arrPlayers[i];
				if (m_arrPos[i].anchor == 2) {

					m_countTime_Chat[i]++;
					if (m_arrGif[i].in != null)
						m_arrGif[i].onDraw(g);

					p.paintChatPlayer(g, m_arrPos[i].x
							+ GameResource.instance.m_frameTienLen_PanelInfo.frameWidth / 4 * 3,
							m_arrPos[i].y, m_arrPos[i].anchor);
				} else if (m_arrPos[i].anchor == 1) {
					m_countTime_Chat[i]++;
					if (m_arrGif[i].in != null)
						m_arrGif[i].onDraw(g);

					p.paintChatPlayer(g, m_arrPos[i].x, m_arrPos[i].y, m_arrPos[i].anchor);
				} else if (m_arrPos[i].anchor == 3) {
					m_countTime_Chat[i]++;
					if (m_arrGif[i].in != null)
						m_arrGif[i].onDraw(g);

					p.paintChatPlayer(g, m_arrPos[i].x - 80, m_arrPos[i].y - 16, m_arrPos[i].anchor);
				} else {
					p.paintChatPlayer(g, m_arrPos[i].x
							+ GameResource.instance.m_frameTienLen_PanelInfo.frameWidth / 4 * 3,
							m_arrPos[i].y, m_arrPos[i].anchor);
				}
				if (m_strWhoTurn.equals(p.itemName) && m_iGameTick > 0) {
					paintTimerBox(
							g,
							m_arrPos[i].x
									- DetailImage.imgTienLen_KhungUser_w
									/ 2
									+ (DetailImage.imgTienLen_KhungUser_w - DetailImage.imgimgTienLen_Time_Bg_w)
									/ 2, m_arrPos[i].anchor == 0 ? m_arrPos[i].y
									- DetailImage.imgTienLen_KhungUser_h / 4
									+ (10 / HDCGameMidlet.instance.scale) : m_arrPos[i].y
									- DetailImage.imgTienLen_KhungUser_h / 4);
				}
			}
		}
		p = null;
	}

	protected void paintAfterPlayerInfo() {
		// TODO Auto-generated method stub

	}

	// TODO paint player info
	private void paintPlayerInfo(Graphics g) {
		g.drawImage(GameResource.instance.imgMenu_ContextMenu_3, 0, 0, Graphics.LEFT | Graphics.TOP);
		// nick
		BitmapFont.drawNormalFont(g, "Nick :" + m_ViewInfo.itemName, 30 / HDCGameMidlet.scale,
				DetailImage.imgMenu_ContextMenu_3_h / 10, 0xC7C7C7, Graphics.VCENTER
						| Graphics.LEFT);
		// gioi tinh
		BitmapFont.drawNormalFont(g, "Giới tính :" + m_ViewInfo.gender, 30 / HDCGameMidlet.scale,
				DetailImage.imgMenu_ContextMenu_3_h / 10 * 3, 0xC7C7C7, Graphics.VCENTER
						| Graphics.LEFT);
		// cap do
		BitmapFont.drawNormalFont(g, "Cấp độ :" + m_ViewInfo.level, 30 / HDCGameMidlet.scale,
				DetailImage.imgMenu_ContextMenu_3_h / 10 * 5, 0xC7C7C7, Graphics.VCENTER
						| Graphics.LEFT);
		// dina
		GameResource.instance.m_frameMenuIconGame.drawFrame(12, 30 / HDCGameMidlet.scale,
				DetailImage.imgMenu_ContextMenu_3_h / 10 * 7, Sprite.TRANS_NONE, Graphics.LEFT
						| Graphics.VCENTER, g);
		BitmapFont.drawNormalFont(g, " " + m_ViewInfo.dina, 30 / HDCGameMidlet.scale
				+ GameResource.instance.m_frameMenuIconGame.frameWidth,
				DetailImage.imgMenu_ContextMenu_3_h / 10 * 7, 0xC7C7C7, Graphics.VCENTER
						| Graphics.LEFT);
		// gold
		GameResource.instance.m_frameMenuIconGame.drawFrame(5, 30 / HDCGameMidlet.scale,
				DetailImage.imgMenu_ContextMenu_3_h / 10 * 9, Sprite.TRANS_NONE, Graphics.LEFT
						| Graphics.VCENTER, g);
		BitmapFont.drawNormalFont(g, "" + m_ViewInfo.gold, 30 / HDCGameMidlet.scale
				+ GameResource.instance.m_frameMenuIconGame.frameWidth,
				DetailImage.imgMenu_ContextMenu_3_h / 10 * 9, 0xC7C7C7, Graphics.VCENTER
						| Graphics.LEFT);

		// close context menu
		GameResource.instance.m_frameMenu_Context_Close.drawFrame(flagContextMenu_Close,
				DetailImage.imgMenu_ContextMenu_3_w
						- GameResource.instance.m_frameMenu_Context_Close.frameHeight / 4,
				GameResource.instance.m_frameMenu_Context_Close.frameHeight / 4, Sprite.TRANS_NONE,
				Graphics.RIGHT | Graphics.TOP, g);

	}

	// TODO update key for show View info
	private void updateKey_ViewPlayerInfo() {
		if (GameCanvas.isPointerDown) {
			if (GameCanvas.isPointer_Down(DetailImage.imgMenu_ContextMenu_3_w
					- GameResource.instance.m_frameMenu_Context_Close.frameHeight / 2 * 3, 0,
					GameResource.instance.m_frameMenu_Context_Close.frameWidth / 2 * 3,
					GameResource.instance.m_frameMenu_Context_Close.frameHeight / 2 * 3)) {
				flagContextMenu_Close = 0;
			}
		}

		if (GameCanvas.isPointerClick) {
			if (GameCanvas.isPointer(DetailImage.imgMenu_ContextMenu_3_w
					- GameResource.instance.m_frameMenu_Context_Close.frameHeight / 2 * 3, 0,
					GameResource.instance.m_frameMenu_Context_Close.frameWidth / 2 * 3,
					GameResource.instance.m_frameMenu_Context_Close.frameHeight / 2 * 3)) {
				showViewInfo = false;
			}
		}
	}

	// TODO paint context menu
	private void paintContextMenu(Graphics g) {
		if (flag_ContextMenu == 1 && countTime_ContextMenu != -1) {
			g.drawImage(imgContextMenu, xContextMenu, yContextMenu, Graphics.TOP | Graphics.HCENTER);
			int h = imgContextMenu.getHeight();
			// Xem thông tin && kết bạn
			if (typeMenu == 0) {

				// TODO paint hight light context menu
				if (idx_ContextMenu != -1)
					g.drawImage(GameResource.instance.imgMenu_ContextMenu_HighLight, xContextMenu,
							yContextMenu + h / 4 + idx_ContextMenu * h / 2, Graphics.VCENTER
									| Graphics.HCENTER);

				BitmapFont.drawNormalFont(g, "Xem thông tin", xContextMenu, yContextMenu + h / 4,
						idx_ContextMenu == 0 ? 0xffb901 : 0xfffff, Graphics.HCENTER
								| Graphics.VCENTER);
				BitmapFont.drawNormalFont(g, "Kết bạn", xContextMenu, yContextMenu + h / 4 * 3,
						idx_ContextMenu == 1 ? 0xffb901 : 0xfffff, Graphics.HCENTER
								| Graphics.VCENTER);

			} else {
				// TODO paint hight light context menu
				if (idx_ContextMenu != -1)
					g.drawImage(GameResource.instance.imgMenu_ContextMenu_HighLight, xContextMenu,
							yContextMenu + h / 6 + idx_ContextMenu * h / 6 * 2, Graphics.VCENTER
									| Graphics.HCENTER);

				// Xem thông tin && kết bạn && kíck
				BitmapFont.drawNormalFont(g, "Xem thông tin", xContextMenu, yContextMenu + h / 6,
						idx_ContextMenu == 0 ? 0xffb901 : 0xfffff, Graphics.HCENTER
								| Graphics.VCENTER);
				BitmapFont.drawNormalFont(g, "Kết bạn", xContextMenu, yContextMenu + h / 6 * 3,
						idx_ContextMenu == 1 ? 0xffb901 : 0xfffff, Graphics.HCENTER
								| Graphics.VCENTER);
				BitmapFont.drawNormalFont(g, "Kick", xContextMenu, yContextMenu + h / 6 * 5,
						idx_ContextMenu == 2 ? 0xffb901 : 0xfffff, Graphics.HCENTER
								| Graphics.VCENTER);

			}
		}
	}

	// TODO update key context menu
	@SuppressWarnings("static-access")
	private void updateKey_ItemContextMenu() {
		// down
		if (GameCanvas.instance.isPointerDown) {
			int w = imgContextMenu.getWidth();
			int h = imgContextMenu.getHeight();
			if (GameCanvas.instance.isPointer_Down(xContextMenu - w / 2, yContextMenu, w, h)) {
				int height = h / (typeMenu == 0 ? 2 : 3);
				idx_ContextMenu = (GameCanvas.pyLast - yContextMenu) / height;
			}
		}

		// click
		if (GameCanvas.instance.isPointerClick) {
			int w = imgContextMenu.getWidth();
			int h = imgContextMenu.getHeight();
			if (GameCanvas.instance.isPointer(xContextMenu - w / 2, yContextMenu, w, h)) {
				int height = h / (typeMenu == 0 ? 2 : 3);
				idx_ContextMenu = (GameCanvas.py - yContextMenu) / height;
				if (idx_ContextMenu == 0) {
					// GameCanvas.startOKDlg("Xem thông tin");

					PlayerInfo p = m_arrPlayers[idx_player];
					final String strName = p.itemName;
					if (!strName.equals("") && strName.length() > 0
							&& !strName.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
						GlobalService.onViewInfoFriend(strName);

				} else if (idx_ContextMenu == 1) {
					PlayerInfo p = m_arrPlayers[idx_player];
					if (!p.itemName.equals(HDCGameMidlet.m_myPlayerInfo.itemName)
							&& p.itemName.length() > 0) {
						final String playerName = p.itemName;
						GlobalService.onRequestMakeFriend(playerName);
					}
				} else {
					// GameCanvas.startOKDlg("Kick");
					PlayerInfo p = m_arrPlayers[idx_player];
					final String strName = p.itemName;
					if (!strName.equals("") && strName.length() > 0
							&& !strName.equals(m_strOwnerName))
						GlobalService.onKick(strName);
				}
				idx_ContextMenu = -1;
				flag_ContextMenu = 0;
				countTime_ContextMenu = -1;
			}
		}

	}

	void fireworkEffect(Graphics g) {
		g.setColor(0xffffff);
		if (!paintSubFire) {
			g.fillArc(pos.x, pos.y, 3, 3, 0, 360);
			g.fillArc(pos.x + CRes.random(30, 50), pos.y + CRes.random(80, 120), 3, 3, 0, 360);
			g.fillArc(pos.x + CRes.random(50, 80), pos.y + CRes.random(10, 20), 3, 3, 0, 360);
			g.fillArc(pos.x + CRes.random(80, 120), pos.y + CRes.random(30, 40), 3, 3, 0, 360);

		}

		if (pos.x == pos.xTo && pos.y == pos.yTo && !paintSubFire) {
			paintSubFire = true;
			for (int i = 0; i < star.length; i++) {
				star[i] = new ExplosionStar();
				star[i].startEffect(pos.x + CRes.random(-100, 100), pos.y + CRes.random(-100, 100),
						10, 100);
			}
		}
	}

	protected void paintIconChat(Graphics g) {
	}

	protected void paintIconMail(Graphics g) {
		super.paintIconMail(g);
	}

	public abstract void paintPlayGame(Graphics g);

	public void setPlayer(PlayerInfo p, int seat) {
		p.isReady = 0;
		p.isOwner = false;

		if (seat >= 0 && seat < m_iMaxPlayer) {
			m_arrPlayers[seat] = p;
		}
	}

	public void doLeaveBoard() {
		stopGame();
		GlobalService.onOutTable(boardId);
		// ListBoardScr.getInstance().switchToMe(TabScr.gI(),
		// ListBoardScr.getInstance().romType);
		// close();
	}

	// TODO select table
	private void setImageTable() {
		if (GameCanvas.currentScreen instanceof TienLenScr) {
			imgTable = GameResource.instance.imgTienLen_Table;
		} else if (GameCanvas.currentScreen instanceof BaiCaoScr) {
			imgTable = GameResource.instance.imgBaiCao_Table;
		} else if (GameCanvas.currentScreen instanceof PhomScr) {
			imgTable = GameResource.instance.imgPhom_Table;
		} else {
			imgTable = GameResource.instance.imgXiTo_Table;
		}
	}

	public void initiFirework() {
		// startFire = true;
		for (int i = 0; i < posFire.length; i++) {
			posFire[i] = new Position();
		}
		pos.setPosition(CRes.random(0, 240), 600);
		pos.setPositionTo(pos.x, CRes.random(0, 100));

		paintSubFire = false;
	}

	public void resetReady() {
		PlayerInfo p;
		for (int i = 0; i < m_arrPlayers.length; i++) {
			if (m_arrPlayers[i].itemId != -1) {
				p = m_arrPlayers[i];
				p.isReady = 0;
			}
		}
		// flagStart = 2;
		readyCmd.caption = "Sẵn sàng";
		m_bMyReady = 0;
		p = null;
	}

	public void playerLeave(String strLeave) {
		PlayerInfo p = findPlayer(strLeave);
		if (p != null && !p.itemName.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
			p.itemId = -1;
			p.itemName = "";
			p.isReady = 0;
			p.isOwner = false;

			if (countPlayer() == 2 && !m_bIsPlaying) {
				arrangePlayersInBoard();
			}
		}
		p = null;
	}

	public void setBetGoldOfTable(long gold) {
		betGoldOfTable = gold;
		resetReady();
		if (GameCanvas.currentScreen instanceof XiToScr) {
			XiToScr.gI().betGold = 0;
			XiToScr.gI().lastCuoc = 0;
		}
		m_iTickSetMoney = 200;
		if (HDCGameMidlet.m_myPlayerInfo.itemName.equals(m_strOwnerName) && !m_bIsPlaying) {
			m_cmdCenter = waitingCmd;
		}
	}

	public void stopGame() {
		resetAllCard();
		int i;
		// reset player
		for (i = 0; i < m_arrPlayers.length; i++) {
			m_arrPlayers[i].itemId = -1;
			m_arrPlayers[i].itemName = "";
		}
	}

	public void switchToMe(String ownerString) {
		super.switchToMe();
		GameCanvas.cameraList.close();

		// TODO select table
		setImageTable();

		GameCanvas.cameraList.close();
		int i;
		for (i = 0; i < m_arrPlayers.length; i++) {
			m_arrPlayers[i].itemId = -1;
			m_arrPlayers[i].itemName = "";
			m_arrPlayers[i].isReady = 0;
			m_arrPlayers[i].isOwner = false;
		}

		m_iColorMoney = 0xffff00;
		m_strOwnerName = ownerString;
		m_bIsPlaying = false;
		m_bIsWaitingBeforPlay = true;
		showMenuButton = true;
		m_bIsDisable = false;
		resetAllCard();

		initiFirework();
		System.gc();
	}

	public void startGame() {
		// TODO kiểm tra điều kiện là show menu cho dt nhỏ
		showMenuButton = true;
		if (HDCGameMidlet.scale != 1)
			showMenuButton = false;

		m_bIsStartDeliver = true;
		m_bIsWaitingBeforPlay = false;
		m_bIsPlaying = true;
		m_cmdCenter = null;
		m_cmdRight = null;
		if (isCompetition) {
			m_cmdLeft = giveUpCmd;
		} else {
			m_cmdLeft = menuNormalCmd;
			createImage_MenuBg(3);
			m_tfChatFromMe.y = (GameCanvas.h - imgMenuBg_h) / 2
					+ GameResource.instance.m_frameMenuBgGame.frameHeight;
			typeMenu = 0;
			imgContextMenu = GameResource.instance.imgMenu_ContextMenu_2;
		}
		m_bCountAnchor = 0;
		m_iGameTick = 0;
	}

	public void resetAllCard() {
		m_bMyReady = 0;
		m_bCountAnchor = 0;
		m_iGameTick = 0;
		m_bIsPlaying = false;
		m_cmdRight = null;
		m_cmdCenter = null;
		HDCGameMidlet.m_myPlayerInfo.isReady = 0;
		setOwner(m_strOwnerName);
		resetReady();

		arrangePlayersInBoard();
		// resetCard(m_arrHitCard, -1);
	}

	public void setOwner(String strNewOwnerName) {
		m_strOwnerName = strNewOwnerName;

		PlayerInfo p = findPlayer(strNewOwnerName);
		if (p != null) {
			p.isReady = 1;
			p.isOwner = true;
		}

		if (m_bIsPlaying || m_cmdCenter == waitCmd)
			return;

		if (isCompetition) {
			m_cmdLeft = giveUpCmd;
		} else {
			if (m_strOwnerName.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
				m_cmdLeft = menuOwnerCmd;
				createImage_MenuBg(6);
				m_tfChatFromMe.y = (GameCanvas.h - imgMenuBg_h) / 2
						+ GameResource.instance.m_frameMenuBgGame.frameHeight;
				setCommandOwner();
				typeMenu = 1;
				imgContextMenu = GameResource.instance.imgMenu_ContextMenu_1;
			} else {
				idxIcon_Start_Ready = 1;
				flagStart = 2;
				m_cmdLeft = menuNormalCmd;
				createImage_MenuBg(3);
				m_tfChatFromMe.y = (GameCanvas.h - imgMenuBg_h) / 2
						+ GameResource.instance.m_frameMenuBgGame.frameHeight;
				typeMenu = 0;
				imgContextMenu = GameResource.instance.imgMenu_ContextMenu_2;
				m_cmdCenter = readyCmd;
			}
		}
		p = null;
	}

	private void setCommandOwner() {
		int i;
		int countPlayer = 0;
		int have = 0;
		for (i = 0; i < m_arrPlayers.length; i++) {
			if (!m_arrPlayers[i].itemName.toLowerCase().equals(
					HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())
					&& m_arrPlayers[i].itemId != -1) {
				countPlayer++;
				if (m_arrPlayers[i].isReady == 1)
					have++;
			}
		}
		if (have < countPlayer || countPlayer == 0) {
			flagStart = 0;
			m_cmdCenter = waitingCmd;
		} else {
			idxIcon_Start_Ready = 0;
			flagStart = 1;
			m_cmdCenter = beginCmd;
		}
	}

	public void setReady(String strPlayer, byte isReady) {
		if (m_bIsPlaying)
			return;

		PlayerInfo p = findPlayer(strPlayer);
		if (p != null) {
			p.isReady = isReady;
			if (p.itemName.toLowerCase()
					.equals(HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
				m_bMyReady = isReady;
				if (m_bMyReady == 1) {
					readyCmd.caption = "Không sẵn sàng";
				} else {
					readyCmd.caption = "Sẵn sàng";
				}
			} else if (HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase().equals(
					m_strOwnerName.toLowerCase())) {
				setCommandOwner();
			}
		}
		p = null;
	}

	public void onChatFromMe(String content) {
		if (content.trim().equals("") || content.length() == 0)
			return;
		GlobalService.sendMessageChatInTable(content);
		showChatInTable(HDCGameMidlet.m_myPlayerInfo.itemName, content);
	}

	public void showChatInTable(String strPlayer, String text) {
		PlayerInfo player = findPlayer(strPlayer);
		if (player != null && player.itemId != -1) {
			// countTime_Chat = 1;
			player.chat(text);
			int index = -1;
			for (int i = 0; i < m_arrPos.length; i++) {
				if (m_arrPlayers[i].itemName.equals(strPlayer)) {
					index = i;
					break;
				}
			}
			if (m_arrPos[index].anchor != 0) {
				for (int j = 0; j < GameResource.instance.list_symbols.length; j++) {
					if (text.trim().equals(GameResource.instance.list_symbols[j])) {
						// HDCGameMidlet.instance.setGift(j, m_arrPos[index].x,
						// m_arrPos[index].y);

						m_arrGif[index].in = null;
						m_arrGif[index] = new GifView(GameResource.instance.inputEmotion[j]);
						m_arrGif[index].setX(m_arrPos[index].x);
						m_arrGif[index].setY(m_arrPos[index].y);

						m_countTime_Chat[index] = 0;
						// m_arrGif[index].play();

						break;
					}
				}
			}

			player = null;
		}
	}

	public int countPlayer() {
		int i;
		int count = 0;
		for (i = 0; i < m_arrPlayers.length; i++) {
			if (m_arrPlayers[i] != null && m_arrPlayers[i].itemId != -1) {
				count++;
			}
		}
		return count;
	}

	public void onAllCardPlayerFinish(Message message) {
	}

	public void onSetTurn(String nick, Message message) {

	}

	public void onUserJoinTable(int tbID, String nick, boolean isChampion, int pos, long money,
			int level, int avatarId, int playerStatus) {
		// play - ready - view
		PlayerInfo p = new PlayerInfo();
		p.itemName = nick;
		p.gold = money;
		p.avatarId = avatarId;
		p.isChampion = isChampion;
		p.level = level;

		if (p.itemName.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
			m_iMyPosInVt = pos;
		}

		setPlayer(p, pos);
		if (HDCGameMidlet.m_myPlayerInfo.itemName.equals(m_strOwnerName) && !m_bIsPlaying) {
			flagStart = 0;
			m_cmdCenter = waitingCmd;
		} else if (HDCGameMidlet.m_myPlayerInfo.itemName.equals(nick) && playerStatus == 2
				&& !m_bIsPlaying && m_bIsWaitingBeforPlay) {
			flagStart = 0;
			m_cmdCenter = waitCmd;
			m_cmdRight = null;
		}

		if (!m_bIsPlaying)
			arrangePlayersInBoard();
		else
			arrangeNoPlayers(pos);
		p = null;
	}

	public void resetCard(Card[] listCard, int card) {
		int i;
		// if (listCard != null) {
		for (i = 0; i < listCard.length; i++) {
			listCard[i].m_id = card;
			listCard[i].isSpecial = false;
			listCard[i].isPhom = false;
			listCard[i].isUpCard = false;
			listCard[i].isCallMove = false;
		}
		// }
	}

	private void arrangeNoPlayers(int pos) {
		// init where is not pos
		int i;
		int n = m_arrPlayers.length;
		int[] m_arrPosTmp = new int[m_iMaxPlayer];
		for (i = 0; i < n; i++) {
			m_arrPosTmp[i] = m_arrPos[i].anchor;
			if (m_arrPlayers[i].itemId == -1)
				m_arrPosTmp[i] = -1;
		}
		i = 0;
		int j = 0;
		while (i < n) {
			for (j = 0; j < n; j++) {
				if (m_arrPosTmp[j] == i) {// tim thấy thằng có rồi
					i++;
					break;
				}
			}
			if (j >= n)// không tìm thấy nè
				break;
		}

		if (i < n) {
			m_arrPos[pos].createPosInBoard(i);
		}

		m_arrPosTmp = null;
	}

	public void arrangePlayersInBoard() {
		int i;
		int n = m_arrPlayers.length;

		// set position for my player
		// int m_iHeightPos = GameCanvas.ch;

		int count = countPlayer();
		m_arrPos[m_iMyPosInVt].createPosInBoard(0, 0);
		m_arrGif = new GifView[n];
		m_countTime_Chat = new int[n];
		// set position for others
		int index = 0;
		PlayerInfo p;
		for (i = 0; i < n; i++) {
			index = (m_iMyPosInVt + i) % n;
			p = m_arrPlayers[index];

			if (p != null && p.itemName.length() > 0 && p.itemId != -1) {
				m_arrPos[index].createPosInBoard(i, count);
			}

			m_arrGif[i] = new GifView(null);
			m_countTime_Chat[i] = 0;
		}
		p = null;

	}

	public static String[] strTextFinishGame = { "Trắng", "Nhất", "Nhì", "Ba", "Bét", "Chết", "Ù",
			"Thắng", "Hòa", "Thua", "Móm", "Đền" };
	public static int[] intRank = { 1, 2, 3, 4, 7, 8, 9 };
	public static String[] strRank = { " I", " II", " III", " IV" };

	Vector numbers = new Vector();

	@SuppressWarnings("unchecked")
	private void Calculate_Number_1(int number, int count) {
		String m_strNumber = Integer.toString(number);
		for (int i = 0; i < count; i++) {
			numbers.addElement((Integer) Integer.parseInt(Character.toString(m_strNumber.charAt(i))));
		}
	}

	// TODO create image money
	@SuppressWarnings("rawtypes")
	private Image createImage_Money(int money, boolean plusMoney) {
		numbers = new Vector();
		// Calculate_Number(money, Integer.toString(money).trim().length());
		Calculate_Number_1(money, Integer.toString(money).trim().length());
		Image img = Image.createImage((numbers.size() + 2)
				* GameResource.instance.m_frameTienLen_Number.frameWidth,
				GameResource.instance.m_frameTienLen_Number.frameHeight);
		Graphics g1 = img.getGraphics();

		if (plusMoney) {

			GameResource.instance.m_frameTienLen_Number.drawFrame(11, 0, 0, Sprite.TRANS_NONE,
					Graphics.LEFT | Graphics.TOP, g1);
		} else {

			GameResource.instance.m_frameTienLen_Number.drawFrame(12, 0, 0, Sprite.TRANS_NONE,
					Graphics.LEFT | Graphics.TOP, g1);
		}

		for (int i = 0; i < numbers.size(); i++) {
			GameResource.instance.m_frameTienLen_Number.drawFrame((Integer) (numbers.elementAt(i)),
					GameResource.instance.m_frameTienLen_Number.frameWidth * (i + 1), 0,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.TOP, g1);
		}
		GameResource.instance.m_frameTienLen_Number.drawFrame(10,
				GameResource.instance.m_frameTienLen_Number.frameWidth * (numbers.size() + 1), 0,
				Sprite.TRANS_NONE, Graphics.LEFT | Graphics.TOP, g1);
		return img;
	}

	// TODO create image score
	@SuppressWarnings("rawtypes")
	private Image createImage_Score(int score) {
		numbers = new Vector();
		// Calculate_Number(money, Integer.toString(money).trim().length());
		Calculate_Number_1(score, Integer.toString(score).trim().length());
		Image img = Image.createImage((numbers.size() + 1)
				* GameResource.instance.m_frameBaiCao_Number.frameWidth
				+ GameResource.instance.imgBaiCao_Diem.getWidth(),
				GameResource.instance.imgBaiCao_Diem.getHeight());
		Graphics g1 = img.getGraphics();

		for (int i = 0; i < numbers.size(); i++) {
			GameResource.instance.m_frameBaiCao_Number.drawFrame((Integer) (numbers.elementAt(i)),
					GameResource.instance.m_frameBaiCao_Number.frameWidth * (i + 1), 0,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.TOP, g1);
		}
		g1.drawImage(GameResource.instance.imgBaiCao_Diem, (numbers.size() + 1)
				* GameResource.instance.m_frameBaiCao_Number.frameWidth, 0, Graphics.LEFT
				| Graphics.TOP);
		return img;
	}

	@SuppressWarnings("static-access")
	public void onFinishGame(Message message) {
		int totalUser = MessageIO.readByte(message);
		String info[] = new String[totalUser * 4];
		int i, rank, pos, posOwner = -2, count = 0, score;
		long money, finalMoney = 0;
		String nick;
		PlayerInfo p;
		String[] m_name = new String[totalUser];
		String[] m_rank = new String[totalUser];
		String[] m_money = new String[totalUser];
		for (i = 0; i < totalUser; i++) {
			info[count] = strRank[i];
			count++;

			nick = MessageIO.readString(message);// nick

			info[count] = nick;
			count++;
			m_name[i] = nick;

			rank = MessageIO.readInt(message);// rank
			money = MessageIO.readLong(message);// money
			finalMoney = MessageIO.readLong(message);

			info[count] = "" + money;
			count++;
			m_money[i] = money + " Gold";

			score = MessageIO.readByte(message);
			if (rank < 0 || rank == 0 || rank == 5 || rank == 6 || rank == 11
					|| GameCanvas.currentScreen instanceof TienLenScr
					|| GameCanvas.currentScreen instanceof XiToScr) {
				info[count] = strTextFinishGame[rank];
				m_rank[i] = strTextFinishGame[rank];
			} else {
				info[count] = "" + score;// score
				m_rank[i] = score + " Điểm";
			}
			count++;

			p = findPlayer(nick);
			// int idx = (m_iMyPosInVt + i) % m_arrPos.length;
			pos = findPlayerPos(nick);
			if (p != null) {
				if (GameCanvas.currentScreen instanceof TienLenScr) {

					if (rank > 0 && rank <= 4)
						m_arrPlayers[pos].setRank(rank - 1);
				}

				finalMoney -= p.gold;
				if (money >= 0) {
					p.addMoney((int) finalMoney, 0);
					Image img = createImage_Money((int) Math.abs(finalMoney), true);
					FlyImage f = new FlyImage();
					f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y, img);
					f = null;
					img = null;

					if (GameCanvas.instance.currentScreen instanceof XiToScr) {
						FlyImage f1 = new FlyImage();
						f1.startEffect(m_arrPos[pos].x, m_arrPos[pos].y
								+ +GameResource.instance.m_imgCards[0].getHeight() / 2,
								GameResource.instance.imgWin_Thang);
						f1 = null;
					}

				} else {
					p.subMoney(Math.abs((int) finalMoney), 0);
					Image img = createImage_Money((int) Math.abs(finalMoney), false);
					FlyImage f = new FlyImage();
					f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y, img);
					f = null;
					img = null;

					if (GameCanvas.instance.currentScreen instanceof XiToScr) {
						FlyImage f1 = new FlyImage();
						f1.startEffect(m_arrPos[pos].x, m_arrPos[pos].y
								+ +GameResource.instance.m_imgCards[0].getHeight() / 2,
								GameResource.instance.imgWin_Thua);
						f1 = null;
					}

				}
			}

			// TODO effect điểm cho game bài cào
			if (GameCanvas.instance.currentScreen instanceof BaiCaoScr) {
				if (score != -1 && score < 10) {
					Image img = createImage_Score((int) Math.abs(score));
					FlyImage f = new FlyImage();
					f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y + DetailImage.imgCard_h / 2, img);
					f = null;
					img = null;

				} else if (score == 10) {
					FlyImage f = new FlyImage();
					f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y + DetailImage.imgCard_h / 2,
							GameResource.instance.imgWin_BaTay);
					f = null;

				} else if (score == 11) {
					FlyImage f = new FlyImage();
					f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y + DetailImage.imgCard_h / 2,
							GameResource.instance.imgWin_Xam);
					f = null;
				}
			}

			if (nick.equals(m_strOwnerName))
				posOwner = i;
		}
		m_cmdCenter = null;
		m_cmdLeft = null;
		m_cmdRight = null;
		// centerCmd = null;
		// rightCmd = null;

		m_bIsPlaying = false;
		m_bIsWaitingBeforPlay = true;
		m_iGameTick = 0;

		for (i = 0; i < m_arrPlayers.length; i++)
			m_arrPlayers[i].isReady = 0;
		setOwner(m_strOwnerName);
		resetReady();

		arrangePlayersInBoard();
		// GameCanvas.startOKEndGame(info, new IAction() {
		//
		// public void perform() {
		// arrangePlayersInBoard();
		// GameCanvas.endDlg();
		// }
		// }, posOwner + 1);

		HDCGameMidlet.instance.showDialogReSult(m_name, m_rank, m_money);

		info = null;
		nick = null;
		p = null;
	}

	public void actionEndGame() {
	}

	public void onClearFiredCards() {

	}

	// public void saveCmd(Command center, Command right) {
	// m_bIsDisable = true;
	// centerCmd = center;
	// rightCmd = right;
	// m_cmdCenter = null;
	// m_cmdRight = null;
	// }
	//
	// public void openCmd() {
	// m_bIsDisable = false;
	// if (centerCmd != null) {
	// m_cmdCenter = centerCmd;
	// centerCmd = null;
	// }
	// if (rightCmd != null) {
	// m_cmdRight = rightCmd;
	// rightCmd = null;
	// }
	// }

	public void updateMoney(Message message) {
		PlayerInfo p;
		long money;
		int pos;
		for (int i = 0; i < 2; i++) {
			p = findPlayer(MessageIO.readString(message));
			money = MessageIO.readLong(message);
			pos = findPlayerPos(p.itemName);
			if (p == null)
				return;
			if (p.itemName.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
				if (money > 0) {
					HDCGameMidlet.m_myPlayerInfo.addMoney(Math.abs((int) money), 0);

					Image img = createImage_Money((int) Math.abs(money), true);
					FlyImage f = new FlyImage();
					f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y, img);

				} else {
					HDCGameMidlet.m_myPlayerInfo.subMoney(Math.abs((int) money), 0);

					Image img = createImage_Money((int) Math.abs(money), false);
					FlyImage f = new FlyImage();
					f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y, img);

				}
			} else {
				// int idx = (m_iMyPosInVt + i) % m_arrPos.length;
				// int pos = 0;
				// for (i = 0; i < m_arrPlayers.length; i++) {
				// if (m_arrPlayers[i].itemName.equals(p.itemName)) {
				// pos = i;
				// break;
				// }
				// }

				if (money >= 0) {
					p.addMoney((int) money, 0);

					Image img = createImage_Money((int) Math.abs(money), true);
					FlyImage f = new FlyImage();
					f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y, img);

				} else {
					p.subMoney(Math.abs((int) money), 0);

					Image img = createImage_Money((int) Math.abs(money), false);
					FlyImage f = new FlyImage();
					f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y, img);

				}
			}
		}
	}

	// TODO create image for menu bg
	private void createImage_MenuBg(int numberItem) {
		imgMenuBg_h = GameResource.instance.m_frameMenuBgGame.frameHeight * numberItem;
		imgMenuBg_w = GameResource.instance.m_frameMenuBgGame.frameWidth;
		imgMenuBg = Image.createImage(imgMenuBg_w, imgMenuBg_h);
		Graphics g1 = imgMenuBg.getGraphics();
		for (int i = 0; i < numberItem; i++) {
			if (i == 0) {
				GameResource.instance.m_frameMenuBgGame.drawFrame(0, 0, 0, Sprite.TRANS_NONE,
						Graphics.LEFT | Graphics.TOP, g1);
			} else if (i == numberItem - 1) {
				GameResource.instance.m_frameMenuBgGame.drawFrame(2, 0,
						GameResource.instance.m_frameMenuBgGame.frameHeight * i, Sprite.TRANS_NONE,
						Graphics.LEFT | Graphics.TOP, g1);
			} else {
				GameResource.instance.m_frameMenuBgGame.drawFrame(1, 0,
						GameResource.instance.m_frameMenuBgGame.frameHeight * i, Sprite.TRANS_NONE,
						Graphics.LEFT | Graphics.TOP, g1);
			}
		}
	}

	// TODO paint button for menu
	private void paintMenu_Button(Graphics g, int number, int x, int y) {
		// kiểm tra điều kiện
		if (!showMenuButton)
			return;

		int idx = 0;
		for (int i = 0; i < number; i++) {
			if (idx_MenuButton != i)
				GameResource.instance.m_frameMenuButton.drawFrame(0, x + imgMenuBg_w / 2, y
						+ GameResource.instance.m_frameMenuBgGame.frameHeight / 2
						+ GameResource.instance.m_frameMenuBgGame.frameHeight * i,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);
			else
				GameResource.instance.m_frameMenuButton.drawFrame(1, x + imgMenuBg_w / 2, y
						+ GameResource.instance.m_frameMenuBgGame.frameHeight / 2
						+ GameResource.instance.m_frameMenuBgGame.frameHeight * i,
						Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

			if (number == 2) {
				if (i == 0)
					idx = 1;
				else
					idx = 4;
			} else {
				if (i == 0)
					idx = 1;
				else if (i == 1)
					idx = 4;
				else if (i == 2)
					idx = 0;
				else if (i == 3)
					idx = 3;
				else if (i == 4)
					idx = 2;
				else
					idx = 13;
			}

			if (idx_MenuButton != -1 && i == idx_MenuButton) {
				if (i == 0)
					idx = 7;
				else if (i == 1)
					idx = 10;
				else if (i == 2)
					idx = 6;
				else if (i == 3)
					idx = 9;
				else if (i == 4)
					idx = 8;
				else
					idx = 14;
			}

			GameResource.instance.m_frameMenuIconGame.drawFrame(idx, x + imgMenuBg_w / 2, y
					+ GameResource.instance.m_frameMenuBgGame.frameHeight / 2
					+ GameResource.instance.m_frameMenuBgGame.frameHeight * i, Sprite.TRANS_NONE,
					Graphics.VCENTER | Graphics.HCENTER, g);

		}
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		HDCGameMidlet.instance.Toast("doMenu - PlayGameScr");

	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
//		if (m_cmdRight != null)
//			m_cmdRight.action.perform();
	}

}