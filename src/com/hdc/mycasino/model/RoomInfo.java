package com.hdc.mycasino.model;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

public class RoomInfo extends MyObj {
	public static final byte NORMAL = 0;
	public static final byte VIP = 1;
	public static final byte CLAN = 2;

	public int type;
	public long goldJoinRoom;
	public long minBetGold;
	public byte status = 0;

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

	public void paintInfo(Graphics g, int x, int y) {

	}

	public void paintInRow(Graphics g, int x, int y, int width, int height) {
		switch (type) {
		case NORMAL:
			// GameResource.instance.imgRoom.drawFrame(0, x + 7, y + 6, 0, g);
			GameResource.instance.m_frameRoom_IconPhong.drawFrame(0, x, y + height / 2,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);
			break;
		case VIP:
			// GameResource.instance.imgRoom.drawFrame(1, x + 7, y + 6, 0, g);
			GameResource.instance.m_frameRoom_IconPhong.drawFrame(1, x, y + height / 2,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);
			break;
		case CLAN:
			// GameResource.instance.imgRoom.drawFrame(2, x + 7, y + 6, 0, g);
			GameResource.instance.m_frameRoom_IconGiaToc.drawFrame(0, x, y + height / 2,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);

			break;
		default:
			break;
		}

		if (type == VIP || type == CLAN) {
			BitmapFont.drawBoldFont(g, "Phòng " + itemName, x
					+ GameResource.instance.m_frameRoom_IconPhong.frameWidth / 2 * 3, y + height
					/ 2, 0xffb901, Graphics.VCENTER | Graphics.LEFT);
			BitmapFont.drawItalicFont(g, "[Đại gia]    ", x + width
					- (GameResource.instance.imgListScr_Panel.getWidth() / 8), y + height / 2,
					0xffb901, Graphics.VCENTER | Graphics.RIGHT);

			BitmapFont.drawItalicFont(g, "Gold: " + goldJoinRoom, GameCanvas.w / 2, y + height / 2,
					0xffb901, Graphics.VCENTER | Graphics.HCENTER);

			// BitmapFont.drawBoldFont(g, "Gold: " + goldJoinRoom, x + 48, y +
			// 29,
			// 0xffffff, Graphics.VCENTER | Graphics.LEFT);
		} else {
			BitmapFont.drawBoldFont(g, "Phòng " + itemName, x
					+ GameResource.instance.m_frameRoom_IconPhong.frameWidth / 2 * 3, y + height
					/ 2, 0xffb901, Graphics.VCENTER | Graphics.LEFT);
			BitmapFont.drawItalicFont(g, "[Bình dân]    ", x + width
					- (GameResource.instance.imgListScr_Panel.getWidth() / 8), y + height / 2,
					0xffb901, Graphics.VCENTER | Graphics.RIGHT);
		}

		GameResource.instance.frameStatusRoom.drawFrame(status, x + width
				- (GameResource.instance.imgListScr_Panel.getWidth() / 8), y + height / 2,
				Sprite.TRANS_NONE, Graphics.RIGHT | Graphics.VCENTER, g);
	}

	public void focusItem() {
	}
}
