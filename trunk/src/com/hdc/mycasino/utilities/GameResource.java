package com.hdc.mycasino.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.Vector;

import android.graphics.drawable.Drawable;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.customcontrol.CustomGalleryImageAdapter;
import com.hdc.mycasino.font.BitmapFont;
import com.hdc.mycasino.model.CardData;

public class GameResource {
	public static GameResource instance = new GameResource();

	public GameResource gI() {
		if (instance == null) {
			instance = new GameResource();
		}
		return instance;
	}

	public static final String plzWait = "Vui lòng chờ";
	public static final String menu = "Menu";
	public static final String update = "Cập nhật";
	public static final String close = "Đóng";
	public static final String closeTab = "Đóng tab";
	public static final String doYouWantGoToForum = "Bạn có muốn chuyển đến diễn đàn không?";
	public static final String doYouWantGoToUpdate = "Bạn có muốn đến trang cập nhật \n phiên bản mới không ?";
	public static final String forum = "Diễn đàn";
	public static final String cleanCache = "Xóa dữ liệu";
	public static final String introduction = "Giới thiệu";
	public static final String accept = "Đồng ý";
	public static final String chargeMoney = "Nạp Tiền";
	public static final String del = "Xóa";
	public static final String yes = "Đồng ý";
	public static final String no = "Không";
	public static final String info = "Thông tin";
	public static final String exit = "Thoát";
	public static final String chat = "Chat";
	public static final String makeFriend = "Kết bạn";
	public static final String OK = " Đóng ";
	public static final String gold = "Gold";
	public static final String dina = "Dina";
	public static final String benefit = "Quyền lợi";
	public static final String select = "Chọn";
	public static final String selectImage = "Chọn hình đại diện :";
	public static final String doYouWantExit = "Bạn có muốn thoát không?";
	public static final String disconnect = "Mất kết nối";
	public static final String checkConnection = "Không thể kết nối, kiểm tra đường truyền";
	public static final String signIn = "Đăng nhập";
	public static final String register = " Đăng ký ";
	public static final String remember = "Nhớ";
	public static final String rememberInfo = "Nhớ mật khẩu";
	public static final String plzInputInfo = "Vui lòng nhập đầy đủ thông tin";
	public static final String incorrectPass = "Mật khẩu không trùng khớp";
	public static final String specialCharNotAllow = "Tài khoản không được có ký tự đặt biệt";
	public static final String username = "Tên";
	public static final String password = "Mật khẩu    ";
	public static final String smsPrefix = "sms://";
	public static final String smsSent = "Gửi tin nhắn thành công";
	public static final String sendSMSFail = "Gửi tin nhắn thất bại";
	public static final String forgetPass = "Quên mật khẩu";
	public static final String nhapLai = "Nhập lại";
	public static final String changePass = "Quên MK";
	public static final String to_bai = "Tố";
	public static final String invalid = "Không hợp lệ";
	public static final String moneyInvalid = "Tiền cược không hợp lệ";
	public static final String skip = "Bỏ";
	public static final String back = "Quay lại";
	public static final String getIn = " đã vào phòng chat";
	public static final String getOut = " đã rời phòng chat";
	public static final String nameClan = "Tên gia tộc: ";
	public static final String status = "Status: ";
	public static final String imageClan = "Hình đại diện: ";
	public static final String timeUsed = "Thời hạn: ";
	public static final String date = " ngày";
	public static final String used = "Sử dụng: ";
	public static final String useAvatar = "Sử dụng Avatar";
	public static final String useGolds = "Sử dụng";
	public static final String count = " lần";
	public static final String endDate = "Hết hạn";
	public static final String ends = "HSD: ";
	public static final String end = "Hết";
	public static final String have = "Còn: ";
	public static final String work = "Tác dụng: ";
	public static final String buy = "Mua";
	public static final String buyAvatar = "Mua Avatar";
	public static final String buyItem = "Mua Vật phẩm";
	public static final String friend = "Bạn bè";
	public static final String detail = "Xem chi tiết";
	public static final String sendSMS = "Gửi tin nhắn";
	public static final String remove = "Xóa tin chọn";
	public static final String moveGold = "Chuyển gold";
	public static final String inviteClan = "Mời vào bang";
	public static final String owner = "Cá nhân";
	public static final String clan = "Gia tộc";
	public static final String king = "Thần bài";
	public static final String guide = "Hướng dẫn";
	public static final String topPlayer = "Cao thủ";
	public static final String topMoney = "Đại gia";
	public static final String top = "Thập đại cao thủ";
	public static final String mail = "Hòm thư";
	public static final String removeList = "Xóa tin đã đọc";
	public static final String removeAll = "Xóa tất cả";
	public static final String emptyMail = "Hòm thư rỗng";
	public static final String read = "Đọc";
	public static final String deni = "Từ chối";
	public static final String from = "Từ: ";
	public static final String join = "Gia nhập";
	public static final String edit = "Chỉnh sửa";
	public static final String save = "Lưu";
	public static final String saveMoney = "Đóng góp tiền";
	public static final String savePoint = "Đóng góp điểm";
	public static final String out = "Rời khỏi gia tộc";
	public static final String points = "Điểm tích lũy: ";
	public static final String member = "Số thành viên: ";
	public static final String clanOwner = "Tộc chủ: ";
	public static final String haveGold = "Tài sản gold: ";
	public static final String space = ": ";
	public static final String point = "Điểm";
	public static final String money = "Tiền";
	public static final String level = "Level";
	public static final String nick = "Nick";
	public static final String passKing = "Thần bài tuần trước";
	public static final String send = "Gửi";
	public static final String joinCPTT = " đã vào phòng thi đấu";
	public static final String outCPTT = " đã rời phòng thi đấu";
	public static final String joinRoom = " đã vào phòng chat";
	public static final String outRoom = " đã rời phòng chat";
	public static final String infoCom = "Thông tin giải đấu";
	public static final String shop = "Cửa hàng";
	// TODO save info
	public static final String saveInfo = "Lưu thông tin";
	public static int MAX_LENGTH = 30;

	public static final String[] list_symbols = {":)","^_^","-_-","x.x",":D","^^","+_+","~.~","-.-"};
	
	public static final String[] listNameAvatar = { "Dân chơi", "Tự tin", "Cảnh sát", "Thất tình", "Thầy giáo",
			"Doanh nhân", "Khủng bố", "Tóc biếc", "Hip hop", "Loạn thị", "Y tá", "Sát thủ", "Trung niên", "Già cả",
			"Dễ thương", "Trẻ trung", "Cô đơn", "Năng động", "Thùy mị", "Mọt sách" };

	public static final String[] infoEndGame = { "Hạng", "Tên", "Tiền", "KQ" };

	public String[] listLevel = { "Mới chơi", "Tập chơi", "Nâng cao", "Phong trào", "Nghiệp dư", "Chuyên nghiệp",
			"Sư bài", "Thánh bài", king, "Huyền thoại", "Thần thoại" };

	public static int[] listColor = { 0xffffff, 0x05fdf4, 0x9dfba5, 0x15ff0f, 0xf3fc0a, 0xfcaa82, 0xfdb505, 0xff7d0d,
			0xf47ffb, 0xfd05d4, 0xff0000 };

