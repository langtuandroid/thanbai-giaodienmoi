package com.hdc.mycasino.model;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

public class Item extends MyObj {
	public static final byte AVATAR = 0;
	public static final byte THE_NHAN_DOI_KINH_NGHIEM = 1;
	public static final byte THE_CHONG_KICK = 2;
	public static final byte THE_MIEN_THUE = 3;
	public static final byte THE_RESET_THANH_TICH = 4;
	public static final byte BUT_DOI_MAU = 5;
	public static final byte UY_DANH_LENH = 6;

	public byte m_bItemType;// = -1 là không có icon ban hội,
							// != -1 là icon ban hội
	public byte m_iNumItem;

	@Override
	public void paintItem(Graphics g, float x, float y, int m_IdFrame, int select,
			FrameImage m_frame) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paintInfo_Item(Graphics g, int x, int y, int width, int height, MyObj myObj,
			int type, int m_widthItem) {
		// TODO Auto-generated method stub

	}

	public void paintIcon(Graphics g, int x, int y) {
		GameResource.instance.m_frameIconLan
				.drawFrame(itemId, x + 20, y + 10, Sprite.TRANS_NONE, g);

		if (m_iNumItem > 0) {
			g.setColor(0xff0000);
			g.fillRoundRect(x + 42, y + 32, 20, 20, 20, 20);
			BitmapFont.drawBoldFont(g, "" + m_iNumItem, x + 52, y + 42, 0xffffff, Graphics.HCENTER
					| Graphics.VCENTER);
		}
		if (BitmapFont.m_bmFont.stringWidth(itemName) > 80) {
			int i;
			String[] str = BitmapFont.m_bmFont.splitFontBStrInLine(itemName, 80);
			for (i = 0; i < str.length; i++) {
				BitmapFont.drawBoldFont(g, str[i], x + 35, y + 62 + i * 13, 0xffffff,
						Graphics.HCENTER | Graphics.VCENTER);
			}
			str = null;
		} else
			BitmapFont.drawBoldFont(g, itemName, x + 35, y + 62, 0xffffff, Graphics.HCENTER
					| Graphics.VCENTER);
	}

	public void paintInRow(Graphics g, int x, int y, int width, int height) {
	}

	public void paintInfo(Graphics g, int x, int y) {
	}

	public void focusItem() {

	}

}
