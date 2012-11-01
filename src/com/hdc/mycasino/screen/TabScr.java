package com.hdc.mycasino.screen;

import java.util.Vector;

import android.widget.Toast;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.MyObj;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;

public class TabScr extends Screen {
	public static Screen backScr;
	public Vector m_vtScr = new Vector();
	Vector m_vtTitle = new Vector();
	public Screen m_activeScr;
	public boolean m_bIsHaveShop = false;

	public int m_iFocusTab = 0;

	static TabScr instance;

	private Image imgTab;

	public static TabScr gI() {
		if (instance == null) {
			instance = new TabScr();
		}
		return instance;
	}
	
	public static Screen getBackScr(){
		return backScr;
	}

	public void switchToMe(Screen backScr) {
		this.backScr = backScr;
		super.switchToMe();
		// TODO set location
		setLocation();
	}

	public void loadImg() {
		if (GameResource.instance.imgFrames == null)
			GameResource.instance.imgFrames = ImagePack.createImage(ImagePack.FRAMES_PNG);

		// if (HDCGameMidlet.instance.scale == 1f) {
		// } else {
		// float scale;
		// if ((GameCanvas.w <= 480 && GameCanvas.w > 320)
		// && (GameCanvas.h <= 320 && GameCanvas.h > 240)) {
		// }
		// // màn hình 240x320
		// // scale panel cho list screen tỷ lệ : 2/5
		// else {
		// scale = (float) 2 / 5;
		// GameResource.instance.imgListScr_Panel = ImagePack.createImage(
		// ImagePack.LIST_SCREEN_PANEL, scale);
		//
		// }
		// }
		//
		// // scale hight light
		// GameResource.instance.imgTabs_HightLightRow = Image.scaleImage(
		// GameResource.instance.imgTabs_HightLightRow,
		// GameResource.instance.imgListScr_Panel.getWidth(),
		// GameResource.instance.imgTabs_HightLightRow.getHeight());
		//
		// HDCGameMidlet.instance.Toast(GameResource.instance.imgListScr_Panel
		// .getWidth()
		// + " - "
		// + GameResource.instance.imgListScr_Panel.getHeight());

	}

	public void keyPress(int keyCode) {
		if (m_activeScr != null)
			m_activeScr.keyPress(keyCode);
		super.keyPress(keyCode);
	}

	public void close() {
		if (backScr != null) {
			backScr.switchToMe();
			if (backScr instanceof ListBoardScr) {
				// ListBoardScr.getInstance().initCam();
			}
		}
	}

	public void update() {
		if (m_activeScr != null)
			m_activeScr.update();
	}

	public void setSelected(int index) {

		if (m_activeScr != null) {
			m_activeScr.setSelected(index);
		} else
			super.setSelected(index);

	}

	// TODO select tab
	private void selectTab() {
		int numTab = m_vtTitle.size();
		switch (numTab) {
		case 1:
			imgTab = GameResource.instance.imgTabs_1_0;
			break;
		case 2:
			switch (m_iFocusTab) {
			case 0:
				imgTab = GameResource.instance.imgTabs_2_0;
				break;
			case 1:
				imgTab = GameResource.instance.imgTabs_2_1;
				break;
			default:
				break;
			}
			break;
		case 3:
			switch (m_iFocusTab) {
			case 0:
				imgTab = GameResource.instance.imgTabs_3_0;
				break;
			case 1:
				imgTab = GameResource.instance.imgTabs_3_1;
				break;
			case 2:
				imgTab = GameResource.instance.imgTabs_3_2;
				break;
			default:
				break;
			}
			break;
		// case 4:
		// switch (m_iFocusTab) {
		// case 0:
		// imgTab = GameResource.instance.imgTabs_4_0;
		// break;
		// case 1:
		// imgTab = GameResource.instance.imgTabs_4_1;
		// break;
		// case 2:
		// imgTab = GameResource.instance.imgTabs_4_2;
		// break;
		// case 3:
		// imgTab = GameResource.instance.imgTabs_4_3;
		// break;
		//
		// default:
		// break;
		// }
		// break;
		default:
			break;
		}

		if (GameResource.instance.imgListScr_Panel.getWidth() != imgTab.getWidth()) {
			imgTab = Image.scaleImage(imgTab, GameResource.instance.imgListScr_Panel.getWidth(),
					imgTab.getHeight());
		}

	}

	public void updateKey() {
		if (GameCanvas.isPointerClick && m_vtTitle != null) {
			// draw tab
			int i;
			int x = m_left;
			int numTab = m_vtTitle.size();
			int width = imgTab.getWidth() / numTab;
			for (i = 0; i < numTab; i++) {
				if (i != m_iFocusTab) {
					if (GameCanvas.isPointer(x + 1, GameResource.instance.imgHeaderBg.getHeight(),
							width - 2, imgTab.getHeight())) {
						m_iFocusTab = i;
						setActiveScr((Screen) m_vtScr.elementAt(m_iFocusTab));
						// selectTab();

						HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.button4);
						HDCGameMidlet.sound.play();
						break;
					}
				}
				x += width;
			}
		}
		if (!(m_activeScr instanceof ShopScr)) {
			if (m_activeScr != null)
				m_activeScr.updateKey();
		} else {
			if (!m_bIsHaveShop || m_activeScr.m_selected == -1) {
			} else if (m_activeScr != null)
				m_activeScr.updateKey();
		}

