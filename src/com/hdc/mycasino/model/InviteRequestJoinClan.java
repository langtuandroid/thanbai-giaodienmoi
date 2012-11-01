package com.hdc.mycasino.model;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

public class InviteRequestJoinClan extends MyObj {
	public String nickToJoin = "";
	public boolean isInviteType = true;

	public void paintIcon(Graphics g, int x, int y) {

	}

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

	public void paintInRow(Graphics g, int x, int y, int width, int height) {

		String content;
		if (isInviteType) {// list invite
			content = "Gia tộc " + itemName + " muốn mời bạn làm thành viên";
			// GameResource.instance.imgIconMail.drawFrame(1, x + 2, y + 7,
			// Graphics.LEFT|Graphics.TOP, g);
			GameResource.instance.m_frameMail_HomThu.drawFrame(0, x, y + height / 2,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);
		} else {// list request
			content = nickToJoin + " muốn làm thành viên gia tộc " + itemName + " của bạn";
			// GameResource.instance.imgIconMail.drawFrame(2, x + 2, y + 7,
			// Graphics.LEFT|Graphics.TOP, g);
			GameResource.instance.m_frameMail_HomThu.drawFrame(1, x, y + height / 2,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);
		}

		Vector listStr = BitmapFont.splitStringByWidth(content, GameCanvas.w - 40, true);
		int i;
		for (i = 0; i < listStr.size(); i++) {
			// BitmapFont.drawBoldFont(g, (String) listStr.elementAt(i), x + 10
			// + 17, y + 3 + i * 13, 0x23f7f0, Graphics.LEFT|Graphics.TOP);
			if (isInviteType) {
				BitmapFont.drawNormalFont(g, "Từ : ", x
						+ GameResource.instance.m_frameMail_HomThu.frameWidth / 2 * 3, y + height
						/ 4, 0xffb901, Graphics.LEFT | Graphics.VCENTER);
				BitmapFont.drawNormalFont(g, itemName, x
						+ GameResource.instance.m_frameMail_HomThu.frameWidth / 2 * 3
						+ BitmapFont.m_bmNormalFont.stringWidth("Từ : "), y + height / 4, 0xCCCCCC,
						Graphics.LEFT | Graphics.VCENTER);
				BitmapFont.drawItalicFont(g, content, x
						+ GameResource.instance.m_frameMail_HomThu.frameWidth / 2 * 3, y + height
						/ 4 * 3, 0xffb901, Graphics.LEFT | Graphics.VCENTER);
			} else {
				BitmapFont.drawNormalFont(g, "Từ : ", x
						+ GameResource.instance.m_frameMail_HomThu.frameWidth / 2 * 3, y + height
						/ 4, 0xffb901, Graphics.LEFT | Graphics.VCENTER);
				BitmapFont.drawNormalFont(g, nickToJoin, x
						+ GameResource.instance.m_frameMail_HomThu.frameWidth / 2 * 3
						+ BitmapFont.m_bmNormalFont.stringWidth("Từ : "), y + height / 4, 0xCCCCCC,
						Graphics.LEFT | Graphics.VCENTER);

				BitmapFont.drawItalicFont(g, content, x
						+ GameResource.instance.m_frameMail_HomThu.frameWidth / 2 * 3, y + height
						/ 4 * 3, 0xffb901, Graphics.LEFT | Graphics.VCENTER);
			}
		}
		listStr.removeAllElements();
		listStr = null;
		content = null;
	}

	public void paintInfo(Graphics g, int x, int y) {

	}

	public void focusItem() {
		// TODO Auto-generated method stub

	}
}
