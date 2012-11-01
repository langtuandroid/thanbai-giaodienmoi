package com.hdc.mycasino.screen;

import java.util.Vector;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.view.ListView;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Button;
import com.hdc.mycasino.model.ChatInfo;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.MyObj;
import com.hdc.mycasino.model.PlayerInfo;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.TField;

@SuppressWarnings("unused")
public class ListScr extends Screen {

	static ListScr instance;

	public static ListScr gI() {
		if (instance == null) {
			instance = new ListScr();
		}
		return instance;
	}

	Command cmdClose;
	Command cmdMenu;
	public int i;
	public Vector m_arrItems;
	public Screen backScreen;
	public int itemHeight = 50;
	// public int m_iTopScroll = 60;
	boolean changeFocus;
	// //////////variable is scroll
	int curHeight = 0; // current height of selected item
	int totalHeight = 0;
	// /////////////////////variable for chat room
	public boolean m_bIsChatRoom;
	public Vector m_arrListMembers;
	boolean isShowListMembers;
	byte m_iMaxMember = 0;
	// /////////////////variable is paint player
	boolean m_bIsPlayer = true;
	boolean m_bIsPaint = true;

	public int m_iHeightItem;// = (int) (70 / HDCGameMidlet.instance.scale);
	public int m_iTopScroll;// = (int) (70 / HDCGameMidlet.instance.scale);
	private Image imgBackground;
	public TField tfChat;
	private Button mButton_Gui;

	// TODO listview
	private ListView m_ListView;

	public void setListItem(Vector items, Vector member, boolean isPaint, boolean isChat,
			boolean isPlayer) {
		m_bIsChatRoom = false;
		m_arrListMembers = member;
		isShowListMembers = true;
		// /////////////
		m_arrItems = items;
		curHeight = 0;
		m_strTitle = "";
		totalHeight = 0;

		init();
		m_selected = 0;

		m_bIsChatRoom = isChat;
		m_bIsPaint = isPaint;

		isShowListMembers = false;
		m_width = GameCanvas.w;
		if (isChat) {
			isShowListMembers = true;
			m_width = GameCanvas.w - 70;
			m_cmdLeft = cmdMenu;
		}
		m_bIsPlayer = isPlayer;
		// m_height = GameCanvas.h - m_iTopScroll - GameCanvas.hBottomBar;

		// TODO set location
		setLocation();

		if (m_ListView == null && m_arrItems != null && m_arrItems.size() > 0) {
			m_ListView = new ListView();
			m_ListView.setInfo(m_left, m_top + m_iTopScroll,
					GameResource.instance.imgListScr_Panel.getWidth(), GameCanvas.h - m_top - 2
							* m_iTopScroll, m_iHeightItem, m_arrItems);
			m_ListView.setCommand(m_cmdCenter);
		}
	}

	@Override
	public void loadImg() {
		// TODO Auto-generated method stub
		super.loadImg();

		// if (HDCGameMidlet.instance.scale == 2f) {
		//
		// float scale;
		// scale = (float) 2 / 5;
		// GameResource.instance.imgListScr_Panel = ImagePack.createImage(
		// ImagePack.LIST_SCREEN_PANEL, scale);
		// // scale hight light
		// GameResource.instance.imgListScr_HightLight = Image.scaleImage(
		// GameResource.instance.imgListScr_HightLight,
		// GameResource.instance.imgListScr_Panel.getWidth(),
		// GameResource.instance.imgListScr_HightLight.getHeight());
		//
		// // scale hight light
		// GameResource.instance.imgTabs_HightLightRow = Image.scaleImage(
		// GameResource.instance.imgTabs_HightLightRow,
		// GameResource.instance.imgListScr_Panel.getWidth(),
		// GameResource.instance.imgTabs_HightLightRow.getHeight());
		// }

	}

	public void init() {

		// Scroll.gI().init(m_height - m_iTopScroll,
		// m_arrItems.size() * m_iHeightItem, CameraList.cmy);
		//
		// GameCanvas.cameraList.setInfo(/* m_left */0, /* m_top */0 +
		// m_iTopScroll
		// + m_iHeightItem, m_width, m_iHeightItem, m_width,
		// m_arrItems.size() * m_iHeightItem, m_width, m_height
		// - m_iHeightItem - m_iTopScroll, m_arrItems.size());
	}

