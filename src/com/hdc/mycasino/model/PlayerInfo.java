package com.hdc.mycasino.model;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.gif.GifView;
import com.hdc.mycasino.screen.MainScr;
import com.hdc.mycasino.screen.PaintPopup;
import com.hdc.mycasino.utilities.DetailImage;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;

public class PlayerInfo extends MyObj {

	public boolean isChampion;
	public int avatarId;
	public Image customAvatar;
	public int level;
	public int exp;
	public int role = 0;
	public int[][] score;
	public int competitionGameId = -1;
	public long gold;
	public int dina;

	public int rank = -1;
	private long m_iAddGold;
	private long m_iAddDina;
	private long m_iDeltaGold;
	private long m_iDeltaDina;

	public byte isReady;
	public byte onlineStatus = 0;
	public boolean isOwner;
	public boolean isHighlight = false;
	public String fullName = "";
	public String medal = "";
	public String gender;
	public String status;
	public String[] cardName;

	public static final byte ROLE_HEADMAN = 1;
	public static final byte ROLE_DEPUTY = 2;
	public static final byte ROLE_MEMBER = 3;
	public Vector m_listAvatars = new Vector();
	public Vector m_listItems = new Vector();
	public Image imgBg_Chat;

	private float scaleRank = 1f;

	public void paintIcon(Graphics g, int x, int y) {

	}

	public int getColor() {
		int get = level / 5;

		if (level < 0)
			return GameResource.listColor[0];
		if (level > 51)
			return GameResource.listColor[GameResource.instance.listLevel.length - 1];
		return GameResource.listColor[get];
	}

	public void getHonors() {
		int get = level / 5;
		if (level < 0) {
			medal = GameResource.instance.listLevel[0];
			return;
		}
		if (level > 51) {
			medal = GameResource.instance.gI().listLevel[GameResource.instance.gI().listLevel.length - 1];
			return;
		}
		medal = GameResource.instance.gI().listLevel[get];
	}

	int tick = 100;

	@Override
	public void paintItem(Graphics g, float x, float y, int m_IdFrame, int select, FrameImage m_frame) {
		// TODO Auto-generated method stub

	}

