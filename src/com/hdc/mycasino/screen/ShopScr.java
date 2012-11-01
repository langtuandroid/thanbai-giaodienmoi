package com.hdc.mycasino.screen;

import java.util.Vector;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.danh.view.GridView;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Button;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.ItemInfo;
import com.hdc.mycasino.model.MyObj;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;
import com.hdc.mycasino.utilities.TField;

public class ShopScr extends Screen {

	private static ShopScr instance;

	public static ShopScr gI() {
		if (instance == null) {
			instance = new ShopScr();
		}
		return instance;
	}

	int i, x, y;
	int m_col = 4;// so luong ban choi trong 1 hang
	public int m_wCell;
	int m_iGameTick;
	public int m_selectItem;
	boolean m_bIsShowPopupInfo = true;
	@SuppressWarnings("rawtypes")
	public Vector m_arrItems = new Vector();
	protected Screen lastScr;
	// /////////////reg clan
	public TField tfClanName;
	public TField tfClanStatus;
	public byte m_bIstype = 0;
	// //////////////command
	Command m_close;

	// TODO button
	Button mButton_Mua;
	Button mButton_Dongy;

	// TODO columne row in tab avatar
	private int col;
	private int row;
	private int col_select;
	private int row_select;
	private int m_widthItem;
	private int m_heightItem;
	private int m_colAvt;

	public int m_iTopDisplay = 80;

	// TODO toa do Y cho ve GridItem
	private float top;

	// TODO name & status hiển thị
	// textfield trong màn hình "Đăng ký gia tộc"
	private String name = "";
	private String status = "";
	// TODO init gridview
	private GridView m_gridview;

	public void setItems() {
		if (m_bIstype == 1) {// 0 binh thuong - 1 dang ky - 2 shop
			m_selected = -2;
			// m_cmdRight = txtClanName.cmdClear;
			m_cmdRight = m_close;
			m_cmdLeft = null;
		}

		// initCam();
	}

	public void switchToMe(Screen scr, Command center) {
		super.switchToMe();
		m_selected = 0;
		m_selectItem = 0;

		lastScr = null;
		if (scr != null)
			lastScr = scr;

		m_cmdCenter = center;
		m_cmdLeft = null;
		m_cmdRight = m_close;
		System.gc();

		// TODO set command for button Dong Y
		if (mButton_Dongy != null)
			mButton_Dongy.setCommand(m_cmdCenter);
	}

	// TODO set location for list item
	private void setLocation() {
		m_left = (GameCanvas.w - GameResource.instance.imgListScr_Panel.getWidth()) / 2;
		m_top = (int) (60 / HDCGameMidlet.instance.scale);

	}

	// TODO set location TOP for griditem
	private void setLocation_GridItem() {
		switch (m_bIstype) {
		case 1:
			top = tfClanStatus.y + (120 / HDCGameMidlet.instance.scale)
					+ GameResource.instance.imgAvatar_Khung.getHeight() / 4;
			break;
		case 2:
		case 3:
			top = m_top + GameResource.instance.imgAvatar_Khung.getHeight() + (16 / HDCGameMidlet.instance.scale)
					+ GameResource.instance.imgAvatar_Khung.getHeight() / 4;
			break;
		default:
			break;
		}
	}


	public ShopScr() {
		m_bIsShowPopupInfo = false;
		loadImg();
		m_close = new Command(GameResource.close, new IAction() {
			public void perform() {
				close();
			}
		});

		// define the textfield to clan's name and clan's status
		tfClanName = new TField();
		tfClanName.isFocus = false;
		tfClanName.isPaintBG = false;
		tfClanName.setMaxTextLenght(15);
		tfClanName.setIputType(TField.INPUT_ALPHA_NUMBER_ONLY);
		tfClanName.focusTextColor = 0x969696;
		tfClanName.textColor = 0x969696;

		tfClanStatus = new TField();
		tfClanStatus.isFocus = false;
		tfClanStatus.isPaintBG = false;
		tfClanStatus.m_bShowFullText = true;
		tfClanStatus.setMaxTextLenght(50);
		tfClanStatus.setIputType(TField.INPUT_TYPE_ANY);
		tfClanStatus.focusTextColor = 0xffffff;
		tfClanStatus.textColor = 0xffffff;

	}

