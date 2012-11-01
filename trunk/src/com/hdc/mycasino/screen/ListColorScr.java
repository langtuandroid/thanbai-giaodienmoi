package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.model.Color;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.GameResource;

public class ListColorScr extends ShopScr {

	private static ListColorScr instance;

	public static ListColorScr getInstance() {
		return instance == null ? instance = new ListColorScr() : instance;
	}

	public void switchToMe(Screen screen) {
		super.switchToMe(screen, m_cmdCenter);
		m_arrItems = new Vector();
		Color color;
		for (i = 0; i < GameResource.listColorMessenger.length; i++) {
			color = new Color();
			color.itemId = i;
			color.color = GameResource.listColorMessenger[i];
			m_arrItems.addElement(color);
		}

		setListItems(m_arrItems, (byte) -1, 0, GameCanvas.hBottomBar + 5, GameCanvas.w, GameCanvas.h
				- GameCanvas.hBottomBar * 2, "Màu chữ", 20, 18);
		m_selected = 0;
		// GameCanvas.cameraList.setSelect(m_selected);
	}

	public ListColorScr() {
		m_cmdCenter = new Command("Đổi màu", new IAction() {
			public void perform() {
				GlobalService.sendMessageChangeChatColor(GameResource.listColorMessenger[m_selected]);
			}
		});
	}

	@Override
	public void updateKey() {
		// TODO Auto-generated method stub
		super.updateKey();
		if (GameCanvas.isPointerClick) {
			if (GameCanvas.isPointer(0, 0, GameResource.instance.imgMenuBg.getWidth() / 2 * 3,
					GameResource.instance.imgMenuBg.getHeight() / 2 * 3)
					&& Math.abs(GameCanvas.instance.pyLast - GameCanvas.instance.py) < 5) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				HDCGameMidlet.instance.onBackPressed();
			}
		}
	}

	public void close() {
		super.close();
		// if (lastScr instanceof ProfileScr) {
		lastScr.switchToMe();
		// }
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		super.doBack();
		close();
	}
}
