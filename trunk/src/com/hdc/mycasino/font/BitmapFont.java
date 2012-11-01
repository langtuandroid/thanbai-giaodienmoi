/*
 * Copyright (c) 2005-2009 Sergey Tkachev http://sergetk.net
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hdc.mycasino.font;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.HDCGameMidlet;

/**
 * 
 * 
 * <p>
 * BitmapFont allows developers to use his own fonts in mobile applications.
 * </p>
 * 
 * <p>
 * This class includes a mixed set of methods of Graphics and Font classes. It
 * has character measurement methods from Font, such as stringWidth() and
 * charWidth(). It also has text drawing methods from Graphics, such as
 * drawString() and drawChar(). Text will be drawn by current Graphics color.
 * </p>
 * 
 * <p>
 * You may create your own font using Bitmap Character Editor. This is
 * crossplatform desktop application written on Java using SWT library. It can
 * be downloaded free from project site.
 * </p>
 * 
 * <p>
 * Each bitmap font consists of a set of parameters such as height, baseline
 * position etc, of character code map, of array width character widthes and of
 * principal part: one or more images in PNG format store character outlines.
 * </p>
 * 
 * <p>
 * By default all fonts are considered normal. Bold, italic and bold italic
 * styles are generated programmaticaly.
 * </p>
 * 
 * <p>
 * This code is a part of the <a
 * href="http://sourceforge.net/projects/mobilefonts">Mobile Fonts Project</a>.
 * </p>
 * 
 * <p>
 * Note: if this code was useful to you, write me please. I will be proud :)
 * </p>
 * 
 * @author Sergey Tkachev <a href="http://sergetk.net">http://sergetk.net</a>
 */
public class BitmapFont {
	public static BitmapFont m_bmNormalFont;
	public static BitmapFont m_bmFont;

	public static BitmapFont m_bmFontNumber;
	public static FontFacade m_facedeFont;
	public static FontFacade m_facedeFontNumber;

	public static Paint paint = new Paint();
	public static float textSize = 0;

	static {
		m_bmFont = new BitmapFont("arialFont.fnt");
		m_facedeFont = new FontFacade(m_bmFont);
		m_bmNormalFont = new BitmapFont("arialNormalFont.fnt");

		m_facedeFontNumber = new FontFacade(new BitmapFont("font_number.fnt"));
	}

	public BitmapFont(String fontName) {
		this(fontName, DEFAULT_COLOR_CACHE_CAPACITY);
	}

	public BitmapFont(String fontName, int colorCacheCapacity) {
		this.style = 0;
		this.currentColor = 0;
		try {
			InputStream input = HDCGameMidlet.assets.open(fontName);
			if (input == null) {
				throw new IOException();
			}
			DataInputStream data = new DataInputStream(input);

			imagesOffset = data.available() - 1;
			this.imageName = fontName;

			this.version = data.readByte();
			this.height = data.readByte();
			this.baseline = data.readByte();
			this.xIndent = data.readByte();
			this.yIndent = data.readByte();
			this.spaceWidth = data.readByte();

			characterMap = data.readUTF();

			int count = characterMap.length();

			// read characters widthes
			this.widthes = new int[count];
			this.x = new int[count];
			this.y = new int[count];
			this.idx = new int[count];

			for (int i = 0; i < count; i++) {
				widthes[i] = data.readByte();
			}

			// read font images
			int imagesCount = data.readByte();
			baseImages = new Image[imagesCount];
			byte[] buffer;
			for (int i = 0, imageLength; i < imagesCount; i++) {
				imageLength = data.readShort();
				buffer = new byte[imageLength];
				data.read(buffer, 0, imageLength);
				baseImages[i] = Image.createImage(buffer, 0, imageLength);

				imagesOffset -= imageLength + 2;
			}

			currentImages = baseImages;

			// calculate characters coordinates
			int curX = 0, curY = 0, curIdx = 0;
			int curImageWidth = currentImages[0].getWidth();
			int curImageHeight = currentImages[0].getHeight();

			for (int i = 0; i < count; i++) {
				if (widthes[i] < 0) {
					// negative width points to another character
					int sourceIndex = -widthes[i];
					widthes[i] = widthes[sourceIndex];
					x[i] = x[sourceIndex];
					y[i] = y[sourceIndex];
					idx[i] = idx[sourceIndex];
				} else {
					if (curX + widthes[i] > curImageWidth) {
						curX = 0;
						curY += height;
						if (curY > curImageHeight) {
							curY = 0;
							curIdx++;
							curImageWidth = currentImages[curIdx].getWidth();
							curImageHeight = currentImages[curIdx].getHeight();
						}
					}

					x[i] = curX;
					y[i] = curY;
					idx[i] = curIdx;
					curX += widthes[i];
				}
			}
			colorCache = new int[colorCacheCapacity];
			colorUsage = new int[colorCacheCapacity];
			imageCache = new Image[colorCacheCapacity][];

			if (defaultFont == null) {
				defaultFont = this;
			}
		} catch (IOException e) {
		}
	}