	// set items in shop
	@SuppressWarnings({ "rawtypes", "static-access" })
	public void setListItems(Vector listItemInShop, byte isType, int left, int top, int width, int height,
			String title, int widthCell, int widthEffect) {
		m_left = left;
		m_top = top;
		m_width = width;
		m_height = height;

		m_bIstype = isType;
		m_wCell = widthCell;
		// m_col = m_width / m_wCell;

		m_iGameTick = 0;
		m_strTitle = title;

		m_arrItems = new Vector();
		m_arrItems = listItemInShop;
		// reset the focus
		// initCam();
		deltaWidth = widthEffect;
		GameCanvas.instance.msgdlg.isWaiting = false;
		// TODO set location
		setLocation();

		// TODO cột trong tab avatar
		m_widthItem = GameResource.instance.imgAvatar_Khung.getWidth() / 2 * 3;
		m_heightItem = GameResource.instance.imgAvatar_Khung.getHeight() / 2 * 3;
		m_colAvt = GameResource.instance.imgListScr_Panel.getWidth() / m_widthItem;

		// TODO set text field
		setTextField();

		// TODO init button
		initButton();

		// TODO set location for gridItem
		setLocation_GridItem();

		// TODO init Grid View
//		if (m_gridview == null) {
			m_gridview = new GridView();
			int x = m_left;
			int y = m_top + GameResource.instance.imgAvatar_Khung.getHeight()
					+ (int) (16 / HDCGameMidlet.instance.scale);
			int width_Grid = GameResource.instance.imgListScr_Panel.getWidth();
			FrameImage m_frame = null;
			switch (m_bIstype) {
			case 0:
				break;
			case 1:
				y = tfClanStatus.y + (int) (120 / HDCGameMidlet.instance.scale);
				m_frame = GameResource.instance.m_frameGiaToc_ThanhLap;
				break;
			case 2:
				m_frame = GameResource.instance.m_frameAvatar_IconAvatar;
				m_gridview.setPaintInfo(true);
				m_gridview.setTypeInfo(2);
				m_gridview.setWidthHeigh_Info((int) (170 / HDCGameMidlet.instance.scale),
						(int) (100 / HDCGameMidlet.instance.scale));
				break;
			case 3:
				m_frame = GameResource.instance.m_frameVatPham_Icon;
				m_gridview.setPaintInfo(true);
				m_gridview.setTypeInfo(3);
				m_gridview.setWidthHeigh_Info((int) (320 / HDCGameMidlet.instance.scale),
						(int) (220 / HDCGameMidlet.instance.scale));
				break;
			default:
				x = 0;
				y = GameCanvas.h / 8;
				m_frame = GameResource.instance.m_frameRoom_IconTable;
				m_widthItem = m_frame.frameWidth / 2 * 3;
				m_heightItem = m_frame.frameHeight / 2 * 3;
				m_colAvt = GameCanvas.w / m_widthItem;
				width_Grid = GameCanvas.w;
				if (m_cmdCenter != null)
					m_gridview.setCommand(m_cmdCenter);
				break;
			}
			m_gridview.setInfo(x, y, width_Grid, GameCanvas.h - y, m_widthItem, m_heightItem, m_arrItems, m_frame);
//		}
	}


	public MyObj getSelectItems() {
		if (m_gridview.getIndex() < m_arrItems.size()) {
			return (MyObj) m_arrItems.elementAt(m_gridview.getIndex());
		} else
			return null;
	}

	// TODO init button
	@SuppressWarnings("static-access")
	private void initButton() {
		switch (m_bIstype) {
		case 1:
			// TODO button đồng ý đăng ký gia tộc
			mButton_Dongy = new Button(GameResource.instance.imgButton_Login, GameResource.instance.yes,
					this.m_cmdCenter);
			mButton_Dongy.setXY((GameResource.instance.imgListScr_Panel.getWidth() - mButton_Dongy.w) + m_left
					- (int) (20 / HDCGameMidlet.instance.scale),
					(tfClanStatus.y + (int) (70 / HDCGameMidlet.instance.scale)));
			// mButton_Mua.startEffect();
			break;
		case 2:
		case 3:
			// TODO button mua avatar
			mButton_Mua = new Button(GameResource.instance.imgButton_Login, GameResource.instance.buy, this.m_cmdCenter);
			mButton_Mua.setXY((GameResource.instance.imgListScr_Panel.getWidth() - mButton_Mua.w) + m_left
					- (int) (20 / HDCGameMidlet.instance.scale),
					m_top + GameResource.instance.imgAvatar_Khung.getHeight() / 2);
			// mButton_Mua.startEffect();
			break;
		default:
			break;
		}
	}

