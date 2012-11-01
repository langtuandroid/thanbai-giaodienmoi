package com.hdc.mycasino.model;

import com.danh.standard.Graphics;
import com.danh.standard.Image;
import com.danh.standard.Sprite;
import com.hdc.mycasino.GameCanvas;
import com.hdc.mycasino.HDCGameMidlet;
import com.hdc.mycasino.R;
import com.hdc.mycasino.screen.MainScr;
import com.hdc.mycasino.utilities.GameResource;

//điểm trên ellipse
public class Point {
	// cạnh a trong hình ellipse
	float radiusX;
	// cạnh b trong hình ellipse
	float radiusY;
	// góc Alpha ban đầu
	public float mAlpha;
	// góc cho di chuyển
	float mSpeed;

	public Location mCenter;
	public Location mPosition_XY;
	public float m_Scale;
	// icon index
	public int mIcon_Index;
	// focus
	public boolean isFocus;

	public float getScale() {
		return m_Scale;
	}

	public void setScale(float m_Scale) {
		this.m_Scale = m_Scale;
	}

	public int getIndex() {
		return mIcon_Index;
	}

	public void setIndex(int mIcon_Index) {
		switch (mIcon_Index) {
		case 0: // chơi game
			this.mIcon_Index = 6;
			break;
		case 1: // gia tộc
			this.mIcon_Index = 1;
			break;
		case 2: // cá nhân
			this.mIcon_Index = 3;
			break;
		case 3: // cửa hàng
			this.mIcon_Index = 4;
			break;
		case 4: // bạn bè
			this.mIcon_Index = 2;
			break;
		case 5: // tin nhắn
			this.mIcon_Index = 0;
			break;
		case 6: // xếp hạng
			this.mIcon_Index = 5;
			break;
		case 7: // tiến lên miền Nam
			this.mIcon_Index = 8;
			break;
		case 8: // tiến lên miền Bắc
			this.mIcon_Index = 7;
			break;
		case 9: // xì tố
			this.mIcon_Index = 9;
			break;
		case 10: // bài cào
			this.mIcon_Index = 11;
			break;
		case 11: // phỏm
			this.mIcon_Index = 10;
			break;

		default:
			break;
		}

	}

	public float getSumDegree() {
		return mAlpha + mSpeed;
	}

	public Command getCommand() {
		return mCommand;
	}

	public void setCommand(Command mCommand) {
		this.mCommand = mCommand;
	}

	// TODO paint
	@SuppressWarnings("static-access")
	public void paint(Graphics g) {
		// paint icon
		GameResource.instance.m_frameEffectIcon_Large.drawFrame(this.getIndex(),
				this.mPosition_XY.getX(), this.mPosition_XY.getY(), Sprite.TRANS_NONE,
				Graphics.VCENTER | Graphics.HCENTER, this.m_Scale, g);

		// paint hình màu tròn màu đen
		// đã dc opacity
		g.drawImageOpacity(
				new Image(g.BitmapResize(GameResource.instance.imgEffectIcon_Shadow.getBitmap(),
						GameResource.instance.imgEffectIcon_Shadow.getBitmap().getWidth()
								* this.m_Scale, GameResource.instance.imgEffectIcon_Shadow
								.getBitmap().getHeight() * this.m_Scale)),
				this.mPosition_XY.getX(), this.mPosition_XY.getY(), Graphics.VCENTER
						| Graphics.HCENTER, (int) (255 * (1 - this.m_Scale)));
	}

	// TODO paint hight_light and text menu
	public void paintHightLight_Text(Graphics g, float degree) {
		// paint hight light
		g.drawImageDegree(GameResource.instance.imgEffectIcon_HightLight, this.mPosition_XY.getX(),
				this.mPosition_XY.getY(), degree, Graphics.HCENTER | Graphics.VCENTER);

		// paint text menu
		GameResource.instance.m_frameEffectIcon_Text_Menu.drawFrame(this.getCommand().index,
				this.mPosition_XY.getX(), this.mPosition_XY.getY()
						+ GameResource.instance.m_frameEffectIcon_Large.frameWidth / 4 * 3,
				Sprite.TRANS_NONE, Graphics.HCENTER | Graphics.VCENTER, g);
	}

	// TODO update
	public void update() {
		// if (getSumDegree() == (float) (Math.PI / 2))
		// isFocus = true;
	}

	// TODO update key
	public boolean updateKey() {
		// nếu focus là true
		// thì action command
		if (GameCanvas.isPointer(mPosition_XY.getX()
				- GameResource.instance.m_frameEffectIcon_Large.frameWidth * m_Scale / 2,
				mPosition_XY.getY() - GameResource.instance.m_frameEffectIcon_Large.frameWidth
						* m_Scale / 2, GameResource.instance.m_frameEffectIcon_Large.frameWidth
						* m_Scale, GameResource.instance.m_frameEffectIcon_Large.frameWidth
						* m_Scale)) {
			if (isFocus) {
				if(GameCanvas.isPointerClick){
				HDCGameMidlet.sound.openFile(HDCGameMidlet.instance, R.raw.click);
				HDCGameMidlet.sound.play();

				mCommand.action.perform();}
				// HDCGameMidlet.instance.Toast(mCommand.caption);
				return true;
			} else {
				// nếu focus là false;
				// thì chuẩn bị tranlate item trên ellipse
				if (GameCanvas.isPointerClick) {
					MainScr.gI().isTranlate = true;
					MainScr.gI().idx_IconTranlate = mIcon_Index;
					MainScr.gI().resetFocus();

					if (mPosition_XY.getX() > mCenter.getX()) {
						MainScr.gI().isLeft = false;
					} else {
						MainScr.gI().isLeft = true;
					}
				}

			}
		}
		return false;
	}

	// command cho point trên ellipse
	public Command mCommand;

	public void setInfo(float x, float y, float width, float height, float m_Alpha) {
		// tọa độ tâm của ellipse
		mCenter = new Location();
		mCenter.setX(x + width / 2);
		mCenter.setY(y + height / 2);

		// set focus
		isFocus = false;

		// radius X
		radiusX = width / 2;
		// radisu Y
		radiusY = height / 2;

		// góc ban đầu của item
		this.mAlpha = m_Alpha;
		// góc dịch chuyển
		this.mSpeed = 0.0f;

		// tọa độ item
		mPosition_XY = new Location();
		Caculate_Location();
		m_Scale = Caculate_Scale();
	}

	// TODO cài đặt góc thay đổi
	public void setSpeed(float m_Speed) {
		this.mSpeed = m_Speed;
	}

	// TODO tính toán tọa độ item

	public void Caculate_Location() {
		mPosition_XY.setX((float) (mCenter.getX() + Math.cos(this.mAlpha + this.mSpeed)
				* this.radiusX));
		mPosition_XY.setY((float) (mCenter.getY() + Math.sin(this.mAlpha + this.mSpeed)
				* this.radiusY));
	}

	// TODO tính toán độ scale
	public float Caculate_Scale() {
		return (mPosition_XY.getY() / (radiusY + mCenter.getY()));
	}

	public class Location {
		float x;

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}

		float y;
	}
}
