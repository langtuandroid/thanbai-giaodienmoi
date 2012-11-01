package com.hdc.mycasino.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Vector;

import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.messageHandler.GlobalMsgHandler;

public class Session {

	protected static Session instance = new Session();

	public static Session gI() {
		return instance;
	}

	private DataOutputStream dos;
	public DataInputStream dis;
	public GlobalMsgHandler messageHandler;
	private Socket sc;
	public boolean connected, connecting;

	private final Sender sender = new Sender();

	public Thread initThread;
	public Thread collectorThread;

	public int sendByteCount;
	public int recvByteCount;
	public byte[] key = null;
	long timeConnected;
	public String strRecvByteCount = "";
	public static boolean isCancel;
	private final static int maxRetry = 15;
	StringWriter stringWriter = new StringWriter();
	private PrintWriter printWriter = new PrintWriter(stringWriter, true);

	public boolean isConnected() {
		return connected;
	}

	public void setHandler(GlobalMsgHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	public void connect(String ip, int port) {
		if (connected)
			return;
		sc = null;
		initThread = new Thread(new NetworkInit(ip, port));
		initThread.start();

	}

	public void sendMessage(Message message) {
		sender.AddMessage(message);
	}

	private synchronized void doSendMessage(Message m) throws IOException {
		byte[] data = m.getData();
		try {
			dos.writeByte(m.command);
			if (data != null) {
				int size = data.length;
				dos.writeShort(size);
				dos.write(data);
				sendByteCount += (5 + data.length);
			} else {
				dos.writeShort(0);
				sendByteCount += 5;
			}

			System.out.println("send " + m.command);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		cleanNetwork();
	}

	private void cleanNetwork() {
		key = null;
		try {
			connected = false;
			connecting = false;
			if (sc != null) {
				sc.close();
				sc = null;
			}
			if (dos != null) {
				dos.close();
				dos = null;
			}
			if (dis != null) {
				dis.close();
				dis = null;
			}
			collectorThread = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void connect(String iP, int pORT) {
	// String host = "socket://" + iP + ":" + pORT;
	// connect(host);
	// }

	class NetworkInit implements Runnable {

		private final String host;
		private final int port;

		NetworkInit(String host, int port) {
			this.host = host;
			this.port = port;
		}

		public void run() {
			isCancel = false;
			int i = 0;
			while (i < maxRetry && !connected) {
				try {
					doConnect(host, port);
					messageHandler.onConnectOK();
					break;
				} catch (Exception ex) {
					ex.printStackTrace();
					i++;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException ex1) {
						ex1.printStackTrace();
					}
				}
			}

			if (!connected && messageHandler != null) {
				close();
				messageHandler.onConnectionFail();
			}
		}

		public void doConnect(String ip, int port) throws Exception {
			// sc = new Socket("192.168.1.9", 3210);
			if (sc == null)
				sc = new Socket();
			sc.connect(new InetSocketAddress(ip, port));
			dos = new DataOutputStream(sc.getOutputStream());
			dis = new DataInputStream(sc.getInputStream());
			connected = true;
			new Thread(sender).start();
			collectorThread = new Thread(new MessageCollector());
			collectorThread.start();
			timeConnected = System.currentTimeMillis();
			connecting = false;
		}
	}

	private class Sender implements Runnable {

		private final Vector sendingMessages;

		public Sender() {
			sendingMessages = new Vector();
		}

		public void AddMessage(Message message) {
			sendingMessages.addElement(message);
		}

		public void run() {
			try {
				while (connected) {
					while (sendingMessages.size() > 0) {
						Message message = (Message) sendingMessages.elementAt(0);
						sendingMessages.removeElementAt(0);
						doSendMessage(message);
						message.release();
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class MessageCollector implements Runnable {
		public void run() {
			com.hdc.mycasino.network.Message message;
			try {
				while (isConnected()) {
					message = readMessage();
					if (message != null) {
						messageHandler.onMessage(message);
						message.release();
					} else {
						break;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace(printWriter);
				printWriter.flush();
				stringWriter.flush();
				HDCGameMidlet.instance.Toast(stringWriter.toString());
			}

			if (connected) {
				if (messageHandler != null) {
					if (System.currentTimeMillis() - timeConnected > 500) {
						messageHandler.onDisconnected();
					} else {
						messageHandler.onConnectionFail();
					}
				}
				if (sc != null) {
					cleanNetwork();
				}
			}
		}

		private Message readMessage() throws Exception {
			byte cmd = dis.readByte();
			int size;
			size = dis.readUnsignedShort();
			byte data[] = new byte[size];
			int len = 0;
			int byteRead = 0;
			while (len != -1 && byteRead < size) {
				len = dis.read(data, byteRead, size - byteRead);
				if (len > 0) {
					byteRead += len;
				}
			}
			recvByteCount += (5 + byteRead);
			int Kb = (Session.gI().recvByteCount + Session.gI().sendByteCount);
			strRecvByteCount = Kb / 1024 + "." + Kb % 1024 / 1024 + "Kb";
			Message msg = new Message(cmd, data);
			System.out.println("read " + cmd);
			return msg;
		}
	}
}
