package com.hdc.mycasino.screen;

import java.util.ArrayList;
import java.util.Vector;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.utilities.GameResource;

public class Menu {
	public boolean m_showMenu;
	private boolean m_showMiniMenu;
	private int m_selectedMini;
	private int m_miniH, m_miniY, m_cmy, m_cmtoY, m_cmyLim;

	public Vector m_list;
	public Vector m_listMini;
	public int m_selectedIndex;
	int m_left, m_top, m_menuW, menuH, menuTemY;

	public int cmtoX, cmx, cmdx, cmvx;
	public int cmtoY, cmy, cmdy, cmvy, cmyLim;
	private int xL;
	private int size = 0;
	int pa = 0;
	int[] deltaX;
	int wHeight = 0;
	// TODO biến move để di chuyển menu
	int move;
	// dùng để update sự di chuyền của list menu
	// 0 : chưa di chuyển
	// 1 : đang di chuyển
	// 2 : di chuyển hoàn thành
	ArrayList<Integer> mArray_Flag = new ArrayList<Integer>();

	@SuppressWarnings("rawtypes")
	public void startAt(Vector menuList, int pos) {
		// move = GameResource.instance.imgMenuEnable.getWidth();
		// wHeight = Screen.ITEM_HEIGHT + 10;
		wHeight = (GameResource.instance.imgMenuEnable.getHeight() * 25 / 24);
		xL = GameCanvas.h;

		if (m_list != null) {
			m_list.removeAllElements();
			m_list = null;
		}
		m_list = menuList;
		if (menuList != null)
			size = m_list.size();
		// m_menuW = menuH = 0;
		// Command c;
		// int w;
		// for (int i = 0; i < size; i++) {
		// c = (Command) m_list.elementAt(i);
		// menuH += wHeight;
		//
		// }
		m_menuW = GameResource.instance.imgMenuEnable.getWidth();
		menuH = wHeight * size;
		// c = null;

		// m_menuW += 50;
		// if (m_menuW < 100)
		// m_menuW = 100;

		m_left = GameCanvas.w - GameResource.instance.imgMenuEnable.getWidth();

		m_top = GameResource.instance.imgMenuEnable.getHeight();

		menuTemY = GameCanvas.h; // - wHeight;

		m_showMenu = true;
		m_showMiniMenu = false;
		m_listMini = null;

		m_miniH = 0;
		m_selectedMini = 0;
		m_selectedIndex = 0;
		deltaX = null;
		deltaX = new int[size];

		cmyLim = size * wHeight - (GameCanvas.h - wHeight);

		if (cmyLim < 0)
			cmyLim = 0;
		cmtoY = 0;
		cmy = 0;
		m_cmy = 0;
		m_cmtoY = 0;
		m_cmyLim = 0;
	}

