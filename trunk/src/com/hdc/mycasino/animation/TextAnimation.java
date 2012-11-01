package com.hdc.mycasino.animation;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.utilities.GameResource;

public class TextAnimation extends Effect {

	// TODO hình màu đen
	Image imgBackground;
	// TODO hình hight_light
	Image imgHightLight;
	// TODO hình mask
	Image imgMask;
	// TODO vị trí di chuyển của mask
	int pos;
	public boolean finish;

	public void startEffect(String text, int x, int y, Image m_imgHightLight) {
		super.startEffect(x, y);

		this.imgHightLight = m_imgHightLight;

		// create background
		imgBackground = createBackground(text);

		// vị trí di chuyển của mask
		pos = 0;

		finish = false;
	}

	// TODO tạo image background
	// gồm có hình nền màu đen và text
	private Image createBackground(String text) {
		Image mask = Image.createImage(BitmapFont.m_bmNormalFont.stringWidth(text) + 2
				* this.imgHightLight.getWidth(), this.imgHightLight.getHeight());
		Graphics g = mask.getGraphics();
		g.setColor(0x000000);
		g.fillRect(0, 0, mask.getWidth(), mask.getHeight());
		BitmapFont.drawItalicFont(g, text, this.imgHightLight.getWidth(), mask.getHeight() / 2,
				0x969696, Graphics.LEFT | Graphics.VCENTER);

		return mask;
	}

	// TODO tạo ra hình hightLight text
	private Image createImageHightLight(Image m_imgMask, Image m_imgHightLight) {
		Bitmap mBitmapTmp = m_imgHightLight.getBitmap().copy(Config.ARGB_8888, true);

		int[] pixels_HightLight = new int[mBitmapTmp.getWidth() * mBitmapTmp.getHeight()];
		mBitmapTmp.getPixels(pixels_HightLight, 0, mBitmapTmp.getWidth(), 0, 0,
				mBitmapTmp.getWidth(), mBitmapTmp.getHeight());

		int[] pixels_Mask = new int[m_imgMask.getWidth() * m_imgMask.getHeight()];
		m_imgMask.getBitmap().getPixels(pixels_Mask, 0, m_imgMask.getWidth(), 0, 0,
				m_imgMask.getWidth(), m_imgMask.getHeight());

		for (int i = 0; i < pixels_Mask.length; i++) {
			int red = Color.red(pixels_Mask[i]);
			if (red >= 0 && red <= 130) {
				pixels_HightLight[i] = pixels_HightLight[i] & 0x00FFFFFF;
			}
		}

		try {
			// set pixels
			mBitmapTmp.setPixels(pixels_HightLight, 0, mBitmapTmp.getWidth(), 0, 0,
					mBitmapTmp.getWidth(), mBitmapTmp.getHeight());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Image(mBitmapTmp);
	}

	public void removeEffect() {
		GameCanvas.m_arrEffect.removeAllElements();
		finish = true;
	}

	@Override
	public void update() {
		// TODO
		// Auto-generated method stub
		// tạo ra sub_image từ background
		// sao cho hình ảnh bằng với hình hightLight

		pos += 10;
		if (pos > (imgBackground.getWidth() - imgHightLight.getWidth())) {
			pos = 0;
			m_iGameTick++;
		}
		try {
			Image imgSub = Image.createImageFromSrc(imgBackground, pos, 0,
					imgHightLight.getWidth(), imgHightLight.getHeight());

			imgMask = createImageHightLight(imgSub, imgHightLight);
		} catch (Exception ex) {

		}
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		try {
			g.setClip(m_posCenterEffect.x + imgHightLight.getWidth(), m_posCenterEffect.y,
					imgBackground.getWidth() - 2 * imgHightLight.getWidth(),
					imgBackground.getHeight());

			if (m_iGameTick == 0 && pos >= imgHightLight.getWidth()) {
				if (imgMask != null)
					g.drawImage(imgMask, m_posCenterEffect.x + pos, m_posCenterEffect.y,
							Graphics.LEFT | Graphics.TOP);
			} else if (m_iGameTick > 0) {
				if (imgMask != null)
					g.drawImage(imgMask, m_posCenterEffect.x + pos, m_posCenterEffect.y,
							Graphics.LEFT | Graphics.TOP);
			}
		} catch (Exception e) {

		}
	}

}
