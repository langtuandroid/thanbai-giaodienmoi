package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Button;
import com.hdc.mycasino.model.Clan;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.Item;
import com.hdc.mycasino.model.ItemInfo;
import com.hdc.mycasino.model.MyObj;
import com.hdc.mycasino.model.PlayerInfo;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;
import com.hdc.mycasino.utilities.TField;

public class ClanScr extends Screen {
	private static ClanScr instance;

	Vector listItems;

	static Vector LBL_CLANS = new Vector();

	int selectedIndex = 0;
	int numberLetterClans = 0;

	static final byte SELECT_MY_CLAN = 0;
	static final byte SELECT_NOTIFICATION = 1;
	static final byte SELECT_LIST_CLANS = 2;
	static final byte SELECT_LIST_TOP_CLANS = 3;

	static final byte HEADMAN = 1;
	static final byte DEPUTY = 2;
	// nếu là mình có lan thì mới vẽ
	public boolean hasMyClan;

	// gia tộc của tôi
	public Clan myClanOwner;

	// init
	int m_col;
	int m_wCell;
	// ////
	// Command menuPlayer_Center;
	Command menuPlayer;

	Command m_cmdLeft_ThongTin;
	Command m_cmdCenter_Thongtin;

	// TODO columne row in tab avatar
	private int col;
	private int row;
	private int col_select;
	private int row_select;
	private int m_widthItem;
	private int m_heightItem;
	private int m_colAvt;

	// TODO button
	private Button mButton_CapNhat;
	private Button mButton_QuyenLoi;

	// TODO Command
	private Command m_cmdUpdate;
	private Command m_cmdBenefit;

	// TODO degree
	private int degree = 0;

	public static ClanScr gI() {
		if (instance == null) {
			instance = new ClanScr();
		}

		return instance;
	}

	// TODO paint avatar
	@SuppressWarnings("static-access")
	private void paintItem(Graphics g, float x, float y, int index, int select, FrameImage m_frame,
			Item item) {
		// TODO paint icon
		m_frame.drawFrame(index, x + GameResource.instance.m_frameGiaToc_Icon.frameWidth / 2, y,
				Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);
		// TODO paint item name
		BitmapFont.m_bmNormalFont.drawItalicFont(g, item.itemName, x
				+ GameResource.instance.m_frameGiaToc_Icon.frameWidth / 2, y
				+ GameResource.instance.m_frameGiaToc_Icon.frameHeight / 4 * 3, 0xffb901,
				Graphics.HCENTER | Graphics.VCENTER);
		// TODO paint high light
		if (select == 1)
			g.drawImageDegree(GameResource.instance.imgGiaToc_HighLight, x
					+ GameResource.instance.m_frameGiaToc_Icon.frameWidth / 2, y, degree,
					Graphics.HCENTER | Graphics.VCENTER);

	}

	// TODO init button
	private void initButton() {
		// TODO button quyền lợi
		mButton_QuyenLoi = new Button(GameResource.instance.imgButton_Login, GameResource.benefit,
				m_cmdBenefit);
		mButton_QuyenLoi.setXY(m_left + GameResource.instance.imgListScr_Panel.getWidth()
				- (mButton_QuyenLoi.w / 2 * 3), GameCanvas.h
				- (GameResource.instance.imgButton_Login.getHeight() / 2 * 3));
		// TODO button cập nhật
		mButton_CapNhat = new Button(GameResource.instance.imgButton_Login, GameResource.update,
				m_cmdUpdate);
		mButton_CapNhat.setXY(mButton_QuyenLoi.x - (mButton_CapNhat.w / 2 * 3), GameCanvas.h
				- (GameResource.instance.imgButton_Login.getHeight() / 2 * 3));
		// setButtonDefault(mButton_CapNhat);
	}

