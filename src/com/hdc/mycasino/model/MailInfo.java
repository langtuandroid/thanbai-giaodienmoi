package com.hdc.mycasino.model;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.screen.ListScr;
import com.hdc.mycasino.screen.Screen;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.TField;

public class MailInfo extends MyObj {
	public static byte PLAN_TEXT = 0;
	public static byte REQUEST_MAKE_FRIEND = 1;

	public ListScr parentScr;
	public String sender;
	public String content;
	public String time;
	public byte isRead;
	public int type = 0;
	private Command readPlainTextMessage;
	private Command readRequestMakeFriendMessage;

	@Override
	public void paintInfo_Item(Graphics g, int x, int y, int width, int height, MyObj myObj,
			int type, int m_widthItem) {
		// TODO Auto-generated method stub

	}

	public MailInfo(final ListScr parentScr) {
		this.parentScr = parentScr;

		readPlainTextMessage = new Command(GameResource.read, new IAction() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void perform() {
				final MailInfo messageInfo = (MailInfo) parentScr.getSelectItems();
				if (messageInfo != null) {
					if (messageInfo.isRead == 0) {
						GlobalService.sendMessageIsReadMessage(PLAN_TEXT, messageInfo.itemId);
						messageInfo.isRead = 1;
					}

					if (Screen.numberUnreadMail > 0) {
						Screen.numberUnreadMail--;
					}

					Vector vt = new Vector();

					if (!messageInfo.sender.toLowerCase().equals("system")
							&& !messageInfo.sender.toLowerCase().equals(
									HDCGameMidlet.m_myPlayerInfo.itemName.toLowerCase())) {
						vt.addElement(new Command("Trả lời", new IAction() {
							public void perform() {
								showDialogMessanger();
							}
						}));
					}

					vt.addElement(new Command(GameResource.close, new IAction() {
						public void perform() {
							GameCanvas.endDlg();
						}
					}));

					GameCanvas.startMsgDlg("Từ: " + messageInfo.sender + "   " + messageInfo.time
							+ "\n" + messageInfo.content, vt);
					vt = null;
				}
			}
		});