	// TODO paint dina
	private void paintDina(Graphics g) {
		GameResource.instance.m_frameHeaderIcon_DinaGold.drawFrame(0, m_left + (40 / HDCGameMidlet.instance.scale),
				m_top + (60 / HDCGameMidlet.instance.scale), Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);
		BitmapFont.drawMoney(g, " " + HDCGameMidlet.m_myPlayerInfo.dina, m_left
				+ (int) (40 / HDCGameMidlet.instance.scale)
				+ GameResource.instance.m_frameHeaderIcon_DinaGold.frameWidth, m_top
				+ (int) (60 / HDCGameMidlet.instance.scale), Graphics.LEFT | Graphics.VCENTER, 0x00ff00);

	}

	// TODO paint gold
	private void paintGold(Graphics g) {
		GameResource.instance.m_frameHeaderIcon_DinaGold.drawFrame(
				1,
				m_left + (40 / HDCGameMidlet.instance.scale)
						+ GameResource.instance.m_frameHeaderIcon_DinaGold.frameWidth * 2
						+ BitmapFont.m_bmNormalFont.stringWidth(" " + HDCGameMidlet.m_myPlayerInfo.dina), m_top
						+ (60 / HDCGameMidlet.instance.scale), Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);
		BitmapFont.drawMoney(
				g,
				" " + HDCGameMidlet.m_myPlayerInfo.gold,
				m_left + (int) (40 / HDCGameMidlet.instance.scale)
						+ GameResource.instance.m_frameHeaderIcon_DinaGold.frameWidth * 3
						+ BitmapFont.m_bmNormalFont.stringWidth(" " + HDCGameMidlet.m_myPlayerInfo.dina), m_top
						+ (int) (60 / HDCGameMidlet.instance.scale), Graphics.LEFT | Graphics.VCENTER, 0xffff00);

	}

	// TODO paint avatar
	private void paintItem(Graphics g, float x, float y, int avatarID, int select, FrameImage m_frame) {
		// TODO paint khung avatar
		if (select == 0)
			g.drawImage(GameResource.instance.imgAvatar_Khung, x, y, Graphics.LEFT | Graphics.VCENTER);
		else
			g.drawImage(GameResource.instance.imgAvatar_Khung_High_Light,
					x + GameResource.instance.imgAvatar_Khung.getWidth() / 2, y, Graphics.HCENTER | Graphics.VCENTER);

		// TODO paint icon
		m_frame.drawFrame(avatarID, x + GameResource.instance.imgAvatar_Khung.getWidth() / 2, y, Sprite.TRANS_NONE,
				Graphics.HCENTER | Graphics.VCENTER, g);
	}

	// TODO paint Line
	private void paintLine(Graphics g, float x, float y) {
		g.setColor(Color.GRAY);
		g.setAlpha(120);
		g.fillRectWithTransparent(x, y, GameResource.instance.imgListScr_Panel.getWidth(), 2);
	}

	// TODO set text field
	private void setTextField() {
		// TODO textfield name
		tfClanName.MAX_LENGTH_TEXT = 30;
		tfClanName.x = m_left;
		tfClanName.y = m_top + (int) (10 / HDCGameMidlet.instance.scale);
		tfClanName.width = GameResource.instance.imgListScr_Panel.getWidth();
		tfClanName.height = (int) (60 / HDCGameMidlet.instance.scale);
		tfClanName.isFocus = true;

		// TODO textfield status
		tfClanStatus.MAX_LENGTH_TEXT = 40;
		tfClanStatus.x = m_left;
		tfClanStatus.y = tfClanName.y + (int) (60 / HDCGameMidlet.instance.scale);
		tfClanStatus.width = GameResource.instance.imgListScr_Panel.getWidth();
		tfClanStatus.height = (int) (60 / HDCGameMidlet.instance.scale);
		tfClanStatus.isFocus = false;
	}

