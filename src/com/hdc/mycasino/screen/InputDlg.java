package com.hdc.mycasino.screen;

import java.util.Vector;

import android.graphics.Color;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Button;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.TField;

public class InputDlg extends Dialog {
	protected String[] info;
	public TField tfInput;
	public IAction okAction;
	public IAction backAction;

	private int m_iWDlg; // width dialog
	private int m_iHDlg; // height dialog
	private int m_iLeftDlg; // left of dialog
	private int m_iTopDlg; // top of dialog

	private boolean m_bTypeDialog = true;
	private long m_iRank = 0;
	private long m_iMaxRank = 0;
	private long m_iMinRank = 0;
	private long m_iDelRank = 0;

	// TODO image background for input dialog
	private Image imgBackground;
	private int col; // cột background
	private int row; // dòng background
	// title
	private String m_title;
	// TODO button
	Button mButton_Agree;
	Button mButton_Disagree;

	public InputDlg() {
		tfInput = new TField();

		tfInput.isFocus = true;
		// right = tfInput.cmdClear;
	}

	/*
	 * init for the title of dialog calculate the width of dialog and split
	 * string to fit arrange the text field
	 */
	public void setUpDlg(String info) {
		m_iWDlg = GameCanvas.w - 80;

		String[] subInfo = BitmapFont.splitString(info, "\n");
		Vector vtTmp = new Vector();
		int i, j;
		for (i = 0; i < subInfo.length; i++) {
			String[] tmp = BitmapFont.m_bmFont.splitFontBStrInLine(subInfo[i], m_iWDlg - 20);
			for (j = 0; j < tmp.length; j++)
				vtTmp.addElement(tmp[j]);
			tmp = null;
		}

		this.info = new String[vtTmp.size()];
		j = vtTmp.size();
		for (i = 0; i < j; i++)
			this.info[i] = (String) vtTmp.elementAt(i);

		subInfo = null;
		CRes.cleanVector(vtTmp);

		m_iHDlg = this.info.length * 15 + 40;

		if (m_iHDlg < 55)
			m_iHDlg = 55;

		// m_iLeftDlg = GameCanvas.hw - m_iWDlg / 2;
		// m_iTopDlg = GameCanvas.h - GameCanvas.hBottomBar - m_iHDlg - 10;

		// tfInput.x = m_iLeftDlg + 10;
		// tfInput.y = m_iTopDlg + this.info.length * 15 + 10;
		// tfInput.width = m_iWDlg - 20;
		// tfInput.height = Screen.ITEM_HEIGHT + 2;
	}

	@SuppressWarnings("static-access")
	public void setInfo(String info, IAction ok, int type) {
		tfInput.setText("");
		tfInput.setIputType(type);

		setUpDlg(info);
		// this.info = BitmapFont.m_bmNormalFont.splitFontBStrInLine(info,
		// m_iWDlg - 20);
		this.m_title = info;

		this.okAction = ok;
		this.backAction = new IAction() {
			public void perform() {
				GameCanvas.endDlg();
			}
		};

		left = new Command(GameResource.close, backAction);
		center = new Command(GameResource.accept, okAction);

		m_bTypeDialog = true;
		tfInput.isPaintBG = true;
		tfInput.focusTextColor = 0x000000;

		// TODO create image background
		col = GameResource.instance.imgTextField_Disable.getWidth()
				/ GameResource.instance.imgPoupPanel.getHeight() + 6;
		row = GameResource.instance.imgTextField_Disable.getHeight() * 4
				/ GameResource.instance.imgPoupPanel.getHeight() + 1;
		imgBackground = PaintPopup.gI().createImgBackground_Popup(col, row);
		Graphics g1 = imgBackground.getGraphics();
		for (int i = 0; i < col; i++) {
			g1.drawImage(GameResource.instance.imgPopupLine,
					i * GameResource.instance.imgPopupLine.getWidth(),
					GameResource.instance.imgPoupPanel.getHeight() / 2 * 3, Graphics.LEFT
							| Graphics.TOP);
		}

		// TODO paint title
		BitmapFont.m_bmNormalFont.drawNormalFont(g1, m_title, imgBackground.getWidth() / 2,
				(10 / HDCGameMidlet.instance.scale), 0xffffff, Graphics.HCENTER | Graphics.TOP);

		// TODO set location
		// left - top
		setLocation();

		// TODO set textfield
		setTextField();

		// TODO create button
		createButton();
	}