		super.updateKey();
	}

	// TODO paint tabs
	private void paintTabs(Graphics g, float x, float y) {
		if (imgTab != null && m_vtTitle != null && m_vtTitle.size() != 0) {
			int w = (int) (imgTab.getWidth() / (m_vtTitle.size() * 2));
			g.drawImage(imgTab, x, y, Graphics.HCENTER | Graphics.TOP);
			for (int i = 0; i < m_vtTitle.size(); i++) {
				if (i == m_iFocusTab) {
					BitmapFont.drawBoldFont(g, (String) m_vtTitle.get(i), m_left + (2 * i + 1) * w,
							y + imgTab.getHeight() / 2, 0xffb901, Graphics.HCENTER
									| Graphics.VCENTER);
				} else {
					BitmapFont.drawNormalFont(g, (String) m_vtTitle.get(i), m_left + (2 * i + 1)
							* w, y + imgTab.getHeight() / 2, 0xffffff, Graphics.HCENTER
							| Graphics.VCENTER);
				}
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

	public void paint(Graphics g) {
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		// g.drawImage(m_imgBackground, 0, 0, 0);

		PaintPopup.paintBackground(g, m_strTitle);

		// TODO paint panel
		g.drawImage(GameResource.instance.imgListScr_Panel, m_left, m_top, Graphics.LEFT
				| Graphics.TOP);

		// TODO paint tabs
		paintTabs(g, GameCanvas.w / 2, 50 / HDCGameMidlet.instance.scale);

		if (m_activeScr != null) {
			m_activeScr.paint(g);
		}
		super.paint(g);
	}

	public void setActiveScr(Screen scr) {
		m_activeScr = scr;
		m_bIsHaveShop = false;
		if (scr instanceof ShopScr)
			m_bIsHaveShop = true;

		m_iFocusTab = m_vtScr.indexOf(m_activeScr);

		m_cmdCenter = m_activeScr.m_cmdCenter;
		m_cmdLeft = m_activeScr.m_cmdLeft;
		m_cmdRight = m_activeScr.m_cmdRight;
		if (m_activeScr instanceof ListScr) {
			ListScr tab = (ListScr) m_activeScr;
			tab.init();
		} else if (m_activeScr instanceof ShopScr) {
			ShopScr tab = (ShopScr) m_activeScr;
			// tab.initCam();
			// GameCanvas.cameraList.setSelect(tab.m_selected);
			CameraList.cmy = CameraList.cmtoY;
		} else if (m_activeScr instanceof ListTabScr) {
			ListTabScr tab = (ListTabScr) m_activeScr;
			tab.init();
			CameraList.cmy = CameraList.cmtoY;
		} else if (m_activeScr instanceof ListStringScr) {
			ListStringScr tab = (ListStringScr) m_activeScr;
			tab.initScroll();
			// CameraList.cmy = CameraList.cmtoY;
		}

		// TODO set image tab
		selectTab();

	}

	public void setCommandActive(Command left, Command center, Command right) {
		m_activeScr.m_cmdCenter = center;
		m_activeScr.m_cmdLeft = left;
		m_activeScr.m_cmdRight = right;
		m_cmdCenter = center;
		m_cmdLeft = left;
		m_cmdRight = right;
	}

	@SuppressWarnings("unchecked")
	public void addScreen(Screen screen, String title) {
		m_vtScr.addElement(screen);
		m_vtTitle.addElement(title);

		// setActiveScr(screen);
	}

	// TODO remover screen
	public void removeScreen(int index) {
		m_vtScr.removeElementAt(index);
		m_vtTitle.removeElementAt(index);
	}

	// TODO get screen
	public Screen getScreen(int index) {
		Screen mScreen = (Screen) m_vtScr.elementAt(index);
		return mScreen;
	}

	// TODO get title screen
	public String getTitle(int index) {
		String title = (String) m_vtTitle.elementAt(index);
		return title;
	}

	public MyObj getSelectedItem() {
		if (m_activeScr != null) {
			if (m_activeScr instanceof ListScr) {
				ListScr lst = (ListScr) m_activeScr;
				return lst.getSelectItems();
			}
			if (m_activeScr instanceof ListTabScr) {
				ListTabScr lst = (ListTabScr) m_activeScr;
				return lst.getSelectItems();
			}
		}
		return null;
	}

	public Vector getListItems() {
		if (m_activeScr != null && m_activeScr instanceof ListScr) {
			ListScr lst = (ListScr) m_activeScr;
			return lst.m_arrItems;
		}
		if (m_activeScr != null && m_activeScr instanceof ListTabScr) {
			ListTabScr lst = (ListTabScr) m_activeScr;
			return lst.m_arrItems;
		}
		return null;
	}

	public void removeAll() {
		m_vtScr.removeAllElements();
		m_vtTitle.removeAllElements();
		m_activeScr = null;
		backScr = null;
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		// HDCGameMidlet.instance.Toast("doMenu - TabScr");
		if (m_activeScr != null)
			m_activeScr.doMenu();
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		// m_vtScr = null;
		// m_vtTitle = null;
		
		HDCGameMidlet.instance.Toast("doback - TabScr");
		if (m_cmdRight != null)
			m_cmdRight.action.perform();
		// close();
	}
}
