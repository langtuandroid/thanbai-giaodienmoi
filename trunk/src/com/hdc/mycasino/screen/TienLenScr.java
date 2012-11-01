package com.hdc.mycasino.screen;

import java.util.Vector;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.animation.FlyImage;
import com.hdc.mycasino.model.Command;
import com.hdc.mycasino.model.IAction;
import com.hdc.mycasino.model.Position;
import com.hdc.mycasino.network.Message;
import com.hdc.mycasino.network.MessageIO;
import com.hdc.mycasino.service.GlobalService;
import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.DetailImage;
import com.hdc.mycasino.utilities.GameResource;
import com.hdc.mycasino.utilities.ImagePack;

public class TienLenScr extends PlayGameScr {
	static TienLenScr instance;

	public static TienLenScr gI() {
		if (instance == null) {
			instance = new TienLenScr((byte) 4, (byte) 13);
		}
		return instance;
	}

	Command m_cmdHit;
	Command m_cmdPass;
	Command m_cmdBoChon;
	Command m_cmdDat;
	Command m_cmdSort;
	boolean m_bIsPushSort = false;

	String m_strWhoHit;
	public boolean m_bIsMustHitCmd = false;
	@SuppressWarnings("rawtypes")
	private Vector m_arrStoreHitCard;
	private Vector m_arrListCard;
	// private Vector m_arrCardEnd;
	private Vector m_arrSelectCard;

	public Card[] m_arrHitCard;
	int[] m_arrListCardPlayer;
	byte m_bTypeSort = 0;
	boolean m_bIsEndGame = false;

	int m_iyUp = GameCanvas.h - GameResource.instance.m_imgCards[0].getHeight() / 2 * 3
			- GameResource.instance.m_frameTienLen_Button.frameHeight;
	int m_iyDown = GameCanvas.h - GameCanvas.hBottomBar - GameResource.instance.imgCard.getHeight() / 2;
	int m_iy = GameCanvas.h - GameResource.instance.m_imgCards[0].getHeight()
			- GameResource.instance.m_frameTienLen_Button.frameHeight;

	Command cmdBack;

	// TODO cây bài được chia ra
	private int countCard_Out = 0;
	// TODO position for 4 cây bài bay ra
	private Position[] mPositions_CardOut;
	// TODO position for 13 cây bài người chơi
	private Position[] mPositions_Card;
	// TODO index for 4 buttons
	private int idx_BoChon = 0;
	private int idx_XepBai = 0;
	private int idx_BoLuot = 0;
	private int idx_Danh = 0;
	// TODO scale "Nhất - nhì - ba - bét"
	private int[] scaleRank = new int[4];

	// ///////kiểm tra tình trạng chặt heo
	boolean m_bTypeKillBig = false;// 0 binh thuong - 1 chat heo - 2 chat de - 3
									// chat doi thong - 4 chat tu quy
	
	boolean flagMove;

	// TODO flag Move and Fire Card
	boolean flagMove_FireCard = false;

	public static void clear() {
		if (instance != null) {
			instance.m_cmdHit = null;
			instance.m_cmdPass = null;
			instance.m_cmdBoChon = null;
			instance.m_cmdDat = null;
			instance.m_cmdSort = null;
			instance.m_strWhoHit = null;
			instance.m_arrStoreHitCard = null;
			instance.m_arrListCard = null;
			// instance.m_arrCardEnd = null;
			instance.m_arrListCardPlayer = null;
		}
		instance = null;
	}

	public TienLenScr(byte maxPlayer, byte maxCard) {
		super(maxPlayer, maxCard);

		cmdBack = new Command("Rời bàn", new IAction() {

			public void perform() {
				if (m_bIsPlaying) {
					// GameCanvas.startOKDlg(
					// "Nếu thoát khi đang chơi bạn sẽ bị xử thua. Bạn có muốn thoát",
					// new IAction() {
					// public void perform() {
					// GameCanvas.endDlg();
					// m_iStatusOutGame = 2;
					// doLeaveBoard();
					// }
					// });

					HDCGameMidlet.instance.showDialog_yes_no("Thông báo",
							"Nếu thoát khi đang chơi bạn sẽ bị xử thua.\n Bạn có muốn thoát không ?", new IAction() {
								public void perform() {
									GameCanvas.endDlg();
									m_iStatusOutGame = 2;
									doLeaveBoard();
								}
							});

				} else {
					m_iStatusOutGame = 0;
					doLeaveBoard();
				}
			}
		});

		m_cmdHit = new Command("Đánh", new IAction() {

			public void perform() {
				if (m_arrSelectCard.size() == 0) {
					// GameCanvas.startOKDlg("Chọn bài đi bạn!");
					HDCGameMidlet.instance.showDialog_Okie("Thông báo", "Chọn bài đi bạn!");
				} else {
					int[] sendCard = new int[m_arrSelectCard.size()];
					Card c;
					int i;
					for (i = 0; i < m_arrSelectCard.size(); i++) {
						c = (Card) m_arrSelectCard.elementAt(i);
						sendCard[i] = c.m_id;
					}
					GlobalService.onFireCardTL(boardId, sendCard);
					sendCard = null;
					c = null;
					// saveCmd(m_cmdCenter, m_cmdRight);
				}
			}
		});

		m_cmdPass = new Command("Bỏ lượt", new IAction() {
			public void perform() {
				GlobalService.onSendSkipTurn(roomId, boardId);
				// saveCmd(m_cmdCenter, m_cmdRight);
			}
		});

		m_cmdBoChon = new Command(GameResource.skip, new IAction() {
			public void perform() {
				flagMove = false;
				m_arrSelectCard.removeAllElements();
				initPosCardOwner(false, true);
				m_cmdRight = m_cmdPass;
				if (m_bIsMustHitCmd)
					m_cmdRight = null;
			}
		});

		m_cmdDat = new Command("Đặt", new IAction() {
			public void perform() {
				actionHandSort();
			}
		});

		m_cmdSort = new Command("Sắp xếp", new IAction() {
			public void perform() {
				m_arrSelectCard.removeAllElements();
				initPosCardOwner(false, true);
				m_cmdRight = m_cmdPass;
				if (m_bIsMustHitCmd)
					m_cmdRight = null;

				sortcard();
			}
		});

		m_arrListCard = new Vector();
		m_arrStoreHitCard = new Vector();
		// m_arrCardEnd = new Vector();
		m_arrSelectCard = new Vector();

		m_arrListCardPlayer = new int[maxPlayer];

		m_arrHitCard = new Card[52];

		int i = 0;
		for (i = 0; i < 52; i++) {
			m_arrHitCard[i] = new Card(-1);
			m_arrHitCard[i].setScale(1.5f);
		}
	}