	public static int[] listColorMessenger = { 0xffffff, 0xff0000, 0xffff00, 0x00ff00, 0x00ffff, 0x0000ff, 0xff00ff,
			0xdf0024, 0xff7f27, 0x880015, 0x7f7f7f, 0x000000, 0xc3c3c3, 0xb97a57, 0xffaec9, 0xffc90e, 0xefe4b0,
			0xb5e61d, 0x99d9ea, 0x7092be, 0xc8bfe7, 0x9e8b75, 0x332a29, 0x004b18, 0xe3007b, 0x3d107b, 0x00a8ec,
			0x009f3c, 0xfff99d, 0xec6b76, 0x7a7700 };

	public static final String guideTLMN = "Luật chơi:"
			+ "\n - Mỗi người được có nhiệm vụ sắp xếp 13 cây bài thành nhóm bài (như đã nói ở trên) hoặc theo tính toán của mình để giành chiến thắng. "
			+ "\n - Chơi theo vòng chiều kim đồng hồ, người vòng sau phải đánh ra lá bài hoặc nhóm bài mạnh hơn lá/nhóm bài người vòng trước đã đánh. Ai hết bài trước sẽ chiến thắng."
			+ "\n Cách tính tiền trong game:"
			+ "\n - Nếu chơi 4 người sau khi xác định được thứ tự Nhất, Nhì, Ba, Bét, tiền thưởng, phạt của mỗi người được tính như sau : Người Nhất : được thêm 100% số tiền cược quy định của phòng, người Nhì được 50% tiền cược, người Ba mất 50% số tiền cược, người Bét mất 100% tiền cược."
			+ "\n - Nếu chơi 3 người, Người nhất được 150% số tiền cược, nhì mất 50% số tiền cược, ba mất 100% số tiền cược. "
			+ "\n - Nếu chơi 2 người nhất được 100%; nhì mất 100% số tiền cược. "
			+ "\n Ví dụ: 4 người chơi, cược 1000, tỉ lệ như sau: nhất ăn 1000, nhì ăn 500, ba thua 500, chót thua 1000. 3 người chơi, nhất ăn 1500, nhì thua 500, bét thua 1000. Chơi 2 người: nhất ăn 1000, nhì mất 1000. "
			+ "\n - Thuế mỗi ván chơi là 5% và thuế sẽ được áp dụng cho tất cả các khoản tiền trong ván chơi (tiền cược, phạt, chặt, thúi heo). "
			+ "\n - Tiền cược thắng thua sẽ được trừ sau khi kết thúc ván chơi, thay vì trừ trước ván như bây giờ, chặt, phạt sẽ được tính ngay lúc xảy ra.";

	public static final String guideCAO = "Luật chơi: "
			+ "\n - Tính tổng điểm 3 lá bài và lấy số hàng đơn vị làm điểm. Trường hợp tổng điểm của người chơi là 10, 20, 30 thì lấy là 0."
			+ "\n - So sánh điểm giữa Chương và từng người chơi : Điểm cao hơn thì thắng. Điểm bằng nhau thì hòa."
			+ "\n Cách tính tiền trong game:"
			+ "\n - Người thắng sẽ được của chương bằng số tiền cược, trừ đi 5% thuế. Người thua mất cho chương bằng số tiền cược, chương được nhận tiền cược sau khi khấu trừ thuế. ";

	public static final String guideTLMB = "Luật chơi: "
			+ "\n - Mỗi người được nhận 13 lá bài bất kỳ, người chơi sắp xếp 13 lá thành nhóm hoặc theo tính toán của mình."
			+ "\n - Chơi theo lượt và vòng chiều kim đồng hồ, người vòng sau phải đánh ra lá bài hoặc nhóm bài mạnh hơn lá/nhóm bài người vòng trước đã đánh. Nếu người sau không có quân bài để chặn người trước, chịu thôi và không được tham gia tiếp vòng sau cho đến khi lượt đánh bài kết thúc. Ai hết bài trước sẽ chiến thắng, không được đánh cây 2 cuối cùng.( 2 cuối cùng sẽ bị thối)"
			+ "\n Cách tính tiền trong game:"
			+ "\n - Nếu chơi 4 người,sau khi xác định được thứ tự Nhất, Nhì, Ba, Bét, tiền thưởng, phạt của mỗi người được tính như sau : Người Nhất : được thêm 100% số tiền cược quy định của phòng, người Nhì được 50% tiền cược, người Ba mất 50% số tiền cược, người Bét mất 100% tiền cược."
			+ "\n - Nếu chơi 3 người, Người nhất được 150% số tiền cược, nhì mất 50% số tiền cược, ba mất 100% số tiền cược."
			+ "\n - Nếu chơi 2 người: nhất được 100%; nhì mất 100% số tiền cược."
			+ "\n Ví dụ: 4 người chơi, cược 1000, tỉ lệ như sau: nhất ăn 1000, nhì ăn 500, ba thua 500, chót thua 1000. 3 người chơi, nhất ăn 1500, nhì thua 500, bét thua 1000. Chơi 2 người: nhất ăn 1000, nhì mất 1000."
			+ "\n - Thuế mỗi ván chơi là 5% và thuế sẽ được áp dụng cho tất cả các khoản tiền trong ván chơi (tiền cược, phạt, chặt, thúi heo)."
			+ "\n - Tiền cược thắng thua sẽ được trừ sau khi kết thúc ván chơi, thay vì trừ trước ván như bây giờ, chặt, phạt sẽ được tính ngay lúc xảy ra.";
	public static final String guidePHOM = "Luật chơi: "
			+ "\n - Người chơi sẽ đánh bài rác và ăn bài của người chơi khác \n để tạo thành Phỏm."
			+ "\n - 1 Phỏm gồm 3 lá bài trở lên cùng chất liên tiếp hoặc cùng số."
			+ "\n - Ai hết bài rác sẽ ù. Nếu không ai ù ván bài sẽ kết thúc sau 4 vòng đánh."
			+ "\n Cách tính tiền trong game:"
			+ "\n - Bàn chơi 2 người : \n + Bét mất 1 lần tiền cược của bàn.\n + Nhất Được 1 lần tiền cược của bàn sau khi trừ thuế"
			+ "\n - Bàn chơi 3 người : \n + Bét Mất 2 lần tiền cược.\n + Nhì mất 1 lần tiền cược.\n + Nhất Ăn tất cả (bị trừ 5% thuế)"
			+ "\n - Bàn chơi 4 người : \n + Bét mất 3 lần tiền cược.\n + Ba mất 2 lần tiền cược.\n + Nhì mất 1 lần tiền cược.\n + Nhất nhận được tất cả số tiền sau khi trừ thuế"
			+ "\n Móm mất 4 lần tiền cược"
			+ "\n - Khi có user ù, mỗi user còn lại mất 5 lần tiền cược cho người ù."
			+ "\n Ù đền : User A đánh cho user B ăn 3 cây, user B Ù \n=> User A phải đền số tiền thua của cả bàn cho user B.\n Khi đó, những user còn lại không bị trừ tiền Ù"
			+ "\n - Cho ăn lá đầu tiên mất 1 lần tiền cược" + "Cho ăn lá thứ 2 mất 2 lần tiền cược"
			+ "\n - Cho ăn lá thứ 3 mất 3 lần tiền cược" + "Cho ăn chốt mất 4 lần tiền cược.";
	public static final String guideXITO = "Luật chơi: "
			+ "\n - Trước khi chia bài, mỗi người chơi bỏ ra 1 số tiền cược bằng nhau (bằng tiền cược quy định của phòng chơi)."
			+ "\n - Mỗi người được chia 2 lá bài, trong đó có 1 cây lật và 1 cây úp không cho những người chơi khác nhìn thấy."
			+ "\n - Người có cây bài lật cao nhất có quyền đặt cược, quy định tiền cược cho lượt tiếp theo (Tố)."
			+ "\n - Những người chơi còn lại có thể chọn Theo hoặc Úp bỏ."
			+ "\n - Sau mỗi vòng tố, mỗi người được nhận 1 lá bài, tiếp tục Tố cho tới khi nhận đủ 5 lá. Trước khi người chơi nhận đủ bài, chỉ còn lại 1 người chơi chưa Úp bỏ, người chơi đó sẽ thắng và nhận được số tiền cược trên bàn."
			+ "\n - Sau khi đã chia đủ 5 quân bài thì cơ hội đặt cược (tố) là như nhau. Lúc này người chơi có thể tiếp tục tố,theo hoặc bỏ. Nếu tiếp tục tố thì tiền cược sẽ cộng dồn, nếu theo thì sẽ bỏ ra so bài (chỉ so bài khi mọi người đều theo vòng đó)."
			+ "\n - Người có bộ bài mang giá trị cao nhất sẽ thắng cuộc. Nếu các lá bài giống nhau sẽ xử hòa."
			+ "\n Cách tính tiền trong game:" + "\n Người thắng sẽ được tất cả số tiền cược, trừ đi 5% thuế.";

