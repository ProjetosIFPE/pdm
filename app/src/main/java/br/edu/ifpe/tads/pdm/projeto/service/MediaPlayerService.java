package br.edu.ifpe.tads.pdm.projeto.service;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.MainActivity;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;
import br.edu.ifpe.tads.pdm.projeto.util.IOUtil;
import br.edu.ifpe.tads.pdm.projeto.util.NotificationUtil;

/**
 * Created by Edmilson on 22/10/2016.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_PLAY = "br.edu.ifpe.tads.pdm.projeto.ACTION_PLAY";
    public static final String ACTION_PAUSE = "br.edu.ifpe.tads.pdm.projeto.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "br.edu.ifpe.tads.pdm.projeto.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "br.edu.ifpe.tads.pdm.projeto.ACTION_NEXT";
    public static final String ACTION_STOP = "br.edu.ifpe.tads.pdm.projeto.ACTION_STOP";
    public static final String BROADCAST_PLAY_NEW_AUDIO = "br.edu.ifpe.tads.pdm.projeto.BROADCAST_PLAY_NEW_AUDIO";
    private static int NOTIFICATION_ID = 101;
    private final String TAG = getClass().getSimpleName();
    private final IBinder iBinder = new LocalBinder();
    public static final String AUDIO_INDEX = "AUDIO_INDEX";
    public static final String AUDIO_LIST = "AUDIO_LIST";
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;
    private MediaPlayer mediaPlayer;
    private int resumePosition;
    private AudioManager audioManager;
    private List<Musica> playlist;
    private int audioIndex = -1;
    private Musica musicaAtiva;
    private Boolean ongoingCall = Boolean.FALSE;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    private BroadcastReceiver audioBecomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseMedia();
            buildNotification(MediaPlayerStatus.PAUSED);
        }
    };
    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            audioIndex = IOUtil.getInt(getApplicationContext(), AUDIO_INDEX);
            if (audioIndex != -1 && audioIndex < playlist.size()) {
                musicaAtiva = playlist.get(audioIndex);
            } else {
                stopSelf();
            }
            stopMedia();
            resetMedia();
            initMediaPlayer();
            updateMetadata();
            buildNotification(MediaPlayerStatus.PLAYING);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        callStateListener();

        registerBecomingNoisyReceiver();

        registerPlayNewAudio();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        this.playlist = IOUtil.getList(Musica[].class, getApplicationContext(), AUDIO_LIST);
        audioIndex = IOUtil.getInt(getApplicationContext(), AUDIO_INDEX);

        if (audioIndex != -1 && audioIndex < playlist.size()) {
            musicaAtiva = playlist.get(audioIndex);
        } else {
            stopSelf();
        }


        if (requestAudioFocus() == false) {
            stopSelf();
        }

        if (mediaSessionManager == null) {
            initMediaSession();
            initMediaPlayer();
            buildNotification(MediaPlayerStatus.PLAYING);
        }

        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);

        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(musicaAtiva.getUrlMusica());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            stopSelf();
        }
    }

    private void initMediaSession() {
        if (mediaSessionManager != null) {
            return;
        }

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        transportControls = mediaSession.getController().getTransportControls();

        mediaSession.setActive(Boolean.TRUE);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        updateMetadata();

        mediaSession.setCallback(createMediaSessionCallback());
    }

    public MediaSessionCompat.Callback createMediaSessionCallback() {
        return new MediaSessionCompat.Callback() {

            @Override
            public void onPlay() {
                super.onPlay();
                resumeMedia();
                buildNotification(MediaPlayerStatus.PLAYING);
            }

            @Override
            public void onPause() {
                super.onPause();
                pauseMedia();
                buildNotification(MediaPlayerStatus.PAUSED);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                skipToPrevious();
                updateMetadata();
                buildNotification(MediaPlayerStatus.PLAYING);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                skipToNext();
                updateMetadata();
                buildNotification(MediaPlayerStatus.PLAYING);
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                stopSelf();
            }


        };
    }

    private void updateMetadata() {
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, musicaAtiva.getTitulo())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, musicaAtiva.getNomesArtistas())
                .build());
    }

    private void skipToNext() {
        if (audioIndex == playlist.size() - 1) {
            audioIndex = 0;
            musicaAtiva = playlist.get(audioIndex);
        } else {
            musicaAtiva = playlist.get(++audioIndex);
        }

        IOUtil.putInt(getApplicationContext(), AUDIO_INDEX, audioIndex);

        stopMedia();
        resetMedia();
        initMediaPlayer();
    }

    private void skipToPrevious() {
        if (audioIndex == 0) {
            audioIndex = playlist.size() - 1;
            musicaAtiva = playlist.get(audioIndex);
        } else {
            musicaAtiva = playlist.get(--audioIndex);
        }

        IOUtil.putInt(getApplicationContext(), AUDIO_INDEX, audioIndex);

        stopMedia();
        resetMedia();
        initMediaPlayer();
    }

    private void playMedia() {
        if (!isMediaPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopMedia() {
        if (isMediaPlaying()) {
            mediaPlayer.start();
        }
    }

    private void resetMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
    }

    private void pauseMedia() {
        if (isMediaPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }

    private void resumeMedia() {
        if (!isMediaPlaying()) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
        }
    }

    private boolean isMediaPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mediaPlayer == null) {
                    initMediaPlayer();
                }
                playMedia();
                increaseVolume();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                stopMedia();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pauseMedia();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                decreaseVolume();
                break;
        }
    }

    private Boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        Boolean requestGranted = Boolean.FALSE;
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            requestGranted = Boolean.TRUE;
        }
        return requestGranted;
    }

    private Boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    public void decreaseVolume() {
        if (isMediaPlaying()) {
            mediaPlayer.setVolume(0.1f, 0.1f);
        }
    }

    public void increaseVolume() {
        if (isMediaPlaying()) {
            mediaPlayer.setVolume(1.0f, 1.0f);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.i(TAG, "onBufferingUpdate() " + percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopMedia();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }
        removeAudioFocus();
        disablePhoneStateListener();
        removeNotification();
        unregisterReceiver(audioBecomingNoisyReceiver);
        unregisterReceiver(playNewAudio);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d(TAG, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d(TAG, "MEDIA_ERROR_SERVER_DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d(TAG, "MEDIA_ERROR_UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.i(TAG, "onSeekComplete()");
    }

    private void registerPlayNewAudio() {
        IntentFilter filter = new IntentFilter(BROADCAST_PLAY_NEW_AUDIO);
        registerReceiver(playNewAudio, filter);
    }

    private void registerBecomingNoisyReceiver() {
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(audioBecomingNoisyReceiver, intentFilter);
    }

    private void disablePhoneStateListener() {
        if (telephonyManager != null && phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    private void callStateListener() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        phoneStateListener = createPhoneStateListener();

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    private PhoneStateListener createPhoneStateListener() {
        return new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            pauseMedia();
                            ongoingCall = Boolean.TRUE;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (mediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };
    }

    private void buildNotification(MediaPlayerStatus mediaPlayerStatus) {
        int notificationAction = android.R.drawable.ic_media_pause;

        PendingIntent playPauseAction = null;

        if (mediaPlayerStatus.equals(MediaPlayerStatus.PLAYING)) {
            notificationAction = android.R.drawable.ic_media_pause;
            playPauseAction = playbackAction(1);
        } else if (mediaPlayerStatus.equals(MediaPlayerStatus.PAUSED)) {
            notificationAction = android.R.drawable.ic_media_play;
            playPauseAction = playbackAction(0);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        notificationBuilder.setShowWhen(Boolean.FALSE)
                .setStyle(new NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2))
                .setColor(getResources().getColor(R.color.primary))
                .setContentText(musicaAtiva.getNomesArtistas())
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(musicaAtiva.getTitulo())
                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", playPauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(NOTIFICATION_ID, notificationBuilder.build());


    }

    private void removeNotification() {
        NotificationUtil.removeNotification(getApplicationContext(), NOTIFICATION_ID);
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playBackAction = new Intent(this, MediaPlayerService.class);
        PendingIntent pendingIntent = null;
        switch (actionNumber) {
            case 0:
                playBackAction.setAction(ACTION_PLAY);
                pendingIntent = PendingIntent.getService(this, actionNumber, playBackAction, 0);
                break;
            case 1:
                playBackAction.setAction(ACTION_PAUSE);
                pendingIntent = PendingIntent.getService(this, actionNumber, playBackAction, 0);
                break;
            case 2:
                playBackAction.setAction(ACTION_NEXT);
                pendingIntent = PendingIntent.getService(this, actionNumber, playBackAction, 0);
                break;
            case 3:
                playBackAction.setAction(ACTION_PREVIOUS);
                pendingIntent = PendingIntent.getService(this, actionNumber, playBackAction, 0);
                break;
            default:
                break;
        }
        return pendingIntent;
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction != null && playbackAction.getAction() != null) {
            String actionString = playbackAction.getAction();
            if (ACTION_PLAY.equalsIgnoreCase(actionString)) {
                transportControls.play();
            } else if (ACTION_PAUSE.equalsIgnoreCase(actionString)) {
                transportControls.pause();
            } else if (ACTION_NEXT.equalsIgnoreCase(actionString)) {
                transportControls.skipToNext();
            } else if (ACTION_PREVIOUS.equalsIgnoreCase(actionString)) {
                transportControls.skipToPrevious();
            } else if (ACTION_STOP.equalsIgnoreCase(actionString)) {
                transportControls.stop();
            }
        }
    }

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

}
