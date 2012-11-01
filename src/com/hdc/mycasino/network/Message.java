package com.hdc.mycasino.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Message {

	public byte command;
	private ByteArrayOutputStream os = null;
	private ByteArrayInputStream is = null;
	public DataOutputStream dos = null;
	public DataInputStream dis = null;

	public Message(int command) {
		this.command = (byte) command;
		os = new ByteArrayOutputStream();
		dos = new DataOutputStream(os);
	}

	public byte getCommand() {
		return command;
	}

	Message(byte command, byte[] data) {
		this.command = command;
		is = new ByteArrayInputStream(data);
		dis = new DataInputStream(is);
	}

	public Message(byte command) {
		this.command = command;
		os = new ByteArrayOutputStream();
		dos = new DataOutputStream(os);
	}

	public byte[] getData() {
		return os.toByteArray();
	}

	public void release() {
		try {
			if (dis != null) {
				dis.close();
			}
			if (dos != null) {
				dos.close();
			}
		} catch (IOException e) {
		}
	}
}
