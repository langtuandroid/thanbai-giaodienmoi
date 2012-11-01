package com.hdc.mycasino.model;

import com.danh.standard.Graphics;
import com.hdc.mycasino.utilities.FrameImage;

public abstract class MyObj {
	public String itemName = "";
	public int itemId;

	public abstract void paintIcon(Graphics g, int x, int y);

	public abstract void paintInRow(Graphics g, int x, int y, int width, int height);

	public abstract void paintInfo(Graphics g, int x, int y);

	public abstract void paintInfo_Item(Graphics g, int x, int y, int width, int height,
			MyObj myObj, int type, int m_widthItem);

	public abstract void focusItem();

	public abstract void paintItem(Graphics g, float x, float y, int m_IdFrame, int select,
			FrameImage m_frame);

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MyObj other = (MyObj) obj;
		if (this.itemId != other.itemId) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int hash = 7;
		return hash;
	}
}
