package com.hdc.mycasino.network;

import java.io.IOException;

/**
 * MessageUtil is used to read and write data into entity Message.
 */
public class MessageIO {

	public static byte readByte(Message message) {
		try {
			return message.dis.readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static short readShort(Message message) {
		try {
			return message.dis.readShort();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static byte[] readBytes(Message message) {
		try {
			int len = message.dis.readShort();
			byte[] b = new byte[len];
			message.dis.read(b, 0, len);
			return b;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static long readLong(Message message) {
		try {
			return message.dis.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static int readInt(Message message) {
		try {
			return message.dis.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static boolean readBoolean(Message message) {
		try {
			return message.dis.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static char readChar(Message message) {
		try {
			return message.dis.readChar();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return '\0';
	}

	public static String readString(Message message) {
		try {
			return message.dis.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String[] readArrayString(Message message) {
		try {
			short len = message.dis.readShort();
			String arrayString[] = new String[len];
			for (int i = 0; i < len; i++) {
				arrayString[i] = message.dis.readUTF();
			}
			return arrayString;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static int[] readArrayInt(Message message) {
		try {
			int len = message.dis.readShort();
			int result[] = new int[len];
			for (int i = 0; i < len; i++) {
				result[i] = message.dis.readInt();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void writeBytes(byte[] buffer, int offset, int len, Message message) {
		try {
			message.dos.writeShort(len);
			message.dos.write(buffer, offset, len);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeByte(byte value, Message message) {
		try {
			message.dos.writeByte(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeBoolean(boolean value, Message message) {
		try {
			message.dos.writeBoolean(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeInt(int value, Message message) {
		try {
			message.dos.writeInt(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeShort(short value, Message message) {
		try {
			message.dos.writeShort(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeLong(long value, Message message) {
		try {
			message.dos.writeLong(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeChar(char value, Message message) {
		try {
			message.dos.writeChar(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeString(String s, Message message) {
		try {
			message.dos.writeUTF(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeArrayString(String[] s, Message message) {
		try {
			message.dos.writeShort(s.length);
			for (int i = 0; i < s.length; i++) {
				message.dos.writeUTF(s[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