	public static final String guideClan = "\n - Quen biết thêm được nhiều người bạn mới trên mọi miền tổ quốc.\n \n - Lúc buồn có thể vào tám thả ga với những thành viên trong \n gia tộc của mình mọi lúc mọi nơi."
			+ "\n \n - Khi chơi bài cùng nhau sẽ được giảm thuế.\n \n - Khi chuyển Gold cho người cùng gia tộc sẽ được giảm \n 50% phí chuyển Gold. "
			+ "\n - Quen biết thêm được nhiều người bạn mới trên mọi miền tổ quốc.\n \n - Lúc buồn có thể vào tám thả ga với những thành viên trong \n gia tộc của mình mọi lúc mọi nơi."
			+ "\n \n - Khi chơi bài cùng nhau sẽ được giảm thuế.\n \n - Khi chuyển Gold cho người cùng gia tộc sẽ được giảm \n 50% phí chuyển Gold. "
			+ "\n - Quen biết thêm được nhiều người bạn mới trên mọi miền tổ quốc.\n \n - Lúc buồn có thể vào tám thả ga với những thành viên trong \n gia tộc của mình mọi lúc mọi nơi."
			+ "\n \n - Khi chơi bài cùng nhau sẽ được giảm thuế.\n \n - Khi chuyển Gold cho người cùng gia tộc sẽ được giảm \n 50% phí chuyển Gold. "
			+ "\n - Quen biết thêm được nhiều người bạn mới trên mọi miền tổ quốc.\n \n - Lúc buồn có thể vào tám thả ga với những thành viên trong \n gia tộc của mình mọi lúc mọi nơi."
			+ "\n \n - Khi chơi bài cùng nhau sẽ được giảm thuế.\n \n - Khi chuyển Gold cho người cùng gia tộc sẽ được giảm \n 50% phí chuyển Gold. "
			+ "\n - Quen biết thêm được nhiều người bạn mới trên mọi miền tổ quốc.\n \n - Lúc buồn có thể vào tám thả ga với những thành viên trong \n gia tộc của mình mọi lúc mọi nơi."
			+ "\n \n - Khi chơi bài cùng nhau sẽ được giảm thuế.\n \n - Khi chuyển Gold cho người cùng gia tộc sẽ được giảm \n 50% phí chuyển Gold. "
			+ "\n - Quen biết thêm được nhiều người bạn mới trên mọi miền tổ quốc.\n \n - Lúc buồn có thể vào tám thả ga với những thành viên trong \n gia tộc của mình mọi lúc mọi nơi."
			+ "\n \n - Khi chơi bài cùng nhau sẽ được giảm thuế.\n \n - Khi chuyển Gold cho người cùng gia tộc sẽ được giảm \n 50% phí chuyển Gold. ";

	public static final String guideCompetition = "\n - Giải thi đấu của mỗi game bài sẽ diễn ra vào lúc 8h sáng \n các ngày chủ nhật hàng tuần, "
			+ "\n - Thời gian đăng kí sẽ bắt đầu từ thứ hai cho đến khi đủ số lượng người \n đăng kí hoặc trước lúc thi đấu. Số gold để đăng kí thi đấu là 10000 gold. "
			+ "\n - Các thành viên sẽ được sắp xếp tự động vào các bàn chơi,\n và sẽ đấu loại trực tiếp. Người chiến thắng ở vòng đấu cuối cùng \n sẽ trở thành nhà vô địch của game bài đó. "
			+ "\n - Khi trở thành nhà vô địch, server sẽ thông báo đến tất cả thành viên \n trong game, đồng thời avatar của bạn sẽ có thêm vương niệm của vua bài, "
			+ "\n - Bạn sẽ được miễn thuế và không bị kick khi đang chơi.";

	public Image imgLogo;
	public Image imgWaiting;
	public Image imgArrow;
	public Image imgMenuBar;
	public FrameImage imgRoom;
	public Image imgBox;
	public Image imgBoxFocus;
	public FrameImage frameCheck;
	public Image imgFrames;
	public Image imgTableFourPlayer;
	public Image imgGirl;
	public Image imgHandGirl;
	public Image imgEyeGirl;
	public Image imgArrowBig;
	public Image imgIconCard;
	public FrameImage imgMakeFriend;
	public Image imgCard;
	public Image imgBackPlay;
	public Image imgCardUpside;
	// public Image imgCard2;
	// public Image imgCardUpside2;
	public FrameImage imgIconMail;
	public Image imgCoinIcon;

	// public Image imgEffectFirework[] = new Image[4];
	public FrameImage imgChatNotify;

	public Image imgRankGame;
	public FrameImage m_frameWaiting;
	public FrameImage m_frameLabai;
	public FrameImage m_frameMouthGirl;
	public FrameImage m_frameIconMenu;
	public FrameImage m_frameIconGame;
	public FrameImage m_frameIconLan;
	public FrameImage frameItems;
	public FrameImage frameStatusRoom;
	public FrameImage frameClans;

	// TODO Login
	// logo casino
	public Image imgBackRound;
	public Image imgBackRound_2;
	public Image imgLogoCasino;
	public Image imgLight_1; // chui bóng đèn
	public Image imgLight_2; // ánh sáng đèn
	public Image imgBulb;

	// TODO Text field
	public Image imgTextField_Disable; // textfield disable
	public Image imgTextField_Enable; // textfield enable
	public FrameImage m_frameTextField_IconEdit; // icon edit