	public void paint(Graphics g) {
		PaintPopup.paintBackground(g, m_strTitle);

		// TODO paint panel
		g.drawImage(GameResource.instance.imgListScr_Panel, m_left, m_top, Graphics.LEFT
				| Graphics.TOP);

		// g.translate(-g.getTranslateX(), -g.getTranslateY());
		// g.setClip(m_left, m_top, m_width, m_height);

		int top = m_top + GameResource.instance.m_frameGiaToc_Icon.frameHeight / 4;
		int select = 0;
		for (int i = 0; i < listItems.size(); i++) {
			col = i % m_colAvt;
			row = i / m_colAvt;

			if (col == col_select && row == row_select)
				select = 1;
			else
				select = 0;

			paintItem(g, m_left + GameResource.instance.m_frameGiaToc_Icon.frameWidth / 4
					+ m_widthItem * col, top + m_heightItem * row
					+ GameResource.instance.m_frameGiaToc_Icon.frameHeight / 2,
					(((Item) listItems.elementAt(i)).itemId), select,
					GameResource.instance.m_frameGiaToc_Icon, (Item) listItems.elementAt(i));
		}

		// TODO paint button
		mButton_CapNhat.paint(g);
		mButton_QuyenLoi.paint(g);

		super.paint(g);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ClanScr() {
		LBL_CLANS.addElement("Thành lập");
		LBL_CLANS.addElement("Thông báo");
		LBL_CLANS.addElement("Bảng gia tộc");
		LBL_CLANS.addElement("Top gia tộc");

		hasMyClan = false;

		myClanOwner = new Clan();
		listItems = new Vector();

		m_left = 0;
		m_width = GameCanvas.w;
		m_top = GameCanvas.hBottomBar + 5;
		m_height = GameCanvas.h - m_top - GameCanvas.hBottomBar;

		Item items;
		int i;
		for (i = 0; i < LBL_CLANS.size(); i++) {
			items = new Item();
			items.itemName = (String) LBL_CLANS.elementAt(i);
			items.m_iNumItem = 0;
			items.m_bItemType = -1;
			items.itemId = i + 1;
			switch (i) {
			case 0:
				items.itemId = 1;
				break;
			case 1:
				items.itemId = 3;
				break;
			case 2:
				items.itemId = 4;
				break;
			case 3:
				items.itemId = 2;
				break;
			}
			listItems.addElement(items);

		}
		items = null;

		m_cmdUpdate = new Command(GameResource.update, new IAction() {
			public void perform() {
				refeshClanScr();
			}
		});
		m_cmdBenefit = new Command("Quyền lợi", new IAction() {
			public void perform() {
				Command center = null;
				Command left = null;
				if (!hasMyClan) {
					center = new Command(GameResource.register, new IAction() {
						public void perform() {
							ListStringScr.gI().close();
							showClanRegister();
						}
					});
				}

				if (numberLetterClans > 0) {
					left = new Command("Xem thư", new IAction() {
						public void perform() {
							ListStringScr.gI().close();
							GlobalService.sendMessageGetClanNotifications();
						}
					});
				}
				ListStringScr.gI().switchToMe(GameCanvas.currentScreen, "Quyền Lợi Gia Nhập", left,
						center, null);
				ListStringScr.gI().getStringGuide();
			}
		});

		// m_cmdLeft = new Command(GameResource.menu, new IAction() {
		// public void perform() {
		// Vector vt = new Vector();
		// Command menu = new Command(GameResource.update, new IAction() {
		// public void perform() {
		// refeshClanScr();
		// }
		// });
		// vt.addElement(menu);
		//
		// Command guide = new Command("Quyền lợi", new IAction() {
		// public void perform() {
		// Command center = null;
		// Command left = null;
		// if (!hasMyClan) {
		// center = new Command(GameResource.register,
		// new IAction() {
		// public void perform() {
		// ListStringScr.gI().close();
		// showClanRegister();
		// }
		// });
		// }
		//
		// if (numberLetterClans > 0) {
		// left = new Command("Xem thư", new IAction() {
		// public void perform() {
		// ListStringScr.gI().close();
		// GlobalService
		// .sendMessageGetClanNotifications();
		// }
		// });
		// }
		// ListStringScr.gI().switchToMe(GameCanvas.currentScreen,
		// "Quyền Lợi Gia Nhập", left, center, null);
		// ListStringScr.gI().getStringGuide();
		// }
		// });
		// vt.addElement(guide);
		// GameCanvas.instance.menu.startAt(vt, 0);
		// vt = null;
		// }
		// });

		m_cmdRight = new Command(GameResource.close, new IAction() {
			public void perform() {
				close();
			}
		});

		m_cmdCenter = new Command(GameResource.select, new IAction() {
			public void perform() {
				// selectedIndex = m_selected;
				switch (selectedIndex) {
				case SELECT_MY_CLAN:
					if (hasMyClan) {
						GameCanvas.startWaitDlg();
						GlobalService.sendMessageEnterChatRoom();
						GlobalService.sendMessageGetMyClanInformation();
					} else {
						showClanRegister();
					}
					break;
				case SELECT_NOTIFICATION:
					if (numberLetterClans > 0) {
						GameCanvas.startWaitDlg();
						GlobalService.sendMessageGetClanNotifications();
					} else {
//						GameCanvas.startOKDlg("Không có thông tin nào");
						HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Không có thông tin nào");	
					}
					break;
				case SELECT_LIST_CLANS:
					GameCanvas.startWaitDlg();
					GlobalService.sendMessageGetListClans();
					break;
				case SELECT_LIST_TOP_CLANS:
					GameCanvas.startWaitDlg();
					GlobalService.sendMessageGetListTopClans();
					break;
				default:
					break;
				}
			}
		});

		// TODO command left cho tab thông tin gia tộc
		m_cmdLeft_ThongTin = new Command("", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				Vector vt = new Vector();
				vt.addElement(new Command(GameResource.update, new IAction() {
					public void perform() {
						GameCanvas.startWaitDlg();
						GlobalService.sendMessageGetMyClanInformation();
					}
				}));

				vt.addElement(new Command(GameResource.info, new IAction() {
					public void perform() {
						String name = ((PlayerInfo) TabScr.gI().getSelectedItem()).itemName;
						if (name.length() == 0 || name.equals("")) {
//							GameCanvas.startOKDlg("Không có lựa chọn nào");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Không có lựa chọn nào");							
							return;
						}

						GlobalService.onViewInfoFriend(name);
						GameCanvas.startWaitDlg();
						name = null;
					}
				}));

				vt.addElement(new Command(GameResource.moveGold, new IAction() {
					public void perform() {
						MyObj p = TabScr.gI().getSelectedItem();
						if (p.itemName.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
//							GameCanvas.startOKDlg("Không thể chuyển cho chính mình");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Không thể chuyển cho chính mình");							
							return;
						}
						MainScr.gI().showDialogGold((PlayerInfo) p);
						p = null;
					}
				}));

				GameCanvas.instance.menu.startAt(vt, 0);
			}
		});

