package com.hdc.mycasino;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

import javax.crypto.spec.PSource;

import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sound;
import com.hdc.mycasino.animation.Effect;
import com.hdc.mycasino.gif.GifView;
import com.hdc.mycasino.model.Footer;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.Position;
import com.hdc.mycasino.screen.CameraList;
import com.hdc.mycasino.screen.ChatTextField;
import com.hdc.mycasino.screen.Dialog;
import com.hdc.mycasino.screen.InputDlg;
import com.hdc.mycasino.screen.LoginScr;
import com.hdc.mycasino.screen.Menu;
import com.hdc.mycasino.screen.MenuNhanh;
import com.hdc.mycasino.screen.MsgDlg;
import com.hdc.mycasino.screen.Screen;
import com.hdc.mycasino.utilities.GameResource;

public class GameCanvas extends SurfaceView implements Runnable {
	// 0 1 2 3 4 5 6 7 8 9 * LSoft RSoft

	public static boolean[] keyPressed = new boolean[14];
	public static boolean[] keyReleased = new boolean[14];
	public static boolean[] keyHold = new boolean[14];
	public static boolean[] keyNumPressed = new boolean[10];
	public static boolean isMoto;
	long lastTimePress = 0;
	int vibrateTime;
	public static CameraList cameraList = new CameraList();
	// Black Berry
	public static boolean isBB;
	// /////////////
	public static GameCanvas instance;
	static boolean bRun;
	public boolean isClick = false;
	int count = 1;
	public static int gameTick;

	public static boolean isPointerDown;
	public static boolean isPointerClick;
	public static boolean isPointerMove;
	public static boolean isMove;

	public static int px, py, pxLast, pyLast;
	Position p = new Position();// = (Position)
								// GameCanvas.instance.listPoint.elementAt(GameCanvas.instance.listPoint.size()
								// - 1);
	Position pLast = new Position();// = (Position)
									// GameCanvas.instance.listPoint.elementAt(GameCanvas.instance.listPoint.size()
									// - 2);
	public int disMove = 0;

	public static int w, h, hw, hh, wd3, hd3, w2d3, h2d3, w3d4, h3d4, ch;
	public static int hBottomBar;

	public static Vector listPoint;
	private Image m_imgBuff;
	StringWriter stringWriter = new StringWriter();
	private PrintWriter printWriter = new PrintWriter(stringWriter, true);

	// DECRALCE SCREEN AND DIALOG
	public static InputDlg inputDlg = new InputDlg();
	public static Screen currentScreen;
	public static MsgDlg msgdlg = new MsgDlg();
	public Menu menu = new Menu();
	public static Dialog currentDialog;
	public static Vector m_arrEffect = new Vector();

	// //////
	public static boolean hasPointerEvents = false;
	public static boolean hasPopUp = false;
	SurfaceHolder surfaceHolder;
	Image framebuffer;
	public Sound mSound;
	// dùng cho dialog "Xin chờ"
	public static int iTimeWaitting = 0;

	// Declare screens ...
	public static LoginScr loginScr;

	public GameCanvas(HDCGameMidlet midlet) {
		// this.setFullScreenMode(true);
		super(midlet);
		instance = this;

		// get screen size
		// w = GameCanvas.instance.getWidth();
		// h = GameCanvas.instance.getHeight();

		// create image buffer
		// m_imgBuff = Image.createImage(w, h);
		// //////
		this.surfaceHolder = getHolder();
		this.mSound = new Sound(midlet);
		// this.mSound.openFile(midlet, R.raw.click);

	}

	public void start() {
		bRun = true;
		new Thread(this).start();
	}

	public void resume() {
		bRun = true;
		start();
	}

	public void pause() {
		bRun = false;
	}

	// paint effect
	private void paintEffects(Graphics g) {
		try {
			Effect effect;
			for (int i = 0, a = m_arrEffect.size(); i < a; i++) {
				effect = (Effect) m_arrEffect.elementAt(i);
				effect.paint(g);
			}
		} catch (Exception e) {
		}
	}

