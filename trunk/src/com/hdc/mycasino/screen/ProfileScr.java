package com.hdc.mycasino.screen;

import java.util.Vector;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.danh.view.GridView;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Button;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.Item;
import com.hdc.mycasino.model.ItemInfo;
import com.hdc.mycasino.model.PlayerInfo;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;
import com.hdc.mycasino.utilities.TField;

public class ProfileScr extends Screen {
	public int m_iNumTabDisplay = 4;
	public int m_iTopDisplay = 98;
	public int m_iLeftDisplay = 10;
	// public int m_iTopScroll = GameCanvas.hBottomBar + 25;
	public int numberItemsGood = 0;
	public int m_wCellAvt = 30;
	public int m_iHeightItemGood = 55;

	private int tab;
	private int m_top = 10;
	private int m_left = 10;
	private int m_colAvt = 6;
	private int rectHeight = 23;
	private int focusedTabIndex;

	private static ProfileScr instance;

	private Command m_cmdSavePersonalInfo;
	private Command m_cmdSaveAvatar;
	private Command m_cmdUseItem;
	private Command m_cmdEdit;
	private Command m_cmdClose;

	private TField tfName;
	private TField tfGender;
	private TField tfStatus;

	private Screen backScr;
	private PlayerInfo playerInfo;

	public boolean m_bIsEdit = false;

	private Vector m_strTab = new Vector();
	public String info[];

	// TODO image tab
	Image imgTab;
	int m_iFocusTab = 0;

	// TODO button
	Button mButton_LuuThongTin;
	Button mButton_SuDungAvatar;
	Button mButton_SuDungVatPham;

	// TODO scroll
	public int m_iHeightItem = (int) (120 / HDCGameMidlet.scale);// =
																	// (int)
	// (70 /
	// HDCGameMidlet.instance.scale);
	public int m_iTopScroll = (int) (40 / HDCGameMidlet.scale);// =
																// (int)
																// (70
																// /
																// HDCGameMidlet.instance.scale);
	// TODO columne row in tab avatar
	private int col;
	private int row;
	private int col_select;
	private int row_select;
	private int m_widthItem;
	private int m_heightItem;

	int index = 0;

	// TODO gridview avatar
	private GridView m_gridView_Avatar;
	// TODO gridview vatpham
	private GridView m_gridView_VatPham;

	public void close() {
		backScr.switchToMe();
		if (backScr instanceof TabScr) {
			TabScr.gI().setActiveScr(TabScr.gI().m_activeScr);
		} else if (backScr instanceof ListBoardScr) {
			// ListBoardScr.getInstance().initCam();
		}
		m_iFocusTab = 0;
	}

	@SuppressWarnings("unchecked")
	public ProfileScr() {
		m_strTab.addElement(GameResource.info);
		m_strTab.addElement("Thành tích");
		m_strTab.addElement("Avatar");
		m_strTab.addElement("Vật phẩm");

		tfName = new TField();
		// tfName.setIputType(TField.INPUT_TYPE_ANY);
		// tfName.setMaxTextLenght(25);
		tfName.x = 60 + BitmapFont.m_bmNormalFont.stringWidth("Ten: ");
		tfName.y = 55;
		tfName.width = GameCanvas.w - 88;
		tfName.height = 17;

		tfGender = new TField();
		tfGender.x = 60 + BitmapFont.m_bmNormalFont.stringWidth("Gioi Tinh: ");
		tfGender.y = 72;
		tfGender.width = GameCanvas.w - 114;
		tfGender.height = 17;

		tfStatus = new TField();
		tfStatus.setMaxTextLenght(100);
		tfStatus.x = m_iLeftDisplay + BitmapFont.m_bmNormalFont.stringWidth("Status: ");
		tfStatus.y = 95;
		tfStatus.width = GameCanvas.w - 58;
		tfStatus.height = 17;

		numberItemsGood = HDCGameMidlet.m_myPlayerInfo.m_listItems.size();
		m_width = GameCanvas.w - 20;
		m_height = GameCanvas.h;
		m_wCellAvt = 45;
		// / m_colAvt = m_width / (m_wCellAvt + 5);
		m_width = m_colAvt * (m_wCellAvt + 5);
		m_left = GameCanvas.hw - m_width / 2;
		m_selected = -1;

		m_cmdSavePersonalInfo = new Command("Lưu", new IAction() {
			public void perform() {
				info = new String[] { tfName.getText(), GameResource.getEnum(tfGender.getText()),
						tfStatus.getText() };
				GlobalService.onUpdateProfile(info);
				GameCanvas.startWaitDlg();
			}
		});

		m_cmdSaveAvatar = new Command("Đổi Avatar", new IAction() {
			public void perform() {
				// m_selected = row_select * m_colAvt + col_select;
				m_selected = m_gridView_Avatar.getIndex();
				ItemInfo avatar = (ItemInfo) HDCGameMidlet.m_myPlayerInfo.m_listAvatars
						.elementAt(m_selected);
				if (avatar != null && avatar.itemId != HDCGameMidlet.m_myPlayerInfo.avatarId) {
					GlobalService.sendMessageChangeAvatar(avatar.itemId);
					GameCanvas.startWaitDlg();
				} else {
					// GameCanvas.startOKDlg("Hãy chọn một avatar khác với avatar hiện tại");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",
							"Hãy chọn một avatar khác với avatar hiện tại");
				}
			}
		});

