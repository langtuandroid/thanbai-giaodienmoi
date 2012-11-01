package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Button;
import com.hdc.mycasino.model.Clan;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.ItemInfo;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;
import com.hdc.mycasino.utilities.TField;

public class ClanInformationScr extends Screen {
	private static final byte LINE_SPACING = (byte) (20 * (int) HDCGameMidlet.instance.scale);

	public int itemId = 0;
	public String clanName;
	public String nickHeadman;
	public int numberMember;
	public long gold;
	public int score;
	public int level;
	Command cmdSave;
	Command cmdEdit;
	Command cmClose;
	Command cmdBack;
	Command cmdMenu;
	TField statusTF;
	String m_strStatus;

	public int colorName = 0xffffff;
	int colorLevel = 0xffffff;

	Screen backScreen;

	boolean isPaint = false;

	private static ClanInformationScr instance;

	// TODO Clan
	private Clan m_Clan;

	// TODO button
	private Button mButton_LuuThongTin;

	// TODO array status
	private String[] m_arrStatus;

	public static ClanInformationScr gI() {
		if (instance == null)
			instance = new ClanInformationScr();
		return instance;
	}

	public ClanInformationScr() {
		cmdSave = new Command(GameResource.save, new IAction() {
			public void perform() {
				// if (statusTF.getText().equals(m_strStatus)) {
				// GameCanvas.startOKDlg("Không có thay đổi gì!");
				// } else {
				GameCanvas.startWaitDlg();
				GlobalService.sendMessageUpdateClanStatus(statusTF.getText());
				// }
			}
		});

		cmdEdit = new Command(GameResource.edit, new IAction() {
			public void perform() {
				TabScr.gI().setCommandActive(cmdBack, cmdSave, statusTF.cmdClear);
				statusTF.isFocus = true;
				statusTF.isReadonly = false;
			}
		});

		cmdBack = new Command(GameResource.back, new IAction() {
			public void perform() {
				TabScr.gI().setCommandActive(cmdMenu, cmdEdit, cmClose);
				statusTF.isFocus = false;
				statusTF.isReadonly = true;
			}
		});
		cmdMenu = new Command(GameResource.menu, new IAction() {

			@SuppressWarnings({ "unchecked", "rawtypes", "rawtypes" })
			public void perform() {

				Vector vt = new Vector();
				vt.addElement(new Command(GameResource.update, 0, new IAction() {
					public void perform() {
						GlobalService.sendMessageGetMyClanInformation();
						GameCanvas.startWaitDlg();
					}
				}));
				vt.addElement(new Command(GameResource.saveMoney, 0, new IAction() {
					public void perform() {
						String nameClan = ClanScr.gI().myClanOwner.itemName;
						GameCanvas.inputDlg.setInfo("Đóng góp tiền cho ban " + nameClan,
								new IAction() {
									public void perform() {
										try {
											int money = Integer
													.parseInt(GameCanvas.inputDlg.tfInput.getText());
											if (money > 0) {
												GlobalService.sendMessageContributeMoneyClan(money,
														0);
											} else {
//												GameCanvas
//														.startOKDlg("Vui lòng điền số tiền muốn chuyển cho gia tộc");
												HDCGameMidlet.instance.showDialog_Okie("Thông báo","Vui lòng điền số tiền muốn chuyển cho gia tộc");
											}
										} catch (Exception ex) {
										}
									}
								}, TField.INPUT_TYPE_NUMERIC);
						GameCanvas.inputDlg.show();
					}
				}));

				vt.addElement(new Command(GameResource.savePoint, 0, new IAction() {
					public void perform() {
						String nameClan = ClanScr.gI().myClanOwner.itemName;
						GameCanvas.inputDlg.setInfo("Đóng góp điểm (1 điểm gia tộc = 1000 gold)"
								+ nameClan, new IAction() {
							public void perform() {
								try {
									int score = Integer.parseInt(GameCanvas.inputDlg.tfInput
											.getText());
									if (score > 0) {
										GlobalService.sendMessageContributeMoneyClan(0, score);
									} else {
//										GameCanvas
//												.startOKDlg("Vui lòng điền số điểm muốn chuyển cho gia tộc");
										HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Vui lòng điền số điểm muốn chuyển cho gia tộc");
									}
								} catch (Exception ex) {
								}
							}
						}, TField.INPUT_TYPE_NUMERIC);
						GameCanvas.inputDlg.show();
					}
				}));

				vt.addElement(new Command(GameResource.out, 0, new IAction() {
					public void perform() {
						GameCanvas.startOKDlg("Bạn có muốn rời khỏi gia tộc?", new IAction() {
							public void perform() {
								GlobalService.sendMessageRejectClan();
							}
						});
					}
				}));

				GameCanvas.instance.menu.startAt(vt, 0);
				vt = null;
			}
		});

		statusTF = new TField();
		statusTF.x = m_left;
		statusTF.y = m_top + LINE_SPACING * 6;
		statusTF.height = GameCanvas.h - GameCanvas.hBottomBar - statusTF.y - 20
				* (int) HDCGameMidlet.instance.scale;
		statusTF.width = GameCanvas.w - m_left * 2;
		statusTF.isFocus = false;
		statusTF.isReadonly = true;
		statusTF.textColor = 0xffffff;
		statusTF.focusTextColor = 0xffffff;
		statusTF.setIputType(TField.INPUT_TYPE_ANY);
		statusTF.setMaxTextLenght(100);
	}