	public static void drawMoney(Graphics g, String text, int x, int y, int anchors, int color) {
		g.setColor(color);
		m_facedeFontNumber.drawOutlinedString(g, text, x, y, anchors);
	}

	public static void drawMoneyMini(Graphics g, String text, int x, int y, int anchors, int color) {
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		paint.setColor(0xFF000000 | color);
		paint.setTypeface(Typeface.SERIF);
		setTextSize(8f);
		m_bmNormalFont.drawString(g, text, x, y, paint, anchors);
	}

	public static void drawBoldFont(Graphics g, String text, float x, float y, int color, int anchors) {
		// paint.setAntiAlias(true);
		// paint.setFakeBoldText(true);
		// paint.setColor(0xFF000000 | color);
		// paint.setTypeface(Typeface.create("serif", Typeface.BOLD));
		// paint.setTextSize(14.0f/HDCGameMidlet.instance.scale);
		//
		// m_bmFont.drawString(g, text, x, y, paint, anchors);

		paint.setColor(0xFF000000 | color);
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		setTextSize(HDCGameMidlet.scale != 1 ? 9.8f : 18.50f);

		paint.setTypeface(Typeface.create("serif", Typeface.BOLD));
		// paint.setTypeface(Typeface.SERIF);

		m_bmFont.drawString(g, text, x, y, paint, anchors);
	}

	public static void drawBoldFont_1(Graphics g, String text, float x, float y, int color, int anchors) {
		// paint.setAntiAlias(true);
		// paint.setFakeBoldText(true);
		// paint.setColor(0xFF000000 | color);
		// paint.setTypeface(Typeface.create("serif", Typeface.BOLD));
		// paint.setTextSize(14.0f/HDCGameMidlet.instance.scale);
		//
		// m_bmFont.drawString(g, text, x, y, paint, anchors);

		paint.setColor(0xFF000000 | color);
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		paint.setTypeface(Typeface.create("serif", Typeface.BOLD));
		m_bmFont.drawString(g, text, x, y, paint, anchors);
	}

	// TODO rút gọn chuỗi
	public static String opt_String(String str, int length) {
		String m_String = "";
		if (str.length() > length)
			m_String = str.substring(0, length) + " ... ";
		else
			m_String = str;

		return m_String;
	}

	public static void drawNormalFont(Graphics g, String text, float x, float y, int color, int anchors) {
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		// paint.setUnderlineText(true);
		// paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
		paint.setColor(0xFF000000 | color);
		paint.setTypeface(Typeface.SERIF);
		setTextSize(20.0f / HDCGameMidlet.scale);

		m_bmNormalFont.drawString(g, text, x, y, paint, anchors);
	}

	// TODO draw text italic
	public static void drawItalicFont(Graphics g, String text, float x, float y, int color, int anchors) {
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
		paint.setColor(0xFF000000 | color);
		setTextSize(20.0f / HDCGameMidlet.scale);

		m_bmNormalFont.drawString(g, text, x, y, paint, anchors);
	}

