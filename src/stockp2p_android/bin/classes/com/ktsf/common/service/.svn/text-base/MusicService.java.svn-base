package com.pactera.nci.common.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.pactera.nci.R;

public class MusicService extends Service {

	private static final String TAG = "MyService";
	private MediaPlayer mediaPlayer;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.v(TAG, "onCreate");
		if (mediaPlayer == null) {
//			mediaPlayer = MediaPlayer.create(this, R.raw.background);
//			mediaPlayer.setLooping(true);
//			mediaPlayer.start();
		}
	}

	@Override
	public void onDestroy() {
		Log.v(TAG, "onDestroy");
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
	}
	
	

	@Override
	public void onStart(Intent intent, int startId) {
		Log.v(TAG, "onStart");
//		if (intent != null) {
//			Bundle bundle = intent.getExtras();
//			if (bundle != null) {
//				int op = bundle.getInt("op");
//				switch (op) {
//				case 1:
//					play();
//					System.out.println("播放");
//					break;
//				case 2:
//					stop();
//					break;
//				case 3:
//					pause();
//					break;
//				}
//			}
//		}
	}

	public void play() {
		if (!mediaPlayer.isPlaying()) {
			mediaPlayer.start();
		}
	}

	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			try {
				mediaPlayer.prepare();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}