package com.hdc.mycasino;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class IntroGame extends Activity {

	MediaController mc;
	VideoView mVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// mc = new MediaController(this);

		mVideo = (VideoView) findViewById(R.id.videoView1);
		// mVideo.setMediaController(mc);
		// mc.setAnchorView(mVideo);
		// mVideo.setMediaController(mc);
		String uriPath = "android.resource://com.hdc.mycasino/raw/hdc";
		Uri uri = Uri.parse(uriPath);
		mVideo.setVideoURI(uri);
		mVideo.requestFocus();
		mVideo.start();
		mVideo.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IntroGame.this, HDCGameMidlet.class);
				startActivity(intent);
				finish();
			}
		});

	}
}
