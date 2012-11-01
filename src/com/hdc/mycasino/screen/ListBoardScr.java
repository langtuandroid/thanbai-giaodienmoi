package com.hdc.mycasino.screen;

import java.util.Vector;

import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.messageHandler.GlobalMsgHandler;
import com.hdc.mycasino.model.BoardInfo;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.RoomInfo;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.GameResource;

public class ListBoardScr extends ShopScr {
	private static ListBoardScr instance;
	Command cmdSellect, cmdMenu, cmdClose;
	public int romType = 0;
	public String m_strTitleGame;

	public static ListBoardScr getInstance() {
		return ((instance == null) ? (instance = new ListBoardScr()) : instance);
	}

	public void switchToMe(Screen lastScreen, int roomType) {
		super.switchToMe(lastScreen, null);
		m_cmdRight = cmdClose;
		m_cmdCenter = cmdSellect;
		m_cmdLeft = cmdMenu;
		m_selected = 0;
		this.romType = roomType;
		if (roomType == RoomInfo.CLAN) {
			m_wCell = 120;
			deltaWidth = 30;
		} else {
			m_wCell = 60;
			deltaWidth = 24;
		}
		m_cmdCenter = cmdSellect;
		ChatTextField.gI().isShow = false;
		m_strTitle = m_strTitleGame + " - Phòng " + PlayGameScr.roomCode;

		if (m_arrItems != null && m_arrItems.size() > 0) {
			// initCam();
		}
	}

	public ListBoardScr() {
		m_left = 0;
		m_top = GameCanvas.hBottomBar + 10;
		m_width = GameCanvas.w;
		m_height = GameCanvas.h - m_top;

		cmdMenu = new Command(GameResource.menu, new IAction() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void perform() {
				Vector menu = new Vector();
				menu.addElement(new Command(GameResource.update, new IAction() {
					public void perform() {
						GlobalService.onUpdateRoom();
					}
				}));

				// menu.addElement(new Command("Nạp Dina", new IAction() {
				// public void perform() {
				// MainScr.gI().sms();
				// }
				// }));

				menu.addElement(new Command("Đổi dina", new IAction() {

					public void perform() {
						HDCGameMidlet.instance.showDialog_DoiDina();
					}
				}));

				menu.addElement(new Command(GameResource.mail, new IAction() {
					public void perform() {
						GameCanvas.startWaitDlg();
						GlobalService.onGetInboxMessage();
					}
				}));

				menu.addElement(new Command(GameResource.owner, new IAction() {
					public void perform() {
						GlobalService.sendMessageGetPlayerProfile();
						GameCanvas.startWaitDlg();
					}
				}));

				menu.addElement(new Command(GameResource.shop, new IAction() {
					public void perform() {
						GameCanvas.startWaitDlg();
						GlobalService.onGetListShopAvatars();
					}
				}));

				menu.addElement(new Command("Tìm bàn chơi", new IAction() {
					public void perform() {
						GlobalService.sendMessageFindBoard();
					}
				}));

				GameCanvas.instance.menu.startAt(menu, 0);
			}
		});

		cmdSellect = new Command(GameResource.select, new IAction() {
			public void perform() {
				doJoinBoard();
			}
		});