	@SuppressWarnings("static-access")
	public void paint(Graphics g) {
		// if (lastScr != null)
		// lastScr.paint(g);

		switch (m_bIstype) {
		case 0:
			g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
			PaintPopup.paintBackground(g, m_strTitle);
			// paintList(g);
			break;
		case 1:// dăng ky
				// TODO paint background
			PaintPopup.paintBackground(g, m_strTitle);

			// TODO paint panel
			g.drawImage(GameResource.instance.imgListScr_Panel, m_left, m_top, Graphics.LEFT | Graphics.TOP);

			// TODO paint name gia toc
			BitmapFont.m_bmNormalFont.drawNormalFont(g, "Tên gia tộc : ", m_left + (20 / HDCGameMidlet.instance.scale),
					tfClanName.y + (30 / HDCGameMidlet.instance.scale), 0xffb901, Graphics.LEFT | Graphics.VCENTER);
			BitmapFont.m_bmNormalFont.drawNormalFont(g, name, m_left + (20 / HDCGameMidlet.instance.scale)
					+ BitmapFont.m_bmNormalFont.stringWidth("Tên gia tộc : "), tfClanName.y
					+ (30 / HDCGameMidlet.instance.scale), 0xCCCCCC, Graphics.LEFT | Graphics.VCENTER);

			// TODO paint status
			BitmapFont.m_bmNormalFont.drawNormalFont(g, "Status : ", m_left + (20 / HDCGameMidlet.instance.scale),
					tfClanStatus.y + (30 / HDCGameMidlet.instance.scale), 0xffb901, Graphics.LEFT | Graphics.VCENTER);
			BitmapFont.m_bmNormalFont.drawNormalFont(g, status, m_left + (20 / HDCGameMidlet.instance.scale)
					+ BitmapFont.m_bmNormalFont.stringWidth("tfClanStatus : "), tfClanStatus.y
					+ (30 / HDCGameMidlet.instance.scale), 0xCCCCCC, Graphics.LEFT | Graphics.VCENTER);

			// TODO paint text "Chọn hình đại diện"
			BitmapFont.m_bmNormalFont.drawNormalFont(g, GameResource.instance.selectImage, m_left
					+ (20 / HDCGameMidlet.instance.scale), tfClanStatus.y + (90 / HDCGameMidlet.instance.scale),
					0xffb901, Graphics.LEFT | Graphics.VCENTER);

			// TODO paint line
			paintLine(g, m_left, tfClanStatus.y + (120 / HDCGameMidlet.instance.scale));

			// TODO paint list
			// paintList(g, m_left, top);
			m_gridview.paint(g);

			// TODO paint button
			mButton_Dongy.paint(g);

			// TODO paint text field
			tfClanName.paint(g);
			tfClanStatus.paint(g);
			break;
		case 2:// shop avatar
		case 3:// shop vat pham
				// TODO paint dina
			paintDina(g);
			// TODO paint gold
			paintGold(g);
			// TODO paint line
			paintLine(g, m_left, m_top + GameResource.instance.imgAvatar_Khung.getHeight()
					+ (16 / HDCGameMidlet.instance.scale));
			// TODO paint button mua avatar
			mButton_Mua.paint(g);

			// TODO paint list
			// paintList(g, m_left, top);
			if (m_gridview != null)
				m_gridview.paint(g);
			break;
		default:
			// paintList(g);
			g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
			PaintPopup.paintBackground(g, m_strTitle);
			if (m_gridview != null)
				m_gridview.paint(g);
			break;
		}
		super.paint(g);
	}

