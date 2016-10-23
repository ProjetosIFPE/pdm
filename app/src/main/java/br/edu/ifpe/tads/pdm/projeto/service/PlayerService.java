package br.edu.ifpe.tads.pdm.projeto.service;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.MediaPlayerActivity;
import br.edu.ifpe.tads.pdm.projeto.util.NotificationUtil;

/**
 * Created by Edmilson on 22/10/2016.
 */

public class PlayerService extends Service {

    public static final String ACTION_PLAY = "br.edu.ifpe.tads.pdm.projeto.action.PLAY";

    private static final String URL_MUSIC = "urlMusic";

    private final String TAG = getClass().getSimpleName();

    private final int BACKWARD_FORWARD = 2500;

    private int NOTIFICATION_ID = 1;

    private MediaPlayer mediaPlayer;

    private final IBinder playerServiceBinder = new PlayerServiceBinder();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return playerServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String urlMusic = intent.getStringExtra(URL_MUSIC);
        initializeMediaPlayer(urlMusic);
        startForeground(NOTIFICATION_ID, createNotification());

        return START_STICKY_COMPATIBILITY;
    }

    public Notification createNotification(){
        Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
        return NotificationUtil.createNotification(getApplicationContext(), intent,
                "Soundtrack", "Tocando música", Boolean.FALSE);
    }

    private void initializeMediaPlayer(String urlMusic) {
        try {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
          //  mediaPlayer.setDataSource(urlMusic);
            mediaPlayer.setOnPreparedListener(onPlayerPreparedListener());
            mediaPlayer.setOnErrorListener(onPlayerErrorListener());
            mediaPlayer.setOnCompletionListener(onPlayerCompletionListener());
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    public MediaPlayer.OnErrorListener onPlayerErrorListener() {
        return new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mediaPlayer.reset();
                return false;
            }
        };
    }

    public MediaPlayer.OnPreparedListener onPlayerPreparedListener() {
        return new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        };
    }

    public MediaPlayer.OnCompletionListener onPlayerCompletionListener() {
        return new  MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopSelf();
            }
        };
    }

    public boolean isMediaPlayerPlaying() {
        Boolean playing = Boolean.FALSE;
        if (mediaPlayer != null) {
            playing = mediaPlayer.isPlaying();
        }
        return playing;
    }

    public void mediaPlayerBackward() {
        if (this.isMediaPlayerPlaying()) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - BACKWARD_FORWARD);
        }
    }

    public void mediaPlayerForward() {
        if (this.isMediaPlayerPlaying()) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + BACKWARD_FORWARD);
        }
    }



    public class PlayerServiceBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }
}
