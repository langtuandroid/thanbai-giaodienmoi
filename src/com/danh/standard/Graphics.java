package com.danh.standard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.Log;

import com.hdc.mycasino.utilities.GameResource;

public class Graphics {
	public static final int BASELINE = 64;
	public static final int BOTTOM = 32;
	public static final int LEFT = 4;
	public static final int RIGHT = 8;
	public static final int TOP = 16;
	public static final int VCENTER = 2;
	public static final int HCENTER = 1;

	public static final int DOTTED = 1;
	public static final int SOLID = 0;

	private android.graphics.Canvas canvas;
	private Image img;

	private Paint paint;
	private Font font;
	private int tx, ty;
	private Rect srcRec = new Rect();
	private Rect dstRec = new Rect();
	public Align align;
	Matrix matrix;
	Canvas canvas1;
	float ixA = 0, iyA = 0;
	float ix = 0, iy = 0;

	public Graphics(android.graphics.Canvas canvas) {
		setFont(Font.getDefaultFont());
		paint = new Paint();
		paint.setAntiAlias(false);
		this.canvas = canvas;
		canvas.save();
	}

	public Graphics(Image img) {

		this(new Canvas(img.getBitmap()), img);
	}

	public Graphics(Canvas canvas, Image img) {
		setFont(Font.getDefaultFont());
		this.canvas = canvas;
		this.img = img;
		canvas.save();
	}

	public Image getImage() {
		return this.img;
	}

	public android.graphics.Canvas getCanvas() {
		return this.canvas;
	}

	public int getClipX() {
		return this.canvas.getClipBounds().left;
	}

	public int getClipY() {
		return this.canvas.getClipBounds().top;
	}

	public int getClipWidth() {
		return this.canvas.getClipBounds().width();
	}

	public int getClipHeight() {
		return this.canvas.getClipBounds().height();
	}

	public int getColor() {
		return this.paint.getColor() & 0x00FFFFFF;
	}

	public void setColor(int color) {
		this.paint.setColor(0xFF000000 | color);
	}

	public void setColor(int r, int g, int b) {
		// int val = (r << 16) | (g << 8) | b;
		setColor((r << 16) | (g << 8) | b);
	}

	public void fillRect(int x, int y, int width, int height) {
		paint.setStyle(Style.FILL);
		this.canvas.drawRect(x, y, x + width, y + height, this.paint);

	}

	public void setAlpha(int alpha) {
		paint.setAlpha(alpha);
	}

	// TODO fillRect with Transparent
	public void fillRectWithTransparent(float x, float y, float width, float height) {
		// paint.setColor(color);
		paint.setStyle(Style.FILL);
		// paint.setAlpha(120);
		this.canvas.drawRect(x, y, x + width, y + height, this.paint);
	}

	// TODO fill round rect transparent
	public void fillRoundRectWithTransparenr(int x, int y, int width, int height, int rx, int ry) {
		// paint.setColor(color);
		// paint.setStyle(Style.FILL);
		// paint.setAlpha(120);
		// fillRoundRect(x, y, width, height, rx, ry);
		this.canvas.drawRoundRect(new RectF(x, y, x + width, y + height), rx, ry, this.paint);

	}

	// TODO fill Round Rect With Gadient
	public void fillRoundRectWithGadient(int x, int y, int width, int height, int rx, int ry) {

		paint.setAlpha(255);
		paint.setStyle(Style.FILL);
		paint.setShader(new LinearGradient(x + width / 2, y - 10, x + width / 2, y + height / 2,
				0x30662d, Color.GRAY, TileMode.MIRROR));
		this.canvas.drawRoundRect(new RectF(x, y, x + width, y + height), rx, ry, this.paint);
		paint.reset();

	}

	public void fillRoundRect(int x, int y, int width, int height, int rx, int ry) {
		paint.setStyle(Style.FILL);
		this.canvas.drawRoundRect(new RectF(x, y, x + width, y + height), rx, ry, this.paint);
	}

	public void drawImage(Image image, float x, float y, int anchor) {
		try {
			float ax = x;
			float ay = y;
			if ((anchor & RIGHT) != 0) {
				ax = x - image.getWidth();
			} else if ((anchor & HCENTER) != 0) {
				ax = x - image.getWidth() / 2;
			} else {
				ax = x;
			}
			if ((anchor & BOTTOM) != 0) {
				ay = y - image.getHeight();
			} else if ((anchor & VCENTER) != 0) {
				ay = y - image.getHeight() / 2;
			} else {
				ay = y;
			}
			this.canvas.drawBitmap(image.getBitmap(), ax, ay, null);
		} catch (Exception e) {

		}

	}