	// TODO reset menu
	public void resetMenu() {
		move = GameResource.instance.imgMenuEnable.getWidth();
		mArray_Flag = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			// cho list menu chưa di chuyển
			mArray_Flag.add(0);
		}
		// cho menu với index = 0
		// di chuyển trước
		mArray_Flag.set(0, 1);
	}

	public void updateMenuKey() {
		// updateMenuKeyMain();
		updateKey_Menu();

		if (GameCanvas.keyPressed[5] || GameCanvas.keyPressed[12]) {
			m_showMenu = false;
			GameCanvas.keyPressed[5] = false;
			GameCanvas.keyPressed[12] = false;
			Command cmd = ((Command) m_list.elementAt(m_selectedIndex));
			cmd.action.perform();
			// actionMini();
			cmd = null;
		} else if (GameCanvas.keyPressed[13]) {
			m_showMenu = false;
			GameCanvas.keyPressed[13] = false;
		}

		if (GameCanvas.instance.isPointerClick) {
			if (GameCanvas.isPointer(GameCanvas.w - GameResource.instance.imgMenuBg.getWidth(), 0,
					GameResource.instance.imgMenuBg.getWidth(),
					GameResource.instance.imgMenuBg.getHeight())) {
				// play sound
				HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.click);
				HDCGameMidlet.sound.play();
				GameCanvas.instance.menu.m_showMenu = false;
			}
		}
	}

	// private void actionMini() {
	// if (((Command) m_list.elementAt(m_selectedIndex)).list != null) {
	// if (m_listMini != null) {
	// ((Command) m_listMini.elementAt(m_selectedMini)).action
	// .perform();
	// return;
	// }
	// m_selectedMini = 0;
	// m_showMiniMenu = true;
	// m_showMenu = true;
	// m_listMini = ((Command) m_list.elementAt(m_selectedIndex)).list;
	// m_miniH = m_listMini.size() * wHeight;
	// // m_miniY = menuTemY + m_selectedIndex * wHeight - cmy - m_miniH
	// // + (wHeight);
	// }
	// }

	private void updateMenuKeyMain() {
		boolean changeFocus = false;

		if (GameCanvas.isPointer(m_left, menuTemY, m_menuW, menuH)) {
			if (GameCanvas.isPointerDown) {
				pa = GameCanvas.pyLast - GameCanvas.py;
				int aa = (m_list.size() * wHeight / (menuH * 2));
				if (aa != 0)
					pa *= aa;
			}

			// move list menu
			if (GameCanvas.isPointerMove) {
				cmtoY = cmy + pa;
				if (cmtoY < 0)
					cmtoY = 0;
				if (cmtoY > cmyLim)
					cmtoY = cmyLim;
				if (pa == 0)
					changeFocus = true;
			}

			// click menu
			if (GameCanvas.isPointerClick) {
				cmtoY = cmy + pa;

				if (cmtoY < 0)
					cmtoY = 0;
				if (cmtoY > cmyLim)
					cmtoY = cmyLim;
				int b = menuTemY;
				if (pa == 0)
					changeFocus = true;
				int aa = (cmtoY + GameCanvas.py - b) / wHeight;
				if (m_selectedIndex == aa && pa == 0) {
					m_showMenu = false;
					((Command) m_list.elementAt(m_selectedIndex)).action.perform();
				}
				m_selectedIndex = aa;
				if (m_selectedIndex < 0)
					m_selectedIndex = 0;
				if (m_selectedIndex >= m_list.size())
					m_selectedIndex = m_list.size() - 1;
			}
		}

		if (changeFocus) {
			cmtoY = m_selectedIndex * wHeight - 2 * wHeight;
			if (cmtoY > cmyLim)
				cmtoY = cmyLim;
			if (cmtoY < 0)
				cmtoY = 0;
			if (m_selectedIndex == size - 1 || m_selectedIndex == 0)
				cmy = cmtoY;
		}
	}

	public void paintMenu(Graphics g) {
		g.setColor(Color.BLACK);
		g.setAlpha(120);
		g.fillRectWithTransparent(0, 0, GameCanvas.w, GameCanvas.h);
		g.translate(xL, 0);
		paintMain(g);
	}

	public void paintMain(Graphics g) {
		if (size == 0)
			return;
		g.translate(-g.getTranslateX(), -g.getTranslateY());

		paintMenuNormal(g);

		g.translate(-g.getTranslateX(), -g.getTranslateY());
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
	}

	// public void paintMenuMini(Graphics g) {
	// if (m_listMini == null && m_listMini.size() <= 0)
	// return;
	// g.translate(-g.getTranslateX(), -g.getTranslateY());
	// g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
	// PaintPopup.paintBox(g, m_left + m_menuW + 2, m_miniY - 7, m_menuW + 4,
	// m_miniH + 15, 1, 0x007639, 0xfcb108);
	//
	// g.translate(m_left + m_menuW + 9, m_miniY + 1);
	// g.setClip(0, 0, m_menuW - 4, m_miniH - 2);
	// g.translate(0, -m_cmy);
	//
	// String str;
	// for (int i = 0; i < m_listMini.size(); i++) {
	// str = ((Command) m_listMini.elementAt(i)).caption;
	// if (i == m_selectedMini) {
	// g.setColor(0x163610);
	// g.fillRect(-1, i * wHeight + 1, m_menuW - 5, wHeight);
	// // BitmapFont.drawNormalFont(g, str, 3, 4 + i * wHeight,
	// // 0xffffff, 0);
	// BitmapFont.drawNormalFont(g, str, 3, wHeight / 2 + i * wHeight,
	// 0xffffff, Graphics.LEFT | Graphics.TOP);
	// } else
	// BitmapFont.drawNormalFont(g, str, 3, wHeight / 2 + i * wHeight,
	// 0xffffff, Graphics.LEFT | Graphics.TOP);
	// // BitmapFont.drawNormalFont(g, str, 3, 4 + i * wHeight, 0xf7dc23,
	// // 0);
	//
	// }
	// str = null;
	// }

	// TODO update key for menu
	private void updateKey_Menu() {
		if (GameCanvas.isPointerDown) {
			for (int i = 0; i < size; i++) {
				if (GameCanvas.isPointer_Down(
						GameCanvas.w - GameResource.instance.imgMenuEnable.getWidth(), (i + 1)
								* GameResource.instance.imgMenuEnable.getHeight() * 25 / 24,
						GameResource.instance.imgMenuEnable.getWidth(),
						GameResource.instance.imgMenuEnable.getHeight())) {
					m_selectedIndex = i;
					break;
				}
			}
		}

		// if (GameCanvas.isPointerMove) {
		// m_selectedIndex = -1;
		// }

		if (GameCanvas.isPointerClick) {
			for (int i = 0; i < size; i++) {
				if (GameCanvas.isPointer_Down(
						GameCanvas.w - GameResource.instance.imgMenuEnable.getWidth(), (i + 1)
								* GameResource.instance.imgMenuEnable.getHeight() * 25 / 24,
						GameResource.instance.imgMenuEnable.getWidth(),
						GameResource.instance.imgMenuEnable.getHeight())) {
					if (m_selectedIndex != -1) {
						m_showMenu = false;
						((Command) m_list.elementAt(m_selectedIndex)).action.perform();
						break;
					}
				}
			}
		}
	}

	public void paintMenuNormal(Graphics g) {
		g.setClip(m_left, m_top, m_menuW, GameCanvas.h - m_top);
		g.translate(0, GameResource.instance.imgMenuEnable.getHeight() * 25 / 24);
		g.translate(0, -cmy);

		String str;

		for (int i = 0; i < size; i++) {
			str = ((Command) m_list.elementAt(i)).caption;
			if (mArray_Flag.get(i) != 0) {
				if (i == m_selectedIndex) {
					g.drawImage(GameResource.instance.imgMenuEnable,
							GameCanvas.w + (mArray_Flag.get(i) == 2 ? 0 : move), i
									* GameResource.instance.imgMenuEnable.getHeight() * 25 / 24,
							Graphics.RIGHT | Graphics.TOP);
					BitmapFont.drawFontMenu(g, str,
							GameCanvas.w - GameResource.instance.imgMenuEnable.getWidth() / 2
									+ (mArray_Flag.get(i) == 2 ? 0 : move), (i + 1)
									* GameResource.instance.imgMenuEnable.getHeight() + i
									* GameResource.instance.imgMenuEnable.getHeight() / 24,
							0xffffff, Graphics.BOTTOM | Graphics.HCENTER);

					// paint icon
					GameResource.instance.m_frameMenuListIconDisable
							.drawFrame(((Command) m_list.elementAt(i)).index,
							// GameCanvas.w
							// -
							// GameResource.instance.imgMenuDisable
							// .getWidth() / 2
									(GameCanvas.w - GameResource.instance.imgMenuDisable.getWidth())
											+ (GameResource.instance.imgMenuDisable.getWidth() - GameResource.instance.m_frameMenuListIconDisable.frameWidth)
											/ 2 + (mArray_Flag.get(i) == 2 ? 0 : move), i
											* GameResource.instance.imgMenuDisable.getHeight() * 25
											/ 24, Graphics.TOP | /*
																 * Graphics.HCENTER
																 */Graphics.TOP, g);

				} else {
					g.drawImage(GameResource.instance.imgMenuDisable,
							GameCanvas.w + (mArray_Flag.get(i) == 2 ? 0 : move), i
									* GameResource.instance.imgMenuDisable.getHeight() * 25 / 24,
							Graphics.RIGHT | Graphics.TOP);
					BitmapFont.drawFontMenu(g, str,
							GameCanvas.w - GameResource.instance.imgMenuDisable.getWidth() / 2
									+ (mArray_Flag.get(i) == 2 ? 0 : move), (i + 1)
									* GameResource.instance.imgMenuDisable.getHeight() + i
									* GameResource.instance.imgMenuDisable.getHeight() / 24,
							0xffffff, Graphics.BOTTOM | Graphics.HCENTER);

					// paint icon
					GameResource.instance.m_frameMenuListIconEnable
							.drawFrame(
									((Command) m_list.elementAt(i)).index,
									/*
									 * GameCanvas.w -
									 * GameResource.instance.imgMenuDisable
									 * .getWidth() / 2
									 */
									(GameCanvas.w - GameResource.instance.imgMenuDisable.getWidth())
											+ (GameResource.instance.imgMenuDisable.getWidth() - GameResource.instance.m_frameMenuListIconEnable.frameWidth)
											/ 2 + (mArray_Flag.get(i) == 2 ? 0 : move), i
											* GameResource.instance.imgMenuDisable.getHeight() * 25
											/ 24, Graphics.TOP | /*
																 * Graphics.HCENTER
																 */Graphics.LEFT, g);

				}
			}

		}
		str = null;
	}

	// TODO update tranlate menu
	private void updateTranlateMenu() {
		if (move < (10 / HDCGameMidlet.instance.scale)) {

			for (int i = 0; i < size; i++) {
				if (mArray_Flag.get(i) == 1) {
					mArray_Flag.set(i, 2);
					// play sound
					if (i == (size - 2)) {
						HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.dropdown);
						HDCGameMidlet.sound.play();
					}
				} else if (mArray_Flag.get(i) == 0) {
					mArray_Flag.set(i, 1);
					move = GameResource.instance.imgMenuEnable.getWidth();
					break;
				}
			}
		}

		if (move > (50 / HDCGameMidlet.instance.scale)) {
			move -= (50 / HDCGameMidlet.instance.scale);
		}

		if (xL != 0)
			xL += (-xL >> 1);
		if (xL == -1)
			xL = 0;
	}

	public void updateMenu() {
		// update tranlate for menu
		updateTranlateMenu();
		// move camera
		moveCamera();

		// update menu
		updateMain();

		if (!m_showMiniMenu && ((Command) m_list.elementAt(m_selectedIndex)).list != null) {
			deltaX[m_selectedIndex]++;
			if (deltaX[m_selectedIndex] > 5)
				deltaX[m_selectedIndex] = 0;
		}
	}

	public void moveCamera() {
		if (cmx != cmtoX) {
			cmvx = (cmtoX - cmx) << 2;
			cmdx += cmvx;
			cmx += cmdx >> 4;
			cmdx = cmdx & 0xf;
		}
		if (cmy != cmtoY) {
			cmvy = (cmtoY - cmy) << 2;
			cmdy += cmvy;
			cmy += cmdy >> 4;
			cmdy = cmdy & 0xf;
		}

		// if (m_showMiniMenu) {
		// if (m_cmy != m_cmtoY) {
		// cmvy = (m_cmtoY - m_cmy) << 2;
		// cmdy += cmvy;
		// m_cmy += cmdy >> 4;
		// cmdy = cmdy & 0xf;
		// }
		// }
	}

	private void updateMain() {
		if (menuTemY > m_top) {
			int delta = ((menuTemY - m_top) >> 2);
			if (delta < 1)
				delta = 1;
			menuTemY -= delta;
		}
		menuTemY = m_top;
	}
}