		m_cmdUseItem = new Command("Sử dụng", new IAction() {
			public void perform() {
				m_selected = m_gridView_VatPham.getIndex();
				if (m_selected >= 0) {
					ItemInfo item = (ItemInfo) HDCGameMidlet.m_myPlayerInfo.m_listItems
							.elementAt(m_selected);
					if (item != null && !item.m_bExpired) {
						switch (item.m_itemType) {
						case Item.BUT_DOI_MAU:
							ListColorScr.getInstance().switchToMe(ProfileScr.gI());
							break;
						case Item.THE_CHONG_KICK:
							// GameCanvas.startOKDlg("Đang sử dụng vật phẩm này");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo",
									"Đang sử dụng vật phẩm này");
							break;
						case Item.THE_RESET_THANH_TICH:
							if (item.m_iNumberExpiredUses > 0) {
								GlobalService.sendMessageResetThanhTich();
							} else {
								// GameCanvas.startOKDlg("Đã hết số lần sử dụng vật phẩm này");
								HDCGameMidlet.instance.showDialog_Okie("Thông báo",
										"Đã hết số lần sử dụng vật phẩm này");
							}
							break;
						case Item.THE_NHAN_DOI_KINH_NGHIEM:
						case Item.THE_MIEN_THUE:
						case Item.UY_DANH_LENH:
							// GameCanvas.startOKDlg("Đang sử dụng vật phẩm này");
							HDCGameMidlet.instance.showDialog_Okie("Thông báo",
									"Đang sử dụng vật phẩm này");
							break;
						default:
							break;
						}
					} else {
						// GameCanvas.startOKDlg("Không thể sử dụng vật phẩm này");
						HDCGameMidlet.instance.showDialog_Okie("Thông báo",
								"Không thể sử dụng vật phẩm này");
					}
				} else {
					// GameCanvas.startOKDlg("Vui lòng chọn vật phẩm để sử dụng");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo",
							"Vui lòng chọn vật phẩm để sử dụng");
				}
			}
		});

		m_cmdClose = new com.hdc.mycasino.model.Command(GameResource.close, new IAction() {

			public void perform() {
				close();
			}
		});

		m_cmdEdit = new Command("Chỉnh sửa", new IAction() {
			public void perform() {
				if (m_bIsEdit) {
					if (focusedTabIndex == 7) {
						String[] arrayValues = GameResource.getArrayValues();
						Vector vt = new Vector();
						for (int i = 0; i < arrayValues.length; i++) {
							final String value = arrayValues[i];
							vt.addElement(new Command(value, new IAction() {
								public void perform() {
									tfGender.setText(value);
								}
							}));
						}
						GameCanvas.instance.menu.startAt(vt, 0);
						vt = null;
					}
				}
			}
		});

		tfStatus.focus_color1 = 0x0f2a14;
		tfStatus.focus_color2 = 0xf7dc23;
		tfStatus.focus_color3 = 0xf7dc23;

		tfStatus.textColor = 0x969696;
		tfStatus.focusTextColor = 0x969696;

		tfName.textColor = 0x969696;
		tfName.focusTextColor = 0x969696;

		tfGender.textColor = 0x969696;
		tfGender.focusTextColor = 0x969696;

		tfStatus.isReadonly = true;
		tfStatus.isPaintBG = false;

		tfName.isReadonly = true;
		tfName.isPaintBG = false;

		tfGender.isReadonly = true;
		tfGender.isPaintBG = false;
	}

	// TODO init button
	@SuppressWarnings("static-access")
	private void initButton() {
		// TODO button lưu thông tin
		mButton_LuuThongTin = new Button(GameResource.instance.imgButton_Login,
				GameResource.instance.saveInfo, m_cmdSavePersonalInfo);
		mButton_LuuThongTin.setXY(
				(GameResource.instance.imgListScr_Panel.getWidth() - mButton_LuuThongTin.w)
						+ m_left - (int) (20 / HDCGameMidlet.instance.scale), m_top
						+ GameResource.instance.imgAvatar_Khung.getHeight() / 4 * 3);
		// mButton_LuuThongTin.startEffect();

		// TODO button sử dụng avatar
		mButton_SuDungAvatar = new Button(GameResource.instance.imgButton_Login,
				GameResource.instance.useAvatar, m_cmdSaveAvatar);
		mButton_SuDungAvatar.setXY(
				(GameResource.instance.imgListScr_Panel.getWidth() - mButton_SuDungAvatar.w)
						+ m_left - (int) (20 / HDCGameMidlet.instance.scale), m_top
						+ GameResource.instance.imgAvatar_Khung.getHeight() / 4 * 3);
		// mButton_SuDungAvatar.startEffect();

		// TODO button sử dụng avatar
		mButton_SuDungVatPham = new Button(GameResource.instance.imgButton_Login,
				GameResource.instance.useGolds, m_cmdUseItem);
		mButton_SuDungVatPham.setXY(
				(GameResource.instance.imgListScr_Panel.getWidth() - mButton_SuDungVatPham.w)
						+ m_left - (int) (20 / HDCGameMidlet.scale), m_top
						+ GameResource.instance.imgAvatar_Khung.getHeight() / 4 * 3);
		// mButton_SuDungVatPham.startEffect();

	}

	// TODO set text field
	private void setTextField() {
		// TODO textfield name
		tfName.MAX_LENGTH_TEXT = 30;
		tfName.x = m_left;
		tfName.y = m_top + GameResource.instance.imgAvatar_Khung.getHeight() / 2 * 3
				+ (int) (10 / HDCGameMidlet.scale);
		tfName.width = GameResource.instance.imgListScr_Panel.getWidth();
		tfName.height = (int) (60 / HDCGameMidlet.scale);
		tfName.isFocus = true;

		// TODO textfield status
		tfStatus.MAX_LENGTH_TEXT = 30;
		tfStatus.x = m_left;
		tfStatus.y = tfName.y + (int) (70 / HDCGameMidlet.scale);
		tfStatus.width = GameResource.instance.imgListScr_Panel.getWidth();
		tfStatus.height = (int) (60 / HDCGameMidlet.scale);
		tfStatus.isFocus = false;

		// TODO textfield status
		tfGender.MAX_LENGTH_TEXT = 20;
		tfGender.x = m_left;
		tfGender.y = tfStatus.y + (int) (70 / HDCGameMidlet.scale);
		tfGender.width = GameResource.instance.imgListScr_Panel.getWidth();
		tfGender.height = (int) (60 / HDCGameMidlet.scale);
		tfGender.isFocus = false;
		tfGender.isShowPopup_Gender = true;
	}

	public static ProfileScr gI() {

		return instance == null ? instance = new ProfileScr() : instance;
	}

	// TODO select tab
	private void selectTab() {
		int numTab = m_strTab.size();
		switch (numTab) {
		case 2:
			switch (m_iFocusTab) {
			case 0:
				imgTab = GameResource.instance.imgTabs_2_0;
				break;
			case 1:
				imgTab = GameResource.instance.imgTabs_2_1;
				break;
			}
			break;
		case 4:
			switch (m_iFocusTab) {
			case 0:
				imgTab = GameResource.instance.imgTabs_4_0;
				break;
			case 1:
				imgTab = GameResource.instance.imgTabs_4_1;
				break;
			case 2:
				imgTab = GameResource.instance.imgTabs_4_2;
				break;
			case 3:
				imgTab = GameResource.instance.imgTabs_4_3;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

		if (GameResource.instance.imgListScr_Panel.getWidth() != imgTab.getWidth()) {
			imgTab = Image.scaleImage(imgTab, GameResource.instance.imgListScr_Panel.getWidth(),
					imgTab.getHeight());
		}

	}

	private static int deltaX = 0;
	private static int deltaY = 0;

	public void selectEffect(Graphics g, int x, int y) {
		if (deltaX <= 2) {
			g.drawRegion(GameResource.instance.imgIconCard, 93, 86, 11, 11, 0, x - deltaX - 5, y
					- deltaY - 5, 0);
			g.drawRegion(GameResource.instance.imgIconCard, 93, 86, 11, 11, Sprite.TRANS_ROT90, x
					+ deltaX + 27, y - deltaY - 5, 0);

			g.drawRegion(GameResource.instance.imgIconCard, 93, 86, 11, 11,
					Sprite.TRANS_MIRROR_ROT180, x - deltaX - 5, y + deltaY + 30, 0);
			g.drawRegion(GameResource.instance.imgIconCard, 93, 86, 11, 11,
					Sprite.TRANS_MIRROR_ROT90, x + 27 + deltaX, y + deltaY + 30, 0);

			deltaX += 1;
			deltaY += 1;
		} else {
			deltaX = 0;

			deltaY = 0;
		}
	}

	public void setGridItem() {
		int yP = (HDCGameMidlet.m_myPlayerInfo.m_listAvatars.size()) / m_colAvt;
		if (HDCGameMidlet.m_myPlayerInfo.m_listAvatars.size() % m_colAvt != 0)
			yP++;
		GameCanvas.cameraList.setInfo(m_left - 5, m_top + 85, m_wCellAvt, m_wCellAvt, m_width - 15,
				yP * m_wCellAvt, m_width, GameCanvas.h - GameCanvas.hBottomBar - m_top - 90,
				HDCGameMidlet.m_myPlayerInfo.m_listAvatars.size());

		m_selected = -1;
	}

	public void setListItem() {
		// m_selected = 0;
		// GameCanvas.cameraList.setInfo(m_left, m_iTopScroll, m_width,
		// m_iHeightItemGood, GameCanvas.w, numberItemsGood
		// * m_iHeightItemGood, m_width, GameCanvas.h - m_iTopScroll -
		// m_iHeightItemGood, numberItemsGood);
		// setSelected(0);
		GameCanvas.cameraList.setInfo(m_left, m_iTopScroll, m_width, m_iHeightItemGood, m_width,
				numberItemsGood * m_iHeightItemGood, m_width, GameCanvas.h - m_iTopScroll
						- m_iHeightItemGood, numberItemsGood);
	}

	// TODO set location for list item
	// m_left & m_top
	private void setLocation() {
		m_left = (GameCanvas.w - GameResource.instance.imgListScr_Panel.getWidth()) / 2;
		m_top = (int) (60 / HDCGameMidlet.scale);

	}

	// TODO paint nick
	@SuppressWarnings("static-access")
	private void paintNick(Graphics g) {
		BitmapFont.m_bmNormalFont.drawNormalFont(g, "Nick : ", m_left + (20 / HDCGameMidlet.scale)
				+ GameResource.instance.imgAvatar_Khung.getWidth() * 2, m_top
				+ GameResource.instance.imgAvatar_Khung.getHeight() / 4 * 3, 0xffb901,
				Graphics.LEFT | Graphics.VCENTER);

		BitmapFont.m_bmNormalFont.drawNormalFont(g, playerInfo.itemName, m_left
				+ (20 / HDCGameMidlet.scale) + GameResource.instance.imgAvatar_Khung.getWidth() * 2
				+ BitmapFont.m_bmNormalFont.stringWidth("Nick : "), m_top
				+ GameResource.instance.imgAvatar_Khung.getHeight() / 4 * 3, 0xffffff,
				Graphics.LEFT | Graphics.VCENTER);
	}

	// TODO paint danh hiệu
	@SuppressWarnings("static-access")
	private void paintDanhHieu(Graphics g) {
		BitmapFont.m_bmNormalFont.drawNormalFont(
				g,
				"Danh hiệu : ",
				m_left + (20 / HDCGameMidlet.scale)
						+ GameResource.instance.imgAvatar_Khung.getWidth() * 2, m_top
						+ GameResource.instance.imgAvatar_Khung.getHeight() / 4 * 5, 0xffb901,
				Graphics.LEFT | Graphics.VCENTER);

		BitmapFont.m_bmNormalFont.drawNormalFont(g, playerInfo.medal, m_left
				+ (20 / HDCGameMidlet.scale) + GameResource.instance.imgAvatar_Khung.getWidth() * 2
				+ BitmapFont.m_bmNormalFont.stringWidth("Danh hiệu : "), m_top
				+ GameResource.instance.imgAvatar_Khung.getHeight() / 4 * 5, 0xff0000,
				Graphics.LEFT | Graphics.VCENTER);
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

	// TODO paint Line
	private void paintLine(Graphics g) {
		g.setColor(Color.GRAY);
		g.setAlpha(120);
		g.fillRectWithTransparent(m_left, m_top + GameResource.instance.imgAvatar_Khung.getHeight()
				/ 2 * 3 + (6 / HDCGameMidlet.scale),
				GameResource.instance.imgListScr_Panel.getWidth(), 2);
	}

	// TODO paint background
	private void paintBackground(Graphics g) {
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);

		PaintPopup.paintBackground(g, m_strTitle);

		// TODO paint panel
		g.drawImage(GameResource.instance.imgListScr_Panel, m_left, m_top, Graphics.LEFT
				| Graphics.TOP);
	}

	public void paint(Graphics g) {
		// TODO paint background
		paintBackground(g);

		// TODO paint tab
		paintHeaderTabs(g, GameCanvas.w / 2, 50 / HDCGameMidlet.scale);

		// TODO paint avatar
		paintItem(g, m_left + (20 / HDCGameMidlet.scale), m_top
				+ GameResource.instance.imgAvatar_Khung.getHeight(), playerInfo.avatarId, 0,
				GameResource.instance.m_frameAvatar_IconAvatar);

		// TODO paint nick
		paintNick(g);

		// TODO paint danh hiệu
		paintDanhHieu(g);

		// TODO paint line
		paintLine(g);

		// TODO paint info tab
		paintInfoTab(g);

		super.paint(g);
	}

	// TODO paint info tab
	private void paintInfoTab(Graphics g) {
		switch (m_iFocusTab) {
		case 0:
			paintTabThongTin(g);
			break;
		case 1:
			paintTabThanhTich(g);
			break;
		case 2:
			paintTabAvatar(g);
			break;
		case 3:
			paintTabVatPham(g);
			break;
		}
		g.translate(-g.getTranslateX(), -g.getTranslateY());
	}

	// TODO paint info in tab vật phẩm
	@SuppressWarnings({ "static-access", "unused" })
	private void paintInfo(Graphics g, int x, int y, int width, int height, ItemInfo itemInfo) {
		g.setColor(0xff0000);
		if (x < GameCanvas.w / 2)
			x += m_widthItem / 2;
		else
			x -= (width + m_widthItem / 2);

		if (y < GameCanvas.w / 4 * 3)
			y -= (height / 2);
		else
			y -= height;

		g.fillRoundRectWithGadient(x, y, width, height, (int) (8 / HDCGameMidlet.scale),
				(int) (8 / HDCGameMidlet.scale));

		// paint description
		BitmapFont.setTextSize(16f);
		for (int i = 0; i < itemInfo.m_arrDes.size(); i++) {
			try {
				BitmapFont.m_bmNormalFont.drawNormalFont_1(g,
						(String) itemInfo.m_arrDes.elementAt(i), x + (10 / HDCGameMidlet.scale), y
								+ (10 / HDCGameMidlet.scale), 0xCCCCCC, Graphics.LEFT
								| Graphics.TOP);
				y += (30 / HDCGameMidlet.scale);
			} catch (Exception e) {
			}
		}

		BitmapFont.m_bmNormalFont.drawNormalFont_1(g, itemInfo.getExperiment(), x
				+ (10 / HDCGameMidlet.scale), y + (10 / HDCGameMidlet.scale), 0xffb901,
				Graphics.LEFT | Graphics.TOP);
	}

	public void paintTabVatPham(Graphics g) {
		if (m_gridView_VatPham != null && HDCGameMidlet.m_myPlayerInfo.m_listItems.size() > 0) {
			// TODO paint gridview vat pham
			m_gridView_VatPham.paint(g);

			// TODO paint button sử dụng avatar
			mButton_SuDungVatPham.paint(g);
		}
	}

	// TODO select item (avartar - vật phẩm)
	private void selectItem() {
		if (GameCanvas.isPointer(tfName.x, m_iTopDisplay,
				GameResource.instance.imgListScr_Panel.getWidth(), GameCanvas.h - m_iTopDisplay)) {
			col_select = (int) (GameCanvas.px - tfName.x) / m_widthItem;
			row_select = (int) (GameCanvas.py - m_iTopDisplay) / m_heightItem;

			// TODO sound
			HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.box_plan);
			HDCGameMidlet.sound.play();
		}
	}

	// TODO paint tab avatar
	public void paintTabAvatar(Graphics g) {
		if (m_gridView_Avatar != null && HDCGameMidlet.m_myPlayerInfo.m_listAvatars.size() > 0) {
			// TODO paint gridview avatar
			m_gridView_Avatar.paint(g);

			g.translate(-g.getTranslateX(), -g.getTranslateY());
			g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
			// TODO paint button sử dụng avatar
			mButton_SuDungAvatar.paint(g);
		}
	}

	// TODO paint họ tên
	@SuppressWarnings("static-access")
	private void paintName(Graphics g) {
		BitmapFont.m_bmNormalFont.drawNormalFont(g, "Họ tên : ", m_left
				+ (20 / HDCGameMidlet.scale), tfName.y + (30 / HDCGameMidlet.scale), 0xffb901,
				Graphics.LEFT | Graphics.VCENTER);

		BitmapFont.m_bmNormalFont.drawNormalFont(g, playerInfo.fullName, m_left
				+ (20 / HDCGameMidlet.scale) + BitmapFont.m_bmNormalFont.stringWidth("Họ tên : "),
				tfName.y + (30 / HDCGameMidlet.scale), 0xffffff, Graphics.LEFT | Graphics.VCENTER);
	}

	// TODO paint status
	@SuppressWarnings("static-access")
	private void paintStatus(Graphics g) {
		BitmapFont.m_bmNormalFont.drawNormalFont(g, "Status : ", m_left
				+ (20 / HDCGameMidlet.scale), tfStatus.y + (30 / HDCGameMidlet.scale), 0xffb901,
				Graphics.LEFT | Graphics.VCENTER);

		BitmapFont.m_bmNormalFont
				.drawNormalFont(g, playerInfo.status, m_left + (20 / HDCGameMidlet.scale)
						+ BitmapFont.m_bmNormalFont.stringWidth("Status : "), tfStatus.y
						+ (30 / HDCGameMidlet.scale), 0xffffff, Graphics.LEFT | Graphics.VCENTER);
	}

	// TODO paint Giới tính
	@SuppressWarnings("static-access")
	private void paintGioiTinh(Graphics g) {
		BitmapFont.m_bmNormalFont.drawNormalFont(g, "Giới tính : ", m_left
				+ (20 / HDCGameMidlet.scale), tfGender.y + (30 / HDCGameMidlet.scale), 0xffb901,
				Graphics.LEFT | Graphics.VCENTER);

		BitmapFont.m_bmNormalFont.drawNormalFont(
				g,
				playerInfo.gender,
				m_left + (20 / HDCGameMidlet.scale)
						+ BitmapFont.m_bmNormalFont.stringWidth("Giới tính : "), tfGender.y
						+ (30 / HDCGameMidlet.scale), 0xffffff, Graphics.LEFT | Graphics.VCENTER);
	}

	// TODO paint Cấp độ
	@SuppressWarnings("static-access")
	private void paintCapDo(Graphics g) {
		BitmapFont.m_bmNormalFont.drawNormalFont(g, "Cấp độ : ", m_left
				+ (20 / HDCGameMidlet.scale), tfGender.y + (90 / HDCGameMidlet.scale), 0xffb901,
				Graphics.LEFT | Graphics.VCENTER);

		BitmapFont.m_bmNormalFont
				.drawNormalFont(g, playerInfo.level + "", m_left + (20 / HDCGameMidlet.scale)
						+ BitmapFont.m_bmNormalFont.stringWidth("Cấp độ : "), tfGender.y
						+ (90 / HDCGameMidlet.scale), 0x7CFC00, Graphics.LEFT | Graphics.VCENTER);
	}

	// TODO paint Gold
	@SuppressWarnings("static-access")
	private void paintGold(Graphics g) {
		BitmapFont.m_bmNormalFont.drawNormalFont(g, "Gold : ", GameCanvas.w / 2, tfGender.y
				+ (90 / HDCGameMidlet.scale), 0xffb901, Graphics.RIGHT | Graphics.VCENTER);

		BitmapFont.m_bmNormalFont
				.drawNormalFont(g, playerInfo.gold + "", GameCanvas.w / 2, tfGender.y
						+ (90 / HDCGameMidlet.scale), 0x7CFC00, Graphics.LEFT | Graphics.VCENTER);
	}

	// TODO paint dina
	@SuppressWarnings("static-access")
	private void paintDina(Graphics g) {
		BitmapFont.m_bmNormalFont.drawNormalFont(g, "Dina : ", m_left - (20 / HDCGameMidlet.scale)
				- BitmapFont.m_bmNormalFont.stringWidth(playerInfo.dina + "")
				+ GameResource.instance.imgListScr_Panel.getWidth(), tfGender.y
				+ (90 / HDCGameMidlet.scale), 0xffb901, Graphics.RIGHT | Graphics.VCENTER);

		BitmapFont.m_bmNormalFont.drawNormalFont(g, playerInfo.dina + "", m_left
				- (20 / HDCGameMidlet.scale) + GameResource.instance.imgListScr_Panel.getWidth(),
				tfGender.y + (90 / HDCGameMidlet.scale), 0x7CFC00, Graphics.RIGHT
						| Graphics.VCENTER);
	}

	// TODO paint tab thông tin
	@SuppressWarnings("static-access")
	public void paintTabThongTin(Graphics g) {
		// TODO paint tên
		paintName(g);

		// TODO paint status
		paintStatus(g);

		// TODO paint giới tính
		paintGioiTinh(g);

		// TODO paint cấp độ
		paintCapDo(g);

		// TODO paint gold
		paintGold(g);

		// TODO paint dina
		paintDina(g);

		// TODO paint button
		if (m_bIsEdit) {
			mButton_LuuThongTin.paint(g);
			// TODO paint textfield
			tfName.paint(g);
			tfStatus.paint(g);
			tfGender.paint(g);
		}
	}

	// TODO paint thông tin mỗi thành tích
	private void paintInfo_ThanhTich(Graphics g, float x, float y, int i) {
		// TODO paint tên game
		BitmapFont.drawBoldFont(g, playerInfo.cardName[i], x + (float) (20 / HDCGameMidlet.scale),
				y, 0xffb901, Graphics.LEFT | Graphics.TOP);
		// TODO Thắng
		BitmapFont.drawNormalFont(g, "Thắng: " + playerInfo.score[i][0], x
				+ (20 / HDCGameMidlet.scale), y + (20 / HDCGameMidlet.scale), 0xBABABA,
				Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawNormalFont(g, playerInfo.score[i][0] + "", x + (20 / HDCGameMidlet.scale)
				+ BitmapFont.m_bmNormalFont.stringWidth("Thắng: "), y + (20 / HDCGameMidlet.scale),
				0xffffff, Graphics.LEFT | Graphics.TOP);

		// TODO thua
		BitmapFont.drawNormalFont(g, "Thua: ", x + (20 / HDCGameMidlet.scale), y
				+ (40 / HDCGameMidlet.scale), 0xBABABA, Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawNormalFont(g, playerInfo.score[i][1] + "", x + (20 / HDCGameMidlet.scale)
				+ BitmapFont.m_bmNormalFont.stringWidth("Thua: "), y + (40 / HDCGameMidlet.scale),
				0xffffff, Graphics.LEFT | Graphics.TOP);

		// TODO Hòa
		BitmapFont.drawNormalFont(g, "Hòa: ", x + (20 / HDCGameMidlet.scale), y
				+ (60 / HDCGameMidlet.scale), 0xBABABA, Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawNormalFont(g, playerInfo.score[i][2] + "", x + (20 / HDCGameMidlet.scale)
				+ BitmapFont.m_bmNormalFont.stringWidth("Hòa: "), y + (60 / HDCGameMidlet.scale),
				0xffffff, Graphics.LEFT | Graphics.TOP);

		// TODO Bỏ cuộc
		BitmapFont.drawNormalFont(g, "Bỏ Cuộc: " + playerInfo.score[i][3], x
				+ (20 / HDCGameMidlet.scale), y + (80 / HDCGameMidlet.scale), 0xBABABA,
				Graphics.LEFT | Graphics.TOP);
		BitmapFont.drawNormalFont(g, playerInfo.score[i][3] + "", x + (20 / HDCGameMidlet.scale)
				+ BitmapFont.m_bmNormalFont.stringWidth("Bỏ Cuộc: "), y
				+ (80 / HDCGameMidlet.scale), 0xffffff, Graphics.LEFT | Graphics.TOP);

	}

	public void paintTabThanhTich(Graphics g) {

		g.setClip(0, m_iTopDisplay, GameCanvas.w, GameCanvas.h - m_iTopDisplay);
		g.translate(0, cmy);

		int i;
		// int top = y;
		int top = tfName.y + (int) (10 / HDCGameMidlet.scale);

		for (i = 0; i < (int) playerInfo.cardName.length / 2; i++) {
			// TODO Cột 1
			paintInfo_ThanhTich(g, tfName.x, top, 2 * i);
			// TODO Cột 2
			paintInfo_ThanhTich(g, GameCanvas.w / 2, top, 2 * i + 1);
			top += (120 / HDCGameMidlet.scale);
		}
		if (playerInfo.cardName.length % 2 != 0) {
			paintInfo_ThanhTich(g, tfName.x, top, playerInfo.cardName.length - 1);
		}
	}

	public void updateScroll(int dir) {
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

	public int l = 20;
	public int limit = 0;

	public void initScroll() {
		limit = (GameCanvas.h - m_iTopDisplay) - (27 * l);
		// limit = (GameCanvas.h - tfName.y);
		cmtoY = cmy = 0;

		// GameCanvas.cameraList.setInfo(/* m_left */0, tfName.y + m_iTopScroll
		// + m_iHeightItem, GameCanvas.w, m_iHeightItem, GameCanvas.w,
		// playerInfo.cardName.length / 2 * m_iHeightItem, m_width,
		// m_height - m_iHeightItem - m_iTopScroll,
		// playerInfo.cardName.length);
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void switchToMe(PlayerInfo p, boolean isEdit) {
		GameCanvas.endDlg();

		playerInfo = p;

		m_bIsEdit = isEdit;
		m_cmdRight = m_cmdClose;
		if (m_bIsEdit) {
			m_cmdCenter = m_cmdSavePersonalInfo;
			m_iNumTabDisplay = 4;
			// m_cmdLeft = tfName.cmdClear;
			numberItemsGood = HDCGameMidlet.m_myPlayerInfo.m_listItems.size();
			// tfName.setFocused(true);
		} else {
			m_iNumTabDisplay = 2;
			m_cmdLeft = null;
			m_cmdCenter = null;
			// m_cmdRight = m_cmdClose;
			// tfName.setFocused(false);
		}

		tfStatus.setText(playerInfo.status);
		tfStatus.isReadonly = true;
		tfStatus.m_bShowFullText = true;

		tfName.setText(playerInfo.fullName);
		tfName.isReadonly = false;
		tfName.m_bShowFullText = true;
		tfName.textColor = playerInfo.getColor();

		tfGender.setText(GameResource.getValue(playerInfo.gender));
		tfGender.isReadonly = true;

		playerInfo.getHonors();

		focusedTabIndex = 6;
		m_iHeightItemGood = 55;
		backScr = GameCanvas.currentScreen;
		m_iFocusTab = 0;
		// setGridItem();
		// setListItem();

		// TODO set title
		m_strTitle = GameResource.instance.owner;

		super.switchToMe();

		// TODO setlocation
		setLocation();

		// TODO set tab neu co 2 tab
		if (!m_bIsEdit) {
			m_strTab = new Vector();
			m_strTab.addElement(GameResource.info);
			m_strTab.addElement("Thành tích");
		} else {
			m_strTab = new Vector();
			m_strTab.addElement(GameResource.info);
			m_strTab.addElement("Thành tích");
			m_strTab.addElement("Avatar");
			m_strTab.addElement("Vật phẩm");
		}

		// TODO select tab
		selectTab();

		// TODO set text field
		setTextField();

		// TODO init scroll
		initScroll();

		// TODO init button
		initButton();

		m_iTopDisplay = tfName.y;

		// TODO cột trong tab avatar
		m_widthItem = GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3;
		m_heightItem = GameResource.instance.imgAvatar_Khung.getHeight() / 2 * 3;
		m_colAvt = GameResource.instance.imgListScr_Panel.getWidth() / m_widthItem;
		if (m_bIsEdit) {
			// TODO init gridview for Avatar
			initGridView_Avatar();
			// TODO init gridview for VatPham
			initGridView_VatPham();
		}

		// TODO set status
		playerInfo.status = BitmapFont.opt_String(playerInfo.status,
				GameResource.instance.MAX_LENGTH);
	}

	// TODO init gridview for avatar
	private void initGridView_Avatar() {
		if (m_gridView_Avatar == null && HDCGameMidlet.m_myPlayerInfo.m_listAvatars.size() > 0) {
			m_gridView_Avatar = new GridView();
			int x = m_left;
			int y = tfName.y;
			FrameImage m_frame = GameResource.instance.m_frameAvatar_IconAvatar;

			m_gridView_Avatar.setInfo(x, y, GameResource.instance.imgListScr_Panel.getWidth(),
					GameCanvas.h - y, m_widthItem, m_heightItem,
					HDCGameMidlet.m_myPlayerInfo.m_listAvatars, m_frame);
		}
	}

	// TODO init gridview for VatPham
	private void initGridView_VatPham() {
		if (m_gridView_VatPham == null && HDCGameMidlet.m_myPlayerInfo.m_listItems.size() > 0) {
			m_gridView_VatPham = new GridView();
			int x = m_left;
			int y = tfName.y;
			FrameImage m_frame = GameResource.instance.m_frameVatPham_Icon;
			m_gridView_VatPham.setPaintInfo(true);
			m_gridView_VatPham.setTypeInfo(0);
			m_gridView_VatPham.setWidthHeigh_Info((int) (300 / HDCGameMidlet.scale),
					(int) (100 / HDCGameMidlet.scale));

			m_gridView_VatPham.setInfo(x, y, GameResource.instance.imgListScr_Panel.getWidth(),
					GameCanvas.h - y, m_widthItem, m_heightItem,
					HDCGameMidlet.m_myPlayerInfo.m_listItems, m_frame);

		}
	}

	// TODO paint header tab
	private void paintHeaderTabs(Graphics g, float x, float y) {
		if (imgTab != null) {
			g.drawImage(imgTab, x, y, Graphics.HCENTER | Graphics.TOP);
			int w = (int) (imgTab.getWidth() / (m_strTab.size() * 2));
			if (m_strTab.size() == 2) {
				for (int i = 0; i < m_strTab.size(); i++) {
					if (i == m_iFocusTab) {
						BitmapFont.drawBoldFont(g, (String) m_strTab.get(i), m_left + (2 * i + 1)
								* w, y + imgTab.getHeight() / 2, 0xffb901, Graphics.HCENTER
								| Graphics.VCENTER);
					} else {
						BitmapFont.drawNormalFont(g, (String) m_strTab.get(i), m_left + (2 * i + 1)
								* w, y + imgTab.getHeight() / 2, 0xffffff, Graphics.HCENTER
								| Graphics.VCENTER);
					}
				}
			}
		}

	}

	public void update() {
		// backScr.update();

		switch (m_iFocusTab) {
		case 0:
			if (!tfName.getText().equals("") && !tfName.getText().equals(playerInfo.fullName)) {
				playerInfo.fullName = tfName.getText();
			}
			if (!tfStatus.getText().equals("") && !tfStatus.getText().equals(playerInfo.status)) {
				playerInfo.status = BitmapFont.opt_String(tfStatus.getText(),
						GameResource.MAX_LENGTH);
			}
			// if (!tfGender.getText().equals("") &&
			// !tfGender.getText().equals(playerInfo.gender)) {
			// playerInfo.gender = tfGender.getText();
			// }
			if (HDCGameMidlet.instance.flag_Gender == 1)
				playerInfo.gender = GameResource.getValue(HDCGameMidlet.instance.idx_Gender);
			break;
		case 1:
			break;
		case 2:
			if (m_gridView_Avatar != null && HDCGameMidlet.m_myPlayerInfo.m_listAvatars.size() > 0)
				m_gridView_Avatar.update();
			break;
		case 3:
			if (m_gridView_VatPham != null && HDCGameMidlet.m_myPlayerInfo.m_listItems.size() > 0)
				m_gridView_VatPham.update();
			break;
		default:
			break;
		}

	}

	public void updateKey() {
		boolean isChangeTab = false;

		// TODO update key for button
		if (m_bIsEdit) {
			if (m_iFocusTab == 0) {
				mButton_LuuThongTin.updateKey();
				tfStatus.update();
				tfName.update();
				tfGender.update();
			} else if (m_iFocusTab == 2) {
				if (m_gridView_Avatar != null
						&& HDCGameMidlet.m_myPlayerInfo.m_listAvatars.size() > 0) {
					mButton_SuDungAvatar.updateKey();
					m_gridView_Avatar.updateKey();
				}
			} else if (m_iFocusTab == 3) {
				if (m_gridView_VatPham != null
						&& HDCGameMidlet.m_myPlayerInfo.m_listItems.size() > 0) {
					mButton_SuDungVatPham.updateKey();
					m_gridView_VatPham.updateKey();
				}
			}
		}

		if (GameCanvas.isPointerClick && m_strTab != null) {

			// draw tab
			int i;
			int x = m_left;
			int numTab = m_strTab.size();
			int width = imgTab.getWidth() / numTab;
			for (i = 0; i < numTab; i++) {
				if (i != m_iFocusTab) {
					if (GameCanvas.isPointer(x + 1, GameResource.instance.imgHeaderBg.getHeight(),
							width - 2, imgTab.getHeight())) {
						m_iFocusTab = i;
						selectTab();

						HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.box_plan);
						HDCGameMidlet.sound.play();
						break;
					}
				}
				x += width;
			}
		}

		if (GameCanvas.isPointerDown && m_iFocusTab == 1 || m_iFocusTab == 2) {
			if (GameCanvas.pyLast > GameCanvas.py) {
				updateScroll(-10);
			} else {
				updateScroll(10);
			}
		}

		if (isChangeTab) {
			if (tab == 2) {
				setGridItem();
			} else if (tab == 3) {
				setListItem();
			} else {
				GameCanvas.cameraList.close();
			}
		}

		super.updateKey();
	}

	public void setSelected(int se) {
		if (se != m_selected) {
			super.setSelected(se);
		}
	}

	public void setMyInfo() {
		HDCGameMidlet.m_myPlayerInfo.fullName = info[0];
		HDCGameMidlet.m_myPlayerInfo.gender = info[1];
		HDCGameMidlet.m_myPlayerInfo.status = info[2];

	}

	public void updateListUserItems(int itemId, int numberExpiredUses) {
		ItemInfo item = HDCGameMidlet.m_myPlayerInfo.getItemById(itemId);
		if (item != null) {
			if (numberExpiredUses > 0) {
				item.m_iNumberExpiredUses = numberExpiredUses;
			} else {
				HDCGameMidlet.m_myPlayerInfo.m_listItems.removeElementAt(m_selected);
				numberItemsGood = HDCGameMidlet.m_myPlayerInfo.m_listItems.size();
				item = null;
			}
		}
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		GameCanvas.instance.menu.m_list = null;
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		if (GameCanvas.m_arrEffect != null)
			GameCanvas.m_arrEffect.removeAllElements();
		close();
	}
}
