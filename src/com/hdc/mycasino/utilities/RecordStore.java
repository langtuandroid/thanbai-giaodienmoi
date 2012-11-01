package com.hdc.mycasino.utilities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.hdc.mycasino.HDCGameMidlet;

public class RecordStore {
	private static String TAG = "RecordStore";

	public static SharedPreferences settings1;
	public static SharedPreferences settings2;

	static {
		try {
			// Log.e(TAG, "tao sharedRreferences");
			settings1 = PreferenceManager.getDefaultSharedPreferences(HDCGameMidlet.instance
					.getApplicationContext());
			settings2 = HDCGameMidlet.instance.getSharedPreferences("Save_User_Password",
					Activity.MODE_PRIVATE);
		} catch (Exception e) {
			// Log.e(TAG, "error sharedRreferences");
			e.printStackTrace();
		}
	}

	public static int getNumRecords(String filename) {
		String tmp = settings1.getString(filename, null);
		// Log.e(TAG, "tmp="+tmp);
		if (tmp != null) {
			return 1;
		}
		return 0;
	}

	public static void setRecord(String filename, byte[] data) {
		Editor et = settings1.edit();
		et.remove(filename);
		et.commit();
		et.putString(filename, new String(data).trim());
		et.commit();
	}

	public static void addRecord(String filename, byte[] data) {
		Editor et = settings1.edit();
		et.putString(filename, new String(data).trim());
		et.commit();
		// Log.e(TAG,
		// "AddRecord filename="+filename+" -- data="+settings1.getString(filename,
		// null));
	}

	public static void addRecordInt(String filename, int data) {
		Editor et = settings1.edit();
		et.putInt(filename, data);
		et.commit();
		// Log.e(TAG,
		// "addRecordInt filename="+filename+" -- data="+settings1.getInt(filename,
		// -1));
	}

	public static void addRecordStringSave(String filename, String data) {
		Editor et = settings2.edit();
		et.putString(filename, data);
		et.commit();
		// Log.e(TAG,
		// "addRecordStringSave filename="+filename+" -- data="+settings2.getString(filename,
		// null));
	}

	public static void deleteRecordStoreSave(String filename) {
		Editor et = settings2.edit();
		et.remove(filename);
		et.commit();
	}

	public static String getRecordSave(String filename) {
		// Log.e(TAG,
		// "getRecordSave filename="+filename+" -- data="+settings2.getString(filename,
		// null));
		return settings2.getString(filename, null);
	}

	public static int getRecordIntSave(String filename) {
		// Log.e(TAG,
		// "getRecordIntSave filename="+filename+" -- data="+settings2.getInt(filename,
		// -1));
		return settings2.getInt(filename, 0);
	}

	public static void addRecordIntSave(String filename, int data) {
		Editor et = settings2.edit();
		et.putInt(filename, data);
		et.commit();
		// Log.e(TAG,
		// "addRecordIntSave filename="+filename+" -- data="+settings2.getInt(filename,
		// -1));
	}

	public static void addRecordString(String filename, String data) {
		Editor et = settings1.edit();
		et.putString(filename, data);
		et.commit();
		// Log.e(TAG,
		// "addRecordString filename="+filename+" -- data="+settings1.getString(filename,
		// null));
	}

	public static void deleteRecordStore(String filename) {
		Editor et = settings1.edit();
		et.remove(filename);
		et.commit();
	}

	public static byte[] getRecord(String filename) {
		// Log.e(TAG,
		// "getRecord filename="+filename+" -- data="+settings1.getString(filename,
		// null));
		return settings1.getString(filename, null).getBytes();
	}

	public static int getRecordInt(String filename) {
		// Log.e(TAG,
		// "getRecordInt filename="+filename+" -- data="+settings1.getInt(filename,
		// -1));
		return settings1.getInt(filename, 0);
	}

	public static void removeSettings1() {
		Editor et = settings1.edit();
		et.clear();
		et.commit();
	}

	public static void removeSettings2() {
		Editor et = settings2.edit();
		et.clear();
		et.commit();
	}

	public static void removeAll() {
		removeSettings1();
		removeSettings2();
	}
}
