package com.hdc.mycasino.screen;

import java.io.File;
import java.util.Vector;

import android.os.Environment;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.gif.GifView;
import com.hdc.mycasino.model.Button;
import com.hdc.mycasino.model.CardTest;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.Footer;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.FileManager;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;
import com.hdc.mycasino.utilities.TField;

public class LoginScr extends Screen {

	public TField tfUser;
	public TField tfPass;
	public TField tfRegPass;
	int focus;

	boolean isCheck = false, isRes = false, isChangePass = false;
	Command cmdLogin, cmdCheck, cmdRes, cmdChangePass;

	@SuppressWarnings("rawtypes")
	Vector m_arrEffect;

	// TODO Button
	Button mButton_Login;
	Button mButton_Register;
	Button mButton_Screen_Reg; // button cho màn hình đăng ký
	Button mButton_ChangePass; // button thay đổi mật khẩu

	// TODO biến sử dụng cho update
	// ánh sáng đèn
	int opacity = 255;
	int flag = 0;
	int time = 0;
	// Command screen register
	Command cmdScreenRegister;
	// flagScreen = 0 : màn hình đăng nhập
	// flagScreen = 1 : màn hình đăng ký
	// flagScreen = 2 : màn hình quên mật khẩu
	int flagScreen = 0;

	// TODO degree
	// int degree = -90;
	// int degree1 = -90;
	// int x1 = 100;
	int a = 100;
	int b = 200;
	int a1 = 100;
	int b1 = 200;
	int a2 = 100;
	int b2 = 200;
	int count = -1;

	// FOOTER
	Footer mFooter_QuenMK;
	Footer mFooter_XoaDuLieu;
	Footer mFooter_DienDan;
	Footer mFooter_CapNhat;
	Footer mFooter_GioiThieu;

	// TODO switch to me
	public void switchToMe() {
		super.switchToMe();

	}

	public void loadImg() {
		// if (GameResource.instance.imgFrames == null)
		// GameResource.instance.imgFrames = ImagePack
		// .createImage(ImagePack.FRAMES_PNG);
	}

	public void close() {
		HDCGameMidlet.destroy();
		System.exit(0);
	}

	Footer demo;

