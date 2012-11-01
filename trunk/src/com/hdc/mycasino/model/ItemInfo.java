package com.hdc.mycasino.model;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.screen.PaintPopup;
import com.hdc.mycasino.screen.Screen;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

public class ItemInfo extends MyObj {

	public int m_imgID = 0;
	public int m_itemId = 0;
	public long m_gold = 0;
	public int m_dina = 0;
	public String m_sDetail = "";
	public Vector m_arrDes = new Vector();
	public boolean m_bExpired = false;
	// protected Vector m_vtDescription = new Vector();
	public int m_iWidthInfo = 0;
	public int m_iHeightInfo = 0;
	public boolean m_bPaintRowForShopScr = false;
	public int m_itemType;

	// Only use for shop.
	public int m_numberUseDays = 0;
	public int m_numberUses = 0;

	// Only user for player items.
	public String m_sExpiredTime = "";
	public int m_iNumberExpiredUses = 0;
	public int widthPage = GameResource.instance.imgListScr_Panel.getWidth();

	public void paintIcon(Graphics g, int x, int y) {
		g.setColor(0x1e9457);
		DataManager.gI().m_imgPartAvatar.drawFrame(m_imgID, x, y, 0, Graphics.LEFT | Graphics.TOP, g);
	}

	@Override
	public void paintInfo_Item(Graphics g, int x, int y, int width, int height, MyObj myObj, int type, int m_widthItem) {
		// TODO Auto-generated method stub
		if (x < widthPage / 2 || (x > widthPage && x < widthPage / 2 * 3))
			x += m_widthItem / 2;
		else
			x -= (width + m_widthItem / 2);

		if (y < GameCanvas.w / 4 * 3)
			y -= (height / 2);
		else
			y -= height;

		// paint description
		BitmapFont.setTextSize(16f);
		switch (type) {
		case 2:
			g.fillRoundRectWithGadient((int) x, (int) y, width, height, (int) (8 / HDCGameMidlet.scale),
					(int) (8 / HDCGameMidlet.scale));

			paintInfo_Avatar(g, x, y, (ItemInfo) myObj);
			break;
		case 3:
			g.fillRoundRectWithGadient((int) x, (int) y + GameResource.instance.imgAvatar_Khung.getHeight(), width,
					height, (int) (8 / HDCGameMidlet.scale), (int) (8 / HDCGameMidlet.scale));

			paintInfo_Item(g, x, y + GameResource.instance.imgAvatar_Khung.getHeight(), (ItemInfo) myObj);
			break;
		default:
			g.fillRoundRectWithGadient((int) x, (int) y, width + BitmapFont.m_bmFont.getHeight(), height,
					(int) (8 / HDCGameMidlet.scale), (int) (8 / HDCGameMidlet.scale));

			paintInfo_ItemInProfile(g, x, y, (ItemInfo) myObj);
		}
	}

	// TODO paint info avatar
	private void paintInfo_Avatar(Graphics g, float x, float y, ItemInfo itemInfo) {
		y += (20 / HDCGameMidlet.scale);
		// paint item name
		BitmapFont.drawBoldFont_1(g, (String) itemInfo.itemName, x + (10 / HDCGameMidlet.scale), y
				+ (10 / HDCGameMidlet.scale), 0xCCCCCC, Graphics.LEFT | Graphics.TOP);
		y += (30 / HDCGameMidlet.scale);
		// paint gold
		BitmapFont.drawBoldFont_1(g, "Gold : " + itemInfo.m_gold, x + (10 / HDCGameMidlet.scale), y
				+ (10 / HDCGameMidlet.scale), 0xffb901, Graphics.LEFT | Graphics.TOP);
	}

	// TODO paint info vật phẩm
	private void paintInfo_Item(Graphics g, float x, float y, ItemInfo itemInfo) {
		// paint item name
		BitmapFont.drawBoldFont_1(g, (String) itemInfo.itemName, x + (10 / HDCGameMidlet.scale), y
				+ (10 / HDCGameMidlet.scale), 0xffb901, Graphics.LEFT | Graphics.TOP);
		y += (30 / HDCGameMidlet.scale);
		// paint mô tả item
		for (int i = 0; i < itemInfo.m_arrDes.size(); i++) {
			BitmapFont.drawBoldFont_1(g, (String) itemInfo.m_arrDes.elementAt(i), x + (10 / HDCGameMidlet.scale), y
					+ (10 / HDCGameMidlet.scale), /* 0xCCCCCC */0xffffff, Graphics.LEFT | Graphics.TOP);
			y += (30 / HDCGameMidlet.scale);
		}

		// paint giá sản phẩm
		BitmapFont.drawItalicFont_1(g, "Giá : " + (itemInfo.m_gold > 0 ? itemInfo.m_gold : itemInfo.m_dina), x
				+ (10 / HDCGameMidlet.scale), y + (10 / HDCGameMidlet.scale), 0xffb901, Graphics.LEFT | Graphics.TOP);
		y += (30 / HDCGameMidlet.scale);

		// paint hạn sử dụng
		BitmapFont.drawItalicFont_1(g, "Thời gian: " + itemInfo.m_numberUseDays, x
				+ (10 / HDCGameMidlet.scale), y + (10 / HDCGameMidlet.scale), 0xffb901, Graphics.LEFT | Graphics.TOP);
	}

