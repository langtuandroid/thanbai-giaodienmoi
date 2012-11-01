package com.hdc.mycasino.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

import com.hdc.mycasino.utilities.CRes;
import com.hdc.mycasino.utilities.FrameImage;
import com.hdc.mycasino.utilities.RecordStore;

public class DataManager {

	public static String LINK_FORUM;
	public static String SMS_CHANGE_PASS_SYNTAX;
	public static String SMS_CHANGE_PASS_NUMBER;
	private static final String AVATAR_RMS_ID = "avatarRmsId";

	public Vector m_vtSMS = null;

	private static DataManager instance = new DataManager();

	public static DataManager gI() {
		return instance;
	}

	public ImageData avatarsImageData;

	public FrameImage m_imgPartAvatar;

	public boolean isUpdatedAvatarImage(int versionImg) {
		avatarsImageData = loadAvatarImageData();
		if ((avatarsImageData == null) || (avatarsImageData.version != versionImg)) {
			return false;
		} else {
			return true;
		}
	}

	private ImageData loadAvatarImageData() {
		byte[] bData = CRes.loadRMS(AVATAR_RMS_ID);
		if (bData == null) {
			return null;
		}
		ByteArrayInputStream bi = new ByteArrayInputStream(bData);
		DataInputStream dis = new DataInputStream(bi);
		try {
			ImageData imgData = new ImageData();
			imgData.version = dis.readByte();
			int length = dis.readInt();
			imgData.data = new byte[length];
			dis.read(imgData.data);

			// dis.close();
			// bi.close();
			return imgData;
		} catch (Exception ex) {
			return null;
		}
	}

	public void saveAvatarImageData(ImageData imgData) {
		imgData.loadImage();
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bo);
		try {
			if (avatarsImageData == null) {
				avatarsImageData = new ImageData();
			}

			avatarsImageData.version = imgData.version;
			avatarsImageData.data = imgData.data;
			avatarsImageData.loadImage();
			// dos.writeByte(imgData.version);
			// dos.writeInt(imgData.data.length);
			// dos.write(imgData.data);
			// byte[] bData = bo.toByteArray();
			// CRes.saveRMS(AVATAR_RMS_ID, bData);
			// dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private byte[] loadRMS(String filename) {
	// RecordStore rec;
	// byte[] data;
	// try {
	// rec = RecordStore.openRecordStore(filename, false);
	// data = rec.getRecord(1);
	// rec.closeRecordStore();
	// } catch (Exception e) {
	// return null;
	// }
	// return data;
	// }
	//
	// private void saveRMS(String filename, byte[] data) throws Exception {
	// RecordStore rec = RecordStore.openRecordStore(filename, true);
	// if (rec.getNumRecords() > 0) {
	// rec.setRecord(1, data, 0, data.length);
	// } else {
	// rec.addRecord(data, 0, data.length);
	// }
	// rec.closeRecordStore();
	// }

	public void loadFrameImage() {
		if (avatarsImageData != null)
			m_imgPartAvatar = new FrameImage(avatarsImageData.m_img, 32, 32);
	}

}
