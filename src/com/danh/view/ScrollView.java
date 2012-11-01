package com.danh.view;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.utilities.GameResource;

public class ScrollView {
	private float x, y, width, heigh;
	private int cmToX, cmdx, cmvx;
	public int cmX, cmY;
	private int cmToY, cmdy, cmvy;
	// private float m_widthItem, m_heightItem;
	private boolean isTypeScroll;
	private int cmyLimit, disY;
	private int cmxLimit, disX;
	private int page;
	// TODO heigh scroll bar dùng cho scroll đứng
	private static float heighScrollBar;
	// TODO width scroll bar dùng cho scroll ngang
	private float widthScrollBar;
	private float limitScrollBar;
	private Image imgScrollBar;

	public ScrollView() {
		// TODO Auto-generated constructor stub
		this.cmY = this.cmToY = this.cmdy = this.cmvy = 0;
		this.cmX = this.cmToX = this.cmdx = this.cmvx = 0;
	}

	// TODO Init scroll
	@SuppressWarnings("static-access")
	public void initScroll(float x, float y, float width, float heigh, boolean m_isTypeScroll,
			int limit) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.heigh = heigh;
		this.isTypeScroll = m_isTypeScroll;

		// scroll đứng
		if (this.isTypeScroll) {
			cmyLimit = limit;
			if (limit >= 0) {
				heighScrollBar = GameCanvas.h - y;
				limitScrollBar = (limit * heighScrollBar) / (limit + heigh);
				imgScrollBar = Image.scaleImage(GameResource.instance.imgListScr_ScrollBar,
						GameResource.instance.imgListScr_ScrollBar.getWidth(), heighScrollBar
								- limitScrollBar);
			}
			// else
			// imgScrollBar = GameResource.instance.imgListScr_ScrollBar;

		} else {
			if (limit != 0) {
				cmxLimit = limit;
				widthScrollBar = (300 / HDCGameMidlet.instance.scale);
				limitScrollBar = (limit * widthScrollBar) / (limit + width);
//				try {
//					if (widthScrollBar > limitScrollBar)
						GameResource.instance.imgListScr_ScrollBar_Ngang = Image.scaleImage(
								GameResource.instance.imgListScr_ScrollBar_Ngang, widthScrollBar
										- limitScrollBar,
								GameResource.instance.imgListScr_ScrollBar_Ngang.getHeight());
//					else
//						GameResource.instance.imgListScr_ScrollBar_Ngang = Image.scaleImage(
//								GameResource.instance.imgListScr_ScrollBar_Ngang, 50,
//								GameResource.instance.imgListScr_ScrollBar_Ngang.getHeight());
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
		}
	}

	// TODO set page for gridview
	public void setPage(int m_page) {
		this.page = m_page;
	}

	// TODO update
	public void update() {
		// scroll đứng
		if (this.isTypeScroll) {
			if (cmY != cmToY) {
				cmvy = (cmToY - cmY) << 2;
				cmdy += cmvy;
				cmY += cmdy >> 4;
				cmdy = cmdy & 0xf;
			}
			if (Math.abs(cmToY - cmY) < 15 && cmY < 0) {
				cmToY = 0;
			}
			if (Math.abs(cmToY - cmY) < 10 && cmY > cmyLimit) {
				cmToY = cmyLimit;
			}
		} else {
			// TODO scroll ngang
			if (cmX != cmToX) {
				if (cmX != cmToX) {
					cmX += (cmToX - cmX) / 2;
				}
				if (Math.abs((cmToX - cmX) / 2) <= 1)
					cmX = cmToX;
			}
		}
	}

	// TODO update key
	@SuppressWarnings("static-access")
	public void updateKey() {
		if (GameCanvas.instance.hasPointerEvents() && GameCanvas.isPointer(x, y, width, heigh)) {
			if (isTypeScroll) {
				// scroll đứng
				if (GameCanvas.instance.isPointerDown) {
					disY = GameCanvas.pyLast - GameCanvas.py;
				}
				if ((GameCanvas.instance.isPointerMove && GameCanvas.instance.isMove)
						|| GameCanvas.instance.isPointerClick) {
					if (Math.abs(disY) > 50) {
						cmToY = cmY + disY * 3;
						if (cmToY < -30) {
							cmToY = -30;
						}
						if (cmToY > cmyLimit + 30) {
							cmToY = cmyLimit + 30;
						}
					}
				}
			} else {
				// scroll ngang
				if (GameCanvas.isPointerClick) {
					disX = GameCanvas.pxLast - GameCanvas.px;
				}
				if (page > 2) {
					if (disX > 30 && cmX <= width * (page - 2)) {
						cmToX = (int) (cmX + width);
					} else if (disX < -30 && cmX >= width * (page - 2)) {
						cmToX = (int) (cmX - width);
					}
				} else {
					if (disX > 30 && cmX <= 0) {
						cmToX = (int) (cmX + width);
					} else if (disX < -30 && cmX > 0) {
						cmToX = (int) (cmX - width);
					}
				}
				disX = 0;
			}
		}
	}

	// TODO paint
	public void paint(Graphics g) {
		if (this.isTypeScroll && cmyLimit >= 0) {
			// TODO paint height scroll bar
			g.setColor(0x000000);
			g.setAlpha(150);
			g.fillRoundRectWithTransparenr((int) (x + width), (int) y,
					(int) (8 / HDCGameMidlet.instance.scale), (int) heighScrollBar, 4, 4);
			// TODO paint limit scroll

			g.drawImage(imgScrollBar, x + 1 + width,
					(int) (y + (cmY * limitScrollBar / this.cmyLimit)), Graphics.LEFT
							| Graphics.TOP);
		} else {
			// TODO paint height scroll bar
			g.setColor(0x000000);
			g.setAlpha(150);
			g.fillRoundRectWithTransparenr((int) (GameCanvas.w - widthScrollBar) / 2,
					(int) (GameCanvas.h - (15 / HDCGameMidlet.instance.scale)),
					(int) widthScrollBar, (int) (8 / HDCGameMidlet.instance.scale), 4, 4);
			// TODO paint limit scroll
			g.drawImage(GameResource.instance.imgListScr_ScrollBar_Ngang,
					(GameCanvas.w - widthScrollBar) / 2 + (cmX * limitScrollBar / this.cmxLimit),
					(int)(GameCanvas.h - (15 / HDCGameMidlet.instance.scale)), Graphics.LEFT
							| Graphics.TOP);
		}
	}

}