		// TODO command center cho tab thông tin gia tộc
		m_cmdCenter_Thongtin = new Command("", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				Vector action = new Vector();

				action.addElement(new Command("Đuổi ra gia tộc", new IAction() {
					public void perform() {
						final MyObj clanMember = TabScr.gI().getSelectedItem();
						GameCanvas.startOKDlg("Bạn có muốn đuổi " + clanMember.itemName
								+ " ra khỏi gia tộc?", new IAction() {
							public void perform() {
								if (!HDCGameMidlet.m_myPlayerInfo.itemName
										.equals(clanMember.itemName)) {
									GlobalService.sendMessageRemoveClanMember(clanMember.itemName);
								} else {
//									GameCanvas
//											.startOKDlg("Bạn không thể thực hiện điều này với chính mình");
									HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Bạn không thể thực hiện điều này với chính mình");									
								}								
							}
						});
					}
				}));

				action.addElement(new Command("Mời gia nhập", new IAction() {
					public void perform() {
						GameCanvas.inputDlg.setInfo("Nhập nick thành viên bạn muốn mời",
								new IAction() {
									public void perform() {
										if (GameCanvas.inputDlg.tfInput.getText().length() > 0) {
											GlobalService
													.sendMessageInvitePlayerJoinClan(GameCanvas.inputDlg.tfInput
															.getText());
											GameCanvas.endDlg();
										} else {
//											GameCanvas.startOKDlg("Nhập tên cần mời");
											HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Nhập tên cần mời");											
										}
									}
								}, TField.INPUT_TYPE_ANY);
						GameCanvas.inputDlg.show();
					}
				}));

				if (clanRole == HEADMAN) {
					action.addElement(new Command("Chọn làm tộc phó", new IAction() {
						public void perform() {
							PlayerInfo clanMember = ((PlayerInfo) TabScr.gI().getSelectedItem());
							if (HDCGameMidlet.m_myPlayerInfo.itemName.equals(clanMember.itemName)) {
//								GameCanvas
//										.startOKDlg("Bạn không thể thực hiện điều này với chính mình");
								HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Bạn không thể thực hiện điều này với chính mình");								
							} else {
								if (((PlayerInfo) TabScr.gI().getSelectedItem()).role != PlayerInfo.ROLE_DEPUTY) {
									Vector vt = TabScr.gI().getListItems();
									int i;
									boolean found = false;
									PlayerInfo p = null;
									for (i = 0; i < vt.size(); i++) {
										p = (PlayerInfo) vt.elementAt(i);
										if (p.role == PlayerInfo.ROLE_DEPUTY) {
											found = true;

											break;
										}
									}
									String founder = "";
									if (found) {
										founder = p.itemName;
										p = null;
									}
									final String oldDeputy = founder;

									final String newDeputy = ((PlayerInfo) TabScr.gI()
											.getSelectedItem()).itemName;
									String title = "Bạn có muốn " + newDeputy
											+ " làm phó ban không?";
									if (found) {
										title = "Bạn có muốn đổi vị trí phó ban từ " + oldDeputy
												+ " cho người " + newDeputy + " hay không?";
									}

									GameCanvas.startOKDlg(title, new IAction() {
										public void perform() {
											GameCanvas.startWaitDlg();
											GlobalService.sendMessageSetClanDeputy(oldDeputy,
													newDeputy);
										}
									});
									founder = null;
									p = null;
									title = null;
									vt = null;
								} else {
//									GameCanvas.startOKDlg("Thành viên này đã thực sự là phó tộc");
									HDCGameMidlet.instance.showDialog_Okie("Thông báo","Thành viên này đã thực sự là phó tộc");									
								}
							}
							clanMember = null;
						}
					}));
				}

				MenuNhanh.startMenuNhanh(action, GameCanvas.hw, GameCanvas.h / 2);
				action = null;

			}
		});

		menuPlayer = new Command(GameResource.menu, new IAction() {

			public void perform() {

				Vector vt = new Vector();
				vt.addElement(new Command(GameResource.update, new IAction() {
					public void perform() {
						GameCanvas.startWaitDlg();
						GlobalService.sendMessageGetMyClanInformation();
					}
				}));

				vt.addElement(new Command(GameResource.info, new IAction() {
					public void perform() {
						String name = ((PlayerInfo) TabScr.gI().getSelectedItem()).itemName;
						if (name.length() == 0 || name.equals("")) {
							//GameCanvas.startOKDlg("Không có lựa chọn nào");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Không có lựa chọn nào");
							return;
						}

						GlobalService.onViewInfoFriend(name);
						GameCanvas.startWaitDlg();
						name = null;
					}
				}));

				vt.addElement(new Command(GameResource.moveGold, new IAction() {
					public void perform() {
						MyObj p = TabScr.gI().getSelectedItem();
						if (p.itemName.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
//							GameCanvas.startOKDlg("Không thể chuyển cho chính mình");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Không thể chuyển cho chính mình");
							return;
						}
						MainScr.gI().showDialogGold((PlayerInfo) p);
						p = null;
					}
				}));

				if (clanRole == HEADMAN || clanRole == DEPUTY) {
					// Vector action = new Vector();

					vt.addElement(new Command("Đuổi ra gia tộc", new IAction() {
						public void perform() {
							final MyObj clanMember = TabScr.gI().getSelectedItem();
							GameCanvas.startOKDlg("Bạn có muốn đuổi " + clanMember.itemName
									+ " ra khỏi gia tộc?", new IAction() {
								public void perform() {
									if (!HDCGameMidlet.m_myPlayerInfo.itemName
											.equals(clanMember.itemName)) {
										GlobalService
												.sendMessageRemoveClanMember(clanMember.itemName);
									} else {
										GameCanvas
												.startOKDlg("Bạn không thể thực hiện điều này với chính mình");
									}
								}
							});
						}
					}));

					vt.addElement(new Command("Mời gia nhập", new IAction() {
						public void perform() {
							GameCanvas.inputDlg.setInfo("Nhập nick thành viên bạn muốn mời",
									new IAction() {
										public void perform() {
											if (GameCanvas.inputDlg.tfInput.getText().length() > 0) {
												GlobalService
														.sendMessageInvitePlayerJoinClan(GameCanvas.inputDlg.tfInput
																.getText());
												GameCanvas.endDlg();
											} else {
												GameCanvas.startOKDlg("Nhập tên cần mời");
											}
										}
									}, TField.INPUT_TYPE_ANY);
							GameCanvas.inputDlg.show();
						}
					}));

					if (clanRole == HEADMAN) {
						vt.addElement(new Command("Chọn làm tộc phó", new IAction() {
							public void perform() {
								PlayerInfo clanMember = ((PlayerInfo) TabScr.gI().getSelectedItem());
								if (HDCGameMidlet.m_myPlayerInfo.itemName
										.equals(clanMember.itemName)) {
									GameCanvas
											.startOKDlg("Bạn không thể thực hiện điều này với chính mình");
								} else {
									if (((PlayerInfo) TabScr.gI().getSelectedItem()).role != PlayerInfo.ROLE_DEPUTY) {
										Vector vt = TabScr.gI().getListItems();
										int i;
										boolean found = false;
										PlayerInfo p = null;
										for (i = 0; i < vt.size(); i++) {
											p = (PlayerInfo) vt.elementAt(i);
											if (p.role == PlayerInfo.ROLE_DEPUTY) {
												found = true;

												break;
											}
										}
										String founder = "";
										if (found) {
											founder = p.itemName;
											p = null;
										}
										final String oldDeputy = founder;

										final String newDeputy = ((PlayerInfo) TabScr.gI()
												.getSelectedItem()).itemName;
										String title = "Bạn có muốn " + newDeputy
												+ " làm phó ban không?";
										if (found) {
											title = "Bạn có muốn đổi vị trí phó ban từ "
													+ oldDeputy + " cho người " + newDeputy
													+ " hay không?";
										}

										GameCanvas.startOKDlg(title, new IAction() {
											public void perform() {
												GameCanvas.startWaitDlg();
												GlobalService.sendMessageSetClanDeputy(oldDeputy,
														newDeputy);
											}
										});
										founder = null;
										p = null;
										title = null;
										vt = null;
									} else {
										GameCanvas
												.startOKDlg("Thành viên này đã thực sự là phó tộc");
									}
								}
								clanMember = null;
							}
						}));
					}

					// vt.addElement(new Command("Thao tác", new IAction() {
					// public void perform() {
					// }
					// }, action));
					// action = null;
				}

				// GameCanvas.instance.menu.startAt(vt, 0);
				MenuNhanh.startMenuNhanh(vt, GameCanvas.hw, GameCanvas.h / 4);
				vt = null;
			}
		});
		