	// TODO set textfield
	private void setTextField() {
		// TODO textfield name
		statusTF.MAX_LENGTH_TEXT = 40;
		statusTF.x = m_left + GameResource.instance.imgListScr_Panel.getWidth() / 4
				- (int) (40 / HDCGameMidlet.instance.scale);
		statusTF.y = m_top + GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 3 * 2;
		statusTF.width = GameResource.instance.imgListScr_Panel.getWidth() / 4 * 3;
		statusTF.height = (int) (100 / HDCGameMidlet.instance.scale);
		statusTF.isFocus = true;
		statusTF.isReadonly = true;
		statusTF.isPaintBG = false;

	}

	public void setClanInfo(Message message, Command close) {
		m_top = GameCanvas.hBottomBar + 40 * (int) HDCGameMidlet.instance.scale;
		m_left = 10 * (int) HDCGameMidlet.instance.scale;
		m_width = GameCanvas.w - (m_left << 1);
		m_height = LINE_SPACING * 6 + 30 * (int) HDCGameMidlet.instance.scale;
		// statusTF.y = m_top + LINE_SPACING * 7;

		itemId = 0;

		clanName = MessageIO.readString(message);
		setStatus(MessageIO.readString(message));
		nickHeadman = MessageIO.readString(message);
		gold = MessageIO.readLong(message);
		score = MessageIO.readInt(message);
		level = MessageIO.readInt(message);
		// //////get color
		colorLevel = getColor(level);
		// ///////////////
		numberMember = MessageIO.readInt(message);

		cmClose = close;
		m_cmdCenter = null;
		if (ClanScr.gI().clanRole != 3) {
			m_cmdCenter = cmdEdit;
		}
		m_cmdLeft = cmdMenu;
		m_cmdRight = cmClose;

		isPaint = false;

		// TODO set location
		setLocation();

		// TODO set textfeild
		setTextField();

		// TODO init button
		initButton();

	}

	public static void closeAll() {
		if (instance != null) {
			instance.clanName = null;
			instance.nickHeadman = null;
			instance.cmdSave = null;
			instance.cmdEdit = null;
			instance.cmClose = null;
			instance.cmdBack = null;
			instance.cmdMenu = null;
			instance.statusTF = null;
			instance.m_strStatus = null;
			instance.backScreen = null;
		}
		instance = null;
	}

