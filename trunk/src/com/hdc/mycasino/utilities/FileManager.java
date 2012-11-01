package com.hdc.mycasino.utilities;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.danh.standard.Image;
import com.hdc.mycasino.HDCGameMidlet;

public class FileManager {

	// save file
	public static void saveToInternalSorage(Bitmap bitmapImage, String filename) {
		ContextWrapper cw = new ContextWrapper(HDCGameMidlet.instance.getApplicationContext());
		File directory = cw.getDir("myfolder", Context.MODE_PRIVATE);
		File mypath = new File(directory, filename);

		FileOutputStream fos = null;
		try {
			if (mypath.exists())
				mypath.delete();
			fos = new FileOutputStream(mypath);
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	/*
	 * // get list file private static File[] loadInternalImages() {
	 * ContextWrapper cw = new ContextWrapper(
	 * HDCGameMidlet.instance.getApplicationContext()); File directory =
	 * cw.getDir("myfolder", Context.MODE_PRIVATE); File[] imageList =
	 * directory.listFiles(); if (imageList == null) { imageList = new File[0];
	 * } // Log.i("My", "ImageList Size = " + //
	 * Integer.toString(imageList.length)); return imageList; }
	 */
	public static Image loadImage(int index) {
		// File[] mFile = loadInternalImages();
		Bitmap m = null;
		try {
			// Log.i("begin load", Integer.toString(index));
			ContextWrapper cw = new ContextWrapper(HDCGameMidlet.instance.getApplicationContext());
			File directory = cw.getDir("myfolder", Context.MODE_PRIVATE);
			File file = new File(directory, "hd" + Integer.toString(index) + ".png");
			// m = BitmapFactory.decodeStream(new
			// FileInputStream(mFile[index]));
			if (file.exists()) {
				m = BitmapFactory.decodeStream(new FileInputStream(file));
				// Log.i("w - h",
				// Integer.toString(m.getWidth()) + "- "
				// + Integer.toString(m.getHeight()));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// Log.e("Load error", e.getMessage());
			m = null;
			e.printStackTrace();
		}
		return new Image(m);
	}

	// save file
	public static boolean fileIsExits() {
		ContextWrapper cw = new ContextWrapper(HDCGameMidlet.instance.getApplicationContext());
		File directory = cw.getDir("myfolder", Context.MODE_PRIVATE);
		File mypath = new File(directory, "hd0.png");

		FileOutputStream fos = null;
		if (!mypath.exists())
			return false;

		return true;
	}

	// tính toán version
	public static short CaculateVersion(Image img) {
		Bitmap mBitmap = img.getBitmap();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		mBitmap.compress(CompressFormat.PNG, 0, bos);
		byte[] b = bos.toByteArray();
		Log.i("length ", b.length + "");
		int version = b.length % Short.MAX_VALUE;
		return (short) version;
	}

	public static byte[] loadFile(String filename) {
		byte[] data = null;
		try {
			// FileInputStream fis = new FileInputStream(filename);
			InputStream fis = HDCGameMidlet.assets.open(filename);
			DataInputStream dis = new DataInputStream(fis);
			data = new byte[dis.available()];
			dis.read(data);
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	// check file txt exits in internal storage
	public static boolean fileTxtIsExits() {
		ContextWrapper cw = new ContextWrapper(HDCGameMidlet.instance.getApplicationContext());
		File directory = cw.getDir("mydata", Context.MODE_PRIVATE);
		File mypath = new File(directory, "hd0.txt");
		FileOutputStream fos = null;
		if (!mypath.exists())
			return false;
		return true;
	}

	// save file text
	public static void saveFileText(short id, short version, String mfile) {
		FileOutputStream fos = null;
		DataOutputStream dos;
		try {
			ContextWrapper cw = new ContextWrapper(HDCGameMidlet.instance.getApplicationContext());
			File directory = cw.getDir("mydata", Context.MODE_PRIVATE);
			File file = new File(directory, mfile);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			dos = new DataOutputStream(fos);
			dos.writeShort(id);
			dos.writeShort(version);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// load file text in internal storage
	// public static ImageData loadFileText(String name) {
	// ImageData imgData = new ImageData();
	// FileInputStream fis = null;
	// DataInputStream dis;
	// try {
	// ContextWrapper cw = new
	// ContextWrapper(HDCGameMidlet.instance.getApplicationContext());
	// File directory = cw.getDir("mydata", Context.MODE_PRIVATE);
	// File file = new File(directory, name);
	// if (file.exists()) {
	// fis = new FileInputStream(file);
	// dis = new DataInputStream(fis);
	// short id = dis.readShort();
	// imgData.m_sImgId = id;
	// short version = dis.readShort();
	// imgData.m_sImgVersion = version;
	// }
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// imgData = null;
	// } catch (IOException f1) {
	// f1.printStackTrace();
	// }
	//
	// return imgData;
	// }

	// save user and pass
	public static void saveUserAndPass(int check, String user, String pass, String mfile) {
		// Log.i("saveUserAndPass", "ok");
		FileOutputStream fos = null;
		DataOutputStream dos;
		try {
			ContextWrapper cw = new ContextWrapper(HDCGameMidlet.instance.getApplicationContext());
			File directory = cw.getDir("myuser", Context.MODE_PRIVATE);
			File file = new File(directory, mfile);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			dos = new DataOutputStream(fos);
			dos.writeUTF(Integer.toString(check));
			// Log.i("check", Integer.toString(check));
			dos.writeUTF(user);
			// Log.i("user", user);
			dos.writeUTF(pass);
			// Log.i("pass", pass);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// load user and pass
	public static String[] loadUserAndPass(String name) {
		FileInputStream fis = null;
		DataInputStream dis;
		String[] mString = { "1", "", "" };
		try {
			ContextWrapper cw = new ContextWrapper(HDCGameMidlet.instance.getApplicationContext());
			File directory = cw.getDir("myuser", Context.MODE_PRIVATE);
			// Log.i("name", name);
			File file = new File(directory, name);
			if (file.exists()) {
				// Log.i("file", "ok");
				fis = new FileInputStream(file);
				dis = new DataInputStream(fis);
				mString[0] = dis.readUTF();
				mString[1] = dis.readUTF();
				mString[2] = dis.readUTF();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException f1) {
			f1.printStackTrace();
		}

		// for (int i = 0; i < 3; i++)
		// Log.i("mString_" + Integer.toString(i), mString[i]);
		return mString;
	}

	// load file text in external storage
	public static ArrayList<String> loadfileExternalStorage(Context c,int file) {
		ArrayList<String> aa = new ArrayList<String>();
		String str = "";
		InputStream is = c.getResources().openRawResource(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			if (is != null) {
				while ((str = reader.readLine()) != null) {
					aa.add(str.trim().toString());
				}
				is.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aa;
	}

}