	// TODO paint info in tab vật phẩm
	private void paintInfo(Graphics g, float x, float y, int width, int height, ItemInfo itemInfo) {
		// g.setColor(0xff0000);
		if (x < GameCanvas.w / 2)
			x += m_widthItem / 2;
		else
			x -= (width + m_widthItem / 2);

		if (y < GameCanvas.w / 4 * 3)
			y -= (height / 2);
		else
			y -= height;

		g.fillRoundRectWithGadient((int) x, (int) y, width, height, (int) (8 / HDCGameMidlet.instance.scale),
				(int) (8 / HDCGameMidlet.instance.scale));

		// paint description
		BitmapFont.setTextSize(16f);

		switch (m_bIstype) {
		case 2:
			paintInfo_Avatar(g, x, y, itemInfo);
			break;
		case 3:
			paintInfo_Item(g, x, y, itemInfo);
			break;
		default:
			break;
		}
	}

	// TODO paint info avatar
	@SuppressWarnings("static-access")
	private void paintInfo_Avatar(Graphics g, float x, float y, ItemInfo itemInfo) {
		y += (20 / HDCGameMidlet.instance.scale);
		// paint item name
		BitmapFont.m_bmNormalFont.drawBoldFont_1(g, (String) itemInfo.itemName,
				x + (10 / HDCGameMidlet.instance.scale), y + (10 / HDCGameMidlet.instance.scale), 0xCCCCCC,
				Graphics.LEFT | Graphics.TOP);
		y += (30 / HDCGameMidlet.instance.scale);
		// paint gold
		BitmapFont.m_bmNormalFont.drawBoldFont_1(g, "Gold : " + itemInfo.m_gold, x
				+ (10 / HDCGameMidlet.instance.scale), y + (10 / HDCGameMidlet.instance.scale), 0xffb901, Graphics.LEFT
				| Graphics.TOP);
	}

	// TODO paint info vật phẩm
	@SuppressWarnings("static-access")
	private void paintInfo_Item(Graphics g, float x, float y, ItemInfo itemInfo) {
		// paint item name
		BitmapFont.m_bmNormalFont.drawBoldFont_1(g, (String) itemInfo.itemName,
				x + (10 / HDCGameMidlet.instance.scale), y + (10 / HDCGameMidlet.instance.scale), 0xffb901,
				Graphics.LEFT | Graphics.TOP);
		y += (30 / HDCGameMidlet.instance.scale);
		// paint mô tả item
		for (int i = 0; i < itemInfo.m_arrDes.size(); i++) {
			BitmapFont.m_bmNormalFont.drawBoldFont_1(g, (String) itemInfo.m_arrDes.elementAt(i), x
					+ (10 / HDCGameMidlet.instance.scale), y + (10 / HDCGameMidlet.instance.scale), 0xCCCCCC,
					Graphics.LEFT | Graphics.TOP);
			y += (30 / HDCGameMidlet.instance.scale);
		}

		// paint giá sản phẩm
		BitmapFont.m_bmNormalFont.drawItalicFont_1(g, "Giá : "
				+ (itemInfo.m_gold > 0 ? itemInfo.m_gold : itemInfo.m_dina), x + (10 / HDCGameMidlet.instance.scale), y
				+ (10 / HDCGameMidlet.instance.scale), 0xffb901, Graphics.LEFT | Graphics.TOP);
		y += (30 / HDCGameMidlet.instance.scale);

		// paint hạn sử dụng
		BitmapFont.m_bmNormalFont.drawItalicFont_1(g, "Thời gian sử dụng : " + itemInfo.m_numberUseDays, x
				+ (10 / HDCGameMidlet.instance.scale), y + (10 / HDCGameMidlet.instance.scale), 0xffb901, Graphics.LEFT
				| Graphics.TOP);
	}