	public Image imgRememberInfo_Bg; // background ghi nhớ thông tin
	public FrameImage m_frameRememberInfo_Check; // frame ghi nhớ thông tin

	// TODO MENU
	public Image imgMenuDisable;
	public Image imgMenuEnable;
	public Image imgMenuBg;
	public FrameImage m_frameMenuIcon;
	public FrameImage m_frameMenuListIconEnable;
	public FrameImage m_frameMenuListIconDisable;
	// NEW
	public FrameImage m_frameMenuBgGame;
	public FrameImage m_frameMenuButton;
	public FrameImage m_frameMenuIconGame;
	public Image imgMenu_ContextMenu_1;
	public Image imgMenu_ContextMenu_2;
	public Image imgMenu_ContextMenu_3;
	public Image imgMenu_ContextMenu_HighLight;
	public FrameImage m_frameMenu_Context_Close;
	public FrameImage m_frameMenu_ButtonBack;

	// TODO Button
	public Image imgButton_Login;// button cho đăng nhập
	public Image imgHightLight_1; // hight light effect 1
	public Image imgHightLight_2; // hight light effect 2
	public Image imgHightLight_3; // hight light effect 3
	public Image imgButton_HighLight; // button high light

	// TODO Popup
	public Image imgPoupPanel;
	public Image imgPoupButton;
	public Image imgPopupLine;
	public Image imgPopupLine_1;
	public Image imgPopupArrow;
	public Image imgPopupArrow_Rotate;
	public Image imgPopupPop;

	// TODO header
	public Image imgHeaderBg;
	public Image imgHeaderDinaBg;
	public FrameImage m_frameHeaderIcon_DinaGold;
	public FrameImage m_frameHeaderMsgIcon;
	// NEW
	public Image imgHeaderTop;
	public Image imgHeaderBottom;
	public FrameImage m_frameHeaderCheck;

	// TODO Effect Icon
	public Image imgEffectIcon_HightLight;
	public FrameImage m_frameEffectIcon_Large;
	public FrameImage m_frameEffectIcon_Medium;
	public FrameImage m_frameEffectIcon_Small_1;
	public FrameImage m_frameEffectIcon_Small_2;
	public FrameImage m_frameEffectIcon_Text_Menu;
	public Image imgEffectIcon_Shadow;
	public Image imgEffectIcon_TinNhan;

	// TODO List Screen
	public Image imgListScr_Panel;
	public Image imgListScr_HightLight;
	public Image imgListScr_ScrollBar;
	public Image imgListScr_ScrollBar_Ngang;

	// TODO Scroll
	public Image imgScrollBar;
	public FrameImage m_frameScrollPanel;

	// TODO tabs
	public Image imgTabs_HightLightRow;
	public Image imgTabs_1_0;
	public Image imgTabs_2_0;
	public Image imgTabs_2_1;
	public Image imgTabs_3_0;
	public Image imgTabs_3_1;
	public Image imgTabs_3_2;
	public Image imgTabs_4_0;
	public Image imgTabs_4_1;
	public Image imgTabs_4_2;
	public Image imgTabs_4_3;

	// TODO avatar
	public FrameImage m_frameAvatar_IconBlue;
	public FrameImage m_frameAvatar_IconRed;
	public FrameImage m_frameAvatar_IconYellow;
	public Image imgAvatar_Khung;
	public Image imgAvatar_Khung_High_Light;
	public FrameImage m_frameAvatar_IconAvatar;
	// NEW
	public FrameImage m_frameAvatar_IconAvatar_1;
	public Image imgAvatar;

	// TODO mail
	public FrameImage m_frameMail_HomThu;

	// TODO vật phẩm
	public FrameImage m_frameVatPham_Icon;

	// TODO gia tộc
	public FrameImage m_frameGiaToc_Icon;
	public FrameImage m_frameGiaToc_ThanhLap;
	public Image imgGiaToc_HighLight;
	public Image imgGiaTocStar;

	// TODO chat
	public Image imgChat_Man;
	public Image imgChat_Panel;
	public FrameImage m_frameChat_IconGui;

	// TODO room
	public FrameImage m_frameRoom_IconGiaToc;
	public FrameImage m_frameRoom_IconPhong;
	public FrameImage m_frameRoom_IconTable;
	public FrameImage m_frameRoom_IconUser;
	// NEW
	public FrameImage m_frameRoom_IconRoom;

	// TODO Tiến lên
	public Image imgTienLen_Table;
	public FrameImage m_frameTienLen_PanelInfo;
	public Image imgTienLen_Star;
	public Image imgTienLen_Ready;
	public Image imgTienLen_Chat;
	public FrameImage m_frameTienLen_Button;
	public FrameImage m_frameTienLen_Start_Ready_1;
	public FrameImage m_frameTienLen_Start_Ready_2;
	public Image[] m_imgCards;
	public Image imgTienLen_CardBack;
	public Image imgTienLen_CardBack2;
	public Image imgTienLen_Time_Bg;
	public Image imgTienLen_Time_Line;
	public FrameImage m_frameTienLen_Number;
	// NEW TIẾN LÊN
	public Image imgTienLen_User;
	public Image imgTienLen_KhungUser;
	public FrameImage m_frameTienLen_IconUser;
	public Image imgTienLen_LineButton;
	public FrameImage m_frameTienLen_Title;
	// new card tien len
	public Image imgTienLen_CardLight;
	public Image imgTienLen_AllCards;
	public Image imgTienLen_TimeOutLight;

	// TODO Win
	public Image imgWin_Nhat;
	public Image imgWin_Nhi;
	public Image imgWin_Ba;
	public Image imgWin_Bet;
	public Image imgWin_Thui_13_La;
	public Image imgWin_ChatDe;
	public Image imgWin_ChatHeo;
	public FrameImage m_frameWin_Chip;
	public Image imgWin_BaTay;
	public Image imgWin_Xam;
	public Image imgWin_Thang;
	public Image imgWin_Thua;
	// NEW
	public FrameImage m_frameWin_Icon;
	public FrameImage m_frameWin_HighLightWin;
	public Image imgWin_To;
	public Image imgWin_ToThem;
	public Image imgWin_Theo;
	public Image imgWin_UpBo;

	// TODO bài cào
	public Image imgBaiCao_Table;
	public Image imgBaiCao_Diem;
	public FrameImage m_frameBaiCao_Number;
	public Image imgBaiCao_IconHand;

	public Image imgABC;

	// TODO xì tố
	public Image imgXiTo_Table;
	public Image imgXiTo_Box;
	public FrameImage m_frameXiTo_Text;

	// TODO phỏm
	public Image imgPhom_Table;
	public Image imgPhom_HighLight_ChonBai;
	public FrameImage m_framePhom_BocBai;
	public FrameImage m_framePhom_AnBai;
	public Image imgPhom_U;
	public Image imgPhom_Den;

	// TODO gif emotion
	public InputStream inputEmotion[][];

	// TODO HIGH LIGHT CARD
	public Image imgCard_HighLight;
	public Image imgCard_HighLight_1;

	//TODO effect
	public FrameImage frameFlyStar;
	
