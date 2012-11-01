package com.hdc.mycasino.screen;

import android.R.bool;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.animation.BounceInterpolator;

import com.danh.standard.Graphics;
import com.hdc.mycasino.model.Position;
import com.hdc.mycasino.utilities.DetailImage;
import com.hdc.mycasino.utilities.GameResource;

public class Card {
	public static byte PHOM = 0;
	public static byte KHAC = 1;

	public int yTo, xTo;
	public float m_x;
	public float m_y;
	public int m_id;

	public boolean isSpecial = false;
	public boolean isPhom = false;
	public boolean isUpCard = false;
	public boolean isCallMove = false;
	public boolean isActionRotate = false;
	public boolean isOpacity = false;
	private float scale = 1.0f;
	public float scaleTo = 1.0f;

	private int tickRotate = 0;// thời gian chuyển bài up sang bài ngửa
	// init vị trí tiếp theo duy chuyển của quân bài cho hàm duy chuyển theo
	// vòng cung
	public int xFirst, yFirst, xNext, yNext;
	// chiều cao của cung duy chuyển
	final int MaxHeight = GameResource.instance.m_imgCards[0].getHeight() << 1;
	double timeTickMove = 0.00;// thời gian duy chuyển quân bài theo vòng cung
	int anpha = 0;// góc quay của lá bài hiện tại
	public int anphaTo = 0;// góc quay sắp tới của lá bài
	// anchor của paint
	public byte typeAnchorPaint = Graphics.LEFT | Graphics.TOP;

	public void setActionRotate() {
		isActionRotate = true;
		if (!isUpCard)
			tickRotate = 2;
		else
			tickRotate = 0;
	}

	public Card(int id) {
		m_id = id;
	}

	// xét khởi tạo cho điểm bắt đầu
	public void setPos(int x, int y) {
		this.m_x = x;
		this.m_y = y;
		isSpecial = false;
		isPhom = false;
		isUpCard = false;
		isCallMove = false;
		isActionRotate = false;
		isOpacity = false;
		typeAnchorPaint = Graphics.LEFT | Graphics.TOP;
	}

	// xét khởi tạo cho điểm đến
	public void setPosTo(int xTo, int yTo) {
		this.xTo = xTo;
		this.yTo = yTo;
	}

	// init vi trí của những lá bài
	public void setPos(int x, int y, int xTo, int yTo) {
		setPos(x, y);
		setPosTo(xTo, yTo);
	}

	// init vị trí bay ra với góc và cung
	public void setPos(int xTo, int yTo, int anpha, float scale) {
		setPosTo(xTo, yTo);
		this.anphaTo = anpha;
		this.scaleTo = scale;
	}

	// init vị trí cho điểm bắc đầu khi sử dụng hàm duy chuyển theo vòng
	// cung(1,2)
	private void setNextPos(int x, int y) {
		xFirst = x;
		yFirst = y;
		xNext = x;
		yNext = y;
	}

	// xét khởi tạo cho vị trí đến khi sử dụng hàm duy chuyển theo vòng cung(1)
	public void setNextPosTo(int xto, int yto) {
		setPosTo(xto, yto);
		setNextPos((int) m_x, (int) m_y);
	}