	public void addChatContent(String chatContent, int color) {
		ChatInfo chatInfo = new ChatInfo(chatContent, color);
		m_arrItems.addElement(chatInfo);
		setSelected(m_arrItems.size() - 1);
		// add height of item
		totalHeight += chatInfo.getHeightItem(m_width);
		curHeight = totalHeight;

		// init camera
		init();
		// setCam();
		chatInfo = null;
	}

	public ListScr() {
		m_width = GameResource.instance.imgListScr_HightLight.getWidth();
		m_height = (int) (GameCanvas.h - (60 / HDCGameMidlet.instance.scale));
		m_left = 0;
		m_top = 0;
		m_iMaxMember = (byte) ((GameCanvas.h - GameCanvas.hBottomBar) / (ITEM_HEIGHT + 5));

		cmdClose = new Command(GameResource.close, new IAction() {
			public void perform() {
				close();
				if (backScreen instanceof ListBoardScr) {
					// ListBoardScr.getInstance().initCam();
				}
			}
		});

		cmdMenu = new Command(GameResource.menu, new IAction() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void perform() {
				Vector vt = new Vector();
				vt.addElement(new Command("ẩn danh sách", new IAction() {
					public void perform() {
						isShowListMembers = false;
						m_width = GameCanvas.w;
					}
				}));
				vt.addElement(new Command("danh sách người chat", new IAction() {
					public void perform() {
						isShowListMembers = true;
						m_width = GameCanvas.w - 70;
					}
				}));
				GameCanvas.instance.menu.startAt(vt, 0);
				vt = null;
			}
		});
	}

	public void switchToMe(Screen backScr, String title, Command leftCmd, Command centerCmd,
			Command rightCmd) {
		setSelected(-1);
		cmy = cmtoY = 0;

		m_cmdLeft = null;
		m_cmdLeft = leftCmd;

		if (rightCmd == null)
			m_cmdRight = cmdClose;
		else
			m_cmdRight = rightCmd;

		m_cmdCenter = null;
		m_cmdCenter = centerCmd;
		backScreen = backScr;

		m_strTitle = title;

		super.switchToMe();

		// TODO setlocation
		setLocation();

		// TODO set list view
		if (m_ListView == null && m_arrItems != null && m_arrItems.size() > 0) {
			m_ListView = new ListView();
			m_ListView.setInfo(m_left, m_top + m_iTopScroll,
					GameResource.instance.imgListScr_Panel.getWidth(), GameCanvas.h - m_top - 2
							* m_iTopScroll, m_iHeightItem, m_arrItems);
			m_ListView.setCommand(m_cmdCenter);
		}

	}

	// TODO set location for list item
	// m_left & m_top
	private void setLocation() {
		m_left = (GameCanvas.w - GameResource.instance.imgListScr_Panel.getWidth()) / 2;
		m_top = (int) (60 / HDCGameMidlet.instance.scale);

		// HDCGameMidlet.instance.Toast(m_left + " - " + m_top);
	}

	public void paint(Graphics g) {

		if (m_bIsPaint) {
			// TODO paint background header
			PaintPopup.paintBackground(g, m_strTitle);
			// TODO paint panel
			g.drawImage(GameResource.instance.imgListScr_Panel, m_left, m_top, Graphics.LEFT
					| Graphics.TOP);
		}
		if (m_ListView != null && !m_bIsChatRoom)
			m_ListView.paint(g);

		if (isShowListMembers) {
			g.translate(-g.getTranslateX(), -g.getTranslateY());
			int col = (GameCanvas.h - m_top - GameResource.instance.imgTabs_2_0.getHeight())
					/ GameResource.instance.imgPopupLine_1.getHeight();
			for (int i = 0; i < col; i++) {
				g.drawImage(GameResource.instance.imgPopupLine_1, m_left
						+ GameResource.instance.imgListScr_Panel.getWidth() / 4 * 3, m_top
						+ GameResource.instance.imgTabs_2_0.getHeight() + i
						* GameResource.instance.imgPopupLine_1.getHeight(), Graphics.LEFT
						| Graphics.TOP);
			}

			if (m_arrListMembers != null && m_arrListMembers.size() > 0) {
				int length = m_iMaxMember;
				if (m_arrListMembers.size() < m_iMaxMember)
					length = m_arrListMembers.size();

				for (i = 0; i < length; i++) {
					// g.drawRegion(GameResource.instance.imgChat_Man, 0, 81, 9,
					// 16,
					// 0, GameCanvas.w - 70 + 5, m_iTopScroll + i
					// * ITEM_HEIGHT + 5, Graphics.LEFT
					// | Graphics.VCENTER);
					g.drawImage(GameResource.instance.imgChat_Man, m_left
							+ GameResource.instance.imgListScr_Panel.getWidth() / 4 * 3
							+ GameResource.instance.imgChat_Man.getWidth() / 2, m_top
							+ GameResource.instance.imgTabs_2_0.getHeight() + i
							* GameResource.instance.imgChat_Man.getHeight(), Graphics.LEFT
							| Graphics.TOP);
					BitmapFont.drawBoldFont(g, (String) m_arrListMembers.elementAt(i), m_left
							+ GameResource.instance.imgListScr_Panel.getWidth() / 4 * 3
							+ GameResource.instance.imgChat_Man.getWidth() / 2 * 3, m_top
							+ GameResource.instance.imgTabs_2_0.getHeight() + i
							* GameResource.instance.imgChat_Man.getHeight()
							+ GameResource.instance.imgChat_Man.getHeight() / 2, 0xFFFFFF,
							Graphics.LEFT | Graphics.VCENTER);
				}
			}

		}

		if (m_bIsChatRoom) {
			g.drawImage(imgBackground, 0, GameCanvas.h, Graphics.LEFT | Graphics.BOTTOM);
			mButton_Gui.paint(g);
			tfChat.paint(g);
			paintList(g);
		}

		// if (isShowListMembers) {
		// g.translate(-g.getTranslateX(), -g.getTranslateY());
		// int col = (GameCanvas.h - m_top - GameResource.instance.imgTabs_2_0
		// .getHeight())
		// / GameResource.instance.imgPopupLine_1.getHeight();
		// for (int i = 0; i < col; i++) {
		// g.drawImage(
		// GameResource.instance.imgPopupLine_1,
		// m_left
		// + GameResource.instance.imgListScr_Panel
		// .getWidth() / 4 * 3,
		// m_top
		// + GameResource.instance.imgTabs_2_0.getHeight()
		// + i
		// * GameResource.instance.imgPopupLine_1
		// .getHeight(), Graphics.LEFT
		// | Graphics.TOP);
		// }

		// g.translate(-g.getTranslateX(), -g.getTranslateY());
		//
		// g.setClip(GameCanvas.w - 70, GameCanvas.hBottomBar, 70,
		// GameCanvas.h - GameCanvas.hBottomBar);
		//
		// // Draw separated line between list chat contents with list
		// members.
		// g.setColor(0xffffff);
		// g.drawLine(GameCanvas.w - 70, GameCanvas.hBottomBar,
		// GameCanvas.w - 70, GameCanvas.h - GameCanvas.hBottomBar);
		// // Draw list members. just draw some members because we cannot
		// have
		// // enough height to draw.
		// if (m_arrListMembers != null && m_arrListMembers.size() > 0) {
		// int length = m_iMaxMember;
		// if (m_arrListMembers.size() < m_iMaxMember)
		// length = m_arrListMembers.size();
		//
		// for (i = 0; i < length; i++) {
		// g.drawRegion(GameResource.instance.imgIconCard, 0, 81, 9, 16,
		// 0, GameCanvas.w - 70 + 5, m_iTopScroll + i
		// * ITEM_HEIGHT + 5, Graphics.LEFT
		// | Graphics.VCENTER);
		// BitmapFont.drawBoldFont(g,
		// (String) m_arrListMembers.elementAt(i),
		// GameCanvas.w - 70 + 20, m_iTopScroll + i * ITEM_HEIGHT
		// + 2, 0xFFFFFF, Graphics.LEFT | Graphics.TOP);
		// }
		// }
		// }

		if (m_bIsPaint)
			super.paint(g);

	}

	// TODO set chat room
	public void setChatRoom(boolean isChat) {
		m_bIsChatRoom = isChat;
		isShowListMembers = true;

		// TODO create background
		imgBackground = PaintPopup.gI().createImgBackground_Popup(
				GameCanvas.w / GameResource.instance.imgPoupPanel.getHeight(), 3);

		// TODO set text field
		tfChat = new TField();
		tfChat.x = 1 / 16 * GameCanvas.w;
		tfChat.height = (int) (50 / HDCGameMidlet.instance.scale);
		tfChat.y = (GameCanvas.h - imgBackground.getHeight())
				+ (imgBackground.getHeight() - tfChat.height) / 2;
		tfChat.width = GameCanvas.w / 4 * 3;

		tfChat.isPaintBG = false;
		tfChat.isFocus = true;
		tfChat.setColor_TF(Color.WHITE);
		tfChat.setAlpha_TF(255);
		tfChat.setTypeTF(1);

		// TODO init button
		initButton();

		// set width for textfield
		tfChat.width = GameCanvas.w - mButton_Gui.w / 2 * 3;
		tfChat.x = mButton_Gui.w / 8;
	}

	// TODO init button
	private void initButton() {
		mButton_Gui = new Button(GameResource.instance.imgButton_Login, "Gửi", m_cmdCenter);
		mButton_Gui.setXY(
				GameCanvas.w - mButton_Gui.w / 4 * 5,
				(GameCanvas.h - imgBackground.getHeight())
						+ (imgBackground.getHeight() - GameResource.instance.imgButton_Login
								.getHeight()) / 2);
//		mButton_Gui.startEffect();
	}

	protected void paintList(Graphics g) {
		// g.translate(0, m_top);
		//
		// Scroll.gI().paintScroll(g, m_left + m_width - 10, m_iTopScroll);
		// int y = m_iTopScroll;
		//
		// g.setClip(m_left, m_iTopScroll, m_width, m_height);
		//
		// g.translate(0, cmy);

		g.translate(0, m_top);

		Scroll.gI().paintScroll(g, /* m_left + m_width - 10 */
		GameCanvas.w - 10, m_iTopScroll);
		// Position pos = GameCanvas.getMinMaxFor(CameraList.cmy, m_iHeightItem
		// * m_arrItems.size(), m_arrItems.size(), m_arrItems.size(),
		// GameCanvas.h);
		int y = 0;
		// y += m_iTopScroll * (pos.x + 1);

		g.setClip(m_left, m_iTopScroll, m_width, m_height - m_iTopScroll
		/*- m_iHeightItem*/);

		// g.translate(0, -CameraList.cmy);

		if (m_bIsChatRoom) {
			ChatInfo ava;
			for (i = 0; i < m_arrItems.size(); i++) {
				ava = (ChatInfo) m_arrItems.elementAt(i);
				ava.paintInRow(g, m_left + 10, y, m_width - 20, ava.heightItem);

				y += ava.heightItem;
			}
			ava = null;
		} else {
			MyObj ava;
			for (i = 0; i < m_arrItems.size(); i++) {
				ava = (MyObj) m_arrItems.elementAt(i);
				if (i == m_selected) {
					// g.drawImage(GameResource.instance.imgTabs_HightLightRow,
					// GameCanvas.w / 2, y, Graphics.HCENTER
					// | Graphics.TOP);

					g.setColor(0x054B02);
					g.setAlpha(150);
					g.fillRoundRectWithTransparenr(m_left, y,
							GameResource.instance.imgListScr_Panel.getWidth(), m_iHeightItem,
							(int) (8 / HDCGameMidlet.instance.scale),
							(int) (8 / HDCGameMidlet.instance.scale));

					g.setColor(Color.BLACK);
					g.setAlpha(120);
					g.fillRoundRectWithTransparenr(m_left + 2, y + 2,
							GameResource.instance.imgListScr_Panel.getWidth() - 4,
							m_iHeightItem - 4, (int) (8 / HDCGameMidlet.instance.scale),
							(int) (8 / HDCGameMidlet.instance.scale));

				}

				ava.paintInRow(g, m_left + (int) (30 / HDCGameMidlet.instance.scale), y, m_width,
						GameResource.instance.imgTabs_HightLightRow.getHeight());

				y += GameResource.instance.imgTabs_HightLightRow.getHeight();

				if (m_bIsPlayer && (m_arrItems.elementAt(i) instanceof PlayerInfo)) {
					((PlayerInfo) m_arrItems.elementAt(i)).update();
				}
			}
			ava = null;
		}
	}

	public void updateKey() {
		if (m_ListView != null && !m_bIsChatRoom) {
			m_ListView.updateKey();
			if (m_selected != m_ListView.getIndex())
				m_selected = m_ListView.getIndex();
		}

		if (m_bIsChatRoom) {
			mButton_Gui.updateKey();
			tfChat.update();
		}

		if (GameCanvas.instance.menu.m_showMenu || GameCanvas.currentDialog != null)
			return;

		changeFocus = false;

		// if (GameCanvas.instance.hasPointerEvents() &&
		// GameCanvas.isPointer(m_left, m_iTopScroll, m_width, m_height)) {
		//
		// if (GameCanvas.isPointerDown) {
		// int pa = GameCanvas.pyLast - GameCanvas.py;
		// curHeight += pa;
		// changeFocus = true;
		// }
		//
		// if (GameCanvas.isPointerClick) {
		// int heightClick = GameCanvas.py - m_iTopScroll - cmy;
		// int tmpHeight = 0;
		// if (m_bIsChatRoom) {
		// ChatInfo chat;
		// for (i = 0; i < m_arrItems.size(); i++) {
		// chat = (ChatInfo) m_arrItems.elementAt(i);
		// if (heightClick >= tmpHeight && heightClick <= tmpHeight +
		// chat.heightItem) {
		//
		// m_selected = i;
		// changeFocus = true;
		// curHeight = tmpHeight + chat.heightItem;
		// setSelected(m_selected);
		// break;
		// }
		//
		// tmpHeight += chat.heightItem;
		// }
		// chat = null;
		// } else {
		// for (i = 0; i < m_arrItems.size(); i++) {
		// if (heightClick >= tmpHeight && heightClick <= tmpHeight +
		// itemHeight) {
		// if (i == m_selected && m_cmdCenter != null) {
		// m_cmdCenter.action.perform();
		// break;
		// }
		//
		// m_selected = i;
		// changeFocus = true;
		// curHeight = tmpHeight + itemHeight;
		// setSelected(m_selected);
		// break;
		// }
		// tmpHeight += itemHeight;
		// }
		// }
		// }
		// }

		// if (changeFocus)
		// setCam();

		if (m_bIsPaint)
			super.updateKey();
	}

	public void update() {
		// Scroll.gI().updateScroll(CameraList.cmy, CameraList.cmtoY);
		if (m_ListView != null)
			m_ListView.update();
	}

	public MyObj getSelectItems() {
		if (m_selected >= 0 && m_selected < m_arrItems.size()) {
			return (MyObj) m_arrItems.elementAt(m_selected);
		}
		return null;
	}

	public void close() {
		m_cmdCenter = null;
		m_cmdRight = null;
		m_cmdLeft = null;
		// GameCanvas.cameraList.close();

		if (backScreen != null)
			backScreen.switchToMe();

		m_strTitle = null;
		m_arrItems = null;
		backScreen = null;
		m_arrListMembers = null;
	}

	public int getTop() {
		return (m_top + m_selected * m_iHeightItem);
	}

	public void paintIconChat(Graphics g) {
		if (m_strTitle != null && !m_strTitle.equals(GameResource.friend)) {
			super.paintIconChat(g);
		}
	}

	public void setSelected(int index) {
		// if (index != m_selected) {
		// super.setSelected(index);
		// } else if (m_arrItems != null && m_arrItems.size() > 0) {
		// if (m_cmdCenter != null) {
		// if (!m_bIsChatRoom)
		// m_cmdCenter.action.perform();
		// }
		// }
	}

	public void removeSelectedItem(boolean isAll) {
		if (isAll)
			m_arrItems.removeAllElements();
		else
			m_arrItems.removeElementAt(m_selected);

		if (m_arrItems.size() == 0) {
			m_cmdLeft = null;
			m_cmdCenter = null;
		} else {
			setSelected(-1);
		}
		init();
		// setCam();
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		if (m_cmdLeft != null)
			m_cmdLeft.action.perform();
		else
			GameCanvas.instance.menu.m_list = null;
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		// close();
		HDCGameMidlet.instance.Toast("doback - ListScr");
		if (m_cmdRight != null)
			m_cmdRight.action.perform();
	}
}