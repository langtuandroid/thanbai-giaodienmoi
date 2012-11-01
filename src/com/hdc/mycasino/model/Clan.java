package com.hdc.mycasino.model;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

/**
 * Entity is used to manage information of clan.
 */
public class Clan extends MyObj {
	public long money;
	public int score;
	public int level;
	public int totalMembers;

	public void paintIcon(Graphics g, int x, int y) {

	}

	@Override
	public void paintInfo_Item(Graphics g, int x, int y, int width, int height, MyObj myObj,
			int type, int m_widthItem) {
		// TODO Auto-generated method stub

	}

	// TODO paint avatar
	public void paintItem(Graphics g, float x, float y, int avatarID, int select, FrameImage m_frame) {
		// TODO paint khung avatar
		if (select == 0)
			g.drawImage(GameResource.instance.imgAvatar_Khung, x, y, Graphics.LEFT
					| Graphics.VCENTER);
		else
			g.drawImage(GameResource.instance.imgAvatar_Khung_High_Light, x
					+ GameResource.instance.imgAvatar_Khung.getWidth() / 2, y, Graphics.HCENTER
					| Graphics.VCENTER);

		// TODO paint avatar
		m_frame.drawFrame(avatarID, x + GameResource.instance.imgAvatar_Khung.getWidth() / 2, y,
				Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);
	}

	public void paintInRow(Graphics g, int x, int y, int width, int height) {
		// TODO paint avatar
		paintItem(g, x, y + height / 2, itemId, 0, GameResource.instance.m_frameGiaToc_ThanhLap);

		// TODO paint item name
		BitmapFont.drawBoldFont(g, itemName, x + GameResource.instance.imgAvatar_Khung.getWidth()
				/ 2 * 3, y + height / 4, 0xBABABA, Graphics.LEFT | Graphics.VCENTER);

		// TODO paint member
		BitmapFont.drawNormalFont(g, GameResource.member + totalMembers, x
				+ +GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3, y + height / 4 * 3,
				0xffd200, Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawNormalFont(g, "" + totalMembers,
				x + +GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3
						+ BitmapFont.m_bmNormalFont.stringWidth(GameResource.member), y + height
						/ 4 * 3, 0x00FF00, Graphics.LEFT | Graphics.TOP);

		// TODO paint level
		BitmapFont.drawNormalFont(g, GameResource.level + GameResource.space + level, x
				+ +GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3, y + +height / 2,
				0xffd200, Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawNormalFont(
				g,
				"" + level,
				x
						+ +GameResource.instance.imgAvatar_Khung.getWidth()
						/ 2
						* 3
						+ BitmapFont.m_bmNormalFont.stringWidth(GameResource.level
								+ GameResource.space), y + +height / 2, 0x00FF00, Graphics.LEFT
						| Graphics.TOP);

		// TODO paint money
		BitmapFont.drawNormalFont(g, "" + money,
				x + GameResource.instance.imgListScr_Panel.getWidth()
						- (60 / HDCGameMidlet.instance.scale), y + height / 3, 0x00FF00,
				Graphics.RIGHT | Graphics.VCENTER);
		BitmapFont.drawNormalFont(
				g,
				GameResource.money + GameResource.space,
				x + GameResource.instance.imgListScr_Panel.getWidth()
						- (60 / HDCGameMidlet.instance.scale)
						- BitmapFont.m_bmNormalFont.stringWidth("" + money), y + height / 3,
				0xffd200, Graphics.RIGHT | Graphics.VCENTER);

		// TODO paint point
		BitmapFont.drawNormalFont(g, "" + score,
				x + GameResource.instance.imgListScr_Panel.getWidth()
						- (60 / HDCGameMidlet.instance.scale), y + height / 3 * 2, 0x00FF00,
				Graphics.RIGHT | Graphics.VCENTER);
		BitmapFont.drawNormalFont(
				g,
				GameResource.point + GameResource.space,
				x + GameResource.instance.imgListScr_Panel.getWidth()
						- (60 / HDCGameMidlet.instance.scale)
						- BitmapFont.m_bmNormalFont.stringWidth("" + score), y + height / 3 * 2,
				0xffd200, Graphics.RIGHT | Graphics.VCENTER);
	}

	public void paintInfo(Graphics g, int x, int y) {

	}

	public void focusItem() {
		// TODO Auto-generated method stub

	}
}
