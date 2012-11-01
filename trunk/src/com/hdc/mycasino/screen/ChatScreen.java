package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.TField;

public class ChatScreen extends Screen {
	public static int X = 38, Y = 31;
	public Vector tabs = new Vector();
	public Screen lastScr;
	public int selectedTab;
	public TField tfChat;
	private boolean showTfChat = true;
	public boolean flag;
	private ChatTab currentTab = null;

	public static ChatScreen instance;

	public static ChatScreen getInstance() {
		if (instance == null)
			instance = new ChatScreen();
		return instance;
	}

	public void switchToMe(Screen lastScr) {
		this.lastScr = lastScr;
		if (tabs.size() > 0) {
			selectedTab = tabs.size() - 1;
			PaintPopup.gI().setInfo(findTab(PaintPopup.gI().name).title /*
																		 * getTab(
																		 * selectedTab
																		 * )
																		 * .title
																		 */, GameCanvas.w - X * 2,
					GameCanvas.h - Y * 2, tabs.size());
			setChangeTabEnd();
			showTfChat = getTab(selectedTab).showChatTf;
		}
		super.switchToMe();
	}

	public ChatScreen() {
		tfChat = new TField();
		tfChat.x = X + 5;
		tfChat.y = GameCanvas.h - Screen.ITEM_HEIGHT - Y - 7;
		tfChat.width = GameCanvas.w - X * 2 - 10;
		tfChat.height = Screen.ITEM_HEIGHT + 2;
		tfChat.isFocus = true;
		tfChat.setMaxTextLenght(40);
		selectedTab = 0;
		m_cmdLeft = new Command(GameResource.menu, new IAction() {
			public void perform() {
				Vector menu = new Vector();
				if (selectedTab != 0)
					menu.addElement(new Command(GameResource.closeTab, new IAction() {
						public void perform() {
							removeTab(getTab(selectedTab));
						}
					}));

				menu.addElement(new Command(GameResource.close, new IAction() {
					public void perform() {
						close();
					}
				}));
				GameCanvas.instance.menu.startAt(menu, 0);
			}
		});

		setChangeTabEnd();
	}

	public void close() {
		lastScr.switchToMe();
	}

	public void setChangeTabEnd() {
		if (tabs.size() == 0)
			return;
		this.m_cmdCenter = getTab(selectedTab).center;
		this.m_cmdRight = getTab(selectedTab).right;
		tfChat.setText(getTab(selectedTab).currentChatText);
		showTfChat = getTab(selectedTab).showChatTf;
		getTab(selectedTab).reset();
		if (GameCanvas.currentScreen == this)
			PaintPopup.gI().setNameAndFocus(getTab(selectedTab).title, selectedTab);
	}

	private void setChangeTabStart() {
		getTab(selectedTab).currentChatText = tfChat.getText();
	}

	public void onChatFrom(int id, String nick, String content, int color) {
		if (!nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
			currentTab = findTab(nick);
			if (currentTab == null) {
				currentTab = new ChatTab(nick, nick, new Command(GameResource.chat, new IAction() {
					public void perform() {
						doSendMsg();
					}
				}), null, true);
				addTab(currentTab);
			}
		}

		if (currentTab != null) {
			currentTab.addText(content, color);
		}
	}

	protected void doSendMsg() {
		if (tfChat.getText().equals("") || tfChat.getText().length() == 0)
			return;
		ChatTab chatTab = getTab(selectedTab);
		String text = tfChat.getText();

		GlobalService.onSendMsgChat(chatTab.m_strOpponentName, text);
		tfChat.setText("");
		chatTab = null;
	}

	public ChatTab findTab(String strName) {
		int a = tabs.size();
		for (int i = 0; i < a; i++) {
			if (((ChatTab) tabs.elementAt(i)).m_strOpponentName.equals(strName))
				return (ChatTab) tabs.elementAt(i);
		}
		return null;
	}

	public ChatTab getTab(int index) {
		if (index < tabs.size())
			return (ChatTab) tabs.elementAt(index);
		else
			return null;
	}

	public void paint(Graphics g) {
		lastScr.paint(g);

		g.translate(-g.getTranslateX(), -g.getTranslateY());
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		PaintPopup.gI().paintTabs(g);

		g.translate(X, Y + 22);

		ChatTab chatTab = getTab(selectedTab);
		if (chatTab != null) {
			chatTab.paint(g);
			if (showTfChat) {
				g.translate(-g.getTranslateX(), -g.getTranslateY());
				tfChat.paint(g);
			}
		}
		chatTab = null;
		super.paint(g);
	}

	public void keyPress(int keyCode) {
		if (keyCode == -3) {
			setChangeTabStart();
			selectedTab--;
			if (selectedTab < 0)
				selectedTab = tabs.size() - 1;
			setChangeTabEnd();
			getTab(selectedTab).initScroll();
		}
		if (keyCode == -4) {
			setChangeTabStart();
			selectedTab++;
			if (selectedTab >= tabs.size())
				selectedTab = 0;
			setChangeTabEnd();
			getTab(selectedTab).initScroll();
		}
		// if (showTfChat)
		// tfChat.keyPressed(keyCode);
		super.keyPress(keyCode);
	}

	public void updateKey() {
		if (GameCanvas.keyHold[2])
			getTab(selectedTab).update(-1);
		if (GameCanvas.keyHold[8])
			getTab(selectedTab).update(1);
		super.updateKey();
	}

	public void update() {
		if (showTfChat)
			tfChat.update();
		lastScr.update();
	}

	public void removeTab(ChatTab ct) {
		tabs.removeElement(ct);
		if (selectedTab >= tabs.size())
			selectedTab = tabs.size() - 1;
		PaintPopup.gI().focusTab = selectedTab;
		PaintPopup.gI().setNumTab(tabs.size());
		setChangeTabEnd();
	}

	public void startChatTo(int iDDB, String name) {
		flag = false;
		currentTab = findTab(name);
		if (currentTab == null) {
			currentTab = new ChatTab(name, name, new Command(GameResource.chat, new IAction() {
				public void perform() {
					doSendMsg();
				}
			}), null, true);
			addTab(currentTab);
		}
		// Find this tab
		int a = tabs.size();
		for (int i = 0; i < a; i++) {
			if (tabs.elementAt(i) == currentTab) {
				selectedTab = i;
				PaintPopup.gI().name = name;
			}
		}
	}

	public void addTab(ChatTab tab) {
		tabs.addElement(tab);
		if (GameCanvas.currentScreen == this) {
			PaintPopup.gI().setNumTab(tabs.size());
		}
	}

	public void paintIconChat(Graphics g) {
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		close();
	}
}