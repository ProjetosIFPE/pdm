package br.edu.ifpe.tads.pdm.projeto.service;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Serializable;

import br.edu.ifpe.tads.pdm.projeto.activity.MediaPlayerActivity;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;
import br.edu.ifpe.tads.pdm.projeto.util.NotificationUtil;

/**
 * Created by Edmilson on 22/10/2016.
 */

public class PlayerService extends Service implements Serializable{

    public static final String MUSICA = "MUSICA";

    private final String TAG = getClass().getSimpleName();

    private final int BACKWARD_FORWARD = 2500;

    private int NOTIFICATION_ID = 1;

    private MediaPlayer mediaPlayer;

    private Musica musica;

    private final IBinder playerServiceBinder = new PlayerServiceBinder();

    private PlayerServiceCallbacks playerServiceCallbacks;

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

        this.musica  = (Musica) intent.getSerializableExtra(MUSICA);

        startForeground(NOTIFICATION_ID, createNotification(musica));


        return START_STICKY_COMPATIBILITY;
    }

    public Notification createNotification(Musica musica){
        Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
        return NotificationUtil.createNotification(getApplicationContext(), intent,
                "Playing", musica.getTitulo(), Boolean.FALSE);
    }

    public void initializeMediaPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(musica.getUrlMusica());
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
                playerServiceCallbacks.onStart(mediaPlayer);
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

    public void startMediaPlayer() {
        if (!isMediaPlayerPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pauseMediaPlayer() {
        if (isMediaPlayerPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stopMediaPlayer() {
        if (isMediaPlayerPlaying()) {
            mediaPlayer.stop();
        }
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

    public int getMediaPlayerDuration() {
        return mediaPlayer.getDuration();
    }

    public void onMediaPlayerSeek(int position){
        mediaPlayer.seekTo(position);
    }


    public int getMediaPlayerCurrentPosition() {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
    }

    public void setPlayerServiceCallbacks(PlayerServiceCallbacks playerServiceCallbacks) {
        this.playerServiceCallbacks = playerServiceCallbacks;
    }

    public class PlayerServiceBinder extends Binder {

        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    public interface PlayerServiceCallbacks {
        void onStart(MediaPlayer mediaPlayer);
        void onStop(MediaPlayer mediaPlayer);
    }


}
