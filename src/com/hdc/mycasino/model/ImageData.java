package com.hdc.mycasino.model;

import com.danh.standard.Image;

public class ImageData {
	public byte version;
	public byte[] data;
	public Image m_img;

	public void loadImage() {
		if (data != null) {
			m_img = Image.createImage(data, 0, data.length);
		}
	}
}