	// tìm vị trí nằm phía ngoài của dường thằng di qua 2 diển x1,y1 và x2,y2 và
	// vuông góc với đường thẳng
	// tại x0, y0 có độ cao là h so với đường thẳng
	private Position getPoint(int x1, int y1, int x2, int y2, int x0, int y0, int h) {
		double a = (x2 - x1);
		double b = (y2 - y1);
		double c = -a * x0 - b * y0;
		// System.out.println("/////////////////////");
		// System.out.println(a+" -- "+b+" -- "+c);
		double aa, bb, cc, d, kq3, kq4;
		double kq1 = 0, kq2 = 0;
		Position find = new Position();
		if (a != 0) {
			// a = Math.pow(b / a, 2) + 1;
			aa = (b / a) * (b / a) + 1;
			bb = 2 * (c / a + x0) * b / a - 2 * y0;
			// c = Math.pow(c / a + x0, 2) + y0 * y0 - h * h;
			cc = (c / a + x0) * (c / a + x0) + y0 * y0 - h * h;
			d = bb * bb - 4 * aa * cc;
			// System.out.println(aa+" == "+bb+" == "+cc+" == "+d);
			if (aa == 0) {
				kq1 = kq2 = -bb / (2 * aa);
			} else {
				kq1 = (-bb + Math.sqrt(d)) / (2 * aa);
				kq2 = (-bb - Math.sqrt(d)) / (2 * aa);
			}
			// cai nay la y
			kq3 = (-b * kq1 - c) / a;// la x
			kq4 = (-b * kq2 - c) / a;// la x
			// System.out.println(kq1+" =-= "+kq2+" =-= "+kq3+" =-= "+kq4);
			// ////
			a = (y2 - y1);
			b = -(x2 - x1);
			c = -(y2 - y1) * x1 + y1 * (x2 - x1);
			d = a * kq3 + b * kq1 + c;
			// System.out.println(aa+" == "+bb+" == "+cc+" == "+d);
			if (d < 0) {
				find.x = (int) kq3;
				find.y = (int) kq1;
			} else {
				find.x = (int) kq4;
				find.y = (int) kq2;
			}
		} else {
			kq4 = kq3 = -c / b;// la y
			// //////
			aa = 1;
			bb = -2 * x0;
			cc = (y0 + c / b) * (y0 + c / b) + x0 * x0 - h * h;
			d = bb * bb - 4 * aa * cc;
			// System.out.println(aa+" == "+bb+" == "+cc+" == "+d);
			if (aa == 0) {
				kq1 = kq2 = -bb / (2 * aa);
			} else {
				kq1 = (-bb + Math.sqrt(d)) / (2 * aa);
				kq2 = (-bb - Math.sqrt(d)) / (2 * aa);
			}
			// System.out.println(kq1+" =-= "+kq2+" =-= "+kq3+" =-= "+kq4);
			// ////
			a = (y2 - y1);
			b = -(x2 - x1);
			c = -(y2 - y1) * x1 + y1 * (x2 - x1);
			d = a * kq3 + b * kq1 + c;
			// System.out.println(aa+" == "+bb+" == "+cc+" == "+d);
			if (d < 0) {
				find.x = (int) kq1;
				find.y = (int) kq3;
			} else {
				find.x = (int) kq2;
				find.y = (int) kq4;
			}
		}
		return find;
	}

	// duy chuyển quân bài theo các điểm nằm cách điều của hàm getPoint ở trên
	// và speed là khoảng cách giữa các điểm tạo ra
	// tọa độ x0,y0 cho vị trí mới sẽ duy chuyển đến. hàm duy chuyển cho vòng
	// cung(1)
	public int translateArc(int speed) {
		if (m_x == xTo && m_y == yTo)
			return -1;

		if (Math.abs((xTo - xNext) / speed) <= 1 && Math.abs((yTo - yNext) / speed) <= 1) {
			m_x = xFirst = xTo;
			m_y = yFirst = yTo;
			return 0;
		}
		if (m_x != xTo || m_y != yTo) {
			int a = xNext;
			int b = yNext;
			xNext += (xTo - xNext) / speed;
			yNext += (yTo - yNext) / speed;
			double h = Math.hypot(xFirst - xNext, yFirst - yNext) / Math.hypot(xFirst - xTo, yFirst - yTo) * MaxHeight;
			// System.out.println(a+" "+
			// b+" "+xTo+" "+yTo+" "+xNext+" "+yNext+" "+h);
			Position pos = getPoint(a, b, xTo, yTo, xNext, yNext, (int) h);
			m_x = pos.x;
			m_y = pos.y;
			// System.out.println(m_x+"   ++   "+m_y);
			pos = null;
		}
		return 1;
	}

	// xét khởi tạo cho vị trí đến khi sử dụng hàm duy chuyển theo vòng cung(2)
	public void setNextNewPosTo(int xto, int yto, int anpha, float scale) {
		setPos(xto, yto, anpha, scale);
		setNextPos((int) m_x, (int) m_y);
		timeTickMove = 0.00;
		if (m_x == xTo && m_y == yTo)
			timeTickMove = 1;
		// ////////
		xNext += (xTo - xNext) / 2;
		yNext += (yTo - yNext) / 2;
		Position pos = getPoint((int) m_x, (int) m_y, xTo, yTo, xNext, yNext, (int) MaxHeight);
		xNext = pos.x;
		yNext = pos.y;
		pos = null;
	}