	public void update() {

		// Nhất - nhì - ba - tư
		for (int i = 0; i < m_arrPlayers.length; i++) {
			if (m_arrPlayers[i] != null && m_arrPlayers[i].itemId != -1) {
				if (m_arrPlayers[i].rank >= 0 && m_arrPlayers[i].rank < 4) {
					// if (scaleRank[i] < 1.5f)
					// scaleRank[i] += (float) 0.25f;
					if (scaleRank[i] == 0)
						scaleRank[i] = 1;
					else
						scaleRank[i] = 0;
				}
			}
		}

		int i;
		if (m_arrListCard != null && m_arrListCard.size() > 0) {
			for (i = 0; i < m_arrListCard.size(); i++) {
				if (((Card) m_arrListCard.elementAt(i)).m_id != -1 && !((Card) m_arrListCard.elementAt(i)).isCallMove) {
					((Card) m_arrListCard.elementAt(i)).translate(2);
				}
			}
		}

		if (m_arrHitCard != null) {
			for (i = 0; i < m_arrHitCard.length; i++) {
				if (m_arrHitCard[i].m_id != -1 && !m_arrHitCard[i].isCallMove) {
					m_arrHitCard[i].translate(2);
				}
			}
		}

		// if (m_arrCardEnd != null && m_arrCardEnd.size() > 0) {
		// for (i = 0; i < m_arrCardEnd.size(); i++) {
		// if (((Card) m_arrCardEnd.elementAt(i)).m_id != -1) {
		// ((Card) m_arrCardEnd.elementAt(i)).translate(2);
		// }
		// }
		// }
		super.update();
	}

	@Override
	protected void updateForOrtherCard() {
		int i, j;
		if (m_arrListCard != null && m_arrListCard.size() > 0) {
			Card c;
			for (i = 0; i < m_arrListCard.size(); i++) {
				c = ((Card) m_arrListCard.elementAt(i));
				if (c.m_id != -1 && !c.isCallMove) {
					if (m_bIsStartDeliver)
						c.translateNewArc(5);
					else
						c.translate(4);
				}
			}
			c = null;
		}

		for (i = 0; i < m_iMaxPlayer; i++)
			if (m_arrPlayers[i].itemId != -1 && i != m_iMyPosInVt)
				for (j = 0; j < m_iMaxNumCard; j++)
					if (m_arrOtherCards[i][j].m_id != -1 && !m_arrOtherCards[i][j].isCallMove) {
						if (m_bIsStartDeliver)
							m_arrOtherCards[i][j].translateNewArc(5);
						else
							m_arrOtherCards[i][j].translate(2);
					}
	}

	@Override
	protected void deliverCard() {
		updateForOrtherCard();

		// hành động sau khi chia bài thành công
		actionAfterDeliver();

		// kiểm tra điều kiện chia bài
		if (!m_bIsStartDeliver)
			return;

		int i, j, pos;
		for (i = 0; i < m_iMaxPlayer; i++) {
			pos = switchAnchorToPos(m_bCountAnchor);
			m_bCountAnchor--;
			if (m_bCountAnchor < 0)
				m_bCountAnchor = (byte) (m_iMaxPlayer - 1);

			if (pos < 0)
				continue;
			if (pos == m_iMyPosInVt) {
				for (j = 0; j < m_arrListCard.size(); j++) {
					if (!((Card) m_arrListCard.elementAt(j)).isCallMove)
						continue;
					((Card) m_arrListCard.elementAt(j)).isCallMove = false;
					((Card) m_arrListCard.elementAt(j)).setActionRotate();
					return;
				}
			} else {
				for (j = 0; j < m_iMaxNumCard; j++) {
					if (m_arrOtherCards[pos][j].m_id < 0 || !m_arrOtherCards[pos][j].isCallMove)
						continue;
					m_arrOtherCards[pos][j].isCallMove = false;
					return;
				}
			}
		}
		m_bIsStartDeliver = false;
		m_bIsEndDeliver = true;
	}

	// hành động sau khi chia bài(gom bài lại)
	private void actionAfterDeliver() {
		// TODO Auto-generated method stub
		if (!m_bIsEndDeliver)
			return;
		Card c;
		int i, j;
		byte count = 0;
		for (i = 0; i < m_iMaxPlayer; i++) {
			if (i == m_iMyPosInVt || m_arrPlayers[i].itemId == -1) {
				count++;
				continue;
			}
			for (j = 0; j < m_iMaxNumCard / 2; j++) {
				if (m_arrOtherCards[i][j].m_id < 0)
					continue;
				c = m_arrOtherCards[i][j + 1];
				m_arrOtherCards[i][j].setPos(c.xTo, c.yTo, c.anphaTo, c.scaleTo);
				c = m_arrOtherCards[i][m_iMaxNumCard - j - 2];
				m_arrOtherCards[i][m_iMaxNumCard - j - 1].setPos(c.xTo, c.yTo, c.anphaTo, c.scaleTo);
			}
			if (m_arrOtherCards[i][0].compare(m_arrOtherCards[i][m_iMaxNumCard / 2]))
				count++;
		}
		c = null;
		// ket thuc hoàn toàn hiệu ứng chia bài
		if (count >= m_iMaxPlayer) {
			initLastCard();
		}
	}

	// init vị trí các lá bài khi vào phòng choi(dang xem)
	private void initLastCard() {
		int w = (GameResource.instance.imgTienLen_User.getWidth() + GameResource.instance.m_imgCards[0].getWidth() * 2 / 3) >> 1;
		int h = GameCanvas.hh - imgTable.getHeight() / 2 + imgTable.getHeight() / 5;
		// rest het các lá bài khác
		int i, j;
		for (i = 0; i < m_iMaxPlayer; i++) {
			for (j = 1; j < m_iMaxNumCard; j++)
				m_arrOtherCards[i][j].m_id = -1;
			// chừa lại 1 lá bài và init vị trí bay đến cho nó
			switch (m_arrPos[i].anchor) {
			case 1:
				m_arrOtherCards[i][0].setPos(m_arrPos[i].x + w, m_arrPos[i].y, 0, 1.5f);
				break;
			case 3:
				m_arrOtherCards[i][0].setPos(m_arrPos[i].x - w, m_arrPos[i].y, 0, 1.5f);
				break;
			default:
				m_arrOtherCards[i][0].setPos(m_arrPos[i].x - w, /*
																 * (h <
																 * m_arrPos[
																 * i].y) ?
																 */m_arrPos[i].y + DetailImage.imgCard_h / 4/*
																											 * :
																											 * h
																											 */, 0,
						1.5f);
				break;
			}
		}
		m_bIsEndDeliver = false;
		m_bIsStartDeliver = false;
	}

	public void loadImg() {
		if (GameResource.instance.imgArrowBig == null)
			GameResource.instance.imgArrowBig = ImagePack.createImage(ImagePack.BIG_ARROW_PNG);
	}

