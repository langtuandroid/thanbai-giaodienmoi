package com.danh.standard;

import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Typeface;
import android.os.Build;

public class Font {
	public static final int FACE_SYSTEM = 0;
	public static final int FACE_MONOSPACE = 32;
	public static final int FACE_PROPORTIONAL = 64;

	public static final int FONT_STATIC_TEXT = 0;
	public static final int FONT_INPUT_TEXT = 1;

	public static final int SIZE_MEDIUM = 0;
	public static final int SIZE_SMALL = 8;
	public static final int SIZE_LARGE = 16;

	public static final int STYLE_PLAIN = 0;
	public static final int STYLE_BOLD = 1;
	public static final int STYLE_ITALIC = 2;
	public static final int STYLE_UNDERLINED = 4;

	//
	public static final float MEDIUM_FONT_SCALE = 1.2F;
	public static final float LARGE_FONT_SCALE = 1.4F;

	public static final boolean DEVICE_ANDROID_EMULATOR_FLAG = ("generic".equals(Build.BRAND) && "generic"
			.equals(Build.DEVICE));
	public static final boolean DEVICE_GOOGLE_NEXUS_1_FLAG = ("google".equals(Build.BRAND) && "passion"
			.equals(Build.DEVICE));
	public static final boolean DEVICE_HTC_DESIRE_Z_FLAG = ("htc_asia_wwe".equals(Build.BRAND) && "vision"
			.equals(Build.DEVICE));
	public static final boolean DEVICE_SAMSUNG_GT_I9000_FLAG = ("samsung".equals(Build.BRAND) && "GT-I9000"
			.equals(Build.DEVICE));
	public static final boolean DEVICE_UNKNOWN_FLAG = !(DEVICE_ANDROID_EMULATOR_FLAG
			|| DEVICE_GOOGLE_NEXUS_1_FLAG || DEVICE_HTC_DESIRE_Z_FLAG || DEVICE_SAMSUNG_GT_I9000_FLAG);
	public static final float DEVICE_FONT_SCALE = (1.0F
			+ (DEVICE_ANDROID_EMULATOR_FLAG ? 1.0F : 0.0F)
			+ (DEVICE_GOOGLE_NEXUS_1_FLAG ? 1.0F : 0.0F) + (DEVICE_HTC_DESIRE_Z_FLAG ? 1.0F : 0.0F)
			+ (DEVICE_SAMSUNG_GT_I9000_FLAG ? 1.5F : 0.0F) + (DEVICE_UNKNOWN_FLAG ? 1.0F : 0.0F));

	public static Font getFont(int fontSpecifier) {
		// TODO : this isn't actually tied to anything
		return new Font(Typeface.DEFAULT, SIZE_MEDIUM);
	}

	public static Font getDefaultFont() {
		return new Font(Typeface.DEFAULT, SIZE_MEDIUM);
	}

	public static Font getFont(int face, int style, int size) {
		Font font = new Font(size);
		return getFont(font, face, style, size);
	}

	protected static Font getFont(Font font, int face, int style, int size) {
		int paintFlags = 0;
		int typefaceStyle = Typeface.NORMAL;
		Typeface base;
		switch (face) {
		case FACE_MONOSPACE:
			base = Typeface.MONOSPACE;
			break;
		case FACE_SYSTEM:
			base = Typeface.DEFAULT;
			break;
		case FACE_PROPORTIONAL:
			base = Typeface.SANS_SERIF;
			break;
		default:
			throw new IllegalArgumentException("unknown face " + face);
		}
		if ((style & STYLE_BOLD) != 0) {
			typefaceStyle |= Typeface.BOLD;
		}
		if ((style & STYLE_ITALIC) != 0) {
			typefaceStyle |= Typeface.ITALIC;
		}
		if ((style & STYLE_UNDERLINED) != 0) {
			paintFlags |= Paint.UNDERLINE_TEXT_FLAG;
		}
		Typeface typeface = Typeface.create(base, typefaceStyle);
		Paint paint = new Paint(paintFlags);

		paint.setTypeface(typeface);
		font.setTypefacePaint(paint);
		return font;
	}

	private static Paint createPaint(Typeface typeface) {
		Paint paint = new Paint();
		paint.setTypeface(typeface);
		return paint;
	}

	private Paint typefacePaint;
	private FontMetricsInt fontMetrics;
	private int size;

	protected Font(int size) {
		this.size = size;
	}

	protected Font(Typeface typeface, int size) {
		this(createPaint(typeface), size);
	}

	protected Font(Paint typefacePaint, int size) {
		setTypefacePaint(typefacePaint);
		this.size = size;
	}

	public float getScale() {
		if (this.size == Font.SIZE_SMALL) {
			return DEVICE_FONT_SCALE;
		}
		if (this.size == Font.SIZE_MEDIUM) {
			return DEVICE_FONT_SCALE * MEDIUM_FONT_SCALE;
		}
		return DEVICE_FONT_SCALE * LARGE_FONT_SCALE;
	}

	public Paint getTypefacePaint() {
		return this.typefacePaint;
	}

	public void setTypefacePaint(Paint typefacePaint) {
		this.typefacePaint = typefacePaint;
		this.fontMetrics = typefacePaint.getFontMetricsInt();
	}

	public int charsWidth(char[] ch, int offset, int length) {
		return Math.round(this.typefacePaint.measureText(ch, offset, length) * this.getScale());
	}

	public int charWidth(char ch) {
		return this.charsWidth(new char[] { ch }, 0, 1);
	}

	public int getBaselinePosition() {
		return Math.round(-this.typefacePaint.ascent() * this.getScale());
	}

	public int getFace() {
		// TODO: work out the face
		return FACE_SYSTEM;
	}

	public int getHeight() {
		return Math.round(this.typefacePaint.getTextSize() * this.getScale());
	}

	public int getStyle() {
		int style = STYLE_PLAIN;
		Typeface typeface = this.typefacePaint.getTypeface();
		if (typeface.isBold()) {
			style |= STYLE_BOLD;
		}
		if (typeface.isItalic()) {
			style |= STYLE_ITALIC;
		}
		if (this.typefacePaint.isUnderlineText()) {
			style |= STYLE_UNDERLINED;
		}
		return style;
	}

	public boolean isBold() {
		return this.typefacePaint.getTypeface().isBold();
	}

	public boolean isItalic() {
		return this.typefacePaint.getTypeface().isItalic();
	}

	public boolean isPlain() {
		return this.getStyle() == STYLE_PLAIN;
	}

	public int getSize() {
		return this.size;
	}

	public boolean isUnderlined() {
		return this.typefacePaint.isUnderlineText();
	}

	public int stringWidth(String str) {
		return Math.round(this.typefacePaint.measureText(str) * this.getScale());
	}

	public int substringWidth(String str, int offset, int len) {
		return Math.round(this.typefacePaint.measureText(str, offset, len) * this.getScale());
	}
}