	// TODO set textfield
	private void setTextField() {
		tfInput.x = m_iLeftDlg
				+ (imgBackground.getWidth() - GameResource.instance.imgTextField_Disable.getWidth())
				/ 2;
		tfInput.y = m_iTopDlg + GameResource.instance.imgPoupPanel.getHeight() * 4;
		tfInput.width = GameResource.instance.imgTextField_Disable.getWidth();
		tfInput.height = GameResource.instance.imgTextField_Disable.getHeight();
	}

	// TODO set Left & Top for dialog
	private void setLocation() {
		m_iLeftDlg = (GameCanvas.w - imgBackground.getWidth()) / 2;
		m_iTopDlg = (GameCanvas.h - imgBackground.getHeight()) / 2;
	}

	// TODO create button
	// yes - no
	private void createButton() {

		// int m_height = GameResource.instance.imgTextField_Disable.getHeight()
		// * 3;
		int m_height = imgBackground.getHeight() - GameResource.instance.imgPoupButton.getHeight()
				/ 4 * 5;

		mButton_Agree = new Button(GameResource.instance.imgPoupButton, GameResource.accept,
				this.center);

		mButton_Disagree = new Button(GameResource.instance.imgPoupButton, GameResource.no,
				this.left);

		// // chọn chiều dài chữ dài nhất làm chuẩn cho 2 button
		// if (caption_1.length() > caption_2.length()) {
		mButton_Disagree.Calculate_Col(GameResource.accept);
		// } else if (caption_1.length() < caption_2.length()) {
		// mButton_Yes.Calculate_Col(caption_2);
		// }

		int w1 = mButton_Agree.w;
		int w2 = imgBackground.getWidth() / 2;

		// set vị trí button yes
		mButton_Agree.setXY(m_iLeftDlg + (w2 - w1) / 2, m_iTopDlg + m_height);

		// set vị trí button no
		mButton_Disagree.setXY(m_iLeftDlg + w2 + (w2 - w1) / 2, m_iTopDlg + m_height);

		mButton_Agree.startEffect();
	}

	public void setInfo(String info, IAction ok, int type, long MaxRank, long defaultRank) {
		tfInput.setText("");
		tfInput.setIputType(type);

		setUpDlg(info);
		this.info = BitmapFont.m_bmNormalFont.splitFontBStrInLine(info, m_iWDlg - 20);

		this.okAction = ok;
		this.backAction = new IAction() {
			public void perform() {
				GameCanvas.endDlg();
			}
		};

		left = new Command(GameResource.close, backAction);
		center = new Command(GameResource.OK, okAction);

		m_bTypeDialog = false;

		tfInput.x = GameCanvas.hw - BitmapFont.m_bmFont.stringWidth("" + MaxRank) / 2;
		tfInput.width = BitmapFont.m_bmFont.stringWidth("" + MaxRank) + 20;
		tfInput.isPaintBG = false;
		tfInput.focusTextColor = 0xffff00;
		tfInput.y = m_iTopDlg + this.info.length * 15 + 7;
		tfInput.setText("" + defaultRank);
		m_iMaxRank = MaxRank;
		m_iMinRank = 1;
		m_iRank = defaultRank;
		m_iDelRank = defaultRank;
		if (MaxRank < defaultRank)
			m_iRank = MaxRank;
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.setAlpha(120);
		g.fillRectWithTransparent(0, 0, GameCanvas.w, GameCanvas.h);
		// TODO paint background
		g.drawImage(imgBackground, m_iLeftDlg, m_iTopDlg, Graphics.TOP | Graphics.LEFT);

		// TODO paint button
		if (mButton_Agree != null)
			mButton_Agree.paint(g);
		if (mButton_Disagree != null)
			mButton_Disagree.paint(g);

		// TODO paint text field
		tfInput.paint(g);

		// PaintPopup.paintBox(g, m_iLeftDlg, m_iTopDlg, m_iWDlg, m_iHDlg, 2,
		// 0x007639, 0x113e18);
		// int i, y;
		// int yStart = m_iTopDlg;
		//
		// if (!m_bTypeDialog) {
		// for (i = 0, y = yStart; i < info.length; i++, y +=
		// BitmapFont.m_bmNormalFont.getHeight()) {
		// BitmapFont.drawBoldFont(g, info[i], GameCanvas.hw, y + 15, 0xffffff,
		// Graphics.HCENTER
		// | Graphics.VCENTER);
		// }
		//
		// PaintPopup.paintBar(g, m_iMinRank, m_iMaxRank, m_iRank, m_iLeftDlg +
		// 5, tfInput.y + 20, m_iWDlg - 10);
		// } else {
		// for (i = 0, y = yStart; i < info.length; i++, y +=
		// BitmapFont.m_bmNormalFont.getHeight()) {
		// BitmapFont.drawNormalFont(g, info[i], GameCanvas.hw, y + 15,
		// 0xffff00, Graphics.HCENTER
		// | Graphics.VCENTER);
		// }
		// }
		// tfInput.paint(g);

		// super.paint(g);
	}