	// khởi tạo vị trí của các người khác khi chia bài
	private void restOtherPlayer(byte type) {
		int i, j;
		int x = (GameCanvas.w - GameResource.instance.m_imgCards[0].getWidth()) >> 1;
		int y = (GameCanvas.h - GameResource.instance.m_imgCards[0].getHeight()) >> 1;
		// khoi tao vị trí ban đầu xuất hiện
		Card c;
		for (i = 0; i < m_arrListCard.size(); i++) {
			c = ((Card) m_arrListCard.elementAt(i));
			c.setPos(x, y, x, y);
			c.isUpCard = true;
			c.isCallMove = true;
			c.setScale(1.5f);
		}
		c = null;
		for (i = 0; i < m_iMaxPlayer; i++) {
			if (m_arrPlayers[i].itemId == -1 || i == m_iMyPosInVt)
				continue;
			for (j = 0; j < m_iMaxNumCard; j++) {
				m_arrOtherCards[i][j].setPos(x, y, x, y);
				m_arrOtherCards[i][j].isCallMove = true;
				m_arrOtherCards[i][j].isUpCard = true;
			}
		}
		// khởi tạo vị trí kết thúc của quân bài
		int k;
		int top = (int) (GameResource.instance.imgTienLen_User.getHeight());
		int left = (int) (GameResource.instance.imgTienLen_User.getWidth());
		int w = GameResource.instance.m_imgCards[0].getHeight() * 3 / 2;
		int mid = m_iMaxNumCard >> 1;
		int maxRadian = 5 * mid;
		double t;
		// ///////dành cho không phải là cung
		int per = GameResource.instance.imgTienLen_CardBack2.getWidth() * 2 / 9;
		int maxW = per * mid;
		x = (GameResource.instance.m_imgCards[0].getWidth()) >> 1;
		y = (GameResource.instance.m_imgCards[0].getHeight()) >> 1;
		boolean isArc = (CRes.random(10) > 0);
		for (i = 0; i < m_iMaxPlayer; i++) {
			if (m_arrPlayers[i].itemId == -1 || i == m_iMyPosInVt)
				continue;
			for (j = 0; j < m_iMaxNumCard; j++) {
				k = j;
				if (k >= mid)
					k = m_iMaxNumCard - j;
				// xét các diểm trên cung
				t = (double) 1 / m_iMaxNumCard;
				t *= j;
				// xét vị trí trung tâm
				m_arrOtherCards[i][j].typeAnchorPaint = Graphics.HCENTER | Graphics.VCENTER;

				if (isArc) {
					switch (m_arrPos[i].anchor) {
					case 1:
						x = (int) ((1 - t) * (1 - t) * (m_arrPos[i].x + left) + 2 * (1 - t) * t
								* (m_arrPos[i].x + left + w / 3) + t * t * (m_arrPos[i].x + left));
						y = (int) ((1 - t) * (1 - t) * (m_arrPos[i].y - w / 2) + 2 * (1 - t) * t * (m_arrPos[i].y) + t
								* t * (m_arrPos[i].y + w / 2));
						m_arrOtherCards[i][j].setNextNewPosTo(x, y, -90 - maxRadian + j * 5, (float) 1.5);
						break;
					case 2:
						x = (int) ((1 - t) * (1 - t) * (m_arrPos[i].x + w / 2) + 2 * (1 - t) * t * (m_arrPos[i].x) + t
								* t * (m_arrPos[i].x - w / 2));
						y = (int) ((1 - t) * (1 - t) * (m_arrPos[i].y + top) + 2 * (1 - t) * t
								* (m_arrPos[i].y + top + w / 3) + t * t * (m_arrPos[i].y + top));
						m_arrOtherCards[i][j].setNextNewPosTo(x, y, -maxRadian + j * 5, (float) 1.5);
						break;
					default:// 3
						x = (int) ((1 - t) * (1 - t) * (m_arrPos[i].x - left) + 2 * (1 - t) * t
								* (m_arrPos[i].x - left - w / 3) + t * t * (m_arrPos[i].x - left));
						y = (int) ((1 - t) * (1 - t) * (m_arrPos[i].y + w / 2) + 2 * (1 - t) * t * (m_arrPos[i].y) + t
								* t * (m_arrPos[i].y - w / 2));
						m_arrOtherCards[i][j].setNextNewPosTo(x, y, 90 - maxRadian + j * 5, (float) 1.5);
						break;
					}
				} else {
					switch (m_arrPos[i].anchor) {
					case 1:
						m_arrOtherCards[i][j].setNextNewPosTo(m_arrPos[i].x + left / 2 + x, m_arrPos[i].y - maxW + per
								* j, 0, (float) 1.5);
						break;
					case 2:
						m_arrOtherCards[i][j].setNextNewPosTo(m_arrPos[i].x - maxW + per * j, m_arrPos[i].y + top / 2
								+ y, 0, (float) 1.5);
						break;
					default:// 3
						m_arrOtherCards[i][j].setNextNewPosTo(m_arrPos[i].x - left / 2 - x, m_arrPos[i].y - maxW + per
								* j, 0, (float) 1.5);
						break;
					}
				}
			}
		}
	}

	// init vị trí đích đến của bài của mình
	private void initPosCardOwner(boolean isDeliver, boolean isNeedY) {
		if (m_arrListCard.size() <= 0)
			return;
		int wCard = GameCanvas.w / m_arrListCard.size();
		int i = GameResource.instance.m_imgCards[0].getWidth()
				- (int) (GameResource.instance.m_imgCards[0].getWidth() / 5);
		if (wCard > i)
			wCard = i;

		int x = GameCanvas.hw - m_arrListCard.size() * wCard / 2;
		if (m_arrListCard.size() >= m_iMaxNumCard && x > GameResource.instance.m_imgCards[0].getWidth())
			x = (int) (-GameResource.instance.m_imgCards[0].getWidth() / 5);

		for (i = 0; i < m_arrListCard.size(); i++) {
			if (isDeliver) {
				((Card) m_arrListCard.elementAt(i)).setNextNewPosTo(x, m_iy, 0, 1);
			} else {
				if (isNeedY)
					((Card) m_arrListCard.elementAt(i)).yTo = m_iy;
				((Card) m_arrListCard.elementAt(i)).xTo = x;
			}
			x += wCard;
		}
	}

	// tr? v? tráng thái bang d?u khi b?c d?u game ho?c vào phòng choi
	private void resetElement() {
		m_bIsPushSort = false;
		m_bIsStartDeliver = false;
		m_bIsEndGame = false;
		m_bIsEndDeliver = false;
		m_bIsMustHitCmd = false;
		m_bTypeKillBig = false;

		CRes.resetArr(m_arrListCardPlayer);
		m_arrSelectCard.removeAllElements();
		m_arrListCard.removeAllElements();
		// m_arrCardEnd.removeAllElements();
		// init for hitcard
		resetCard(m_arrHitCard, -1);

		m_selected = 0;
		m_bTypeSort = 0;
		m_iGameTick = 0;

		m_strWhoTurn = "";
		m_strWhoHit = "";
	}

	public void switchToMe(String ownerString) {
		resetElement();
		super.switchToMe(ownerString);
		Card.setCardType(Card.KHAC);
		loadImg();
	}

	@SuppressWarnings("unchecked")
	public void startGame(Message message) {
		resetElement();
		super.startGame();
		m_bIsStartDeliver = false;
		flagStart = 4;

		// read my cards
		byte c[] = MessageIO.readBytes(message);
		CRes.SortCardTL(c, (byte) 0);
		int i;
		Card card;
		for (i = 0; i < c.length; i++) {
			card = new Card(c[i]);
			m_arrListCard.addElement(card);
		}
		card = null;

		// thể loại sắp xếp bài
		m_bTypeSort = 1;
		// khởi tạo giá trị của quân bài
		for (i = 0; i < m_iMaxPlayer; i++)
			if (i != m_iMyPosInVt)
				resetCard(m_arrOtherCards[i], 0);
		// init vị trí của những người khác khi thựcc hiện chia bài
		restOtherPlayer((byte) 0);
		// sort card
		initPosCardOwner(true, true);
		// init list card player
		for (i = 0; i < m_iMaxPlayer; i++)
			if (m_arrPlayers[i].itemId != -1 && i != m_iMyPosInVt)
				m_arrListCardPlayer[i] = 13;

		m_bIsStartDeliver = true;
		System.gc();
	}