		readRequestMakeFriendMessage = new Command(GameResource.read, new IAction() {
			@SuppressWarnings("rawtypes")
			public void perform() {
				final MailInfo messageInfo = (MailInfo) parentScr.getSelectItems();
				if (messageInfo != null) {
					if (messageInfo.isRead == 0) {
						GlobalService.sendMessageIsReadMessage(REQUEST_MAKE_FRIEND,
								messageInfo.itemId);
						messageInfo.isRead = 1;
					}

					if (Screen.numberUnreadMail > 0) {
						Screen.numberUnreadMail--;
					}

					Vector vt = new Vector();
					vt.addElement(new Command(GameResource.accept, new IAction() {
						public void perform() {
							GlobalService.sendMessageAcceptRequestMakeFriend(messageInfo.sender);
							GameCanvas.startWaitDlg();
						}
					}));

					vt.addElement(new Command(GameResource.deni, new IAction() {
						public void perform() {
							GlobalService.sendMessageDenyRequestMakeFriend(messageInfo.sender);
							GameCanvas.startWaitDlg();
						}
					}));

					vt.addElement(new Command(GameResource.close, new IAction() {
						public void perform() {
							GameCanvas.endDlg();
						}
					}));

					GameCanvas.startMsgDlg("Từ: " + messageInfo.sender + "   " + messageInfo.time
							+ "\n" + messageInfo.content, vt);
					vt = null;
				}
			}
		});
	}

	public void paintIcon(Graphics g, int x, int y) {
	}

	@Override
	public void paintItem(Graphics g, float x, float y, int m_IdFrame, int select,
			FrameImage m_frame) {
		// TODO Auto-generated method stub

	}

	public void paintInRow(Graphics g, int x, int y, int width, int height) {
		if (type == PLAN_TEXT) {
			if (isRead == 0) {
				// GameResource.instance.imgIconMail.drawFrame(1, x + 5, y + 3,
				// Graphics.LEFT|Graphics.TOP, g);

				GameResource.instance.m_frameMail_HomThu.drawFrame(0, x, y
						+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 2,
						Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);
			} else {
				// GameResource.instance.imgIconMail.drawFrame(0, x + 5, y + 3,
				// Graphics.LEFT|Graphics.TOP, g);

				GameResource.instance.m_frameMail_HomThu.drawFrame(0, x, y
						+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 2,
						Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);
			}
		}

		if (type == REQUEST_MAKE_FRIEND) {
			if (isRead == 0) {
				// GameResource.instance.imgMakeFriend.drawFrame(1, x + 2, y +
				// 3, Graphics.LEFT|Graphics.TOP, g);

				GameResource.instance.m_frameMail_HomThu.drawFrame(1, x, y
						+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 2,
						Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);
			} else {
				// GameResource.instance.imgMakeFriend.drawFrame(0, x + 2, y +
				// 3, Graphics.LEFT|Graphics.TOP, g);

				GameResource.instance.m_frameMail_HomThu.drawFrame(1, x, y
						+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 2,
						Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);
			}
		}

		// TODO paint sender
		BitmapFont.drawNormalFont(g, GameResource.from + sender, x
				+ GameResource.instance.m_frameMail_HomThu.frameWidth * 2, y
				+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 3, 0xC1CDCD,
				Graphics.LEFT | Graphics.VCENTER);

		// TODO paint time
		String[] m_Time = time.trim().split(" ");

		for (int i = 0; i < m_Time.length; i++) {
			BitmapFont.drawNormalFont(g, m_Time[i], /*
													 * GameCanvas.w / 4 * 3 +
													 * GameCanvas.w/8
													 */
					x - (60 / HDCGameMidlet.instance.scale)
							+ GameResource.instance.imgListScr_Panel.getWidth(),
					y + GameResource.instance.imgTabs_HightLightRow.getHeight() / 3 * (i + 1),
					0xC1CDCD, Graphics.RIGHT | Graphics.VCENTER);
		}

		// TODO paint content
		if (BitmapFont.m_bmFont.stringWidth(content) > GameCanvas.w2d3) {
			BitmapFont.drawNormalFont(g,
					BitmapFont.m_bmFont.splitFontBStrInLine(content, GameCanvas.hw)[0] + "...", x
							+ GameResource.instance.m_frameMail_HomThu.frameWidth * 2, y
							+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 3 * 2,
					0xe8980d, Graphics.LEFT | Graphics.VCENTER);
		} else {
			BitmapFont.drawNormalFont(g, content, x
					+ GameResource.instance.m_frameMail_HomThu.frameWidth * 2, y
					+ GameResource.instance.imgTabs_HightLightRow.getHeight() / 3 * 2, 0xe8980d,
					Graphics.LEFT | Graphics.VCENTER);
		}
	}

	public void paintInfo(Graphics g, int x, int y) {

	}

	public void focusItem() {
		if (parentScr != null) {
			if (type == PLAN_TEXT) {
				parentScr.m_cmdCenter = readPlainTextMessage;
			}

			if (type == REQUEST_MAKE_FRIEND) {
				parentScr.m_cmdCenter = readRequestMakeFriendMessage;
			}
		}
	}

	private void showDialogMessanger() {
		final MailInfo messageInfo = (MailInfo) parentScr.getSelectItems();
		GameCanvas.inputDlg.setInfo("Gửi tin nhắn tới " + messageInfo.sender, new IAction() {
			public void perform() {
				if (GameCanvas.inputDlg.tfInput.getText().length() > 0) {
					GlobalService.onSendMessageToUser(HDCGameMidlet.m_myPlayerInfo.itemName,
							messageInfo.sender, GameCanvas.inputDlg.tfInput.getText());
				} else
//					GameCanvas.startOK("Nhập văn bản cần gửi", new IAction() {
//						public void perform() {
//							showDialogMessanger();
//						}
//					});
				
					HDCGameMidlet.instance.showDialog_Okie_withCommand("Thông báo", "Nhập văn bản cần gửi",new IAction() {
						public void perform() {
							showDialogMessanger();
						}
					});
			}
		}, TField.INPUT_TYPE_ANY);
		GameCanvas.inputDlg.show();
	}
}
