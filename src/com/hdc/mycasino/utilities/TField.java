package com.hdc.mycasino.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.danh.standard.Graphics;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.screen.PaintPopup;
import com.hdc.mycasino.screen.Screen;

public class TField {
	public static final int KEY_NUM0 = 0;
	public static final int KEY_NUM1 = 1;
	public static final int KEY_NUM2 = 2;
	public static final int KEY_NUM3 = 3;
	public static final int KEY_NUM4 = 4;
	public static final int KEY_NUM5 = 5;
	public static final int KEY_NUM6 = 6;
	public static final int KEY_NUM7 = 7;
	public static final int KEY_NUM8 = 8;
	public static final int KEY_NUM9 = 9;
	public static final int KEY_STAR = 10;
	public static final int KEY_BOUND = 11;
	public static final int KEY_UP = 12;
	public static final int KEY_DOWN = 13;
	public static final int KEY_LEFT = 14;
	public static final int KEY_RIGHT = 15;
	public static final int KEY_FIRE = 16;
	public static final int KEY_LEFT_SOFTKEY = 17;
	public static final int KEY_RIGHT_SOFTKEY = 18;
	public static final int KEY_CLEAR = 19;
	public static final int KEY_BACK = 20;

	public int x;
	public int y;
	public int width;
	public int height;

	public boolean isFocus;
	public boolean lockArrow = false;
	public boolean paintFocus = true;
	public boolean isReadonly = false;
	public boolean isPaintBG = true;

	public static int typeXpeed = 2;
	private static final int[] MAX_TIME_TO_CONFIRM_KEY = { 18, 14, 11, 9, 6, 4, 2 };
	private static int CARET_HEIGHT = 0;
	private static final int CARET_WIDTH = 1;
	private static final int CARET_SHOWING_TIME = 5;
	private static final int TEXT_GAP_X = (int) (10 / HDCGameMidlet.instance.scale);
	private static final int MAX_SHOW_CARET_COUNER = 10;
	public static final int INPUT_TYPE_ANY = 0;
	public static final int INPUT_TYPE_NUMERIC = 1;
	public static final int INPUT_TYPE_PASSWORD = 2;
	public static final int INPUT_ALPHA_NUMBER_ONLY = 3;
	// type k xử lý gì hết
	public static final int INPUT_TYPE_NOACTION = 4;
	private static String[] print = { " 0", ".,@?!_1\"/$-():*+<=>;%&~#%^&*{}[];\'/1",
			"abc2áàảãạâấầẩẫậăắằẳẵặ2", "def3đéèẻẽẹêếềểễệ3", "ghi4íìỉĩị4", "jkl5",
			"mno6óòỏõọôốồổỗộơớờởỡợ6", "pqrs7", "tuv8úùủũụưứừửữự8", "wxyz9ýỳỷỹỵ9", "*", "#" };
	private static String[] printA = { "0", "1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7",
			"tuv8", "wxyz9", "0", "0" };

	// BB:
	private static String[] printBB = { " 0", "er1", "ty2", "ui3", "df4", "gh5", "jk6", "cv7",
			"bn8", "m9", "0", "0", "qw", "as", "zx", "op", "l" };
	private static String[] printBBA = { " 0", "er1", "ty2", "ui3", "df4", "gh5", "jk6", "cv7",
			"bn8", "m9", "0", "0", "qw", "as", "zx", "op", "l" };

	private String text = "";
	private String passwordText = "";
	private String paintedText = "";

	private int caretPos = 0;
	private int counter = 0;
	private int maxTextLenght = 500;
	private int offsetX = 0;
	private int lastKey = -1984;
	private int keyInActiveState = 0;
	private int indexOfActiveChar = 0;
	private int showCaretCounter = MAX_SHOW_CARET_COUNER;
	public int inputType = INPUT_TYPE_ANY;
	public static boolean isQwerty;
	public static int typingModeAreaWidth;
	public int mode = 0;
	public static final String modeNotify[] = { "abc", "Abc", "ABC", "123" };
	public static final int NOKIA = 0;
	public static final int MOTO = 1;
	public static final int ORTHER = 2;
	public static int changeModeKey = 11;
	// modify
	public int MAX_LENGTH_TEXT = 50;