	public void paintInRow(Graphics g, int x, int y, int width, int height) {
		// TODO paint khung avatar
		if (!isChampion)
			g.drawImage(GameResource.instance.imgAvatar_Khung,
					x + GameResource.instance.imgAvatar_Khung.getWidth() / 2, y
							+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 2, Graphics.HCENTER
							| Graphics.VCENTER);
		else
			g.drawImage(GameResource.instance.imgAvatar_Khung_High_Light,
					x + GameResource.instance.imgAvatar_Khung.getWidth() / 2, y
							+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 2, Graphics.HCENTER
							| Graphics.VCENTER);

		// paint hình avatar

		GameResource.instance.m_frameAvatar_IconAvatar.drawFrame(avatarId,
				x + GameResource.instance.imgAvatar_Khung.getWidth() / 2, y
						+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 2, Sprite.TRANS_NONE,
				Graphics.HCENTER | Graphics.VCENTER, g);

		// draw star
		if (role > 0) {
			int i;
			for (i = 0; i < (3 - role); i++) {
				// g.drawRegion(GameResource.instance.imgIconCard, 25, 81, 16,
				// 16,
				// 0, x + 45 + i * 15, y + height / 2
				// - BitmapFont.m_bmNormalFont.getHeight() - 5, 6);
				g.drawImage(GameResource.instance.imgGiaTocStar, x + GameResource.instance.imgAvatar_Khung.getWidth()
						/ 2 * 3, y + height / 2, Graphics.LEFT | Graphics.VCENTER);
			}
		}

		int y1 = y
				+ (GameResource.instance.imgTabs_HightLightRow.getHeight() - GameResource.instance.imgAvatar_Khung
						.getHeight()) / 2;
		String str = GameResource.nick + GameResource.space;
		// TODO paint nick
		BitmapFont.drawNormalFont(g, str, x + GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3, y + height / 4, /*
																														 * getColor
																														 * (
																														 * )
																														 */
				0xffb901, Graphics.LEFT | Graphics.VCENTER);
		BitmapFont.drawUnderlineFont(g, itemName, x + GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3
				+ BitmapFont.m_bmNormalFont.stringWidth(str), y + height / 4, 0x32CD32, Graphics.LEFT
				| Graphics.VCENTER);

		if (m_bIsShowMoneyAndLevel) {
			// TODO paint gold
			str = GameResource.gold + GameResource.space;
			BitmapFont.drawNormalFont(g, str, x + GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3, y1
					+ BitmapFont.m_bmNormalFont.getHeight(), 0xffb901, Graphics.LEFT | Graphics.TOP);
			BitmapFont.drawItalicFont(g, gold + "", x + GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3
					+ BitmapFont.m_bmNormalFont.stringWidth(str), y1 + BitmapFont.m_bmNormalFont.getHeight(), 0x32CD32,
					Graphics.LEFT | Graphics.TOP);

			// TODO paint level
			str = GameResource.level + GameResource.space;
			BitmapFont.drawNormalFont(g, str, x + GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3, y1
					+ BitmapFont.m_bmNormalFont.getHeight() * 2, 0xffb901, Graphics.LEFT | Graphics.TOP);
			BitmapFont.drawItalicFont(g, level + "", x + GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3
					+ BitmapFont.m_bmNormalFont.stringWidth(str), y1 + BitmapFont.m_bmNormalFont.getHeight() * 2,
					0x32CD32, Graphics.LEFT | Graphics.TOP);
		} else {
			// str = status;
			// if (BitmapFont.m_bmNormalFont.stringWidth(str) > width - 100)
			// str = BitmapFont.m_bmNormalFont.splitFontBStrInLine(str,
			// width - 100)[0] + "...";
			// str = GameResource.status + status;
			BitmapFont.drawNormalFont(g, GameResource.status, x + GameResource.instance.imgAvatar_Khung.getWidth() / 2
					* 3, y + height, 0xffb901, Graphics.LEFT | Graphics.BOTTOM);
			BitmapFont.drawItalicFont(
					g,
					BitmapFont.opt_String(status, GameResource.MAX_LENGTH),
					x + GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3
							+ BitmapFont.m_bmNormalFont.stringWidth(GameResource.status), y + height, 0xcccccc,
					Graphics.LEFT | Graphics.BOTTOM);
		}
		// draw status online/offline
		if (onlineStatus == 1) {
			// g.setColor(0x00ff00);

			GameResource.instance.m_frameAvatar_IconYellow.drawFrame(0,
					x + GameResource.instance.imgListScr_Panel.getWidth()
							- GameResource.instance.m_frameAvatar_IconYellow.frameWidth * 3, y
							+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 2, Sprite.TRANS_NONE,
					Graphics.VCENTER | Graphics.RIGHT, g);

		} else {
			// g.setColor(0xff0000);

			GameResource.instance.m_frameAvatar_IconRed.drawFrame(0,
					x + GameResource.instance.imgListScr_Panel.getWidth()
							- GameResource.instance.m_frameAvatar_IconRed.frameWidth * 3, y
							+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 2, Sprite.TRANS_NONE,
					Graphics.VCENTER | Graphics.RIGHT, g);
		}

		// g.fillRoundRect(x + width - 24, y + height / 2 - 6, 12, 12, 12, 12);
		// g.setColor(0x000000);
		// g.drawRoundRect(x + width - 24, y + height / 2 - 6, 12, 12, 12, 12);

		// Draw name of game which player register to compete.
		if (competitionGameId >= 0) {
			str = "Game thi đấu: " + MainScr.getGameNameById(competitionGameId);
			BitmapFont.drawNormalFont(g, str, x + 55, y + height / 2 + BitmapFont.m_bmNormalFont.getHeight() - 28,
					0xffffff, 6);
		}

		// if (isHighlight) {
		// if ((tick / 8) % 2 == 0) {
		// g.setColor(0xff0000);
		// } else {
		// g.setColor(0xffff00);
		// }
		// g.drawRect(x + 5, y + 7, 34, 34);
		// }

		str = null;
	}

	public void paintInfo(Graphics g, int x, int y) {

	}

	public Vector m_vtChat = new Vector();
	public int m_iTimeChat = 0;
	public int m_iTimeRank = -1;
	public int m_iTimeStr = 0;
	public boolean m_bIsShowMoneyAndLevel = false;
	int m_detaX = 0;
	int m_detaY = 0;
	int m_speed = 10;
	int m_wMove = 20;
	int m_wCell = 34;

	@SuppressWarnings({ "static-access", "unchecked" })
	public void chat(String text) {
		m_vtChat.removeAllElements();

		m_iTimeChat = 0;
		String[] arr = BitmapFont.m_bmNormalFont.splitFontBStrInLine(text, (int) (90 / HDCGameMidlet.instance.scale));
		int i;
		for (i = 0; i < arr.length; i++) {
			m_vtChat.addElement(arr[i]);
		}
		arr = null;
	}

	public void setRank(int rank) {
		if (rank >= 0) {
			this.rank = rank;
			m_iTimeRank = 200;
		} else {
			this.rank = rank;
			m_iTimeRank = -1;
		}
	}

	private String m_strTalk = "";

	public void setString(String str) {
		m_strTalk = str;
		m_iTimeStr = 225;
	}