	public void drawRotateImage(Image image, float x, float y, int anchor, int rotate) {
		try {
			float ax = x;
			float ay = y;

			if ((anchor & RIGHT) != 0) {
				ax = x - image.getWidth();
			} else if ((anchor & HCENTER) != 0) {
				ax = x - image.getWidth() / 2;
			} else {
				ax = x;
			}
			if ((anchor & BOTTOM) != 0) {
				ay = y - image.getHeight();
			} else if ((anchor & VCENTER) != 0) {
				ay = y - image.getHeight() / 2;
			} else {
				ay = y;
			}
			this.canvas.save();
			this.canvas.rotate(rotate, x, y);
			this.canvas.drawBitmap(image.getBitmap(), ax, ay, null);
			this.canvas.restore();
		} catch (Exception e) {

		}

	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		this.canvas.drawLine(x1, y1, x2, y2, this.paint);
	}

	public void drawRect(int x, int y, int width, int height) {
		// Paint outlinePaint = new Paint(this.paint);
		// outlinePaint.setStyle(Style.STROKE);
		// outlinePaint.setAntiAlias(true);
		// this.canvas.drawRect(x, y, x + width, y + height, outlinePaint);

		paint.setStyle(Style.STROKE);
		this.canvas.drawRect(x, y, x + width, y + height, paint);
	}

	public void drawRoundRect(int x, int y, int width, int height, int rx, int ry) {
		// Paint outlinePaint = new Paint(this.paint);
		// outlinePaint.setStyle(Style.STROKE);
		// outlinePaint.setAntiAlias(true);
		// this.canvas.drawRoundRect(new RectF(x, y, x + width, y + height), rx,
		// ry, outlinePaint);

		paint.setStyle(Style.STROKE);
		this.canvas.drawRoundRect(new RectF(x, y, x + width, y + height), rx, ry, paint);
	}

	public Font getFont() {
		return this.font;
	}

	public void setFont(Font font) {
		Paint typefacePaint = font.getTypefacePaint();
		if (this.paint != null) {
			this.paint.setTypeface(typefacePaint.getTypeface());
			this.paint.setUnderlineText(typefacePaint.isUnderlineText());
		} else {
			this.paint = new Paint(typefacePaint);
			// this.paint.setAntiAlias(true);
		}
		this.font = font;
	}

	public void drawSubstring(String str, int offset, int length, int x, int y, int anchor) {
		drawString(str.substring(offset, offset) + length, x, y, anchor);
	}

	public void drawString(String str, int x, int y, int anchor) {
		// Align align;
		if ((anchor & HCENTER) != 0) {
			align = Align.CENTER;
		} else if ((anchor & RIGHT) != 0) {
			align = Align.RIGHT;
		} else {
			align = Align.LEFT;
		}

		this.paint.setTextAlign(align);
		this.canvas.save();

		float offset = 0;
		float scale = this.font.getScale();
		this.canvas.scale(scale, scale);

		if ((anchor & TOP) != 0) {
			offset = paint.getTextSize() - paint.descent();
		} else if ((anchor & BOTTOM) != 0) {
			offset = -paint.descent();
		} else if ((anchor & VCENTER) != 0) {
			offset = paint.getTextSize() / 2.0f - paint.descent();
		}

		float invScale = 1.0F / scale;
		this.canvas.drawText(str, x * invScale, y * invScale + offset, this.paint);

		this.canvas.restore();
	}

	public void drawChar(char character, int x, int y, Paint paint, int anchor) {
		drawString(String.valueOf(character), x, y, anchor);
	}

	public void clipRect(int x, int y, int w, int h) {
		this.canvas.clipRect(x, y, x + w, y + h);
	}

