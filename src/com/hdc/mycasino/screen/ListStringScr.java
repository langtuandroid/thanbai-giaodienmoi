package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.danh.view.TextView;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.DataManager;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.DetailImage;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;

public class ListStringScr extends Screen {

	static ListStringScr instance;

	public static ListStringScr gI() {
		if (instance == null) {
			instance = new ListStringScr();
		}
		return instance;
	}

	public Screen backScreen;
	public String[] m_arrList;

	public Command cmdMenu;
	public Command cmdRegister;
	public Command cmdClose;
	public int limit = 0;
	public int m_iSelectedTypeGame = 0;

	int m_championAvtId;
	String m_championNick;
	String m_championName;
	boolean m_bIsRegistered = true;
	int m_iHeightDrawCham = 0;
	boolean m_bIsShowChampion = false;
	int m_detaX = 0, m_detaY = 0;
	int width = 34, height = 34, speed = 3;

	// TODO test
	int test_y = 0;

	// TODO scroll
	private int m_iTopScroll = (int) (30 / HDCGameMidlet.instance.scale);

	// TODO TextView
	private TextView m_textView;

	// TODO Màn hình thần bài trong Room
	private boolean isTabThanBai = false;

	public void switchToMe(Screen backScr, String title, Command leftCmd, Command centerCmd, Command rightCmd) {
		cmy = 0;
		m_cmdLeft = null;
		m_cmdLeft = leftCmd;

		if (rightCmd == null)
			m_cmdRight = cmdClose;
		else
			m_cmdRight = rightCmd;

		m_cmdCenter = null;
		m_cmdCenter = centerCmd;
		backScreen = backScr;

		m_strTitle = title;

		super.switchToMe();

	}

	public void setListItems(String[] listItems, int top) {
		m_arrList = listItems;
		m_top = top;
		m_height = GameCanvas.h - GameCanvas.hBottomBar - m_top;
		m_bIsShowChampion = false;
		if (m_arrList != null && m_arrList.length > 0)
			initScroll();
		// TODO set location
		setLocation();
		// TODO int textview
		if (!isTabThanBai)
			initTextView();
	}

	// TODO init text view
	private void initTextView() {
		m_textView = new TextView();
		m_textView
				.setInfo(m_left + (int) (20 / HDCGameMidlet.instance.scale), m_top,
						GameResource.instance.imgListScr_Panel.getWidth(), GameCanvas.h - m_top,
						GameResource.instance.imgListScr_Panel.getWidth(), (int) (27 / HDCGameMidlet.instance.scale),
						m_arrList);
		m_textView.setColor(0xffffff);
	}

	public void initScroll() {
		// init scroll
		// limit = m_height - ITEM_HEIGHT * m_arrList.length;
		if (m_arrList != null) {
			limit = m_arrList.length * 30;
			m_iHeightDrawCham = m_top + ITEM_HEIGHT * m_arrList.length + 20;
		}
		if (m_bIsShowChampion)
			limit -= 100;

		cmtoY = cmy = 0;
	}

	public ListStringScr() {
		// m_width = GameCanvas.w - 10;
		// m_left = 5;
		cmdClose = new Command(GameResource.close, new IAction() {
			public void perform() {
				close();
			}
		});

		cmdMenu = new Command(GameResource.menu, new IAction() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void perform() {
				Vector vt = new Vector();
				vt.addElement(new Command("T.tin giải đấu", new IAction() {
					public void perform() {
						ListStringScr scrString = new ListStringScr();
						scrString.switchToMe(GameCanvas.currentScreen, GameResource.infoCom, null, null, null);
						scrString.getStringGuide();
						scrString = null;
					}
				}));

				vt.addElement(new Command("Thần bài", new IAction() {
					public void perform() {
						GameCanvas.startWaitDlg();
						GlobalService.sendMessageGetListChampion();
					}
				}));

				GameCanvas.instance.menu.startAt(vt, 0);
				vt = null;
			}
		});