		cmdClose = new Command(GameResource.close, new IAction() {
			public void perform() {
				if (GameCanvas.currentScreen instanceof ListBoardScr) {
					GameCanvas.startWaitDlg();
					GlobalService.sendMessageLeaveRoom();
					GlobalService.sendMessageUpdateListRoom();
				}
				// close();
			}
		});

	}

	public void doJoinBoard() {
		BoardInfo selectedBoard = (BoardInfo) m_arrItems.elementAt(m_selected);

		boolean isFull = selectedBoard.isFull();
		if (isFull) {
			// GameCanvas.startOKDlg("Bàn đã đầy");
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Bàn đã đầy");

		}
		if (selectedBoard.betGold > HDCGameMidlet.m_myPlayerInfo.gold) {
			// GameCanvas.startOKDlg("Bạn không đủ gold để vào bàn!");
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Bạn không đủ gold để vào bàn!");
			return;
		}
		if (selectedBoard.isLock) {
			GameCanvas.inputDlg.setInfo(GameResource.password, new IAction() {
				public void perform() {
					BoardInfo selectedBoard = (BoardInfo) m_arrItems.elementAt(m_selected);
					GlobalService.sendMessageToJoinTable(selectedBoard.itemId, GameCanvas.inputDlg.tfInput.getText(),
							false);
					GameCanvas.startWaitDlg();
					GameCanvas.endDlg();
				}
			}, 0);
			GameCanvas.inputDlg.show();
		} else {
			GlobalService.sendMessageToJoinTable(selectedBoard.itemId, "", false);
			PlayGameScr.boardId = selectedBoard.itemId;
			GlobalMsgHandler.gI().curScr.m_strTitle = (m_strTitle + " - Bàn " + PlayGameScr.boardId);
			GameCanvas.startWaitDlg();
		}
	}

	@SuppressWarnings("rawtypes")
	public void setBoardList(Vector boardList) {
		m_width = GameCanvas.w;

		m_left = 0;
		m_top = GameCanvas.h / 8;
		m_width = GameCanvas.w;
		m_height = GameCanvas.h / 8 * 7;

		m_cmdRight = cmdClose;
		m_cmdCenter = cmdSellect;
		m_cmdLeft = cmdMenu;
		m_selected = -1;
		
//		for(int i = 0 ; i < boardList.size();i++){
//			HDCGameMidlet.instance.Toast("Bàn " + i + " BET " + ((BoardInfo)boardList.get(i)).betGold + " Num " + ((BoardInfo)boardList.get(i)).numberPlayer);
//		}
		
		setListItems(boardList, (byte) 4, m_left, m_top, m_width, m_height, m_strTitle, m_wCell, deltaWidth);
	}

	public void setBetGoldOfTable(int tableId, long betGold) {
		if (m_arrItems != null && m_arrItems.size() > 0) {
			BoardInfo boardInfo;
			for (int i = 0; i < m_arrItems.size(); i++) {
				boardInfo = (BoardInfo) m_arrItems.elementAt(i);
				if (boardInfo.itemId == tableId) {
					boardInfo.betGold = betGold;
				}
			}
			boardInfo = null;
		}
	}

	@Override
	public void updateKey() {
		// TODO Auto-generated method stub
		
		if (GameCanvas.isPointer(0, 0, GameResource.instance.imgMenuBg.getWidth() / 2 * 3,
				GameResource.instance.imgMenuBg.getHeight() / 2 * 3)
				&& Math.abs(GameCanvas.instance.pyLast - GameCanvas.instance.py) < 5) {
			HDCGameMidlet.instance.m_viberator.vibrate(100);
			HDCGameMidlet.instance.onBackPressed();
		}
		
//		if (GameCanvas.isPointer(
//				GameCanvas.w - GameResource.instance.imgMenuBg.getWidth(), 0,
//				GameResource.instance.imgMenuBg.getWidth(),
//				GameResource.instance.imgMenuBg.getHeight())) {
//			HDCGameMidlet.instance.gameCanvas.currentScreen.actionMenu();
//		}
		
		if (GameCanvas.isPointer(2 * GameResource.instance.imgMenuBg.getWidth(), 0,
				GameResource.instance.imgHeaderDinaBg.getWidth(),
				GameResource.instance.imgHeaderDinaBg.getHeight() / 2 * 3)) {
			if (!(GameCanvas.instance.currentScreen instanceof TabScr))
				MainScr.gI().sms();
			else {
				HDCGameMidlet.instance
						.Toast("Không thể nạp dina \n ở màn hình này !!!");
			}
		}		
		
		super.updateKey();
		
	}
	
	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		if (cmdClose != null)
			cmdClose.action.perform();
		
		super.doBack();
	}

}