	public void setClip(float x, float y, float w, float h) {
		try {
			this.canvas.restore();
		} catch (IllegalStateException e) {
			// Dont need to catch this
		}

		this.canvas.save();
		this.canvas.translate(this.tx, this.ty);
		this.canvas.clipRect(x, y, x + w, y + h);

	}

	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		// Paint fillPaint = new Paint(this.paint);
		// fillPaint.setStyle(Style.FILL);
		// fillPaint.setAntiAlias(true);
		// this.canvas.drawArc(new RectF(x, y, x + width, y + height),
		// startAngle,
		// arcAngle, true, fillPaint);
		paint.setStyle(Style.FILL);
		this.canvas.drawArc(new RectF(x, y, x + width, y + height), startAngle, arcAngle, true,
				paint);
	}

	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		// TODO
		Path path = new Path();
		path.moveTo(x1 + getTranslateX(), y1 + getTranslateY());
		path.lineTo(x2 + getTranslateX(), y2 + getTranslateY());
		path.lineTo(x3 + getTranslateX(), y3 + getTranslateY());
		path.close();

		paint.setStyle(Paint.Style.FILL);
		canvas.drawPath(path, paint);
	}

	public void translate(int x, int y) {
		this.tx += x;
		this.ty += y;
		this.canvas.translate(x, y);
	}

	public int getTranslateX() {
		return this.tx;
	}

	public int getTranslateY() {
		return this.ty;
	}

	// public void drawRegion(Image img, int x_src, int y_src, int width,
	// int height, int transform, int x_dest, int y_dest, int Anchor) {
	//
	// canvas.save();
	//
	// int ixA = 0, iyA = 0;
	//
	// switch (Anchor) {
	// // Graphics.TOP | Graphics.LEFT
	// case 20:
	// ixA = 0;
	// iyA = 0;
	// break;
	// // Graphics.TOP | Graphics.HCENTER
	// case 17:
	// ixA = width / 2;
	// iyA = 0;
	// break;
	// // Graphics.TOP | Graphics.RIGHT
	// case 24:
	// ixA = width;
	// iyA = 0;
	// break;
	// // Graphics.VCENTER | Graphics.LEFT
	// case 6:
	// ixA = 0;
	// iyA = height / 2;
	// break;
	// // Graphics.VCENTER | Graphics.HCENTER
	// case 3:
	// ixA = width / 2;
	// iyA = height / 2;
	// break;
	// // Graphics.VCENTER | Graphics.RIGHT
	// case 10:
	// ixA = width;
	// iyA = height / 2;
	// break;
	// // Graphics.BOTTOM | Graphics.LEFT
	// case 36:
	// ixA = 0;
	// iyA = height;
	// break;
	// // Graphics.BOTTOM | Graphics.HCENTER
	// case 33:
	// ixA = width / 2;
	// iyA = height;
	// break;
	// // Graphics.BOTTOM | Graphics.RIGHT
	// case 40:
	// ixA = width;
	// iyA = height;
	// break;
	// }
	//
	// x_dest -= ixA;
	// y_dest -= iyA;
	//
	// int ix = 0, iy = 0;
	//
	// switch (transform) {
	// case Sprite.TRANS_NONE: // 0
	// break;
	// case Sprite.TRANS_ROT90:
	// canvas.rotate(90, x_dest, y_dest);
	// iy = height;
	// break;
	// case Sprite.TRANS_ROT180:
	// canvas.rotate(180, x_dest, y_dest);
	// iy = height;
	// ix = width;
	// break;
	// case Sprite.TRANS_ROT270:
	// canvas.rotate(270, x_dest, y_dest);
	// ix = width;
	// break;
	// case Sprite.TRANS_MIRROR:
	// canvas.scale(-1, 1, x_dest, y_dest);
	// ix = width;
	// break;
	// case Sprite.TRANS_MIRROR_ROT90:
	// canvas.scale(-1, 1, x_dest, y_dest);
	// canvas.rotate(270, x_dest, y_dest);
	// ix = width;
	// iy = height;
	// break;
	// case Sprite.TRANS_MIRROR_ROT180:
	// canvas.scale(-1, 1, x_dest, y_dest);
	// canvas.rotate(180, x_dest, y_dest);
	// iy = height;
	// break;
	// case Sprite.TRANS_MIRROR_ROT270:
	// canvas.scale(-1, 1, x_dest, y_dest);
	// canvas.rotate(90, x_dest, y_dest);
	// break;
	// }
	// canvas.clipRect(x_dest - ix, y_dest - iy, x_dest - ix + width, y_dest
	// - iy + height);
	// canvas.drawBitmap(img.getBitmap(), x_dest - ix - x_src,
	// y_dest - iy - y_src, null);
	// canvas.restore();
	//
	// }
	// public void drawRegion(Image src, int x_src, int y_src, int width,
	// int height, int transform, int x_dest, int y_dest, int anchor) {
	// canvas.save();
	//
	// switch (anchor) {
	// case 20:// Graphics.TOP | Graphics.LEFT
	// break;
	// case 17:// Graphics.TOP | Graphics.HCENTER
	// x_dest -= (width >> 1);
	// break;
	// case 24:// Graphics.TOP | Graphics.RIGHT
	// x_dest -= width;
	// break;
	// case 6:// Graphics.VCENTER | Graphics.LEFT
	// y_dest -= (height >> 1);
	// break;
	// case 3:// Graphics.VCENTER | Graphics.HCENTER
	// x_dest -= (width >> 1);
	// y_dest -= (height >> 1);
	// break;
	// case 10: // Graphics.VCENTER | Graphics.RIGHT
	// x_dest -= width;
	// y_dest -= (height >> 1);
	// break;
	// case 36:// Graphics.BOTTOM | Graphics.LEFT
	// y_dest -= height;
	// break;
	// case 33:// Graphics.BOTTOM | Graphics.HCENTER
	// x_dest -= (width >> 1);
	// y_dest -= height;
	// break;
	// case 40:// Graphics.BOTTOM | Graphics.RIGHT
	// x_dest -= width;
	// y_dest -= height;
	// break;
	// }
	//
	// switch (transform) {
	// case Sprite.TRANS_NONE: // 0
	// break;
	// case Sprite.TRANS_ROT90:
	// canvas.rotate(90, x_dest, y_dest);
	// y_dest -= height;
	// break;
	// case Sprite.TRANS_ROT180:
	// canvas.rotate(180, x_dest, y_dest);
	// x_dest -= width;
	// y_dest -= height;
	// break;
	// case Sprite.TRANS_ROT270:
	// canvas.rotate(270, x_dest, y_dest);
	// x_dest -= width;
	// break;
	// case Sprite.TRANS_MIRROR:
	// canvas.scale(-1, 1, x_dest, y_dest);
	// x_dest -= width;
	// break;
	// case Sprite.TRANS_MIRROR_ROT90:
	// canvas.scale(-1, 1, x_dest, y_dest);
	// canvas.rotate(270, x_dest, y_dest);
	// x_dest -= width;
	// y_dest -= height;
	// break;
	// case Sprite.TRANS_MIRROR_ROT180:
	// canvas.scale(-1, 1, x_dest, y_dest);
	// canvas.rotate(180, x_dest, y_dest);
	// y_dest -= height;
	// break;
	// case Sprite.TRANS_MIRROR_ROT270:
	// canvas.scale(-1, 1, x_dest, y_dest);
	// canvas.rotate(90, x_dest, y_dest);
	// break;
	// }
	// srcRec.left = x_src;
	// srcRec.top = y_src;
	// srcRec.right = srcRec.left + width;
	// srcRec.bottom = srcRec.top + height;
	// dstRec.left = x_dest;
	// dstRec.top = y_dest;
	// dstRec.right = dstRec.left + width;
	// dstRec.bottom = dstRec.top + height;
	// this.canvas.drawBitmap(src.getBitmap(), srcRec, dstRec, null);
	// canvas.restore();
	// }
	public void drawRegionScale(Image img, float x_src, float y_src, float width, float height,
			int transform, float x_dest, float y_dest, int Anchor, float scale) {

		canvas.save();
		Bitmap b = null;
		try {
			if (y_src != 0.0f) {
				b = Bitmap.createBitmap(img.getBitmap(), (int) x_src, (int) y_src, (int) width,
						(int) height);
			} else {
				b = GameResource.instance.imgEffectIcon_TinNhan.getBitmap();
			}

		} catch (Exception e) {
			Log.i("x_src", x_src + "");
			Log.i("y_src", y_src + "");
			Log.i("width", width + "");
			Log.i("height", height + "");
			Log.i("img.getBitmap() - width", img.getBitmap().getWidth() + "");
			Log.i("img.getBitmap() - height", img.getBitmap().getHeight() + "");
		}

		// if (HDCGameMidlet.scale != 1)
		// b = BitmapResize(b, width / 2, height / 2);

		b = BitmapResize(b, width * scale, height * scale);

		ixA = 0;
		iyA = 0;
		width = width * scale;
		height = height * scale;
		switch (Anchor) {
		// Graphics.TOP | Graphics.LEFT
		case 20:
			ixA = 0;
			iyA = 0;
			break;
		// Graphics.TOP | Graphics.HCENTER
		case 17:
			ixA = width / 2;
			iyA = 0;
			break;
		// Graphics.TOP | Graphics.RIGHT
		case 24:
			ixA = width;
			iyA = 0;
			break;
		// Graphics.VCENTER | Graphics.LEFT
		case 6:
			ixA = 0;
			iyA = height / 2;
			break;
		// Graphics.VCENTER | Graphics.HCENTER
		case 3:
			ixA = width / 2;
			iyA = height / 2;
			break;
		// Graphics.VCENTER | Graphics.RIGHT
		case 10:
			ixA = width;
			iyA = height / 2;
			break;
		// Graphics.BOTTOM | Graphics.LEFT
		case 36:
			ixA = 0;
			iyA = height;
			break;
		// Graphics.BOTTOM | Graphics.HCENTER
		case 33:
			ixA = width / 2;
			iyA = height;
			break;
		// Graphics.BOTTOM | Graphics.RIGHT
		case 40:
			ixA = width;
			iyA = height;
			break;
		}
		x_dest -= ixA;
		y_dest -= iyA;
		ix = 0;
		iy = 0;

		switch (transform) {
		case Sprite.TRANS_NONE: // 0
			break;
		case Sprite.TRANS_ROT90:
			canvas.rotate(90, x_dest, y_dest);
			iy = height;
			break;
		case Sprite.TRANS_ROT180:
			canvas.rotate(180, x_dest, y_dest);
			iy = height;
			ix = width;
			break;
		case Sprite.TRANS_ROT270:
			canvas.rotate(270, x_dest, y_dest);
			ix = width;
			break;
		case Sprite.TRANS_MIRROR:
			canvas.scale(-1, 1, x_dest, y_dest);
			ix = width;
			break;
		case Sprite.TRANS_MIRROR_ROT90:
			canvas.scale(-1, 1, x_dest, y_dest);
			canvas.rotate(270, x_dest, y_dest);
			ix = width;
			iy = height;
			break;
		case Sprite.TRANS_MIRROR_ROT180:
			canvas.scale(-1, 1, x_dest, y_dest);
			canvas.rotate(180, x_dest, y_dest);
			iy = height;
			break;
		case Sprite.TRANS_MIRROR_ROT270:
			canvas.scale(-1, 1, x_dest, y_dest);
			canvas.rotate(90, x_dest, y_dest);
			break;
		}

		canvas.clipRect(x_dest - ix, y_dest - iy, x_dest - ix + width, y_dest - iy + height);
		canvas.drawBitmap(b, x_dest - ix, y_dest - iy, null);
		canvas.restore();
	}

	public void drawRegion(Image img, float x_src, float y_src, float width, float height,
			int transform, float x_dest, float y_dest, int Anchor) {

		canvas.save();

		ixA = 0;
		iyA = 0;
		switch (Anchor) {
		// Graphics.TOP | Graphics.LEFT
		case 20:
			ixA = 0;
			iyA = 0;
			break;
		// Graphics.TOP | Graphics.HCENTER
		case 17:
			ixA = width / 2;
			iyA = 0;
			break;
		// Graphics.TOP | Graphics.RIGHT
		case 24:
			ixA = width;
			iyA = 0;
			break;
		// Graphics.VCENTER | Graphics.LEFT
		case 6:
			ixA = 0;
			iyA = height / 2;
			break;
		// Graphics.VCENTER | Graphics.HCENTER
		case 3:
			ixA = width / 2;
			iyA = height / 2;
			break;
		// Graphics.VCENTER | Graphics.RIGHT
		case 10:
			ixA = width;
			iyA = height / 2;
			break;
		// Graphics.BOTTOM | Graphics.LEFT
		case 36:
			ixA = 0;
			iyA = height;
			break;
		// Graphics.BOTTOM | Graphics.HCENTER
		case 33:
			ixA = width / 2;
			iyA = height;
			break;
		// Graphics.BOTTOM | Graphics.RIGHT
		case 40:
			ixA = width;
			iyA = height;
			break;
		}

		x_dest -= ixA;
		y_dest -= iyA;

		ix = 0;
		iy = 0;

		switch (transform) {
		case Sprite.TRANS_NONE: // 0
			break;
		case Sprite.TRANS_ROT90:// 5
			canvas.rotate(90, x_dest, y_dest);
			iy = height;
			break;
		case Sprite.TRANS_ROT180:// 3
			canvas.rotate(180, x_dest, y_dest);
			iy = height;
			ix = width;
			break;
		case Sprite.TRANS_ROT270:// 6
			canvas.rotate(270, x_dest, y_dest);
			ix = width;
			break;
		case Sprite.TRANS_MIRROR:// 2
			canvas.scale(-1, 1, x_dest, y_dest);
			ix = width;
			break;
		case Sprite.TRANS_MIRROR_ROT90:// 7
			canvas.scale(-1, 1, x_dest, y_dest);
			canvas.rotate(270, x_dest, y_dest);
			ix = width;
			iy = height;
			break;
		case Sprite.TRANS_MIRROR_ROT180:// 1
			canvas.scale(-1, 1, x_dest, y_dest);
			canvas.rotate(180, x_dest, y_dest);
			iy = height;
			break;
		case Sprite.TRANS_MIRROR_ROT270:// 4
			canvas.scale(-1, 1, x_dest, y_dest);
			canvas.rotate(90, x_dest, y_dest);
			break;
		}
		canvas.clipRect(x_dest - ix, y_dest - iy, x_dest - ix + width, y_dest - iy + height);

		canvas.drawBitmap(img.getBitmap(), x_dest - ix - x_src, y_dest - iy - y_src, null);
		canvas.restore();

	}

	public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width,
			int height, boolean processAlpha) {
		this.canvas
				.drawBitmap(rgbData, offset, scanlength, x, y, width, height, processAlpha, null);
	}

	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		// Paint strokePaint = new Paint(this.paint);
		// strokePaint.setStyle(Style.STROKE);
		// strokePaint.setAntiAlias(true);
		// this.canvas.drawArc(new RectF(x, y, x + width, y + height),
		// startAngle,
		// arcAngle, true, strokePaint);

		paint.setStyle(Style.STROKE);
		this.canvas.drawArc(new RectF(x, y, x + width, y + height), startAngle, arcAngle, true,
				paint);
	}

	public void setStrokeStyle(int style) {
		// TODO
	}

	public void reset() {
		this.canvas.restore();
		this.tx = 0;
		this.ty = 0;
		this.canvas.save();
	}

	//
	public static Bitmap BitmapResize(Bitmap bitmap, float newWidth, float newHeight) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// calculate the scale - in this case = 0.4f
		float scaleWidth = (float) (newWidth / width);
		float scaleHeight = (float) (newHeight / height);

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		return resizedBitmap;
	}

	// public static Bitmap BitmapResize(Bitmap bitmap, float scaleW, float
	// scaleH) {
	//
	// int width = bitmap.getWidth();
	// int height = bitmap.getHeight();
	//
	//
	// // createa matrix for the manipulation
	// Matrix matrix = new Matrix();
	// // resize the bit map
	// matrix.postScale(scaleW, scaleH);
	//
	// // recreate the new Bitmap
	// Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
	// matrix, true);
	// return resizedBitmap;
	// }

	public void drawScaleImage(Image pixmap, float x, float y, int anchor, float newWidth,
			float newHeight) {

		int srcWidth = (int) newWidth, srcHeight = (int) newHeight;

		ix = x;
		iy = y;
		switch (anchor) {
		case TOP | LEFT:
			break;
		case TOP | RIGHT:
			ix = x - srcWidth;
			break;
		case TOP | HCENTER:
			ix = x - (srcWidth >> 1);
			break;
		case BOTTOM | LEFT:
			iy = y - srcHeight;
			break;
		case BOTTOM | RIGHT:
			ix = x - srcWidth;
			iy = y - srcHeight;
			break;
		case BOTTOM | HCENTER:
			ix = x - (srcWidth >> 1);
			iy = y - srcHeight;
			break;
		case VCENTER | LEFT:
			iy = y - (srcHeight >> 1);
			break;
		case VCENTER | RIGHT:
			ix = x - srcWidth;
			iy = y - (srcHeight >> 1);
			break;
		case VCENTER | HCENTER:
			ix = x - (srcWidth >> 1);
			iy = y - (srcHeight >> 1);
			break;
		}
		try {
			Bitmap b = BitmapResize(pixmap.getBitmap(), newWidth, newHeight);
			canvas.drawBitmap(b, ix, iy, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void drawRotateScaleImage(Image pixmap, float x, float y, int anchor, float newWidth,
			float newHeight, int rotate) {

		int srcWidth = (int) newWidth, srcHeight = (int) newHeight;

		ix = x;
		iy = y;
		switch (anchor) {
		case TOP | LEFT:
			break;
		case TOP | RIGHT:
			ix = x - srcWidth;
			break;
		case TOP | HCENTER:
			ix = x - (srcWidth >> 1);
			break;
		case BOTTOM | LEFT:
			iy = y - srcHeight;
			break;
		case BOTTOM | RIGHT:
			ix = x - srcWidth;
			iy = y - srcHeight;
			break;
		case BOTTOM | HCENTER:
			ix = x - (srcWidth >> 1);
			iy = y - srcHeight;
			break;
		case VCENTER | LEFT:
			iy = y - (srcHeight >> 1);
			break;
		case VCENTER | RIGHT:
			ix = x - srcWidth;
			iy = y - (srcHeight >> 1);
			break;
		case VCENTER | HCENTER:
			ix = x - (srcWidth >> 1);
			iy = y - (srcHeight >> 1);
			break;
		}
		try {
			Bitmap b = BitmapResize(pixmap.getBitmap(), newWidth, newHeight);
			this.canvas.save();
			this.canvas.rotate(rotate, x, y);
			this.canvas.drawBitmap(b, ix, iy, null);
			this.canvas.restore();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void drawImageOpacity(Image image, float x, float y, int anchor, int opacity) {
		// TODO Auto-generated method stub

		float ax = x;
		float ay = y;

		if ((anchor & RIGHT) != 0) {
			ax = x - image.getWidth();
		} else if ((anchor & HCENTER) != 0) {
			ax = x - image.getWidth() / 2;
		} else {
			ax = x;
		}
		if ((anchor & BOTTOM) != 0) {
			ay = y - image.getHeight();
		} else if ((anchor & VCENTER) != 0) {
			ay = y - image.getHeight() / 2;
		} else {
			ay = y;
		}

		canvas.drawBitmap(adjustOpacity(image.getBitmap(), opacity), ax, ay, null);
		paint.reset();
	}

	private Bitmap adjustOpacity(Bitmap bitmap, int opacity) {

		Bitmap mutableBitmap = /* bitmap.isMutable() ? bitmap : */bitmap.copy(
				Bitmap.Config.ARGB_8888, true);
		canvas1 = new Canvas(mutableBitmap);

		int colour = (opacity & 0xFF) << 24;

		canvas1.drawColor(colour, PorterDuff.Mode.DST_IN);
		return mutableBitmap;

	}

	public void drawImageDegree(Image pixmap, float x, float y, float degree, int anchor) {
		// TODO Auto-generated method stub
		try {
			float ax = x;
			float ay = y;
			float w = 0;
			float h = 0;

			if ((anchor & RIGHT) != 0) {
				ax = x - pixmap.getBitmap().getWidth();
			} else if ((anchor & HCENTER) != 0) {
				ax = x - pixmap.getBitmap().getWidth() / 2;
			} else {
				ax = x;
			}
			if ((anchor & BOTTOM) != 0) {
				ay = y - pixmap.getBitmap().getHeight();
			} else if ((anchor & VCENTER) != 0) {
				ay = y - pixmap.getBitmap().getHeight() / 2;
			} else {
				ay = y;
			}

			if ((anchor & HCENTER) != 0) {
				w = pixmap.getWidth() / 2;
			}
			if ((anchor & VCENTER) != 0) {
				h = pixmap.getHeight() / 2;
			}

			canvas.save();
			canvas.rotate(degree, ax + w, ay + h);

			canvas.drawBitmap(pixmap.getBitmap(), ax, ay, null);
			canvas.restore();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// vẽ hình Buổi tối
	public void drawImageNight(Image image, float x, float y) {
		canvas.drawBitmap(image.getBitmap(), x, y, null);
		paint.setColorFilter(new LightingColorFilter(0x14117e, 1));// 182f15
		canvas.drawBitmap(adjustOpacity(image.getBitmap(), -100), x, y, paint);

		paint.reset();
	}

	public void drawText(String text, float x, float y, Paint paint, int anchors) {
		if ((anchors & HCENTER) != 0) {
			align = Align.CENTER;
		} else if ((anchors & RIGHT) != 0) {
			align = Align.RIGHT;
		} else {
			align = Align.LEFT;
		}
		paint.setTextAlign(align);
		float offset = 0;
		if ((anchors & TOP) != 0) {
			offset = paint.getTextSize() - paint.descent();
		} else if ((anchors & BOTTOM) != 0) {
			offset = -paint.descent();
		} else if ((anchors & VCENTER) != 0) {
			offset = paint.getTextSize() / 2.0f - paint.descent();
		}

		paint.setFilterBitmap(true);

		// outer glow
		// Shader mShader = new LinearGradient(0, 0, 0, 20,/* new
		// int[]{Color.GREEN,Color.BLUE}*/Color.GRAY ,Color.WHITE /*new
		// float[]{0,1}*/, TileMode.CLAMP);
		// paint.setShader(mShader);

		// draw shadow
		// paint.setShadowLayer(0.5f, 0f, 0f, /*0xFF000000*/Color.YELLOW);

		// blurmask filter
		// paint.setColor(0x7FFFD4);
		// paint.setMaskFilter(new BlurMaskFilter(0.5f, Blur.OUTER));

		// paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
		// // paint.setShader(grad);
		// paint.setShader(new RadialGradient(8f, 80f, 90f, Color.RED,
		// Color.WHITE, Shader.TileMode.CLAMP));

		// canvas.drawText(text, x, y + offset, paint);

		// Paint mPaint = new Paint();
		// mPaint.set(paint);
		// mPaint.setColor(0x000000);
		// mPaint.setMaskFilter(new BlurMaskFilter(15 , Blur.OUTER));
		// canvas.drawText(text, x,y + offset, mPaint);

		// Paint mPaint = new Paint();
		// mPaint.set(paint);
		// mPaint.setColor(0x7FFFD4);
		// mPaint.setStyle(Paint.Style.STROKE);
		// mPaint.setStrokeWidth(4);
		//
		// canvas.drawText(text, x, y + offset, mPaint);
		canvas.drawText(text, x, y + offset, paint);
	}

	// TODO paint Clipping mask
	public void drawClippingMask(Image imgMask, int x, int y, int anchor) {

		float ax = x;
		float ay = y;

		if ((anchor & RIGHT) != 0) {
			ax = x - imgMask.getWidth();
		} else if ((anchor & HCENTER) != 0) {
			ax = x - imgMask.getWidth() / 2;
		} else {
			ax = x;
		}
		if ((anchor & BOTTOM) != 0) {
			ay = y - imgMask.getHeight();
		} else if ((anchor & VCENTER) != 0) {
			ay = y - imgMask.getHeight() / 2;
		} else {
			ay = y;
		}

		// LinearGradient grad = new LinearGradient(imgMask.getWidth()/2,
		// 0,imgMask.getWidth()/2, 20, Color.WHITE, 0X00FFFFFF,
		// Shader.TileMode.CLAMP);

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
		// paint.setShader(grad);
		paint.setShader(new RadialGradient(8f, 80f, 90f, Color.RED, Color.WHITE,
				Shader.TileMode.MIRROR));
		// paint.setMaskFilter(new BlurMaskFilter(15, Blur.INNER));
		this.canvas.drawBitmap(imgMask.getBitmap(), ax, ay, paint);

	}

	// TODO draw ellipse
	public void drawOval(float x, float y, float width, float height) {
		paint.setStyle(Paint.Style.STROKE);
		RectF recF = new RectF(x, y, x + width, y + height);
		canvas.drawOval(recF, paint);
	}

	// TODO draw Bitmap with Brightness
	public void drawImage_Brightness(Image image, float x, float y, float brightness, int anchor) {
		// color matrix
		ColorMatrix cm = new ColorMatrix();

		cm.set(new float[] { 1f, 0, 0, 0, brightness, 0, 1f, 0, 0, brightness, 0, 0, 1f, 0,
				brightness, 0, 0, 0, 1, 0 });

		paint.setColorFilter(new ColorMatrixColorFilter(cm));

		float ax = x;
		float ay = y;

		if ((anchor & RIGHT) != 0) {
			ax = x - image.getWidth();
		} else if ((anchor & HCENTER) != 0) {
			ax = x - image.getWidth() / 2;
		} else {
			ax = x;
		}
		if ((anchor & BOTTOM) != 0) {
			ay = y - image.getHeight();
		} else if ((anchor & VCENTER) != 0) {
			ay = y - image.getHeight() / 2;
		} else {
			ay = y;
		}

		this.canvas.drawBitmap(image.getBitmap(), ax, ay, paint);

	}

}