	// TODO paint info vật phẩm trong cá nhân
	private void paintInfo_ItemInProfile(Graphics g, float x, float y, ItemInfo itemInfo) {
		for (int i = 0; i < itemInfo.m_arrDes.size(); i++) {
			BitmapFont.drawNormalFont_1(g, (String) itemInfo.m_arrDes.elementAt(i), x + (10 / HDCGameMidlet.scale), y
					+ (10 / HDCGameMidlet.scale), 0xCCCCCC, Graphics.LEFT | Graphics.TOP);
			y += (30 / HDCGameMidlet.scale);
		}

		BitmapFont.drawNormalFont_1(g, itemInfo.getExperiment(), x + (10 / HDCGameMidlet.scale), y
				+ (10 / HDCGameMidlet.scale), 0xffb901, Graphics.LEFT | Graphics.TOP);
	}

	@Override
	public void paintItem(Graphics g, float x, float y, int m_IdFrame, int select, FrameImage m_frame) {
		// TODO Auto-generated method stub
		if (select == 0)
			g.drawImage(GameResource.instance.imgAvatar_Khung, x, y, Graphics.LEFT | Graphics.VCENTER);
		else
			g.drawImage(GameResource.instance.imgAvatar_Khung_High_Light,
					x + GameResource.instance.imgAvatar_Khung.getWidth() / 2, y, Graphics.HCENTER | Graphics.VCENTER);

		// TODO paint icon
		m_frame.drawFrame(m_IdFrame, x + GameResource.instance.imgAvatar_Khung.getWidth() / 2, y, Sprite.TRANS_NONE,
				Graphics.HCENTER | Graphics.VCENTER, g);
	}

	public Vector getDes(String str, int w) {
		return BitmapFont.splitStringByWidth(str, w, false);
	}

	public String getExperiment() {
		String strExpired = "";
		if (m_bPaintRowForShopScr) {
			if (m_numberUseDays > 0) {
				strExpired = GameResource.timeUsed + m_numberUseDays + GameResource.date;
			} else {
				if (m_numberUses > 0) {
					strExpired = GameResource.used + m_numberUses + GameResource.count;
				}
			}
		} else {
			if (!m_sExpiredTime.equals("") && m_sExpiredTime.length() > 0) {
				if (m_bExpired) {
					strExpired = GameResource.endDate;
				} else {
					strExpired = GameResource.ends + m_sExpiredTime;
				}
			} else {
				if (m_bExpired) {
					strExpired = GameResource.end;
				} else {
					strExpired = GameResource.have + m_iNumberExpiredUses + GameResource.count;
				}
			}
		}

		return strExpired;
	}

	public void paintInRow(Graphics g, int x, int y, int width, int height) {
		BitmapFont.drawBoldFont(g, itemName, x + 40, y + 2, 0xFFB901, Graphics.LEFT | Graphics.TOP);
		GameResource.instance.frameItems.drawFrame(m_imgID, x + 5, y + 9, 0, Graphics.LEFT | Graphics.TOP, g);

		int i;
		String content;
		for (i = 0; i < m_arrDes.size(); i++) {
			content = (String) m_arrDes.elementAt(i);
			BitmapFont.drawNormalFont(g, content, x + 40, y + 12 * i + Screen.ITEM_HEIGHT, 0xFFFFFF, Graphics.LEFT
					| Graphics.TOP);
		}

		if (m_gold != 0) {
			BitmapFont.drawNormalFont(g, m_gold + "", x + GameResource.instance.frameItems.frameHeight / 2 + 5, y
					+ GameResource.instance.frameItems.frameHeight + Screen.ITEM_HEIGHT, 0xFFff01, Graphics.HCENTER
					| Graphics.VCENTER);
		} else {
			if (m_dina != 0) {
				BitmapFont.drawNormalFont(g, m_dina + "", x + GameResource.instance.frameItems.frameHeight / 2 + 5, y
						+ GameResource.instance.frameItems.frameHeight + Screen.ITEM_HEIGHT, 0x00ff01, Graphics.HCENTER
						| Graphics.VCENTER);
			}
		}
		content = null;
	}

	public void paintInfo(Graphics g, int x, int y) {
		m_iWidthInfo = 70;
		String[] hstring = BitmapFont.m_bmNormalFont.splitFontBStrInLine(m_sDetail, 110);
		m_iHeightInfo = hstring.length * BitmapFont.m_bmNormalFont.getHeight() + Screen.ITEM_HEIGHT * 2;

		int delta = y + m_iHeightInfo - (GameCanvas.h - GameCanvas.hBottomBar * 2);
		// select the best position for popup
		if (delta > 0) {
			int delta1 = y - m_iHeightInfo - 40;
			if (delta1 < 0) {
				if (Math.abs(delta) > Math.abs(delta1)) {
					y = delta1;
				} else
					y -= 40;
			} else
				y = delta1;
		}

		if (x + m_iWidthInfo > GameCanvas.w) {
			x -= (x + m_iWidthInfo - GameCanvas.w);
		}

		// paint bounding
		PaintPopup.paintBox(g, x, y + 2, m_iWidthInfo, m_iHeightInfo, 1, 0x007639, 0xfed440);

		// paint item's name
		y += 5;
		BitmapFont.drawBoldFont(g, itemName, x + m_iWidthInfo / 2, y, 0xffffff, Graphics.HCENTER | Graphics.TOP);
		y += Screen.ITEM_HEIGHT * 2;
		BitmapFont.drawNormalFont(g, GameResource.gold + " " + m_gold, x + 5, y - 17, 0xffffff, Graphics.LEFT
				| Graphics.TOP);

		int i;
		for (i = 0; i < hstring.length; i++) {
			y += Screen.ITEM_HEIGHT;
			BitmapFont.drawNormalFont(g, hstring[i], 5 + x, y, 0xffffff, Graphics.LEFT | Graphics.TOP);
		}
		hstring = null;
	}

	public void focusItem() {
	}
}
