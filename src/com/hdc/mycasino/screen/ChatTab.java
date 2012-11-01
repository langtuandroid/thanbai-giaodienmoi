package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.ChatInfo;
import com.hdc.mycasino.model.Command;

public class ChatTab {
	public String title;
	public Command center;
	public Command right;
	private Vector listChatContents = new Vector();
	public String currentChatText = "";
	public String m_strOpponentName;
	public boolean showChatTf;
	public int dis;
	public int l = 13;
	public int limit = 0;
	public static int cmtoY, cmy, cmdy, cmvy;

	public ChatTab(String title, String opponentName, Command center, Command right,
			boolean showChatTf) {
		this.title = title;
		this.m_strOpponentName = opponentName;
		this.center = center;
		this.right = right;
		this.showChatTf = showChatTf;
		dis = GameCanvas.h - ChatScreen.Y * 2 - 54;
		reset();
	}

	public void addText(String text, int color) {
		String[] newTexts = BitmapFont.m_bmNormalFont.splitFontBStrInLine(text, GameCanvas.w
				- (ChatScreen.X * 2 + 30));

		int a = newTexts.length;
		ChatInfo chatInfo;
		for (int i = 0; i < a; i++) {
			chatInfo = new ChatInfo(newTexts[i], color);
			listChatContents.addElement(chatInfo);
			if (listChatContents.size() > 100) {
				listChatContents.removeElementAt(0);
			}
		}

		if (ChatScreen.getInstance().getTab(ChatScreen.getInstance().selectedTab) == this) {
			initScroll();
		}
		chatInfo = null;
		newTexts = null;
	}

	public void initScroll() {
		int a = listChatContents.size();
		Scroll.gI().init(dis, a * 13, cmy);
		limit = a * l - dis;
		if (a * l > dis) {
			cmy = a * l - (dis / l) * l;
		}
	}

	public void paint(Graphics g) {
		if (limit > 0) {
			Scroll.gI().paintScroll(g, GameCanvas.w - 48, 0);
		}

		g.setClip(0, 0, GameCanvas.w - ChatScreen.X * 2, GameCanvas.h - ChatScreen.Y * 2 - 26);
		g.translate(0, -cmy);

		int a = cmy / l;
		if (a < 0) {
			a = 0;
		}
		int b = a + dis / l + 1;

		if (b > listChatContents.size()) {
			b = listChatContents.size();
		}
		ChatInfo chatInfo;
		for (int i = a; i < b; i++) {
			chatInfo = (ChatInfo) listChatContents.elementAt(i);
			BitmapFont.drawNormalFont(g, chatInfo.content, 10, i * l + 5, chatInfo.color, 0);
		}
		chatInfo = null;
	}

	public void update(int dir) {
		if (dir == -1 && cmtoY > l) {
			cmtoY -= l;
		}
		if (dir == 1 && limit > 0 && cmtoY < limit) {
			cmtoY += l;
		}
		if (cmy != cmtoY) {
			cmvy = (cmtoY - cmy) << 2;
			cmdy += cmvy;
			cmy += cmdy >> 4;
			cmdy = cmdy & 0xf;
			Scroll.gI().updateScroll(cmy, cmy + 1);
		}
	}

	public void reset() {
		cmtoY = cmy = 0;
		initScroll();
		cmtoY = cmy;
	}
}