	// TODO draw text italic
	public static void drawItalicFont_1(Graphics g, String text, float x, float y, int color, int anchors) {
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
		paint.setColor(0xFF000000 | color);

		m_bmNormalFont.drawString(g, text, x, y, paint, anchors);
	}

	// TODO draw underline
	public static void drawUnderlineFont(Graphics g, String text, float x, float y, int color, int anchors) {
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		paint.setUnderlineText(true);
		paint.setColor(0xFF000000 | color);
		paint.setTypeface(Typeface.SERIF);
		setTextSize(20.0f / HDCGameMidlet.scale);

		m_bmNormalFont.drawString(g, text, x, y, paint, anchors);
		paint.setUnderlineText(false);
	}

	public static void drawFontMenu(Graphics g, String text, float x, float y, int color, int anchors) {
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		paint.setColor(0xFF000000 | color);
		paint.setTypeface(Typeface.SERIF);
		setTextSize(14.0f / HDCGameMidlet.scale);

		m_bmNormalFont.drawString(g, text, x, y, paint, anchors);
	}

	public static void drawNormalFont_1(Graphics g, String text, float x, float y, int color, int anchors) {
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		paint.setColor(0xFF000000 | color);
		paint.setTypeface(Typeface.SERIF);
		m_bmNormalFont.drawString(g, text, x, y, paint, anchors);
	}

	public static void setTextSize(float size) {
		if (size != textSize) {
			textSize = size;
			paint.setTextSize(size /* / HDCGameMidlet.instance.scale */);
		}
	}

	public static void drawOutlinedString(Graphics g, String text, int x, int y, int contentColor, int boundColor,
			int anchors) {
		g.setColor(contentColor);
		m_facedeFont.drawOutlinedString(g, boundColor, text, x, y, anchors);
	}

	public static void drawOutlinedString(Graphics g, String text, int x, int y, int contentColor, int anchors) {
		g.setColor(contentColor);
		m_facedeFont.drawOutlinedString(g, text, x, y, anchors);
	}

	public static void drawOutlinedString(Graphics g, String text, int x, int y, int red, int green, int blue,
			int anchors) {
		g.setColor(red, green, blue);
		m_facedeFont.drawOutlinedString(g, text, x, y, anchors);
	}

	public void drawString(Graphics g, String text, float x, float y, Paint paint, int anchors) {
		// y += BitmapFont.m_bmFont.getHeight();
		g.drawText(text, x, y, paint, anchors);
	}

	// ////////////////////////////////////////////////////////////

	private final static int DEFAULT_COLOR_CACHE_CAPACITY = 5;

	private String imageName;
	private Image[] baseImages;
	private Image[] currentImages;

	private int height;
	private int baseline;
	private int xIndent;
	private int yIndent;
	private int spaceWidth;

	private int style;
	private int currentColor;
	private int charWidthIncrement = 0;
	private int imagesOffset = 0;

	private String characterMap;
	private int[] widthes, x, y, idx;
	protected byte version;

	private int colorUsageCount;
	private int[] colorUsage;
	private int[] colorCache;
	private Image[][] imageCache;

	private boolean italic;
	private boolean bold;

	private static BitmapFont defaultFont;

	/**
	 * Gets the default font
	 * 
	 * @return the default font
	 */
	public static BitmapFont getDefault() {
		return defaultFont;
	}

	/**
	 * Set a font as default
	 * 
	 * @param font
	 *            the font
	 */
	public static void setDefault(BitmapFont font) {
		defaultFont = font;
	}

	/**
	 * Creates a new font from the resource.
	 * 
	 * @param fontName
	 *            the resource name
	 */
	/**
	 * Creates a new font from the resource. The capacity of the color cache
	 * defines maximum size of the color cache.
	 * 
	 * @param fontName
	 *            the resource name
	 * @param colorCacheCapacity
	 *            the maximum color cache size
	 */
	/**
	 * Gets the style of the font.
	 * 
	 * @return style
	 */
	public int getStyle() {
		return this.style;
	}