	// duy chuyển quân bài theo các điểm nằm cách điều của hàm getPoint ở trên
	// và speed là khoảng cách giữa các điểm tạo ra
	// tọa độ x0,y0 cho vị trí mới sẽ duy chuyển đến. hàm duy chuyển cho vòng
	// cung(2) với tốc độ trung bình theo speed
	public int translateNewArc(int speed) {
		if (timeTickMove >= 1)
			return -1;
		// timeTickMove += (speed / 10);
		translateAnchor(speed);

		if (timeTickMove <= 1) {
			float tmp = (float) ((1 - timeTickMove) / speed);
			if (tmp < 0.05f)
				tmp = 0.05f;
			timeTickMove += tmp;
			// finish
			if (timeTickMove >= 1) {
				timeTickMove = 1;
				m_x = xFirst = xTo;
				m_y = yFirst = yTo;
				anpha = anphaTo;
				scale = scaleTo;
				setWH();
				return -1;
			}

			m_x = (float) ((1 - timeTickMove) * (1 - timeTickMove) * xFirst + 2 * (1 - timeTickMove) * timeTickMove
					* xNext + timeTickMove * timeTickMove * xTo);
			m_y = (float) ((1 - timeTickMove) * (1 - timeTickMove) * yFirst + 2 * (1 - timeTickMove) * timeTickMove
					* yNext + timeTickMove * timeTickMove * yTo);
		}
		return -1;
	}

	private void translateAnchor(int speed) {
		if (anpha == anphaTo && scale == scaleTo)
			return;

		if (Math.abs((anphaTo - anpha) / speed) <= 1)
			anpha = anphaTo;

		if (Math.abs((scaleTo - scale) / speed) <= 1)
			scale = scaleTo;

		if (anpha != anphaTo)
			anpha += (anphaTo - anpha) / speed;

		if (scale != scaleTo)
			scale += (scaleTo - scale) / speed;
		setWH();
	}

	private void setWH() {
		w = DetailImage.imgCard_w;
		h = DetailImage.imgCard_h;
		if (scale != 1f) {
			w /= scale;
			h /= scale;
		}
	}

	// ///////
	public int translate(int speed) {
		translateAnchor(speed);
		if (m_x == xTo && m_y == yTo)
			return -1;

		if (Math.abs((xTo - m_x) / speed) <= 1 && Math.abs((yTo - m_y) / speed) <= 1) {
			m_x = xTo;
			m_y = yTo;
			setWH();
			return -1;
		}
		if (m_x != xTo) {
			m_x += (xTo - m_x) / speed;
		}
		if (m_y != yTo) {
			m_y += (yTo - m_y) / speed;
		}
		return -1;
	}