	// kiểm tra có chặt heo không ?
	private void checkEatBig(String nick, int[] card) {
		if (card.length >= 4) {
			CRes.sortArr(card);
			if (CRes.check3DoiThong(card) || CRes.check4Quy(card)) {
				if (!m_bTypeKillBig)
					m_bTypeKillBig = true;
				else {
					m_bTypeKillBig = false;

					Image img = GameResource.instance.imgWin_ChatDe;
					FlyImage f = new FlyImage();
					f.startEffect(GameCanvas.w / 2, GameCanvas.h / 2, img);
					f = null;
					img = null;

					return;
				}
			}
		}
		// /kiem tra truong hop chat heo
		if (m_strWhoHit != null && m_strWhoHit.length() > 0 && !m_strWhoHit.equals(nick) && (card.length >= 4)) {
			// //////
			int i;
			for (i = 0; i < m_arrHitCard.length; i++) {
				if (m_arrHitCard[i].m_id != -1 && (m_arrHitCard[i].m_id % 13 == 12)) {
					break;
				}
			}

			if (i >= m_arrHitCard.length) {
				return;
			}

			// String strEffect = "Chặt Heo!!!";
			Image img = GameResource.instance.imgWin_ChatHeo;

			int count = 0;
			for (; i < m_arrHitCard.length; i++) {
				if (m_arrHitCard[i].m_id != -1 && m_arrHitCard[i].m_id % 13 != 12) {
					count++;
				}
			}

			if (count > 3)
				img = GameResource.instance.imgWin_ChatDe;
			// effect chat heo
			FlyImage f = new FlyImage();
			f.startEffect(GameCanvas.w / 2, GameCanvas.h / 2, img);
			f = null;
			img = null;
		}
	}

	public void onFireCard(String nick, int[] card) {
		if (m_bIsEndDeliver) {
			m_bIsEndDeliver = false;
			resetCard(m_arrHitCard, -1);
		}

		checkEatBig(nick, card);

		int i, j;
		m_strWhoHit = nick;
		// init pos fly hit card
		initPosHitCard(card);

		if (HDCGameMidlet.m_myPlayerInfo.itemName.equals(m_strWhoHit)) {
			// remove card show
			int k;
			for (i = 0; i < m_arrListCard.size(); i++) {
				for (j = 0; j < card.length; j++) {
					if (((Card) m_arrListCard.elementAt(i)).m_id == card[j]) {

						// init move card to owner
						for (k = 0; k < m_arrHitCard.length; k++) {
							if (((Card) m_arrListCard.elementAt(i)).m_id == m_arrHitCard[k].m_id) {
								m_arrHitCard[k].m_x = ((Card) m_arrListCard.elementAt(i)).m_x;
								m_arrHitCard[k].m_y = ((Card) m_arrListCard.elementAt(i)).m_y;
								break;
							}
						}

						m_arrListCard.removeElementAt(i);
						m_selected--;
						i = -1;
						break;
					}
				}
			}
			// remove card select
			for (i = 0; i < m_arrSelectCard.size(); i++) {
				for (j = 0; j < card.length; j++) {
					if (((Card) m_arrSelectCard.elementAt(i)).m_id == card[j]) {
						m_arrSelectCard.removeElementAt(i);
						i = -1;
						break;
					}
				}
			}
			// init again select card
			if (m_selected >= m_arrListCard.size() || m_selected < 0)
				m_selected = 0;
			if (m_arrListCard.size() <= 0)
				m_selected = -1;
			// sort card
			initPosCardOwner(false, false);
		}

		int pos = findPlayerPos(nick);
		if (pos < 0)
			return;

		m_arrListCardPlayer[pos] -= card.length;
		if (m_arrListCardPlayer[pos] <= 0) {
			m_arrListCardPlayer[pos] = 0;
		}
	}

	private void initPosHitCard(int[] card) {
		int i, j = 0;
		if (m_arrHitCard[0].m_id == -1) {
			m_arrStoreHitCard.removeAllElements();
		}

		float[][] area = new float[4][4];
		int minBound = 120;
		int maxBound = 50;

		int xCenter = GameCanvas.hw;
		int yCenter = GameCanvas.h / 2;
		int wCardLength = (card.length - 1) * m_iUseWCard / 2;
		if (m_arrStoreHitCard.size() > 0) {
			for (i = 0; i < 4; i++) {
				area[i][0] = xCenter - maxBound;
				area[i][1] = xCenter + minBound;
				area[i][2] = yCenter - maxBound;
				area[i][3] = yCenter + minBound;
				/**
				 * 0 3 1 2
				 */
				if (i == 0) {
					area[i][3] = ((Card) m_arrStoreHitCard.elementAt(0)).m_y
							- GameResource.instance.imgCard.getHeight();
					if (area[i][2] > area[i][3])
						area[i][0] = -1;
				} else if (i == 1) {
					area[i][0] = ((Card) m_arrStoreHitCard.elementAt(m_arrStoreHitCard.size() - 1)).m_x + m_iUseWCard
							+ wCardLength;
					if (area[i][0] > area[i][1])
						area[i][0] = -1;
				} else if (i == 2) {
					area[i][2] = ((Card) m_arrStoreHitCard.elementAt(0)).m_y + m_iUseWCard;
					if (area[i][2] > area[i][3])
						area[i][0] = -1;
				} else {
					area[i][1] = ((Card) m_arrStoreHitCard.elementAt(0)).m_x - GameResource.instance.imgCard.getWidth()
							- wCardLength;
					if (area[i][0] > area[i][1])
						area[i][0] = -1;
				}
			}
			int tmpPos = CRes.random(0, 4);
			if (area[tmpPos][0] == -1) {
				j = tmpPos;
				for (i = 0; i < 4; i++) {
					if (area[j][0] != -1) {
						break;
					}
					j = (j + 1) % 4;
				}
				tmpPos = j;
				if (i >= 4) {
					tmpPos = 2;
				}
			}
			xCenter = CRes.random((int) area[tmpPos][0], (int) area[tmpPos][1]);
			yCenter = CRes.random((int) area[tmpPos][2], (int) area[tmpPos][3]);

			area = null;
		} else {
			xCenter = CRes.random(xCenter - maxBound, xCenter + minBound);
			yCenter = CRes.random(yCenter - maxBound, yCenter + minBound);
		}

		if (xCenter < GameCanvas.hw - 30 || xCenter > GameCanvas.hw + 30) {
			xCenter = GameCanvas.hw + CRes.random(30);
		}
		if (yCenter < GameCanvas.hw - 30 || yCenter > GameCanvas.hw + 30) {
			yCenter = GameCanvas.h / 2 - CRes.random(10);
		}

		int pos = findPlayerPos(m_strWhoHit);
		if (pos < 0)
			return;

		j = 0;
		m_arrStoreHitCard.removeAllElements();
		for (i = 0; i < m_arrHitCard.length; i++) {
			if (m_arrHitCard[i].m_id == -1) {
				if (j >= card.length)
					break;

				m_arrHitCard[i].m_id = card[j];

				m_arrHitCard[i].isSpecial = false;
				if (!HDCGameMidlet.m_myPlayerInfo.itemName.equals(m_strWhoHit))
					m_arrHitCard[i].setPos(m_arrPos[pos].x, m_arrPos[pos].y);

				m_arrHitCard[i].xTo = xCenter - wCardLength + j * GameResource.instance.m_imgCards[0].getWidth() / 4;
				m_arrHitCard[i].yTo = yCenter;
				// add to arr
				m_arrStoreHitCard.addElement(m_arrHitCard[i]);
				j++;
			} else
				m_arrHitCard[i].isSpecial = true;
		}
		area = null;
	}

