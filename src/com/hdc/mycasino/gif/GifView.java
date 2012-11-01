package com.hdc.mycasino.gif;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.danh.standard.Graphics;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;

public class GifView {

	public static int IMAGE_TYPE_UNKNOWN = 0;
	public static int IMAGE_TYPE_STATIC = 1;
	public static int IMAGE_TYPE_DYNAMIC = 2;

	public static int DECODE_STATUS_UNDECODE = 0;
	public static int DECODE_STATUS_DECODING = 1;
	public static int DECODE_STATUS_DECODED = 2;

	private GifDecoder decoder;
	private Bitmap bitmap;

	private int imageType = IMAGE_TYPE_UNKNOWN;
	private int decodeStatus = DECODE_STATUS_UNDECODE;

	public int width;
	public int height;

	private long time;
	private int index;
	// private int resId;
	// private String filePath;

	private boolean playFlag = false;
	public InputStream in[];

	public GifView(InputStream input[]) {
		// super(context, attrs);
		if (input != null) {
			in = input;
			Bitmap bitmap = BitmapFactory.decodeStream(input[0]);
			bitmap = Graphics.BitmapResize(bitmap, bitmap.getWidth() / HDCGameMidlet.scale,
					bitmap.getHeight() / HDCGameMidlet.scale);
			setGif(bitmap);
			time = System.currentTimeMillis();
			playFlag = true;
		}
	}

	/**
	 * Constructor
	 */
	// public GifView(Context context) {
	// super(context);
	// }

	// private InputStream getInputStream() {
	// if (filePath != null)
	// try {
	// return HDCGameMidlet.assets.open(filePath);
	// return new FileInputStream(filePath);
	// } catch (IOException e) {
	// }
	// if (resId > 0)
	// return getContext().getResources().openRawResource(resId);
	// return null;
	// }

	/**
	 * set gif file path
	 * 
	 * @param filePath
	 */
	public void setGif(InputStream input) {
		// InputStream in;
		// try {
		// in = input;
		// Bitmap bitmap = BitmapFactory.decodeStream(in);
		// Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		// setGif(filePath, bitmap);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	/**
	 * set gif file path and cache image
	 * 
	 * @param filePath
	 * @param cacheImage
	 */
	public void setGif(Bitmap cacheImage) {
		// this.resId = 0;
		// this.filePath = filePath;
		imageType = IMAGE_TYPE_UNKNOWN;
		decodeStatus = DECODE_STATUS_UNDECODE;
		playFlag = false;
		bitmap = cacheImage;
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		// setLayoutParams(new LayoutParams(width, height));
	}

	/**
	 * set gif resource id
	 * 
	 * @param resId
	 */
	// public void setGif(int resId) {
	// Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
	// setGif(resId, bitmap);
	// }

	/**
	 * set gif resource id and cache image
	 * 
	 * @param resId
	 * @param cacheImage
	 */
	// public void setGif(int resId, Bitmap cacheImage) {
	// this.filePath = null;
	// this.resId = resId;
	// imageType = IMAGE_TYPE_UNKNOWN;
	// decodeStatus = DECODE_STATUS_UNDECODE;
	// playFlag = false;
	// bitmap = cacheImage;
	// width = bitmap.getWidth();
	// height = bitmap.getHeight();
	// setLayoutParams(new LayoutParams(width, height));
	// }

	private void decode() {
		release();
		index = 0;

		decodeStatus = DECODE_STATUS_DECODING;

		new Thread() {
			@Override
			public void run() {
				System.out.println("xxxxxxxxxxxxxxxxxxxx");
				decoder = new GifDecoder();
				decoder.read(in[1]);
				if (decoder.width == 0 || decoder.height == 0) {
					imageType = IMAGE_TYPE_STATIC;
				} else {
					imageType = IMAGE_TYPE_DYNAMIC;
				}
				// postInvalidate();
				time = System.currentTimeMillis();
				decodeStatus = DECODE_STATUS_DECODED;
			}
		}.start();
	}

	public void release() {
		decoder = null;
	}

	int x, y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void onDraw(Graphics g) {
		Canvas canvas = g.getCanvas();
		// bitmap = Graphics.BitmapResize(bitmap, 50, 50);
		if (decodeStatus == DECODE_STATUS_UNDECODE) {
			canvas.drawBitmap(bitmap, x, y, null);
			if (playFlag) {
				decode();
				// invalidate();
			}
		} else if (decodeStatus == DECODE_STATUS_DECODING) {
			canvas.drawBitmap(bitmap, x, y, null);
			// invalidate();
		} else if (decodeStatus == DECODE_STATUS_DECODED) {
			if (imageType == IMAGE_TYPE_STATIC) {
				canvas.drawBitmap(bitmap, x, y, null);
			} else if (imageType == IMAGE_TYPE_DYNAMIC) {
				if (playFlag) {
					long now = System.currentTimeMillis();

					if (time + decoder.getDelay(index) < now) {
						time += decoder.getDelay(index);
						incrementFrameIndex();
					}
					Bitmap bitmap = decoder.getFrame(index);
					bitmap = Graphics.BitmapResize(bitmap, bitmap.getWidth() / HDCGameMidlet.scale,
							bitmap.getHeight() / HDCGameMidlet.scale);
					if (bitmap != null) {
						canvas.drawBitmap(bitmap, x, y, null);
					}
					// invalidate();
				} else {
					Bitmap bitmap = decoder.getFrame(index);
					bitmap = Graphics.BitmapResize(bitmap, bitmap.getWidth() / HDCGameMidlet.scale,
							bitmap.getHeight() / HDCGameMidlet.scale);
					canvas.drawBitmap(bitmap, x, y, null);
				}
			} else {
				canvas.drawBitmap(bitmap, x, y, null);
			}
		}
	}

	private void incrementFrameIndex() {
		index++;
		if (index >= decoder.getFrameCount()) {
			index = 0;
		}
	}

	// private void decrementFrameIndex() {
	// index--;
	// if (index < 0) {
	// index = decoder.getFrameCount() - 1;
	// }
	// }

	public void play() {
		time = System.currentTimeMillis();
		playFlag = true;
		// invalidate();
	}

	public void pause() {
		playFlag = false;
		// invalidate();
	}

	public void stop() {
		playFlag = false;
		index = 0;
		// invalidate();
	}

	// public void nextFrame() {
	// if (decodeStatus == DECODE_STATUS_DECODED) {
	// incrementFrameIndex();
	// invalidate();
	// }
	// }

	// public void prevFrame() {
	// if (decodeStatus == DECODE_STATUS_DECODED) {
	// decrementFrameIndex();
	// invalidate();
	// }
	// }
}