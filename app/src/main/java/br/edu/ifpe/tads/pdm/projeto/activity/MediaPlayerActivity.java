package br.edu.ifpe.tads.pdm.projeto.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;
import br.edu.ifpe.tads.pdm.projeto.service.PlayerService;

public class MediaPlayerActivity extends BaseActivity {

    private Intent serviceIntent;

    private PlayerService playerService;

    private Musica musica;

    public static final String MUSICA = "MUSICA";

    private TextView tituloMusica;

    private TextView duracaoMusica;

    private ImageButton buttonPlay;

    private SeekBar seekBar;

    private final Handler seekBarHandler = new Handler();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        play();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        initMediaPlayerControl();
        play();
    }

    /**
     * Inicializa os objetos da View
     */
    private void initMediaPlayerControl(){
        tituloMusica = (TextView) findViewById(R.id.titulo_musica);
        duracaoMusica = (TextView) findViewById(R.id.duracao_musica);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        ImageButton buttonRew = (ImageButton) findViewById(R.id.media_rew);
        buttonRew.setOnClickListener(onPlayerRewind());
        buttonPlay = (ImageButton) findViewById(R.id.media_play);
        buttonPlay.setOnClickListener(onPlayerStart());
        ImageButton buttonForward = (ImageButton) findViewById(R.id.media_ff);
        buttonForward.setOnClickListener(onPlayerForward());
    }

    public void play() {
        Musica novaMusica = (Musica) getIntent().getSerializableExtra(MUSICA);
        if ( musica == null || ( musica!= null && musica.equals(novaMusica)) ) {
            musica = novaMusica;
            tituloMusica.setText(musica.getTitulo());
            serviceIntent = new Intent(getContext(), PlayerService.class);
            serviceIntent.putExtra(PlayerService.MUSICA, musica);
            startService(serviceIntent);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
           changeButtonPlayImageResource();
        }


    }

    public View.OnClickListener onPlayerStart() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton buttonPlay = (ImageButton) v;
                if (playerService.isMediaPlayerPlaying()) {
                    playerService.pauseMediaPlayer();
                } else {
                    playerService.startMediaPlayer();
                }
                changeButtonPlayImageResource();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        stopService(serviceIntent);
    }

    public View.OnClickListener onPlayerRewind() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (playerService.isMediaPlayerPlaying()) {
                    playerService.mediaPlayerBackward();
                }
             }
        };
    }

    public View.OnClickListener onPlayerForward() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerService.isMediaPlayerPlaying()) {
                    playerService.mediaPlayerForward();
                }
            }
        };
    }

    public PlayerService.PlayerServiceCallbacks createCallbackOnMediaPlayerStart() {
       return new PlayerService.PlayerServiceCallbacks() {
           @Override
           public void onStart(MediaPlayer mediaPlayer) {

           }

           @Override
           public void onStop(MediaPlayer mediaPlayer) {
               changeButtonPlayImageResource();
           }
       };
    }

    public void changeButtonPlayImageResource() {
        if (playerService.isMediaPlayerPlaying()) {
            buttonPlay.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            buttonPlay.setImageResource(android.R.drawable.ic_media_play);
        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection()  {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playerService = ((PlayerService.PlayerServiceBinder)service).getService();
            playerService.setPlayerServiceCallbacks(createCallbackOnMediaPlayerStart());
            playerService.initializeMediaPlayer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playerService = null;
        }
    };
}