	@SuppressWarnings("rawtypes")
	public LoginScr() {
		// TODO create background
		createBackground();

		// TODO init Text field
		initTextField();

		// TODO load user && pass
		loadUser_Pass();

		// TODO init command
		initCommand(); 

		// TODO init button
		initButton();
		// /////
		// gf = new GifView(GameResource.instance.inputEmotion0);
		// gf.play();

		Command forgetPass = new Command("Quên mật khẩu", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				isChangePass = true;
				tfRegPass.isFocus = false;
				tfPass.isFocus = false;
				tfUser.isFocus = true;
				// m_cmdRight = tfUser.cmdClear;

				if (tfUser.y < tfPass.y) {
					int tmp = tfPass.y;
					tfPass.y = tfUser.y;
					tfUser.y = tmp;
				}

				resetTF();
				flagScreen = 2;

				// mBt_Defaut = mButton_ChangePass;
				// mBt_Defaut.count = 0;
				// mBt_Defaut.setButtonDefault(mButton_ChangePass);
				// setButtonDefault(mButton_ChangePass);
			}
		});

		Command forum = new Command("Forum", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				MainScr.gI().onReceivedForumLink("Diễn dàn", GameResource.doYouWantGoToForum,
						"http://m.taigamejava.com");
			}
		});

		Command update = new Command("Update", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				MainScr.gI().onReceivedForumLink("Cập nhật phiên bản",
						GameResource.doYouWantGoToUpdate, HDCGameMidlet.m_strLinkUpdateVersion);
			}
		});

		Command cleanCache = new Command("Clean cache", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub

			}
		});

		Command introduce = new Command("Introduce", new IAction() {

			@Override
			public void perform() {
				// TODO Auto-generated method stub
				// GameCanvas.startOKDlg("Vua Bài Version " +
				// HDCGameMidlet.version
				// + "- Công ty HDC\nCSKH : 04.66.844.524");
				HDCGameMidlet.instance.showDialog_Okie("Giới thiệu", "Vua Bài Version "
						+ HDCGameMidlet.version + "- Công ty HDC\nCSKH : 04.66.844.524");
			}
		});

		int x1 = GameCanvas.w / 5;
		int y1 = GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight();
		mFooter_QuenMK = new Footer(0, GameCanvas.h
				- GameResource.instance.imgHeaderTop.getHeight(), x1, y1, 0xffb901, 0xffffff,
				forgetPass, 9, 23, "QUÊN MẬT KHẨU");
		mFooter_DienDan = new Footer(x1, GameCanvas.h
				- GameResource.instance.imgHeaderTop.getHeight(), x1, y1, 0xffb901, 0xffffff,
				forum, 7, 21, "DIỄN ĐÀN");
		mFooter_CapNhat = new Footer(x1 * 2, GameCanvas.h
				- GameResource.instance.imgHeaderTop.getHeight(), x1, y1, 0xffb901, 0xffffff,
				update, 12, 26, "CẬP NHẬT");
		mFooter_XoaDuLieu = new Footer(x1 * 3, GameCanvas.h
				- GameResource.instance.imgHeaderTop.getHeight(), x1, y1, 0xffb901, 0xffffff,
				cleanCache, 4, 18, "XÓA DỮ LIỆU");
		mFooter_GioiThieu = new Footer(x1 * 4, GameCanvas.h
				- GameResource.instance.imgHeaderTop.getHeight(), x1, y1, 0xffb901, 0xffffff,
				introduce, 10, 24, "GIỚI THIỆU");
	}

	public static CardTest convertByteToCard(int value) {
		int cardVal;
		char cardType = 0;
		int temp = 0;
		cardVal = (int) ((value % 13) + 3);
		if (cardVal > 13) {
			cardVal = cardVal - 13;
		}
		temp = (int) value / 13;
		switch (temp) {
		case 0:
			cardType = 'B';
			break;
		case 1:
			cardType = 'T';
			break;
		case 2:
			cardType = 'R';
			break;
		case 3:
			cardType = 'C';
			break;
		}
		return new CardTest(cardType, cardVal);
	}

	// TODO create background
	private void createBackground() {
		// draw background to image buffer
		m_imgBackground = Image.createImage(GameCanvas.w, GameCanvas.h);
		Graphics g = m_imgBackground.getGraphics();
		// paint background
		PaintPopup.paintBackGround(g);
	}

	// TODO loadUser & Pass
	private void loadUser_Pass() {
		// save status
		String[] mString = FileManager.loadUserAndPass("user.txt");
		int a = Integer.parseInt(mString[0]);
		if (a == 0) {
			isCheck = true;
		} else {
			isCheck = false;
		}
		// load username and pass
		tfUser.setText(mString[1]);
		tfPass.setText(mString[2]);
		resetTF();
		focus = 0;
	}

	// TODO init text field
	private void initTextField() {
		// init text field user
		tfUser = new TField();
		tfUser.y = (GameCanvas.h - 7 * GameResource.instance.imgTextField_Disable.getHeight()) / 2
				+ GameResource.instance.imgTextField_Disable.getHeight();
		tfUser.width = GameResource.instance.imgTextField_Disable.getWidth();
		tfUser.height = GameResource.instance.imgTextField_Disable.getHeight();
		tfUser.isFocus = true;
		tfUser.setIputType(TField.INPUT_ALPHA_NUMBER_ONLY);

		// TField.m = HDCGameMidlet.instance;
		// TField.c = GameCanvas.instance;

		// init text field pass
		tfPass = new TField();
		tfPass.y = tfUser.y + GameResource.instance.imgTextField_Disable.getHeight() * 2;
		tfPass.width = GameResource.instance.imgTextField_Disable.getWidth();
		tfPass.height = GameResource.instance.imgTextField_Disable.getHeight();
		tfPass.isFocus = false;
		tfPass.setIputType(TField.INPUT_TYPE_PASSWORD);

		// init text field register
		tfRegPass = new TField();
		tfRegPass.y = tfPass.y + GameResource.instance.imgTextField_Disable.getHeight() * 2;
		tfRegPass.width = GameResource.instance.imgTextField_Disable.getWidth();
		tfRegPass.height = GameResource.instance.imgTextField_Disable.getHeight();
		tfRegPass.isFocus = false;
		tfRegPass.setIputType(TField.INPUT_TYPE_PASSWORD);
	}

	// TODO init Command
	private void initCommand() {
		cmdLogin = new Command(GameResource.signIn, new IAction() {

			public void perform() {
				// degree = -90;
				// degree1 = -90;
				// x1 = 100;
				// a = 100;
				// b = 200;
				// a1 = 100;
				// b1 = 200;
				// a2 = 100;
				// b2 = 200;
				// count = -1;
				doLogin();
				// HDCGameMidlet.instance.CustomDialog();
			}
		});

		cmdCheck = new Command(GameResource.remember, new IAction() {
			public void perform() {
				if (isCheck) {
					isCheck = false;
				} else {
					isCheck = true;
				}
			}
		});

		m_cmdLeft = new Command(GameResource.menu, new IAction() {
			public void perform() {
				doMenu();
			}
		});

		cmdRes = new Command(GameResource.register, new IAction() {
			public void perform() {
				if (checkInfo())
					doRegister();
			}
		});

		cmdChangePass = new Command(GameResource.forgetPass, new IAction() {
			public void perform() {
				doResetPass();
			}
		});

		m_cmdCenter = cmdLogin;

		// command screen register
		cmdScreenRegister = new Command(GameResource.register, 0, new IAction() {

			public void perform() {
				isRes = true;
				isChangePass = false;

				tfRegPass.isFocus = false;
				tfPass.isFocus = false;
				tfUser.isFocus = true;

				if (tfUser.y > tfPass.y) {
					int tmp = tfPass.y;
					tfPass.y = tfUser.y;
					tfUser.y = tmp;
				}

				resetTF();

				flagScreen = 1;

				// mBt_Defaut = mButton_Register;
				// mBt_Defaut.count = 0;
				// mBt_Defaut.setButtonDefault(mButton_Register);
				// setButtonDefault(mButton_Register);

				// mButton_Register.setButtonDefaut(true);
			}
		});
	}

	// TODO init Button
	@SuppressWarnings("static-access")
	private void initButton() {
		mButton_Login = new Button(GameResource.instance.imgButton_Login,
				GameResource.instance.signIn, cmdLogin);
		mButton_Login.setXY(tfUser.x, tfRegPass.y);

		// setButtonDefault(mButton_Login);

		// button register
		mButton_Screen_Reg = new Button(GameResource.instance.imgButton_Login,
				GameResource.instance.register, cmdScreenRegister);
		mButton_Screen_Reg.setXY(tfUser.x + GameResource.instance.imgTextField_Disable.getWidth()
				/ 2, tfRegPass.y);

		mButton_Register = new Button(GameResource.instance.imgButton_Login,
				GameResource.instance.register, cmdRes);
		mButton_Register.setXY(tfUser.x
				+ (GameResource.instance.imgTextField_Disable.getWidth() - mButton_Register.w) / 2,
				tfRegPass.y + GameResource.instance.imgTextField_Disable.getHeight() * 2
						- GameResource.instance.imgButton_Login.getHeight());

		// button change pass
		mButton_ChangePass = new Button(GameResource.instance.imgButton_Login,
				GameResource.instance.changePass, cmdChangePass);
		mButton_ChangePass
				.setXY(tfUser.x
						+ (GameResource.instance.imgTextField_Enable.getWidth() - mButton_ChangePass.w)
						/ 2, tfRegPass.y);
	}

	// TODO resert pass
	private void doResetPass() {
		if (tfUser.getText().equals("") || tfUser.getText().length() == 0) {
			// GameCanvas.startOKDlg(GameResource.plzInputInfo);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", GameResource.plzInputInfo);
			return;
		}
		GameCanvas.startWaitDlg();
		HDCGameMidlet.Key = "123455";
		HDCGameMidlet.instance.doConnect();
		if (!HDCGameMidlet.Key.equals("")) {
			GlobalService.onSetPass(tfUser.getText());
		}
	}

	// TODO register
	public void createRegister() {
		// isRes = true;
		isChangePass = false;
		tfRegPass.isFocus = false;
		tfPass.isFocus = false;
		tfUser.isFocus = true;

		if (tfUser.y > tfPass.y) {
			int tmp = tfPass.y;
			tfPass.y = tfUser.y;
			tfUser.y = tmp;
		}
		resetTF();
	}

	// TODO back login screen
	public void doBackLoginScreen() {
		// return login screen
		isRes = false;
		tfRegPass.isFocus = false;
		tfPass.isFocus = false;
		tfUser.isFocus = true;
		resetTF();
	}

	// TODO check info
	private boolean checkInfo() {
		if (tfUser.getText().equals("") || tfUser.getText().length() == 0) {
			// GameCanvas.startOKDlg("Vui lòng nhập Tên");
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Vui lòng nhập Tên");
			return false;
		}

		if (tfPass.getText().equals("") || tfPass.getText().length() == 0) {
			// GameCanvas.startOKDlg("Vui lòng nhập Mật khẩu");
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Vui lòng nhập Mật khẩu");
			return false;
		}

		if (isRes && (tfRegPass.getText().equals("") || tfRegPass.getText().length() == 0)) {
			// GameCanvas.startOKDlg("Vui lòng nhập Mật khẩu");
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Vui lòng nhập Mật khẩu");
			return false;
		}

		if (tfUser.getText().length() > 15) {
			// GameCanvas.startOKDlg("Tên đăng nhập không được quá 15 kí tự");
			HDCGameMidlet.instance.showDialog_Okie("Thông báo",
					"Tên đăng nhập không được quá 15 kí tự");
			return false;
		}

		if (tfPass.getText().length() > 100) {
			// GameCanvas.startOKDlg("Mật khẩu không được vượt quá 100 kí tự");
			HDCGameMidlet.instance.showDialog_Okie("Thông báo",
					"Mật khẩu không được vượt quá 100 kí tự");
			return false;
		}

		if (tfPass.getText().length() < 5) {
			// GameCanvas.startOKDlg("Mật khẩu không được dưới 5 kí tự");
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Mật khẩu không được dưới 5 kí tự");
			return false;
		}

		for (int i = 0; i < tfUser.getText().length(); i++) {
			char ch = tfUser.getText().charAt(i);
			if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
					|| ch == '_') {
			} else {
				// GameCanvas.startOKDlg("Tên đăng nhập không được có ký tự đặc biệt");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo",
						"Tên đăng nhập không được có ký tự đặc biệt");
				return false;
			}
		}

		return true;
	}

	// TODO do register
	protected void doRegister() {
		if (tfUser.getText().equals("") || tfPass.getText().equals("")
				|| tfRegPass.getText().equals("")) {
			// GameCanvas.startOKDlg(GameResource.plzInputInfo);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", GameResource.plzInputInfo);
			return;
		}
		char[] ch = tfUser.getText().toCharArray();
		int a = ch.length;
		for (int i = 0; i < a; i++) {
			if (!TField.setNormal(ch[i])) {
				// GameCanvas.startOKDlg(GameResource.specialCharNotAllow);
				HDCGameMidlet.instance.showDialog_Okie("Thông báo",
						GameResource.specialCharNotAllow);
				return;
			}
		}

		if (!tfPass.getText().equals(tfRegPass.getText())) {
			// GameCanvas.startOKDlg(GameResource.incorrectPass);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", GameResource.incorrectPass);
			return;
		}
		HDCGameMidlet.Key = "123455";
		HDCGameMidlet.instance.doConnect();
		if (!HDCGameMidlet.Key.equals("")) {
			GlobalService.onRegister(tfUser.getText(), tfPass.getText());
		}
	}

	// TODO show menu
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doMenu() {
		Vector menu = new Vector();
		if (isRes) {
			menu.addElement(new Command(GameResource.signIn, 0, new IAction() {

				public void perform() {
					isRes = false;
					isChangePass = false;

					tfRegPass.isFocus = false;
					tfPass.isFocus = false;
					tfUser.isFocus = true;
					// m_cmdRight = tfUser.cmdClear;

					if (tfUser.y > tfPass.y) {
						int tmp = tfPass.y;
						tfPass.y = tfUser.y;
						tfUser.y = tmp;
					}

					resetTF();
					flagScreen = 0;
					// setButtonDefault(mButton_Login);
				}
			}));
		} else {
			Vector account = new Vector();

			account.addElement(new Command(GameResource.register, 0, new IAction() {

				public void perform() {
					isRes = true;
					isChangePass = false;

					tfRegPass.isFocus = false;
					tfPass.isFocus = false;
					tfUser.isFocus = true;

					if (tfUser.y > tfPass.y) {
						int tmp = tfPass.y;
						tfPass.y = tfUser.y;
						tfUser.y = tmp;
					}

					resetTF();

					flagScreen = 1;
				}
			}));

			if (isChangePass) {
				account.addElement(new Command(GameResource.signIn, 0, new IAction() {

					public void perform() {
						isRes = false;
						isChangePass = false;

						tfRegPass.isFocus = false;
						tfPass.isFocus = false;
						tfUser.isFocus = true;

						if (tfUser.y > tfPass.y) {
							int tmp = tfPass.y;
							tfPass.y = tfUser.y;
							tfUser.y = tmp;
						}
						resetTF();

						flagScreen = 0;
						// setButtonDefault(mButton_Login);
					}
				}));
			} else {
				account.addElement(new Command(GameResource.forgetPass, 0, new IAction() {

					public void perform() {

						isChangePass = true;
						tfRegPass.isFocus = false;
						tfPass.isFocus = false;
						tfUser.isFocus = true;
						// m_cmdRight = tfUser.cmdClear;

						if (tfUser.y < tfPass.y) {
							int tmp = tfPass.y;
							tfPass.y = tfUser.y;
							tfUser.y = tmp;
						}

						resetTF();
						flagScreen = 2;

						// mBt_Defaut = mButton_ChangePass;
						// mBt_Defaut.count = 0;
						// mBt_Defaut.setButtonDefault(mButton_ChangePass);
						// setButtonDefault(mButton_ChangePass);
					}
				}));
			}

			// menu.addElement(new Command("Tài khoản", new IAction() {
			// public void perform() {
			// }
			// }, account));

			// menu.addElement((Command)account.get(0));
			menu.addElement((Command) account.get(1));
			account = null;
		}
		Vector forum = new Vector();

		forum.addElement(new Command(GameResource.forum, 0, new IAction() {

			public void perform() {
				MainScr.gI().onReceivedForumLink("Diễn đàn", GameResource.doYouWantGoToForum,
						"http://m.taigamejava.com");
				// GameCanvas.startOKDlg(GameResource.doYouWantGoToForum, new
				// IAction() {
				//
				// public void perform() {
				// try {
				// GameCanvas.endDlg();
				// String url = "http://m.taigamejava.com";
				// HDCGameMidlet.instance.platformRequest(url);
				// close();
				// HDCGameMidlet.instance.notifyDestroyed();
				// } catch (ConnectionNotFoundException e) {
				// e.printStackTrace();
				// }
				//
				// }
				// });
			}
		}));

		forum.addElement(new Command(GameResource.update, 0, new IAction() {
			public void perform() {
				MainScr.gI().onReceivedForumLink("Cập nhật", GameResource.doYouWantGoToUpdate,
						HDCGameMidlet.m_strLinkUpdateVersion);
				// GameCanvas.startOKDlg(GameResource.doYouWantGoToUpdate, new
				// IAction() {
				// public void perform() {
				//
				// try {
				// GameCanvas.endDlg();
				// HDCGameMidlet.instance.platformRequest(HDCGameMidlet.m_strLinkUpdateVersion);
				// close();
				// HDCGameMidlet.instance.notifyDestroyed();
				// } catch (ConnectionNotFoundException e) {
				// e.printStackTrace();
				// }
				// }
				// });
			}
		}));

		// menu.addElement(new Command("Trang web", new IAction() {
		// public void perform() {
		// }
		// }, forum));

		menu.addElement((Command) forum.get(0));
		menu.addElement((Command) forum.get(1));

		menu.addElement(new Command(GameResource.cleanCache, 0, new IAction() {
			public void perform() {
			}
		}));

		menu.addElement(new Command(GameResource.introduction, 0, new IAction() {
			public void perform() {
				// GameCanvas.startOKDlg("Vua Bài Version " +
				// HDCGameMidlet.version
				// + "- Công ty HDC\nCSKH : 04.66.844.524");
				HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Vua Bài Version "
						+ HDCGameMidlet.version + "- Công ty HDC\nCSKH : 04.66.844.524");
			}
		}));

		menu.addElement(new Command(GameResource.exit, 0, new IAction() {
			public void perform() {
				GameCanvas.startOKDlg(GameResource.doYouWantExit, new IAction() {
					public void perform() {
						close();
					}
				});
			}
		}));

		GameCanvas.instance.menu.startAt(menu, 0);
		menu = null;

	}

	// TODO do login
	protected void doLogin() {
		if (tfUser.getText().length() == 0 || tfPass.getText().length() == 0) {
			// GameCanvas.startOKDlg(GameResource.plzInputInfo);
			HDCGameMidlet.instance.showDialog_Okie("Thông báo", GameResource.plzInputInfo);
			return;
		}
		GameCanvas.startWaitDlg();
		// HDCGameMidlet.instance.showDialog_Waitting("Xin chờ ...");
		HDCGameMidlet.Key = "123455";
		HDCGameMidlet.instance.doConnect();
		if (!HDCGameMidlet.Key.equals("")) {
			GlobalService.onLogin(tfUser.getText(), tfPass.getText());
		}
		savePass();
	}

	// TODO update ánh sáng đèn
	private void update_Light() {
		time++;
		if (time > 50 && time < 120) {
			opacity = CRes.random(70, 200);
			opacity = CRes.random(0, 255);

			// play sound
			// HDCGameMidlet.sound.openFile(HDCGameMidlet.instance,
			// com.hdc.mycasino.R.raw.light);
			// HDCGameMidlet.sound.play();
		}
		if (time == 120) {
			time = 0;
			opacity = 255;
		}
	}

	// TODO update
	public void update() {

		// if (degree > -135) {
		// degree -= 5;
		// } else {
		// if (degree1 < -45)
		// degree1 += 5;
		// else if (x1 < 130)
		// x1 += 5;
		// }
		// HDCGameMidlet.instance.Toast("degree " + degree);

		// if (a < (130)) {
		// a += 5;
		// b += 5;
		// } else {
		// if (a1 < 130) {
		// a1 += 5;
		// b1 += 5;
		// a += 5;
		// b += 5;
		// } else {
		// if (a2 < 130) {
		// a2 += 5;
		// b2 += 5;
		// a1 += 5;
		// b1 += 5;
		// a += 5;
		// b += 5;
		// } else {
		// if (count < 11)
		// count++;
		// }
		// }
		// }

		// TODO update light
		update_Light();
		if (isChangePass) {
			m_cmdCenter = cmdChangePass;
		} else if (isRes) {
			m_cmdCenter = cmdRes;
		} else {
			m_cmdCenter = cmdLogin;
		}
	}

	// TODO paint background
	private void paintBackground(Graphics g) {
		g.translate(-g.getTranslateX(), -g.getTranslateY());
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);

		// paint background
		g.drawImage(m_imgBackground, 0, 0, Graphics.LEFT | Graphics.TOP);
		// g.drawImage_Brightness(m_imgBackground, 0, 0, opacity, Graphics.LEFT
		// | Graphics.TOP);
	}

	// TODO paint Logo
	private void paintLogo(Graphics g) {
		g.drawImage(GameResource.instance.imgLogoCasino, GameCanvas.w / 20, GameCanvas.h / 3,
				Graphics.LEFT | Graphics.TOP);
	}

	// TODO paint bóng đèn,đèn,chui đèn
	private void paintBulb(Graphics g) {
		// paint bóng đèn
		g.drawImage(GameResource.instance.imgBulb, GameCanvas.w / 20
				+ GameResource.instance.imgLogoCasino.getWidth() / 2,
				GameResource.instance.imgLight_1.getHeight() / 3 * 2, Graphics.HCENTER
						| Graphics.TOP);

		// paint ánh sáng đèn
		g.drawImageOpacity(GameResource.instance.imgLight_2, GameCanvas.w / 20
				+ GameResource.instance.imgLogoCasino.getWidth() / 2
				+ (5 / HDCGameMidlet.instance.scale), GameResource.instance.imgLight_1.getHeight()
				/ 2 - (5 / HDCGameMidlet.instance.scale), Graphics.HCENTER | Graphics.TOP, opacity);
		// paint chui bóng đèn
		g.drawImage(GameResource.instance.imgLight_1, GameCanvas.w / 20
				+ GameResource.instance.imgLogoCasino.getWidth() / 2, 0, Graphics.HCENTER
				| Graphics.TOP);
	}

	// TODO paint text : tên,mật khẩu,đổi mật khẩu ,version,...
	private void paintText(Graphics g) {
		// paint username
		BitmapFont.drawBoldFont(g, GameResource.username, tfUser.x, tfUser.y
				- GameResource.instance.imgTextField_Disable.getHeight() / 2, 0xffffff,
				Graphics.LEFT | Graphics.VCENTER);

		if (flagScreen == 0 || flagScreen == 1) {
			// paint password
			BitmapFont.drawBoldFont(g, GameResource.password, tfPass.x, tfPass.y
					- GameResource.instance.imgTextField_Disable.getHeight() / 2, 0xffffff,
					Graphics.LEFT | Graphics.VCENTER);
		}

		if (flagScreen == 1) {
			// paint nhập lại password
			BitmapFont.drawBoldFont(g, GameResource.nhapLai, tfRegPass.x, tfRegPass.y
					- GameResource.instance.imgTextField_Disable.getHeight() / 2, 0xffffff,
					Graphics.LEFT | Graphics.VCENTER);
		}

		if (flagScreen == 0) {
			// paint background "ghi nhớ thông tin"
			// g.drawImage(GameResource.instance.imgRememberInfo_Bg, tfUser.x
			// + GameResource.instance.imgTextField_Disable.getWidth(), tfPass.y
			// + GameResource.instance.imgTextField_Disable.getHeight() / 2 * 3,
			// Graphics.RIGHT | Graphics.VCENTER);

			// NEW
			GameResource.instance.m_frameHeaderCheck.drawFrame(0, tfUser.x
					+ GameResource.instance.imgTextField_Disable.getWidth()
					- GameResource.instance.imgRememberInfo_Bg.getWidth(), tfPass.y
					+ GameResource.instance.imgTextField_Disable.getHeight() / 2 * 3,
					Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);

			// paint check
			if (isCheck) {
				// GameResource.instance.m_frameRememberInfo_Check.drawFrame(0,
				// tfUser.x
				// + GameResource.instance.imgTextField_Disable.getWidth()
				// - GameResource.instance.imgRememberInfo_Bg.getWidth(),
				// tfPass.y
				// + GameResource.instance.imgTextField_Disable.getHeight() / 2
				// * 3,
				// Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);

				GameResource.instance.m_frameHeaderCheck.drawFrame(1, tfUser.x
						+ GameResource.instance.imgTextField_Disable.getWidth()
						- GameResource.instance.imgRememberInfo_Bg.getWidth(), tfPass.y
						+ GameResource.instance.imgTextField_Disable.getHeight() / 2 * 3,
						Sprite.TRANS_NONE, Graphics.LEFT | Graphics.VCENTER, g);

			} else {
				// paint uncheck
				// GameResource.instance.m_frameRememberInfo_Check.drawFrame(1,
				// tfUser.x
				// + GameResource.instance.imgTextField_Disable.getWidth(),
				// tfPass.y
				// + GameResource.instance.imgTextField_Disable.getHeight() / 2
				// * 3,
				// Sprite.TRANS_NONE, Graphics.RIGHT | Graphics.VCENTER, g);
			}
			// paint Nhớ mật khẩu
			BitmapFont.drawBoldFont(g, GameResource.rememberInfo, tfUser.x, tfPass.y
					+ GameResource.instance.imgTextField_Disable.getHeight() / 2 * 3, 0xffffff,
					Graphics.LEFT | Graphics.VCENTER);
		}

		// // paint version
		// String ver = HDCGameMidlet.version;
		// // BitmapFont.drawOutlinedString(g, ver, 0, GameCanvas.h, 0xffff00,
		// // 0xffe600, Graphics.BOTTOM | Graphics.LEFT);
		// BitmapFont.drawOutlinedString(g, ver, 0, GameCanvas.h, 0xffe600,
		// Graphics.BOTTOM
		// | Graphics.LEFT);
	}

	// TODO paint button
	private void paintButton(Graphics g) {
		// button cho màn hình đăng nhập
		if (flagScreen == 0) {
			// paint button đăng nhập
			mButton_Login.paint(g);
			// paint button đăng ký
			mButton_Screen_Reg.paint(g);
		} else if (flagScreen == 1) {
			// button cho màn hình đăng ký
			mButton_Register.paint(g);
		} else {
			// button thay đổi mật khẩu
			mButton_ChangePass.paint(g);
		}
	}

	// TODO paint textfield
	private void paintTextField(Graphics g) {
		tfUser.paint(g);
		if (!isChangePass) {
			tfPass.paint(g);
		}
		if (isRes) {
			tfRegPass.paint(g);
		}
	}

	// TODO paint
	@SuppressWarnings("static-access")
	public void paint(Graphics g) {

		// paint background
		paintBackground(g);

		// paint girl
		// g.drawImage(GameResource.instance.imgGirl, 0, GameCanvas.h
		// - GameResource.instance.imgHeaderBottom.getHeight(), Graphics.LEFT
		// | Graphics.BOTTOM);

		// paint logo
		paintLogo(g);

		// paint bulb (đèn)
		paintBulb(g);

		// paint text
		paintText(g);

		// paint button
		paintButton(g);

		// paint textfield
		paintTextField(g);

		super.paint(g);

		// paint version
		String ver = HDCGameMidlet.version;
		BitmapFont.m_bmNormalFont.drawNormalFont_1(g, "Phiên bản : " + ver, GameCanvas.w,
				GameResource.instance.imgHeaderTop.getHeight() / 2, 0xffb901, Graphics.RIGHT
						| Graphics.VCENTER);

		BitmapFont.setTextSize(14f);
		// paint icon thoát
		GameResource.instance.m_frameRoom_IconRoom.drawFrame(11, 0,
				GameResource.instance.imgHeaderTop.getHeight() / 2, Sprite.TRANS_NONE,
				Graphics.LEFT | Graphics.VCENTER, g);
		// paint text Thoát
		BitmapFont.m_bmNormalFont.drawNormalFont_1(g, "THOÁT",
				GameResource.instance.m_frameRoom_IconRoom.frameWidth,
				GameResource.instance.imgHeaderTop.getHeight() / 2, 0xffb901, Graphics.LEFT
						| Graphics.VCENTER);

		mFooter_QuenMK.paint(g);
		mFooter_DienDan.paint(g);
		mFooter_CapNhat.paint(g);
		mFooter_XoaDuLieu.paint(g);
		mFooter_GioiThieu.paint(g);

		// paint menu
		// int x1 = GameCanvas.w / 4;
		// item Quên mật khẩu

		// GameResource.instance.m_frameRoom_IconRoom.drawFrame(11,
		// 0,GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight() / 2 ,
		// Sprite.TRANS_NONE, Graphics.LEFT|Graphics.VCENTER, g);
		// //paint text Thoát
		// BitmapFont.m_bmNormalFont.drawNormalFont_1(g,
		// "QUÊN MẬT KHẨU",GameResource.instance.m_frameRoom_IconRoom.frameWidth,
		// GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight() / 2,
		// 0xffe600, Graphics.LEFT
		// | Graphics.VCENTER);

		// //item hướng dẫn
		// GameResource.instance.m_frameRoom_IconRoom.drawFrame(11,x1,GameCanvas.h
		// - GameResource.instance.imgHeaderTop.getHeight() / 2,
		// Sprite.TRANS_NONE, Graphics.LEFT|Graphics.VCENTER, g);
		// //paint text Thoát
		// BitmapFont.m_bmNormalFont.drawNormalFont_1(g, "HƯỚNG DẪN",x1 +
		// GameResource.instance.m_frameRoom_IconRoom.frameWidth,
		// GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight() / 2,
		// 0xffe600, Graphics.LEFT
		// | Graphics.VCENTER);

		// item diễn đàn
		// GameResource.instance.m_frameRoom_IconRoom.drawFrame(11,
		// x1*2,GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight() /
		// 2 , Sprite.TRANS_NONE, Graphics.LEFT|Graphics.VCENTER, g);
		// //paint text Thoát
		// BitmapFont.m_bmNormalFont.drawNormalFont_1(g, "DIỄN ĐÀN",x1*2 +
		// GameResource.instance.m_frameRoom_IconRoom.frameWidth,
		// GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight() / 2,
		// 0xffe600, Graphics.LEFT
		// | Graphics.VCENTER);
		//
		// //item cập nhật
		// GameResource.instance.m_frameRoom_IconRoom.drawFrame(11,
		// x1*3,GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight() /
		// 2 , Sprite.TRANS_NONE, Graphics.LEFT|Graphics.VCENTER, g);
		// //paint text Thoát
		// BitmapFont.m_bmNormalFont.drawNormalFont_1(g, "CẬP NHẬT",x1*3 +
		// GameResource.instance.m_frameRoom_IconRoom.frameWidth,
		// GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight() / 2,
		// 0xffe600, Graphics.LEFT
		// | Graphics.VCENTER);
		//
		// //item xóa dữ liệu
		// GameResource.instance.m_frameRoom_IconRoom.drawFrame(11,
		// x1*4,GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight() /
		// 2 , Sprite.TRANS_NONE, Graphics.LEFT|Graphics.VCENTER, g);
		// //paint text Thoát
		// BitmapFont.m_bmNormalFont.drawNormalFont_1(g, "XÓA DỮ LIỆU",x1*4 +
		// GameResource.instance.m_frameRoom_IconRoom.frameWidth,
		// GameCanvas.h - GameResource.instance.imgHeaderTop.getHeight() / 2,
		// 0xffe600, Graphics.LEFT
		// | Graphics.VCENTER);
//		if(HDCGameMidlet.gifView != null)
//			HDCGameMidlet.gifView.onDraw(g, 0, 0);

	}

	// TODO updatekey for "QUÊN MẬT KHẨU"
	private void updateKey_QuenMatKhau() {

	}

	// TODO updatekey for "HƯỚNG DẪN"

	// TODO updatekey for "QUÊN MẬT KHẨU"

	// TODO updatekey for "QUÊN MẬT KHẨU"

	// TODO updatekey for "QUÊN MẬT KHẨU"

	// TODO save pass
	public void savePass() {
		if (isCheck) {
			FileManager.saveUserAndPass(0, GameCanvas.loginScr.tfUser.getText(),
					GameCanvas.loginScr.tfPass.getText(), "user.txt");
		} else {
			FileManager.saveUserAndPass(1, "", "", "user.txt");
		}
	}

	// TODO do back
	public void doBack() {
		if (flagScreen == 0) {
			// thông báo thoát
			// nếu ở màn hình login
			// GameCanvas.startOKDlg(GameResource.doYouWantExit, new IAction() {
			// public void perform() {
			// close();
			// }
			// });

			HDCGameMidlet.instance.showDialog_yes_no("Thông báo", GameResource.doYouWantExit,
					new IAction() {
						public void perform() {
							close();
						}
					});

		} else if (flagScreen == 1) {
			isRes = false;
			flagScreen = 0;
			// setButtonDefault(mButton_Login);
		} else {
			isChangePass = false;
			flagScreen = 0;

			if (tfUser.y > tfPass.y) {
				int tmp = tfPass.y;
				tfPass.y = tfUser.y;
				tfUser.y = tmp;
			}

			resetTF();
			// setButtonDefault(mButton_Login);
		}
	}

	// TODO updatekey for button Back
	private void updateKey_Back() {
		if (GameCanvas.isPointer(0, 0, GameResource.instance.imgMenuBg.getWidth(),
				GameResource.instance.imgMenuBg.getHeight())) {
			doBack();
		}
	}

	// update for key press
	public void updateKey() {

		mFooter_QuenMK.updateKey();
		mFooter_DienDan.updateKey();
		mFooter_CapNhat.updateKey();
		mFooter_XoaDuLieu.updateKey();
		mFooter_GioiThieu.updateKey();

		boolean checkFocus = false;
		if (GameCanvas.isPointerClick) {
			// updatekey for back
			// updateKey_Back();
			if (GameCanvas.isPointer(tfUser.x, tfUser.y, tfUser.width, tfUser.height)) {
				focus = 0;
				checkFocus = true;
			} else if (GameCanvas.isPointer(tfPass.x, tfPass.y, tfPass.width, tfPass.height)) {
				focus = 1;
				checkFocus = true;
			} else if (GameCanvas.isPointer(
					tfUser.x + GameResource.instance.imgTextField_Disable.getWidth()
							- GameResource.instance.imgRememberInfo_Bg.getWidth(), tfPass.y
							+ GameResource.instance.imgTextField_Disable.getHeight(),
					GameResource.instance.imgRememberInfo_Bg.getWidth(),
					GameResource.instance.imgRememberInfo_Bg.getHeight())) {
				focus = 2;
				// play sound
				HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.light_switch);
				HDCGameMidlet.sound.play();

				if (!isRes && focus == 2) {
					cmdCheck.action.perform();
				}
				checkFocus = true;
			}

			tfUser.update();
			tfPass.update();
			if (isRes) {
				tfRegPass.update();
			}

		}

		if (checkFocus) {
			if (focus == 1) {
				tfUser.isFocus = false;
				tfPass.isFocus = true;
				tfRegPass.isFocus = false;
			} else if (focus == 0) {
				tfUser.isFocus = true;
				tfPass.isFocus = false;
				tfRegPass.isFocus = false;
			} else {
				tfUser.isFocus = false;
				tfPass.isFocus = false;
				if (isRes) {
					tfRegPass.isFocus = true;
				}
			}
		}

		if (flagScreen == 0) {
			// login
			mButton_Login.updateKey();
			// screen register
			mButton_Screen_Reg.updateKey();
		} else if (flagScreen == 1) {
			// register
			mButton_Register.updateKey();
		} else {
			mButton_ChangePass.updateKey();
		}

		super.updateKey();
	}

	// TODO reset Text field
	public void resetTF() {
		int tmp = GameCanvas.w
				- (GameResource.instance.imgTextField_Disable.getWidth() + GameResource.instance.imgMenuEnable
						.getWidth() / 4);
		tfUser.x = tmp;
		tfPass.x = tmp;
		tfRegPass.x = tmp;

	}

}
