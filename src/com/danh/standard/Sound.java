package com.danh.standard;

import android.app.Activity;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Sound {
	AssetManager assets;
	SoundPool soundPool;
	MediaPlayer mMediaPlayer;

	public Sound(Activity activity) {
		// activity.setVolumeControlStream(SoundManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		// this.soundPool = new SoundPool(20, SoundManager.STREAM_MUSIC, 0);
	}

	// public Music newMusic(String filename) {
	// try {
	// AssetFileDescriptor assetDescriptor = assets.openFd(filename);
	// return new AndroidMusic(assetDescriptor);
	// } catch (IOException e) {
	// throw new RuntimeException("Couldn't load music '" + filename + "'");
	// }
	// }

	// public Sound newSound(String filename) {
	// try {
	// AssetFileDescriptor assetDescriptor = assets.openFd(filename);
	// int soundId = soundPool.load(assetDescriptor, 0);
	// return new Sound(soundPool, soundId);
	// } catch (IOException e) {
	// throw new RuntimeException("Couldn't load sound '" + filename + "'");
	// }
	// }

	public void openFile(Activity mActivity, int resID) {
		// TODO Auto-generated method stub
		mMediaPlayer = MediaPlayer.create(mActivity, resID);
	}

	public void play() {
		// TODO Auto-generated method stub
		mMediaPlayer.start();
	}

	public void stop() {
		// TODO Auto-generated method stub
		mMediaPlayer.stop();
	}

	public void pause() {
		// TODO Auto-generated method stub
		mMediaPlayer.pause();
	}

	public MediaPlayer getMediaPlayer() {
		// TODO Auto-generated method stub
		return mMediaPlayer;
	}
}