	/**
	 * Gets the standard height of a line of a text in this font.
	 * 
	 * @return the height in pixels
	 */
	public int getHeight() {
		return (int) paint.descent() - (int) paint.ascent();
	}

	/**
	 * Gets the index of the character.
	 * 
	 * @param c
	 *            the character
	 * @return the index of the character
	 */
	protected int charIndex(char c) {
		try {
			return characterMap.indexOf(c);
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}

	/**
	 * Gets the distance from the top of the text to the text baseline.
	 * 
	 * @return the baseline position in pixels
	 */
	public int getBaselinePosition() {
		return baseline;
	}

	/**
	 * Draws the specified string.
	 * 
	 * @param g
	 *            the graphics context
	 * @param text
	 *            the text to be drawn
	 * @param x
	 *            the x coordinate of the anchor point
	 * @param y
	 *            the y coordinate of the anchor point
	 * @param anchors
	 *            the anchor point for positioning of the text
	 * @return the x coordinate for the next string
	 */
	// public void drawString(Graphics g, String text, int x, int y, int
	// anchors) {
	// y += BitmapFont.m_bmFont.getHeight();
	// g.drawText(text, x, y, paint,anchors);
	// }

	/**
	 * Draws the specified substring.
	 * 
	 * @param g
	 *            the graphics context
	 * @param text
	 *            the text to be drawn
	 * @param offset
	 *            the index of a first character
	 * @param length
	 *            the number of characters
	 * @param x
	 *            the x coordinate of the anchor point
	 * @param y
	 *            the y coordinate of the anchor point
	 * @param anchors
	 *            the anchor point for positioning the text
	 * @return the x coordinate for the next string
	 */
	/* ================= Character measurement functions =============== */
	/**
	 * Gets the width of the specified character in this font.
	 * 
	 * @param c
	 *            the character to be measured
	 * @return the width of the character
	 */
	public int charWidth(char c) {
		if (c == ' ') {
			return spaceWidth + xIndent + charWidthIncrement;
		}
		int index = charIndex(c);
		if (index < 0) {
			return spaceWidth + xIndent + charWidthIncrement;
		} else {
			return widthes[index] + xIndent + charWidthIncrement;
		}
	}

	/**
	 * Gets the width of the characters, starting at the specified offset and
	 * for the specified number of characters (length).
	 * 
	 * @param ch
	 *            the array of characters
	 * @param offset
	 *            zero-based index of a first character
	 * @param length
	 *            the number of characters to measure
	 * @return the width in pixels
	 */
	public int charsWidth(char[] ch, int offset, int length) {
		int w = 0;
		for (int i = offset; i < offset + length; i++) {
			w += charWidth(ch[i]);
		}
		return w;
	}

	/**
	 * Gets the width of the string.
	 * 
	 * @param str
	 *            the String to be measured
	 * @return the width in pixels
	 */
	public int stringWidth(String na) {
		return (int) paint.measureText(na, 0, na.length());
	}

	/**
	 * Gets the width of the substring.
	 * 
	 * @param str
	 *            the string to be measured
	 * @param offset
	 *            zero-based index of a first character in the substring
	 * @param length
	 *            the number of characters to measure
	 * @return the length of the substring
	 */
	public int substringWidth(String str, int offset, int length) {
		/*
		 * int w = 0; for (int i = offset; i < offset + length; i++) { w +=
		 * charWidth(str.charAt(i)); } return w;
		 */
		return (int) paint.measureText(str, offset, length);
	}

	/* ================= Working with the PNG =============== */

	protected static final String PNG_SIGNATURE = "\u0089PNG\r\n\u001A\n";

	/**
	 * Loads images from the resource and replace palette chunks.
	 * 
	 * @param name
	 *            the name of the resource containing the image data in the PNG
	 *            format
	 * @param color
	 *            the color
	 * @return the created image
	 */
	/**
	 * Finds the specified chunk.
	 * 
	 * @param buffer
	 *            the byte array
	 * @param offset
	 *            the offset of the start of the data in the array
	 * @param chunk
	 *            the name of chunk (i.e. PLTE)
	 * @return the offset of chunk (-1 if chunk isn't present)
	 */
	private int getChunk(byte[] buffer, int offset, String chunk) {
		try {
			for (;;) {
				int dataLenght = getInt(buffer, offset);
				if (compareBytes(buffer, offset + 4, chunk)) {
					return offset;
				} else {
					offset += 4 + 4 + dataLenght + 4;
				}
			}
		} catch (Exception e) {
		}
		;
		return -1;
	}

	/**
	 * Compare byte sequence with string
	 * 
	 * @param buffer
	 *            the byte array
	 * @param offset
	 *            the offset of the start of the data in the array
	 * @param str
	 *            the string to compare with bytes in the buffer
	 * @return true if the buffer contains the string
	 */
	private boolean compareBytes(byte[] buffer, int offset, String str) {
		for (int i = 0; i < str.length(); i++) {
			if (((byte) (str.charAt(i))) != buffer[i + offset]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get the integer value from the four bytes. The most signified byte go
	 * first.
	 * 
	 * @param buffer
	 *            the byte array
	 * @param offset
	 *            the offset of the start of the data in the array
	 * @return the integer value
	 */
	private int getInt(byte[] buffer, int offset) {
		int result = buffer[offset++] << 24;
		result |= (buffer[offset++] << 16) & 0x00FF0000;
		result |= (buffer[offset++] << 8) & 0x0000FF00;
		result |= buffer[offset] & 0x000000FF;

		return result;
	}

	/**
	 * Set four bytes to the specified value. The most signified byte go first.
	 * 
	 * @param buffer
	 *            the byte array
	 * @param offset
	 *            the offset of the start of the data in the array
	 * @param value
	 *            the value to set
	 */
	private void setInt(byte[] buffer, int offset, int value) {
		buffer[offset++] = (byte) ((value & 0xFF000000) >>> 24);
		buffer[offset++] = (byte) ((value & 0x00FF0000) >>> 16);
		buffer[offset++] = (byte) ((value & 0x0000FF00) >>> 8);
		buffer[offset] = (byte) ((value & 0x000000FF));
	}

	/**
	 * Replaces black color in the palette chunk to the specified color.
	 * 
	 * @param buffer
	 *            the byte array
	 * @param offset
	 *            the offset of the start of the data in the array
	 * @param color
	 *            the color to replace
	 */
	private void colorizePalette(byte[] buffer, int offset, int color) {
		int dataLength = getInt(buffer, offset);
		int dataOffset = offset + 8;

		int r = (color & 0x00FF0000) >>> 16;
		int g = (color & 0x0000FF00) >>> 8;
		int b = (color & 0x000000FF);

		for (int i = 0, pR, pG, pB, brightness; i < dataLength / 3; i++) {
			pR = buffer[dataOffset + 0] & 0xFF;
			pG = buffer[dataOffset + 1] & 0xFF;
			pB = buffer[dataOffset + 2] & 0xFF;

			brightness = (pR + pG + pB) / 3;

			buffer[dataOffset++] = (byte) (r + (brightness * (255 - r)) / 255); // red
			buffer[dataOffset++] = (byte) (g + (brightness * (255 - g)) / 255); // green
			buffer[dataOffset++] = (byte) (b + (brightness * (255 - b)) / 255); // blue
		}

		int crc = crc32(buffer, offset + 4, dataLength + 4);
		setInt(buffer, offset + 8 + dataLength, crc);
	}

	/* CRC32 calculations */

	private static final int CRC32_POLYNOMIAL = 0xEDB88320;

	/**
	 * Calculates the CRC32 value. This functions doesn't use a table for
	 * reasons of memory saving.
	 * 
	 * @param buffer
	 *            the byte array
	 * @param offset
	 *            the offset of the start of the data in the array
	 * @param count
	 *            the count of bytes
	 * @return the CRC32 value
	 */
	protected static int crc32(byte buffer[], int offset, int count) {
		int crc = 0xFFFFFFFF;
		while (count-- != 0) {
			int t = (crc ^ buffer[offset++]) & 0xFF;
			for (int i = 8; i > 0; i--) {
				if ((t & 1) == 1) {
					t = (t >>> 1) ^ CRC32_POLYNOMIAL;
				} else {
					t >>>= 1;
				}
			}
			crc = (crc >>> 8) ^ t;
		}
		return crc ^ 0xFFFFFFFF;
	}

	public String[] splitFontBStrInLine(String st, int width) {
		Vector vector = new Vector();
		int i = 0, i1 = 0, w1 = 0, ispace = -1;
		char ch;
		while (i < st.length()) {
			ch = st.charAt(i);
			if (ch == ' ')
				ispace = i;
			w1 += stringWidth(String.valueOf(ch));

			if (w1 > width || ch == '\n') {
				if (ch != '\n' && ispace != -1)
					i = ispace;
				vector.addElement(st.substring(i1, i));

				if (st.charAt(i) == ' ' || ch == '\n')
					i++;
				i1 = i;
				w1 = 0;
				ispace = -1;
			} else
				i++;
		}
		if (i > i1)
			vector.addElement(st.substring(i1, i)); // end line

		String[] a = new String[vector.size()];
		for (i = 0; i < a.length; i++) {
			a[i] = (String) vector.elementAt(i);
		}

		// Log.i("splitFontBStrInLine", Integer.toString(a.length));

		return a;
	}

	public static String[] splitString(String _text, String _searchStr) {
		// String buffer to store str
		int count = 0, pos = 0;
		int searchStringLength = _searchStr.length();
		int aa = _text.indexOf(_searchStr, pos);
		while (aa != -1) {
			pos = aa + searchStringLength;
			aa = _text.indexOf(_searchStr, pos);
			count++;
		}
		String[] sb = new String[count + 1];
		// Search for search
		int searchStringPos = _text.indexOf(_searchStr);
		int startPos = 0;
		// Iterate to add string
		int index = 0;
		while (searchStringPos != -1) {
			sb[index] = _text.substring(startPos, searchStringPos);
			startPos = searchStringPos + searchStringLength;
			searchStringPos = _text.indexOf(_searchStr, startPos);
			index++;
		}
		sb[index] = _text.substring(startPos, _text.length());
		return sb;
	}

	public static String replace(String _text, String _searchStr, String _replacementStr) {
		// String buffer to store str
		StringBuffer sb = new StringBuffer();

		// Search for search
		int searchStringPos = _text.indexOf(_searchStr);
		int startPos = 0;
		int searchStringLength = _searchStr.length();

		// Iterate to add string
		while (searchStringPos != -1) {
			sb.append(_text.substring(startPos, searchStringPos)).append(_replacementStr);
			startPos = searchStringPos + searchStringLength;
			searchStringPos = _text.indexOf(_searchStr, startPos);
		}

		// Create string
		int a = _text.length();
		sb.append(_text.substring(startPos, a));

		return sb.toString();
	}

	public static String[] splitStrInLine(String src, int lineWidth) {
		Vector list = new Vector();
		int srclen = src.length();
		if (srclen <= 1) {
			return new String[] { src };
		}
		String tem = "";
		int start = 0, end = 0;
		while (true) {
			while (true) {

				if (getWidthOfWithSmiley(tem) > lineWidth) {

					if (end > 0) {
						end--;
					}
					break;
				}

				tem += src.charAt(end);
				end++;

				if (src.charAt(end) == '\n') {
					break;
				}

				if (end >= srclen - 1) {
					end = srclen - 1;
					break;
				}
			}
			if (end != srclen - 1 && (src.charAt(end + 1) != ' ')) {
				int endAnyway = end;
				while (true) {
					if (src.charAt(end + 1) == '\n') {
						break;
					}
					if (src.charAt(end + 1) == ' ' && src.charAt(end) != ' ') {
						break;
					}
					if (end == start) {
						break;
					}
					end--;
				}
				if (end == start) {
					end = endAnyway;
				}
			}
			String s = src.substring(start, end + 1);
			if (s.endsWith("\n")) {
				s = s.substring(0, s.length() - 1);
			}
			list.addElement(s);
			if (end == srclen - 1) {
				break;
			}
			start = end + 1;
			while (start != srclen - 1 && src.charAt(start) == ' ') {
				start++;
			}
			if (start == srclen - 1) {
				break;
			}
			end = start;
			tem = "";
		}
		String[] strs = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			strs[i] = (String) list.elementAt(i);
		}
		return strs;
	}

	public static int getWidthOfWithSmiley(String str) {
		int w = 0;
		for (int i = 0; i < str.length(); i++) {

			w += getWidthOf(str.charAt(i));

		}
		return w;
	}

	public static int getWidthOf(char c) {
		if (c >= 30000) {
			return 20;
		}
		return m_facedeFont.charWidth(c);
	}

	private static String getFirstStringbyWidth(String str, int width) {
		String previousString = "";

		for (int i = 1; i < str.length(); i++) {
			previousString = str.substring(0, i);
			if (BitmapFont.m_bmNormalFont.stringWidth(previousString) > width) {
				previousString = str.substring(0, i - 1);
				break;
			}
		}

		str = str.trim() + " ";
		if (str != null && str.length() > 0) {
			int firstSpaceIndex = str.indexOf(" ", 0);
			while (firstSpaceIndex < str.length()) {
				String subString = str.substring(0, firstSpaceIndex);
				if (BitmapFont.m_bmNormalFont.stringWidth(subString) < width) {
					firstSpaceIndex = str.indexOf(" ", firstSpaceIndex + 1);
					if (firstSpaceIndex < 0) {
						return subString;
					} else {
						previousString = subString;
					}
				} else {
					return previousString;
				}
			}
		}

		return "";
	}

	/**
	 * Split string by a character.
	 */
	public static String[] splitString(String str, int width, boolean isBoldFont) {
		BitmapFont bitmapFont = null;
		if (isBoldFont) {
			bitmapFont = BitmapFont.m_bmFont;
		} else {
			bitmapFont = BitmapFont.m_bmNormalFont;
		}

		String[] subInfo = BitmapFont.splitString(str, "\n");
		Vector vtTmp = new Vector();
		int i, j;
		String[] tmp;
		for (i = 0; i < subInfo.length; i++) {
			tmp = bitmapFont.splitFontBStrInLine(subInfo[i], width);
			for (j = 0; j < tmp.length; j++)
				vtTmp.addElement(tmp[j]);
			tmp = null;
		}

		String[] info = new String[vtTmp.size()];
		for (i = 0; i < vtTmp.size(); i++)
			info[i] = (String) vtTmp.elementAt(i);

		subInfo = null;
		vtTmp = null;
		tmp = null;
		bitmapFont = null;
		return info;
	}

	/**
	 * Separate a string into multi string by a @width.
	 */
	public static Vector splitStringByWidth(String str, int width, boolean isBoldFont) {
		Vector result = new Vector();
		width = width - 10;
		BitmapFont bitmapFont = null;
		if (isBoldFont) {
			bitmapFont = BitmapFont.m_bmFont;
		} else {
			bitmapFont = BitmapFont.m_bmNormalFont;
		}
		String subStr;
		while (bitmapFont.stringWidth(str) > width) {
			subStr = getFirstStringbyWidth(str, width);
			if (subStr.length() < str.length()) {
				str = str.substring(subStr.length() + 1, str.length());
				result.addElement(subStr);
			} else {
				break;
			}
		}
		subStr = null;
		result.addElement(str);

		return result;
	}

	public static boolean isHasSpecialCharacter(String p) {
		int count = 0;
		for (int i = 0; i < p.length(); i++) {
			char ch = p.charAt(i);
			if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
				count++;
			}
		}

		if (p.length() == count) {
			return false;
		} else {
			return true;
		}
	}

}
