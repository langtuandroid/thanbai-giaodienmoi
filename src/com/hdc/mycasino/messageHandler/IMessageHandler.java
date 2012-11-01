package com.hdc.mycasino.messageHandler;

import com.hdc.mycasino.network.Message;

public interface IMessageHandler {

	public void onMessage(Message message);

	public abstract void onConnectionFail();

	public abstract void onDisconnected();

	public abstract void onConnectOK();

}