		cmdRegister = new Command(GameResource.register, new IAction() {
			public void perform() {
				GlobalService.sendMessageCheckRegisterCompetition(m_iSelectedTypeGame);
			}
		});
	}

	// TODO set location for list item
	// m_left & m_top
	private void setLocation() {
		m_left = (GameCanvas.w - GameResource.instance.imgListScr_Panel.getWidth()) / 2;
		m_top = (int) (60 / HDCGameMidlet.instance.scale);
		m_height = GameCanvas.h - m_top;
		// HDCGameMidlet.instance.Toast(m_left + " - " + m_top);
	}

	@SuppressWarnings("static-access")
	public void paint(Graphics g) {
		if (!isTabThanBai) {
			PaintPopup.paintBackground(g, m_strTitle);

			// TODO paint panel
			g.drawImage(GameResource.instance.imgListScr_Panel, m_left, m_top, Graphics.LEFT | Graphics.TOP);
			// TODO paint textview
			if (m_textView != null)
				m_textView.paint(g);

		} else {
			float y = m_top;
			for (int i = 0; i < m_arrList.length; i++) {
				y += 60 / HDCGameMidlet.instance.scale;
				BitmapFont.drawNormalFont(g, m_arrList[i], GameCanvas.hw, y, 0xffb901, Graphics.HCENTER | Graphics.TOP);
			}

			if (m_bIsShowChampion) {
				y += 60 / HDCGameMidlet.instance.scale;
				
				GameResource.instance.m_frameAvatar_IconAvatar_1.drawFrame(m_championAvtId, GameCanvas.hw, y
						+ GameResource.instance.m_frameAvatar_IconAvatar_1.frameHeight / 2, Sprite.TRANS_NONE,
						Graphics.HCENTER | Graphics.VCENTER, g);
				
				GameResource.instance.m_frameTienLen_IconUser.drawFrame(0, GameCanvas.hw, y
						, Sprite.TRANS_NONE, Graphics.BOTTOM
						| Graphics.HCENTER, g);
				
				y += GameResource.instance.m_frameAvatar_IconAvatar_1.frameHeight / 2 + 60
						/ HDCGameMidlet.instance.scale;
				BitmapFont.drawNormalFont(g, GameResource.nick + GameResource.space + m_championNick, GameCanvas.hw, y,
						0xffb901, Graphics.HCENTER | Graphics.VCENTER);
				y += 60 / HDCGameMidlet.instance.scale;
				BitmapFont.drawNormalFont(g, "Tên: " + m_championName, GameCanvas.hw, y, 0xffb901, Graphics.HCENTER
						| Graphics.VCENTER);
			}
		}

		super.paint(g);
	}

	// TODO load image
	@Override
	public void loadImg() {
		// TODO Auto-generated method stub
		super.loadImg();

		if (HDCGameMidlet.instance.scale == 2f) {

			float scale;
			// màn hình 240x320
			// scale panel cho list screen tỷ lệ : 2/5

			scale = (float) 2 / 5;
			GameResource.instance.imgListScr_Panel = ImagePack.createImage(ImagePack.LIST_SCREEN_PANEL, scale);
			// scale hight light
			GameResource.instance.imgListScr_HightLight = Image.scaleImage(GameResource.instance.imgListScr_HightLight,
					GameResource.instance.imgListScr_Panel.getWidth(),
					GameResource.instance.imgListScr_HightLight.getHeight());

		}

	}

	void moveRect(Graphics g, int x, int y) {
		g.setColor(0xffff00);
		g.setClip(x + 1, y, m_detaX, 1 + m_detaY);
		g.drawRect(x, y, width, height);
		g.setClip(x + width - 1 - m_detaX, y + height - m_detaY, m_detaX + 1, 1 + m_detaY);
		g.drawRect(x, y, width, height);
	}

	int disY;

	public void updateKey() {
		// TODO Updatekey for Textview
		if (m_textView != null)
			m_textView.updateKey();

		if (GameCanvas.instance.hasPointerEvents()
				&& GameCanvas.isPointer(m_left, m_top + m_iTopScroll,
						GameResource.instance.imgListScr_Panel.getWidth(), GameCanvas.h - m_top - m_iTopScroll)) {

			if (GameCanvas.instance.isPointerDown) {
				disY = GameCanvas.pyLast - GameCanvas.py;
			}

			if (GameCanvas.instance.isPointerMove) {
				cmy += GameCanvas.instance.disMove;
				if (cmy < 0) {
					cmy = 0;
				}
				if (cmy > limit) {
					cmy = limit;
				}
			}

			if ((/* GameCanvas.instance.isPointerMove && */!GameCanvas.instance.isMove)
					|| GameCanvas.instance.isPointerClick) {
				// if (disY > 10)
				// disY = GameCanvas.pyLast - GameCanvas.py;
				if (Math.abs(disY) > 5) {
					cmtoY = cmy + disY;
					if (cmtoY < -30) {
						cmtoY = -30;
					}
					if (cmtoY > limit + 30) {
						cmtoY = limit + 30;
					}
				}
			}

			// if ((GameCanvas.instance.isPointerMove &&
			// GameCanvas.instance.isMove)
			// ) {
			// cmy += GameCanvas.instance.disMove;
			// if (cmy < 0) {
			// cmy = 0;
			// }
			// if (cmy > limit) {
			// cmy = limit;
			// }
			// }

			// if(GameCanvas.instance.isPointerMove &&
			// GameCanvas.instance.isMove){
			// Position p = (Position)
			// GameCanvas.instance.listPoint.elementAt(GameCanvas.instance.listPoint.size()
			// - 1);
			// Position pLast = (Position)
			// GameCanvas.instance.listPoint.elementAt(GameCanvas.instance.listPoint.size()
			// - 2);
			// cmtoY = cmy + (Math.abs(pLast.y - p.y));
			// if (cmtoY < -30) {
			// cmtoY = -30;
			// }
			// if (cmtoY > limit + 30) {
			// cmtoY = limit + 30;
			// }
			// }
			// if(GameCanvas.instance.isPointerClick){
			// cmtoY = cmy + disY;
			// if (cmtoY < -30) {
			// cmtoY = -30;
			// }
			// if (cmtoY > limit + 30) {
			// cmtoY = limit + 30;
			// }
			// }

			// else if (GameCanvas.instance.isPointerClick) {
			// cmtoY = cmy + disY;
			// }
			//
			// if (GameCanvas.pyLast > GameCanvas.py) {
			// // updateScroll(-10);
			// cmy += (GameCanvas.pyLast - GameCanvas.py);
			// if(cmy > limit/2)
			// cmy = limit/2;
			// } else {
			// // updateScroll(10);
			// cmy -= (GameCanvas.py - GameCanvas.pyLast);
			// if(cmy < 0)
			// cmy = 0;
			//
			// }
		}

		super.updateKey();
	}

	public void updateScroll(int dir) {
		if (GameCanvas.instance.menu.m_showMenu || GameCanvas.currentDialog != null)
			return;
		cmtoY += dir;
		if (cmtoY > 0)
			cmtoY = 0;
		if (cmtoY < limit)
			cmtoY = limit;
		if (cmy != cmtoY) {
			cmvy = (cmtoY - cmy) << 2;
			cmdy += cmvy;
			cmy += cmdy >> 4;
			cmdy = cmdy & 0xf;
		}
	}

	public void update() {

		// TODO update for textview
		if (m_textView != null)
			m_textView.update();

		if (m_bIsShowChampion) {
			if (m_detaX <= width - 1) {
				if (m_detaX + speed > width - 1) {
					m_detaX = width - 1;
				}
				m_detaX += speed;
			} else if (m_detaY <= height - 1) {
				if (m_detaY + speed > height - 1) {
					m_detaY = height - 1;
				}
				m_detaY += speed;
			} else {
				m_detaX = 0;
				m_detaY = 0;
			}
		}

		// if (!GameCanvas.instance.isMove) {
		if (cmy != cmtoY) {
			cmvy = (cmtoY - cmy) << 2;
			cmdy += cmvy;
			cmy += cmdy >> 4;
			cmdy = cmdy & 0xf;
		}
		if (Math.abs(cmtoY - cmy) < 15 && cmy < 0) {
			cmtoY = 0;
		}
		if (Math.abs(cmtoY - cmy) < 10 && cmy > limit) {
			cmtoY = limit;
		}
		// }
		// else {
		// if (test_y != cmtoY) {
		// cmvy = (cmtoY - test_y) << 2;
		// cmdy += cmvy;
		// test_y += cmdy >> 4;
		// cmdy = cmdy & 0xf;
		// }
		//
		// if (Math.abs(cmtoY - test_y) < 15 && test_y < 0) {
		// cmtoY = 0;
		// }
		// if (Math.abs(cmtoY - test_y) < 10 && test_y > limit) {
		// cmtoY = limit;
		// }
		//
		//
		// }

		// if (test_y != cmtoY) {
		// test_y += (cmtoY - test_y) / 1;
		// if (Math.abs((cmtoY - test_y) / 1) <= 1) {
		// test_y = cmtoY;
		// }
		// }

	}

	public void close() {
		m_cmdCenter = null;
		m_cmdRight = null;
		m_cmdLeft = null;
		backScreen.switchToMe();
		m_arrList = null;
	}

	public void setInfoRegister(Message message) {
		m_bIsRegistered = MessageIO.readBoolean(message);
		isTabThanBai = true;
		String str = "";
		try {
			str = MessageIO.readString(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (m_width == 0)
			m_width = GameResource.instance.imgListScr_Panel.getWidth() / 8 * 7;
		String[] m_list = BitmapFont.splitString(str, m_width, true);
		setListItems(m_list, GameCanvas.hBottomBar + 50);

		m_bIsShowChampion = MessageIO.readBoolean(message);
		if (m_bIsShowChampion) {
			m_championAvtId = MessageIO.readInt(message);
			m_championNick = MessageIO.readString(message);
			m_championName = MessageIO.readString(message);
		}

		if (m_bIsRegistered) {
			m_cmdCenter = null;
		} else {
			m_cmdCenter = cmdRegister;
		}
		m_cmdLeft = cmdMenu;
	}

	public void setCompetition(String readString) {
		m_arrList = BitmapFont.splitString(readString, m_width, true);
		TabScr.gI().m_activeScr.m_cmdCenter = null;
		TabScr.gI().m_cmdCenter = null;
	}

	public void getStringGuide() {
		String[] str = null;
		if (backScreen instanceof ClanScr) {
			str = BitmapFont.splitString(GameResource.guideClan, GameResource.instance.imgListScr_Panel.getWidth()
					- (int) (40 / HDCGameMidlet.instance.scale), false);
		} else if (backScreen instanceof TabScr) {
			if (TabScr.gI().m_activeScr instanceof ListStringScr) {
				str = BitmapFont.splitString(GameResource.guideCompetition,
						GameResource.instance.imgListScr_Panel.getWidth() - (int) (40 / HDCGameMidlet.instance.scale),
						false);
			} else {
				switch (m_iSelectedTypeGame) {
				case MainScr.TLMN:
					str = BitmapFont.splitString(GameResource.guideTLMN, m_width, false);
					break;
				case MainScr.TLMB:
					str = BitmapFont.splitString(GameResource.guideTLMB, m_width, false);
					break;
				case MainScr.PHOM:
					str = BitmapFont.splitString(GameResource.guidePHOM,
							GameResource.instance.imgListScr_Panel.getWidth()
									- (int) (40 / HDCGameMidlet.instance.scale), false);
					break;
				case MainScr.XITO:
					str = BitmapFont.splitString(GameResource.guideXITO, m_width, false);
					break;
				default:
					str = BitmapFont.splitString(GameResource.guideCAO, m_width, false);
					break;
				}
			}
		}
		setListItems(str, GameCanvas.hBottomBar + 10);
		str = null;
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		if (m_cmdLeft == null)
			HDCGameMidlet.instance.gameCanvas.menu.m_list = null;
		else
			m_cmdLeft.action.perform();
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		close();
	}
}