	// update effect
	private void updateEffects() {
		try {
			Effect effect;
			for (int i = 0, a = m_arrEffect.size(); i < a; i++) {
				effect = (Effect) m_arrEffect.elementAt(i);
				effect.update();
			}
		} catch (Exception e) {
		}
	}

	// kiem tra su kien click
	public boolean hasPointerEvents() {
		return hasPointerEvents;
	}

	// check for mouse down event in rectangle (x, y, width, height)
	public static boolean isPointer(float x, float y, float w, float h) {
		if (!isPointerDown && !isPointerClick) {
			return false;
		}
		if (px >= x && px <= x + w && py >= y && py <= y + h) {
			return true;
		}
		return false;
	}

	public static boolean isPointer_Down(float x, float y, float w, float h) {
		if (!isPointerDown && !isPointerClick) {
			return false;
		}
		if (pxLast >= x && pxLast <= x + w && pyLast >= y && pyLast <= y + h) {
			return true;
		}
		return false;
	}

	// clear events
	public static void clearKeyPressed() {
		isPointerClick = false;
		for (int i = 0; i < 14; i++) {
			keyPressed[i] = false;
		}
	}

	protected void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (currentScreen != null) {
			currentScreen.paint(g);
			paintEffects(g);
		}

		g.translate(-g.getTranslateX(), -g.getTranslateY());
		g.setClip(0, 0, w, h);

		if (MenuNhanh.showMenuNhanh) {
			MenuNhanh.paint(g);
		}