	// /danh cho tat ca game khac
	static int pNumber[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,// bich
			13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,// tep
			26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,// ro
			39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };// co
	// danh acho tien len
	static int aNumber[] = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1,// Bich
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 13, 14,// tep
			28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 26, 27,// ro
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 39, 40,// co
	};

	static int cardPaint[];

	public static void setCardType(int type) {
		if (type == 0) {// phom
			cardPaint = pNumber;
		} else {
			cardPaint = aNumber;
		}
	}

	// TODO scale image hit card
	public void setScale(float scale) {
		this.scale = scale;
		this.scaleTo = scale;
	}

	public boolean compare(Card c) {
		return (scale == c.scale && anpha == c.anpha && m_x == c.m_x && m_y == c.m_y);
	}

	public void paintMoveCard(Graphics g, float x, float y) {
		// g.drawScaleImage(GameResource.instance.m_imgCards[cardPaint[m_id]],x,
		// y, 0,
		// GameResource.instance.m_imgCards[cardPaint[m_id]].getWidth() * 2 / 3,
		// GameResource.instance.m_imgCards[cardPaint[m_id]].getHeight());
		g.drawImage(GameResource.instance.m_imgCards[cardPaint[m_id]], x, y, Graphics.LEFT | Graphics.TOP);
	}

	int w, h;

	public void paint(Graphics g) {
		// TODO paint card
		if (isActionRotate && m_x == xTo && m_y == yTo) {
			if (tickRotate > 1) {
				g.drawScaleImage(GameResource.instance.m_imgCards[cardPaint[m_id]], m_x + w / 6, m_y, typeAnchorPaint,
						w * 2 / 3, h);
			} else {
				g.drawScaleImage(GameResource.instance.imgTienLen_CardBack2, m_x + w / 6, m_y, typeAnchorPaint,
						w * 2 / 3, h);
			}

			if (!isUpCard)
				tickRotate--;
			else
				tickRotate++;

			if (tickRotate < 0 || tickRotate > 2) {
				isUpCard = !isUpCard;
				isActionRotate = false;
			}
			return;
		}

		if (!isUpCard) {// vẽ card thông thường
			if (m_id != -1) {
				g.drawScaleImage(GameResource.instance.m_imgCards[cardPaint[m_id]], m_x, m_y, typeAnchorPaint, w, h);
			}
		} else {
			// vẽ upcard
			// g.drawImage(GameResource.instance.imgTienLen_CardBack2,
			// m_x, m_y,
			// 0);
			// g.drawRotateImage(GameResource.instance.imgTienLen_CardBack2,
			// m_x, m_y, 0, anpha);
			if (anpha != 0)
				g.drawRotateScaleImage(GameResource.instance.imgTienLen_CardBack2, m_x, m_y, typeAnchorPaint, w, h,
						anpha);
			else
				g.drawScaleImage(GameResource.instance.imgTienLen_CardBack2, m_x, m_y, typeAnchorPaint, w, h);
			return;
		}

		// if (isUpCard) {
		// g.drawImage(GameResource.instance.imgCardUpside, m_x, m_y, 0);
		// return;
		// }

		// int cid = cardPaint[m_id] % 13;// id
		// int type = cardPaint[m_id] / 13;// type
		//
		// if (isOpacity) {
		// g.drawImageOpacity(GameResource.instance.imgCard, m_x, m_y, 0, 80);
		// } else
		// g.drawImage(GameResource.instance.imgCard, m_x, m_y, 0);

		if (isPhom) {
			// g.setColor(0xffcfcf);
			// g.fillRoundRect(m_x, m_y, 31, 44, 2, 2);
			// g.drawImage(GameResource.instance.imgCard_HighLight, m_x, m_y,
			// Graphics.LEFT
			// | Graphics.TOP);
			g.drawScaleImage(GameResource.instance.imgCard_HighLight_1, m_x, m_y, typeAnchorPaint, w, h);
		} else if (isSpecial) {
			// g.setColor(0xc8c8c8);
			// g.fillRoundRect(m_x, m_y, 31, 44, 2, 2);
			// g.drawImage(GameResource.instance.imgCard_HighLight_1, m_x,
			// m_y,Graphics.LEFT|Graphics.TOP);
			g.drawScaleImage(GameResource.instance.imgCard_HighLight, m_x, m_y, typeAnchorPaint, w, h);
		}

		// if (type < 2) {
		// g.drawRegion(GameResource.instance.imgIconCard, cid * 8, 0, 8, 9,
		// 0, m_x + 2, m_y + 2, 0);
		// } else {
		// g.drawRegion(GameResource.instance.imgIconCard, cid * 8, 9, 8, 9,
		// 0, m_x + 2, m_y + 2, 0);
		// }
		//
		// g.drawRegion(GameResource.instance.imgIconCard, 50 + type * 7, 69, 7,
		// 8, 0, m_x + 2, m_y + 14, 0);
		//
		// if (cid == 10) {
		// type = 6;
		// } else if (cid == 11) {
		// type = 4;
		// } else if (cid == 12) {
		// type = 5;
		// }
		//
		// if (type < 4) {
		// g.drawRegion(GameResource.instance.imgIconCard, type * 18, 18, 18,
		// 18, 0, m_x + 8, m_y + 22, 0);
		// } else {
		// g.drawRegion(GameResource.instance.imgIconCard, (type - 4) * 26,
		// 36, 26, 31, 0, m_x + 4, m_y + 10, 0);
		// }
	}

	public void CopyTo(Card c) {
		this.m_id = c.m_id;
		this.xTo = c.xTo;
		this.yTo = c.yTo;
		this.m_x = c.m_x;
		this.m_y = c.m_y;
	}
}
