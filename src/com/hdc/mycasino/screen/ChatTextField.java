package com.hdc.mycasino.screen;

import com.danh.standard.Graphics;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.TField;

public class ChatTextField {

	private static ChatTextField instance;
	public TField tfChat;
	public boolean isShow = false;
	IChatable parentScreen;
	long lastTimeChat;
	public Command left = new Command(GameResource.close, 0, new IAction() {

		public void perform() {
			tfChat.setText("");
			isShow = false;
		}
	});
	public Command center = new Command(GameResource.chat, 0, new IAction() {

		public void perform() {
			long now = System.currentTimeMillis();
			if (now - lastTimeChat < 2000) {
				return;
			}
			if (parentScreen != null) {
				parentScreen.onChatFromMe(tfChat.getText());
				tfChat.setText("");
				isShow = false;
				lastTimeChat = now;
			}
		}
	});
	public Command right = new Command(GameResource.del, 0, new IAction() {

		public void perform() {
			tfChat.clear();
			if (tfChat.getText().equals("")) {
				isShow = false;
			}
		}
	});

	public void keyPressed(int keyCode) {
		if (isShow) {
			// tfChat.keyPressed(keyCode);
		}
		if (tfChat.getText().equals("")) {
			isShow = false;
		}
	}

	public static ChatTextField gI() {
		return instance == null ? instance = new ChatTextField() : instance;
	}

	protected ChatTextField() {
		tfChat = new TField();
		tfChat.x = 0;
		tfChat.y = GameCanvas.h - GameCanvas.hBottomBar - 22;

		tfChat.width = GameCanvas.w;
		tfChat.height = Screen.ITEM_HEIGHT + 2;
		tfChat.isFocus = true;
		tfChat.setMaxTextLenght(40);
	}

	public void startChat(IChatable parentScreen) {
		// tfChat.keyPressed(firstCharacter);
		// System.out.println("xxxxxxxxxxxxxxxxxxx");
		// if (!tfChat.getText().equals("")) {
		// this.parentScreen = parentScreen;
		// isShow = true;
		// }
		isShow = !isShow;
		if (isShow) {
			this.parentScreen = parentScreen;
		}
	}

	public void update() {
		if (!isShow) {
			return;
		}

		tfChat.update();
	}

	public void paint(Graphics g) {
		if (!isShow) {
			return;
		}

		tfChat.y = GameCanvas.h - GameCanvas.hBottomBar - 22;
		tfChat.paint(g);
	}
}