	int m_iSelectLast = -1;
	int m_iXLastCard;
	int m_iYLastCard;

	// TODO update key for move Cards
	@SuppressWarnings("unchecked")
	public void updateKey_MoveCards() {
		if (GameCanvas.isPointerMove) {
			int w = GameResource.instance.m_imgCards[0].getWidth();
			int h = GameResource.instance.m_imgCards[0].getHeight();
			for (int i = 0; i < m_arrListCard.size(); i++) {
				Card c = (Card) m_arrListCard.elementAt(i);
				if (GameCanvas.instance.isMoveCard(c.m_x, c.m_y, w, h)) {
					if (!m_arrSelectCard.contains(c)) {
						flagMove = true;
						c.yTo = m_iyUp;
						m_arrSelectCard.addElement(c);						
					}
				}
			}
			
//			if(flagMove)
//				HDCGameMidlet.instance.Toast("flagMove (updateKey_MoveCards): true");
//			else
//				HDCGameMidlet.instance.Toast("flagMove (updateKey_MoveCards): false");
		}
	}

	// TODO updateKey for move and fire cards
	@SuppressWarnings("static-access")
	public void updateKey_MoveAndFireCard() {
		if (m_arrSelectCard.size() > 0) {
			if (GameCanvas.isPointerDown) {
				Card c = (Card) m_arrSelectCard.elementAt(0);
				int w = GameResource.instance.m_imgCards[0].getWidth();
				int h = GameResource.instance.m_imgCards[0].getHeight();
				if (GameCanvas.instance.isPointer_Down(c.m_x, c.m_y, m_arrSelectCard.size() * w, h)) {
					flagMove_FireCard = true;
				}
			}

			if (flagMove_FireCard && GameCanvas.isPointerMove
					&& m_strWhoTurn.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
				if (GameCanvas.instance.isMoveCard(0, 0, GameCanvas.w, m_iyUp)) {
					flagMove_FireCard = false;
					m_cmdHit.action.perform();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void updateKey() {

		// TODO update key for button game
		if (m_bIsPlaying && m_arrListCard.size() > 0)
			updateKey_ButtonGame();

		if (!m_bIsPlaying || m_bIsDisable) {
			super.updateKey();
			return;
		}

		// move and fire cards
		updateKey_MoveAndFireCard();

		if (GameCanvas.instance.hasPointerEvents()) {

			// move card
			updateKey_MoveCards();

			if (GameCanvas.isPointerClick) {
				int i = m_arrListCard.size() - 1;
				int cW = GameResource.instance.m_imgCards[0].getWidth();
				int cH = GameResource.instance.m_imgCards[0].getHeight();
				int selected = -1;
				Card c = null;
				for (; i >= 0; i--) {
					c = (Card) m_arrListCard.elementAt(i);
					if (c.m_id != -1 && GameCanvas.isPointer(c.m_x, c.m_y, cW, cH)) {
						selected = i;
						c = (Card) m_arrListCard.elementAt(selected);
						if (c.yTo == m_iy) {
							c.yTo = m_iyUp;
							if (!m_arrSelectCard.contains(c))
								m_arrSelectCard.addElement(c);
						}
						else if (!flagMove && c.yTo == m_iyUp) {
							
//							if(flagMove)
//								HDCGameMidlet.instance.Toast("flagMove (click): true");
//							else
//								HDCGameMidlet.instance.Toast("flagMove (click): false");							
							
							c.yTo = m_iy;
							if (!flagMove && m_arrSelectCard.contains(c))
								m_arrSelectCard.removeElement(c);
						}
						break;
					}
				}
												
				if (selected != -1)
					m_selected = selected;
				c = null;
				flagMove = false;
			}

			if (GameCanvas.isPointer(0, 0, GameCanvas.w, m_iyUp)) {
				// m_arrSelectCard.removeAllElements();
				m_cmdBoChon.action.perform();
			}
		}

//		if (GameCanvas.isPointerClick) {
//			int i = m_arrListCard.size() - 1;
//			int cW = GameResource.instance.m_imgCards[0].getWidth();
//			int cH = GameResource.instance.m_imgCards[0].getHeight();
//			int selected = -1;
//			Card c1 = null;
//			for (; i >= 0; i--) {
//				c1 = (Card) m_arrListCard.elementAt(i);
//				if (c1.m_id != -1 && GameCanvas.isPointer(c1.m_x, c1.m_y, cW, cH)) {
//					selected = i;
//					c1 = (Card) m_arrListCard.elementAt(selected);
////					if (c.yTo == m_iy) {
////						c.yTo = m_iyUp;
////						if (!m_arrSelectCard.contains(c))
////							m_arrSelectCard.addElement(c);
////					}
////					else 
//						if (c1.yTo == m_iyUp) {
//						c1.yTo = m_iy;
//						if (m_arrSelectCard.contains(c1))
//							m_arrSelectCard.removeElement(c1);
//					}
//					break;
//				}
//			}
//		}
		
		
		if (GameCanvas.keyPressed[8]) {// di xuong'
			GameCanvas.keyPressed[8] = false;
			Card c = (Card) m_arrListCard.elementAt(m_selected);
			if (c.yTo == m_iyUp) {
				c.yTo = m_iy;
				// if (m_arrSelectCard.contains(c)) {
				// m_arrSelectCard.removeElement(c);
				// }

				actionHandSort();
			} else if (c.m_y == m_iy) {
			}
			c = null;
		}

		if (GameCanvas.keyPressed[2]) {
			GameCanvas.keyPressed[2] = false;
			Card c = (Card) m_arrListCard.elementAt(m_selected);
			if (c.yTo == m_iy) {
				c.yTo = m_iyUp;
				if (!m_arrSelectCard.contains(c))
					m_arrSelectCard.addElement(c);

				if (HDCGameMidlet.m_myPlayerInfo.itemName.equals(m_strWhoTurn)) {
					m_cmdRight = m_cmdBoChon;
				} else {
					m_cmdRight = m_cmdBoChon;
					if (m_arrSelectCard.size() <= 0)
						m_cmdRight = null;
				}
			} else if (c.m_y == m_iyDown) {
				actionHandSort();
			}
			c = null;
		}

		if (GameCanvas.keyPressed[4]) {
			GameCanvas.keyPressed[4] = false;
			swapcard(true);
		}
		if (GameCanvas.keyPressed[6]) {
			GameCanvas.keyPressed[6] = false;
			swapcard(false);
		}
		super.updateKey();
	}

	private void swapcard(boolean left) {
		// TODO Auto-generated method stub
		if (m_arrListCard.size() <= 1)
			return;
		int keep = m_selected;

		if (left) {
			m_selected--;
			if (m_selected < 0)
				m_selected = m_arrListCard.size() - 1;
		} else {
			m_selected++;
			if (m_selected >= m_arrListCard.size())
				m_selected = 0;
		}

		Card card = (Card) m_arrListCard.elementAt(keep);
		if (m_bIsPushSort && m_selected != -1) {
			if (card.m_x == card.xTo) {
				card = (Card) m_arrListCard.elementAt(m_selected);

				Card c = new Card(-1);
				c.CopyTo(card);
				card.CopyTo((Card) m_arrListCard.elementAt(keep));
				((Card) m_arrListCard.elementAt(keep)).CopyTo(c);
				c = null;

				card.xTo = (int) ((Card) m_arrListCard.elementAt(keep)).m_x;
				((Card) m_arrListCard.elementAt(keep)).xTo = (int) card.m_x;
			} else
				m_selected = keep;
		}
		card = null;
	}

	public void onSetTurn(String nick, Message ms) {
		try {
			// openCmd();
			m_strWhoTurn = nick;
			// init time
			m_iGameTick = TURN_TIME_OUT;

			if (m_bIsPlaying) {

				m_bIsMustHitCmd = true;

				if (HDCGameMidlet.m_myPlayerInfo.itemName.equals(nick)) {
					m_iSelectLast = -1;
					if (m_arrSelectCard != null) {

						m_cmdCenter = m_cmdHit;
						actionHandSort();

						m_cmdRight = m_cmdPass;
						if (m_arrSelectCard.size() > 0)
							m_cmdRight = m_cmdBoChon;

						m_bIsMustHitCmd = false;
						if ((m_strWhoHit.length() > 0 && nick.length() > 0 && m_strWhoHit.equals(nick))
								|| m_strWhoHit.length() == 0) {
							m_bIsMustHitCmd = true;

							m_cmdRight = m_cmdBoChon;
							if (m_arrSelectCard.size() <= 0)
								m_cmdRight = null;
						}
					}
				} else {
					if (m_cmdCenter != m_cmdDat) {
						m_cmdCenter = null;
						m_cmdRight = null;
						if (m_arrSelectCard.size() > 0)
							m_cmdRight = m_cmdBoChon;
					} else if (((Card) m_arrListCard.elementAt(m_selected)).yTo != m_iyDown) {
						actionHandSort();
					}
				}
			}

			// reset in a row
			if (m_strWhoHit == null || m_strWhoTurn == null) {
				return;
			}

			if (m_strWhoHit.trim().length() > 0 && !m_strWhoHit.equals("") && nick.trim().length() > 0
					&& !nick.equals("") && m_strWhoHit.equals(nick)) {
				// init for hitcard
				resetCard(m_arrHitCard, -1);
				m_bTypeKillBig = false;
			}
		} catch (Exception e) {
		}
	}

	public void onClearFiredCards() {
		resetCard(m_arrHitCard, -1);
		m_bTypeKillBig = false;
		m_strWhoHit = "";
	}

	private void sortcard() {
		Card[] tmp = new Card[m_arrListCard.size()];
		byte[] b = new byte[m_arrListCard.size()];
		int i, j;
		for (i = 0; i < m_arrListCard.size(); i++) {
			tmp[i] = new Card(((Card) m_arrListCard.elementAt(i)).m_id);
			tmp[i].m_x = ((Card) m_arrListCard.elementAt(i)).m_x;
			b[i] = (byte) ((Card) m_arrListCard.elementAt(i)).m_id;
		}

		CRes.SortCardTL(b, m_bTypeSort);

		for (i = 0; i < m_arrListCard.size(); i++)
			((Card) m_arrListCard.elementAt(i)).m_id = b[i];
		initPosCardOwner(false, false);

		if (m_selected >= m_arrListCard.size())
			m_selected = 0;
		actionHandSort();
		m_bTypeSort = (byte) ((m_bTypeSort + 1) % 3);

		for (i = 0; i < m_arrListCard.size(); i++) {
			for (j = 0; j < tmp.length; j++) {
				if (tmp[i].m_id == ((Card) m_arrListCard.elementAt(j)).m_id && tmp[i].m_id != -1) {
					((Card) m_arrListCard.elementAt(j)).m_x = tmp[i].m_x;
					break;
				}
			}
		}
		tmp = null;
	}

	public void onAllCardPlayerFinish(Message ms) {
		CRes.resetArr(m_arrListCardPlayer);
		// if (m_bIsAnTrang) {
		// resetCard(m_arrHitCard, -1);
		// m_bIsAnTrang = false;
		// }

		String nick = MessageIO.readString(ms);
		if (nick.equals(HDCGameMidlet.m_myPlayerInfo.itemName))
			return;

		byte c[] = MessageIO.readBytes(ms);

		CRes.SortCardTL(c, (byte) 0);

		int i;
		Card card[] = new Card[c.length];
		for (i = 0; i < c.length; i++) {
			card[i] = new Card(c[i]);
		}

		Card out[] = new Card[52];
		for (i = 0; i < 52; i++)
			out[i] = new Card(-1);
		int pos = findPlayerPos(nick);
		initPosEndGame(card, out, pos);

		int j;
		for (i = 0; i < out.length; i++) {
			if (out[i].m_id != -1) {
				for (j = 0; j < m_arrHitCard.length; j++) {
					if (m_arrHitCard[j].m_id == -1) {
						m_arrHitCard[j].CopyTo(out[i]);
						break;
					}
				}
			}
		}

		c = null;
		out = null;
		card = null;
		setOwner(m_strOwnerName);
	}

	private void initPosEndGame(Card[] card, Card[] cardOut, int pos) {
		int i, j = 0, xNext = 0, yNext = 0;
		if (pos < 0)
			return;

		int numCard;
		int HeightCard = m_iUseWCard * 3 / 5;

		// ////////
		if (m_arrPos[pos].x > GameCanvas.hw + 10)
			pos = 3;
		else if (m_arrPos[pos].x < GameCanvas.hw - 10)
			pos = 1;
		else
			pos = 2;

		if (countPlayer() <= 2)
			pos = 2;
		// ///////

		switch (pos) {
		case 3:
		case 1:
			int maximumY = GameCanvas.h - GameCanvas.hBottomBar - m_iUseWCard * 3 / 4 - m_iUseWCard;
			numCard = (maximumY - m_iUseWCard - HeightCard) / HeightCard;

			if (numCard > card.length)
				maximumY = (GameCanvas.h - GameCanvas.hBottomBar) / 2 + (card.length * HeightCard) / 2;
			for (i = 0; i < cardOut.length; i++) {
				if (cardOut[i].m_id == -1) {
					if (j >= card.length)
						break;
					if (yNext > numCard) {
						yNext = 0;
						xNext++;
					}

					cardOut[i].m_id = card[j].m_id;
					cardOut[i].m_x = xNext * m_iUseWCard;
					cardOut[i].yTo = m_iUseWCard + HeightCard + yNext * HeightCard;

					if (pos == 3) {
						cardOut[i].m_x = GameCanvas.w - m_iUseWCard - xNext * m_iUseWCard;
						cardOut[i].yTo = maximumY - yNext * HeightCard;
					}

					cardOut[i].m_y = m_iUseWCard + HeightCard;
					cardOut[i].xTo = (int) cardOut[i].m_x;

					j++;
					yNext++;
				}
			}
			if (pos == 3) {
				int tmpX, tmpY, k;
				for (k = 0; k < j / 2; k++) {

					tmpX = (int) cardOut[(i - j) + k].m_x;
					cardOut[(i - j) + k].m_x = cardOut[i - k - 1].m_x;
					cardOut[i - k - 1].m_x = tmpX;

					tmpX = cardOut[(i - j) + k].xTo;
					cardOut[(i - j) + k].xTo = cardOut[i - k - 1].xTo;
					cardOut[i - k - 1].xTo = tmpX;

					tmpY = cardOut[(i - j) + k].yTo;
					cardOut[(i - j) + k].yTo = cardOut[i - k - 1].yTo;
					cardOut[i - k - 1].yTo = tmpY;

					tmpY = (int) cardOut[(i - j) + k].m_x;
					cardOut[(i - j) + k].m_x = cardOut[i - k - 1].m_x;
					cardOut[i - k - 1].m_x = tmpY;

				}
			}
			break;
		case 2:
			int w = m_iUseWCard / 2;
			int minX = m_iUseWCard + m_iUseWCard;
			numCard = (GameCanvas.w - minX * 2) / w;

			if (numCard > card.length)
				minX = GameCanvas.hw - (card.length * w) / 2;

			for (i = 0; i < cardOut.length; i++) {
				if (cardOut[i].m_id == -1) {
					if (j >= card.length)
						break;
					if (xNext > numCard) {
						xNext = 0;
						yNext++;
					}

					cardOut[i].m_id = card[j].m_id;
					cardOut[i].m_x = minX;
					cardOut[i].m_y = m_top + HeightCard * yNext;

					cardOut[i].xTo = minX + w * xNext;
					cardOut[i].yTo = (int) cardOut[i].m_y;
					j++;
					xNext++;
				}
			}
			break;
		}
	}

	// TODO paint button
	private void paintButton(Graphics g) {

		if (m_strWhoTurn.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
			// paint line button
			g.drawImage(GameResource.instance.imgTienLen_LineButton, 0, GameCanvas.h, Graphics.LEFT | Graphics.BOTTOM);
		}

		int w = (GameCanvas.w / 2 - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;

		// TODO paint button trái (Bỏ chọn)
		PaintPopup.gI().paintButton_Game(g, 0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight,
				idx_BoChon, "Bỏ chọn");

		if (m_strWhoTurn.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
			// TODO paint button phải (Đánh)
			PaintPopup.gI().paintButton_Game(g, GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight, idx_Danh, "Đánh");
			// TODO paint button xếp bài
			PaintPopup.gI().paintButton_Game(g, w,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight, idx_XepBai, "Xếp bài");
			// TODO paint button bỏ lượt
			PaintPopup.gI().paintButton_Game(g, w + GameCanvas.w / 2,
					GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight, idx_BoLuot, "Bỏ lượt");
		}
	}

	// TODO select button
	private boolean selectButton(int x, int y) {
		if (GameCanvas.isPointer(x, y, GameResource.instance.m_frameTienLen_Button.frameWidth,
				GameResource.instance.m_frameTienLen_Button.frameHeight)) {

			return true;
		}
		return false;
	}

	// TODO update key for button game
	private void updateKey_ButtonGame() {

		if (GameCanvas.isPointerDown) {
			// TODO button bỏ chọn
			if (selectButton(0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				idx_BoChon = 1;
			}
			if (m_strWhoTurn.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
				// TODO button bỏ đánh
				if (selectButton(GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth, GameCanvas.h
						- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					idx_Danh = 1;
				}
				int w = (GameCanvas.w / 2 - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;
				// TODO button xếp bài
				if (selectButton(w, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					idx_XepBai = 1;
				}
				// TODO button bỏ lượt
				if (selectButton(w + GameCanvas.w / 2, GameCanvas.h
						- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					idx_BoLuot = 1;
				}
			}
		}

		if (GameCanvas.isPointerClick) {

			// TODO button bỏ chọn
			if (selectButton(0, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
				HDCGameMidlet.instance.m_viberator.vibrate(100);
				idx_BoChon = 0;
				m_cmdBoChon.action.perform();
			}
			if (m_strWhoTurn.equals(HDCGameMidlet.m_myPlayerInfo.itemName)) {
				// TODO button bỏ đánh
				if (selectButton(GameCanvas.w - GameResource.instance.m_frameTienLen_Button.frameWidth, GameCanvas.h
						- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					HDCGameMidlet.instance.m_viberator.vibrate(100);
					idx_Danh = 0;
					m_cmdHit.action.perform();
				}
				int w = (GameCanvas.w / 2 - GameResource.instance.m_frameTienLen_Button.frameWidth) / 2;
				// TODO button xếp bài
				if (selectButton(w, GameCanvas.h - GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					HDCGameMidlet.instance.m_viberator.vibrate(100);
					idx_XepBai = 0;
					m_cmdSort.action.perform();
				}
				// TODO button bỏ lượt
				if (selectButton(w + GameCanvas.w / 2, GameCanvas.h
						- GameResource.instance.m_frameTienLen_Button.frameHeight)) {
					HDCGameMidlet.instance.m_viberator.vibrate(100);
					idx_BoLuot = 0;
					m_cmdPass.action.perform();
				}
			}
		}
	}

	// TODO paint number card for each player
	private void paintNumberCard(Graphics g, int number, int x, int y) {
		if (number >= 10) {
			x -= GameResource.instance.m_frameTienLen_Number.frameWidth / 2;
			GameResource.instance.m_frameTienLen_Number.drawFrame(1, x, y, Sprite.TRANS_NONE, Graphics.HCENTER
					| Graphics.VCENTER, g);
			x += GameResource.instance.m_frameTienLen_Number.frameWidth;
			number = number % 10;
		}
		GameResource.instance.m_frameTienLen_Number.drawFrame(number, x, y, Sprite.TRANS_NONE, Graphics.HCENTER
				| Graphics.VCENTER, g);
	}

	public void paintPlayGame(Graphics g) {
		// TODO paint title
		// Tiến lên miền nam
		if (ListBoardScr.getInstance().m_strTitleGame.trim().equals("TL.Miền Nam")) {
			GameResource.instance.m_frameTienLen_Title.drawFrame(1, GameCanvas.w / 2, GameCanvas.h / 8 * 5,
					Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);
		} else {
			// tiến lên miền bắc
			GameResource.instance.m_frameTienLen_Title.drawFrame(2, GameCanvas.w / 2, GameCanvas.h / 8 * 5,
					Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);
		}

		// TODO paint button
		if (m_bIsPlaying && m_arrListCard.size() > 0)
			paintButton(g);

		int i;
		// //bài đánh ra
		if (m_arrHitCard != null) {
			for (i = 0; i < m_arrHitCard.length; i++) {
				if (m_arrHitCard[i].m_id != -1) {
					m_arrHitCard[i].paint(g);
				}
			}
		}

		// Nhất - nhì - ba - tu
		for (i = 0; i < m_arrPlayers.length; i++) {
			if (m_arrPlayers[i] != null && m_arrPlayers[i].itemId != -1) {
				m_arrPlayers[i].paintRank(g, m_arrPos[i].anchor, scaleRank[i]);
			}
		}

		// paint card cho hiệu ứng chia bài
		if (m_bIsStartDeliver || m_bIsEndDeliver) {
			// vẽ những lá bài chờ trước
			// paintOrtherCard(g, true);
			if (m_bIsStartDeliver) {
				g.drawImage(GameResource.instance.imgTienLen_AllCards,
						(GameCanvas.w - GameResource.instance.m_imgCards[0].getWidth()) >> 1,
						(GameCanvas.h - GameResource.instance.m_imgCards[0].getHeight()) >> 1, Graphics.LEFT
								| Graphics.TOP);
			}
			// vẽ những lá bài bay ra phía sau
			paintOrtherCard(g, false);
		} else {
			// lá bài còn lại của mỗi người chơi
			float x, y;
			for (i = 0; i < m_iMaxPlayer; i++) {
				if (m_arrListCardPlayer[i] > 0 && i != m_iMyPosInVt) {
					// TODO paint số cây bài
					x = m_arrOtherCards[i][0].m_x;
					y = m_arrOtherCards[i][0].m_y;
					g.drawRotateScaleImage(GameResource.instance.imgTienLen_CardLight, x, y, Graphics.HCENTER
							| Graphics.VCENTER, (float) GameResource.instance.imgTienLen_CardLight.getWidth(),
							(float) GameResource.instance.imgTienLen_CardLight.getHeight(), m_arrOtherCards[i][0].anpha);
					paintNumberCard(g, m_arrListCardPlayer[i], (int) x, (int) y);
				}
			}
		}

		// Bài của mình

		if (m_arrListCard != null && m_arrListCard.size() > 0) {
			for (i = 0; i < m_arrListCard.size(); i++) {
				if (((Card) m_arrListCard.elementAt(i)).m_id != -1) {
					((Card) m_arrListCard.elementAt(i)).paint(g);
				}
			}
		}

		// TODO paint move cards to fire
		// if(flagMove_FireCard && m_arrSelectCard.size() > 0){
		// int yMove = GameCanvas.instance.pyLast;
		// int xMove = GameCanvas.instance.pxLast;
		// int w = GameResource.instance.m_imgCards[0].getWidth()/2;
		// for(i = 0 ; i < m_arrSelectCard.size();i++){
		// ((Card)m_arrSelectCard.elementAt(i)).paintMoveCard(g, xMove + i*w/2,
		// yMove);
		// }
		// }
	}

	private void paintOrtherCard(Graphics g, boolean isStand) {
		int i, j;
		int x = (GameCanvas.w - GameResource.instance.m_imgCards[0].getWidth()) >> 1;
		int y = (GameCanvas.h - GameResource.instance.m_imgCards[0].getHeight()) >> 1;
		for (i = 0; i < m_iMaxPlayer; i++)
			if (i != m_iMyPosInVt && m_arrPlayers[i].itemId != -1)
				for (j = 0; j < m_iMaxNumCard; j++)
					if (m_arrOtherCards[i][j].m_id != -1 && !m_arrOtherCards[i][j].isCallMove)
						if (m_arrOtherCards[i][j].m_x != x || m_arrOtherCards[i][j].m_y != y)
							m_arrOtherCards[i][j].paint(g);
	}

	public void actionEndGame() {
		super.actionEndGame();
		resetCard(m_arrHitCard, -1);
		// m_arrCardEnd.removeAllElements();
		m_bTypeKillBig = false;
		m_arrListCard.removeAllElements();
		m_arrSelectCard.removeAllElements();
		m_selected = -1;
		for (int i = 0; i < m_arrPlayers.length; i++) {
			if (m_arrPlayers[i].itemId != -1) {
				m_arrPlayers[i].setRank(-1);
			}
		}
	}

	private void actionHandSort() {
		for (byte i = 0; i < m_arrListCard.size(); i++)
			if (((Card) m_arrListCard.elementAt(i)).yTo > m_iy)
				((Card) m_arrListCard.elementAt(i)).yTo = m_iy;

		m_bIsPushSort = false;
		m_cmdCenter = null;
		m_cmdRight = null;
		if (HDCGameMidlet.m_myPlayerInfo.itemName.equals(m_strWhoTurn)) {

			m_cmdCenter = m_cmdHit;
			if (m_bIsMustHitCmd) {
				m_cmdRight = m_cmdBoChon;
				if (m_arrSelectCard.size() <= 0)
					m_cmdRight = null;
			} else {
				m_cmdRight = m_cmdPass;
				if (m_arrSelectCard.size() > 0)
					m_cmdRight = m_cmdBoChon;
			}
		} else {
			m_cmdRight = m_cmdBoChon;
			if (m_arrSelectCard.size() <= 0)
				m_cmdRight = null;
		}
	}

	public void displayThui13Cay(String nickPlayer) {
		int pos = findPlayerPos(nickPlayer);

		if (pos < 0)
			return;

		FlyImage f = new FlyImage();
		f.startEffect(m_arrPos[pos].x, m_arrPos[pos].y, GameResource.instance.imgWin_Thui_13_La);

		f = null;
	}

	public void playerLeave(String strLeave) {
		int pos = findPlayerPos(strLeave);
		if (pos < 0)
			return;
		if (pos >= 0 && pos < m_arrListCardPlayer.length) {
			m_arrListCardPlayer[pos] = -1;
		}
		super.playerLeave(strLeave);
	}

	public void displayWinnerLoser(Message message) {
		String nick = MessageIO.readString(message);
		int rank = MessageIO.readInt(message);

		int pos = findPlayerPos(nick);
		if (pos < 0)
			return;
		if (rank > 0 && rank <= 4)
			m_arrPlayers[pos].setRank(rank - 1);
		nick = null;
	}

	public void displayCard(Message message) {
		int total = MessageIO.readByte(message);
		String nick;
		for (int i = 0; i < total; i++) {
			nick = MessageIO.readString(message);
			int num = MessageIO.readByte(message);
			int pos = findPlayerPos(nick);
			if (pos < 0)
				return;
			if (pos >= 0 && pos < m_arrListCardPlayer.length) {
				m_arrListCardPlayer[pos] = num;
			}
		}
		nick = null;
	}

	public void checkNoFireCard(Message message) {
		// GameCanvas.startOKDlg(MessageIO.readString(message));
		HDCGameMidlet.instance.showDialog_Okie("Thông báo", MessageIO.readString(message));
		int i, j;
		for (i = 0; i < m_arrSelectCard.size(); i++) {
			for (j = 0; j < m_arrListCard.size(); j++) {
				if (((Card) m_arrSelectCard.elementAt(i)).m_id == ((Card) m_arrListCard.elementAt(j)).m_id) {
					((Card) m_arrListCard.elementAt(j)).yTo = m_iyUp;
					((Card) m_arrListCard.elementAt(j)).m_y = m_iyUp;
					break;
				}
			}
			if (j >= m_arrListCard.size()) {
				m_arrSelectCard.removeElementAt(i);
				i = -1;
			}
		}
	}

	@Override
	public void doMenu() {
		// TODO Auto-generated method stub
		HDCGameMidlet.instance.Toast("doMenu - TienLen");
		if (m_cmdLeft != null)
			m_cmdLeft.action.perform();
		else
			GameCanvas.instance.menu.m_list = null;
	}

	@Override
	public void doBack() {
		// TODO Auto-generated method stub
		if (cmdBack != null)
			cmdBack.action.perform();

	}

	@Override
	public void onUserJoinTable(int tbID, String nick, boolean isChampion, int pos, long money, int level,
			int avatarId, int playerStatus) {
		super.onUserJoinTable(tbID, nick, isChampion, pos, money, level, avatarId, playerStatus);
		initLastCard();
		int i;
		for (i = 0; i < m_iMaxPlayer; i++) {
			if (m_arrPlayers[i].itemId == -1 || i == m_iMyPosInVt)
				continue;
			m_arrOtherCards[i][0].m_id = 0;
			m_arrOtherCards[i][0].isUpCard = true;
			m_arrOtherCards[i][0].setPos(m_arrOtherCards[i][0].xTo, m_arrOtherCards[i][0].yTo,
					m_arrOtherCards[i][0].xTo, m_arrOtherCards[i][0].yTo);
		}
	}
}
