package com.hdc.mycasino.screen;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * line string reader in J2ME
 * 
 * @author celon
 * 
 */
public class TextFileReader extends Reader {
	private InputStream is;
	private byte[] buffer = new byte[65535];
	private int index = buffer.length;
	private StringBuffer sb = new StringBuffer();
	private boolean fileEOF = false;
	private int scanEndIndex;
	private static final String UTF_ENCODE = "UTF-8";
	private static final char endChar = '\n';

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public TextFileReader(InputStream _is) {
		is = _is;
	}

	public void close() throws IOException {
		is.close();
	}

	/**
	 * never use it
	 */
	public int read(char[] cbuf, int off, int len) throws IOException {
		return 0;
	}

	/**
	 * read a line from text file, return null when get EOF
	 * 
	 * @return
	 * @throws IOException
	 */
	public String readLine() throws IOException {
		boolean endFound = false;
		while (!endFound) {
			// add string to buffer until found endChar
			if (!fileEOF) {
				scanEndIndex = buffer.length;
				if (index >= buffer.length) {
					// reload from file and reset index
					int bufferByteNum = readToBuffer();
					index = 0;
					if (bufferByteNum < buffer.length) {
						scanEndIndex = bufferByteNum;
						fileEOF = true;
					}
				}
			}
			for (; index < scanEndIndex; index++) {
				if (buffer[index] == endChar) {
					// found line end
					endFound = true;
					// skip endChar
					index++;
					return returnBuffer();
				} else {
					// append to buffer
					sb.append((char) (buffer[index]));
				}
			}
			if (endFound) {
				return returnBuffer();
			} else if (fileEOF) {
				return returnBuffer();
			}
		}
		return null;
	}

	private int readToBuffer() throws IOException {
		int result = is.read(buffer);
		return result;
	}

	private String returnBuffer() {
		if (sb.length() == 0)
			return null;
		byte[] data = new byte[sb.length()];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) (sb.charAt(i));
		}
		String result = null;
		try {
			result = new String(data, UTF_ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// clear buffer
		sb.delete(0, sb.length());
		return result;
	}
}