	public Image rescaleImage(Image image, int width, int height) {
		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();

		Image newImage = Image.createImage(width, height);
		Graphics g = newImage.getGraphics();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				g.setClip(x, y, 1, 1);
				int dx = x * sourceWidth / width;
				int dy = y * sourceHeight / height;
				g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
			}
		}

		return Image.createImage(newImage);
	}

	private static final String ENUM_EMPTY = "EMPTY";
	private static final String ENUM_NAM = "NAM";
	private static final String ENUM_NU = "NU";
	private static final String ENUM_GAY = "GAY";
	private static final String ENUM_LES = "LES";
	private static final String EMPTY = "Chưa xác định";
	private static final String NAM = "Nam";
	private static final String NU = "Nữ";
	private static final String GAY = "Gay";
	private static final String LES = "Lesbian";

	public static String getValue(String str) {
		if (str.equalsIgnoreCase(ENUM_EMPTY)) {
			return EMPTY;
		}

		if (str.equalsIgnoreCase(ENUM_NAM)) {
			return NAM;
		}

		if (str.equalsIgnoreCase(ENUM_NU)) {
			return NU;
		}

		if (str.equalsIgnoreCase(ENUM_GAY)) {
			return GAY;
		}

		if (str.equalsIgnoreCase(ENUM_LES)) {
			return LES;
		}

		return EMPTY;
	}
	
	public int getValueFromText(String str) {
		if (str.equalsIgnoreCase(EMPTY)) {
			return 4;
		}

		if (str.equalsIgnoreCase(ENUM_NAM)) {
			return 0;
		}

		if (str.equalsIgnoreCase(ENUM_NU)) {
			return 1;
		}

		if (str.equalsIgnoreCase(ENUM_GAY)) {
			return 2;
		}

		if (str.equalsIgnoreCase(ENUM_LES)) {
			return 3;
		}

		return 4;
	}	
	
	public static String getValue(int index) {
		if (index == 4) {
			return EMPTY;
		}

		if (index == 0) {
			return NAM;
		}

		if (index == 1) {
			return NU;
		}

		if (index == 2) {
			return GAY;
		}

		if (index == 3) {
			return LES;
		}

		return EMPTY;
	}	

	public static String getEnum(String str) {
		if (str.equalsIgnoreCase(EMPTY)) {
			return ENUM_EMPTY;
		}

		if (str.equalsIgnoreCase(NAM)) {
			return ENUM_NAM;
		}

		if (str.equalsIgnoreCase(NU)) {
			return ENUM_NU;
		}

		if (str.equalsIgnoreCase(GAY)) {
			return ENUM_GAY;
		}

		if (str.equalsIgnoreCase(LES)) {
			return ENUM_LES;
		}

		return ENUM_EMPTY;
	}

	public static String[] getArrayValues() {
		String[] str = { EMPTY, NAM, NU, GAY, LES };
		return str;
	}

	/**
	 * Reads a single line using the specified reader.
	 * 
	 * @throws java.io.IOException
	 *             if an exception occurs when reading the line
	 */
	private String readLine(InputStreamReader reader) {
		StringBuffer string = new StringBuffer("");
		try {
			int readChar = reader.read();
			if (readChar == -1) {
				return null;
			}
			while (readChar != -1 && readChar != '\n') {
				if (readChar != '\r') {
					string.append((char) readChar);
				}
				readChar = reader.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string.toString();
	}

	private CardData parseCard(String str) {
		str.trim();
		if (str.length() >= 2) {
			String strValue = str.substring(0, str.length() - 1);
			char type = str.charAt(str.length() - 1);
			CardData cardData = new CardData();
			cardData.setValue(Integer.parseInt(strValue));
			cardData.setType(type);
			return cardData;
		}

		return null;
	}

	public void parseCards(Vector listCards, String str) {
		if (str.length() >= 3) {
			str = str.substring(1, str.length() - 1);
			str = str.replace(',', ' ');
			String[] listItems = BitmapFont.splitString(str, " ");
			for (int i = 0; i < listItems.length; i++) {
				CardData cardData = parseCard(listItems[i]);
				if (cardData != null) {
					listCards.addElement(cardData);
				}
			}
		}
	}

	public Vector readCardsData() {
		InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/cardsdata.txt"));
		String line = null;
		Vector listCardData = new Vector();
		try {
			String firstLine = readLine(reader);
			if (firstLine.equalsIgnoreCase("true")) {
				while ((line = readLine(reader)) != null) {
					parseCards(listCardData, line);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listCardData;
	}

	public static String getNamePoker(int b) {
		String fullName = "";
		int cardVal = (int) ((b % 13) + 1);
		int temp = (int) b / 13;
		String cardType = "";
		switch (temp) {
		case 0:
			cardType = "bich";
			break;
		case 1:
			cardType = "chuon";
			break;
		case 2:
			cardType = "ro";
			break;
		case 3:
			cardType = "co";
			break;
		}

		fullName = cardType + cardVal;
		return fullName;
	}

	public GameResource() {
		loadEmotion();
		imgRoom = new FrameImage(ImagePack.createImage(ImagePack.ROOM_PNG), 36, 35);

		imgBackRound = ImagePack.createImage(ImagePack.BACK_PNG);
		// TODO scale background
		imgBackRound = Image.scaleImage(imgBackRound, GameCanvas.w, GameCanvas.h);

		imgMenuBar = ImagePack.createImage(ImagePack.MENUBAR_PNG);
		imgTableFourPlayer = ImagePack.createImage(ImagePack.TABLE_FOUR_PLAYER_PNG);
		imgIconCard = ImagePack.createImage(ImagePack.ICON_CARD_PNG);

		imgMakeFriend = new FrameImage(ImagePack.createImage(ImagePack.INVITE_PNG), 20, 18);

		imgCard = ImagePack.createImage(ImagePack.CARD_PNG);
		imgCardUpside = ImagePack.createImage(ImagePack.CARDUPSIDE_PNG);
		imgBackPlay = ImagePack.createImage(ImagePack.BACK_PLAY_PNG);

		imgIconMail = new FrameImage(ImagePack.createImage(ImagePack.MAIL_PNG), 18, 18);
		// imgCard2 = rescaleImage(imgCard, imgCard.getWidth() * 2 / 3,
		// imgCard.getHeight());
		// imgCardUpside2 = rescaleImage(imgCardUpside, imgCardUpside.getWidth()
		// * 2 / 3, imgCardUpside.getHeight());

		imgArrow = ImagePack.createImage(ImagePack.ARROW_PNG);
		imgWaiting = ImagePack.createImage(ImagePack.LOADING_PNG);

		frameItems = new FrameImage(ImagePack.createImage(ImagePack.ITEMS_PNG), 27, 27);
		frameStatusRoom = new FrameImage(ImagePack.createImage(ImagePack.ROOM_STATUS_PNG), 14, 16);

		imgChatNotify = new FrameImage(ImagePack.createImage(ImagePack.CHAT_PNG), 17, 18);

		// Image hieuUng = ImagePack.createImage(ImagePack.HIEUUNG_PNG);
		// imgEffectFirework[0] = Image.createImage(hieuUng, 0, 0, 7, 28, 0);
		// imgEffectFirework[1] = Image.createImage(hieuUng, 7, 0, 7, 28, 0);
		// imgEffectFirework[2] = Image.createImage(hieuUng, 14, 0, 7, 28, 0);
		// imgEffectFirework[3] = Image.createImage(hieuUng, 21, 0, 7, 28, 0);

		imgRankGame = ImagePack.createImage(ImagePack.NHATNHIBABET_PNG);
		m_frameLabai = new FrameImage(ImagePack.createImage(ImagePack.LABAI_PNG), 29, 25);
		m_frameWaiting = new FrameImage(ImagePack.createImage(ImagePack.LOADING_PNG), 100, 100);
		m_frameIconLan = new FrameImage(ImagePack.createImage(ImagePack.ICON_CLAN_PNG), 32, 32);
		frameClans = new FrameImage(ImagePack.createImage(ImagePack.CLANS_PNG), 16, 16);

		// TODO Login
		// logo casino
		imgBackRound_2 = ImagePack.createImage(ImagePack.BACK_2_PNG);
		imgLogoCasino = ImagePack.createImage(ImagePack.LOGO_PNG);
		// light 1 (chui bóng đèn)
		imgLight_1 = ImagePack.createImage(ImagePack.LIGHT_1_PNG);
		// light 2 (ánh sáng đèn)
		imgLight_2 = ImagePack.createImage(ImagePack.LIGHT_2_PNG);
		// bóng đèn
		imgBulb = ImagePack.createImage(ImagePack.BULB_PNG);

		// TODO Text field
		imgTextField_Disable = ImagePack.createImage(ImagePack.TEXTFIELD_DISABLE_PNG);
		imgTextField_Enable = ImagePack.createImage(ImagePack.TEXTFIELD_ENABLE_PNG);
		m_frameTextField_IconEdit = new FrameImage(ImagePack.createImage(ImagePack.TEXTFIELD_ICON_EDIT_PNG), 33, 33);

		// background ghi nhớ thông tint
		imgRememberInfo_Bg = ImagePack.createImage(ImagePack.REMEMBERINFO_BG_PNG);
		m_frameRememberInfo_Check = new FrameImage(ImagePack.createImage(ImagePack.REMEMBERINFO_CHECK_PNG), 42, 32);

		// TODO Menu
		imgMenuDisable = ImagePack.createImage(ImagePack.MENU_DISABLE_PNG);
		imgMenuEnable = ImagePack.createImage(ImagePack.MENU_ENABLE_PNG);
		imgMenuBg = ImagePack.createImage(ImagePack.MENU_BG_PNG);
		m_frameMenuIcon = new FrameImage(ImagePack.createImage(ImagePack.MENU_ICON_PNG), 24, 24);
		m_frameMenuListIconEnable = new FrameImage(ImagePack.createImage(ImagePack.MENU_LIST_ICON_ENABLE_PNG), 24, 24);
		m_frameMenuListIconDisable = new FrameImage(ImagePack.createImage(ImagePack.MENU_LIST_ICON_DISABLE_PNG), 24, 24);
		// NEW
		m_frameMenuBgGame = new FrameImage(ImagePack.createImage(ImagePack.MENU_BG_GAME_PNG), 64, 55);
		m_frameMenuButton = new FrameImage(ImagePack.createImage(ImagePack.MENU_BUTTON_PNG), 42, 36);
		m_frameMenuIconGame = new FrameImage(ImagePack.createImage(ImagePack.MENU_ICON_MENU_GAME_PNG), 27, 27);
		imgMenu_ContextMenu_1 = ImagePack.createImage(ImagePack.MENU_CONTEXT_MENU_1_PNG);
		imgMenu_ContextMenu_2 = ImagePack.createImage(ImagePack.MENU_CONTEXT_MENU_2_PNG);
		imgMenu_ContextMenu_3 = ImagePack.createImage(ImagePack.MENU_CONTEXT_MENU_3_PNG);
		imgMenu_ContextMenu_HighLight = ImagePack.createImage(ImagePack.MENU_CONTEXT_HIGHLIGHT_PNG);
		m_frameMenu_Context_Close = new FrameImage(ImagePack.createImage(ImagePack.MENU_CONTEXT_CLOSE_PNG), 40, 40);
		m_frameMenu_ButtonBack = new FrameImage(ImagePack.createImage(ImagePack.MENU_BUTTON_BACK_PNG), 38, 32);

		// TODO Button
		imgButton_Login = ImagePack.createImage(ImagePack.BUTTON_LOGIN);
		imgButton_HighLight = ImagePack.createImage(ImagePack.BUTTON_HIGHLIGHT_BUTTON);
		// imgHightLightText =
		// ImagePack.createImage(ImagePack.HIGHT_LIGHT_TEXT);

		// TODO Popup
		imgPoupPanel = ImagePack.createImage(ImagePack.POPUP_PANEL_PNG);
		imgPoupButton = ImagePack.createImage(ImagePack.POPUP_BUTTON_PNG);
		imgPopupLine = ImagePack.createImage(ImagePack.POPUP_LINE_PNG);
		imgPopupLine_1 = ImagePack.createImage(ImagePack.POPUP_LINE_1_PNG);
		imgPopupArrow = ImagePack.createImage(ImagePack.POPUP_ARROW_PNG);
		imgPopupArrow_Rotate = ImagePack.createImage(ImagePack.POPUP_ARROW_ROTATE_PNG);
		imgPopupPop = ImagePack.createImage(ImagePack.POPUP_POP_PNG);

		// TODO Hight Light Effect
		imgHightLight_1 = ImagePack.createImage(ImagePack.HIGHT_LIGHT_1_PNG);
		imgHightLight_2 = ImagePack.createImage(ImagePack.HIGHT_LIGHT_2_PNG);
		imgHightLight_3 = ImagePack.createImage(ImagePack.HIGHT_LIGHT_3_PNG);

		// TODO header
		imgHeaderBg = ImagePack.createImage(ImagePack.HEADER_BG_PNG);
		imgHeaderDinaBg = ImagePack.createImage(ImagePack.HEADER_DINA_BG_PNG);
		m_frameHeaderIcon_DinaGold = new FrameImage(ImagePack.createImage(ImagePack.HEADER_ICON_GOLD_PNG), 23, 23);
		m_frameHeaderMsgIcon = new FrameImage(ImagePack.createImage(ImagePack.HEADER_MSG_ICON_PNG), 44, 40);
		// NEW
		imgHeaderTop = ImagePack.createImage(ImagePack.HEADER_TOP_PNG);
		imgHeaderBottom = ImagePack.createImage(ImagePack.HEADER_BOTTOM_PNG);
		m_frameHeaderCheck = new FrameImage(ImagePack.createImage(ImagePack.HEADER_CHECK_PNG), 26, 19);

		// TODO Effect Icon
		imgEffectIcon_HightLight = ImagePack.createImage(ImagePack.EFFECT_ICON_HIGHTLIGHT_PNG);
		m_frameEffectIcon_Large = new FrameImage(ImagePack.createImage(ImagePack.EFFECT_ICON_LARGE_PNG), 136, 136);
		m_frameEffectIcon_Medium = new FrameImage(ImagePack.createImage(ImagePack.EFFECT_ICON_MEDIUM_PNG), 130, 130);
		m_frameEffectIcon_Small_1 = new FrameImage(ImagePack.createImage(ImagePack.EFFECT_ICON_SMALL_1_PNG), 93, 93);
		m_frameEffectIcon_Small_2 = new FrameImage(ImagePack.createImage(ImagePack.EFFECT_ICON_SMALL_2_PNG), 62, 62);
		m_frameEffectIcon_Text_Menu = new FrameImage(ImagePack.createImage(ImagePack.EFFECT_ICON_TEXT_MENU_PNG), 218,
				31);
		imgEffectIcon_Shadow = ImagePack.createImage(ImagePack.EFFECT_ICON_SHADOW_PNG);
		imgEffectIcon_TinNhan = ImagePack.createImage(ImagePack.EFFECT_ICON_TINNHAN_PNG);

		// TODO List screen
		imgListScr_Panel = ImagePack.createImage(ImagePack.LIST_SCREEN_PANEL);
		imgListScr_HightLight = ImagePack.createImage(ImagePack.LIST_SCREEN_HIGHT_LIGHT);
		imgListScr_ScrollBar = ImagePack.createImage(ImagePack.LIST_SCREEN_SCROLL_BAR);
		imgListScr_ScrollBar_Ngang = ImagePack.createImage(ImagePack.LIST_SCREEN_SCROLL_BAR_NGANG);

		// TODO Scroll
		imgScrollBar = ImagePack.createImage(ImagePack.SCROLL_BAR_PNG);
		m_frameScrollPanel = new FrameImage(ImagePack.createImage(ImagePack.SCROLL_PANEL_PNG), 8, 8);

		// TODO tabs
		imgTabs_HightLightRow = ImagePack.createImage(ImagePack.TABS_HIGHT_LIGHT_ROW);
		imgTabs_1_0 = ImagePack.createImage(ImagePack.TABS_1_0_PNG);
		imgTabs_2_0 = ImagePack.createImage(ImagePack.TABS_2_0_PNG);
		imgTabs_2_1 = ImagePack.createImage(ImagePack.TABS_2_1_PNG);
		imgTabs_3_0 = ImagePack.createImage(ImagePack.TABS_3_0_PNG);
		imgTabs_3_1 = ImagePack.createImage(ImagePack.TABS_3_1_PNG);
		imgTabs_3_2 = ImagePack.createImage(ImagePack.TABS_3_2_PNG);
		imgTabs_4_0 = ImagePack.createImage(ImagePack.TABS_4_0_PNG);
		imgTabs_4_1 = ImagePack.createImage(ImagePack.TABS_4_1_PNG);
		imgTabs_4_2 = ImagePack.createImage(ImagePack.TABS_4_2_PNG);
		imgTabs_4_3 = ImagePack.createImage(ImagePack.TABS_4_3_PNG);

		// TODO Avatar
		m_frameAvatar_IconBlue = new FrameImage(ImagePack.createImage(ImagePack.AVATAR_ICON_BLUE_PNG), 15, 14);
		m_frameAvatar_IconRed = new FrameImage(ImagePack.createImage(ImagePack.AVATAR_ICON_RED_PNG), 15, 14);
		m_frameAvatar_IconYellow = new FrameImage(ImagePack.createImage(ImagePack.AVATAR_ICON_YELLOW_PNG), 15, 14);
		imgAvatar_Khung = ImagePack.createImage(ImagePack.AVATAR_KHUNG_AVATAR_PNG);
		imgAvatar_Khung_High_Light = ImagePack.createImage(ImagePack.AVATAR_KHUNG_AVATAR_HIGH_LIGHT_PNG);
		m_frameAvatar_IconAvatar = new FrameImage(ImagePack.createImage(ImagePack.AVATAR_ICON_AVATAR_PNG), 64, 64);
		m_frameAvatar_IconAvatar_1 = new FrameImage(ImagePack.createImage(ImagePack.AVATAR_ICON_AVATAR_1_PNG), 40, 40);

		imgAvatar = ImagePack.createImage(ImagePack.AVATAR_ICON_AVATAR_PNG);
		
		// TODO Mail
		m_frameMail_HomThu = new FrameImage(ImagePack.createImage(ImagePack.MAIL_ICON_HOMTHU), 29, 24);

		// TODO vật phẩm
		m_frameVatPham_Icon = new FrameImage(ImagePack.createImage(ImagePack.VATPHAM_ICON_PNG), 82, 72);

		// TODO gia tộc
		m_frameGiaToc_Icon = new FrameImage(ImagePack.createImage(ImagePack.GIATOC_ICON_PNG), 100, 100);
		m_frameGiaToc_ThanhLap = new FrameImage(ImagePack.createImage(ImagePack.GIATOC_ICON_THANHLAP), 45, 42);
		imgGiaToc_HighLight = ImagePack.createImage(ImagePack.GIATOC_ICON_HIGH_LIGHT);
		imgGiaTocStar = ImagePack.createImage(ImagePack.GIATOC_STAR);

		// TODO Chat
		imgChat_Man = ImagePack.createImage(ImagePack.CHAT_MAN_PNG);
		imgChat_Panel = ImagePack.createImage(ImagePack.CHAT_PANEL_PNG);
		m_frameChat_IconGui = new FrameImage(ImagePack.createImage(ImagePack.CHAT_ICON_GUI_PNG), 32, 22);

		// TODO Room
		m_frameRoom_IconGiaToc = new FrameImage(ImagePack.createImage(ImagePack.ROOM_ICON_GIATOC_PNG), 30, 35);
		m_frameRoom_IconPhong = new FrameImage(ImagePack.createImage(ImagePack.ROOM_ICON_PHONG_PNG), 56, 60);
		m_frameRoom_IconTable = new FrameImage(ImagePack.createImage(ImagePack.ROOM_ICON_TABLE_PNG), 160, 84);
		m_frameRoom_IconUser = new FrameImage(ImagePack.createImage(ImagePack.ROOM_ICON_USER_PNG), 26, 35);
		// NEW
		m_frameRoom_IconRoom = new FrameImage(ImagePack.createImage(ImagePack.ROOM_ICON_ROOM_PNG), 33, 28);

		// TODO Tiến lên
		imgTienLen_Table = ImagePack.createImage(ImagePack.TIENLEN_TABLE_PNG);
		m_frameTienLen_PanelInfo = new FrameImage(ImagePack.createImage(ImagePack.TIENLEN_PANEL_INFO_PNG), 94, 107);
		imgTienLen_Star = ImagePack.createImage(ImagePack.TIENLEN_STAR_PNG);
		imgTienLen_Ready = ImagePack.createImage(ImagePack.TIENLEN_READY_PNG);
		imgTienLen_Chat = ImagePack.createImage(ImagePack.TIENLEN_CHAT_PNG);
		m_frameTienLen_Button = new FrameImage(ImagePack.createImage(ImagePack.TIENLEN_BUTTON_PNG), 82, 36);
		// m_frameTienLen_Start_Ready_1 = new FrameImage(
		// ImagePack.createImage(ImagePack.TIENLEN_ICON_START_READY_1_PNG),
		// 128, 122);
		// m_frameTienLen_Start_Ready_2 = new FrameImage(
		// ImagePack.createImage(ImagePack.TIENLEN_ICON_START_READY_2_PNG),
		// 94, 94);
		m_frameTienLen_Start_Ready_1 = new FrameImage(ImagePack.createImage(ImagePack.TIENLEN_ICON_START_READY_1_PNG),
				94, 38);
		m_frameTienLen_Start_Ready_2 = new FrameImage(ImagePack.createImage(ImagePack.TIENLEN_ICON_START_READY_2_PNG),
				94, 38);
		imgTienLen_TimeOutLight = ImagePack.createImage(ImagePack.TIENLEN_CARD_TIME_OUT_LIGHT_PNG);

		// NEW TIẾN LÊN
		imgTienLen_User = ImagePack.createImage(ImagePack.TIENLEN_USER_PNG);
		imgTienLen_KhungUser = ImagePack.createImage(ImagePack.TIENLEN_KHUNG_USER_PNG);
		m_frameTienLen_IconUser = new FrameImage(ImagePack.createImage(ImagePack.TIENLEN_ICON_USER_PNG), 30, 31);

		// TODO cards
		m_imgCards = new Image[52];
		for (int i = 0; i < 52; i++) {
			m_imgCards[i] = ImagePack.createImage("cards/" + getNamePoker(i));
		}
		imgTienLen_CardBack = ImagePack.createImage(ImagePack.TIENLEN_CARD_BACK_PNG);
		imgTienLen_CardBack2 = ImagePack.createImage(ImagePack.TIENLEN_CARD_BACK2_PNG);
		imgTienLen_Time_Bg = ImagePack.createImage(ImagePack.TIENLEN_CARD_TIME_BG_PNG);
		imgTienLen_Time_Line = ImagePack.createImage(ImagePack.TIENLEN_CARD_TIME_LINE_PNG);
		m_frameTienLen_Number = new FrameImage(ImagePack.createImage(ImagePack.TIENLEN_CARD_NUMBER_PNG), 17, 23);
		imgTienLen_LineButton = ImagePack.createImage(ImagePack.TIENLEN_LINE_BUTTON_PNG);
		m_frameTienLen_Title = new FrameImage(ImagePack.createImage(ImagePack.TIENLEN_TITLE_PNG), 219, 27);
		// cards moi cho tien len
		imgTienLen_CardLight = ImagePack.createImage(ImagePack.BACKLIGHT_PNG);
		imgTienLen_AllCards = ImagePack.createImage(ImagePack.TIENLEN_ALLCARDS);
		// TODO Win
		imgWin_Nhat = ImagePack.createImage(ImagePack.WIN_NHAT_PNG);
		imgWin_Nhi = ImagePack.createImage(ImagePack.WIN_NHI_PNG);
		imgWin_Ba = ImagePack.createImage(ImagePack.WIN_BA_PNG);
		imgWin_Bet = ImagePack.createImage(ImagePack.WIN_BET_PNG);
		imgWin_Thui_13_La = ImagePack.createImage(ImagePack.WIN_THUI_13_LA_PNG);
		imgWin_ChatDe = ImagePack.createImage(ImagePack.WIN_CHAT_DE_PNG);
		imgWin_ChatHeo = ImagePack.createImage(ImagePack.WIN_CHAT_HEO_PNG);
		m_frameWin_Chip = new FrameImage(ImagePack.createImage(ImagePack.WIN_CHIP_PNG), 32, 22);
		imgWin_BaTay = ImagePack.createImage(ImagePack.WIN_BATAY_PNG);
		imgWin_Xam = ImagePack.createImage(ImagePack.WIN_XAM_PNG);
		imgWin_Thang = ImagePack.createImage(ImagePack.WIN_THANG_PNG);
		imgWin_Thua = ImagePack.createImage(ImagePack.WIN_THUA_PNG);
		// NEW
		m_frameWin_Icon = new FrameImage(ImagePack.createImage(ImagePack.WIN_ICON_WIN_PNG), 75, 75);
		m_frameWin_HighLightWin = new FrameImage(ImagePack.createImage(ImagePack.WIN_HIGH_LIGHT_WIN_PNG), 280, 152);
		imgWin_To = ImagePack.createImage(ImagePack.WIN_TO_PNG);
		imgWin_ToThem = ImagePack.createImage(ImagePack.WIN_TO_THEM_PNG);
		imgWin_Theo = ImagePack.createImage(ImagePack.WIN_THEO_PNG);
		imgWin_UpBo = ImagePack.createImage(ImagePack.WIN_UP_BO_PNG);

		// TODO Bài cào
		imgBaiCao_Table = ImagePack.createImage(ImagePack.BAICAO_TABLE_PNG);
		imgBaiCao_Diem = ImagePack.createImage(ImagePack.BAICAO_DIEM_PNG);
		m_frameBaiCao_Number = new FrameImage(ImagePack.createImage(ImagePack.BAICAO_NUMBER_PNG), 20, 25);
		imgBaiCao_IconHand = ImagePack.createImage(ImagePack.BAICAO_ICON_HAND_PNG);

		// DEMO
		imgABC = ImagePack.createImage(ImagePack.DEMO);

		// TODO Xì tố
		imgXiTo_Box = ImagePack.createImage(ImagePack.XITO_BOX_PNG);
		imgXiTo_Table = ImagePack.createImage(ImagePack.XITO_TABLE_PNG);
		m_frameXiTo_Text = new FrameImage(ImagePack.createImage(ImagePack.XITO_TEXT_PNG), 248, 105);

		// TODO girl
		imgGirl = ImagePack.createImage(ImagePack.TOURGUIDE_PNG);
		// TODO phom
		imgPhom_Table = ImagePack.createImage(ImagePack.PHOM_TABLE_PNG);
		imgPhom_HighLight_ChonBai = ImagePack.createImage(ImagePack.PHOM_HIGHT_LIGHT_CHONBAI_PNG);
		m_framePhom_BocBai = new FrameImage(ImagePack.createImage(ImagePack.PHOM_BOCBAI_PNG), 60, 39);
		m_framePhom_AnBai = new FrameImage(ImagePack.createImage(ImagePack.PHOM_ANBAI_PNG), 60, 39);
		imgPhom_U = ImagePack.createImage(ImagePack.PHOM_U_PNG);
		imgPhom_Den = ImagePack.createImage(ImagePack.PHOM_DEN_PNG);

		// TODO HightLight card
		imgCard_HighLight = ImagePack.createImage(ImagePack.CARD_HIGH_LIGHT);
		imgCard_HighLight_1 = ImagePack.createImage(ImagePack.CARD_HIGH_LIGHT_1);
		
		//TODO fly star
		frameFlyStar = new FrameImage(ImagePack.createImage(ImagePack.FLY_STAR), 35, 35);
	}

	private void loadEmotion() {
		// TODO Auto-generated method stub
		inputEmotion = new InputStream[CustomGalleryImageAdapter.mImageID.length][2];
		for (int i = 0; i < CustomGalleryImageAdapter.mImageID.length; i++) 
			for(int j = 0 ; j < 2 ; j ++)
				inputEmotion[i][j] = HDCGameMidlet.instance.getResources().openRawResource(
						CustomGalleryImageAdapter.mImageID[i]);
	}

}