	// TODO select item (avartar - vật phẩm)
	private void selectItem() {
		// int top = m_top + GameResource.instance.imgAvatar_Khung.getHeight()
		// + (16 / HDCGameMidlet.instance.scale);
		if (GameCanvas.isPointer(m_left, top, GameResource.instance.imgListScr_Panel.getWidth(), GameCanvas.h - top)) {
			col_select = (int) (GameCanvas.px - m_left) / m_widthItem;
			row_select = (int) (GameCanvas.py - top) / m_heightItem;

			m_selectItem = row_select * m_colAvt + col_select;

			// TODO sound
			HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.box_plan);
			HDCGameMidlet.sound.play();
		}
	}

	protected void paintList(Graphics g, float x, float y) {
		int select;

		for (i = 0; i < m_arrItems.size(); i++) {
			col = i % m_colAvt;
			row = i / m_colAvt;

			if (col == col_select && row == row_select)
				select = 1;
			else
				select = 0;

			switch (m_bIstype) {
			case 1:
				paintItem(g, x + GameResource.instance.imgAvatar_Khung.getWidth() / 4 + m_widthItem * col, top
						+ m_heightItem * row + GameResource.instance.imgAvatar_Khung.getHeight() / 2, i, select,
						GameResource.instance.m_frameGiaToc_ThanhLap);
				break;
			case 2:
				paintItem(g, x + GameResource.instance.imgAvatar_Khung.getWidth() / 4 + m_widthItem * col, top
						+ m_heightItem * row + GameResource.instance.imgAvatar_Khung.getHeight() / 2,
						((ItemInfo) m_arrItems.elementAt(i)).m_imgID, select,
						GameResource.instance.m_frameAvatar_IconAvatar);
				break;
			case 3:
				paintItem(g, x + GameResource.instance.imgAvatar_Khung.getWidth() / 4 + m_widthItem * col, top
						+ m_heightItem * row + GameResource.instance.imgAvatar_Khung.getHeight() / 2,
						((ItemInfo) m_arrItems.elementAt(i)).m_imgID, select, GameResource.instance.m_frameVatPham_Icon);
				break;
			default:
				break;
			}
		}

		switch (m_bIstype) {
		case 2:
			paintInfo(g, x + GameResource.instance.imgAvatar_Khung.getWidth() / 4 * 3 + m_widthItem * col_select, top
					+ m_heightItem * row_select + GameResource.instance.imgAvatar_Khung.getHeight() / 2,
					(int) (150 / HDCGameMidlet.instance.scale),
					GameResource.instance.imgAvatar_Khung_High_Light.getHeight(),
					((ItemInfo) m_arrItems.elementAt(row_select * m_colAvt + col_select)));
			break;
		case 3:
			paintInfo(
					g,
					x + GameResource.instance.imgAvatar_Khung.getWidth() / 4 * 3 + m_widthItem * col_select,
					top + m_heightItem * row_select + GameResource.instance.imgAvatar_Khung.getHeight() / 2,
					(int) (300 / HDCGameMidlet.instance.scale),
					(int) (((((ItemInfo) (m_arrItems.elementAt(row_select * m_colAvt + col_select))).m_arrDes.size() + 3) * 30) / HDCGameMidlet.instance.scale),
					((ItemInfo) m_arrItems.elementAt(row_select * m_colAvt + col_select)));
			break;
		default:
			break;
		}
	}

	public void updateKey_Back() {
		if (GameCanvas.isPointerClick) {
			if (GameCanvas.currentDialog != null)
				return;
			if (GameCanvas.isPointer(0, 0, GameResource.instance.imgMenuBg.getWidth() / 2 * 3,
					GameResource.instance.imgMenuBg.getHeight() / 2 * 3)
					&& Math.abs(GameCanvas.pyLast - GameCanvas.py) < 5) {
				HDCGameMidlet.instance.onBackPressed();
			}
		}
	}

	public void updateKey() {

		// updateKey_Back();

		switch (m_bIstype) {
		case 1:
			if (mButton_Dongy != null) {
				mButton_Dongy.updateKey();
				// selectItem();
				if (m_gridview != null)
					m_gridview.updateKey();
			}
			super.updateKey();
			break;
		case 2:
		case 3:
			if (mButton_Mua != null) {
				mButton_Mua.updateKey();
				// selectItem();
				if (m_gridview != null)
					m_gridview.updateKey();
			}
			break;
		default:
			if (m_gridview != null)
				m_gridview.updateKey();
			if (m_selected != m_gridview.getIndex())
				m_selected = m_gridview.getIndex();
			break;
		}

//		if (m_bIstype == 1) {
//			if (m_selected < 0) {
//				if (GameCanvas.keyPressed[2] || GameCanvas.keyPressed[4]) {
//					m_selected--;
//					if (m_selected < -2) {
//						m_selected = 0;
//					}
//					changeForcus();
//					GameCanvas.keyPressed[2] = false;
//					GameCanvas.keyPressed[4] = false;
//				}
//
//				if (GameCanvas.keyPressed[8] || GameCanvas.keyPressed[6]) {
//					m_selected++;
//					changeForcus();
//					GameCanvas.keyPressed[8] = false;
//					GameCanvas.keyPressed[6] = false;
//				}
//			}
//
//			if (GameCanvas.keyPressed[2] && m_selected < m_col) {
//				GameCanvas.keyPressed[2] = false;
//				m_selected = -1;
//				changeForcus();
//			}
//		}

		// super.updateKey();
		if (m_selected >= 0)
			m_selectItem = m_selected;
	}