	// TODO color for textfield
	private int color = Color.BLACK;
	// TODO alpha for text field
	private int alpha = 120;
	// TODO kiểu textfield
	// Bằng 0 : textfield màu đen
	// Bằng 1 : textfield màu trắng
	private int isType = 0;

	public boolean isShowPopup_Gender = false;

	// modify
	// public static int NAME_LOGIN = 1;
	// public static final int INPUT_NAME = 1;
	// public static final int INPUT_PASSWORD = 2;
	// public static final int INPUT_NUMBER = 3;

	// public static javax.microedition.lcdui.Canvas c;
	// public static javax.microedition.midlet.MIDlet m;

	public void doChangeToTextBox() {
		if (inputType == INPUT_TYPE_NOACTION)
			return;
		HDCGameMidlet.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final Context context = HDCGameMidlet.instance;

				final EditText etInput = new EditText(context);

				final AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setView(etInput);
				InputFilter[] filterArray = new InputFilter[1];
				filterArray[0] = new InputFilter.LengthFilter(MAX_LENGTH_TEXT);
				etInput.setFilters(filterArray);

				if (inputType == INPUT_TYPE_PASSWORD) {
					etInput.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					etInput.setText(text);
					etInput.setGravity(Gravity.TOP);

					// alert.setTitle("Mật khẩu");
				} else if (inputType == INPUT_TYPE_NUMERIC) {
					etInput.setInputType(InputType.TYPE_CLASS_NUMBER
							| InputType.TYPE_NUMBER_FLAG_SIGNED);
				} else
					etInput.setText(text);
				alert.setTitle("Nhập");
				etInput.setGravity(Gravity.TOP);

				final AlertDialog dialog = alert.create();
				etInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
				etInput.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						// TODO Auto-generated method stub
						text = etInput.getText().toString();
						try {
							dialog.dismiss();
							InputMethodManager imm = (InputMethodManager) context
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						return false;
					}
				});

				etInput.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						InputMethodManager imm = (InputMethodManager) context
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
					}
				});
				etInput.requestFocus();

				dialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				dialog.show();

			}
		});

	}

	public static boolean setNormal(char ch) {
		if ((ch < '0' || ch > '9') && (ch < 'A' || ch > 'Z') && (ch < 'a' || ch > 'z')) {
			return false;
		}
		return true;
	}

	public static void setVendorTypeMode(int mode) {
		if (mode == MOTO) {
			print[0] = "0";
			print[10] = " *";
			print[11] = "#";
			changeModeKey = '#';
		} else if (mode == NOKIA) {
			print[0] = " 0";
			print[10] = "*";
			print[11] = "#";
			changeModeKey = '#';
		} else if (mode == ORTHER) {
			print[0] = "0";
			print[10] = "*";
			print[11] = " #";
			changeModeKey = '*';
		}
	}

	public Command cmdClear;

	private void init() {

		CARET_HEIGHT = BitmapFont.m_bmNormalFont.getHeight() + 1;

		cmdClear = new Command(GameResource.del, 0, new IAction() {
			public void perform() {
				clear();
			}
		});
		typingModeAreaWidth = BitmapFont.m_bmNormalFont.stringWidth("ABC") + 5;

	}

	public TField() {
		text = "";
		init();
	}

	public TField(String text, int maxLen, int inputType) {
		this.text = text;
		this.maxTextLenght = maxLen;
		this.inputType = inputType;
		init();
	}

	public void clear() {
		// if (caretPos > 0 && text.length() > 0) {
		// text = text.substring(0, caretPos - 1)
		// + text.substring(caretPos, text.length());
		// caretPos--;
		// setOffset();
		// setPasswordTest();
		// }

		if (text.length() > 0) {
			text = text.substring(0, text.length() - 1);
			setOffset();
			setPasswordTest();
		}

		if (caretPos > 0 && text.length() > 0) {
			text = text.substring(0, caretPos - 1) + text.substring(caretPos, text.length());
			caretPos--;
			setOffset();
			setPasswordTest();
		}

	}

	public void setOffset() {
		if (inputType == INPUT_TYPE_PASSWORD)
			paintedText = passwordText;
		else
			paintedText = text;

		if (offsetX < 0
				&& BitmapFont.m_bmNormalFont.stringWidth(paintedText) + offsetX < width
						- TEXT_GAP_X - 13 - typingModeAreaWidth)
			offsetX = width - 10 - typingModeAreaWidth
					- BitmapFont.m_bmNormalFont.stringWidth(paintedText);
		if (offsetX + BitmapFont.m_bmNormalFont.stringWidth(paintedText.substring(0, caretPos)) <= 0) {
			offsetX = -BitmapFont.m_bmNormalFont.stringWidth(paintedText.substring(0, caretPos));
			offsetX = offsetX + 40;
		} else if (offsetX
				+ BitmapFont.m_bmNormalFont.stringWidth(paintedText.substring(0, caretPos)) >= width
				- 12 - typingModeAreaWidth)
			offsetX = width - 10 - typingModeAreaWidth
					- BitmapFont.m_bmNormalFont.stringWidth(paintedText.substring(0, caretPos)) - 2
					* TEXT_GAP_X;

		if (offsetX > 0)
			offsetX = 0;
	}

	private void keyPressedAny(int keyCode) {
		String[] print;

		// BB:
		if (GameCanvas.isBB) {
			if (inputType == INPUT_TYPE_PASSWORD || inputType == INPUT_TYPE_NUMERIC)
				print = printBBA;
			else
				print = printBB;
		} else {
			if (inputType == INPUT_TYPE_PASSWORD || inputType == INPUT_TYPE_NUMERIC)
				print = printA;
			else
				print = TField.print;
		}

		// if (GameCanvas.isBB) {
		// keyCode = getBBKeyCode(keyCode);
		// if (keyCode == -1)
		// return;
		// }

		if (keyCode == lastKey) {
			indexOfActiveChar = (indexOfActiveChar + 1) % print[keyCode - '0'].length();
			char c = print[keyCode - '0'].charAt(indexOfActiveChar);
			if (mode == 0)
				c = Character.toLowerCase(c);
			else if (mode == 1) {
				c = Character.toUpperCase(c);
			} else if (mode == 2) {
				c = Character.toUpperCase(c);
			} else {
				c = print[keyCode - '0'].charAt(print[keyCode - '0'].length() - 1);
			}
			String ttext = text.substring(0, caretPos - 1) + c;
			if (caretPos < text.length())
				ttext = ttext + text.substring(caretPos, text.length());
			text = ttext;
			keyInActiveState = MAX_TIME_TO_CONFIRM_KEY[typeXpeed];
			setPasswordTest();
		} else {
			if (text.length() < maxTextLenght) {
				if (mode == 1 && lastKey != -1984)
					mode = 0;
				indexOfActiveChar = 0;
				char c = print[keyCode - '0'].charAt(indexOfActiveChar);
				if (mode == 0)
					c = Character.toLowerCase(c);
				else if (mode == 1) {
					c = Character.toUpperCase(c);
				} else if (mode == 2) {
					c = Character.toUpperCase(c);
				} else {
					c = print[keyCode - '0'].charAt(print[keyCode - '0'].length() - 1);
				}
				String ttext = text.substring(0, caretPos) + c;
				if (caretPos < text.length())
					ttext = ttext + text.substring(caretPos, text.length());
				text = ttext;
				keyInActiveState = MAX_TIME_TO_CONFIRM_KEY[typeXpeed];
				caretPos++;
				setPasswordTest();
				setOffset();
			}
		}
		lastKey = keyCode;
	}

	private void keyPressedAscii(int keyCode) {
		if (inputType == INPUT_TYPE_PASSWORD || inputType == INPUT_TYPE_NUMERIC) {
			// BB
			if (GameCanvas.isBB) {
				if ((keyCode < '0' || keyCode > '9') && (keyCode != 32) && (keyCode != 66)
						&& (keyCode != 67) && (keyCode != 68) && (keyCode != 69) && (keyCode != 71)
						&& (keyCode != 74) && (keyCode != 77) && (keyCode != 84) && (keyCode != 85))
					return;
			} else if ((keyCode < '0' || keyCode > '9') && (keyCode < 'A' || keyCode > 'Z')
					&& (keyCode < 'a' || keyCode > 'z')) {
				return;
			}
		}
		if (text.length() < maxTextLenght) {
			String ttext = text.substring(0, caretPos) + (char) keyCode;
			if (caretPos < text.length())
				ttext = ttext + text.substring(caretPos, text.length());
			text = ttext;
			caretPos++;
			setPasswordTest();
			setOffset();
		}
	}

	int holdCount;

	// public void keyHold(int keyCode) {
	// holdCount++;
	// if (holdCount > 15 && !isQwerty && keyCode < print.length) {
	// clear();
	// keyPressedAscii(print[keyCode].charAt(print[keyCode].length() - 1));
	// keyInActiveState = MAX_TIME_TO_CONFIRM_KEY[typeXpeed];
	// holdCount = 0;
	// }
	// if (holdCount > 20) {
	// if (keyCode == KEY_CLEAR) {
	// setText("");
	// holdCount = 0;
	// }
	// }
	// }
	//
	// public boolean keyPressed(int keyCode) {
	// // BB:
	// if (GameCanvas.isBB) {
	// if (keyCode == 8 || keyCode == 127) {
	// clear();
	// }
	// } else {
	// if (keyCode == 8 || keyCode == -8 || keyCode == 204) {
	// clear();
	// return true;
	// }
	// }
	// holdCount = 0;
	// if (keyCode >= 'A' && keyCode <= 'z') {
	// isQwerty = true;
	// typingModeAreaWidth = 0;
	// }
	// if (isQwerty && !GameCanvas.isBB) {// BB:
	// // Nokia E71 character '_' by 2x '-'
	// if (keyCode == '-') {
	// if (keyCode == lastKey
	// && keyInActiveState < MAX_TIME_TO_CONFIRM_KEY[typeXpeed]) {
	// text = text.substring(0, caretPos - 1) + '_';
	// this.paintedText = text;
	// setPasswordTest();
	// setOffset();
	// lastKey = -1984;
	// return false;
	// }
	// lastKey = '-';
	// }
	// if (keyCode >= 32) {
	// keyPressedAscii(keyCode);
	// return false; // swallow
	// }
	// }
	// if (keyCode == changeModeKey) {
	// mode++;
	// if (mode > 3)
	// mode = 0;
	// keyInActiveState = 1;
	// lastKey = keyCode;
	// return false; // swallow
	// }
	// if (keyCode == '*')
	// keyCode = '9' + 1;
	// if (keyCode == '#')
	// keyCode = '9' + 2;
	//
	// //BB:
	// if (GameCanvas.isBB && keyCode >= '0') {
	// if(isQwerty){
	// keyPressedAscii(keyCode);
	// keyInActiveState = 1;
	// }else
	// if (inputType == INPUT_TYPE_ANY || inputType == INPUT_TYPE_PASSWORD
	// || inputType == INPUT_ALPHA_NUMBER_ONLY)
	// keyPressedAny(keyCode);
	// else if (inputType == INPUT_TYPE_NUMERIC) {
	// keyPressedAscii(keyCode);
	// keyInActiveState = 1;
	// }
	// } else if (keyCode >= '0' && keyCode <= '9' + 2) {
	// if (inputType == INPUT_TYPE_ANY || inputType == INPUT_TYPE_PASSWORD
	// || inputType == INPUT_ALPHA_NUMBER_ONLY)
	// keyPressedAny(keyCode);
	// else if (inputType == INPUT_TYPE_NUMERIC) {
	// keyPressedAscii(keyCode);
	// keyInActiveState = 1;
	// }
	// } else {
	// indexOfActiveChar = 0;
	// lastKey = -1984;
	// if (keyCode == KEY_LEFT && !lockArrow) {
	// if (caretPos > 0) {
	// caretPos--;
	// setOffset();
	// showCaretCounter = MAX_SHOW_CARET_COUNER;
	// return false;
	// }
	// } else if (keyCode == KEY_RIGHT && !lockArrow) {
	// if (caretPos < text.length()) {
	// caretPos++;
	// setOffset();
	// showCaretCounter = MAX_SHOW_CARET_COUNER;
	// return false;
	// }
	// } else if (keyCode == KEY_CLEAR) {
	// clear();
	// return false;
	// } else {
	// lastKey = keyCode;
	// }
	// }
	// return true; // Not swallow
	// }
	//
	// public void pointerRelease(final int px, final int py) {
	// // doChangeToTextBox();
	// }

	public int color1 = 0xd0cfd5;
	public int color2 = 0x244900;
	public int color3 = 0x969696;

	public int focus_color1 = 0xffffff;
	public int focus_color2 = 0xd0cfd5;
	public int focus_color3 = 0x0f7540;

	public int textColor = 0x000000;
	public int focusTextColor = 0x000000;

	// public void paint(Graphics g) {
	// g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
	// setPasswordTest();
	// setOffset();
	//
	// boolean isFocus = isFocused();
	// if (inputType == INPUT_TYPE_PASSWORD)
	// paintedText = passwordText;
	// else
	// paintedText = text;
	// if (isPaintBG) {
	// if (isFocus) {
	// PaintPopup.paintInputTf(g, focus_color1, focus_color2, focus_color3, x, y
	// - 2, width, height);
	// // PaintPopup.paintInputTf(g, 0, x, y );
	// } else {
	// PaintPopup.paintInputTf(g, color1, color2, color3, x, y - 2, width,
	// height);
	//
	// // PaintPopup.paintInputTf(g, 1, x, y );
	// }
	// }
	//
	// // g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
	// // g.setClip(x + 3, y - 1, width - 12, height - 4);
	// if (isFocus)
	// // BitmapFont.drawBoldFont(g, GameResource.username, x + 15, y + 30,
	// 0x000000, 0);
	// BitmapFont.drawNormalFont(g, paintedText, x + 15, y, 0x000000,
	// Graphics.VCENTER | Graphics.LEFT);
	// // + (height >> 1) - 3
	// else
	// BitmapFont.drawNormalFont(g, paintedText, x + 15, y, 0x000000,
	// Graphics.VCENTER | Graphics.LEFT);
	// // + (height >> 1) - 3
	//
	// // g.setClip(x + 3, y + 1, width - 4, height - 4);
	// // g.setColor(0);
	// }

	public boolean m_bShowFullText = false;
	public int m_iMoveText = 0;
	public int m_iTickDelayMoveText = 0;

	public void setFocused(boolean isfocus) {
		this.isFocus = isfocus;
		m_iMoveText = 0;
	}

	public void paint(Graphics g) {
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		setPasswordTest();
		setOffset();

		// boolean isFocus = isFocused();
		if (inputType == INPUT_TYPE_PASSWORD)
			paintedText = passwordText;
		else
			paintedText = text;

		if (isPaintBG) {
			PaintPopup.gI().paintInputTf(g, x, y, isFocus);
		}

		g.setClip(x + 3, y - 1, width - 4, height);

		int color = textColor;

		color = focusTextColor;

		color = 0x969696;

		if (isPaintBG) {
			if (m_bShowFullText && isFocus) {
				BitmapFont.drawItalicFont(g, paintedText, TEXT_GAP_X + m_iMoveText + x, y
						+ (height >> 1), color, Graphics.VCENTER | Graphics.LEFT);

				int wText = BitmapFont.m_bmNormalFont.stringWidth(paintedText) - width + 10;
				if (wText > 0 && (-m_iMoveText) < wText)
					m_iMoveText--;
				else {
					m_iTickDelayMoveText++;
					if (m_iTickDelayMoveText > 200) {
						m_iTickDelayMoveText = 0;
						m_iMoveText = 0;
					}
				}
			} else {
				BitmapFont.drawItalicFont(g, paintedText, TEXT_GAP_X + offsetX + x, y
						+ (height >> 1), color, Graphics.VCENTER | Graphics.LEFT);
			}
		} else {
			if (isFocus) {
				g.setColor(this.color);
				g.setAlpha(this.alpha);
				g.fillRoundRectWithTransparenr(x, y, width, height, 8, 8);

				GameResource.instance.m_frameTextField_IconEdit.drawFrame(isFocus ? 0 : 1, x
						+ width - GameResource.instance.m_frameTextField_IconEdit.frameWidth, y
						+ height / 2, Sprite.TRANS_NONE, Graphics.RIGHT | Graphics.VCENTER, g);

			} else {
				if (isType == 1) {
					g.setColor(this.color);
					g.setAlpha(this.alpha);
					g.fillRoundRectWithTransparenr(x, y, width, height, 8, 8);
				}
			}

			if (isType == 1) {
				g.setClip(x, y, width, height);
				BitmapFont.drawItalicFont(g, paintedText, TEXT_GAP_X + offsetX + x, y
						+ (height >> 1), color, Graphics.VCENTER | Graphics.LEFT);
			}

		}
	}

	// TODO set type
	public void setTypeTF(int type) {
		this.isType = type;
	}

	// TODO set color for textfield
	public void setColor_TF(int color) {
		this.color = color;
	}

	// TODO set alpha for textfield
	public void setAlpha_TF(int alpha) {
		this.alpha = alpha;
	}

	public String getText() {
		return text;
	}

	public void paintStatus(Graphics g) {
		g.setClip(0, 0, GameCanvas.w, GameCanvas.h);
		setPasswordTest();
		setOffset();

		boolean isFocus = isFocused();
		if (inputType == INPUT_TYPE_PASSWORD)
			paintedText = passwordText;
		else
			paintedText = text;

		int color = textColor;
		if (isFocus) {
			color = focusTextColor;
		}

		String[] str = BitmapFont.m_bmNormalFont.splitFontBStrInLine(paintedText, width);
		int i;
		int h = Screen.ITEM_HEIGHT >> 1;
		for (i = 0; i < str.length; i++) {
			BitmapFont.drawNormalFont(g, str[i], TEXT_GAP_X + x, y + h, 0xff0000, Graphics.VCENTER
					| Graphics.LEFT);
			if (i == str.length - 1)
				break;
			h += Screen.ITEM_HEIGHT;
		}
		// g.setColor(0xFFFFFF);
		// g.drawRect(x, y, width + 5, h + Screen.ITEM_HEIGHT / 2);
		// if (isFocused() && !isReadonly) {
		// if (keyInActiveState == 0 && (showCaretCounter > 0 || (counter /
		// CARET_SHOWING_TIME) % 2 == 0)) {
		// g.setColor(0xFFFFFF);
		// int len = (str.length == 0) ? 0 :
		// BitmapFont.m_bmNormalFont.stringWidth(str[str.length - 1]);
		// g.fillRect(TEXT_GAP_X + x + len - CARET_WIDTH + 1, y + h -
		// (CARET_HEIGHT >> 1), CARET_WIDTH,
		// CARET_HEIGHT);
		// }
		// }
		str = null;
	}

	private boolean isFocused() {
		return isFocus;
	}

	private void setPasswordTest() {
		if (inputType == INPUT_TYPE_PASSWORD) {
			passwordText = "";
			int a = text.length();
			for (int i = 0; i < a; i++)
				passwordText = passwordText + "*";
			if (keyInActiveState > 0 && caretPos > 0)
				passwordText = passwordText.substring(0, caretPos - 1) + text.charAt(caretPos - 1)
						+ passwordText.substring(caretPos, passwordText.length());
		}
	}

	public void update() {
		counter++;
		if (keyInActiveState > 0) {
			keyInActiveState--;
			if (keyInActiveState == 0) {
				indexOfActiveChar = 0;
				if (mode == 1 && lastKey != changeModeKey)
					mode = 0;
				lastKey = -1984;
				setPasswordTest();
			}
		}
		if (showCaretCounter > 0)
			showCaretCounter--;

		if (GameCanvas.isPointerClick) {
			setTextBox();
		}
	}

	public void setTextBox() {
		if (GameCanvas.isPointer(x, y, width, height) /* && !isReadonly */) {
			if (!isFocus)
				isFocus = true;
			else {
				if (isShowPopup_Gender){
					HDCGameMidlet.instance.showDialog_ChonGioiTinh(/*GameResource.instance.getValueFromText(text)*/);
				}
				else
					doChangeToTextBox();
			}
		} else
			isFocus = false;

		// HDCGameMidlet.instance.Toast(isFocus + "");
	}

	public void setText(String text) {
		if (text == null)
			return;
		this.text = text;
		this.paintedText = text;
	}

	public int getMaxTextLenght() {
		return maxTextLenght;
	}

	public void setMaxTextLenght(int max) {
		maxTextLenght = max;
	}

	public int getIputType() {
		return inputType;
	}

	public void setIputType(int iputType) {
		this.inputType = iputType;
	}
}
