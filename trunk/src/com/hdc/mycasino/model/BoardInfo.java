package com.hdc.mycasino.model;

import java.util.Vector;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.screen.ListBoardScr;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

public class BoardInfo extends MyObj {

	public static final byte STYLE_2PLAYER = 0;
	public static final byte STYLE_4PLAYER = 1;
	public static byte type = STYLE_4PLAYER;

	public long betGold = 0;
	public boolean isLock;
	public byte numberPlayer;
	public boolean isPlaying;
	public Vector m_listClanNames = new Vector();

	public void paintItem(Graphics g, float x, float y, int m_IdFrame, int select,
			FrameImage m_frame) {
		// TODO Auto-generated method stub
		GameResource.instance.m_frameRoom_IconTable.drawFrame(0, x, y, Sprite.TRANS_NONE,
				Graphics.LEFT | Graphics.VCENTER, g);
		if (select == 0) {
			GameResource.instance.m_frameRoom_IconTable.drawFrame(1, x, y, Sprite.TRANS_NONE,
					Graphics.LEFT | Graphics.VCENTER, g);
		}

		// TODO paint tiền đặc cược
		BitmapFont.setTextSize(16f);
		BitmapFont.drawItalicFont_1(g, "" + betGold, x, y
				- GameResource.instance.m_frameRoom_IconTable.frameHeight / 2, 0xffb901,
				Graphics.LEFT | Graphics.TOP);

		// TODO paint tên bàn
		BitmapFont.drawItalicFont(g, "Bàn " + m_IdFrame, x
				+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 2, y, 0xffffff,
				Graphics.HCENTER | Graphics.VCENTER);

		// TODO paint icon người chơi
//		System.err.println(numberPlayer);
		paintIcon_PeoPle(g, x, y);

	}

	// TODO paint icon người chơi
	private void paintIcon_PeoPle(Graphics g, float x, float y) {
		int heigh = GameResource.instance.m_frameRoom_IconUser.frameHeight / 2;
		switch (numberPlayer) {
		case 1:
			// chính giữa - dưới
			GameResource.instance.m_frameRoom_IconUser.drawFrame(0, x
					+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 2, y
					+ GameResource.instance.m_frameRoom_IconTable.frameHeight / 4 - heigh,
					Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.TOP, g);
			break;
		case 2:
			// chính giữa - dưới
			GameResource.instance.m_frameRoom_IconUser.drawFrame(0, x
					+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 2, y
					+ GameResource.instance.m_frameRoom_IconTable.frameHeight / 4 - heigh,
					Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.TOP, g);
			// chính giữa - trên
			GameResource.instance.m_frameRoom_IconUser.drawFrame(0, x
					+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 2, y
					- GameResource.instance.m_frameRoom_IconTable.frameHeight / 4 - heigh * 2,
					Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.TOP, g);
			break;
		case 3:
			// chính giữa - dưới
			GameResource.instance.m_frameRoom_IconUser.drawFrame(0, x
					+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 2, y
					+ GameResource.instance.m_frameRoom_IconTable.frameHeight / 4 - heigh,
					Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.TOP, g);
			// chính giữa - trên
			GameResource.instance.m_frameRoom_IconUser.drawFrame(1, x
					+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 2, y
					- GameResource.instance.m_frameRoom_IconTable.frameHeight / 4 - heigh * 2,
					Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.TOP, g);

			// bên trái
			GameResource.instance.m_frameRoom_IconUser.drawFrame(1, x
					+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 4, y - heigh,
					Sprite.TRANS_NONE, Graphics.RIGHT | Graphics.TOP, g);

			break;
		case 4:
			// chính giữa - dưới
			GameResource.instance.m_frameRoom_IconUser.drawFrame(0, x
					+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 2, y
					+ GameResource.instance.m_frameRoom_IconTable.frameHeight / 2 - heigh,
					Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.TOP, g);
			// chính giữa - trên
			GameResource.instance.m_frameRoom_IconUser.drawFrame(1, x
					+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 2, y
					- GameResource.instance.m_frameRoom_IconTable.frameHeight / 2 - heigh,
					Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.TOP, g);

			// bên trái
			GameResource.instance.m_frameRoom_IconUser.drawFrame(1, x
					+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 4, y - heigh,
					Sprite.TRANS_NONE, Graphics.RIGHT | Graphics.TOP, g);
			// bên phải
			GameResource.instance.m_frameRoom_IconUser.drawFrame(0, x
					+ GameResource.instance.m_frameRoom_IconTable.frameWidth / 4 * 3, y - heigh,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.TOP, g);

			break;
		default:
			break;
		}
	}

	@Override
	public void paintInfo_Item(Graphics g, int x, int y, int width, int height, MyObj myObj,
			int type, int m_widthItem) {
		// TODO Auto-generated method stub

	}

	public void paintIcon(Graphics g, int x, int y) {
		x += (ListBoardScr.getInstance().m_wCell >> 1) - 5;
		y += (ListBoardScr.getInstance().m_wCell >> 1) - 5;
		g.drawRegion(GameResource.instance.imgTableFourPlayer, 0, (numberPlayer % 5) * 52, 50, 52,
				Sprite.TRANS_NONE, x, y, Graphics.HCENTER | Graphics.VCENTER);

		BitmapFont.drawMoneyMini(g, betGold + "", x, y + 26, Graphics.HCENTER | Graphics.VCENTER,
				0xffff00);

		if (isLock) {
			g.setColor(0xff4b00);
			g.drawRegion(GameResource.instance.imgIconCard, 41, 68, 9, 11, Sprite.TRANS_NONE,
					x + 20, y + 6, 0);
		}
		BitmapFont.drawNormalFont(g, "" + itemId, x, y, 0xffffff, Graphics.HCENTER
				| Graphics.VCENTER);
		if (isPlaying) {
			g.setColor(0x93fba2);
			g.drawRegion(GameResource.instance.imgIconCard, 72, 18, 9, 11, Sprite.TRANS_NONE,
					x - 20, y + 15, 0);
		}

		if (m_listClanNames != null && m_listClanNames.size() > 0) {
			for (int i = 0; i < m_listClanNames.size(); i++) {
				switch (i) {
				case 0:
					BitmapFont.drawNormalFont(g, (String) m_listClanNames.elementAt(i), x, y - 29,
							0xffffff, Graphics.HCENTER | Graphics.VCENTER);
					break;
				case 1:
					BitmapFont.drawNormalFont(g, (String) m_listClanNames.elementAt(i), x, y + 29,
							0xffffff, Graphics.HCENTER | Graphics.VCENTER);

					break;
				case 2:
					BitmapFont.drawNormalFont(g, (String) m_listClanNames.elementAt(i), x - 25,
							y - 2, 0xffffff, Graphics.VCENTER | Graphics.RIGHT);
					break;
				case 3:
					BitmapFont.drawNormalFont(g, (String) m_listClanNames.elementAt(i), x + 25,
							y - 2, 0xffffff, Graphics.VCENTER | Graphics.LEFT);
					break;
				}
			}
		}
	}

	public void paintInRow(Graphics g, int x, int y, int width, int height) {

	}

	public boolean isFull() {
		return ((type == STYLE_4PLAYER && numberPlayer >= 4) || (type == STYLE_2PLAYER && numberPlayer >= 2));
	}

	public void paintInfo(Graphics g, int x, int y) {

	}

	public void focusItem() {

	}
}