//	private void changeForcus() {
//		switch (m_selected) {
//		case -2:
//			tfClanName.isFocus = true;
//			tfClanStatus.isFocus = false;
//			// m_cmdRight = m_close;
//			// m_cmdRight = txtClanName.cmdClear;
//			break;
//		case -1:
//			tfClanStatus.isFocus = true;
//			tfClanName.isFocus = false;
//			// m_cmdRight = m_close;
//			// m_cmdRight = txtClanStatus.cmdClear;
//			break;
//		default:
//			tfClanStatus.isFocus = false;
//			tfClanName.isFocus = false;
//			break;
//		}
//		m_cmdLeft = null;
//		m_cmdRight = m_close;
//	}

	public void update() {
		if (lastScr != null)
			lastScr.update();

		if (m_bIstype == 1) {
			if (m_gridview != null)
				m_gridview.update();

			if (!tfClanName.getText().equals("") && !tfClanName.getText().equals(name)) {
				name = tfClanName.getText();
			} else if (!tfClanStatus.getText().equals("") && !tfClanStatus.getText().equals(status)) {
				status = tfClanStatus.getText();
			}

			tfClanName.update();
			tfClanStatus.update();

		} else if (m_bIstype == 2) {
			HDCGameMidlet.m_myPlayerInfo.update();
			if (m_gridview != null)
				m_gridview.update();
		} else if (m_bIstype == 4) {
			if (m_gridview != null)
				m_gridview.update();
		} else {
			if (m_gridview != null)
				m_gridview.update();
		}

		if (!m_bIsShowPopupInfo) {
			m_iGameTick++;
			if (m_iGameTick > 10) {
				m_bIsShowPopupInfo = true;
				m_iGameTick = 0;
			}
		}
	}

	public void close() {
		// GameCanvas.cameraList.close();
//		m_gridview = null;
//		m_arrItems = null;
		if (m_bIstype == 1 && lastScr != null)
			lastScr.switchToMe();
		// System.gc();
	}

	private static int deltaX = 0;
	private static int deltaY = 0;
	int deltaWidth = 20;

	public void selectEffect(Graphics g, int x, int y) {
		if (deltaX <= 2) {
			g.drawRegion(GameResource.instance.imgIconCard, 93, 86, 11, 11, 0, x - deltaX - deltaWidth, y - deltaY
					- deltaWidth, 0);
			g.drawRegion(GameResource.instance.imgIconCard, 93, 86, 11, 11, Sprite.TRANS_ROT90,
					x + deltaX + deltaWidth, y - deltaY - deltaWidth, Graphics.RIGHT | Graphics.TOP);

			g.drawRegion(GameResource.instance.imgIconCard, 93, 86, 11, 11, Sprite.TRANS_MIRROR_ROT180, x - deltaX
					- deltaWidth, y + deltaY + deltaWidth, Graphics.LEFT | Graphics.BOTTOM);
			g.drawRegion(GameResource.instance.imgIconCard, 93, 86, 11, 11, Sprite.TRANS_MIRROR_ROT90, x + deltaX
					+ deltaWidth, y + deltaY + deltaWidth, Graphics.RIGHT | Graphics.BOTTOM);

			deltaX += 1;
			deltaY += 1;
		} else {
			deltaX = 0;
			deltaY = 0;
		}
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		if (m_cmdLeft == null)
			GameCanvas.instance.menu.m_list = null;
		else
			m_cmdLeft.action.perform();
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		if (GameCanvas.m_arrEffect != null)
			GameCanvas.m_arrEffect.removeAllElements();
		if (m_cmdRight != null)
			m_cmdRight.action.perform();
		// close();
	}
}