//		//TODO set command footer
//		mFooter_BanBe.setCmd(MainScr.instance.m_cmdFriend);
//		mFooter_ChoiGame.setCmd(MainScr.instance.m_cmdPlay);
//		mFooter_GiaToc.setCmd(MainScr.instance.m_cmdClan);
//		mFooter_MuaSam.setCmd(MainScr.instance.m_cmdShop);
//		mFooter_XepHang.setCmd(MainScr.instance.m_cmdListLayer);
		
	}

	public void updateLetterClans(int num) {
		numberLetterClans = num;
		Item item = (Item) listItems.elementAt(1);
		item.m_iNumItem = (byte) num;
		item = null;
	}

	public void switchToMe() {
		GameCanvas.instance.currentDialog = null;
		m_wCell = 85;
		m_col = GameCanvas.w / m_wCell;
		m_strTitle = GameResource.clan;
		myClanOwner.itemId = 0;
		setCam();
		super.switchToMe();

		// TODO set location
		setLocation();

		// TODO cột trong tab avatar
		m_widthItem = GameResource.instance.m_frameGiaToc_Icon.frameWidth / 2 * 3;
		m_heightItem = GameResource.instance.m_frameGiaToc_Icon.frameHeight / 2 * 3;
		m_colAvt = GameResource.instance.imgListScr_Panel.getWidth() / m_widthItem;

		// TODO init button
		initButton();
	}

	// TODO set location for list item
	// m_left & m_top
	private void setLocation() {
		m_left = (GameCanvas.w - GameResource.instance.imgListScr_Panel.getWidth()) / 2;
		m_top = (int) (60 / HDCGameMidlet.instance.scale);

		// HDCGameMidlet.instance.Toast(m_left + " - " + m_top);
	}

	public void loadImg() {
		if (GameResource.instance.imgBox == null)
			GameResource.instance.imgBox = ImagePack.createImage(ImagePack.BOX_PNG);
		if (GameResource.instance.imgBoxFocus == null)
			GameResource.instance.imgBoxFocus = ImagePack.createImage(ImagePack.BOX_FOCUS_PNG);

		// if (HDCGameMidlet.instance.scale == 1f) {
		// } else {
		// float scale;
		// if ((GameCanvas.w <= 480 && GameCanvas.w > 320)
		// && (GameCanvas.h <= 320 && GameCanvas.h > 240)) {
		// }
		// // màn hình 240x320
		// // scale panel cho list screen tỷ lệ : 2/5
		// else {
		// scale = (float) 2 / 5;
		// GameResource.instance.imgListScr_Panel = ImagePack.createImage(
		// ImagePack.LIST_SCREEN_PANEL, scale);
		// // scale hight light
		// GameResource.instance.imgListScr_HightLight = Image
		// .scaleImage(
		// GameResource.instance.imgListScr_HightLight,
		// GameResource.instance.imgListScr_Panel
		// .getWidth(),
		// GameResource.instance.imgListScr_HightLight
		// .getHeight());
		// }
		// }
	}

	// update cho trạng thái trong ban hội
	public void updateStatusClan() {
		Item items = (Item) listItems.elementAt(0);

		if (hasMyClan) {
			// lấy tên ban hôi
			if (myClanOwner.itemName.equals("") || myClanOwner.itemName.length() == 0)
				items.itemName = GameResource.clan + " tôi";
			else {
				items.itemName = myClanOwner.itemName;
			}
			// lấy icon ban hội
			items.m_bItemType = -1;
			items.itemId = 1;
		} else {
			// chuyển thành trạng thái đăng ký
			myClanOwner.itemName = "";
			items.itemName = (String) LBL_CLANS.elementAt(0);// ten icon
			items.itemId = 0;// id của icon clan screen
			items.m_iNumItem = 0;// số luong lá thư
			items.m_bItemType = -1;// id của icon ban hội
		}
	}

	public void refeshClanScr() {
		GameCanvas.startWaitDlg();
		GlobalService.sendMessageGetClanName();
		GlobalService.sendMessageGetNumberNotificationJoinClan();
	}

	public void close() {
		hasMyClan = false;
		MainScr.gI().switchToMe();
		System.gc();
	}

	public void update() {
		degree += 20;
		if (degree == 360)
			degree = 0;
	}

	private void setCam() {
		int yP = (listItems.size()) / m_col;
		if (listItems.size() % m_col != 0)
			yP++;
		GameCanvas.cameraList.setInfo(m_left, m_top, m_wCell, m_wCell, m_width, yP * m_wCell,
				m_width, m_height, listItems.size());
	}

	// TODO select item (avartar - vật phẩm)
	private void selectItem() {
		if (GameCanvas.isPointer(m_left, m_top, GameResource.instance.imgListScr_Panel.getWidth(),
				GameCanvas.h - m_top)) {
			col_select = (int) (GameCanvas.px - m_left) / m_widthItem;
			row_select = (int) (GameCanvas.py - m_top) / m_heightItem;

			if (row_select * m_colAvt + col_select < listItems.size()) {
				if ((row_select * m_colAvt + col_select) != selectedIndex)
					selectedIndex = row_select * m_colAvt + col_select;
				else
					m_cmdCenter.action.perform();
				HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.box_plan);
				HDCGameMidlet.sound.play();
			}
		}
	}

	public void updateKey() {
		if (GameCanvas.isPointerClick) {
			selectItem();
		}

		// TODO updatekey for button
		mButton_CapNhat.updateKey();
		mButton_QuyenLoi.updateKey();
		super.updateKey();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showClanRegister() {
		Vector vt = new Vector();
		int i;
		int h = (int) (GameResource.instance.m_frameGiaToc_ThanhLap.totalHeight / GameResource.instance.m_frameGiaToc_ThanhLap.frameHeight);
		Clan m_Clan;
		for (i = 0; i < h; i++) {
			m_Clan = new Clan();
			m_Clan.itemId = i;
			vt.addElement(m_Clan);
		}
		ShopScr.gI().setListItems(vt, (byte) 1, 0, GameCanvas.hBottomBar + 90, GameCanvas.w,
				GameCanvas.h - (GameCanvas.hBottomBar << 1) - 90, "Thành lập gia tộc", 30, 13);
		ShopScr.gI().switchToMe(GameCanvas.currentScreen,
				new Command(GameResource.register, new IAction() {
					public void perform() {
						if (!ShopScr.gI().tfClanName.getText().equals("")
								&& ShopScr.gI().tfClanName.getText().length() > 0) {
							if (ShopScr.gI().tfClanName.getText().length() <= 30
									&& !BitmapFont.isHasSpecialCharacter(ShopScr.gI().tfClanName
											.getText())) {
								GlobalService.sendMessageCheckRegisterClan(ShopScr.gI().tfClanName
										.getText());
							} else {
//								GameCanvas
//										.startOKDlg("Tên gia tộc phải nhỏ hơn \n hoặc bằng 30 kí tự, "
//												+ "không chứa kí tự \n đặt biệt hay khoản trắng");
								HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Tên gia tộc phải nhỏ hơn \n hoặc bằng 30 kí tự, "
										+ "không chứa kí tự \n đặt biệt hay khoản trắng");								
							}
						} else {
//							GameCanvas
//									.startOKDlg("Tên gia tộc bạn muốn đăng ký \n không được bỏ trống");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Tên gia tộc bạn muốn đăng ký \n không được bỏ trống");							
						}
					}
				}));
		ShopScr.gI().setItems();
		vt = null;
	}

	public int clanRole = 3;
	public ListScr m_scrChatRoom;

	@SuppressWarnings("rawtypes")
	public void showMyClanScr(Message message) {
		clanRole = MessageIO.readInt(message);

		TabScr.gI().removeAll();
		Command close = new Command(GameResource.close, new IAction() {
			public void perform() {
				GameCanvas.startWaitDlg();
				GlobalService.sendMessageLeaveClanChatRoom();
				TabScr.gI().removeAll();
				refeshClanScr();
				TabScr.gI().close();
				m_scrChatRoom.close();
				m_scrChatRoom = null;
			}
		});
		// /////////
		m_scrChatRoom.m_cmdRight = close;
		m_scrChatRoom.m_cmdCenter = new Command(GameResource.send, new IAction() {
			public void perform() {

				if (m_scrChatRoom.tfChat.getText().length() > 0) {
					GlobalService.sendMessageChatInClanRoom(m_scrChatRoom.tfChat.getText());
					m_scrChatRoom.tfChat.setText("");
				}

			}
		});
		m_scrChatRoom.setChatRoom(true);

		TabScr.gI().addScreen(m_scrChatRoom, "Tán gẫu");

		m_strTitle = myClanOwner.itemName;
		// ////////
		ClanInformationScr.gI().setClanInfo(message, close);
		// ////////
		int numberClanMember = MessageIO.readInt(message);
		Vector listMembers = new Vector();
		int i;
		PlayerInfo p;
		boolean online;
		for (i = 0; i < numberClanMember; i++) {
			p = new PlayerInfo();
			p.itemName = MessageIO.readString(message);
			p.isChampion = MessageIO.readBoolean(message);
			p.role = MessageIO.readInt(message);
			online = MessageIO.readBoolean(message);
			p.onlineStatus = (byte) ((online) ? 1 : 0);
			p.gold = MessageIO.readLong(message);
			p.level = MessageIO.readInt(message);
			p.status = BitmapFont
					.opt_String(MessageIO.readString(message), GameResource.MAX_LENGTH);
			p.avatarId = MessageIO.readInt(message);
			listMembers.addElement(p);
		}

		ListTabScr scr = new ListTabScr();
		scr.m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);
		scr.m_iHeightItem = (int) (GameResource.instance.imgTabs_HightLightRow.getHeight());
		scr.setListItem(listMembers, null, menuPlayer, close, true);
		TabScr.gI().addScreen(scr, "Thành viên");
		TabScr.gI().addScreen(ClanInformationScr.gI(), GameResource.info);
		TabScr.gI().setActiveScr((Screen) TabScr.gI().m_vtScr.get(0));
		TabScr.gI().m_strTitle = ClanInformationScr.gI().clanName;
		TabScr.gI().switchToMe(ClanScr.gI());

		p = null;
		listMembers = null;
		scr = null;
		close = null;
		GameCanvas.endDlg();
		System.gc();
	}

	public void addMember(String nickMember) {
		if (m_scrChatRoom != null && !m_scrChatRoom.m_arrListMembers.contains(nickMember)) {
			m_scrChatRoom.m_arrListMembers.insertElementAt(nickMember, 0);
			m_scrChatRoom.addChatContent(nickMember + GameResource.joinRoom, 0xffffff);
		}
	}

	public void removeMember(String nickMember) {
		if (m_scrChatRoom != null) {
			m_scrChatRoom.m_arrListMembers.removeElement(nickMember);
			m_scrChatRoom.addChatContent(nickMember + GameResource.outRoom, 0xffffff);
		}
	}

	public void addChatRoom(Message message) {
		String chatContent = MessageIO.readString(message);
		int color = MessageIO.readInt(message);
		if (m_scrChatRoom != null)
			m_scrChatRoom.addChatContent(chatContent, color);
		chatContent = null;
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		HDCGameMidlet.instance.gameCanvas.menu.m_list = null;
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		m_cmdRight.action.perform();
	}
}