	public void switchToMe(Screen backScr) {
		m_cmdLeft = null;
		m_cmdCenter = null;
		m_cmdRight = new Command(GameResource.close, new IAction() {
			public void perform() {
				// TODO Auto-generated method stub
				if (backScreen != null)
					backScreen.switchToMe();
				closeAll();
			}
		});

		backScreen = backScr;
		m_strTitle = clanName;

		super.switchToMe();
	}

	public void setClanInfo(Clan clan) {
		m_top = GameCanvas.hBottomBar + 10 * (int) HDCGameMidlet.instance.scale;
		m_left = 10 * (int) HDCGameMidlet.instance.scale;
		m_width = GameCanvas.w - (m_left << 1);
		m_height = LINE_SPACING * 6 + 30 * (int) HDCGameMidlet.instance.scale;
		// statusTF.y = m_top + LINE_SPACING * 7;

		clanName = clan.itemName;
		gold = clan.money;
		score = clan.score;
		level = clan.level;
		// //////get color
		colorLevel = getColor(level);
		// ///////////////
		numberMember = clan.totalMembers;
		isPaint = true;

		// TODO set clan
		// m_Clan = clan;
		itemId = 0;

		// TODO set location
		setLocation();

		// TODO set textfeild
		setTextField();
	}

	public int getColor(int level) {
		int get = level / 5;

		if (level < 0)
			return GameResource.listColor[0];
		if (level > 51)
			return GameResource.listColor[GameResource.instance.listLevel.length - 1];
		return GameResource.listColor[get];
	}

	@Override
	public void loadImg() {
		// TODO Auto-generated method stub
		super.loadImg();

		if (HDCGameMidlet.instance.scale == 2f) {

			float scale;
			if ((GameCanvas.w <= 480 && GameCanvas.w > 320)
					&& (GameCanvas.h <= 320 && GameCanvas.h > 240)) {
			}
			// màn hình 240x320
			// scale panel cho list screen tỷ lệ : 2/5
			else {
				scale = (float) 2 / 5;
				GameResource.instance.imgListScr_Panel = ImagePack.createImage(
						ImagePack.LIST_SCREEN_PANEL, scale);
				// scale hight light
				GameResource.instance.imgListScr_HightLight = Image.scaleImage(
						GameResource.instance.imgListScr_HightLight,
						GameResource.instance.imgListScr_Panel.getWidth(),
						GameResource.instance.imgListScr_HightLight.getHeight());

			}
		}

	}

	// TODO set location for list item
	// m_left & m_top
	private void setLocation() {
		m_left = (GameCanvas.w - GameResource.instance.imgListScr_Panel.getWidth()) / 2;
		m_top = (int) (60 / HDCGameMidlet.instance.scale);

		// HDCGameMidlet.instance.Toast(m_left + " - " + m_top);
	}

	public void close() {

	}

	public void update() {
		// if (statusTF.isFocus)
		// statusTF.update();

		if (!statusTF.getText().equals("") && !statusTF.getText().equals(m_strStatus)) {
			m_strStatus = statusTF.getText();
			m_arrStatus = BitmapFont
					.splitString(
							m_strStatus,
							(int) (GameResource.instance.imgListScr_Panel.getWidth() / 4 * 3 - (20 / HDCGameMidlet.instance.scale)),
							false);
		}
		if (!isPaint)
			statusTF.update();

	}

	public void updateKey() {

		if (!isPaint && m_cmdCenter != null)
			mButton_LuuThongTin.updateKey();

		if (GameCanvas.isPointerDown) {
			if (GameCanvas.pyLast > GameCanvas.py) {
				updateScroll(-10);
			} else {
				updateScroll(10);
			}
		}

		if (GameCanvas.keyHold[2]) {
			updateScroll(+10);
		}
		if (GameCanvas.keyHold[8]) {
			updateScroll(-10);
		}

		super.updateKey();
	}

	public int limit = 0;

	public void initScroll() {
		limit = GameCanvas.h - m_top - m_height;
		cmtoY = cmy = limit;
	}