	public void paintInBoard(Graphics g, int x, int y, boolean isOwner) {
		// TODO paint panel TienLen
		// GameResource.instance.m_frameTienLen_PanelInfo.drawFrame(0, x, y,
		// Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.TOP, g);
		// TODO paint khung user
		g.drawImage(GameResource.instance.imgTienLen_KhungUser, x, y, Graphics.HCENTER | Graphics.TOP);
		g.drawImage(GameResource.instance.imgTienLen_User, x, y + DetailImage.imgTienLen_KhungUser_h / 2,
				Graphics.HCENTER | Graphics.VCENTER);

		// TODO paint avatar
		GameResource.instance.m_frameAvatar_IconAvatar_1.drawFrame(avatarId, x, y + DetailImage.imgTienLen_KhungUser_h
				/ 2, Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);

		y += (DetailImage.imgTienLen_KhungUser_h / 2 + GameResource.instance.m_frameAvatar_IconAvatar_1.frameHeight / 2);

		BitmapFont.setTextSize(14f / HDCGameMidlet.scale);
		BitmapFont.drawItalicFont_1(g, itemName, x, y, 0xffb901, Graphics.TOP | Graphics.HCENTER);

		y += (16 / HDCGameMidlet.scale);
		BitmapFont.drawBoldFont_1(g, "" + gold, x, y, 0xffb901, Graphics.TOP | Graphics.HCENTER);

		// effect for champion
		if (isChampion) {
			GameResource.instance.m_frameTienLen_IconUser.drawFrame(0, x, y
					+ GameResource.instance.m_frameTienLen_IconUser.frameHeight, Sprite.TRANS_NONE, Graphics.HCENTER
					| Graphics.VCENTER, g);
		}
	}

	public void paintChatPlayer(Graphics g, int x, int y, int anchors) {
		if (m_vtChat.size() > 0) {
			int i, count = m_vtChat.size();
			imgBg_Chat = PaintPopup.gI().createImgBackground_Popup(7, count == 1 ? 2 : count + 1);

			if (anchors == 0 || anchors == 2) {
				g.drawImage(GameResource.instance.imgPopupArrow_Rotate, x, y, Graphics.RIGHT | Graphics.VCENTER);
				g.drawImage(imgBg_Chat, x, y, Graphics.LEFT | Graphics.VCENTER);
				y -= imgBg_Chat.getHeight() / 2;
			} else if (anchors == 1 || anchors == 3) {
				g.drawImage(GameResource.instance.imgPopupArrow, x + imgBg_Chat.getWidth() / 2, y, Graphics.HCENTER
						| Graphics.BOTTOM);
				y -= GameResource.instance.imgPopupArrow.getHeight();
				g.drawImage(imgBg_Chat, x + imgBg_Chat.getWidth() / 2, y, Graphics.HCENTER | Graphics.BOTTOM);
				y -= imgBg_Chat.getHeight();
			}

			float heigh = (20 / HDCGameMidlet.scale);
			BitmapFont.setTextSize(16f / HDCGameMidlet.scale);
			for (i = 0; i < count; i++) {
				String str = (String) m_vtChat.elementAt(i);
				BitmapFont.drawItalicFont_1(g, str, x + 5, y + heigh * i + heigh / 4, 0xcccccc, Graphics.LEFT
						| Graphics.TOP);
			}

			m_iTimeChat++;
			if (m_iTimeChat > 100) {
				m_vtChat.removeAllElements();
			}
		}
	}

	public void paintStrTalk(Graphics g, int x, int y, int anchor, boolean isCut) {
		if (m_iTimeStr > 0) {
			if (isCut) {
				BitmapFont.drawOutlinedString(g, m_strTalk, x, y - BitmapFont.m_bmFont.getHeight(), 225, m_iTimeStr, 0,
						anchor);
			} else
				BitmapFont.drawOutlinedString(g, m_strTalk, x, y, 255, m_iTimeStr, 0, anchor);
		}
	}

	@Override
	public void paintInfo_Item(Graphics g, int x, int y, int width, int height, MyObj myObj, int type, int m_widthItem) {
		// TODO Auto-generated method stub

	}

