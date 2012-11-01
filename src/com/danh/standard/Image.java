package com.danh.standard;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class Image {
	public static Image createImage(InputStream stream) throws IOException {
		Bitmap bitmap = BitmapFactory.decodeStream(stream);
		if (bitmap == null)
			throw new IOException();
		return new Image(bitmap);
	}

	public static Image createImage(int width, int height) {
		// TODO it would be nice to use RGB_565 if the display is 16 bit
		// (ARGB_8888 always works but uses more memory and eats resources
		// during the conversion):

		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		return new Image(bitmap);
	}

	// TODO tạo hình từ hình bitmap có sẵn
	public static Image createImageFromSrc(Image src, int x, int y, int w, int h) {
		Bitmap bitmap = Bitmap.createBitmap(src.getBitmap(), x, y, w, h);
		return new Image(bitmap);
	}

	public static Image createImage(String resource) throws IOException {
		resource = "/assets" + resource;
		return createImage(Image.class.getResourceAsStream(resource));
	}

	public static Image createImage(byte[] imageData, int imageOffset, int imageLength) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, imageOffset, imageLength);

		if (bitmap == null) {
			throw new IllegalArgumentException("Could not decode image data");
		}
		return new Image(bitmap);
	}

	// TODO scale image
	public static Image scaleImage(Image img, float newWidth, float newHeight) {
		return new Image(Graphics.BitmapResize(img.getBitmap(), newWidth, newHeight));
	}

	public static final Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
		Bitmap.Config config;
		if (processAlpha) {
			config = Bitmap.Config.ARGB_8888;
		} else {
			config = Bitmap.Config.RGB_565;
		}
		Bitmap bitmap = Bitmap.createBitmap(rgb, width, height, config);
		return new Image(bitmap);
	}

	private Bitmap bitmap;

	public Image(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return this.bitmap;
	}

	public int getWidth() {
		if (bitmap == null)
			return 0;
		return this.bitmap.getWidth();
	}

	public int getHeight() {
		if (this.bitmap == null) {
			return 0;
		}

		return this.bitmap.getHeight();
	}

	public Graphics getGraphics() {
		return new Graphics(this);
	}

	public void getRGB(int[] rgb, int offset, int scanlength, int x, int y, int width, int height) {
		if (bitmap == null)
			return;
		this.bitmap.getPixels(rgb, offset, scanlength, x, y, width, height);
	}

	public static Image createImage(Image source) {
		return createImage(source.getWidth(), source.getHeight());
	}

	public boolean isMutable() {
		return false;
	}

	// merge pixels for 2 bitmaps
	// bitmap A - > bitmap B
	public Image mergePixels(Image imgA, Image imgB) {
		// get pixels for image A
		int[] pixels_A = new int[imgA.getBitmap().getWidth() * imgA.getBitmap().getHeight()];
		imgA.getBitmap().getPixels(pixels_A, 0, imgA.getBitmap().getWidth(), 0, 0,
				imgA.getBitmap().getWidth(), imgA.getBitmap().getHeight());
		// get pixels for image B
		int[] pixels_B = new int[imgB.getBitmap().getWidth() * imgB.getBitmap().getHeight()];
		imgB.getBitmap().getPixels(pixels_B, 0, imgB.getBitmap().getWidth(), 0, 0,
				imgB.getBitmap().getWidth(), imgB.getBitmap().getHeight());

		// merge pixels A - > pixels B
		// for()

		return null;
	}

}