	public void updateScroll(int dir) {
		// if (GameCanvas.instance.menu.m_showMenu
		// || GameCanvas.currentDialog != null)
		// return;
		cmtoY += dir * (int) HDCGameMidlet.instance.scale;
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

	// TODO init button lưu thông tin
	private void initButton() {
		// TODO button lưu thông tin
		mButton_LuuThongTin = new Button(GameResource.instance.imgButton_Login,
				GameResource.saveInfo, cmdSave);
		mButton_LuuThongTin.setXY(
				(GameResource.instance.imgListScr_Panel.getWidth() - mButton_LuuThongTin.w)
						+ m_left - (int) (20 / HDCGameMidlet.instance.scale), GameCanvas.h
						- GameResource.instance.imgButton_Login.getHeight() / 2 * 3);

	}

	public void setStatus(String status) {
		m_strStatus = status;
		m_arrStatus = BitmapFont
				.splitString(
						status,
						(int) (GameResource.instance.imgListScr_Panel.getWidth() / 4 * 3 - (20 / HDCGameMidlet.instance.scale)),
						false);
		m_height = LINE_SPACING * 6 + 60 * (int) HDCGameMidlet.instance.scale;
		initScroll();
		statusTF.setText(status);
	}

	// TODO paint avatar
	private void paintItem(Graphics g, float x, float y, int avatarID, int select,
			FrameImage m_frame) {
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

	// TODO paint name
	private void paintName(Graphics g) {
		BitmapFont.drawItalicFont(g, "Gia tộc : " + clanName, m_left
				+ GameResource.instance.imgAvatar_Khung_High_Light.getWidth() / 4, m_top
				+ GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 4 * 7, /* colorName */
				0xffb901, Graphics.LEFT | Graphics.TOP);
	}

	// TODO paint tộc chủ
	private void paintTocChu(Graphics g) {
		BitmapFont.drawItalicFont(g, GameResource.clanOwner, m_left
				+ GameResource.instance.imgAvatar_Khung_High_Light.getWidth() / 4, m_top
				+ GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 4 * 7
				+ (30 / HDCGameMidlet.instance.scale), 0xffb901, Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawBoldFont(g, nickHeadman, m_left
				+ GameResource.instance.imgAvatar_Khung_High_Light.getWidth() / 4
				+ BitmapFont.m_bmNormalFont.stringWidth(GameResource.clanOwner), m_top
				+ GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 4 * 7
				+ (30 / HDCGameMidlet.instance.scale), 0xcccccc, Graphics.LEFT | Graphics.TOP);
	}

	// TODO paint thành viên
	private void paintThanhVien(Graphics g) {
		BitmapFont.drawItalicFont(g, GameResource.member, m_left
				+ GameResource.instance.imgAvatar_Khung_High_Light.getWidth() / 4, m_top
				+ GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 4 * 7
				+ (60 / HDCGameMidlet.instance.scale), 0xffb901, Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawBoldFont(g, numberMember + "", m_left
				+ GameResource.instance.imgAvatar_Khung_High_Light.getWidth() / 4
				+ BitmapFont.m_bmNormalFont.stringWidth(GameResource.member), m_top
				+ GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 4 * 7
				+ (60 / HDCGameMidlet.instance.scale), 0xcccccc, Graphics.LEFT | Graphics.TOP);
	}

	// TODO paint gold
	private void paintGold(Graphics g) {
		BitmapFont.drawItalicFont(g, GameResource.haveGold, m_left
				+ GameResource.instance.imgAvatar_Khung_High_Light.getWidth() / 4, m_top
				+ GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 4 * 7
				+ (90 / HDCGameMidlet.instance.scale), 0xffb901, Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawBoldFont(g, gold + "", m_left
				+ GameResource.instance.imgAvatar_Khung_High_Light.getWidth() / 4
				+ BitmapFont.m_bmNormalFont.stringWidth(GameResource.haveGold), m_top
				+ GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 4 * 7
				+ (90 / HDCGameMidlet.instance.scale), 0xcccccc, Graphics.LEFT | Graphics.TOP);
	}

	// TODO paint Điểm
	private void paintDiem(Graphics g) {
		BitmapFont.drawItalicFont(g, GameResource.points, m_left
				+ GameResource.instance.imgAvatar_Khung_High_Light.getWidth() / 4, m_top
				+ GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 4 * 7
				+ (120 / HDCGameMidlet.instance.scale), 0xffb901, Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawBoldFont(g, score + "", m_left
				+ GameResource.instance.imgAvatar_Khung_High_Light.getWidth() / 4
				+ BitmapFont.m_bmNormalFont.stringWidth(GameResource.points), m_top
				+ GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 4 * 7
				+ (120 / HDCGameMidlet.instance.scale), 0xcccccc, Graphics.LEFT | Graphics.TOP);
	}

	// TODO paint status
	private void paintStatus(Graphics g) {
		BitmapFont.drawUnderlineFont(g, GameResource.status, m_left
				+ GameResource.instance.imgListScr_HightLight.getWidth() / 4, m_top
				+ GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 3, 0xffb901,
				Graphics.LEFT | Graphics.TOP);
		for (int i = 0; i < m_arrStatus.length; i++) {
			BitmapFont.drawBoldFont(g, m_arrStatus[i], statusTF.x
					+ (20 / HDCGameMidlet.instance.scale),
					statusTF.y + (20 / HDCGameMidlet.instance.scale) + i
							* (30 / HDCGameMidlet.instance.scale), 0xcccccc, Graphics.LEFT
							| Graphics.TOP);
		}
	}

	public void paint(Graphics g) {
		if (isPaint) {
			PaintPopup.paintBackground(g, m_strTitle);
			// TODO paint panel
			g.drawImage(GameResource.instance.imgListScr_Panel, m_left, m_top, Graphics.LEFT
					| Graphics.TOP);
		}

		// TODO paint avatar
		paintItem(g, m_left + GameResource.instance.imgAvatar_Khung_High_Light.getWidth() / 4,
				m_top + GameResource.instance.imgAvatar_Khung_High_Light.getHeight() / 4 * 5,
				itemId, 1, GameResource.instance.m_frameVatPham_Icon);

		// TODO paint name
		paintName(g);

		// TODO paint toc chu
		paintTocChu(g);

		// TODO paint thanh vien
		paintThanhVien(g);

		// TODO paint gold
		paintGold(g);

		// TODO paint diem
		paintDiem(g);

		// TODO paint status
		paintStatus(g);

		// TODO paint button Luu thông tin
		// trong trường hợp "Tộc chủ" của gia tộc đó
		if (!isPaint && m_cmdCenter != null)
			mButton_LuuThongTin.paint(g);

		statusTF.paint(g);

		if (isPaint)
			super.paint(g);
	}

	int m_iNumHeight = 0;

	public void keyPress(int keyCode) {
		if (statusTF.isFocus) {
			// statusTF.keyPressed(keyCode);
			String[] str = BitmapFont.m_bmNormalFont.splitFontBStrInLine(statusTF.getText(),
					statusTF.width);
			if (str.length > m_iNumHeight) {
				m_iNumHeight = str.length;
				m_height += LINE_SPACING;
				initScroll();
			}

		}
		super.keyPress(keyCode);
	}

	public void updateGoldAndScore(Message message) {
		String content = MessageIO.readString(message);
		long money = MessageIO.readLong(message);
		int score = MessageIO.readInt(message);
		this.gold = this.gold + money;
		this.score = this.score + score;
//		GameCanvas.startOKDlg(content);
		HDCGameMidlet.instance.showDialog_Okie("Thông báo", content);		
		content = null;
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		if (!isPaint && m_cmdLeft != null) {
			// HDCGameMidlet.instance.Toast("doMenu - ClanInfomationScr");
			m_cmdLeft.action.perform();
		}

	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		if (m_cmdRight != null)
			m_cmdRight.action.perform();

	}
}