	public void paintRank(Graphics g, int anchor, int scale) {
		if (rank >= 0 && rank <= 6) {
			int x = 0;
			int y = 0;
			switch (anchor) {
			case 0:
				x = GameCanvas.w / 2;
				y = GameCanvas.h - GameResource.instance.imgTienLen_User.getHeight();
				break;
			case 1:
				x = GameCanvas.w / 8;
				y = (GameCanvas.h - GameResource.instance.imgTienLen_User.getWidth()) / 2;
				break;
			case 2:
				x = GameCanvas.w / 2;
				y = GameResource.instance.imgTienLen_User.getHeight() / 2;
				break;
			case 3:
				x = GameCanvas.w / 8 * 7;
				y = (GameCanvas.h - GameResource.instance.imgTienLen_User.getWidth()) / 2;
				break;
			}
			switch (rank) {
			case 0:
				GameResource.instance.m_frameWin_HighLightWin.drawFrame(scale, x, y, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
			case 1:
			case 2:
			case 3:
			case 4:// TODO: móm
				GameResource.instance.m_frameWin_Icon.drawFrame(rank, x, y, Sprite.TRANS_NONE, Graphics.HCENTER
						| Graphics.VCENTER, g);
				break;
			case 5:// TODO:Ù
				g.drawImage(GameResource.instance.imgPhom_U, x, y, Graphics.HCENTER | Graphics.VCENTER);
				break;
			case 6:// TODO: đền
				g.drawImage(GameResource.instance.imgPhom_Den, x, y, Graphics.HCENTER | Graphics.VCENTER);
				break;
			default:
				break;
			}
		}
	}

	// update money animation
	public void update() {
		if (m_iAddGold != 0) {
			if (Math.abs(m_iAddGold) > Math.abs(m_iDeltaGold)) {
				m_iAddGold -= m_iDeltaGold;
				gold += m_iDeltaGold;
			} else {
				gold += m_iAddGold;
				m_iAddGold = 0;
			}
		}
		if (m_iAddDina != 0) {
			if (Math.abs(m_iAddDina) > Math.abs(m_iDeltaDina)) {
				m_iAddDina -= m_iDeltaDina;
				dina += m_iDeltaDina;
			} else {
				dina += m_iAddDina;
				m_iAddDina = 0;
			}
		}
		if (m_iTimeRank >= 0) {
			m_iTimeRank--;
			if (m_iTimeRank <= 0)
				rank = -1;
		}

		if (m_iTimeStr > 0) {
			m_iTimeStr -= 10;
			if (m_iTimeStr <= 0)
				m_iTimeStr = 0;
		}

		if (isChampion) {
			if (m_detaX + m_wMove < m_wCell) {
				if (m_detaX + m_wMove + m_speed > m_wCell) {
					m_detaX = m_wCell - m_wMove;
					m_detaY = m_detaX + m_speed - m_wCell + m_wMove;
				}
				m_detaX += m_speed;
			} else if (m_detaY < m_wCell) {
				m_detaY += m_speed;
				if (m_detaY > m_wCell) {
					m_detaY = m_wCell;
					m_detaX -= m_detaY - m_wCell;
				}

				if (m_detaX < m_wCell) {
					m_detaX += m_speed;
					if (m_detaX > m_wCell)
						m_detaX = m_wCell;

				}
			} else {
				m_detaY += m_speed;
				if (m_detaY > m_wCell + m_wMove) {
					m_detaY = m_wCell + m_wMove;
				}
				m_detaX -= m_speed;
				if (m_detaX + m_wMove <= m_wCell) {
					m_detaX = 0;
					m_detaY = 0;
				}
			}
		}

		if (isHighlight) {
			tick++;
			if (tick == 500) {
				tick = 0;
			}
		}
	}

	// add money to player (create animation for money increase)
	public void addMoney(int gold, int dina) {

		m_iAddGold += gold;
		m_iAddDina += dina;

		m_iDeltaGold = m_iAddGold / 5;
		if (m_iDeltaGold == 0) {
			m_iDeltaGold = 1;
			m_iAddGold = 0;
			this.gold += gold;
		}

		m_iDeltaDina = m_iAddDina / 5;
		if (m_iDeltaDina == 0) {
			m_iDeltaDina = 1;
			m_iAddDina = 0;
			this.dina -= dina;
		}
	}

	// minus money to player (create animation for money decrease)
	public void subMoney(int money, int dina) {
		m_iAddGold -= money;
		m_iAddDina -= dina;

		m_iDeltaGold = m_iAddGold / 5;
		if (m_iDeltaGold == 0) {
			m_iDeltaGold = -1;
			m_iAddGold = 0;
			this.gold -= money;
		}

		m_iDeltaDina = m_iAddDina / 5;
		if (m_iDeltaDina == 0) {
			m_iDeltaDina = -1;
			m_iAddDina = 0;
			this.dina -= dina;
		}
	}

	public ItemInfo getItemById(int itemId) {
		if (m_listItems != null) {
			for (int i = 0; i < m_listItems.size(); i++) {
				ItemInfo item = (ItemInfo) m_listItems.elementAt(i);
				if (item.m_itemId == itemId) {
					return item;
				}
			}
		}

		return null;
	}

	public void focusItem() {
		// TODO Auto-generated method stub

	}
}