		if (currentDialog != null) {
			currentDialog.paint(g);
			// paintEffects(g);
		} else if (menu.m_showMenu)
			if (menu.m_list != null) {
				menu.paintMenu(g);
			}
	}

	public int pxLast_1 = 0, pyLast_1 = 0;

	@SuppressWarnings({ "unchecked", "static-access" })
	protected void pointerDragged(int x, int y) {
		// TODO Auto-generated method stub
		listPoint.addElement(new Position(x, y));

		// if (listPoint.size() > 1) {
		// p = (Position) GameCanvas.instance.listPoint
		// .elementAt(GameCanvas.instance.listPoint.size() - 1);
		// pLast = (Position) GameCanvas.instance.listPoint
		// .elementAt(GameCanvas.instance.listPoint.size() - 2);
		// }

		if (/* pxLast != x || pyLast != y */pLast.y != p.y) {
			// pxLast = x;
			// pyLast = y;
			isMove = true;
			disMove = pLast.y - p.y;
		} else {
			isPointerMove = false;
			isMove = false;
			disMove = 0;
		}
		px = x;
		py = y;
		isPointerMove = true;
		// isClick = false;
		// isPointerDown = false;
	}

	protected void pointerPressed(int x, int y) {
		// TODO Auto-generated method stub
		isClick = true;
		isPointerDown = true;
		isPointerMove = false;
		isMove = false;
		pxLast = x;
		pyLast = y;
		px = x;
		py = y;
	}

	protected void pointerReleased(int x, int y) {
		// TODO Auto-generated method stub
		isPointerDown = false;
		isPointerClick = true;
		isPointerMove = false;
		isMove = false;
		px = x;
		py = y;

	}

	public boolean isMove() {
		boolean kq = false;
		if (isPointerClick) {
			if (px == pxLast && py == pyLast)
				kq = false;
			else
				kq = true;
		}
		return kq;
	}

	public boolean isMove(float x, float y, float w, float h) {
		boolean kq = false;
		if (isPointerClick) {
			if (px == pxLast && py == pyLast)
				kq = false;
			else {
				if ((px >= x && px <= x + w && py >= y && py <= y + h)
						&& (pxLast >= x && pxLast <= x + w && pyLast >= y && pyLast <= y + h))
					kq = false;
				else
					kq = true;
			}
		}
		return kq;
	}

	public boolean isMoveCard(float x, float y, float w, float h) {
		if (px >= x && px <= x + w && py >= y && py <= y + h) {
			return true;
		}
		return false;
	}

	protected void sizeChanged(int w, int h) {
		// TODO Auto-generated method stub
	}

	// implement CommandListener
	// public void commandAction(Command arg0, Displayable arg1) {
	// // TODO Auto-generated method stub
	// }

	// desired fps
	private final static int MAX_FPS = 50;
	// maximum number of frames to be skipped
	private final static int MAX_FRAME_SKIPS = 5;
	// the frame period
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;

	// implement Runnable
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// bRun = true;
		// while (bRun) {
		// try {
		// if (vibrateTime > 0) {
		// vibrateTime--;
		// if (vibrateTime == 0) {
		// Display.getDisplay(HDCGameMidlet.instance).vibrate(0);
		// }
		// }
		// long l1 = System.currentTimeMillis();
		// gameTick++;
		// if (gameTick > 10000) {
		// if (System.currentTimeMillis() - lastTimePress > 20000
		// && GameCanvas.currentScreen == GameCanvas.loginScr) {
		// // destroy application if it is not active in a long
		// // time ....
		// // HDCGameMidlet.instance.notifyDestroyed();
		// }
		// gameTick = 0;
		// }
		//
		// if (currentScreen != null) {
		//
		// // update current screen
		// ChatTextField.gI().update();
		// currentScreen.update();
		//
		// // update others
		// if (cameraList.isShow) {
		// cameraList.moveCamera();
		// }
		// if (currentDialog != null) {
		// currentDialog.update();
		// } else if (menu.m_showMenu) {
		// menu.updateMenu();
		// menu.updateMenuKey();
		// } else {
		// currentScreen.updateKey();
		// if (cameraList.isShow) {
		// cameraList.updateKey();
		// }
		// }
		//
		// int i;
		// Effect ef;
		// for (i = 0; i < m_arrEffect.size(); i++) {
		// ef = (Effect) m_arrEffect.elementAt(i);
		// ef.update();
		// }
		// ef = null;
		// }
		// isPointerClick = false;
		// repaint();
		// serviceRepaints();
		//
		// // Synchronize time
		// long l2 = System.currentTimeMillis() - l1;
		// try {
		// if (l2 < 50) {
		// Thread.sleep(50 - l2);
		// } else {
		// Thread.sleep(1);
		// }
		// } catch (InterruptedException e) {
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		Canvas canvas;
		long beginTime; // the time when the cycle begun
		long timeDiff; // the time it took for the cycle to execute
		int sleepTime; // ms to sleep (<0 if we're behind)
		int framesSkipped; // number of frames being skipped
		sleepTime = 0;
		// bRun = true;
		Rect dstRect = new Rect();

		while (bRun) {
			// Log.e("run", "run");
			canvas = null;
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try {

				if (!surfaceHolder.getSurface().isValid())
					continue;
				gameTick++;
				if (gameTick > 1000) {
					gameTick = 0;
				}

				// if (iTimeWaitting > 0 && iTimeWaitting < 3000) {
				// iTimeWaitting++;
				// }

				// update sound
				// if (HDCGameMidlet.isTurnOnOffSound) {
				// if (!mSound.getMediaPlayer().isPlaying()) {
				// mSound.play();
				// }
				// }

				// continue;
				canvas = surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0; // resetting the frames skipped
					// System.gc();
					// update game state
					update();
					// render state to the screen
					// draws the canvas on the panel
					paint(HDCGameMidlet.instance.graphics);

					canvas.getClipBounds(dstRect);

					canvas.drawBitmap(framebuffer.getBitmap(), null, dstRect, null);

					// calculate how long did the cycle take
					timeDiff = System.currentTimeMillis() - beginTime;
					// calculate sleep time
					sleepTime = (int) (FRAME_PERIOD - timeDiff);

					if (sleepTime > 0) {
						// if sleepTime > 0 we're OK
						try {
							// send the thread to sleep for a short period
							// very useful for battery saving
							Thread.sleep(32);
						} catch (InterruptedException e) {
						}
					}
					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						// we need to catch up
						// update without rendering
						// update();
						// add frame period to check if in next frame
						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace(printWriter);
				printWriter.flush();
				stringWriter.flush();
				HDCGameMidlet.instance.Toast(stringWriter.toString());

			} finally {
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}

			} // end finally
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		if (currentScreen != null) {
			// MainScr.gI().updateMsg();
			// update current screen
			currentScreen.update();

			if (currentScreen.mBt_Defaut != null) {
				currentScreen.mBt_Defaut.startEffect();
			}

			updateEffects();

			// update others
			if (cameraList.isShow) {
				cameraList.moveCamera();
			}
			if (currentDialog != null) {
				currentDialog.update();
			} else if (MenuNhanh.showMenuNhanh) {
				MenuNhanh.updateKey();
			}
			// else if (MenuNhanh.showMenuNhanh) {
			// MenuNhanh.updateKey();
			// }
			else if (menu.m_showMenu) {
				if (menu.m_list != null) {
					menu.updateMenu();
					menu.updateMenuKey();
				}
			} else {
				currentScreen.updateKey();
				if (cameraList.isShow) {
					cameraList.updateKey();
				}
			}
			// if (menu.move > 0 && !GameCanvas.menu.m_showMenu) {
			// menu.move-=menu.a++;
			// if(menu.move<=0)
			// {
			// menu.move=0;
			// menu.a=10;
			// }
			//
			// }

		}
		if (isClick) {
			if (gameTick % 2 == 0)
				count++;
			if (count > 3) {
				isClick = false;
				count = 0;
			}
		}
		// if (m_strMarqueeMsg != null && m_strMarqueeMsg.length() > 0) {
		// m_iPosXMsg -= 2;
		// if (m_iPosXMsg <= -m_iStrWidth) {
		// m_iPosXMsg = GameCanvas.w;
		// m_iTimeDisplay += 3;
		// }
		// if (m_iTimeDisplay >= 1) {
		// m_iTimeDisplay = 0;
		// m_strMarqueeMsg = null;
		// }
		// }
		hasPointerEvents = false;
		isPointerClick = false;
	}

	// TODO create new touch for game
	public static Vector<Position> m_listNewPoint;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// Log.e("www=" + scaleW, "hhh=" + scaleH);
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (currentScreen.isTouchSpecial) {
				if (m_listNewPoint == null)
					m_listNewPoint = new Vector<Position>();
				else
					m_listNewPoint.removeAllElements();
				m_listNewPoint.addElement(new Position(x, y));
				currentScreen.isPointerDown = true;
			} else
				pointerPressed(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			if (currentScreen.isTouchSpecial) {
				m_listNewPoint.addElement(new Position(x, y));
				if (m_listNewPoint.size() > 3) {
					currentScreen.isPointerClick = false;
					if (m_listNewPoint.size() > 10) {
						int i;
						x = m_listNewPoint.elementAt(m_listNewPoint.size() - 10).x;
						y = m_listNewPoint.elementAt(m_listNewPoint.size() - 10).y;
						int xx, yy;
						for (i = m_listNewPoint.size() - 10; i < m_listNewPoint.size(); i++) {
							xx = m_listNewPoint.elementAt(i).x;
							yy = m_listNewPoint.elementAt(i).y;
							if ((xx >= x - 10 && xx <= x + 10) && (yy >= y - 10 && yy <= y + 10))
								continue;
							else
								break;
						}
						currentScreen.isPointerMove = true;
						currentScreen.isPointerHold = false;
						if (i >= m_listNewPoint.size()) {
							currentScreen.isPointerMove = false;
							currentScreen.isPointerHold = true;
						}
					} else
						currentScreen.isPointerHold = true;
				} else
					currentScreen.isPointerClick = true;
			} else
				pointerDragged(x, y);
			break;
		case MotionEvent.ACTION_UP:
			if (currentScreen.isTouchSpecial) {
				currentScreen.isPointerClick = false;
				currentScreen.isPointerDown = false;
				currentScreen.isPointerMove = false;
				currentScreen.isPointerHold = false;
				if (m_listNewPoint.size() <= 3)
					currentScreen.isPointerClick = true;
				m_listNewPoint.removeAllElements();
			} else
				pointerReleased(x, y);
			break;
		}
		hasPointerEvents = true;
		return true;
	}

	// public void start() {
	// new Thread(this).start();
	// }

	// stuff
	private void mapKeyRelease(int keyCode) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case 42:
			keyHold[10] = false;
			keyReleased[10] = true;
			break; // Key [*]
		case 35:
			keyHold[11] = false;
			keyReleased[11] = true;
			break; // Key [#]
		case -6:
		case -21:
			keyHold[12] = false;
			keyReleased[12] = true;
			break; // Soft1
		case -7:
		case -22:
			keyHold[13] = false;
			keyReleased[13] = true;
			break; // Soft2
		case -5:
		case 10:
			keyHold[5] = false;
			keyReleased[5] = true;
			break; // [i]
		case -1:
		case -38:
			keyHold[2] = false;
			break; // UP
		case -2:
		case -39:
			keyHold[8] = false;
			break; // DOWN
		case -3:
			keyHold[4] = false;
			break; // LEFT
		case -4:
			keyHold[6] = false;
			break; // RIGHT
		}
	}

	@SuppressWarnings("unused")
	private void mapKeyPress(int keyCode) {
		// TODO Auto-generated method stub

		if (currentDialog != null) {
			currentDialog.keyPress(keyCode);
		} else if (!GameCanvas.instance.menu.m_showMenu) {
			if (ChatTextField.gI().isShow)
				ChatTextField.gI().keyPressed(keyCode);
			else
				currentScreen.keyPress(keyCode);
		}

		switch (keyCode) {
		case 42:

			keyPressed[10] = true;
			break; // Key [*]
		case 35:
			keyHold[11] = true;
			keyPressed[11] = true;
			break; // Key [#]
		case -6:
		case -21:
			keyHold[12] = true;
			keyPressed[12] = true;
			break; // Soft1
		case -7:
		case -22:
			keyHold[13] = true;
			keyPressed[13] = true;
			break; // Soft2
		case -5:
		case 10:
			keyHold[5] = true;
			keyPressed[5] = true;
			break; // [i]
		case -1:
		case -38:
			keyHold[2] = true;
			keyPressed[2] = true;
			break; // UP
		case -2:
		case -39:
			keyHold[8] = true;
			keyPressed[8] = true;
			break; // DOWN
		case -3:
			keyHold[4] = true;
			keyPressed[4] = true;
			break; // LEFT
		case -4:
			keyHold[6] = true;
			keyPressed[6] = true;
			break; // RIGHT
		}
	}

	public void initGameCanvas() {
		// isMoto = (getKeyCode(Canvas.FIRE) == -20);

		w = HDCGameMidlet.width;
		h = HDCGameMidlet.height;

		hw = w / 2;
		hh = h / 2;
		wd3 = w / 3;
		hd3 = h / 3;
		w2d3 = 2 * w / 3;
		h2d3 = 2 * h / 3;
		w3d4 = 3 * w / 4;
		h3d4 = 3 * h / 4;
		ch = (h - hBottomBar) / 2;

		hBottomBar = 26;

		// ///////////////INIT SCREEN////////////////
		loginScr = new LoginScr();
		listPoint = new Vector();
		m_listNewPoint = new Vector<Position>();
	}

	public static Position getMinMaxFor(int cmy, int w, int size, int numW, int hLimit) {
		Position p = new Position(0, size);
		if (cmy > 0) {
			p.x = (cmy / w) * numW;
		}
		if (p.y * w > hLimit - w) {
			p.y = p.x + ((hLimit) / w) * numW + numW;
			if (p.y > size) {
				p.y = size;
			}
		}
		return p;
	}

	public static com.hdc.mycasino.model.Command cmdEndDlg = new com.hdc.mycasino.model.Command(GameResource.no,
			new IAction() {
				public void perform() {
					GameCanvas.msgdlg.delX.setPositionTo(0, 0);
					GameCanvas.msgdlg.delX.setPosition(GameCanvas.instance.msgdlg.imgBackground.getWidth() / 2,
							GameCanvas.instance.msgdlg.imgBackground.getWidth() / 2);
					// GameCanvas.msgdlg.isShowText = false;
				}
			});

	// close dialog
	public static void endDlg() {
		if (currentDialog instanceof InputDlg)
			currentDialog = null;
		else if (!msgdlg.isNoClose) {
			msgdlg.isWaiting = false;
			if (msgdlg.isWaitingDialog)
				currentDialog = null;
			else {
				msgdlg.delX.setPositionTo(0, 0);
			}
		}

		// HDCGameMidlet.instance.endDialog();
	}

	// open yes no dialog with interval time
	public static void startOKDlg(String info, int interval, IAction yes, IAction no) {
		endDlg();
		msgdlg.isNoClose = true;
		msgdlg.setInfo(info, interval, new com.hdc.mycasino.model.Command(GameResource.yes, yes),
				new com.hdc.mycasino.model.Command(GameResource.no, no));
		currentDialog = msgdlg;
	}

	static String[] a = { "" };

	// open ok dialog with the info message
	public static void startOKDlg(String info) {
		if (msgdlg.isNoClose)
			return;

		endDlg();
		msgdlg.setInfo(info, new com.hdc.mycasino.model.Command(GameResource.OK, new IAction() {

			public void perform() {
				if ((GameCanvas.msgdlg.delX.x == GameCanvas.msgdlg.delX.xTo)
						&& (GameCanvas.msgdlg.delX.y == GameCanvas.msgdlg.delX.yTo)) {
					GameCanvas.msgdlg.resetPositionTo();
					// GameCanvas.msgdlg.isShowText = false;
				}
			}
		}), null);
		currentDialog = msgdlg;
	}

	public static void startMsgDlg(String info, Vector listCmd) {
		if (msgdlg.isNoClose)
			return;

		endDlg();
		msgdlg.setDetailInfo(info, new com.hdc.mycasino.model.Command("", new IAction() {

			public void perform() {
				currentDialog = null;

			}
		}), listCmd);
		currentDialog = msgdlg;
	}

	// open dialog with the info message and list command
	public static void startOKDlg(String info, Vector listCmd) {
		if (msgdlg.isNoClose)
			return;

		endDlg();
		msgdlg.setInfo(info, new com.hdc.mycasino.model.Command("", new IAction() {

			public void perform() {
				// currentDialog = null;

			}
		}), listCmd);
		currentDialog = msgdlg;
	}

	// open yes no dialog
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void startOKDlg(String info, IAction yes) {
		Vector menu = new Vector();
		menu.addElement(new com.hdc.mycasino.model.Command(GameResource.yes, yes));
		menu.addElement(cmdEndDlg);
		startOKDlg(info, menu);
	}

	// open ok dialog with custom handler event when user choice ok
	public static void startOK(String info, IAction ok) {
		Vector menu = new Vector();
		menu.addElement(new com.hdc.mycasino.model.Command(GameResource.OK, ok));
		startOKDlg(info, menu);
	}

	// open ok dialog with custom handler event when user choice ok
	public static void startOKEndGame(String[] info, IAction ok, int posBold) {
		if (msgdlg.isNoClose)
			return;

		Vector menu = new Vector();
		menu.addElement(new com.hdc.mycasino.model.Command(GameResource.OK, ok));
		endDlg();
		msgdlg.setInfo(info, new com.hdc.mycasino.model.Command("", new IAction() {

			public void perform() {
				// currentDialog = null;
			}
		}), menu, posBold);
		currentDialog = msgdlg;
	}

	// open waiting dialog
	public static void startWaitDlg() {
		startWaitDlg(GameResource.plzWait);
	}

	// open waiting dialog
	public static void startWaitDlg(String content) {
		if (msgdlg.isNoClose)
			return;

		endDlg();
		msgdlg.isWaiting = true;
		msgdlg.isWaitingDialog = true;
		msgdlg.setInfo(content, null, null);
		currentDialog = msgdlg;

		// HDCGameMidlet.instance.showDialog_Waitting(content);
	}
}