	public void keyPress(int keyCode) {
		// tfInput.keyPressed(keyCode);

		if (!m_bTypeDialog) {
			try {
				int tmp = Integer.parseInt(tfInput.getText());
				if (tmp != m_iRank)
					m_iRank = tmp;
			} catch (Exception e) {
			}
		}
		super.keyPress(keyCode);
	}

	public void update() {

		if (mButton_Agree != null)
			mButton_Agree.updateKey();
		if (mButton_Disagree != null)
			mButton_Disagree.updateKey();

		if (!m_bTypeDialog) {
			if (m_iRank > m_iMaxRank) {
				m_iRank = m_iMaxRank;
				tfInput.setText("" + m_iMaxRank);
			}
			if (GameCanvas.isPointerClick) {
				if (GameCanvas.isPointer(m_iLeftDlg + 5, tfInput.y + 20, m_iWDlg - 10, 10)) {
					float p = (float) ((GameCanvas.px - m_iLeftDlg - 5) * 100 / (m_iWDlg - 10));
					m_iRank = (long) ((p * m_iMaxRank) / 100);
					tfInput.setText("" + m_iRank);
				}
			}

			if (GameCanvas.keyPressed[4]) {
				m_iRank -= m_iMaxRank / 10;
				if (m_iMaxRank / 10 == 0)
					m_iRank--;

				if (m_iRank > 100) {
					long tmp = m_iRank;
					tmp /= 100;
					long sodu = tmp % 100;
					if (sodu > 0)
						tmp++;
					m_iRank = tmp * 100;
				}

				if (m_iRank < m_iMinRank)
					m_iRank = m_iMinRank;
				tfInput.setText("" + m_iRank);
				GameCanvas.keyPressed[4] = false;
			}

			if (GameCanvas.keyPressed[6]) {
				m_iRank += m_iMaxRank / 10;
				if (m_iMaxRank / 10 == 0)
					m_iRank--;

				if (m_iRank >= 100) {
					long tmp = m_iRank;
					tmp /= 100;
					long sodu = tmp % 100;
					if (sodu > 0)
						tmp++;
					m_iRank = tmp * 100;
				}

				if (m_iRank > m_iMaxRank)
					m_iRank = m_iMaxRank;
				tfInput.setText("" + m_iRank);
				GameCanvas.keyPressed[6] = false;
			}

			if (GameCanvas.keyPressed[2]) {
				m_iRank += m_iDelRank;
				if (m_iRank > m_iMaxRank)
					m_iRank = m_iMaxRank;
				tfInput.setText("" + m_iRank);
				GameCanvas.keyPressed[2] = false;
			}

			if (GameCanvas.keyPressed[8]) {
				m_iRank -= m_iDelRank;
				if (m_iRank < m_iMinRank)
					m_iRank = m_iMinRank;
				tfInput.setText("" + m_iRank);
				GameCanvas.keyPressed[8] = false;
			}
		}
		tfInput.update();
		super.update();
	}

	public void show() {
		GameCanvas.currentDialog = this;
		tfInput.isFocus = true;
	}

	// @Override
	// public void doBack() {
	// // TODO Auto-generated method stub
	// GameCanvas.instance.m_arrEffect.removeAllElements();
	// GameCanvas.currentDialog = null;
	// }
}
