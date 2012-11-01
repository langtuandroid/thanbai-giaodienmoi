package com.hdc.mycasino.utilities;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.HDCGameMidlet;

public class FrameImage {
	public int frameWidth;
	public int frameHeight;
	public int nFrame;
	public int m_iCurrentFrame = 0;
	private Image imgFrame;
	public float totalHeight, frameWidthF, frameHeightF;

	public FrameImage(Image img, int width, int height) {
		imgFrame = img;

		frameWidthF = width / HDCGameMidlet.instance.scale;
		frameHeightF = height / HDCGameMidlet.instance.scale;

		// if (GameCanvas.w == 320 && GameCanvas.h == 480) {
		// frameWidthF = width / 4 * 3;
		// frameHeightF = height / 4 * 3;
		// }
		frameWidth = (int) frameWidthF;
		frameHeight = (int) frameHeightF;
		totalHeight = img.getHeight();
		nFrame = (int) (totalHeight / frameHeight);
		m_iCurrentFrame = 0;
	}

	public void drawFrame(int idx, float x, float y, int trans, Graphics g) {
		if (idx >= 0 && idx < nFrame)
			g.drawRegion(imgFrame, 0, idx * frameHeightF, frameWidthF, frameHeightF, trans, x, y,
					20);
	}

	public void drawFrame(int idx, float x, float y, int trans, int author, float scale, Graphics g) {
		if (idx >= 0 && idx < nFrame) {
			// g.drawRegion(imgFrame, 0, idx * frameHeightF, frameWidthF,
			// frameHeightF, trans, x, y, author);
			g.drawRegionScale(imgFrame, 0, idx * frameHeightF, frameWidthF, frameHeightF, trans, x,
					y, author, scale);

		}
	}

	public void drawFrame(int idx, float x, float y, int trans, int author, Graphics g) {
		if (idx >= 0 && idx < nFrame)
			g.drawRegion(imgFrame, 0, idx * frameHeightF, frameWidthF, frameHeightF, trans, x, y,
					author);
	}

	public void drawSeriFrame(int x, int y, int trans, Graphics g) {
		m_iCurrentFrame++;
		if (m_iCurrentFrame >= nFrame) {
			m_iCurrentFrame = 0;
		}

		g.drawRegion(imgFrame, 0, m_iCurrentFrame * frameHeightF, frameWidthF, frameHeightF, trans,
				x, y, 20);
	}
}
